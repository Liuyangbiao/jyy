package com.nsc.dem.service.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TLogFile;
import com.nsc.dem.bean.system.TOperateLogTemp;
import com.nsc.dem.service.base.IService;

public interface IlogService extends IService {
	
	
	/**
	 * 日志管理--日志查看--系统日志 
	 * 需要根据查询条件从操作日志表中获取日志列表信息
	 * @param  参数为日志实体类
	 * @return
	 */
	public List<Object> logInfoList();
	/**
	 * 日志管理--日志查看--档案日志 
	 * 需要查询出oracle自动记录的日志信息
	 * @param  参数为日志实体类
	 * @return
	 */
	public List<Object> logOracleInfoList();
	
	
	/**
	 * 日志管理--日志备份 (手动备份)
	 */
	@SuppressWarnings("unchecked")
	public List logBackupHand(Map<String,Object> map);
	
	/*
	 * 删除备份过的日志
	 */
	public void deleteLog(Map<String,Object> map);
	
	/**
	 * 日志管理--日志删除 (查询=删除立标)
	 * @param  
	 */
	public List<TLogFile> logDelList(Map<String,Object> map);
	
	/**
	 * 日志信息 -- 文档最近更新日期 
	 * @param  
	 */
	public Date logLastUpdate();
	
	/**
	 * 档案日志数据字典
	 * @return
	 */
	public List<TDictionary> docLogDicList();
	
	/**
	 * 系统日志数据字典
	 * @return
	 */
	public List<TDictionary> sysLogDicList();
	@SuppressWarnings("unchecked")
	public List queryOperateLogList(Object[] obj,int firstResult,int maxResults,TableBean table) throws Exception;

	
	List<Object[]> queryBackupList(Map<String,Object> map,int firstResult,int maxResults,TableBean table)throws Exception;
	
	List<Object[]> queryLogDeleteList(Map<String,Object> map,int firstResult,int maxResults,TableBean table)throws Exception;

	/**
	 * 按日志类型查询日志
	 * @param typeName
	 * @return
	 */
	
	
	public List<TOperateLogTemp> findOperateTempLog(Map<String,Object> map);
	
	/**
	 * 删除所有的日志临时表数据
	 */
	public void deleteAllTempOperateLog();
}
