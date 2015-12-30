package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.DataSynStatus;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TOperateLogTemp;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.service.system.IlogService;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.DataSynXmlUtils;
import com.nsc.dem.util.xml.FailFileIDXMLUtils;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.WSClient;

/**
 * 产生文件同步清单
 */
public class CreateTaskListTask extends TaskBase implements Job{

	private IlogService logService;
	private IarchivesService archivesService;
	private IprojectService projectService;
	private Logger logger = null;
	private TUser user = null;
	
	public CreateTaskListTask(String taskName, ServletContext context, long period) {
		super(taskName, context, period);
		logService = (IlogService) Component.getInstance("logService",super.context);
		archivesService = (IarchivesService) Component.getInstance("archivesService", super.context);
		projectService = (IprojectService) Component.getInstance("projectService", super.context);
		user = (TUser) logService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = logService.getLogManager(user).getLogger(
				CreateTaskListTask.class);
	}

	
	public CreateTaskListTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		logService = (IlogService) Component.getInstance("logService",super.context);
		archivesService = (IarchivesService) Component.getInstance("archivesService", super.context);
		projectService = (IprojectService) Component.getInstance("projectService", super.context);
		user = (TUser) logService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = logService.getLogManager(user).getLogger(
				CreateTaskListTask.class);
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String taskName = context.getTrigger().getKey().getName();
		logger.info("任务[ " + taskName + " ]启动于 "
					+ DateToString(context.getFireTime(), "yyyy-MM-dd HH:mm:ss"));
		try{
			doTask();
		}catch(Exception e){
			throw new JobExecutionException(e);
		}finally{
			logger.info("任务[ "+ taskName+ " ]下次将于"
					+ DateToString(context.getNextFireTime(),"yyyy-MM-dd HH:mm:ss") + " 启动");
		}		
	}	
	
	@Override
	public void doTask() throws Exception {
		try{
			//创建清单===>>分配清单===>>发送清单
			createTaskListXml();
			plotTask();
			sendTask();
		}catch(Exception e){
			logger.warn(e);
		}
	}
	
	
	/**
	 * 生成清单文件
	 * 
	 */
	private void createTaskListXml() {
		//删除文件产生的清单，放在最前面
		deleteFile();
		//插入
		insertFile();
		//其他操作
		updateAndOtherFile();
		//删除日志临时表数据
		deleteAllOperateLog();
	}
	
	/**
	 * 查询归档、更新（含迁移）、恢复、销毁文件
	 */
	@SuppressWarnings("unchecked")
	private void updateAndOtherFile() {
		// 查询归档、更新（含迁移）、恢复、销毁文件时产生的日志
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("in", new Object[] { DataSynStatus.ARCHIVE.toString(),DataSynStatus.UPDATE.toString(),
				DataSynStatus.DESTROY.toString(),DataSynStatus.RESTORE.toString()});
		List<TOperateLogTemp> list = logService.findOperateTempLog(map);
		for (TOperateLogTemp operateLog : list) {
			String content = operateLog.getContent();
			// 截取ID
			String docId = content.substring(content.lastIndexOf("id=") + 3,content.length());
			// 查询该文档是否存在
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("in", new Object[] { docId });
			List<TDoc> docList = archivesService.queryDocIsArchives(param);
			// 如果文件不存在，可能已经删除
			if (docList != null && docList.size() == 1) {
				TDoc doc = docList.get(0);
				//更新文档日志：如果path为空，则为文件迁移，否则为文档基本信息更新
				//如果基本信息更新不用处理
				if(operateLog.getType().equals(DataSynStatus.UPDATE.toString())){
					if(content.indexOf(" Path:") > 0){
						operateLog.setType(DataSynStatus.REMOVE.toString());
						try {
							String path = ""; 
							content = content.substring(content.indexOf(" Path:"));
							path = content.substring(content.indexOf(" Path:")+6, content.indexOf(","));
							doc.setPath(path);
						} catch (Exception e) {
							logger.warn(e);
						}
					}
					if(content.indexOf(" PreviewPath:") > 0){
						operateLog.setType(DataSynStatus.REMOVE.toString());
						try {
							String path = "";
							path = content.substring(content.indexOf(" PreviewPath:")+13, content.indexOf(" WHERE"));
							doc.setPreviewPath(path);
						} catch (Exception e) {
							logger.warn(e);
						}
					}
				}
				
				//更新不用处理
				if(!operateLog.getType().equals(DataSynStatus.UPDATE.toString())){
					TProject project = archivesService.getTProjectBydocId(doc.getId());
					createNode(doc, operateLog.getType(),project, operateLog.getOperateTime()+"");
				}
			}
		}
	}

	/**
	 * 创建节点
	 * 
	 * @param doc
	 * @param project
	 * @param operate
	 */
	private void createNode(TDoc doc, String operate, TProject project,
			String updateTime) {
		// 根据文档ID查询工程

		// 如果是工程
		if (project.getParentId() == null
				|| "".equals(project.getParentId() + "".trim())) {
			DataSynXmlUtils.createProjectNode(project);
			DataSynXmlUtils.createFileNode(doc, operate, project.getId() + "",
					DataSynXmlUtils.PROJECT, updateTime);
		} else {// 子工程
			TProject parentProject = (TProject) projectService.EntityQuery(
					TProject.class, project.getParentId());
			DataSynXmlUtils.createSubProjectNode(parentProject, project);
			DataSynXmlUtils.createFileNode(doc, operate, project.getId() + "",
					DataSynXmlUtils.SUBPROJECT, updateTime);
		}
	}

	/**
	 * 删除文件 特殊处理，删除的文件在t_doc表中不存在记录 所以，必须获取到文件所属工程
	 */
	private void deleteFile() {
		// 查询文档更新日志
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("in", new Object[] { DataSynStatus.DELETE.toString() });
		List<TOperateLogTemp> list = logService.findOperateTempLog(map);

		for (TOperateLogTemp operateLog : list) {
			String content = operateLog.getContent();
			if (content.indexOf("Projectid:") > 0) {

				// projectId
				String proTemp = content.substring(content
						.indexOf("Projectid:"));
				String projectid = proTemp.substring(10, proTemp.indexOf(","));

				// 工程ID为空，不需要同步
				if (StringUtils.isNotBlank(projectid)
						&& !projectid.equals("null")) {
					TProject project = (TProject) projectService.EntityQuery(
							TProject.class, Long.valueOf(projectid.trim()));

					TDoc doc = new TDoc();
					// docid
					String docTemp = content.substring(content.indexOf("Id:"));
					String docId = docTemp.substring(3, docTemp.indexOf(","));
					doc.setId(docId);

					// docPath
					String pathTemp = content.substring(content
							.indexOf("Path:"));
					String docPath = pathTemp.substring(5, pathTemp
							.indexOf(","));
					doc.setPath(docPath);

					// docStatus
					String statusTemp = content.substring(content
							.indexOf(" Status:"));
					String status = statusTemp.substring(8, statusTemp
							.indexOf(","));
					if(null != project){
						if (!"03".equals(status))// 未归档文件不处理
							createNode(doc, operateLog.getType(), project,
									operateLog.getOperateTime() + "");
					}
				}
			}
		}
	}

	/**
	 * 发送任务
	 */
	private void sendTask() {
		try {
			Configurater config = Configurater.getInstance();
			String taskDir = config.getConfigValue("failTaskListDir");
			String pwd = config.getConfigValue("wspwd");
			
			String path = XmlUtils.getPath() + taskDir;
			//得到web-info/faitask目录下的所有文件名
			File taskFile = new File(path);
			
			File[] list = taskFile.listFiles();  //得到所有的文件列表（含文件名）
			
			//以文件为单位进行发送
			for (File file : list) {
				if(file.getName().indexOf(".xml") > 0){
					//单位ID
					String unit = file.getName().substring(0, file.getName().indexOf(".xml"));
					DataHandler dataHandler = new DataHandler(new URL("file:////"+file.getAbsolutePath()));
/**********************************************************************************************/
					//调用webservice
					XmlUtils intenterUtil = XmlUtils.getInstance("intenterIp.xml");
					Document document = intenterUtil.getDocument();
					//获取接收单位的webservice地址
					Element element = (Element) document.selectSingleNode("//intenter[@code="+unit+"]");
					if(element != null){
						String wsUrl = element.attributeValue("appIp");
						if(wsUrl != null){
							try{
								WSClient client = WSClient.getClient(wsUrl);
								//发送成功后删除文件
								if(client.getService().receiveFileList(dataHandler,pwd)){
									if(file.exists()){
										file.delete();
									}
								}
							}catch(Exception e){
								logger.info(wsUrl+"连接失败");
							}
						}
					}else{
						logger.info(unit+"信息没有成功获取");
					}
/**********************************************************************************************/
				}
			}
		} catch (Exception e) {
			logger.warn(e);
		}
	}	

	/**
	 * 分配任务 将任务分配好以后，保存在web-info/failtask/目录下 如果该单位有没有发送成功的任务，追加
	 */
	@SuppressWarnings("unchecked")
	private void plotTask() {
		try {
			Document document = XmlUtils.getInstance().getDocument();
			
			//优化
			//1.删除归档和插入相同的文件ID
			document = deleteAchieveAndInsertSame(document);
			// 获取所有工程节点
			List<Element> list = document.selectNodes("//"
					+ DataSynXmlUtils.PROJECT);

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			for (Element element : list) {
				// 得到to_unit_id
				String totemp = element.attributeValue("to_unit_id");
				String path = XmlUtils.getPath()
						+ Configurater.getInstance().getConfigValue(
								"failTaskListDir");
				String[] to = totemp.split("#");
				
				for (String filename : to) {
					// 根据ID查找
					File file = new File(path, filename + ".xml");
					
					Document documentTemp = null;
					//如果文件不存在，创建一个新的document
					if(!file.exists()){
						file.createNewFile();
						documentTemp = DocumentHelper.createDocument();
						documentTemp.add(DocumentHelper.createElement("tasks"));
					}else{
						SAXReader reader = new SAXReader();
						reader.setEncoding("utf-8");
						documentTemp = reader.read(file);
					}
					
					XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
					//解除element父子关系
					element.detach();
					//得到tasks节点
					Element rootElement = (Element) documentTemp.selectSingleNode("tasks");
					rootElement.add(element);
					
					documentTemp.getRootElement().add(element.detach());
					writer.write(documentTemp);
					writer.close();
				}
			}
			//任务分发完成以后删除所有除根节点以外的其他节点
			XmlUtils.getInstance().deleteAllNode(document);
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	/**
	 * 归档文件重新上传
	 *  1.查询L06日志
	 *  2.截取ID
	 *  3.查询文档
	 *  4.查询文档所属工程
	 *  5.产生清单
	 */
	@SuppressWarnings("unchecked")
	private void insertFile(){
		//查询L06日志
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("in", new Object[] {DataSynStatus.INSERT.toString()});
		List<TOperateLogTemp> list = logService.findOperateTempLog(map);
		
		for (TOperateLogTemp operateLog : list) {
				String content = operateLog.getContent();
				content = content.substring(content.indexOf("Id:"));
				// 截取ID
				String docId = content.substring(content.lastIndexOf("Id:") + 3,content.indexOf(","));
				//查询文档
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("in", new Object[] { docId });
				List<TDoc> docList = archivesService.queryDocIsArchives(param);
				//如果文件存在
				if (docList != null && docList.size() == 1) {
					TProject project = archivesService.getTProjectBydocId(docList.get(0).getId());
					createNode(docList.get(0), operateLog.getType(),project, operateLog.getOperateTime()+"");
				}
		}
		
		Configurater config = Configurater.getInstance();
		String loationCode = "";
		if("1".equals(config.getConfigValue("system_type"))){
			loationCode = config.getConfigValue("country");
		}else if("3".equals(config.getConfigValue("system_type"))){
			loationCode = config.getConfigValue("unitCode");
		}
		
		//用户返回的失败文件
		String failIds = FailFileIDXMLUtils.getFailFileIDByCode(loationCode);		
		String[] ids = failIds.split(",");
		if(ids.length > 0){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("in", ids);
			List<TDoc> docList = archivesService.queryDocIsArchives(param);
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//如果文件存在
			for(TDoc tdoc : docList) {
				TProject project = archivesService.getTProjectBydocId(tdoc.getId());
				if(null != project)
					createNode(tdoc, "L06",project, timeFormat.format(new Date())+"");
			}
		}
	}
	
	
	
	
	
	/**
	 * 删除日志
	 */
	private void deleteAllOperateLog(){
		logService.deleteAllTempOperateLog();
	}
	
	
	/**
	 * 删除归档和插入文件ID相同的节点
	 */
	@SuppressWarnings("unchecked")
	public Document deleteAchieveAndInsertSame(Document document){
		//插入
		List<Element> insertLists = document.selectNodes("//file[@operate='"+DataSynStatus.INSERT.toString()+"']");
		//归档
		List<Element> arthieveLists = document.selectNodes("//file[@operate='"+DataSynStatus.ARCHIVE.toString()+"']");
		
		for(Element arthieve : arthieveLists){
			String artId = arthieve.attributeValue("id");
			for(Element insert : insertLists){
				String inId = insert.attributeValue("id");
				if(artId.trim().equals(inId.trim()))
				  insert.getParent().remove(insert.detach());
			}
		}
		return document;
	}
	
}
