package com.nsc.dem.service.system.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nsc.base.conf.Configurater;
import com.nsc.base.conf.ConstConfig;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.system.TMenu;
import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TProfileTemp;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.profile.TUserProfile;
import com.nsc.dem.bean.system.TUserShortcut;
import com.nsc.dem.service.base.BaseService;
import com.nsc.dem.service.system.IprofileService;

@SuppressWarnings("unchecked")
public class ProfileServiceImpl extends BaseService implements IprofileService {
	// 菜单权限
	public static String MENU_KEY = "P01";
	// 页面权限
	public static String PAGE_KEY = "P02";
	// 功能权限
	public static String FUN_KEY = "P03";

	/**
	 * 权限管理--权限定义--查询列表 需要根据查询条件从权限定义表中获取权限信息列表，
	 * 
	 * @param 参数为权限定义表实体类
	 * @return
	 */
	public List<Object> profileInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 权限管理--角色授权--查询列表 需要根据查询条件从角色权限表中获取权限信息列表，
	 * 
	 * @param 参数为权限定义表实体类
	 * @return
	 */
	public List<Object> roleProfileInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 权限管理--用户授权--查询列表 需要根据查询条件从用户权限表中获取权限信息列表，
	 * 
	 * @param 参数为用户权限表实体类
	 * @return
	 */
	public List<Object> userProfileInfoList() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 *
	 * 获得用户所有的权限菜单
	 */
	public List<TMenu> queryUserAllProList(String userId, String node,
			String menuType, String roleId, String isLocal) {

		// List<TProfile> resultList = new ArrayList();
		List menuList = new ArrayList();

		// 得到用户的角色ID user.getTRole().getId()
		// TUser user = (TUser) super.EntityQuery(TUser.class, userId);

		menuList = setMenu(userId, null, menuType, roleId, isLocal);
		return menuList;
	}

	/**
	 * 获得用户所有的权限菜单
	 * @param userId 用户ID
	 * @param node 节点
	 * @param menuType
	 *        权限分类 01：模块权限 02：菜单权限
	 * @param roleId 角色ID
	 * @param isLocal 是否是远程登录用户
	 * @return
	 */
	private List<TMenu> setMenu(String userId, String node, String menuType,
			String roleId, String isLocal) {
		List<Map<String, Object>> listall = new ArrayList<Map<String, Object>>();
		String str = "";
		if (node != null) {
			str = " AND T2.REMARK1='" + node + "'";
		}
		// 如果传来的用户是远程用户，只菜单权限只查询角色授权
		String sql = "SELECT T2.* FROM T_Role_Profile T1, T_Profile T2 WHERE T1.profile_id=T2.id AND T1.role_id='"
				+ roleId
				+ "'"
				+ " AND T2.TYPE='"
				+ menuType
				+ "'"
				+ " AND T2.TYPE !='"
				+ FUN_KEY
				+ "'"
				+ str
				+ " order by t2.sort_no";
		List<Map<String, Object>> list = super.generalDAO
				.queryByNativeSQL2(sql);

		// 用户权限
		// TUserProfile tUserProfile = new TUserProfile();
		
		sql = "SELECT T2.* FROM T_User_Profile T1, T_Profile T2 WHERE T1.profile_id=T2.id AND T1.user_id='"
				+ userId
				+ "'"
				+ " AND T2.TYPE='"
				+ menuType
				+ "'"
				+ " AND T2.TYPE !='"
				+ FUN_KEY
				+ "'"
				+ str
				+ " order by t2.sort_no";

		List<Map<String, Object>> list2 = isLocal.equals("R") ? null
				: super.generalDAO.queryByNativeSQL2(sql);

		// 临时权限
		// TProfileTemp tProfileTemp = new TProfileTemp();

		sql = "SELECT T2.* FROM T_Profile_Temp T1, T_Profile T2 WHERE T1.profile_id=T2.id AND T1.user_id='"
				+ userId
				+ "'"
				+ " AND T2.TYPE='"
				+ menuType
				+ "'"
				+ "AND T2.TYPE !='"
				+ FUN_KEY
				+ "'"
				+ str
				+ " order by t2.sort_no";

		List<Map<String, Object>> list3 = isLocal.equals("R") ? null
				: super.generalDAO.queryByNativeSQL2(sql);

		if (list != null) {
			listall.addAll(list);
		}
		if (list2 != null) {
			listall.addAll(list2);
		}
		if (list3 != null) {
			listall.addAll(list3);
		}

		List menuList = new ArrayList();
		for (Map<String, Object> map : listall) {
			// List list = new ArrayList();
			TMenu menu = new TMenu();

			menu.setId((String) map.get("ID"));
			menu.setLable((String) map.get("NAME"));
			// 是否伸缩
			if (map.get("REMARK3").toString().equals(ConstConfig.REMARK3_YES)) {
				menu.setPopUp(true);
			} else {
				menu.setPopUp(false);
			}

			// 是否可用
			if (map.get("REMARK4").toString().equals(ConstConfig.REMARK4_YES)) {
				menu.setUse(true);
			} else {
				menu.setUse(false);
			}

			// Remark2代表URL
			menu.setUrl((String) map.get("REMARK2"));

			// Remark5 代表节点图片路径
			menu.setImage((String) map.get("REMARK5"));

			// Remark1 代表父菜单ID
			TProfile tpro = new TProfile();
			tpro.setRemark1((String) map.get("ID"));

			// 查询该节点下是否有子节点
			List<TProfile> proList = super.EntityQuery(tpro);

			if (proList != null && proList.size() != 0) {
				menu.setLeaf(false);
				// 递归
				List list1 = setMenu(userId, (String) map.get("ID"), "P02",
						roleId, isLocal);
				menu.setList(list1);
			} else {
				menu.setLeaf(true);
			}

			// //递归
			// List list1 = setMenu(userId,(String)map.get("ID"),"02");
			// menu.setList(list1);
			if(menu.isUse()==false){
				
			}else {
				menuList.add(menu);
			}
			
		}
		return menuList;
	}

