package com.nsc.base.task;

import java.util.Date;

import javax.servlet.ServletContext;

import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.ee.servlet.QuartzInitializerListener;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import com.nsc.base.util.DateUtils;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class QuartzTaskManager extends TaskManager{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5609370069790373554L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private ServletContext cx;
	
	/**
	 * 放入计划中
	 * @param time 启动时间
	 * @param period 运行周期 (分钟)
	 * @param clazz 任务执行类
	 * @param taskName 任务名称
	 * @param status 启用标识
	 */
	public void doSchedule(String time, String period, String clazz,
			String taskName, String status) {
		// First we must get a reference to a scheduler
		cx=super.getServletContext();
		
		try{
	        SchedulerFactory sf = (SchedulerFactory)cx.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
	        Scheduler sched = sf.getScheduler();
	
	        // computer a time that is on the next round minute
	        int hour=Integer.parseInt(time.split(":")[0]);
	        int minute=Integer.parseInt(time.split(":")[1]);
	        int interval=Integer.parseInt(period);
	        
	        Date runTime = DateBuilder.todayAt(hour,minute,0);
	        //Date runTime = evenMinuteDate(new Date());
	
	        log.info("------- 排定任务  "+taskName+" -------------------");
	        Class<Job> jobClass = (Class<Job>) Class.forName(clazz);
	        // define the job and tie it to our HelloJob class
	        JobDetail job = newJob(jobClass)
	            .withIdentity(taskName, "edm")
	            .build();
	        
	        // Trigger the job to run on the next round minute
	        Trigger trigger = newTrigger()
	            .withIdentity(taskName, "edm")
	            .startAt(runTime)
	            .withSchedule(simpleSchedule()
	                    .withIntervalInMinutes(interval)
	                    .repeatForever())
	            .build();
	        
	        // Tell quartz to schedule the job using our trigger
	        sched.scheduleJob(job, trigger);
	        
	        //暂停状态为false的任务
	        if(status=="false"){
	        	sched.pauseTrigger(trigger.getKey());
	        }
	        log.info(job.getKey() + " 将运行在: " + DateUtils.DateToString(runTime,"yyyy-MM-dd HH:mm:ss"));
		} catch (Exception ex) {
			log.warn("任务[ "+ taskName +" ]设置期间发生错误：", ex);
		}
	}
	
	/**
	 * 启用任务
	 */
	public void start(String taskName, String time, String period, String status){
		SchedulerFactory sf = (SchedulerFactory)cx.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
		
		try{
	        Scheduler sched = sf.getScheduler();
	        TriggerKey triggerKey=new TriggerKey(taskName, "edm");
	        sched.resumeTrigger(triggerKey);
		} catch (Exception ex) {
			log.warn("任务[ "+ taskName +" ]设置期间发生错误：", ex);
		}
	}
	
	public void stop(String taskName) {
		SchedulerFactory sf = (SchedulerFactory)cx.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
		
		try{
	        Scheduler sched = sf.getScheduler();
	        TriggerKey triggerKey=new TriggerKey(taskName, "edm");
	        sched.pauseTrigger(triggerKey);
		} catch (Exception ex) {
			log.warn("任务[ "+ taskName +" ]设置期间发生错误：", ex);
		}
	}
}
