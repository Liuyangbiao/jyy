package com.nsc.dem.service.system.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TLogFile;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.bean.system.TOperateLogTemp;
import com.nsc.dem.service.base.BaseService;
import com.nsc.dem.service.system.IlogService;

@SuppressWarnings("unchecked")
public class LogServiceImpl extends BaseService implements IlogService{

	
	public List<TLogFile> logDelList(Map<String,Object> map) {
		
		return super.generalDAO.getResult("logDelete",map);
	}

	public List<Object> logInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date logLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Object> logOracleInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 得到档案日志
	 */
	public List<TDictionary> docLogDicList() {
		return getLogDic("L00","L19");
	}
	
	/**
	 * 得到系统日志
	 */
	public List<TDictionary> sysLogDicList() {
		return getLogDic("L20","L99");
	}
	
	/**
	 * 查询数据字典 日志分类
	 * @param begin  开始 code
	 * @param end    结束 code
	 * @return
	 */
	private List<TDictionary> getLogDic(String begin,String end){
		String hqlChild = "from TDictionary as td where td.parentCode = 'RZFL' and td.code between '"+begin+"' and '"+end+"'";
		List<TDictionary> listChild=generalDAO.queryByHQL(hqlChild);
		return listChild;
	}
	
	/**
	 * 日志 分页 查询
	 */
	public List queryOperateLogList(Object[] obj, int firstResult,
			int maxResults, TableBean table) throws Exception {
		Long count=super.generalDAO.getResultCount("operateLogSearch",obj);
		table.setRecords(count.intValue());

		return count.intValue()==0?null:super.generalDAO.getResult("operateLogSearch",obj,firstResult,maxResults);
	}


	/**
	 * 日志管理--日志备份 (手动备份)
	 * @param  
	 */
	public List<Object[]> logBackupHand(Map<String,Object> map) {
		
		List<Object[]> list=super.generalDAO.getResult("backup",map);
		
		return list;
		
	}


	public List<Object[]> queryBackupList(Map<String,Object> map,int firstResult, int maxResults, TableBean table) throws Exception {
		Long count=super.generalDAO.getResultCount("backupList",map);
		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"backupList", map, firstResult, maxResults);
		
	}


	public List<Object[]> queryLogDeleteList(Map<String,Object> map,int firstResult, int maxResults, TableBean table) throws Exception {
		Long count=super.generalDAO.getResultCount("logDeleteList",map);
		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"logDeleteList", map, firstResult, maxResults);
		
	}

	/*
	 * 删除备份过的日志
	 */
	public void deleteLog(Map<String,Object> map) {
		String sql="delete from t_operate_log t where " +
				"to_timestamp(to_char(t.operate_time,'yyyy-mm-dd'),'yyyy-mm-dd')>=nvl(to_timestamp('"+map.get("timeFrom")+"','yyyy-mm-dd'),to_timestamp(to_char(t.operate_time,'yyyy-mm-dd'),'yyyy-mm-dd')) "        
                +"and to_timestamp(to_char(t.operate_time,'yyyy-mm-dd'),'yyyy-mm-dd')<=nvl(to_timestamp('"+map.get("timeTo")+"','yyyy-mm-dd'),to_timestamp(to_char(t.operate_time,'yyyy-mm-dd'),'yyyy-mm-dd')) "
	    		+"and t.type=nvl('"+map.get("type")+"',t.type)";
	
		super.generalDAO.deleteAll(sql);
	}

	/**
	 * 按类型查询日志
	 */
	public List<TOperateLog> findOperateLogByType(String typeName) {
		// TODO Auto-generated method stub
		return super.generalDAO.getResult("queryOperateLogByType", new Object[]{typeName});
	}

	public List<TOperateLogTemp> findOperateTempLog(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return super.generalDAO.getResult("queryOperateLogTempList",map);
	}
	
	/**
	 * 删除所有的日志临时表数据
	 */
	public void deleteAllTempOperateLog(){
		super.generalDAO.deleteAll(" delete from t_operate_log_temp ");
	}
}
