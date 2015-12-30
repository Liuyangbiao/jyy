package com.nsc.dem.action.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.nsc.base.conf.Configurater;
import com.nsc.base.conf.ConstConfig;
import com.nsc.base.excel.ExportUtil;
import com.nsc.base.task.TaskManager;
import com.nsc.base.util.DateUtils;
import com.nsc.base.util.GetCh2Spell;
import com.nsc.base.util.PropertyModify;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.LogFileBean;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TLogFile;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.system.IlogService;
import com.nsc.dem.service.system.IsortSearchesService;
import com.nsc.dem.service.system.impl.LogServiceImpl;
import com.nsc.dem.util.log.Logger;
import com.opensymphony.xwork2.ActionContext;

public class LogOperateAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IsortSearchesService sortSearchesService;
	private IlogService logService;
	private TaskManager taskManager;

	private List<Map<String, Object>> list;
	// 默认的页数
	private int page;
	private int rows;
	private TableBean tablebean = new TableBean();
	private String returnValue;// ajax返回字符串
	private Object[] obj;// ajax返回json对象

	private String[] scope;// 备份范围
	private String timeFrom;// 备份起始时间
	private String timeTo;// 备份结束时间

	private String status;// 备份状态
	private String circle;// 备份周期
	private String time;// 备份周期（天/月）
	private String amount;// 备份周期

	/**
	 * 初始化 操作人
	 */
	public String opertor() {
		TUser tUser = new TUser();
		list = new ArrayList<Map<String, Object>>();
		List<Object> tUserList = sortSearchesService.EntityQuery(tUser);
		for (int i = 0; i < tUserList.size(); i++) {
			tUser = (TUser) tUserList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", tUser.getLoginId());
			map.put("id", tUser.getLoginId());
			map.put("name", tUser.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tUser.getName()));
			map.put("other", tUser.getDuty());
			list.add(map);
		}
		return "list";
	}

	/**
	 * 初始化系统日志分类
	 */
	public String typeLog() {
		list = new ArrayList<Map<String, Object>>();
		List<TDictionary> tDictionaryList = logService.sysLogDicList();
		for (int i = 0; i < tDictionaryList.size(); i++) {
			TDictionary tDictionary = tDictionaryList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", tDictionary.getCode());
			map.put("name", tDictionary.getName());
			list.add(map);
		}
		return "list";
	}

	/**
	 * 初始化档案日志分类
	 */
	public String typeLog2() {
		list = new ArrayList<Map<String, Object>>();
		List<TDictionary> tDictionaryList = logService.docLogDicList();
		for (int i = 0; i < tDictionaryList.size(); i++) {
			TDictionary tDictionary = tDictionaryList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", tDictionary.getCode());
			map.put("name", tDictionary.getName());
			list.add(map);
		}
		return "list";
	}

	/**
	 * 日志查看,系统日志,分页显示
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String display() throws Exception {
		int firstResult = (page - 1) * rows;
		int maxResults = page * rows;
		// 传过来的查询条件
		String vals = getRequest().getParameter("vals");
		List<Object[]> list = null;
		// 如果条件为空 则是认为第一次进来没点查询之前 不查询数据库
		if (vals == null) {
			list = new ArrayList<Object[]>();
		} else {
			String[] vStr = vals.split(",");
			String from = vStr[1]; // 开始时间
			if ("".equals(from) || from == null) {
				from = "1900-04-12";
			}
			String to = vStr[2]; // 结束时间
			if ("".equals(to) || to == null) {
				to = "3999-04-01";
			}
			String typeLog = vStr[3]; // 日志分类
			String start = "";
			String end = "";
			if ("".equals(typeLog) || typeLog == null) {
				String flag = getRequest().getParameter("flag");
				if ("sys".equals(flag)) {
					start = "L20";
					end = "L99";
				} else if ("doc".equals(flag)) {
					start = "L00";
					end = "L19";
				}
			}
			String operatorCode = vStr[4]; // 操作人
			String content = vStr[5]; // 内容
			if (!("".equals(content) || content == null)) {
				content = java.net.URLDecoder.decode(content, "UTF-8");
			}
			Object[] obj = null;
			if ("".equals(typeLog) || typeLog == null) {
				obj = new Object[] { start, end, "%" + operatorCode + "%", to,
						from, "%" + content + "%" };
			} else {
				obj = new Object[] { typeLog, typeLog,
						"%" + operatorCode + "%", to, from, "%" + content + "%" };
			}

			list = logService.queryOperateLogList(obj, firstResult, maxResults,
					tablebean);
		}

		List rowsList = new ArrayList();
		if (list != null) {
			for (Object[] objs : list) {
				TOperateLog tol = (TOperateLog) objs[0];
				TDictionary dic = (TDictionary) objs[2];
				// TDictionary dic = (TDictionary) logService.EntityQuery(
				// TDictionary.class, tol.getType());
				RowBean rowbean = new RowBean();
				rowbean.setCell(new Object[] { tol.getId(), dic.getName(),
						tol.getTUser().getName(), tol.getTarget(),
						DateUtils.DateToString(tol.getOperateTime()),
						tol.getContent() });
				rowbean.setId(tol.getId() + "");
				rowsList.add(rowbean);
			}
		}

		tablebean.setRows(rowsList);
		tablebean.setPage(this.page);
		int records = tablebean.getRecords();
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return "tab";
	}

	/****************************** 日志备份 *******************************************/

	/**
	 * 获取备份列表（无查询参数）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLogList() {
		int firstResult = (page - 1) * rows;
		int maxResults = page * rows;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object[]> list;

		String backupPath = Configurater.getInstance().getConfigValue("backupPath");// 备份路径
		try {
			list = logService.queryBackupList(map, firstResult, maxResults,
					tablebean);

			List rowsList = new ArrayList();
			if (list != null) {
				for (Object[] obj : list) {
					TLogFile log = (TLogFile) obj[0];
					TUser user = (TUser) obj[1];
					String lastTime = "";
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					if (log.getBackupTime() != null) {
						lastTime = timeFormat.format(log.getBackupTime());
					}
					RowBean rowbean = new RowBean();
					rowbean.setCell(new Object[] { lastTime,
							"/" + backupPath + "/" + log.getFileName(),
							user.getName() });
					rowbean.setId(log.getId() + "");
					rowsList.add(rowbean);
				}
			}

			tablebean.setRows(rowsList);
			tablebean.setPage(this.page);
			int records = tablebean.getRecords();
			tablebean.setTotal(records % rows == 0 ? records / rows : records
					/ rows + 1);

		} catch (Exception e) {
			logger.getLogger(LogOperateAction.class).warn(e.getMessage());
		}

		return "tab";
	}

	/**
	 * 查询日志备份状态，启用或停止
	 * 
	 * @return
	 */
	public String status() {
		//taskList.properties
		String taskBackup = Configurater.getInstance().getConfigValue("taskList.properties",
				"taskBackup");

		String backupPath = Configurater.getInstance().getConfigValue("backupPath");// 备份路径
		if (taskBackup != null && !"".equals(taskBackup)) {// 是否有备份配置

			String[] backupInfo = taskBackup.split(",");
			String time = backupInfo[0];// 执行时间
			String period = backupInfo[1];// 执行周期
			String status = backupInfo[4];// 执行状态
			if (status.equals("false")) {// 任务未启动
				obj = new Object[] { "error", backupPath };
			} else {
				int per = Integer.parseInt(period) / (60 * 24);
				String circle = "d";// 天
				if (per >= 30) {
					per = per / 30;
					circle = "m";// 月
				}

				ServletContext application = ServletActionContext.getRequest()
						.getSession().getServletContext();// 备份范围

				obj = new Object[] { "success", time, circle, per,
						(String[]) application.getAttribute("backupScope"),
						backupPath };
			}
		} else {

			obj = new Object[] { "error", backupPath };
		}

		return "status";
	}

	/**
	 * 手动日志备份
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String hand() {
		Logger logger = super.logger.getLogger(LogServiceImpl.class);
		logger.info("日志备份，手动备份");

		ActionContext ac = ActionContext.getContext();
		ServletContext sc = (ServletContext) ac
				.get(ServletActionContext.SERVLET_CONTEXT);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timeFrom", timeFrom);
		map.put("timeTo", timeTo);

		if (scope != null && scope.length > 0) {
			for (String type : scope) {
				map.put("type", type);
				// 将查询要备份的数据放入bean中
				List<Object[]> list = logService.logBackupHand(map);

				if (list != null && list.size() > 0) {

					String savePath = sc.getRealPath("/");
					String backupPath = Configurater.getInstance()
							.getConfigValue("backupPath");// 备份路径
					savePath = savePath + backupPath + "\\";
					File f = new File(savePath);
					if (!f.exists()) {// 如果目录不存在则创建该目录
						f.mkdirs();
					}

					Long i = 1L;
					List<LogFileBean> dataset = new ArrayList<LogFileBean>();

					for (Object[] obj : list) {
						TOperateLog log = (TOperateLog) obj[0];
						TUser user = (TUser) obj[1];
						TDictionary dic = (TDictionary) obj[2];

						LogFileBean logfile = new LogFileBean();
						logfile.setId(i);
						logfile.setTarget(log.getTarget());
						logfile.setContent(log.getContent());
						logfile.setType(dic.getName());
						logfile.setOperate(user.getName());
						logfile.setOperateTime(log.getOperateTime());

						dataset.add(logfile);
						i++;

					}

					// excle表头
					ExportUtil<LogFileBean> ex = new ExportUtil<LogFileBean>();
					String[] headers = { "序号", "对象", "日志分类", "内容", "操作人",
							"操作日期" };

					OutputStream out;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						String fileName = "log_bak_" + sdf.format(new Date())
								+ "_" + System.currentTimeMillis() + ".xls";
						savePath += fileName;
						logger.info("备份路径路径：------->" + savePath);

						out = new FileOutputStream(savePath);

						ex.exportExcel(headers, dataset, out);

						out.flush();
						out.close();
						logger.info("excel导出成功！");

						// 插入备份表
						TLogFile logfile = new TLogFile();
						logfile.setBackupPath(backupPath);
						logfile.setFileName(fileName);
						logfile.setType(type);
						logfile.setBackupTime(new Timestamp(System
								.currentTimeMillis()));
						logfile.setTUserByBackupOperator(super.getLoginUser());
						logService.insertEntity(logfile);

						// 刪除备份表记录
						logService.deleteLog(map);

					} catch (FileNotFoundException e) {
						logger.warn(e.getMessage());
					} catch (IOException e) {
						logger.warn(e.getMessage());
					}

				}
			}

			// 插入日志表
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年MM月dd日HH时mm分ss秒");

			TOperateLog tlog = new TOperateLog();
			tlog.setOperateTime(new Timestamp(System.currentTimeMillis()));
			tlog.setTarget(TUser.class.getSimpleName());
			tlog.setTUser(super.getLoginUser());
			tlog.setType("L45");
			tlog.setContent(super.getLoginUser().getName() + ","
					+ format.format(new Date()) + "进行日志备份");
			logService.insertEntity(tlog);

			returnValue = "success";
		} else {
			returnValue = "error";
		}
		return "backup";
	}

	/**
	 * 自动日志备份
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String auto() throws UnsupportedEncodingException {
		taskManager = new TaskManager();
		if (status != null && !"".equals(status)) {
			if (scope == null || scope.length == 0) {
				returnValue = "error";
				return "backup";
			} else {// 将备份范围放入application
				ServletContext application = ServletActionContext.getRequest()
						.getSession().getServletContext();// 备份范围
				application.setAttribute("backupScope", scope);
			}
		}

		String task = Configurater.getInstance().getConfigValue("taskList.properties","taskBackup");

		String[] taskInfo = task.split(",");
		String taskTime = taskInfo[0];// 执行时间
		String taskPeriod = taskInfo[1];// 执行周期
		String taskClazz = taskInfo[2];// 执行业务逻辑
		String taskName = taskInfo[3];// 执行任务名称，对应timerMap中的key
		String taskStatus = taskInfo[4];// 执行状态

		String path = Thread.currentThread().getContextClassLoader()
				.getResource("taskList.properties").getPath();
		path = java.net.URLDecoder.decode(path, "utf-8");

		taskTime = time;
		if (circle.equals("d")) {
			taskPeriod = String.valueOf(Integer.parseInt(amount) * 24 * 60);
		} else {
			taskPeriod = String
					.valueOf(Integer.parseInt(amount) * 24 * 60 * 30);
		}

		if (status == null || "".equals(status)) {// 停用自动备份
			taskStatus = "false";

			task = taskTime + "," + taskPeriod + "," + taskClazz + ","
					+ taskName + "," + taskStatus;
			PropertyModify.writeProperties(path, "taskBackup", task);// 修改配置文件
			try {// 重新加载配置文件
				Configurater.getInstance().loadConfigure(
						ConstConfig.TASK_LIST, "taskList.properties");
			} catch (IOException e) {
				logger.getLogger(LogOperateAction.class).warn(e.getMessage());
			}

			taskManager.stop("taskBackup");// 终止任务
		} else {// 启用自动备份
			taskStatus = "true";

			task = taskTime + "," + taskPeriod + "," + taskClazz + ","
					+ taskName + "," + taskStatus;
			PropertyModify.writeProperties(path, "taskBackup", task);// 修改配置文件
			try {// 重新加载配置文件
				Configurater.getInstance().loadConfigure(
						ConstConfig.TASK_LIST, "taskList.properties");
			} catch (IOException e) {
				logger.getLogger(LogOperateAction.class).warn(e.getMessage());
			}

			taskManager.start("taskBackup", time, circle, status);// 重新启动任务
		}
		returnValue = taskStatus;
		return "backup";
	}

	/****************************** 日志删除 *******************************************/

	/**
	 * 获去要删除的日志备份
	 */
	@SuppressWarnings("unchecked")
	public String getDeleteList() {
		int firstResult = (page - 1) * rows;
		int maxResults = page * rows;
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object[]> list;

		String backupPath = Configurater.getInstance().getConfigValue("backupPath");// 备份路径
		try {
			list = logService.queryLogDeleteList(map, firstResult, maxResults,
					tablebean);

			List rowsList = new ArrayList();
			if (list != null) {
				for (Object[] obj : list) {
					TLogFile log = (TLogFile) obj[0];
					TUser user = (TUser) obj[1];
					TDictionary dic = (TDictionary) obj[2];
					String lastTime = "";
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					if (log.getBackupTime() != null) {
						lastTime = timeFormat.format(log.getBackupTime());
					}
					RowBean rowbean = new RowBean();
					rowbean.setCell(new Object[] { lastTime, dic.getName(),
							"/" + backupPath + "/" + log.getFileName(),
							user.getName() });
					rowbean.setId(log.getId() + "");
					rowsList.add(rowbean);
				}
			}

			tablebean.setRows(rowsList);
			tablebean.setPage(this.page);
			int records = tablebean.getRecords();
			tablebean.setTotal(records % rows == 0 ? records / rows : records
					/ rows + 1);

		} catch (Exception e) {
			logger.getLogger(LogOperateAction.class).warn(e.getMessage());
		}
		return "tab";
	}

	/**
	 * 自动删除任务状态(启用，停用)
	 * 
	 * @return
	 */
	public String deleteStatus() {
		String taskLogDelete = Configurater.getInstance().getConfigValue("taskList.properties",
				"taskLogDelete");

		String backupPath = Configurater.getInstance().getConfigValue("backupPath");// 备份路径
		if (taskLogDelete != null && !"".equals(taskLogDelete)) {// 是否有备份配置

			String[] logDeleteInfo = taskLogDelete.split(",");
			String time = logDeleteInfo[0];// 执行时间
			String period = logDeleteInfo[1];// 执行周期
			String status = logDeleteInfo[4];// 执行状态
			if (status.equals("false")) {// 任务未启动
				obj = new Object[] { "error", backupPath };
			} else {
				int per = Integer.parseInt(period) / (24 * 60);
				String circle = "d";// 天
				if (per >= 30) {
					per = per / 30;
					circle = "m";// 月
				}

				ServletContext application = ServletActionContext.getRequest()
						.getSession().getServletContext();// 删除范围

				obj = new Object[] { "success", time, circle, per,
						(String[]) application.getAttribute("logDeleteScope"),
						backupPath };
			}
		} else {
			obj = new Object[] { "error", backupPath };
		}

		return "status";
	}

	/**
	 * 手动删除日志备份文件
	 * 
	 * @return
	 */
	public String handDelete() {

		try {
			Logger logger = super.logger.getLogger(LogServiceImpl.class);
			logger.info("日志删除，手动删除");

			ActionContext ac = ActionContext.getContext();
			ServletContext sc = (ServletContext) ac
					.get(ServletActionContext.SERVLET_CONTEXT);
			String savePath = sc.getRealPath("/");// 工程路径
			String backupPath = Configurater.getInstance().getConfigValue("backupPath");// 备份路径

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("timeFrom", timeFrom);
			map.put("timeTo", timeTo);

			if (scope != null && scope.length > 0) {
				map.put("in", scope);

				// 将查询要备份的数据放入bean中
				List<TLogFile> list = logService.logDelList(map);
				for (TLogFile log : list) {
					String filePath = savePath + backupPath + "\\"
							+ log.getFileName();
					logger.info("path=====" + filePath);
					File f = new File(filePath);
					if (f.exists()) {// 如果文件存在，则删除文件
						f.delete();
					}

					log.setTUserByDeleteOperator(super.getLoginUser());
					log
							.setOperateTime(new Timestamp(System
									.currentTimeMillis()));

					logService.updateEntity(log);// 删除日志表记录
				}

				// 插入日志表
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy年MM月dd日HH时mm分ss秒");

				TOperateLog tlog = new TOperateLog();
				tlog.setOperateTime(new Timestamp(System.currentTimeMillis()));
				tlog.setTarget(TUser.class.getSimpleName());
				tlog.setTUser(super.getLoginUser());
				tlog.setType("L46");
				tlog.setContent(super.getLoginUser().getName() + ","
						+ format.format(new Date()) + "进行日志删除");
				logService.insertEntity(tlog);

				returnValue = "success";
			} else {
				returnValue = "error";
			}

		} catch (Exception e) {
			logger.getLogger(LogOperateAction.class).warn(e.getMessage());
		}
		return "delete";
	}

	/**
	 * 日志删除--自动删除
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String autoDelete() throws UnsupportedEncodingException {
		taskManager = new TaskManager();
		if (status != null && !"".equals(status)) {
			if (scope == null || scope.length == 0) {
				returnValue = "error";// 未选择删除范围
				return "delete";
			} else {// 将删除范围放入application
				ServletContext application = ServletActionContext.getRequest()
						.getSession().getServletContext();// 备份范围
				application.setAttribute("logDeleteScope", scope);
			}
		}

		String task = Configurater.getInstance()
				.getConfigValue("taskList.properties","taskLogDelete");

		String[] taskInfo = task.split(",");
		String taskTime = taskInfo[0];// 执行时间
		String taskPeriod = taskInfo[1];// 执行周期
		String taskClazz = taskInfo[2];// 执行业务逻辑
		String taskName = taskInfo[3];// 执行任务名称，对应timerMap中的key
		String taskStatus = taskInfo[4];// 执行状态

		String path = Thread.currentThread().getContextClassLoader()
				.getResource("taskList.properties").getPath();
		path = java.net.URLDecoder.decode(path, "utf-8");

		taskTime = time;
		if (circle.equals("d")) {
			taskPeriod = String.valueOf(Integer.parseInt(amount) * 24 * 60);
		} else {
			taskPeriod = String
					.valueOf(Integer.parseInt(amount) * 24 * 60 * 30);
		}

		if (status == null || "".equals(status)) {// 停用自动备份
			taskStatus = "false";

			task = taskTime + "," + taskPeriod + "," + taskClazz + ","
					+ taskName + "," + taskStatus;
			PropertyModify.writeProperties(path, "taskLogDelete", task);// 修改配置文件
			try {// 重新加载配置文件
				Configurater.getInstance().loadConfigure(
						ConstConfig.TASK_LIST, "taskList.properties");
			} catch (IOException e) {
				logger.getLogger(LogOperateAction.class).warn(e.getMessage());
			}

			taskManager.stop("taskLogDelete");// 终止任务
		} else {// 启用自动备份
			taskStatus = "true";

			task = taskTime + "," + taskPeriod + "," + taskClazz + ","
					+ taskName + "," + taskStatus;
			PropertyModify.writeProperties(path, "taskLogDelete", task);// 修改配置文件
			try {// 重新加载配置文件
				Configurater.getInstance().loadConfigure(
						ConstConfig.TASK_LIST, "taskList.properties");
			} catch (IOException e) {
				logger.getLogger(LogOperateAction.class).warn(e.getMessage());
			}

			taskManager.start("taskLogDelete", time, circle, status);// 重新启动任务
		}

		returnValue = taskStatus;
		return "delete";
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public TableBean getTablebean() {
		return tablebean;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public void setLogService(IlogService logService) {
		this.logService = logService;
	}

	public void setSortSearchesService(IsortSearchesService sortSearchesService) {
		this.sortSearchesService = sortSearchesService;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Object[] getObj() {
		return obj;
	}

	public void setObj(Object[] obj) {
		this.obj = obj;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

}
