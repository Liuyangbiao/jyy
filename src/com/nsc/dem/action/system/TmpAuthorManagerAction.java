package com.nsc.dem.action.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TProfileTemp;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TRoleProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.system.IprofileTempService;
import com.nsc.dem.service.system.IroleService;

public class TmpAuthorManagerAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;

	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	private TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	private IprofileTempService tmpProfileService;

	public void setTmpProfileService(IprofileTempService tmpProfileService) {
		this.tmpProfileService = tmpProfileService;
	}

	private String loginId;

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	private IroleService roleService;

	public void setRoleService(IroleService roleService) {
		this.roleService = roleService;
	}

	private String guoqishouquan;

	public void setGuoqishouquan(String guoqishouquan) {
		this.guoqishouquan = guoqishouquan;
	}

	public String selectYiShouQuanInfo() throws Exception {
		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("loginId", loginId);
		map.put("guoqishouquan", guoqishouquan);
		list = tmpProfileService.queryYiProfileInfoList(map, firstResult,
				maxResults, tablebean);

		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TProfileTemp profileTemp = (TProfileTemp) obj[0];

				TDictionary dic = (TDictionary) obj[1];
				TProfile profile = (TProfile) obj[2];

				TUser user = (TUser) obj[4];
				TUser creator = (TUser) obj[3];
				String startTime = "";
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

				if (profileTemp.getStartTime() != null) {
					startTime = timeFormat.format(profileTemp.getStartTime());
				}
				String endTime = "";
				if (profileTemp.getEndTime() != null) {
					endTime = timeFormat.format(profileTemp.getEndTime());
				}

				String createTime = "";
				if (profileTemp.getGrantTime() != null) {
					createTime = timeFormat.format(profileTemp.getGrantTime());
				}

				String authControle = roleService
						.getfieldSelectAuthControl(profileTemp
								.getGrantPrivilege());
				String update = "";
				if (guoqishouquan == null) {
					update = "<a href='#' onclick='insertYiShouQuanInfo(\""
							+ profileTemp.getId() + "\")' >编辑</a>";
				} else {
					update = "过期不可编辑";
				}
				RowBean rowbean = new RowBean();
				rowbean.setCell(new Object[] { creator.getName(),
						dic.getName(), profile.getName(), authControle,
						startTime, endTime, profileTemp.getPermitCount(),
						profileTemp.getPrcsCount(), user.getName(), createTime,
						update });
				rowbean.setId(profileTemp.getId().toString());
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

	private List<Map<String, Object>> profileTempTypeList;

	public List<Map<String, Object>> getProfileTempTypeList() {
		return profileTempTypeList;
	}

	public void setProfileTempTypeList(
			List<Map<String, Object>> profileTempTypeList) {
		this.profileTempTypeList = profileTempTypeList;
	}

	private String roleId;
	private String type;

	/**
	 * 根据授权类型和角色查询授权信息
	 * 
	 * @return
	 */
	public String getTprofileTempByTypeandRoleIdAction() {
		profileTempTypeList = new ArrayList<Map<String, Object>>();
		List<Object[]> profileList = new ArrayList<Object[]>();
		Map<String, Object> newmap = new HashMap<String, Object>();
		newmap.put("roleId", roleId);
		newmap.put("type", type);
		profileList = tmpProfileService
				.getTProfileTempByroleIdandProfileId(newmap);
		for (int i = 0; i < profileList.size(); i++) {
			Object[] obj = (Object[]) profileList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", obj[0]);
			map.put("name", obj[1]);
			profileTempTypeList.add(map);
		}

		return SUCCESS;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setType(String type) {
		this.type = type;
	}

	private List<Map<String, Object>> rolePriority;

	public List<Map<String, Object>> getRolePriority() {
		return rolePriority;
	}

	public void setRolePriority(List<Map<String, Object>> rolePriority) {
		this.rolePriority = rolePriority;
	}

	private String roleProfileId;

	public void setRoleProfileId(String roleProfileId) {
		this.roleProfileId = roleProfileId;
	}

	// 角色表
	private List<TRole> tRoleList;

	public void settRoleList(List<TRole> tRoleList) {
		this.tRoleList = tRoleList;
	}

	/**
	 * 授 权信息
	 * 
	 * @return
	 */
	public String profileTempInfoAction() {
		rolePriority = new ArrayList<Map<String, Object>>();
		List<TProfileTemp> roleprofileList = tmpProfileService
				.getRoleProfileTempByRoleIdandProfileId(roleId, roleProfileId);
		tRoleList = roleService.queryTRoleList(roleId);
		if (roleprofileList.size() != 0) {
			for (int i = 0; i < roleprofileList.size(); i++) {
				TProfileTemp profile = roleprofileList.get(i);
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

	private TProfileTemp profileTemp;

	public TProfileTemp getProfileTemp() {
		return profileTemp;
	}

	public void setProfileTemp(TProfileTemp profileTemp) {
		this.profileTemp = profileTemp;
	}

	private String returnValue;
	private String profileId;

	public String getReturnValue() {
		return returnValue;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	private String userId;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private String updateId;

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 插入授权信息
	 * 
	 * @return
	 */
	public String insertYiShouQuanInfoAction() {

		TUser user = (TUser) tmpProfileService.EntityQuery(TUser.class, userId);
		profileTemp.setTUser(super.getLoginUser());
		profileTemp.setUserId(user.getLoginId());
		profileTemp.setGrantTime(new Timestamp(System.currentTimeMillis()));
		if (profileId != null) {
			TProfile profile = (TProfile) tmpProfileService.EntityQuery(
					TProfile.class, profileId);
			profileTemp.setTProfile(profile);
		}
		if (updateId == null || (updateId.equals(""))) {
			profileTemp.setPrcsCount(Long.valueOf(0));
			tmpProfileService.insertEntity(profileTemp);
		} else {
			tmpProfileService.updateEntity(profileTemp);
		}

		returnValue = "true";
		return SUCCESS;
	}

	private List<Map<String, Object>> userList;;

	public List<Map<String, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, Object>> userList) {
		this.userList = userList;
	}

	/**
	 * 初始化用户名称
	 * 
	 * @return
	 */
	public String userListAction() {
		userList = new ArrayList<Map<String, Object>>();
		List<TUser> users = tmpProfileService.getTUserByRoleId(roleId);
		for (int i = 0; i < users.size(); i++) {
			TUser user = users.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", user.getLoginId());
			map.put("name", user.getName());
			userList.add(map);
		}
		return SUCCESS;
	}

	private String profileIdList;

	public void setProfileIdList(String profileIdList) {
		this.profileIdList = profileIdList;
	}

	/**
	 * 删除授权信息
	 * 
	 * @return
	 */
	public String deleteProfileTempAction() {
		String[] profileLists = profileIdList.split(",");
		for (int i = 0; i < profileLists.length; i++) {
			String eachCode = profileLists[i];
			TProfileTemp profile = (TProfileTemp) tmpProfileService
					.EntityQuery(TProfileTemp.class, Long.valueOf(eachCode));
			tmpProfileService.delEntity(profile);
		}
		returnValue = "success";
		return SUCCESS;
	}

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	private List<Map<String, Object>> updateList;

	public void setUpdateList(List<Map<String, Object>> updateList) {
		this.updateList = updateList;
	}

	public List<Map<String, Object>> getUpdateList() {
		return updateList;
	}

	/**
	 * 根据ID查询授权信息
	 * 
	 * @return
	 */
	public String updateYiShouQuanAction() {
		updateList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = new ArrayList<Object[]>();

		list = tmpProfileService.updateYiShouQuan(id);
		for (Object[] obj : list) {
			TProfileTemp profileTemp = (TProfileTemp) obj[0];
			TProfile profile = (TProfile) obj[1];
			TUser user = (TUser) obj[2];
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

			String statTime = "";
			if (profileTemp.getStartTime() != null) {
				statTime = timeFormat.format(profileTemp.getStartTime());
			}
			String endTime = "";
			if (profileTemp.getEndTime() != null) {
				endTime = timeFormat.format(profileTemp.getEndTime());
			}
			map.put("id", profileTemp.getId());// id
			map.put("permitCount", profileTemp.getPermitCount());// 允许次数
			map.put("startTime", statTime);// 开始时间
			map.put("endTime", endTime);// 结束时间
			map.put("roleId", profileTemp.getRole());// 角色
			map.put("userName", user.getName());
			map.put("userId", user.getLoginId());// 用户名称
			map.put("profileType", profile.getType());
			map.put("profileObject", profile.getId());
			map.put("profileName", profile.getName());
			map.put("prcsCount", profileTemp.getPrcsCount());
			updateList.add(map);
		}
		return SUCCESS;
	}

	public String selectProfileTempIdBooleanAction() {
		TProfileTemp pfTemp = (TProfileTemp) tmpProfileService.EntityQuery(
				TProfileTemp.class, Long.valueOf(id));
		if (pfTemp != null) {
			returnValue = "true";
		} else {
			returnValue = "false";
		}
		return SUCCESS;
	}

	private String dataloginId;

	public void setDataloginId(String dataloginId) {
		this.dataloginId = dataloginId;
	}

	/**
	 * 查询数据授权
	 * @return
	 * @throws Exception
	 */
	public String dataProfileInfoAction() throws Exception {
		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("datalogin", dataloginId);
		map.put("guoqishouquan", guoqishouquan);
		list = tmpProfileService.queryDataProfileList(map, firstResult,
				maxResults, tablebean);

		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TProfileTemp profileTemp = (TProfileTemp) obj[0];
				TRole role = (TRole) obj[1];
				TUser cre = (TUser) obj[2];
				TUser user = (TUser) obj[3];
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = "";
				if (profileTemp.getStartTime() != null) {
					startTime = timeFormat.format(profileTemp.getStartTime());
				}
				String createTime = "";
				if (profileTemp.getGrantTime() != null) {
					createTime = timeFormat.format(profileTemp.getGrantTime());
				}

				String endTime = "";
				if (profileTemp.getEndTime() != null) {
					endTime = timeFormat.format(profileTemp.getEndTime());
				}

				String update = "";
				if (guoqishouquan == null) {
					update = "<a href='#' onclick='insertDataProfileInfo(\""
							+ profileTemp.getId() + "\")' >编辑</a>";
				} else {
					update = "过期不可编辑";
				}
				RowBean rowbean = new RowBean();
				rowbean
						.setCell(new Object[] { cre.getName(), role.getName(),
								startTime, endTime, user.getName(), createTime,
								update });
				rowbean.setId(profileTemp.getId().toString());
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

		// 查询出的记录数为100
		// tablebean.setRecords(records.intValue());

		int records = tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);

		return SUCCESS;
	}

	private List<Map<String, Object>> updateDataList;

	public List<Map<String, Object>> getUpdateDataList() {
		return updateDataList;
	}

	public void setUpdateDataList(List<Map<String, Object>> updateDataList) {
		this.updateDataList = updateDataList;
	}

	/**
	 * 更新数据授权
	 * @return
	 */
	public String updateDataProfileInfoAction() {
		updateDataList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = new ArrayList<Object[]>();

		list = tmpProfileService.updateDataProfile(id);
		for (Object[] obj : list) {
			TProfileTemp profileTemp = (TProfileTemp) obj[0];// 获取ID
			TRole tmpRole = (TRole) obj[1];// 临时授权角色
			TUser user = (TUser) obj[2];// 创建者
			TRole role = (TRole) obj[4];
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

			String statTime = "";
			if (profileTemp.getStartTime() != null) {
				statTime = timeFormat.format(profileTemp.getStartTime());
			}
			String endTime = "";
			if (profileTemp.getEndTime() != null) {
				endTime = timeFormat.format(profileTemp.getEndTime());
			}
			map.put("id", profileTemp.getId());
			map.put("roleId", role.getId());
			map.put("tmpRoleId", tmpRole.getId());
			map.put("userId", user.getLoginId());
			map.put("userName", user.getName());
			map.put("startTime", statTime);// 开始时间
			map.put("endTime", endTime);// 结束时间
			updateDataList.add(map);
		}
		return SUCCESS;
	}
}
