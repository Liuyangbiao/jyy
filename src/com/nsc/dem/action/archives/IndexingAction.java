package com.nsc.dem.action.archives;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.FileField;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DesUtil;
import com.nsc.base.util.DownloadStatus;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.util.index.IndexStoreUitls;
import com.nsc.dem.util.task.DocIndexingTask;
import com.nsc.dem.util.task.FileReceiveTask;

public class IndexingAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -936659800363579380L;
	private IarchivesService archivesService;
	private IprojectService projectService;
	private String unitName1;
	private String unitNameCode1;
	private String projectName1;
	private String projectNameCode1;
	private String message1;
	private String indexCheckBox;

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	public void setProjectService(IprojectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 对文档进行索引处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String indexingArchive() throws Exception {
		
		String uCode = unitNameCode1;
		String pCode = projectNameCode1;
		if (StringUtils.isBlank(unitName1)) {
			uCode = "%%";
		}
		if (StringUtils.isBlank(projectNameCode1)) {
			pCode = "%%";
		}
		
		//如果重新创建索引  先删除旧的索引库及更新数据库
		if("on".equals(indexCheckBox)){
			Object[] o1 = new Object[] { "%%", "%%", uCode, pCode };
			List<Object[]> docList = this.archivesService.creatIndexingTDoc(o1);
			
			for (Object[] obj : docList) {
				TDoc doc = (TDoc) obj[0];
				doc.setMetaFlag(BigDecimal.ZERO);
				archivesService.updateEntity(doc);
				//检索库存储目录
				String storeLocation = IndexStoreUitls.getStoreLocation(doc.getId(), this.getSession().getServletContext());
				this.archivesService.deleteArchiveIndex(storeLocation,"local", doc.getId());
			}
		}
		
		//查询已归档且没有产生索引的文档
		Object[] o = new Object[] { "%" + BigDecimal.ZERO + "%", "%01%",uCode, pCode };
		List<Object[]> lists = this.archivesService.creatIndexingTDoc(o);
		
		//找出主工程
		Map<Long,TProject> mainProject = new HashMap<Long,TProject>(); 
		for(Object[] obj : lists){
			TProject project = (TProject) obj[1];
			if(project.getParentId() != null){
				project = (TProject) projectService.EntityQuery(TProject.class, project.getParentId());
			}
			if(project.getParentId() == null){
				mainProject.put(project.getId(), project);
			}
		}
		
		if (mainProject.isEmpty()) {
			message1 = "系统中不存在未建索引的文档";
			return SUCCESS;
		}
		
		ContinueFTP ftpUtil = null;
		String tempDir = Configurater.getInstance().getConfigValue("temp");
		tempDir = super.getRealPath(tempDir);
		File tempFolder = new File(tempDir);
		if (!tempFolder.isDirectory()) {
			tempFolder.mkdirs();
		}
		
		int count = 0;
		
		for(TProject project : mainProject.values()){
			//得到子工程
			List<Object> ids = new ArrayList<Object>();
			for(Object[] obj : lists){
				TProject subProject = (TProject) obj[1];
				if(subProject.getId().toString().equals(project.getId().toString())){
					ids.add(subProject.getId().toString());
					continue;
				}
				Long parentId = subProject.getParentId();
				if( parentId != null && parentId.toString().equals(project.getId().toString())){
					ids.add(subProject.getId().toString());
				}
			}
			
			List<TDoc> dList = archivesService.getDocByNoIndexProject(ids.toArray());
			
			Map<File, Map<Enum<?>, FileField>> files = new HashMap<File, Map<Enum<?>, FileField>>();
			Map<File, TDoc> docs = new HashMap<File, TDoc>();
			
			// 遍历文档
			for (TDoc tdoc : dList) {
				String format = Configurater.getInstance().getConfigValue("format",
						tdoc.getSuffix().toLowerCase());
				
				if(format==null){
					Logger.getLogger(DocIndexingTask.class).warn("未得到该文件的文件类型 "+tdoc.getSuffix().toLowerCase());
					continue;
				}
				String abstractor = Configurater.getInstance().getConfigValue(
						format.toLowerCase() + "_Abstractor");

				// 无此文档的文字提取工具，跳过
				if (abstractor == null)
					continue;

				// 查工程
				TProject tPro = projectService.getProjectByDoc(tdoc);
				if (tPro == null)
					continue;


				String remotePath = tdoc.getPath();

				String local = tempFolder.getAbsolutePath() + File.separator
						+ tdoc.getName() + "." + tdoc.getSuffix();

				//获取文档的所属业主单位
				String unitCode = project.getTUnitByOwnerUnitId().getCode();
				
				ftpUtil=ContinueFTP.getInstance();
				DownloadStatus status = ftpUtil.download(remotePath, local);

				if (status == DownloadStatus.Download_New_Success
						|| status == DownloadStatus.Download_From_Break_Success) {
					
					String dest = local;
					File file=new File(local);
					
					String mimeType = Configurater.getInstance().getConfigValue(
							"mime", FilenameUtils.getExtension(file.getName()));
					if (mimeType == null) {
						mimeType = Configurater.getInstance().getConfigValue(
								"mime", "*");
					}

					// 非图片需要解密
					if (mimeType.indexOf("image") == -1) {
						dest = Configurater.getInstance().getConfigValue("decrypt");

						File destPathFolder = new File(file.getParentFile(), dest);
						if (!destPathFolder.isDirectory())
							destPathFolder.mkdirs();

						dest = destPathFolder.getAbsolutePath() + File.separator
								+ file.getName();

						DesUtil.decrypt(local, dest);
					}
					TUnit unit = (TUnit) archivesService.EntityQuery(TUnit.class, unitCode);
					File f = new File(dest);
					files.put(f, archivesService.setArchivesIndex(tdoc, tPro, dest, unit));
					docs.put(f, tdoc);
					// 不是同一文件，即存在解密文件，清除未解密文件
					if (!file.equals(f))
						file.delete();
				}
			}
			try {
				Logger.getLogger(DocIndexingTask.class).info("需处理文件数量: "+files.size());
				count = files.size();
				String storeLocation = IndexStoreUitls.getStoreLocation(project.getTUnitByOwnerUnitId().getCode(),
						project.getApproveUnit().getProxyCode());
				if(StringUtils.isBlank(storeLocation))
					continue;
				
				Set<File> set =archivesService.addArchiveIndex(files,storeLocation,"local");
				//处理那些失败的文件
				if(set!=null){
					Logger.getLogger(DocIndexingTask.class).warn("失败文件数量: "+set.size());
					count -= set.size();
					Logger.getLogger(DocIndexingTask.class).warn("失败文件: "+set.toArray());
					Iterator<File> fileSet=set.iterator();
					
					while(fileSet.hasNext()){
						fileSet.next().delete();
					}
				}
				
			  }finally {
				for (File file : files.keySet()) {
					if (file.exists() && file.canRead()){
						TDoc doc = docs.get(file);
						doc.setMetaFlag(BigDecimal.ONE);
						this.archivesService.updateEntity(doc);
						file.delete();
					}
				}
			}
		}
		
		this.message1 = "本次共建索引 <b>" + count + "</b> 条";
		return SUCCESS;
	}

		
		
	public String importArchivesAction() {
		// 读取配置文件
		Configurater config = Configurater.getInstance();

		FileReceiveTask fileTack = new FileReceiveTask("FileReceiveTask",config
				.getServletContext(), 0);
		try {
			message1 = fileTack.importArchieves();

		} catch (Exception e) {
			message1 = e.getMessage();
			Logger.getLogger(IndexingAction.class).warn("档案同步过程出现异常", e);
		}

		return SUCCESS;
	}

	public String getUnitName1() {
		return unitName1;
	}

	public void setUnitName1(String unitName1) {
		this.unitName1 = unitName1;
	}

	public String getUnitNameCode1() {
		return unitNameCode1;
	}

	public void setUnitNameCode1(String unitNameCode1) {
		this.unitNameCode1 = unitNameCode1;
	}

	public String getProjectName1() {
		return projectName1;
	}

	public void setProjectName1(String projectName1) {
		this.projectName1 = projectName1;
	}

	public String getProjectNameCode1() {
		return projectNameCode1;
	}

	public void setProjectNameCode1(String projectNameCode1) {
		this.projectNameCode1 = projectNameCode1;
	}

	public String getMessage1() {
		return message1;
	}

	public void setMessage1(String message1) {
		this.message1 = message1;
	}

	public String getIndexCheckBox() {
		return indexCheckBox;
	}

	public void setIndexCheckBox(String indexCheckBox) {
		this.indexCheckBox = indexCheckBox;
	}

}
