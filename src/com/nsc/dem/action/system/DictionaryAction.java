package com.nsc.dem.action.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.DictionaryBean;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.system.IdictionaryService;
import com.nsc.dem.service.system.IroleService;

/**
 * 
 * 系统管理——数据字典
 * 
 * @author ibm
 * 
 */
public class DictionaryAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;
	// 删除传来的codes
	String codes;
	String returnValue;
	// 传来的下拉列表框的值
	String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	String dicname;

	public void setDicname(String dicname) {
		this.dicname = dicname;
	}

	String diccode;

	public String getDiccode() {
		return diccode;
	}

	public void setDiccode(String diccode) {
		this.diccode = diccode;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	private String updateCode;

	public void setUpdateCode(String updateCode) {
		this.updateCode = updateCode;
	}

	// 默认的页数
	private int page;

	public void setPage(int page) {
		this.page = page;
	}

	private int rows;

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 定义数据字典的类
	private TDictionary tdictionary = new TDictionary();

	public TDictionary getTdictionary() {
		return tdictionary;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setTdictionary(TDictionary tdictionary) {
		this.tdictionary = tdictionary;
	}

	// 传来的code
	String code;

	// 角色表
	private List<TRole> tRoleList;

	public void settRoleList(List<TRole> tRoleList) {
		this.tRoleList = tRoleList;
	}

	IdictionaryService dictionaryService;

	IroleService roleService;

	public void setRoleService(IroleService roleService) {
		this.roleService = roleService;
	}

	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	// 返回的json

	private List<Map<String, Object>> rolePriority;

	public List<Map<String, Object>> getRolePriority() {
		return rolePriority;
	}

	public void setRolePriority(List<Map<String, Object>> rolePriority) {
		this.rolePriority = rolePriority;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	// 返回下列拉表框中的值
	private List<DictionaryBean> selectList;

	public List<DictionaryBean> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<DictionaryBean> selectList) {
		this.selectList = selectList;
	}

	private List<Map<String, Object>> updateList;

	public List<Map<String, Object>> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Map<String, Object>> updateList) {
		this.updateList = updateList;
	}

	/**
	 * 查询权限，更改时，给信息赋值
	 * 
	 * @return
	 */
	public String getRoleProioityAction() {

		rolePriority = new ArrayList<Map<String, Object>>();

		TDictionary dic = (TDictionary) dictionaryService.EntityQuery(
				TDictionary.class, code);
		tRoleList = roleService.queryTRoleList(null);
		if (dic != null) {
			rolePriority = roleService.getRolePriority(dic, dic
					.getAuthControl(), tRoleList);
		} else {
			rolePriority = roleService.getRolePriority(dic, null, tRoleList);
		}

		return SUCCESS;
	}

	/**
	 * 
	 * 查询数据字典的action
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public String getDictionaryDataAction() throws Exception {

		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getRequest().setCharacterEncoding("utf-8");
		// 起始位置为：(页码-1)*rows
		int firstResult = (page - 1) * rows;
		// 终止位置为：页码*行数-1
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();

		String tempName = dicname == null ? "%" : "%" + dicname + "%";

		Object[] object;
		String flog = "";
		if (diccode == null || diccode.equals("0")) {
			diccode = null;
			flog = "0";
			object = new Object[] { tempName };
		} else {
			object = new Object[] { diccode, tempName };
		}
		list = dictionaryService.querydictionaryList(object, firstResult,
				maxResults, tablebean, flog);

		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {

			for (Object[] obj : list) {
				RowBean rowbean = new RowBean();

				TDictionary tdic = (TDictionary) obj[0];
				String temptime = tdic.getCreateDate().toString();
				String[] create = temptime.split(" ");

				String authControle = roleService
						.getfieldSelectAuthControl(tdic.getAuthControl());
				rowbean.setCell(new Object[] {

						tdic.getCode(),
						tdic.getName(),
						authControle,
						tdic.getTUser().getName(),

						create[0],
						tdic.getRemark(),
						"<a href='#' onclick='updateDictionary(\""
								+ tdic.getCode()
								+ "\")' >更新</a>" });

				// 当前的ID为1
				rowbean.setId(tdic.getCode());
				rowsList.add(rowbean);
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
		}

		return SUCCESS;
	}

	/**
	 * 
	 * 查询出所有父类的编码，放在下拉列表框 中
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getSelectDictionaryAction()
			throws UnsupportedEncodingException {
		selectList = new ArrayList<DictionaryBean>();
		List<Object> list = dictionaryService.dictionaryInfoList();
		for (int i = 0; i < list.size(); i++) {
			DictionaryBean dicbean = new DictionaryBean();
			TDictionary tdic = (TDictionary) list.get(i);
			dicbean.setCode(tdic.getCode());
			dicbean.setName(tdic.getName());
			selectList.add(dicbean);
		}

		return SUCCESS;
	}

	/**
	 * 
	 * 更新插入action
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getUpdateDictionaryAction()
			throws UnsupportedEncodingException {
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getRequest().setCharacterEncoding("utf-8");

		// 创建者
		tdictionary.setTUser(super.getLoginUser());

		tdictionary.setCreateDate(new Date());
		if (updateCode == null || updateCode.equals("")) {
			dictionaryService.insertEntity(tdictionary);
		} else {
			dictionaryService.updateEntity(tdictionary);
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 删除数据字典
	 * 
	 * @return
	 */
	public String getDeleteDictionaryAction() {

		String[] codeList = codes.split(",");

		dictionaryService.dictionaryDel(codeList);

		returnValue = "success";
		return SUCCESS;

	}

	/**
	 * 
	 *查询当前字典编码是否存在
	 * 
	 * @return
	 */
	public String selectDictionaryCodeAction() {
		TDictionary tdic = (TDictionary) dictionaryService.EntityQuery(
				TDictionary.class, checkCode);
		if (tdic != null) {
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
	public String getDictionaryByCodeAction() {
		updateList = new ArrayList<Map<String, Object>>();
		TDictionary dic = (TDictionary) dictionaryService.EntityQuery(
				TDictionary.class, code);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pCode", dic.getParentCode());
		map.put("code", dic.getCode());
		map.put("dicName", dic.getName());
		map.put("remark", dic.getRemark());
		updateList.add(map);
		return SUCCESS;
	}
}
