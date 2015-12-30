package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang.xwork.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DataSynStatus;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.archives.TSynProject;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.util.filestore.FileStoreLocation;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FailFileIDXMLUtils;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.StoreFileReceiveID;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.WSClient;

public class DataSynTask extends TaskBase implements Job{

	private IarchivesService archivesService;
	private TUser user = null;
	private Logger logger = null;

	public DataSynTask(String taskName, ServletContext context, long period) {
		super(taskName,context, period);
		archivesService = (IarchivesService) Component.getInstance("archivesService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = archivesService.getLogManager(user).getLogger(
				DataSynTask.class);
	}
	
	public DataSynTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		archivesService = (IarchivesService) Component.getInstance("archivesService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = archivesService.getLogManager(user).getLogger(
				DataSynTask.class);
	}
	
	public void execute(JobExecutionContext context)
		throws JobExecutionException {
		String taskName = context.getTrigger().getKey().getName();
		logger.info("任务[ " + taskName + " ]启动于 "
					+ DateToString(context.getFireTime(), "yyyy-MM-dd HH:mm:ss"));
		try {
			doTask();
		} catch(Exception e){
			throw new JobExecutionException(e);
		}finally{
			logger.info("任务[ "+ taskName+ " ]下次将于"
					+ DateToString(context.getNextFireTime(),"yyyy-MM-dd HH:mm:ss") + " 启动");
		}		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void doTask() throws Exception {
		Configurater config = Configurater.getInstance();
		String taskDir = config.getConfigValue("mytask");
		String taskPath = XmlUtils.getPath() + taskDir;
		File taskFile = new File(taskPath);
		String systemType = config.getConfigValue("system_type");
		File[] list = taskFile.listFiles();
		String unit = "";
		for (File file : list) {
			if (file.exists() && file.getName().endsWith(".xml")) {
				File tempFile=new File(file.getAbsolutePath()+".lck");
				//转存
				file.renameTo(tempFile);
				file=tempFile;
				
				XmlUtils util = XmlUtils.getInstance(taskDir + "/" + file.getName());
				Document document = util.getDocument();
				
				//区域不用保存工程信息
				if(!"2".equals(systemType)){
					//处理工程
					saveProject(document);
					saveSubProject(document);
				}
				
				//分工程处理
				List<Element> projects = document.selectNodes("//project");
				
				for(Element project : projects){
					String mainProjectId = project.attributeValue("id");//工程ID
					List<Element> mainProjectFiles = document.selectNodes("//project[@id='"+mainProjectId+"']/file");
					
					List<Element> subProjectFiles = document.selectNodes("//project[@id='"+mainProjectId+"']/sub_project/file");
					List<Element> allFiles = new ArrayList<Element>();
					allFiles.addAll(mainProjectFiles);
					allFiles.addAll(subProjectFiles);
					
					// 获取文件下载 地址
					unit = project.attributeValue("from_unit_id");
					//检查FTP是否可用
					String ftpInfo[] = FtpXmlUtils.getFTPInfo(unit);
					ContinueFTP ftp = ContinueFTP.getDownLoadInstance(ftpInfo[0], Integer.parseInt(ftpInfo[1]), 
							ftpInfo[2], ftpInfo[3]);
					if(ftp == null){
						logger.warn(unit+"FTP连接失败,任务即将退出！");
						return;
					}
					String location = project.attributeValue("location");
					for (Element element : allFiles) {
						/************************************ 获取文档的信息、工程信息、用户的单位 信息 **********************/
						String pid = element.getParent().attributeValue("id"); // 工程id
						String pname = element.getParent().attributeValue("project");// 工程名称
						String ptype = element.getParent().attributeValue("protype");// 工程类型
						String pstatus = element.getParent().attributeValue("pharase");// 工程状态
						String owerUnitUnitId = element.getParent().attributeValue("owerUnitUnitId");// 业主单位
						String designerUnitId = element.getParent().attributeValue("designerUnitId");// 初设单位
						String path = element.attributeValue("path");// 文件路径
						String fid = element.attributeValue("id");// 文件id
						String previewPath = element.attributeValue("previewpath");// 预览路径
						String type = element.attributeValue("operate");// 操作类型
						String name = element.attributeValue("title");// 文件名称
						String format = element.attributeValue("format");// 文件格式
						String doctypeCode = element.attributeValue("doctypeCode");// 文档分类
						String suffix = element.attributeValue("suffix");// 文件的后缀名
						String version = element.attributeValue("version");// 版本
						String updateTime = element.attributeValue("update_time");// 更新时间
						String  storeLocation = element.attributeValue("storeLocation");
						TDoc doc = new TDoc();// 文档信息表
						String oldPath = "";
						String newPath = "";
						// 路径不为空时
						if (path != null) {
							//文件迁移时，有两上path,且以=>分隔
							String[] pathArray = path.split("=>");
							if (pathArray.length ==2) {
								oldPath = pathArray[0];
								newPath = pathArray[1];
							} else {
								newPath = path;
							}
							
							doc.setPath(newPath);
							doc.setFormat(format);
							doc.setId(fid);
							doc.setName(name);
							doc.setStoreLocation(storeLocation);
							doc.setPreviewPath(previewPath);
							
							if (updateTime != null) {
								SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Timestamp ts = new Timestamp(timeFormat.parse(updateTime).getTime());
								doc.setCreateDate(ts);
							}
							if (version != null) {
								doc.setVersion(version);
							}
							if (suffix != null) {
								doc.setSuffix(suffix);
							}
						}
						
						// 获得创建者
						TUser user = new TUser();
						user.setName(element.attributeValue("author"));
						doc.setTUser(user);
						
						
						// 文档分类
						TDocType doctype = new TDocType();
						doctype.setCode(doctypeCode);
						doctype.setName(element.attributeValue("doctypeCodeName"));
						doc.setTDocType(doctype);
						
						
						
						// 工程信息
						TProject pro = new TProject();
						pro.setId(Long.valueOf(pid));
						pro.setName(pname);
						pro.setType(ptype);
						pro.setStatus(pstatus);
						
						
						// 业主单位
						TUnit owerUnit = new TUnit();
						owerUnit.setCode(owerUnitUnitId);
						owerUnit.setName(project.attributeValue("owerUnitName"));
						pro.setTUnitByOwnerUnitId(owerUnit);
						
						// 初设单位
						TUnit designerUnit = new TUnit();
						designerUnit.setCode(designerUnitId);
						designerUnit.setName(project.attributeValue("designerName"));
						pro.setTUnitByDesignUnitId(designerUnit);
						
						// 获取索引保存目录
						String indexStore = config.getConfigValue("unitCode");
						TSynDoc synDoc = null;
						
						if(!"2".equals(systemType)){
							synDoc = (TSynDoc) archivesService.EntityQuery(TSynDoc.class,fid);
							//该文件区域先同步，已经把存储位置发过来
							if(synDoc != null){
								String storeTemp = synDoc.getStoreLocation();
								if(StringUtils.isNotBlank(storeTemp)){
									synDoc.setStoreLocation(storeTemp + "#" + storeLocation);
								}else{
									synDoc.setStoreLocation(storeLocation);
								}
							}else{
								synDoc = new TSynDoc();
								synDoc.setStoreLocation(storeLocation);
							}
							//保存同步档案信息
							synDoc.setId(fid);
							synDoc.setName(name);
							synDoc.setPath(newPath);
							//文件存储位置
							
							synDoc.setPreviewPath(previewPath);
							synDoc.setSuffix(suffix);
							TSynProject synProject = (TSynProject) archivesService.EntityQuery(TSynProject.class, pid);
							synDoc.setTSynProject(synProject);
						}
						boolean isIndex = true;
						
						//区域评审的电压等级文档不需要产生索引
						if("2".equals(systemType) && location.trim().length() ==6){
							isIndex = false;
						}
						
						
						execute(synDoc,unit, type, doc,oldPath, newPath,pro,indexStore,isIndex,systemType);
					}
				}
				String newFile = taskPath + File.separator + "completed"
				+ File.separator + file.getName().substring(0, file.getName().length()-4);
				FileUtil.moveFile(file.getAbsolutePath(), newFile);
				uploadFileID(unit);
			}
		}
	}

	private static void uploadFileID(String to) {
		Configurater config = Configurater.getInstance();
		//上传成功和失败的文件ID到国网
		String success = StoreFileReceiveID.createInstance().getSuccessFileID();
		String fail = StoreFileReceiveID.createInstance().getFailFileID();
		String unitCode = config.getConfigValue("unitCode");
		String pwd = config.getConfigValue("wspwd");
		
		//获取上一次发送失败的文件ID
		String failFromXml = FailFileIDXMLUtils.getFailFileIDByCode(unitCode, to);
		String successFromXml = FailFileIDXMLUtils.getSuccessFileIDByCode(unitCode, to);
		if(StringUtils.isNotBlank(failFromXml)){
			fail += "," + failFromXml;
		}
		if(StringUtils.isNotBlank(successFromXml)){
			success += "," + successFromXml;
		}
		
		if(StringUtils.isBlank(fail) && StringUtils.isBlank(success))
			return ;
		
		//删除重复的ID
		fail = FailFileIDXMLUtils.deleteRepeatID(new StringBuffer(fail));
		success = FailFileIDXMLUtils.deleteRepeatID(new StringBuffer(success));
		
		//如果国网连接成功
		String wsUrl = config.getConfigValue("wsUrl");
		if(StringUtils.isNotBlank(wsUrl)){
			try{
				WSClient client = WSClient.getClient(wsUrl);
				client.getService().receiveSynFileID(success, fail, unitCode, pwd);
			}catch(Exception e){
				saveUploadFailIDS(to, success, fail, unitCode);
			}
		}else{
			saveUploadFailIDS(to, success, fail, unitCode);
		}
	}

	private static void  saveUploadFailIDS(String to, String success, String fail,
			String unitCode) {
		//logger.warn("上传接收失败和成功的文件ID失败，文件ID已经保存到本地，下一次发送");
		FailFileIDXMLUtils.saveUploadFileFailIDs("fail", fail, unitCode, to);
		FailFileIDXMLUtils.saveUploadFileFailIDs("success", success, unitCode, to);
	}


	/**
	 * 执行具体操作
	 * @param tempDir
	 * @param file
	 * @param unit
	 * @param fid
	 * @param type
	 * @param doc
	 * @param oldPath
	 * @param newPath
	 * @param userUnit
	 * @param doctype
	 * @param pro
	 * @param folder
	 * @param isDelete
	 * @return
	 */
	private void execute(TSynDoc synDoc ,String unit,
			String type, TDoc doc, String oldPath, String newPath,
			TProject pro, String storeLocation,boolean isIndex,String systemType) {
		
		String tempDir = Configurater.getInstance().getConfigValue("temp");
		tempDir = super.context.getRealPath(tempDir);
		
		// 迁移
		if (type.equals(DataSynStatus.REMOVE.toString())) {
			try{
				archivesService.removeIndex("syn", oldPath, newPath, doc, pro, storeLocation, isIndex);
				if(!"2".equals(systemType)){
					//改变存储
					synDoc.setStoreLocation(FileStoreLocation.changStoreLocation(synDoc.getStoreLocation()));
					archivesService.saveOrUpdateSynDoc(synDoc);
				}
			}catch(Exception e){
				logger.warn(e);
			}
		// 更新文档
		} else if (type.equals(DataSynStatus.UPDATE.toString())) {
			try{
				archivesService.updateIndex("syn", doc, pro, storeLocation,isIndex);
			}catch(Exception e){
				logger.warn(e);
			}
		// 归档
		} else if (type.equals(DataSynStatus.ARCHIVE.toString()) || type.equals(DataSynStatus.INSERT.toString())) {
			try{
				archivesService.archivesIndex(unit, tempDir, doc, pro,storeLocation,"syn",isIndex);
				if(!"2".equals(systemType)){
					//改变存储
					synDoc.setStoreLocation(FileStoreLocation.changStoreLocation(synDoc.getStoreLocation()));
					archivesService.saveOrUpdateSynDoc(synDoc);
				}
			}catch(Exception e){
				logger.warn(e);
			}
		} else if (type.equals(DataSynStatus.DELETE.toString())) {
			try{
				ContinueFTP.getInstance().deleteFile(doc.getPath());
				if(isIndex){
					archivesService.deleteArchiveIndex(storeLocation,"syn",doc.getId());
				}
				if(!"2".equals(systemType)){
					archivesService.deleteSynDoc(doc.getId());
				}
			}catch(Exception e){
				logger.warn(e);
			}
			// 销毁索引
		} else if (type.equals(DataSynStatus.DESTROY.toString())) {
			try{
				if(isIndex){
					doc.setStatus("02");
					archivesService.updateIndex("syn", doc, pro, storeLocation,true);
				}
			}catch(Exception e){
				logger.warn(e);
			}
			// 恢复索引
		} else if (type.equals(DataSynStatus.RESTORE.toString())) {
			try{
				ContinueFTP.getInstance().deleteFile(doc.getPath());
				if(isIndex){
					doc.setStatus("01");
					archivesService.updateIndex("syn", doc, pro, storeLocation,true);
				}
			}catch(Exception e){
				logger.warn(e);
			}
		}
	}

	
	/**
	 * 保存主工程
	 * @param document
	 */
	@SuppressWarnings("unchecked")
	private void saveProject(Document document){
		List<Element> projectNodes = document.selectNodes("//project");
		for(Element element : projectNodes){
			TSynProject project = new TSynProject();
			project.setCode(element.attributeValue("id"));
			project.setName(element.attributeValue("project"));
			project.setApproveUnitid(element.attributeValue("from_unit_id"));
			project.setGiveoutUnitid(element.attributeValue("from_unit_id")+"#"+element.attributeValue("to_unit_id"));
			project.setOwnerUnitId(element.attributeValue("owerUnitUnitId"));
			archivesService.saveOrUpdateSynProject(project);
		}
	}
	
	/**
	 * 保存子工程
	 * @param document
	 */
	@SuppressWarnings("unchecked")
	private void saveSubProject(Document document){
		List<Element> projectNodes = document.selectNodes("//sub_project");
		for(Element element : projectNodes){
			TSynProject project = new TSynProject();
			project.setCode(element.attributeValue("id"));
			project.setName(element.attributeValue("project"));
			project.setApproveUnitid(element.getParent().attributeValue("from_unit_id"));
			project.setGiveoutUnitid(element.getParent().attributeValue("from_unit_id")+"#"+element.getParent().attributeValue("to_unit_id"));
			project.setOwnerUnitId(element.getParent().attributeValue("owerUnitUnitId"));
			archivesService.saveOrUpdateSynProject(project);
		}
	}
}