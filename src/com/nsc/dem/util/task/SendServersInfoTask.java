package com.nsc.dem.util.task;


import static com.nsc.base.util.DateUtils.DateToString;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import com.nsc.dem.bean.system.TServersInfo;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.StoreFtpNode;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.WSClient;

/**
 *   发送ftp.xml和intenterIp.xml到省公司
 *   发送整个文档
 * @author Administrator
 *
 */
public class SendServersInfoTask extends TaskBase implements Job{
	
	private IuserService userService;
	private TUser user = null;
	private Logger logger = null;
	
	public SendServersInfoTask(String taskName, ServletContext context, long period) {
		super(taskName, context, period);
		userService = (IuserService) Component.getInstance("userService",super.context);
		user = (TUser) userService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = userService.getLogManager(user).getLogger(
				SendServersInfoTask.class);
	}
	
	public SendServersInfoTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		userService = (IuserService) Component.getInstance("userService",super.context);
		user = (TUser) userService.EntityQuery(TUser.class, Configurater.getInstance().getConfigValue("ws_user"));
		logger = userService.getLogManager(user).getLogger(
				SendServersInfoTask.class);
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
		String country = config.getConfigValue("country");
		String pwd = config.getConfigValue("wspwd");
		
		StoreFtpNode store = StoreFtpNode.createInstance();
		//合并Map中的信息
		Hashtable<String, String> ftpNodes = store.getAllFtpNode();
		List<String> dels = new ArrayList<String>();
		for(Map.Entry<String, String> entry : ftpNodes.entrySet()){
			FtpXmlUtils.saveSynFtpNode(entry.getValue());
			dels.add(entry.getKey());
		}
		for (String str : dels) {
			store.deleteFtpNode(str);
		}
		
		List<TServersInfo> allServiceInfos = userService.findAllServersInfo();
		//重新ping
		Element element = FtpXmlUtils.createUnitNodeByLocal(country,allServiceInfos);
		FtpXmlUtils.saveFtpFile(element);
		
		XmlUtils util = XmlUtils.getInstance("ftp.xml");
		Document document = util.getDocument();
		//去掉默认值
		List<Element> ftpList = document.selectNodes("//ftp[@default='Y']");
		for(Element ftp : ftpList){
			ftp.addAttribute("default", null);
		}
		String ftpContent = XmlUtils.document2String(document);
		
		XmlUtils intenterUtil = XmlUtils.getInstance("intenterIp.xml");
		document = intenterUtil.getDocument();
		String intenterContent = XmlUtils.document2String(document);
		
		//send
		List<Element> intenterList = document.selectNodes("//intenter");
		for(Element intenter : intenterList){
			String wsUrl = intenter.attributeValue("appIp");
			String code = intenter.attributeValue("code");
			String name = intenter.attributeValue("name");
			if(StringUtils.isNotBlank(wsUrl) && StringUtils.isNotBlank(country) && code.length() >= 6){
				try {
					WSClient client = WSClient.getClient(wsUrl);
					client.getService().receiveAllServersInfo(ftpContent, intenterContent,pwd);
					logger.info(name+"   发送ftp.xml和intenterIp.xml成功");
				} catch (Exception e) {
					logger.info(name+"   发送ftp.xml和intenterIp.xml失败");
				}
			}
		}
	}
	
}
