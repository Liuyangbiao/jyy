package com.nsc.dem.action.archives;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.thumbnail.ThumbnailFactory;
import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DeleteFileStatus;
import com.nsc.base.util.DesUtil;
import com.nsc.base.util.ExtractImages;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;

public class ThumbnailsAction extends BaseAction {

	private static final long serialVersionUID = -936659800363579380L;
	private IarchivesService archivesService;
	private IprojectService projectService;
	private String unitName2;
	private String unitNameCode2;
	private String projectName2;
	private String projectNameCode2;
	private String message2;
	private String thumbnailsCheckBox;

	public String getMessage2() {
		return message2;
	}

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}
	
	public void setProjectService(IprojectService projectService){
		this.projectService=projectService;
	}

	/**
	 * 生成缩略图
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String generate() throws Exception {
		String tempDir=Configurater.getInstance().getConfigValue("temp");
		
		tempDir=super.getRealPath(tempDir);

		File tempFolder = new File(tempDir);

		if (!tempFolder.isDirectory()) {
			tempFolder.mkdirs();
		}

		List<Object> dList=new ArrayList<Object>();
		
		String uCode=unitNameCode2;
		String pCode=projectNameCode2;
		if("".equals(unitNameCode2) || unitNameCode2==null){
			uCode="%%";
		}
		if("".equals(projectNameCode2) || projectNameCode2==null){
			pCode="%%";
		}
		
		//生成
		if(thumbnailsCheckBox==null){
			Object[] o=new Object[]{uCode,pCode};
			//"1"代表缩略图  不存在
			List<Object[]> list1=this.archivesService.creatThumbnailsTDoc(o,"1");
			for (Object[] objs : list1) {
				TDoc tn=(TDoc)objs[0];
				ThumbnailInterface thum=ThumbnailFactory.getInstance().getAbstractor(tn.getName()+"."+tn.getSuffix());
				if(thum!=null){
					dList.add(tn);
				}
			}
		  }else if("on".equals(thumbnailsCheckBox)){ //重新生成
			  //批量查询TDoc
			  Object[] o=new Object[]{uCode,pCode};
			  //"2"代表缩略图  存在
			  List<Object[]> list=this.archivesService.creatThumbnailsTDoc(o,"2");
			  //删除已有缩略图
				for(int i=0;i<list.size();i++){
					Object[] obj=(Object[])list.get(i);
					TDoc doc=(TDoc) obj[0];
					
					DeleteFileStatus dfs=ContinueFTP.getInstance().deleteFile(doc.getPreviewPath());
					if(DeleteFileStatus.Delete_Remote_Success.name().equals(dfs.name())){
						
						Logger.getLogger(ThumbnailsAction.class).warn("缩略图删除成功!");
					}else{
						Logger.getLogger(ThumbnailsAction.class).warn("缩略图删除失败!");
					}
				}
			  //更新文件状态
			  for (Object[] objs : list) {
					TDoc tn=(TDoc)objs[0];
					tn.setPreviewPath(null);
					archivesService.updateEntity(tn);
			  }
			

			
			//得到须要生成缩略图的文件
			Object[] o2=new Object[]{uCode,pCode};
			//"1"代表缩略图  不存在
			List<Object[]> list2=this.archivesService.creatThumbnailsTDoc(o2,"1");
			for (Object[] objs : list2) {
				TDoc tn=(TDoc)objs[0];
				ThumbnailInterface thum=ThumbnailFactory.getInstance().getAbstractor(tn.getName()+"."+tn.getSuffix());
				if(thum!=null){
					dList.add(tn);
				}
			}
		}
		
		if(dList.isEmpty()){
			message2="系统中不存在要生成缩略图的文档";
			return SUCCESS;
		}
		
		ContinueFTP ftpUtil=null;
		int i=0;
		// 遍历文档
		for (Object obj : dList) {
			TDoc doc = (TDoc) obj;

			// 查工程
			TProject tPro = projectService.getProjectByDoc(doc);
			
			if(tPro==null) continue;
            //ftp文件存放路径
			String remotePath = doc.getPath();
            //下载到本地存放路径
			String local = tempFolder.getAbsolutePath() + File.separator
					+ doc.getName() + "." + doc.getSuffix();
			File file=new File(local);
			
			ftpUtil=ContinueFTP.getInstance();
		    //下载
			ftpUtil.download(remotePath, local);
			
			if(file.exists() && file.canRead()){
				
				String mimeType = Configurater.getInstance().getConfigValue("mime", FilenameUtils.getExtension(file.getName()));
				if (mimeType == null) {
					mimeType = Configurater.getInstance().getConfigValue("mime", "*");
				}
				
				String dest=local;
				
				//非图片需要解密
				if(mimeType.indexOf("image")==-1){
					dest = Configurater.getInstance().getConfigValue("decrypt");
	
					File destPathFolder = new File(file.getParentFile(),dest);
					if (!destPathFolder.isDirectory())
						destPathFolder.mkdirs();
					
					dest=destPathFolder.getAbsolutePath()+File.separator+file.getName();
	
					DesUtil.decrypt(local, dest);
				}
				
				//缩略图本地路径
				String imagePath="";
				//利用工厂模式 生成缩略图
				ThumbnailInterface thum=ThumbnailFactory.getInstance().getAbstractor(doc.getName()+"."+doc.getSuffix());
				if(thum!=null){
					imagePath=thum.makeThumbnil(dest);
					// FTP服务器缩略图路径
					
					remotePath=remotePath.replaceFirst("archives", "images");
					String ftpImagePath = remotePath.substring(0, remotePath.lastIndexOf("."))+ ".jpg";
					
					// 上传缩略图   此方法已经把缩略图FTP路径放到doc中
					ExtractImages.uploadImage(doc, imagePath, ftpImagePath);
					
				    //更新档案状态
					this.archivesService.updateEntity(doc);
					i++;
				}
				
				if (!dest.equals("")) {
					FileUtil.deleteFile(dest);
				}

				if (!local.equals("")) {
					FileUtil.deleteFile(local);
				}

				if (!imagePath.equals("")) {
					FileUtil.deleteFile(imagePath);
				}
				//1.生成缩略图
				//2.把缩略图上传
				//3.删除已经解密文件
				//4.生成了缩略图的 要把缩略图路径 加上
			}
		}
		
		
		
		this.message2="本次共生成缩略图 <b>"+i+"</b> 个!";
		return SUCCESS;
	}

	public String getUnitName2() {
		return unitName2;
	}

	public void setUnitName2(String unitName2) {
		this.unitName2 = unitName2;
	}

	public String getUnitNameCode2() {
		return unitNameCode2;
	}

	public void setUnitNameCode2(String unitNameCode2) {
		this.unitNameCode2 = unitNameCode2;
	}

	public String getProjectName2() {
		return projectName2;
	}

	public void setProjectName2(String projectName2) {
		this.projectName2 = projectName2;
	}

	public String getProjectNameCode2() {
		return projectNameCode2;
	}

	public void setProjectNameCode2(String projectNameCode2) {
		this.projectNameCode2 = projectNameCode2;
	}

	public String getThumbnailsCheckBox() {
		return thumbnailsCheckBox;
	}

	public void setThumbnailsCheckBox(String thumbnailsCheckBox) {
		this.thumbnailsCheckBox = thumbnailsCheckBox;
	}
}
