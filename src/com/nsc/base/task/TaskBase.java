package com.nsc.base.task;

import java.util.Date;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.nsc.base.util.DateUtils;

public abstract class TaskBase extends TimerTask {

	protected ServletContext context;
	protected long period;
	protected String taskName;

	protected TaskBase(String taskName,ServletContext context, long period) {
		this.context = context;
		this.period = period;
		this.taskName=taskName;
	}

	@Override
	public void run() {
		try {
			doTask();
			long nextTime = super.scheduledExecutionTime() + period;
			Date date = new Date(nextTime);
			Logger.getLogger(TaskBase.class).info(
					taskName+" 下次执行时间: "
							+ DateUtils.DateToString(date,
									"yyyy-MM-dd HH:mm:ss"));
		} catch (Exception ex) {
			Logger.getLogger(TaskBase.class).warn("异常", ex);
		}
	}

	protected abstract void doTask() throws Exception;

}
