package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

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
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.WSClient;

/**
 * 发送服务器信息到国家电网
 *   只发送ftp即可
 *
 */
public class UploadServerInfoTask extends TaskBase implements Job{
	private IuserService userService;
	private TUser user = null;
	private Logger logger = null;
	
	public UploadServerInfoTask(String taskName, ServletContext context, long period) {
		super(taskName, context, period);
		userService = (IuserService) Component.getInstance("userService",super.context);
		user = (TUser) userService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = userService.getLogManager(user).getLogger(
				UploadIndexTask.class);
	}
	
	public UploadServerInfoTask(){
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
		//获取公司ID
		Configurater config = Configurater.getInstance();
		String unitId = config.getConfigValue("unitCode");
		String pwd = config.getConfigValue("wspwd");
		String wsUrl = config.getConfigValue("wsUrl");
		if(StringUtils.isNotBlank(unitId)){
			XmlUtils util = XmlUtils.getInstance("ftp.xml");
			Document document = util.getDocument();
			Element ftpElement = (Element) document.selectSingleNode("//ftp[@code='"+unitId+"']");
			if(null != ftpElement){
				ftpElement = FtpXmlUtils.createUnitNodeByWs(ftpElement);
				// to String 
				String ftpContent = FtpXmlUtils.getFtpNodeByUnitCode(unitId);
				util.saveDocument(document);
				//send
				try {
					WSClient client = WSClient.getClient(wsUrl);
					client.getService().receivePartServersInfo(ftpContent, null,pwd);
				} catch (Exception e) {
					logger.warn(e);
				}
			}
		}
	}
	
}
