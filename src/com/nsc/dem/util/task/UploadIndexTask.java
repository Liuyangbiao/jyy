package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.ContinueFTP;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.util.index.FileDirUtils;
import com.nsc.dem.util.index.StoreLastModifyTime;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FtpXmlUtils;

/**
 * 上传本地和同步索引库到国网FTP站点
 *    
 *
 */
public class UploadIndexTask extends TaskBase implements Job{
	private IuserService userService;
	private TUser user = null;
	private Logger logger = null;
	
	public UploadIndexTask(String taskName, ServletContext context, long period) {
		super(taskName, context, period);
		userService = (IuserService) Component.getInstance("userService",super.context);
		user = (TUser) userService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = userService.getLogManager(user).getLogger(
				UploadIndexTask.class);
	}
	
	public UploadIndexTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		userService = (IuserService) Component.getInstance("userService",super.context);
		user = (TUser) userService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = userService.getLogManager(user).getLogger(
				UploadIndexTask.class);
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
		Configurater config = Configurater.getInstance();
		String unitCode = config.getConfigValue("unitCode");
		String countryCode = config.getConfigValue("country");
		//获取索引库的位置
		String localDir = FileDirUtils.getRealPathByUnitId("doc_read_Dir", unitCode, "local");
		String synDir = FileDirUtils.getRealPathByUnitId("doc_read_Dir", unitCode, "syn");
		String localCon = FileDirUtils.getRealPathByUnitId("doc_content_Dir", unitCode, "local");
		String synCon = FileDirUtils.getRealPathByUnitId("doc_content_Dir", unitCode, "syn");
		
		//记录文件上传时的最后修改时间
		StoreLastModifyTime store = StoreLastModifyTime.createInstance();
		
		String[] ftpInfo = FtpXmlUtils.getFTPInfo(countryCode);
		if(ftpInfo  == null || ftpInfo.length < 4){
			logger.warn("请检查国网FTP信息");
			return;
		}
		ContinueFTP ftp = ContinueFTP.getDownLoadInstance(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
		if(ftp == null){
			logger.warn("国网FTP连接失败");
			return;
		}
		
		//上传本地文件索引库
		if(isUpload(store.getLocal(), new File(localDir), true)){
			for(File file : new File(localDir).listFiles()){
				ftp.upload(file.getAbsolutePath(), "write/"+unitCode+"/local"+File.separator+file.getName());
			}
			StoreLastModifyTime.createInstance().setLocal(new File(localDir).lastModified());
		}
		
		//上传同步索引库
		if(isUpload(store.getSyn(), new File(synDir),true)){
			for(File file : new File(synDir).listFiles()){
				ftp.upload(file.getAbsolutePath(), "write/"+unitCode+"/syn"+File.separator+file.getName());
			}
			StoreLastModifyTime.createInstance().setSyn(new File(synDir).lastModified());
		}
		
		//上传本地文件内容库
		if(isUpload(store.getLocalContent(),new File(localCon), false)){
			for(File file : new File(localCon).listFiles()){
				ftp.upload(file.getAbsolutePath(), "content/"+unitCode+"/local"+File.separator+file.getName());
			}
			StoreLastModifyTime.createInstance().setLocalContent(new File(localCon).lastModified());
		}
		
		//上传同步的内容库
		if(isUpload(store.getSynContent(), new File(synCon), false)){
			for(File file : new File(synCon).listFiles()){
				ftp.upload(file.getAbsolutePath(), "content/"+unitCode+"/syn"+File.separator+file.getName());
			}
			StoreLastModifyTime.createInstance().setSynContent(new File(synCon).lastModified());
		}
	}
	
	/**
	 * 
	 * @param lastModify :文件上一次上传时的最后修改时间
	 * @param file       ：需要上转的目录库
	 * @param ischeck    ：是否对索引进行检查
	 * @return
	 */
	private boolean isUpload(long lastModify ,File file, boolean ischeck){
		if(!file.exists()){
			logger.info("索引库不存在:"+file.getAbsolutePath());
			return false;
		}
		
		if(lastModify == 0 && file.listFiles().length > 0){
			if(ischeck){
				for(String fileName : file.list()){
					if(fileName.lastIndexOf("segments") != -1){
						logger.info("第一次上传索引库:"+file.getAbsolutePath());
						return true;
					}
				}
				logger.info("索引库是一个不完整的索引库 ,请检查:"+file.getAbsolutePath());
				return false;
			}
			logger.info("第一次上传内容库:"+file.getAbsolutePath());
			return true;
		}
		
		if(lastModify < file.lastModified() && file.listFiles().length > 0){
			if(ischeck){
				for(String fileName : file.list()){
					if(fileName.lastIndexOf("segments") != -1){
						logger.info("开始上传索引库:"+file.getAbsolutePath());
						return true;
					}
				}
				logger.info("索引库是一个不完整的索引库 ,请检查:"+file.getAbsolutePath());
				return false;
			}else{
				logger.info("开始上传内容库:"+file.getAbsolutePath());
			}
			return true;
		}else{
			if(ischeck){
				logger.info("索引库"+file.getAbsolutePath()+"不用上传,上一次修改时间:"+new Date(lastModify));
			}else{
				logger.info("内容库"+file.getAbsolutePath()+"不用上传,上一次修改时间:"+new Date(lastModify));
			}
			return false;
		}
	}
}