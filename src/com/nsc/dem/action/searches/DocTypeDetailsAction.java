package com.nsc.dem.action.searches;

import java.util.HashMap;
import java.util.List;

import com.nsc.dem.action.BaseAction;

import com.nsc.dem.action.system.TMenu;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.searches.IsearchesService;

public class DocTypeDetailsAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private List<TMenu> tm;

	private IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public List<TMenu> getTm() {
		return tm;
	}

	public void setTm(List<TMenu> tm) {
		this.tm = tm;
	}

	private String options;

	public void setOptions(String options) {
		this.options = options;
	}

	private Long nodeId;

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 菜单 快速定位菜单 设置
	 * 
	 * @return
	 */
	public String menu() {
		String fastURL = "search/parentType.action?nodeId=";
		TUser user = super.getLoginUser();

		String roleId = user.getTRole().getId();
		tm = searchesService.quarryUserTreeList(user.getLoginId(),roleId);


		for (int i = 0; i < tm.size(); i++) {
			TMenu menu = tm.get(i);
			String id = menu.getMap().get("id") + "";

			String url = fastURL + id;
			menu.setUrl(url);
			menu.setId(id);
		}

		getRequest().setAttribute("tm", tm);
		return "left";

	}



	/**
	 * 当前类型下的父节点
	 * 
	 * @return
	 */
	private List<Object> parentList;

	public List<Object> getParentList() {
		return parentList;
	}

	public void setParentList(List<Object> parentList) {
		this.parentList = parentList;
	}

	public String parentTypeAction() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (options != null) {
			String[] optionsArray = options.split(";");
			for (int i = 0; i < optionsArray.length; i++) {
				String[] tempString = optionsArray[i].split("=");
				String key = tempString[0];
				String value = tempString[1];
				map.put(key, value);
			}
		}

		parentList = searchesService.treeDefList(nodeId, map.isEmpty(), map);

		return SUCCESS;
	}

	private String returnValue = "";

	public String getReturnValue() {
		return returnValue;
	}

}
