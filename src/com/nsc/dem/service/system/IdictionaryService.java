package com.nsc.dem.service.system;

import java.util.List;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.base.IService;

public interface IdictionaryService extends IService {
	
	/**
	 * 	数据字典管理--查询列表    需要根据查询条件从数据字典表中获取数据字典列表信息
	 * @param  参数为数据字典实体类
	 * @return
	 */
	public List<Object> dictionaryInfoList();
	
	public List<Object[]> dicSourceInfoList(String sql);
	
	/**
	 * 	数据字典管理--删除
	 * @param  参数为数据字典主键数组
	 */
	public void dictionaryDel(String dictionary[]);
	
	/**
	 * 	数据字典管理--根据数据编码规则获取分类信息
	 * @param  参数为数据字典主键数组
	 */
	public List<TDictionary> dictionaryList(String name);
	
	
	/**
	 * 数据字典管理分页查询
	 * 
	 * @param map
	 * @param firstResult
	 *            开始记录行数
	 * @param maxResults
	 *            结束记录行数
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> querydictionaryList(Object[] obj,int firstResult,int maxResults,TableBean table,String flag) throws Exception;
	
}
