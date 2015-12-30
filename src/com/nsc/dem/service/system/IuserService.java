package com.nsc.dem.service.system;

import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TServersInfo;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.base.IService;


public interface IuserService extends IService {

	/**
	 * 用户管理查询列表 需要从用户表中获取用户列表信息，
	 * 
	 * @param 参数为用户实体类
	 * @return
	 */
	public List<Object> userInfoList();

	/**
	 * 用户管理--删除
	 * 
	 * @param 参数为用户表主键数组
	 */
	public void userDel(String user[]);

	/**
	 * 单位信息分页查询
	 * 
	 * @param map
	 * @param firstResult
	 *            开始记录行数
	 * @param maxResults
	 *            结束记录行数
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> queryUnitInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	public List<TUnit> unitList();
	public List<TUnit> unitAllList(String type);

	public List<TUnit> designList();

	/**
	 * 用户管理信息 分页查询
	 * 
	 * @param map
	 * @param firstResult
	 *            开始记录行数
	 * @param maxResults
	 *            结束记录行数
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> queryUserInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	
	public List<TUser> getUserByUnitCode(String unitCode);
	
	public List<TServersInfo> getServersInfoByCode(String unitCode);
	
	public List<TServersInfo> findAllServersInfo();
}
