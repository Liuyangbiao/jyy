package com.nsc.base.task;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.DateUtils;

@SuppressWarnings("all")
public class TaskManager extends HttpServlet {

	private static final long serialVersionUID = 930742281559699951L;

	private static Map<String, Timer> timerMap = new HashMap<String, Timer>();

	private static ServletContext context;

	// init方法启动定时器
	public void init() throws ServletException {

		context = getServletContext();

		// (true为用定时间刷新缓存)
		String startTask = getInitParameter("startTask");

		// 周期 单位：小时
		// Long period = Long.parseLong(getInitParameter("period"));

		// 启动定时器
		if (startTask.equals("true")) {
			Configurater con = Configurater.getInstance();
			Properties pros = Configurater.getInstance().getConfig("taskList");
			Set set = pros.stringPropertyNames();
			for (Object obj : set) {
				String task = (String)pros.get(obj);
				String[] taskInfo = task.split(",");
				String time = taskInfo[0];// 执行时间
				String period = taskInfo[1];// 执行周期
				String clazz = taskInfo[2];// 执行业务逻辑
				String taskName = taskInfo[3];// 执行任务名称，对应timerMap中的key
				String status = taskInfo[4];// 执行状态
				if ("true".equals(status)) {
					doSchedule(time, period, clazz, taskName, status);
				}
			}
		}
	}

	/**
	 * 启用定时器
	 */
	public void start(String taskKey, String time, String period, String status) {
		String task = Configurater.getInstance().getConfigValue("taskList.properties",taskKey);

		String[] taskInfo = task.split(",");

		String taskTime = taskInfo[0];// 执行时间
		String taskPeriod = taskInfo[1];// 执行周期
		String taskClazz = taskInfo[2];// 执行业务逻辑
		String taskName = taskInfo[3];// 执行任务名称，对应timerMap中的key
		String taskStatus = taskInfo[4];// 执行状态

		Timer timer = timerMap.get(taskKey);
		if (timer != null) {// 任务已存在，先清除再添加
			timer.cancel();
			Logger.getLogger(TaskManager.class).info("取消任务设置");
			timerMap.remove(taskKey);
			doSchedule(taskTime, taskPeriod, taskClazz, taskName, taskStatus);
		} else {// 添加任务
			doSchedule(taskTime, taskPeriod, taskClazz, taskName, taskStatus);
		}
	}

	/**
	 * 停止定时器
	 */
	public void stop(String taskKey) {
		Timer timer = timerMap.get(taskKey);
		if (timer != null) {// 如果任务已存在就清除
			timer.cancel();
			Logger.getLogger(TaskManager.class).info("取消任务设置");
			timerMap.remove(taskKey);
		}
	}

	/**
	 * 放入计划中
	 * @param time 启动时间
	 * @param period 运行周期
	 * @param clazz 任务执行类
	 * @param taskName 任务名称
	 * @param status 启用标识
	 */
	public void doSchedule(String time, String period, String clazz,
			String taskName, String status) {

		Date now = new Date();
		String today = DateUtils.getDate(now);

		Timer timer = new Timer(true);

		String runTime = today + " " + time;

		Date runDate = DateUtils.StringToDate(runTime, "yyyy-MM-dd HH:mm");

		// 在此时之前，推迟到明天
		runDate = runDate.after(now) ? runDate : DateUtils.getDayAfterDays(
				runDate, 1);

		// 时间差-延迟时间-秒
		long delay = runDate.getTime() - now.getTime();

		try {
			Logger.getLogger(TaskManager.class).info(
					"正在设置任务["
							+ clazz
							+ "] "
							+ DateUtils.DateToString(runDate,
									"yyyy-MM-dd HH:mm:ss"));
			Class runClass = Class.forName(clazz);

			Constructor<TaskBase> cons = runClass.getConstructor(String.class,
					ServletContext.class, long.class);

			TaskBase taskObj = cons.newInstance(taskName,context, 60 * 1000 * Integer
					.parseInt(period));

			timer
					.schedule(taskObj, delay, 60 * 1000 * Integer
							.parseInt(period));// period 为小时

			// 将timer放入timerMap中
			timerMap.put(taskName, timer);

			Logger.getLogger(TaskManager.class).info("设置成功");
		} catch (Exception ex) {
			Logger.getLogger(TaskManager.class).warn("设置期间发生错误：", ex);
			timer.cancel();// 取消任务
			Logger.getLogger(TaskManager.class).warn("取消任务设置：", ex);
		}

	}

}
