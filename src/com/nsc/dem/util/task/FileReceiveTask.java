package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;
import javax.servlet.ServletContext;

import org.jdom.Document;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.MainProject;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.webservice.archive.ArchiveUpdater;
import com.nsc.dem.webservice.client.RPCClient;
import com.nsc.dem.webservice.project.ProjectXmlParser;
import com.nsc.dem.webservice.project.ReviewPlanService;
import com.nsc.dem.webservice.util.BuildErrorXml;
import com.nsc.dem.webservice.util.FileAccept;

public class FileReceiveTask extends TaskBase implements Job {

	private IarchivesService archivesService;

	private TUser user = null;

	private Logger logger = null;

	public FileReceiveTask(String taskName, ServletContext context, long period) {
		super(taskName, context, period);
		archivesService = (IarchivesService) Component.getInstance(
				"archivesService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = archivesService.getLogManager(user).getLogger(
				FileReceiveTask.class);
	}

	@Override
	public void doTask() throws Exception {
		importArchieves();
	}

	/**
	 * 支持quartz
	 */
	public FileReceiveTask() {
		super(null, Configurater.getInstance().getServletContext(), 0);

		archivesService = (IarchivesService) Component.getInstance(
				"archivesService", super.context);
		user = (TUser) archivesService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = archivesService.getLogManager(user).getLogger(
				FileReceiveTask.class);
	}

	/**
	 * 支持quartz
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String taskName = context.getTrigger().getKey().getName();
		logger.info("任务[ " + taskName + " ]启动于 "
					+ DateToString(context.getFireTime(), "yyyy-MM-dd HH:mm:ss"));
		
		try{
			importArchieves();
		}finally{
			logger.info("任务[ "+ taskName+ " ]下次将于"
						+ DateToString(context.getNextFireTime(),"yyyy-MM-dd HH:mm:ss") + " 启动");
		}
	}

	@SuppressWarnings("unchecked")
	public String importArchieves() {
		String stream = null;
		String xmlPath = FileUtil.getWebRootPath() + File.separator + "temp";
		File xmlFolder = new File(xmlPath);
		if (!xmlFolder.exists()) {
			logger.info("XML文件目录不存在!");
			xmlFolder.mkdirs();
		} else {
			// 处理工各程
			FileAccept accept = new FileAccept("proj");
			File[] files = xmlFolder.listFiles(accept);
			if (files != null && files.length > 0) {
				ProjectXmlParser parser = new ProjectXmlParser();
				ReviewPlanService review = new ReviewPlanService();
				for (File file : files) {
					List<MainProject> list = null;
					try {
						Document document = parser.getDocument(file);
						list = parser.parseXml(document, false);
					} catch (Exception e) {
						logger.warn("运行导入档案任务时发生异常!" + e);
						continue;
					}
					review.saveProject2Database(list);
					String newPath = file.getAbsolutePath().substring(0,
							file.getAbsolutePath().lastIndexOf("\\"));
					String newFile = newPath + File.separator + "completed"
							+ File.separator + file.getName();
					FileUtil.moveFile(file.getAbsolutePath(), newFile);
				}
			}

			// 处理文件
			accept = new FileAccept("file");
			files = xmlFolder.listFiles(accept);
			List<Map> errorList = new ArrayList<Map>();
			if (files != null && files.length > 0) {
				for (File file : files) {
					ArchiveUpdater archiveUpdater = new ArchiveUpdater();
					try {
						errorList.add(archiveUpdater.getXmlInfo(file));
					} catch (Exception e) {
						logger.warn("运行导入档案任务时发生异常!XML文件:"
								+ file.getAbsolutePath(), e);
						continue;
					}
					String newPath = file.getAbsolutePath().substring(0,
							file.getAbsolutePath().lastIndexOf("\\"));
					String newFile = newPath + File.separator + "completed"
							+ File.separator + file.getName();
					FileUtil.moveFile(file.getAbsolutePath(), newFile);
					//清空下载文件夹
					/*String path = FileUtil.getWebRootPath() + File.separator + "archieve";
					File fodler = new File(path);
					File[] allFile = fodler.listFiles();
					for(File f : allFile){
						if(file.exists())
						f.delete();
					}*/
				}
				stream = BuildErrorXml.buildXml(errorList);
			}
		}
		if (stream != null && !stream.trim().equals("")) {
			logger.warn("同步失败的文件ID:" + stream);
			RPCClient.getRPCClient(null, "serviceUrl").updateErrorFilesFlag(
					stream);
		}
		return stream;
	}
}
