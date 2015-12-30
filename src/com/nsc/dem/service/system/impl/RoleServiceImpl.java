package com.nsc.dem.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.action.bean.RoleBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TProfileTemp;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TRoleTree;
import com.nsc.dem.service.base.BaseService;
import com.nsc.dem.service.system.IroleService;

@SuppressWarnings("unchecked")
public class RoleServiceImpl extends BaseService implements IroleService {

	/**
	 * 角色管理--查询列表 需要根据查询条件从角色表中获取角色信息列表，
	 * 
	 * @param 参数为角色表实体类
	 * @return
	 */
	public List<Object> roleInfoList() {
		return null;
	}

	/**
	 * 角色管理--删除
	 * 
	 * @param 参数为角色表主键数组
	 */
	public void roleDel(String role[]) {

	}

	/**
	 * 根据现有权限获得默认权限字符串
	 * 
	 * @param field
	 *            字段名称
	 * @return
	 */
	public List<Map<String, Object>> getRolePriority(Object entity,
			String field, List<TRole> tRoleList) {
		List<Map<String, Object>> rolePriority = new ArrayList<Map<String, Object>>();
		// 读取配置文件
		Configurater config = Configurater.getInstance();
		// 取出配置文件中的所有信息
		String proiority = config.getConfigValue("PRIORITY_CONFIG_KEY");
		// 用分号将元素折分
		String[] tempproiority = proiority.split(";");
		// 将名字存放在Object数组中
		Object[] names = new Object[tempproiority.length];

		String cfgStr = "";
		for (int i = 0; i < tempproiority.length; i++) {
			String[] nameArray = tempproiority[i].split(":");
			// 取出配置文件中的值，存在一个Object中
			names[i] = nameArray[1];
			// 取出配置文件中的key，存在一个字符串中
			cfgStr += nameArray[0];
		}

		if (entity != null && field != null) {
			// 取当前角色
			String privilege = field;
			// 角色1:01;角色2:11
			String[] tempprivilege = privilege.split(";");

			for (int j = 0; j < tRoleList.size(); j++) {
				TRole trole = (TRole) tRoleList.get(j);
				// 取出角色数组
				String[] roles = new String[tRoleList.size()];
				// 取出授权信息数组
				String[] priStrs = new String[tRoleList.size()];
				RoleBean rolebean = new RoleBean();
				String priStr = "";
				String tempId = "";
				String tempPri = "";
				// 角色1:01
				for (int i = 0; i < tempprivilege.length; i++) {
					String[] rolpriStrArray = tempprivilege[i].split(":");
					// 找到该角色，数据库中已经定义了。
					if (rolpriStrArray[0].equals(trole.getId())) {
						tempId = rolpriStrArray[0];
						tempPri = rolpriStrArray[1];
						break;
					}
				}
				// 角色1
				roles[j] = tempId == null ? "" : trole.getId();
				// 根据角角ID查询出角色名称
				// 01
				priStrs[j] = tempPri == null ? "" : tempPri;

				rolebean.setRoleid(trole.getId());
				rolebean.setRolename(trole.getName());
				priStr = priStrs[j];
				// 01+XXX
				priStr += cfgStr.substring(priStr.length());
				char[] readPri = priStr.toCharArray();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("rolebean", rolebean);
				map.put("readPri", readPri);
				map.put("names", names);
				// 0,1,X,X,X
				// Object[] readPri = new Object []{priStr};
				// 存放在list中 用户角色 是否读取 是否再授权 读取再授权的名称
				rolePriority.add(map);
			}
		} else {
			// 角色
			char[] readPri = cfgStr.toCharArray();
			String roleid = "";
			String rolename = "";
			for (int j = 0; j < tRoleList.size(); j++) {
				TRole trole = (TRole) tRoleList.get(j);
				roleid = trole.getId();
				rolename = trole.getName();
				RoleBean rolebean = new RoleBean();
				rolebean.setRoleid(roleid);
				rolebean.setRolename(rolename);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("rolebean", rolebean);
				map.put("readPri", readPri);
				map.put("names", names);
				// 0,1,X,X,X
				// Object[] readPri = new Object []{priStr};
				// 存放在list中 用户角色 是否读取 是否再授权 读取再授权的名称
				rolePriority.add(map);
			}

		}
		return rolePriority;
	}

