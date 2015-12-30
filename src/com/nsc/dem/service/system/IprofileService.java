package com.nsc.dem.service.system;

import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.system.TMenu;
import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.profile.TUserProfile;
import com.nsc.dem.bean.system.TUserShortcut;
import com.nsc.dem.service.base.IService;

public interface IprofileService extends IService {

	/**
	 * 权限管理--权限定义--查询列表 需要根据查询条件从权限定义表中获取权限信息列表，
	 * 
	 * @param 参数为权限定义表实体类
	 * @return
	 */
	public List<Object> profileInfoList();

	/**
	 * 权限管理--角色授权--查询列表 需要根据查询条件从角色权限表中获取权限信息列表，
	 * 
	 * @param 参数为权限定义表实体类
	 * @return
	 */
	public List<Object> roleProfileInfoList();

	/**
	 * 权限管理--用户授权--查询列表 需要根据查询条件从用户权限表中获取权限信息列表，
	 * 
	 * @param 参数为用户权限表实体类
	 * @return
	 */
	public List<Object> userProfileInfoList();

	/**
	 * 获得用户所有的权限菜单
	 */
	public List<TMenu> queryUserAllProList(String userId, String node,
			String menuType,String roleId,String isLocal);

	public List<TUserShortcut> queryShortcutByUser(String userId);

	/**
	 * 根据权限类型查询所有授权信息
	 */
	public List<TProfile> getTProfileByType(String type);

	/**
	 * 权限管理信息 分页查询
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
	public List<Object[]> queryTProfileInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 删除授权及授权的相关信息
	 * 
	 * @param profileId
	 */
	public void deleteTProfileInfo(IprofileService profileService,
			String profileId);

	/**
	 * 角色授权信息 分页查询
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
	public List<Object[]> queryTRoleProfileInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 删除角色授权信息
	 * 
	 * @param roleId
	 * @param profileId
	 */
	public void deleteTRoleProfile(String roleId, String profileId);

	/**
	 * 删除用户授权信息
	 * 
	 * @param userId
	 * @param profileId
	 */
	public void deleteTUserProfile(String userId, String profileId);

	/**
	 * 根据角色ID，授权ID查询角色的信息
	 * 
	 * @param roleId
	 * @param profileId
	 * @return
	 */
	public List<TRoleProfile> getRoleProfileByRoleIdandProfileId(String roleId,
			String profileId);

	public List<TUserProfile> getUserProfileByRoleIdAndProfileId(String roleId,
			String profileId);

	/**
	 * 根据角色ID，权限分类查询授权信息
	 * 
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<TProfile> getTProfileByroleIdandProfileId(String roleId,
			String type);

	/**
	 * 用户授权信息 分页查询
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
	public List<Object[]> queryTUserProfileInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 根据角色ID查询所有用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<TUser> queryAllUserList(String roleId);

	/**
	 * 根据用户查询未控权的对象
	 * 
	 * @param userId
	 * @return
	 */
	public List<TProfile> getTProfileByUserId(String userId);

	/**
	 * 查看权限
	 * 
	 * @param userId
	 *            用户ID
	 * @param profileId
	 *            权限ID
	 * @param site
	 *            位置
	 * @return boolean
	 */
	public boolean getProfileByauthControl(TUser user, String profileId,
			String siteName);
}
