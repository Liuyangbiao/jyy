package com.nsc.dem.util.task;

import static com.nsc.base.util.DateUtils.DateToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nsc.base.conf.Configurater;
import com.nsc.base.excel.ExportUtil;
import com.nsc.base.task.TaskBase;
import com.nsc.base.util.Component;
import com.nsc.base.util.DateUtils;
import com.nsc.dem.action.bean.LogFileBean;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TLogFile;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.system.IlogService;

public class BackupTask extends TaskBase implements Job{

	private IlogService logService;
	private TUser user = null;
	private Logger logger = null;

	public BackupTask(String taskName,ServletContext context,long period) {
		super(taskName,context,period);
		logService = (IlogService) Component.getInstance("logService",
				super.context);
		user = (TUser) logService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = logService.getLogManager(user).getLogger(
				BackupTask.class);
	}
	
	public BackupTask(){
		super(null,Configurater.getInstance().getServletContext(),0);
		logService = (IlogService) Component.getInstance("logService",
				super.context);
		user = (TUser) logService.EntityQuery(TUser.class, Configurater
				.getInstance().getConfigValue("ws_user"));
		logger = logService.getLogManager(user).getLogger(
				BackupTask.class);

	}
	public void execute(JobExecutionContext context) throws JobExecutionException{
		String taskName = context.getTrigger().getKey().getName();
		logger.info("任务[ " + taskName + " ]启动于 "
					+ DateToString(context.getFireTime(), "yyyy-MM-dd HH:mm:ss"));
		try {
			doTask();
		} catch (Exception e) {
			throw new JobExecutionException(e);
		} finally{
			logger.info("任务[ "+ taskName+ " ]下次将于"
						+ DateToString(context.getNextFireTime(),"yyyy-MM-dd HH:mm:ss") + " 启动");
		}	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doTask() throws Exception {
		logger.info(
				"任务 BackupTask 已经启动于 "
						+ DateUtils.DateToString(new Date(),
								"yyyy-MM-dd HH:mm:ss"));

		// 根据配置文件自动备份日志
		String taskBackup = Configurater.getInstance().getConfigValue(
				"taskBackup");
		Integer period = Integer.parseInt(taskBackup.split(",")[1]);// 执行周期

		// 取出备份时间，备份范围为当前时间往前移一个备份周期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -period / (60 * 24));

		String timeTo = DateUtils.DateToString(new Date(), "yyyy-MM-dd");// 结束时间
		String timeFrom = DateUtils.DateToString(cal.getTime(), "yyyy-MM-dd");// 开始时间
		String[] backupScope = (String[]) super.context.getAttribute("backupScope");//备份范围
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("timeFrom",timeFrom);
		map.put("timeTo", timeTo);
		
		if(backupScope==null || backupScope.length==0){
			//application中无备份范围，则备份所有日志分类
			TDictionary dic=new TDictionary();
			dic.setParentCode("RZFL");
			List<Object> dicList=logService.EntityQuery(dic);
			
			backupScope=new String[dicList.size()];
			for(int i=0;i<dicList.size();i++){
				TDictionary di=(TDictionary)dicList.get(i);
				backupScope[i]=di.getCode();
			}
		}
		
		TUser admin=(TUser)logService.EntityQuery(TUser.class, "admin");
	    for(String type:backupScope){
			map.put("type", type);
			
			Long i=1L;
			List<LogFileBean> dataset = new ArrayList<LogFileBean>();
			
			List<Object[]> list=logService.logBackupHand(map);
			for(Object[] obj:list){
				TOperateLog log=(TOperateLog)obj[0];
				TUser user=(TUser)obj[1];
				TDictionary dic=(TDictionary)obj[2];
				
				LogFileBean logfile=new LogFileBean();
				logfile.setId(i);
				logfile.setTarget(log.getTarget());
				logfile.setContent(log.getContent());
				logfile.setType(dic.getName());
				logfile.setOperate(user.getName());
				logfile.setOperateTime(log.getOperateTime());
				
				dataset.add(logfile);
				i++;
			}
			
			if(dataset!=null && dataset.size()>0){
				String savePath = super.context.getRealPath("/");
				
				String backupPath=Configurater.getInstance().getConfigValue("backupPath");//备份路径
				savePath = savePath + backupPath+"\\";
				
				File f = new File(savePath);   
			    if (!f.exists()) {//如果目录不存在则创建该目录
			        f.mkdirs();   
			    }
				
				ExportUtil<LogFileBean> ex = new ExportUtil<LogFileBean>();
				String[] headers = { "序号", "对象", "日志分类" , "内容" , "操作人" , "操作日期" };

				OutputStream out;
				try {
					SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
					String fileName="log_bak_"+sdf.format(new Date())+"_"+System.currentTimeMillis()+".xls";
					savePath+=fileName;
					logger.info("备份路径路径：------->"+savePath);
					
					out = new FileOutputStream(savePath);
					
					ex.exportExcel(headers, dataset, out);
					
					out.flush();
					out.close();

					logger.info("excel导出成功！");
					 
					//插入备份表
					TLogFile logfile=new TLogFile();
					logfile.setBackupPath(backupPath);
					logfile.setFileName(fileName);
					logfile.setType(type);
					logfile.setBackupTime(new Timestamp(System.currentTimeMillis()));
					logfile.setTUserByBackupOperator(admin);
					logService.insertEntity(logfile);
					
					 //插入日志表
					SimpleDateFormat format = new SimpleDateFormat(
					"yyyy年MM月dd日HH时mm分ss秒");

					TOperateLog tlog = new TOperateLog();
					tlog.setOperateTime(new Timestamp(System.currentTimeMillis()));
					tlog.setTarget(TUser.class.getSimpleName());
					tlog.setTUser(admin);
					tlog.setType("L45");
					tlog.setContent(admin.getName() + ","+ format.format(new Date()) + "进行日志备份");
					logService.insertEntity(tlog);
					logService.deleteLog(map);
				} catch (FileNotFoundException e) {
					logger.warn(e);
				} catch (IOException e) {
					logger.warn(e);
				}
			}
		}
	}
}
