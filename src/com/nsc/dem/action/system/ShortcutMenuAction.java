package com.nsc.dem.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TUserShortcut;
import com.nsc.dem.service.system.IprofileService;
import com.nsc.dem.util.log.Logger;

public class ShortcutMenuAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private List<Map<String,Object>> list;
	private List<TUserShortcut> tusList;
    private String menuIds;
    private IprofileService profileService;
    
	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
  
	/**
	 * 显示该登录用户下,准备添加或者更新的快捷菜单
	 * ListBox 左边
	 * @return
	 */
	public String newMenu(){
		list=new ArrayList<Map<String,Object>>();
		String id=getRequest().getParameter("id");  
		boolean flag=false;
			long tid=Long.parseLong(id);
			//得到该用户已有的快捷菜单
			List<TUserShortcut> tus=profileService.queryShortcutByUser(super.getLoginUser().getLoginId());
			for(int i=0;i<tus.size();i++){
				TUserShortcut ts=(TUserShortcut)tus.get(i);
				long tPid=Long.parseLong(ts.getTProfile().getId());
				//判断是否该菜单已在 快捷菜单栏中 
				if(tPid==tid){
					flag=true;
				}
			}
			// 如果在 则不在左边显示
			if(!flag){
				TProfile tProfile=new TProfile();
				tProfile.setId(id);
				Object tpfList= profileService.EntityQuery(TProfile.class,id);
				TProfile tpf=(TProfile)tpfList;
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("code", tpf.getId());
				map.put("name", tpf.getName());
				list.add(map);
			}
		return "list";
	}
	
	/**
	 * 显示该登录用户下  已有的快捷菜单
	 * ListBox 右边
	 * @return
	 */
	public String initMenu(){
		list=new ArrayList<Map<String,Object>>();
		List<TUserShortcut> tus=profileService.queryShortcutByUser(super.getLoginUser().getLoginId());
		for(int i=0;i<tus.size();i++){
			TUserShortcut ts=(TUserShortcut)tus.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("code", ts.getTProfile().getId());
			map.put("name", ts.getTProfile().getName());
			list.add(map);
		}
		return "list";
	}
	
	/**
	 * 初始化top页面处, 快捷菜单的显示
	 * @return
	 */
    public String initTop(){
    	String homePage=Configurater.getInstance().getConfigValue("homePage");
    	getRequest().setAttribute("homePage", homePage);
    	TUser user=super.getLoginUser();
    	tusList=profileService.queryShortcutByUser(user.getLoginId());
    	return "top";
    }
    
    /**
     * 更新快捷菜单时  调用此方法
     * @return
     */
	public String  edit(){
		//menuIds 为ListBox 右边 所有菜单的ID  其中以,分开
		String[] str=menuIds.split(",");
		TUserShortcut tTUserShortcut=new TUserShortcut();
		tTUserShortcut.setTUser(getLoginUser());
		List<Object> pfsList=profileService.EntityQuery(tTUserShortcut);
		Logger logger=super.logger.getLogger(ShortcutMenuAction.class);
		//先删除已有快捷菜单
		for(Object obj:pfsList){
			profileService.delEntity(obj);
		}
		//如果ListBox右边没有任何内容,则不作添加
		if(!"".equals(menuIds)){
			for(int i=0;i<str.length;i++){
				TUserShortcut tus=new TUserShortcut();
				tus.setTUser(getLoginUser());
				tus.setShortOrder((long)(i+1));
				TProfile tProfile=new TProfile();
				tProfile.setId(str[i]);
				Object tpfList= profileService.EntityQuery(TProfile.class,str[i]);
				TProfile tpf=(TProfile)tpfList;
				tus.setTProfile(tpf);
				profileService.insertEntity(tus);
				logger.info("放置快捷方式： "+str[i]);
			}
		}
		return "list";
	}
	
	/**
	 * 控制页面设置成快捷菜单是否能点
	 * @return
	 */
	public String imgDisplay(){
		list=new ArrayList<Map<String,Object>>();
		String id=getRequest().getParameter("id");  
		boolean flag=false;
		long tid=Long.parseLong(id);
		//得到该用户已有的快捷菜单
		List<TUserShortcut> tus=profileService.queryShortcutByUser(super.getLoginUser().getLoginId());
		for(int i=0;i<tus.size();i++){
			TUserShortcut ts=(TUserShortcut)tus.get(i);
			long tPid=Long.parseLong(ts.getTProfile().getId());
			//判断是否该菜单已在 快捷菜单栏中 
			if(tPid==tid){
				flag=true;
			}
		}
		if(!flag){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("code", "success");
			list.add(map);
		}
		return "list";
	}
	
	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public List<TUserShortcut> getTusList() {
		return tusList;
	}

	public void setTusList(List<TUserShortcut> tusList) {
		this.tusList = tusList;
	}
}
