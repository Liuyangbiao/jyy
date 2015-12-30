package com.nsc.dem.service.system;

import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TProfileTemp;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TRoleTree;
import com.nsc.dem.service.base.IService;

public interface IroleService extends IService {

	/**
	 * 角色管理--查询列表 需要根据查询条件从角色表中获取角色信息列表，
	 * 
	 * @param 参数为角色表实体类
	 * @return
	 */
	public List<Object> roleInfoList();

	/**
	 * 角色管理--删除
	 * 
	 * @param 参数为角色表主键数组
	 */
	public void roleDel(String role[]);

	/**
	 * 根据现有权限获得默认权限字符串
	 * 
	 * @param field
	 *            字段名称
	 * @return
	 */
	public List<Map<String, Object>> getRolePriority(Object entity,
			String field, List<TRole> tRoleList);

	/**
	 * 根据字段名查询出相对应权限
	 * 
	 * @param field
	 */
	public String getfieldSelectAuthControl(String field);

	/**
	 * 查询所有的角色
	 * 
	 * @return
	 */
	public List<TRole> roleList();

	/**
	 * 角色信息管理 分页查询
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
	public List<Object[]> queryRoleInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 根据角色ID查询所有使用角色的用户
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TUser> getUserByRoleId(String roleid);

	/**
	 * 根据角色ID查询树形角色
	 * 
	 * @param id
	 * @return
	 */
	public List<TRoleTree> getRoleIdByTRoleTreeId(String id);

	/**
	 * 根据角色ID查询快捷菜单
	 * 
	 * @param id
	 * @return
	 */
	public List<TRoleProfile> getRoleIdByTRoleProFileId(String id);

	/**
	 * 
	 *根据角色ID查询 数据字典表
	 * 
	 * @param id
	 * @return
	 */
	public List<TDictionary> getRoleIdByTDictionaryAuthControl(String roleid);

	/**
	 * 根据角色ID查询文档分类表
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TDocType> getRoleIdByTDocTypePrivilege(String roleid);

	/**
	 * 根据角色ID查询临时授权表
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TProfileTemp> getRoleIdByTProfileTempRole(String roleid);

	/**
	 * 删除角色信息
	 * 
	 * @param roleId
	 */
	public void deleteRoleInfo(IroleService roleService, String roleId);

	/**
	 * 查询用户角色
	 * 
	 * @param roleId
	 * @return
	 */
	public List<TRole> queryTRoleList(String roleId);
}