	/**
	 * 查询所有的角色
	 * 
	 * @return
	 */

	public List<TRole> roleList() {

		String sql = "select * from t_role r";

		List<TRole> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRole.class);

		return list;
	}

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
			int firstResult, int maxResults, TableBean table) throws Exception {

		Long count = super.generalDAO.getResultCount("roleInfoSearch", map);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"roleInfoSearch", map, firstResult, maxResults);
	}

	/**
	 * 根据角色ID查询所有使用角色的用户
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TUser> getUserByRoleId(String roleid) {
		String sql = "select * from t_user u where u.role_id='" + roleid + "'";

		List<TUser> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUser.class);

		return list;
	}

	/**
	 * 根据角色ID查询树形角色
	 * 
	 * @param id
	 * @return
	 */
	public List<TRoleTree> getRoleIdByTRoleTreeId(String id) {
		String sql = "select *  from t_role_tree t where t.id='" + id + "'";

		List<TRoleTree> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRoleTree.class);

		return list;

	}

	/**
	 * 根据角色ID查询快捷菜单
	 * 
	 * @param id
	 * @return
	 */
	public List<TRoleProfile> getRoleIdByTRoleProFileId(String id) {
		String sql = "select * from t_role_profile t where t.role_id = '" + id
				+ "'";

		List<TRoleProfile> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRoleProfile.class);

		return list;
	}

	/**
	 * 
	 *根据角色ID查询 数据字典表
	 * 
	 * @param id
	 * @return
	 */
	public List<TDictionary> getRoleIdByTDictionaryAuthControl(String roleid) {
		String sql = "select t.* from t_dictionary t where t.auth_control  like '%"
				+ roleid + "%'";

		List<TDictionary> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDictionary.class);

		return list;
	}

	/**
	 * 根据角色ID查询文档分类表
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TDocType> getRoleIdByTDocTypePrivilege(String roleid) {
		String sql = "select * from t_doc_type  t where t.privilege like '%"
				+ roleid + "%'";

		List<TDocType> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDocType.class);

		return list;
	}

	/**
	 * 根据角色ID查询临时授权表
	 * 
	 * @param roleid
	 * @return
	 */
	public List<TProfileTemp> getRoleIdByTProfileTempRole(String roleid) {
		String sql = "select * from t_profile_temp  t where t.role like '%"
				+ roleid + "%'";

		List<TProfileTemp> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProfileTemp.class);

		return list;
	}

	/**
	 * 删除角色信息
	 * 
	 * @param roleId
	 */
	public void deleteRoleInfo(IroleService roleService, String roleId) {

		TRole role = (TRole) roleService.EntityQuery(TRole.class, roleId);

		List<TRoleTree> roleTreeList = roleService
				.getRoleIdByTRoleTreeId(roleId);
		if (roleTreeList.size() != 0) {
			for (int j = 0; j < roleTreeList.size(); j++) {
				TRoleTree roletree = (TRoleTree) roleTreeList.get(j);
				roleService.delEntity(roletree);
			}

		}

		List<TRoleProfile> roleProfileList = roleService
				.getRoleIdByTRoleProFileId(roleId);
		if (roleProfileList.size() != 0) {
			for (int k = 0; k < roleProfileList.size(); k++) {
				TRoleProfile roleProfile = (TRoleProfile) roleProfileList
						.get(k);
				roleService.delEntity(roleProfile);
			}
		}

		List<TDictionary> dicList = roleService
				.getRoleIdByTDictionaryAuthControl(roleId);
		for (int j = 0; j < dicList.size(); j++) {
			TDictionary dic = dicList.get(j);

			String[] authArray = dic.getAuthControl().split(";");

			for (int k = 0; k < authArray.length; k++) {
				if (authArray[k].startsWith(roleId)) {
					authArray[k] = null;
				}
			}
			String auth = "";
			for (int k = 0; k < authArray.length; k++) {

				if (authArray[k] != null) {
					auth += authArray[k] + ";";
				}

			}
			if (auth.trim().length() > 0) {
				auth = auth.substring(0, auth.length() - 1);
			}
			dic.setAuthControl(auth);
			roleService.updateEntity(dic);

		}

		List<TDocType> docTypeList = roleService
				.getRoleIdByTDocTypePrivilege(roleId);
		for (int j = 0; j < docTypeList.size(); j++) {
			TDocType docType = docTypeList.get(j);

			String[] authArray = docType.getPrivilege().split(";");

			for (int k = 0; k < authArray.length; k++) {
				if (authArray[k].startsWith(roleId)) {
					authArray[k] = null;
				}
			}
			String auth = "";
			for (int k = 0; k < authArray.length; k++) {

				if (authArray[k] != null) {
					auth += authArray[k] + ";";
				}

			}
			if (auth.trim().length() > 0) {
				auth = auth.substring(0, auth.length() - 1);
			}
			docType.setPrivilege(auth);
			roleService.updateEntity(docType);

		}

		List<TProfileTemp> profileTempList = roleService
				.getRoleIdByTProfileTempRole(roleId);

		for (int j = 0; j < profileTempList.size(); j++) {
			TProfileTemp profiletemp = profileTempList.get(j);

			String[] authArray = profiletemp.getRole().split(";");

			for (int k = 0; k < authArray.length; k++) {
				if (authArray[k].startsWith(roleId)) {
					authArray[k] = null;
				}
			}
			String auth = "";
			for (int k = 0; k < authArray.length; k++) {

				if (authArray[k] != null) {
					auth += authArray[k] + ";";
				}

			}
			if (auth.trim().length() > 0) {
				auth = auth.substring(0, auth.length() - 1);
			}
			profiletemp.setRole(auth);
			roleService.updateEntity(profiletemp);

		}
		roleService.delEntity(role);
	}

	/**
	 * 查询用户角色
	 * 
	 * @param roleId
	 * @return
	 */
	public List<TRole> queryTRoleList(String roleId) {
		String sql;
		if (roleId != null) {
			sql = "select * from t_role r where r.id='" + roleId + "'";
		} else {
			sql = "select * from t_role r";
		}

		List<TRole> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRole.class);

		return list;

	}

	/**
	 * 根据字段名查询出相对应权限
	 * 
	 * @param field
	 */
	public String getfieldSelectAuthControl(String field) {
		String authControle = "";
		// 读取配置文件
		Configurater config = Configurater.getInstance();
		// 取出配置文件中的所有信息
		String proiority = config.getConfigValue("PRIORITY_CONFIG_KEY");
		// 用分号将元素折分
		String[] tempproiority = proiority.split(";");
		String configpro = "";
		for (int i = 0; i < tempproiority.length; i++) {
			String[] eachpro = tempproiority[i].split(":");
			configpro += eachpro[1] + ",";
		}

		if (field != null) {
			String[] authControlArray = field.split(";");
			for (int i = 0; i < authControlArray.length; i++) {
				String[] eachAuth = authControlArray[i].split(":");
				String tempconfigPro = configpro.substring(0, configpro
						.length() - 1);
				String[] eachConfigPro = tempconfigPro.split(",");
				TRole role = (TRole) super
						.EntityQuery(TRole.class, eachAuth[0]);
				authControle += role.getName() + ":";
				char[] tempeach = eachAuth[1].toCharArray();
				for (int j = 0; j < tempeach.length; j++) {
					if (tempeach[j] == '1') {
						authControle += eachConfigPro[j];
						authControle += ",";
					}
				}
				authControle = authControle.substring(0,
						authControle.length() - 1);

				authControle += ";";

			}
			authControle = authControle.substring(0, authControle.length() - 1);
		}

		return authControle;
	}
}
