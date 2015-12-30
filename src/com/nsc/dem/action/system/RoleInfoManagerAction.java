package com.nsc.dem.action.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.util.DateUtils;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.system.IroleService;

/**
 * 角色信息管理
 * 
 * @author Administrator
 * 
 */
public class RoleInfoManagerAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;

	// 行数和页数
	private int page;
	private int rows;

	// 前台传来的条件
	private String roleId;
	private String description;
	private String insertRoelId;
	private String updateId;

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public void setInsertRoelId(String insertRoelId) {
		this.insertRoelId = insertRoelId;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	IroleService roleService;

	
	public void setRoleService(IroleService roleService) {
		this.roleService = roleService;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	private String codes;

	public void setCodes(String codes) {
		this.codes = codes;
	}

	private String returnValue;

	public String getReturnValue() {
		return returnValue;
	}

	private TRole role;

	public TRole getRole() {
		return role;
	}

	public void setRole(TRole role) {
		this.role = role;
	}

	private List<Map<String, Object>> updateList;

	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public void setUpdateList(List<Map<String, Object>> updateList) {
		this.updateList = updateList;
	}

	public List<Map<String, Object>> getUpdateList() {
		return updateList;
	}

	/**
	 * 查询角色信息管理 分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectRoleInfoAction() throws Exception {
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> searchlist = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();

		String ption = "%" + description + "%";

		map.put("rid", roleId);

		map.put("description", ption);
		searchlist = roleService.queryRoleInfoList(map, firstResult,
				maxResults, tablebean);
		List<RowBean> rowsList = new ArrayList<RowBean>();

		if (searchlist != null) {
			for (Object[] obj : searchlist) {
				TRole role = (TRole) obj[0];
				TDictionary dic = (TDictionary) obj[1];
				TUser user = (TUser) obj[2];
				RowBean rowbean = new RowBean();
				String createDate = DateUtils
						.DateToString(role.getCreateDate());
				rowbean.setCell(new Object[] {
						role.getId(),
						role.getName(),
						role.getDescription(),
						dic.getName(),
						user.getName(),
						createDate,
						"<a href='#'  onclick='insertRoleShowDialog(\""
								+ role.getId()
								+ "\")' >编辑</a>"

				});
				// 当前的ID为1
				rowbean.setId(role.getId());
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
	 * 
	 * 删除角色信息
	 * 
	 * @return
	 */
	public String deleteRoleInfoAction() {

		String[] codeList = codes.split(",");

		for (int i = 0; i < codeList.length; i++) {
			String eachCode = codeList[i];
			roleService.deleteRoleInfo(roleService, eachCode);
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 
	 * 插入角色信息
	 * 
	 * @return
	 */
	public String insertRoleInfoAction() {
		role.setCreator(super.getLoginUser().getLoginId());
		role.setCreateDate(new Date());
		if (updateId == null || (updateId.equals(""))) {
			roleService.insertEntity(role);
		} else {

			roleService.updateEntity(role);
		}

		returnValue = "true";
		return SUCCESS;
	}

	/**
	 * 查询当前角色是否存在
	 * 
	 * @return
	 */
	public String selectRoleBooleanAction() {
		TRole role = (TRole) roleService.EntityQuery(TRole.class, insertRoelId);
		if (role != null) {
			returnValue = "true";
		} else {
			returnValue = "false";
		}
		return SUCCESS;

	}

	/**
	 * 更新时查询
	 * 
	 * @return
	 */
	public String updateSelectRoleAction() {
		updateList = new ArrayList<Map<String, Object>>();
		TRole role = (TRole) roleService.EntityQuery(TRole.class, insertRoelId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", role.getId());
		map.put("roleName", role.getName());
		map.put("description", role.getDescription());
		map.put("baomi", role.getSecurity());
		updateList.add(map);
		return SUCCESS;
	}

	public String getUserByRoleIdAction() {
		list = new ArrayList<Map<String, Object>>();
		List<TUser> userList = roleService.getUserByRoleId(roleId);
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TUser user = userList.get(i);
			map.put("username", user.getName());
			map.put("loginId", user.getLoginId());
			map.put("uname", user.getTUnit().getName());
			map.put("duty", user.getDuty());
			map.put("isValid", user.getIsValid());
			list.add(map);
		}

		return SUCCESS;
	}
}