	/**
	 * 快捷菜单
	 */
	public List<TUserShortcut> queryShortcutByUser(String userId) {

		String hql = "from TUserShortcut tus where tus.TUser.loginId = '"
				+ userId + "' order by tus.shortOrder ";

		List<TUserShortcut> list = generalDAO.queryByHQL(hql);

		for (TUserShortcut cut : list) {
			cut.getTProfile().getId();
			cut.getTProfile().getName();
			cut.getTUser().getLoginId();
		}

		// String sql
		// ="select * from TUserShortcut t where t.user_id='"+userId+"'"+" order by t.shortOrder";
		// List<TUserShortcut> list =
		// super.generalDAO.queryByNativeSQLEntity(sql,TUserShortcut.class);

		return list;
	}

	/**
	 * 根据权限类型查询所有授权信息
	 */
	public List<TProfile> getTProfileByType(String type) {
		String sql = "select * from t_profile t where t.type ='" + type + "'";

		List<TProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfile.class);

		return list;
	}

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
			int firstResult, int maxResults, TableBean table) throws Exception {

		Long count = super.generalDAO.getResultCount("profileSearch", map);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"profileSearch", map, firstResult, maxResults);
	}

	/**
	 * 删除授权及授权的相关信息
	 * 
	 * @param profileId
	 */
	public void deleteTProfileInfo(IprofileService profileService,
			String profileId) {
		List<TRoleProfile> profileList = getTRoleProFileByProfileId(profileId);
		for (int i = 0; i < profileList.size(); i++) {
			TRoleProfile roleProfile = profileList.get(i);
			profileService.delEntity(roleProfile);
		}
		List<TUserProfile> userProfileList = getTuserProfileByProfileId(profileId);
		for (int i = 0; i < userProfileList.size(); i++) {
			TUserProfile userProfile = userProfileList.get(i);
			profileService.delEntity(userProfile);
		}

		List<TProfileTemp> profileTempList = getTProfileTempByProfileId(profileId);
		for (int i = 0; i < profileTempList.size(); i++) {
			TProfileTemp profileTemp = profileTempList.get(i);
			profileService.delEntity(profileTemp);
		}

		TProfile profile = (TProfile) profileService.EntityQuery(
				TProfile.class, profileId);
		if (getTProfileByRemark(profile.getId()).size() != 0) {
			List<TProfile> pro = getTProfileByRemark(profile.getId());
			for (int i = 0; i < pro.size(); i++) {
				TProfile tempPro = pro.get(i);
				List<TRoleProfile> tempprofileList = getTRoleProFileByProfileId(tempPro
						.getId());
				for (int j = 0; j < tempprofileList.size(); j++) {
					TRoleProfile roleProfile = tempprofileList.get(j);
					profileService.delEntity(roleProfile);
				}
				List<TUserProfile> tempuserProfileList = getTuserProfileByProfileId(tempPro
						.getId());
				for (int k = 0; k < tempuserProfileList.size(); k++) {
					TUserProfile userProfile = userProfileList.get(k);
					profileService.delEntity(userProfile);
				}

				List<TProfileTemp> tempprofileTempList = getTProfileTempByProfileId(tempPro
						.getId());
				for (int l = 0; l < tempprofileTempList.size(); l++) {
					TProfileTemp profileTemp = profileTempList.get(l);
					profileService.delEntity(profileTemp);
				}
				profileService.delEntity(tempPro);
			}

			profileService.delEntity(profile);
		} else {
			profileService.delEntity(profile);
		}

	}

	private List<TProfile> getTProfileByRemark(String remark) {
		String sql = "select * from t_profile p where p.remark1='" + remark
				+ "'";
		List<TProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfile.class);

		return list;
	}

	/**
	 * 根据授权ID查询授权信息
	 */
	private List<TRoleProfile> getTRoleProFileByProfileId(String profileId) {
		String sql = "select * from t_role_profile t where t.profile_id ="
				+ profileId;

		List<TRoleProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRoleProfile.class);

		return list;
	}

	/**
	 * 根据授权ID查询用户授权信息
	 * 
	 * @param profileId
	 * @return
	 */
	private List<TUserProfile> getTuserProfileByProfileId(String profileId) {
		String sql = "select * from t_user_profile t  where t.profile_id="
				+ profileId;

		List<TUserProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUserProfile.class);

		return list;
	}

	private List<TProfileTemp> getTProfileTempByProfileId(String profileId) {
		String sql = "select * from t_profile_temp t where t.profile_id="
				+ profileId;

		List<TProfileTemp> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfileTemp.class);

		return list;
	}

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
			int firstResult, int maxResults, TableBean table) throws Exception {
		Long count = super.generalDAO.getResultCount("roleProfileSearch", map);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"roleProfileSearch", map, firstResult, maxResults);
	}

	/**
	 * 删除角色授权信息
	 * 
	 * @param roleId
	 * @param profileId
	 */
	public void deleteTRoleProfile(String roleId, String profileId) {
		List<TRoleProfile> list = getRoleProfileByRoleIdandProfileId(roleId,
				profileId);
		for (int i = 0; i < list.size(); i++) {
			TRoleProfile roleprofile = (TRoleProfile) list.get(i);
			super.delEntity(roleprofile);
		}
	}

	/**
	 * 删除用户授权信息
	 * 
	 * @param userId
	 * @param profileId
	 */
	public void deleteTUserProfile(String userId, String profileId) {
		String sql = "select * from t_user_profile up where up.user_id='"
				+ userId + "' and up.profile_id='" + profileId + "'";
		List<TUserProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUserProfile.class);
		for (int i = 0; i < list.size(); i++) {
			TUserProfile userProfile = (TUserProfile) list.get(i);
			super.delEntity(userProfile);
		}
	}

	/**
	 * 根据角色ID，授权ID查询角色的信息
	 * 
	 * @param roleId
	 * @param profileId
	 * @return
	 */
	public List<TRoleProfile> getRoleProfileByRoleIdandProfileId(String roleId,
			String profileId) {
		String sql = "select * from t_role_profile rp where rp.role_id='"
				+ roleId + "' and rp.profile_id='" + profileId + "'";
		List<TRoleProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRoleProfile.class);

		return list;
	}

	public List<TUserProfile> getUserProfileByRoleIdAndProfileId(String userId,
			String profileId) {
		String sql = "select * from t_user_profile p  where  p.user_id='"
				+ userId + "' and p.profile_id='" + profileId + "'";
		List<TUserProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUserProfile.class);

		return list;
	}

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
			String siteName) {

		//无权限标识符号
		if(profileId==null || profileId.trim().length()==0 ||"null".equals(profileId)){
			return true;
		}
		// 读取配置文件
		Configurater config = Configurater.getInstance();
		// 取出配置文件中的所有信息
		String proiority = config.getConfigValue("PRIORITY_CONFIG_KEY");
		// 用分号将元素折分
		String[] tempproiority = proiority.split(";");
		// 将名字存放在Object数组中
		int tempsite = 0;
		for (int i = 0; i <= tempproiority.length; i++) {
			String[] names = tempproiority[i].split(":");
			String name = names[1];
			if (siteName.equals(name)) {
				tempsite = i;
				break;
			}

		}

		String sqlrole = "select * from t_role_profile rp where rp.role_id='"
				+ user.getTRole().getId() + "' and rp.profile_id='" + profileId
				+ "'";
		List<TRoleProfile> roleProfileList = super.generalDAO
				.queryByNativeSQLEntity(sqlrole, TRoleProfile.class);

		boolean rolepro = false;
		for (int i = 0; i < roleProfileList.size(); i++) {
			TRoleProfile roleProfile = roleProfileList.get(i);

			//无此定义，默认为真
			if(roleProfile.getGrantPrivilege()==null){
				rolepro = true;
			}else{
				String[] auths = roleProfile.getGrantPrivilege().split(";");
				String[] eachAuth = auths[i].split(":");
				char[] roleauths = eachAuth[1].toCharArray();
				
				//如果定义了该权限
				if (roleauths.length > tempsite) {
					// 存在该授权即跳出循环
					if (roleauths[tempsite] == '1') {
						rolepro = true;
						break;
					}else{
						rolepro = false;
					}
				//未定义，默认为真
				}else{
					rolepro = true;
				}
			}
		}
		
		String sql = "select * from t_user_profile p  where  p.user_id='"
				+ user.getLoginId() + "' and p.profile_id='" + profileId + "'";
		List<TUserProfile> userProfileList = super.generalDAO
				.queryByNativeSQLEntity(sql, TUserProfile.class);
		
		boolean userpro = false;
		for (int i = 0; i < userProfileList.size(); i++) {
			TUserProfile userProfile = userProfileList.get(i);
			
			//无此定义，默认为真
			if(userProfile.getGrantPrivilege()==null){
				userpro = true;
			}else{
				String[] auths = userProfile.getGrantPrivilege().split(";");
				String[] eachAuth = auths[i].split(":");
				char[] roleauths = eachAuth[1].toCharArray();
				//如果定义了该权限
				if (roleauths.length > tempsite) {
					if (roleauths[tempsite] == '1') {
						userpro = true;
					} else {
						userpro = false;
					}
				//未定义，默认为真
				}else{
					userpro = true;
				}
			}
		}
		
		return rolepro||userpro;
	}

	/**
	 * 根据角色ID，权限分类查询授权信息
	 * 
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<TProfile> getTProfileByroleIdandProfileId(String roleId,
			String type) {
		String sql = " select * from t_profile t where  not exists"
				+ " (select null from t_role_profile rp  "
				+ "where rp.profile_id=t.id  and rp.role_id='" + roleId
				+ "')  and  t.type='" + type + "' ";
		List<TProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfile.class);

		return list;
	}

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
			int firstResult, int maxResults, TableBean table) throws Exception {
		Long count = super.generalDAO.getResultCount("userProfileSearch", map);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"userProfileSearch", map, firstResult, maxResults);
	}

	/**
	 * 根据角色ID查询所有用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List<TUser> queryAllUserList(String roleId) {
		String sql = "select * from t_user  u where u.role_id='" + roleId + "'";
		List<TUser> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUser.class);

		return list;
	}

	/**
	 * 根据用户查询未控权的对象
	 * 
	 * @param userId
	 * @return
	 */
	public List<TProfile> getTProfileByUserId(String userId) {
		String sql = "select p.* from t_profile p where "
				+ "not exists(select up.profile_id from t_user u ,t_user_profile up ,t_profile where"
				+ " u.login_id=up.user_id  and up.profile_id = p.id and u.login_id = '"
				+ userId
				+ "') and "
				+ "not exists(select p1.profile_id from t_role r1 ,t_role_profile p1,t_user u1 where"
				+ " r1.id=p1.role_id and r1.id=u1.role_id and p1.profile_id=p.id and u1.login_id='"
				+ userId + "')";
		List<TProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfile.class);

		return list;
	}

}