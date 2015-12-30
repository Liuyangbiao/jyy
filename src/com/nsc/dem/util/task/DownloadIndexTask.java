package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.Local;
import javax.servlet.ServletContext;


import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.ContinueFTP;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.util.index.FileDirUtils;
import com.nsc.dem.util.index.IndexSearchManager;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.XmlUtils;

public class DownloadIndexTask extends TaskBase implements Job{
	private StringBuffer buffer = new StringBuffer();
	private Logger logger = null;
	private TUser user = null;
	private IService baseService = null;
	public DownloadIndexTask(String taskName, ServletContext context,long period) throws URISyntaxException{
		super(taskName, context,period);
		baseService = (IService) Component.getInstance("baseService", super.context);
		user = (TUser) baseService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = baseService.getLogManager(user).getLogger(
				DownloadIndexTask.class);
	}
	
	public DownloadIndexTask() throws URISyntaxException{
		super(null,Configurater.getInstance().getServletContext(),0);
		baseService = (IService) Component.getInstance("baseService", super.context);
		user = (TUser) baseService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = baseService.getLogManager(user).getLogger(
				DownloadIndexTask.class);
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
		XmlUtils util = XmlUtils.getInstance("ftp.xml");
		Configurater config = Configurater.getInstance();
		org.dom4j.Document document = util.getDocument();
		List<Element> ftplements = document.selectNodes("//ftp");
		String countryCode = config.getConfigValue("country");
		String[] ftpInfo = FtpXmlUtils.getFTPInfo(countryCode);
		ContinueFTP ftp = ContinueFTP.getDownLoadInstance(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
		if(ftp != null){
			for (Element element : ftplements) {
				String code = element.attributeValue("code");
				//FTP对应的目录不为空
				if(!ftp.indexDirIsEmpty("/write/"+code+"/local/")){
					downloadAndInitIndex(ftp, code,"local");
				}
				if(!ftp.indexDirIsEmpty("/write/"+code+"/syn/")){
					downloadAndInitIndex(ftp, code,"syn");
				}
				if(!ftp.indexDirIsEmpty("/content/"+code+"/local/")){
					downloadIndex(ftp,code,"local");
				}
				if(!ftp.indexDirIsEmpty("/content/"+code+"/syn/")){
					downloadIndex(ftp,code,"syn");
				}
			}
			logger.info(buffer.toString());
		}else
		logger.warn("FTP连接失败");
	}
	
	private void downloadAndInitIndex(ContinueFTP ftp, String code,String isLocal)
			throws URISyntaxException, IOException {
		String localDir = FileDirUtils.getRealPathByUnitId("doc_write_Dir", code, isLocal);
		File writeFile = new File(localDir);
		if(!writeFile.exists()){
			writeFile.mkdirs();
		}
		if(writeFile.length() > 0 ){
			for (File file : writeFile.listFiles()) {
				file.delete();
			}
		}
		//String ftpPath = FileDirUtils.getDirByUnitId("doc_write_Dir", code, isLocal);
		ftp.downloadByFolder("/write/"+code+File.separator+isLocal, localDir);
		buffer.append(code+":"+isLocal+"下载成功\n");
		File readFile = FileDirUtils.getReadFileByWriteFile(writeFile);
		IndexSearchManager.getInstance().releaseSearch(readFile);
		if(IndexSearchManager.getInstance().initReadFolder(writeFile, readFile)){
			IndexSearchManager.getInstance().reloadSingleFile(readFile);
		}
	}
	
	private void downloadIndex(ContinueFTP ftp, String code,String isLocal) 
	            throws IOException, URISyntaxException{
		String localDir = FileDirUtils.getRealPathByUnitId("doc_content_Dir", code, isLocal);
		File writeFile = new File(localDir);
		if(!writeFile.exists()){
			writeFile.mkdirs();
		}
		if(writeFile.length() > 0 ){
			for (File file : writeFile.listFiles()) {
				file.delete();
			}
		}
		//String ftpPath = FileDirUtils.getDirByUnitId("doc_content_Dir", code, isLocal);
		ftp.downloadByFolder("/content/"+code+File.separator+isLocal, localDir);
		buffer.append(code+":"+isLocal+"下载成功\n");
	}
}