package com.nsc.dem.service.system;

import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TProfileTemp;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.base.IService;

public interface IprofileTempService  extends IService{
	

	// /**
	// * 临时权限管理--查询列表
	// * 需要根据查询条件从临时权限中获取临时权限信息列表，
	// * @param 参数为临时权限表实体类
	// * @return
	// */
	// public List<Object> profileTempInfoList();
	
	/**
	 * 查询已授权信息分页
	 * 
	 * @param map
	 *            条件
	 * @param firstResult
	 *            开始页数
	 * @param maxResults
	 *            结尾页数
	 * @param table
	 * @return Object[]
	 * @throws Exception
	 */
	public List<Object[]> queryYiProfileInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;
	
	
	/**
	 * 查询数据授权信息分页
	 * 
	 * @param map
	 *            条件
	 * @param firstResult
	 *            开始页数
	 * @param maxResults
	 *            结尾页数
	 * @param table
	 * @return Object[]
	 * @throws Exception
	 */
	public List<Object[]> queryDataProfileList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 根据角色ID，权限分类查询授权信息
	 * 
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<Object[]> getTProfileTempByroleIdandProfileId(Map<String,Object> map);

	/**
	 * 根据角色ID，授权ID查询角色的信息
	 * 
	 * @param roleId
	 * @param profileId
	 * @return
	 */
	public List<TProfileTemp> getRoleProfileTempByRoleIdandProfileId(
			String roleId, String profileId);

	/**
	 * 根据角色查询用户信息
	 * 
	 * @param roleId
	 * @return
	 */
	public List<TUser> getTUserByRoleId(String roleId);

	/**
	 * 根据ID查询信息
	 * @param map
	 * @return
	 */
	public List<Object[]> updateYiShouQuan(String id);
	
	/**
	 * 根据ID查询授权信息
	 * @param map
	 * @return
	 */
	public List<Object[]> updateDataProfile(String id);
}
