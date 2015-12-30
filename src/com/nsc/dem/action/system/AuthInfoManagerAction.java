package com.nsc.dem.action.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.util.DateUtils;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TRoleProfileId;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.profile.TUserProfile;
import com.nsc.dem.bean.profile.TUserProfileId;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.system.IprofileService;
import com.nsc.dem.service.system.IroleService;

public class AuthInfoManagerAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;
	// 返回的json
	private String selectparentId;

	public void setSelectparentId(String selectparentId) {
		this.selectparentId = selectparentId;
	}

	private List<Map<String, Object>> rolePriority;

	public List<Map<String, Object>> getRolePriority() {
		return rolePriority;
	}

	public void setRolePriority(List<Map<String, Object>> rolePriority) {
		this.rolePriority = rolePriority;
	}

	private TRoleProfile roleProfile;

	public TRoleProfile getRoleProfile() {
		return roleProfile;
	}

	public void setRoleProfile(TRoleProfile roleProfile) {
		this.roleProfile = roleProfile;
	}

	private String type;

	public void setType(String type) {
		this.type = type;
	}

	// 传来的页数和行数
	private int page;
	private int rows;
	private String updateProfileId;

	public void setUpdateProfileId(String updateProfileId) {
		this.updateProfileId = updateProfileId;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 传来检查和更新时的ID
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	// profile类
	private TProfile profile;

	public TProfile getProfile() {
		return profile;
	}

	public void setProfile(TProfile profile) {
		this.profile = profile;
	}

	// 权限管理查询时传来的条件(说明、分类)
	private String remark;
	private String authClass;

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setAuthClass(String authClass) {
		this.authClass = authClass;
	}

	// 权限分类的List
	private List<Map<String, Object>> authClassList;

	public List<Map<String, Object>> getAuthClassList() {
		return authClassList;
	}

	public void setAuthClassList(List<Map<String, Object>> authClassList) {
		this.authClassList = authClassList;
	}

	// 所属权限分类
	private List<Map<String, Object>> typeProfileList;

	public List<Map<String, Object>> getTypeProfileList() {
		return typeProfileList;
	}

	public void setTypeProfileList(List<Map<String, Object>> typeProfileList) {
		this.typeProfileList = typeProfileList;
	}

	// 权限服务
	IprofileService profileService;

	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	// 删除时传来的profileID
	private String profileIdList;
	// 更新插入删除时返回值
	private String returnValue;

	public void setProfileIdList(String profileIdList) {
		this.profileIdList = profileIdList;
	}

	public String getReturnValue() {
		return returnValue;
	}

	// 更新权限表的List
	private List<Map<String, Object>> updateProfileList;

	public List<Map<String, Object>> getUpdateProfileList() {
		return updateProfileList;
	}

	public void setUpdateProfileList(List<Map<String, Object>> updateProfileList) {
		this.updateProfileList = updateProfileList;
	}

	private String deleteRolefileList;

	public void setDeleteRolefileList(String deleteRolefileList) {
		this.deleteRolefileList = deleteRolefileList;
	}

	// 更新权限表的List
	private List<Map<String, Object>> profileInfo;

	public List<Map<String, Object>> getProfileInfo() {
		return profileInfo;
	}

	public void setProfileInfo(List<Map<String, Object>> profileInfo) {
		this.profileInfo = profileInfo;
	}

	/**
	 * 权限分页查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectProfileAllAction() throws Exception {
		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dcode", authClass);
		map.put("parentId", selectparentId);
		String description = "%" + remark + "%";
		map.put("description", description);
		list = profileService.queryTProfileInfoList(map, firstResult,
				maxResults, tablebean);
		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TProfile profile = (TProfile) obj[0];
				TDictionary dic = (TDictionary) obj[1];

				RowBean rowbean = new RowBean();
				String createDate = DateUtils.DateToString(profile
						.getCreateDate());
				rowbean.setCell(new Object[] {
						dic.getName(),
						profile.getId(),
						profile.getName(),
						profile.getDescription(),
						profile.getSortNo(),
						createDate,
						"<a href='#'  onclick='insertProfileshowDialog(\""
								+ profile.getId() + "\",\"" + profile.getType()
								+ "\")' >编辑</a>" });
				// 当前的ID为1
				rowbean.setId(profile.getId());
				rowsList.add(rowbean);
			}
		}

		// 给单元格里存放数据
		tablebean.setRows(rowsList);
		if (tablebean.getRecords() == 0) {
			tablebean.setPage(0);
		} else {
			// 当前页是1页
			tablebean.setPage(this.page);

		}

		int records = tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return SUCCESS;

	}

	/**
	 * 删除授权信息
	 * 
	 * @return
	 */
	public String deleteProfileAction() {
		String[] profileList = profileIdList.split(",");
		for (int i = 0; i < profileList.length; i++) {
			profileService.deleteTProfileInfo(profileService, profileList[i]);
		}
		returnValue = "success";
		return SUCCESS;
	}

	private String zhuimageUrl;

	public void setZhuimageUrl(String zhuimageUrl) {
		this.zhuimageUrl = zhuimageUrl;
	}

	/**
	 * 插入更新权限profile Action
	 * 
	 * @return
	 */
	public String insertProfileAction() {
		if (!profile.getType().equals("P02")) {
			profile.setRemark1(null);
			profile.setRemark5(zhuimageUrl);
		} else {
			profile.setType("P02");
			profile.setRemark5(profile.getRemark5());
		}
		profile.setTUser(super.getLoginUser());
		profile.setCreateDate(new Date());

		if (updateProfileId == null || (updateProfileId.equals(""))) {
			profileService.insertEntity(profile);
		} else {
			profileService.updateEntity(profile);
		}
		returnValue = "true";
		return SUCCESS;
	}

	private String parenttype;

	public void setParenttype(String parenttype) {
		this.parenttype = parenttype;
	}

	/**
	 * 初始化所属权限分类
	 * 
	 * @return
	 */
	public String getTProfileByTypeAction() {
		List<TProfile> profileList = profileService
				.getTProfileByType(parenttype);

		typeProfileList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < profileList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TProfile profile = profileList.get(i);
			map.put("id", profile.getId());
			map.put("name", profile.getName());
			typeProfileList.add(map);
		}
		return SUCCESS;

	}

	/**
	 * 根据权限ID，查询当初权限分类的详细信息
	 * @return
	 */
	public String getProfileByIdAction() {
		TProfile profile = (TProfile) profileService.EntityQuery(
				TProfile.class, id);
		updateProfileList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", profile.getId());
		map.put("name", profile.getName());
		map.put("type", profile.getType());
		map.put("isParent", profile.getRemark1());
		// 路径
		map.put("url", profile.getRemark2());
		// 备注
		map.put("remark", profile.getDescription());
		// 是否伸缩
		map.put("shensuo", profile.getRemark3());
		// 是否可用
		map.put("keyong", profile.getRemark4());
		map.put("imageurl", profile.getRemark5());
		map.put("sortNo", profile.getSortNo());
		updateProfileList.add(map);
		return SUCCESS;
	}

	/**
	 *查询当前权限ID是否存在
	 * 
	 * @return
	 */
	public String selectProfileIdAction() {
		TProfile profile = (TProfile) profileService.EntityQuery(
				TProfile.class, id);
		if (profile != null) {
			returnValue = "true";
		} else {
			returnValue = "false";
		}
		return SUCCESS;
	}

	// 查询角色时传来的(权限分类、角色名称)
	private String roleProfileType;

	public void setRoleProfileType(String roleProfileType) {
		this.roleProfileType = roleProfileType;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	private String roleId;

	/**
	 * 查询角色授权信息列表
	 * @return
	 * @throws Exception
	 */
	public String selectRoleProfileAction() throws Exception {

		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", roleProfileType);
		map.put("roleId", roleId);

		list = profileService.queryTRoleProfileInfoList(map, firstResult,
				maxResults, tablebean);
		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TRoleProfile roleProfile = (TRoleProfile) obj[0];
				TProfile profile = (TProfile) obj[1];
				TUser user = (TUser) obj[2];
				TRole role = (TRole) obj[3];
				TDictionary dic = (TDictionary) obj[4];
				String createDate = DateUtils.DateToString(roleProfile
						.getGrantTime());
				String authControl = roleService
						.getfieldSelectAuthControl(roleProfile
								.getGrantPrivilege());
				RowBean rowbean = new RowBean();
				rowbean.setCell(new Object[] {
						role.getName(),
						profile.getName(),
						authControl,
						user.getCreator(),
						createDate,
						"<a href='#'  onclick='insertRoleProfileshowDialog(\""
								+ role.getId() + "\",\"" + profile.getId()
								+ "\",\"" + profile.getType() + "\",\""
								+ role.getName() + "\",\"" + profile.getName()
								+ "\",\"" + dic.getName() + "\",\""
								+ "\")' >编辑</a>" });
				// 当前的ID为1
				String ids = roleProfile.getId().getTRole().getId() + ","
						+ roleProfile.getId().getTProfile().getId();
				rowbean.setId(ids.toString());
				rowsList.add(rowbean);
			}
		}

		// 给单元格里存放数据
		tablebean.setRows(rowsList);
		if (tablebean.getRecords() == 0) {
			tablebean.setPage(0);
		} else {
			// 当前页是1页
			tablebean.setPage(this.page);

		}

		int records = tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return SUCCESS;

	}

	/**
	 * 删除角色授权
	 * 
	 * @return
	 */
	public String deleteRoleProfileAction() {

		String[] deleteIds = deleteRolefileList.split(";");
		for (int i = 0; i < deleteIds.length; i++) {
			String[] roleprofiles = deleteIds[i].split(",");
			for (int j = 0; j < roleprofiles.length; j++) {
				String roleId = roleprofiles[0];
				String profileId = roleprofiles[1];
				profileService.deleteTRoleProfile(roleId, profileId);
			}
		}
		returnValue = "success";
		return SUCCESS;
	}

	// 角色表
	private List<TRole> tRoleList;

	public void settRoleList(List<TRole> tRoleList) {
		this.tRoleList = tRoleList;
	}

	IroleService roleService;

	public void setRoleService(IroleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 角色权限授 权信息
	 * 
	 * @return
	 */
	public String profileInfoAction() {
		rolePriority = new ArrayList<Map<String, Object>>();
		List<TRoleProfile> roleprofileList = profileService
				.getRoleProfileByRoleIdandProfileId(roleId, roleProfileId);
		tRoleList = roleService.queryTRoleList(roleId);
		if (roleprofileList.size() != 0) {
			for (int i = 0; i < roleprofileList.size(); i++) {
				TRoleProfile profile = roleprofileList.get(i);
				tRoleList = roleService.queryTRoleList(roleId);
				rolePriority = roleService.getRolePriority(profile, profile
						.getGrantPrivilege(), tRoleList);

			}
		} else {
			rolePriority = roleService.getRolePriority(new TRoleProfile(),
					null, tRoleList);
		}

		return SUCCESS;

	}

	/**
	 * 用户权限授 权信息
	 * 
	 * @return
	 */
	public String userProfileInfoAction() {
		rolePriority = new ArrayList<Map<String, Object>>();
		List<TUserProfile> roleprofileList = profileService
				.getUserProfileByRoleIdAndProfileId(userId, roleProfileId);
		tRoleList = roleService.queryTRoleList(roleId);
		if (roleprofileList.size() != 0) {
			for (int i = 0; i < roleprofileList.size(); i++) {
				TUserProfile userprofile = roleprofileList.get(i);
				tRoleList = roleService.queryTRoleList(roleId);
				rolePriority = roleService.getRolePriority(userprofile,
						userprofile.getGrantPrivilege(), tRoleList);

			}
		} else {
			rolePriority = roleService.getRolePriority(new TUserProfile(),
					null, tRoleList);
		}

		return SUCCESS;

	}

	private String roleProfileId;

	public void setRoleProfileId(String roleProfileId) {
		this.roleProfileId = roleProfileId;
	}

	public void setProfileParentID(String profileParentID) {
		this.profileParentID = profileParentID;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private String profileID;

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	private String profileParentID;
	private String roleName;
	private String updateroleId;
	private String updateroleprofileId;

	public void setUpdateroleprofileId(String updateroleprofileId) {
		this.updateroleprofileId = updateroleprofileId;
	}

	public void setUpdateroleId(String updateroleId) {
		this.updateroleId = updateroleId;
	}

	/**
	 * 插入角色授权信息
	 * 
	 * @return
	 */
	public String insertRoleProfileAction() {
		roleProfile.setTUser(super.getLoginUser());

		if (updateroleId == null || (updateroleId.equals(""))) {
			TProfile profile;
			if (profileID != null) {
				profile = (TProfile) profileService.EntityQuery(TProfile.class,
						profileID);
			} else {
				profile = (TProfile) profileService.EntityQuery(TProfile.class,
						profileParentID);
			}
			TRole role = (TRole) profileService.EntityQuery(TRole.class,
					roleName);

			TRoleProfileId roleprofileId = new TRoleProfileId();

			roleprofileId.setTRole(role);
			roleprofileId.setTProfile(profile);

			roleProfile.setId(roleprofileId);
			roleProfile.setGrantTime(new Timestamp(System.currentTimeMillis()));
			profileService.insertEntity(roleProfile);
		} else {
			TProfile profile = (TProfile) profileService.EntityQuery(
					TProfile.class, updateroleprofileId);

			TRole role = (TRole) profileService.EntityQuery(TRole.class,
					updateroleId);
			
			TRoleProfileId roleprofileId = new TRoleProfileId();

			roleprofileId.setTRole(role);
			roleprofileId.setTProfile(profile);

			roleProfile.setId(roleprofileId);
			roleProfile.setGrantTime(new Timestamp(System.currentTimeMillis()));
			profileService.updateEntity(roleProfile);
		}

		returnValue = "success";
		return SUCCESS;
	}

	private List<Map<String, Object>> profileTypeList;

	public List<Map<String, Object>> getProfileTypeList() {
		return profileTypeList;
	}

	public void setProfileTypeList(List<Map<String, Object>> profileTypeList) {
		this.profileTypeList = profileTypeList;
	}

	/**
	 * 根据授权类型和角色查询授权信息
	 * 
	 * @return
	 */
	public String getTprofileByTypeandRoleIdAction() {
		profileTypeList = new ArrayList<Map<String, Object>>();
		List<TProfile> profileList = profileService
				.getTProfileByroleIdandProfileId(roleId, type);
		for (int i = 0; i < profileList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TProfile profile = profileList.get(i);
			map.put("id", profile.getId());
			map.put("name", profile.getName());
			profileTypeList.add(map);
		}
		return SUCCESS;
	}

	// 前台传来查询用户授权的条件
	private String userRoleName;
	private String userProfileType;
	private String userName;

	/**
	 * 用户授权查询分页信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectUserProfileInfoAction() throws Exception {

		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("uroleId", userRoleName);
		map.put("ptype", userProfileType);
		String name = "%" + userName + "%";
		map.put("uname", name);

		list = profileService.queryTUserProfileInfoList(map, firstResult,
				maxResults, tablebean);
		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TUserProfile userProfile = (TUserProfile) obj[0];
				TProfile profile = (TProfile) obj[1];
				TUser user = (TUser) obj[2];
				TRole role = (TRole) obj[3];
				String createDate = DateUtils.DateToString(userProfile
						.getGrantTime());
				String authControl = roleService
						.getfieldSelectAuthControl(userProfile
								.getGrantPrivilege());
				RowBean rowbean = new RowBean();
				rowbean.setCell(new Object[] {
						user.getName(),
						profile.getName(),
						authControl,
						user.getCreator(),
						createDate,
						"<a href='#'  onclick='insertUserProfile(\""
								+ role.getName() + "\",\"" + user.getName()
								+ "\",\"" + profile.getName() + "\",\""
								+ user.getLoginId() + "\",\"" + profile.getId()
								+ "\",\"" + role.getId()
								+ "\")' >编辑</a>" });
				String ids = user.getLoginId() + "," + profile.getId();
				rowbean.setId(ids);
				rowsList.add(rowbean);
			}
		}

		// 给单元格里存放数据
		tablebean.setRows(rowsList);
		if (tablebean.getRecords() == 0) {
			tablebean.setPage(0);
		} else {
			// 当前页是1页
			tablebean.setPage(this.page);
		}

		int records = tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return SUCCESS;

	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public void setUserProfileType(String userProfileType) {
		this.userProfileType = userProfileType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String deleteuserProfiles;

	public void setDeleteuserProfiles(String deleteuserProfiles) {
		this.deleteuserProfiles = deleteuserProfiles;
	}

	/**
	 * 删除用户授权信息
	 * 
	 * @return
	 */
	public String deleteUserProfileAction() {
		String[] deleteIds = deleteuserProfiles.split(";");
		for (int i = 0; i < deleteIds.length; i++) {
			String[] userProfile = deleteIds[i].split(",");
			for (int j = 0; j < userProfile.length; j++) {
				String userId = userProfile[0];
				String profileId = userProfile[1];
				profileService.deleteTUserProfile(userId, profileId);
			}
		}
		returnValue = "success";
		return SUCCESS;
	}

	private String userId;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private List<Map<String, Object>> allUserList;

	public List<Map<String, Object>> getAllUserList() {
		return allUserList;
	}

	public void setAllUserList(List<Map<String, Object>> allUserList) {
		this.allUserList = allUserList;
	}

	private String userRoleId;

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * 初始化用户名称
	 * 
	 * @return
	 */
	public String selectAllUserAction() {

		List<TUser> userList = profileService.queryAllUserList(userRoleId);

		allUserList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TUser user = (TUser) userList.get(i);
			map.put("id", user.getLoginId());
			map.put("name", user.getName());
			allUserList.add(map);
		}
		return SUCCESS;
	}

	private List<Map<String, Object>> allprofileList;

	/**
	 * 根据用户Id，查询未关联的权限分类
	 * @return
	 */
	public String getTProfileByUserIdAction() {
		allprofileList = new ArrayList<Map<String, Object>>();
		List<TProfile> profileList = profileService.getTProfileByUserId(userId);
		for (int i = 0; i < profileList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TProfile profile = profileList.get(i);
			map.put("id", profile.getId());
			map.put("name", profile.getName());
			allprofileList.add(map);
		}
		return SUCCESS;
	}

	public List<Map<String, Object>> getAllprofileList() {
		return allprofileList;
	}

	public void setAllprofileList(List<Map<String, Object>> allprofileList) {
		this.allprofileList = allprofileList;
	}

	private TUserProfile userProfile;

	public TUserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(TUserProfile userProfile) {
		this.userProfile = userProfile;
	}

	private String insertUserName, userProfileObject;

	public void setInsertUserName(String insertUserName) {
		this.insertUserName = insertUserName;
	}

	public void setUserProfileObject(String userProfileObject) {
		this.userProfileObject = userProfileObject;
	}

	private String updateUserId, updateUserProfileId;

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public void setUpdateUserProfileId(String updateUserProfileId) {
		this.updateUserProfileId = updateUserProfileId;
	}

	/**
	 * 插入用户授权信息
	 * 
	 * @return
	 */
	public String insertUserProfileAction() {
		if (updateUserId == null || (updateUserId.equals(""))) {
			TUser user = (TUser) profileService.EntityQuery(TUser.class,
					insertUserName);
			TProfile pro = (TProfile) profileService.EntityQuery(
					TProfile.class, userProfileObject);
			TUserProfileId userprofileId = new TUserProfileId();
			userprofileId.setTProfile(pro);
			userprofileId.setTUser(user);
			userProfile.setId(userprofileId);
			userProfile.setTUserByGrantUserId(super.getLoginUser());
			userProfile.setGrantTime(new Timestamp(System.currentTimeMillis()));
			profileService.insertEntity(userProfile);

		} else {
			TUser user = (TUser) profileService.EntityQuery(TUser.class,
					updateUserId);
			TProfile pro = (TProfile) profileService.EntityQuery(
					TProfile.class, updateUserProfileId);
			TUserProfileId userprofileId = new TUserProfileId();
			userprofileId.setTProfile(pro);
			userprofileId.setTUser(user);
			userProfile.setId(userprofileId);
			userProfile.setTUserByGrantUserId(super.getLoginUser());
			userProfile.setGrantTime(new Timestamp(System.currentTimeMillis()));
			profileService.updateEntity(userProfile);
		}
		returnValue = "true";

		return SUCCESS;
	}

	private List<Map<String, Object>> parentIdList;

	public void setParentIdList(List<Map<String, Object>> parentIdList) {
		this.parentIdList = parentIdList;
	}

	private String parentId;

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 根据权限父Id，查询子权限菜单
	 * @return
	 */
	public String getTProfileByparentIdAction() {
		parentIdList = new ArrayList<Map<String, Object>>();
		List<TProfile> profileList = profileService.getTProfileByType(parentId);
		for (int i = 0; i < profileList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TProfile profile = profileList.get(i);
			map.put("id", profile.getId());
			map.put("name", profile.getName());
			parentIdList.add(map);
		}
		return SUCCESS;
	}

	public List<Map<String, Object>> getParentIdList() {
		return parentIdList;
	}
}
