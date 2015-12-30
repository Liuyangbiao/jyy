package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.thumbnail.ThumbnailFactory;
import com.nsc.base.thumbnail.ThumbnailInterface;
import com.nsc.base.util.Component;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DesUtil;
import com.nsc.base.util.ExtractImages;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.util.log.Logger;

public class DocImagingTask extends TaskBase implements Job{

	private IarchivesService archivesService;
	private IprojectService projectService;
	private TUser user = null;
	private Logger logger = null;
	
	public DocImagingTask(String taskName, ServletContext context,long period) {
		super(taskName, context, period);
		archivesService = (IarchivesService) Component.getInstance(
				"archivesService", super.context);
		projectService = (IprojectService) Component.getInstance(
				"projectService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = projectService.getLogManager(user).getLogger(
				DocImagingTask.class);
	}
	
	public DocImagingTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		archivesService = (IarchivesService) Component.getInstance(
				"archivesService", super.context);
		projectService = (IprojectService) Component.getInstance(
				"projectService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = projectService.getLogManager(user).getLogger(
				DocImagingTask.class);
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

	@Override
	public void doTask() throws Exception {
		String tempDir=Configurater.getInstance().getConfigValue("temp");
		tempDir = super.context.getRealPath(tempDir);

		File tempFolder = new File(tempDir);

		if (!tempFolder.isDirectory()) {
			tempFolder.mkdirs();
		}

		List<Object> dList=new ArrayList<Object>();
		
		//生成
			Object[] o=new Object[]{"%%","%%"};
			//"1"代表缩略图  不存在
			List<Object[]> list1=this.archivesService.creatThumbnailsTDoc(o,"1");
			for (Object[] objs : list1) {
				TDoc tn=(TDoc)objs[0];
				ThumbnailInterface thum=ThumbnailFactory.getInstance().getAbstractor(tn.getName()+"."+tn.getSuffix());
				if(thum!=null){
					dList.add(tn);
				}
			}
		
		if(dList.isEmpty()){
			String message2="系统中不存在要生成缩略图的文档";
			logger.info(message2);
			return ;
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
		String message2="本次共生成缩略图 <b>"+i+"</b> 个!";
		logger.info(message2);
	}
}
