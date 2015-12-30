package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.DateUtils;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.webservice.client.CXFClient;

/**
 * 跨域 统计
 * 
 * @author Administrator
 * 
 */
public class DataCountTask extends TaskBase implements Job{

	private IprojectService projectService;
	private IsearchesService searchesService;
	private Logger logger = null;
	private TUser user = null;

	public DataCountTask(String taskName, ServletContext context, long period) {

		super(taskName, context, period);
		projectService = (IprojectService) Component.getInstance(
				"projectService", super.context);
		searchesService = (IsearchesService) Component.getInstance(
				"searchesService", super.context);
		user = (TUser) searchesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = searchesService.getLogManager(user).getLogger(
				DataCountTask.class);
	}
	
	public DataCountTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		projectService = (IprojectService) Component.getInstance(
				"projectService", super.context);
		searchesService = (IsearchesService) Component.getInstance(
				"searchesService", super.context);
		user = (TUser) searchesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = searchesService.getLogManager(user).getLogger(
				DataCountTask.class);
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
		// 创建父节点
		Element root = new Element("input");
		Element node;
		Configurater config = Configurater.getInstance();
		String code = config.getConfigValue("unitCode");
		String pwd = config.getConfigValue("wspwd");
		// 子节点
		node = new Element("count");
		node.setAttribute("code", code);

		// 取当前时间
		String time = DateUtils.DateToString(new Date(), "yyyy-MM-dd");
		node.setAttribute("create_time", time);

		// 决定yearMonth的长度
		List<TProject> pros = projectService.getProjectByOwenCode(code);
		for (int i = 0; i < pros.size(); i++) {
			TProject pro = pros.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pid", pro.getId());
			map.put("unit", pro.getTUnitByOwnerUnitId().getCode());
			List<Object[]> sour = searchesService.selectInsertDocPic(map,code);

			Element pidNode = new Element("project_id");
			pidNode.setText(pro.getId().toString());

			Element pnameNode = new Element("project_name");
			pnameNode.setText(pro.getName());

			Element vlevelNode = new Element("voltage_level");
			vlevelNode.setText(pro.getVoltageLevel());
			
			for (Object[] obj : sour) {
				String tdoc = (String) obj[0];
				int count = projectService.getDocCountByProjectId(pro.getId()
						.toString(), tdoc);
				// 创建count的子节点
				Element year = new Element("year_month");
				year.setAttribute("value", tdoc);
				year.setAttribute("docsCount", String.valueOf(count));
				
				year.addContent(pidNode);
				year.addContent(pnameNode);
				year.addContent(vlevelNode);
				
				node.addContent(year);
			}
		}
		
		root.addContent(node);
		
		Document document = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		out.setFormat(format);
		String stream = out.outputString(document);
		String wsUrl = Configurater.getInstance().getConfigValue("wsUrl");
		CXFClient client = CXFClient.getCXFClient(wsUrl);
		client.getService().crossdomainStatistics(stream,pwd);
	}
}
