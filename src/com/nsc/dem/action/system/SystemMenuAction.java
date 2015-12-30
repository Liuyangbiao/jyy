package com.nsc.dem.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IprofileService;
import com.nsc.dem.util.xml.IntenterXmlUtils;

public class SystemMenuAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private List<TMenu> tMenuList;
	private List<TMenu> tm;
	private IprofileService profileService;
	private IsearchesService searchesService;
	public static final int height = 32;
	public static final int heightAll = 330;
	private String areaId;
	private List<Map<String,Object>> provinces;

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setProvinces(List<Map<String, Object>> provinces) {
		this.provinces = provinces;
	}

	
	public List<Map<String, Object>> getProvinces() {
		return provinces;
	}

	public IprofileService getProfileService() {
		return profileService;
	}

	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public List<TMenu> gettMenuList() {
		return tMenuList;
	}

	public void settMenuList(List<TMenu> tMenuList) {
		this.tMenuList = tMenuList;
	}

	/**
	 * 菜单 快速定位菜单 设置
	 * 
	 * @return
	 */
	public String menu() {
		String fastURL = Configurater.getInstance().getConfigValue("fastURL");
		TUser user = super.getLoginUser();
		String roleId = user.getTRole().getId();
		String isLocal = user.getIsLocal();
		tMenuList = profileService.queryUserAllProList(user.getLoginId(), null,
				"P01",roleId,isLocal);
		for (int i = 0; i < tMenuList.size(); i++) {
			TMenu menu = tMenuList.get(i);
			if (!menu.isUse()) {
				tMenuList.remove(i);
				i=i-1;
			}
		}
		tm = searchesService.quarryUserTreeList(user.getLoginId(),roleId);
		for (int i = 0; i < tm.size(); i++) {
			TMenu menu = tm.get(i);
			String id = menu.getMap().get("id") + "";
			String url = fastURL + id;
			menu.setUrl(url);
		}
		getRequest().setAttribute("height",
				(heightAll - (tMenuList.size() * height)) + "px");
		getRequest().setAttribute("tMenuList", tMenuList);
		getRequest().setAttribute("tm", tm);
		return "left";

	}

	/**
	 * 根据areaId取得所有下属省公司
	 * @return code,name
	 */
	public String getProvincesByAreaId(){
		provinces = new ArrayList<Map<String,Object>>();
		List<Object[]> list = searchesService.getProvincesByAreaId(areaId); 
		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			String appIp = IntenterXmlUtils.getAppServerAdd(obj[0].toString());
			map.put("code", obj[0]);
			map.put("name", obj[1]);
			map.put("appIp", appIp);
			provinces.add(map);
		}
		return SUCCESS;
	}
	public List<TMenu> getTm() {
		return tm;
	}

	public void setTm(List<TMenu> tm) {
		this.tm = tm;
	}
}
