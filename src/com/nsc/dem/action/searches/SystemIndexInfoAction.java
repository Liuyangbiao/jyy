package com.nsc.dem.action.searches;

import com.nsc.base.util.DateUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.searches.IsearchesService;

public class SystemIndexInfoAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String loginId;

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	private String unitId;
	
	private String searchType;

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	IsearchesService searchesService;
	int count = 0;

	public int getCount() {
		return count;
	}

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	List<Map<String, Object>> browseList;

	public List<Map<String, Object>> getBrowseList() {
		return browseList;
	}

	public void setBrowseList(List<Map<String, Object>> browseList) {
		this.browseList = browseList;
	}

	private List<Integer> countList;

	public List<Integer> getCountList() {
		return countList;
	}

	/**
	 * 新增档案数Action
	 * 
	 * @return 档案数
	 */
	public String insertDocCountAction() {
		countList = new ArrayList<Integer>();
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String beginTime = timeFormat.format(super.getLoginUser()
				.getLastLoginTime());
		int doccount = searchesService.selectInsertDocCount(beginTime, unitId);
		int total = searchesService.selectInsertDocCount(null, unitId);//本地数据
		int synTotal = searchesService.selectInsertDocCountBySyn(null, unitId);
		total += synTotal;
		//新增的档案
		countList.add(doccount);
		countList.add(total);
		return SUCCESS;
	}

	private List<Integer> projectCountList;

	public List<Integer> getProjectCountList() {
		return projectCountList;
	}

	/**
	 * 新增工程数Action
	 * 
	 * @return 工程数
	 */
	public String insertProjectCountAction() {
		projectCountList = new ArrayList<Integer>();
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String beginTime = timeFormat.format(super.getLoginUser()
				.getLastLoginTime());
		int projectcount = searchesService.selectInsertProjectCount(beginTime,
				unitId);
		int total = searchesService.selectInsertProjectCount(null, unitId);
		//新增的工程
		projectCountList.add(projectcount);
		projectCountList.add(total);
		return SUCCESS;
	}

	/**
	 * 查询最近浏览文档
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws URIException 
	 */
	public String selectBrowseOperateLogAction() throws UnsupportedEncodingException, URIException {
		browseList = new ArrayList<Map<String, Object>>();
		List<TOperateLog> logs = searchesService
				.selectBrowseOperateLog(loginId);
		for (int i = 0; i < logs.size(); i++) {
			TOperateLog operateLog = logs.get(i);
			String[] contents = operateLog.getContent().split(";");
			
			String docid = contents[1].split(":")[1];
			String projectid = contents[2].split(":")[1];
			String companyid = contents[3].split(":")[1];
			String name = contents[4].split(":")[1];
			String path = contents[5].split(":")[1];
			String suffix = contents[6].split(":")[1];
			
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat timeFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			String datetime = (timeFormat.format(operateLog.getOperateTime()));
			map.put("datetime", datetime);
			map.put("docid", docid);
			map.put("projectid", projectid);
			map.put("companyid", companyid);
			map.put("name", name);
			map.put("path", URIUtil.encodePath(path,"UTF-8"));
			map.put("suffix", suffix);

			browseList.add(map);
		}

		return SUCCESS;
	}

	private List<Map<String, Object>> userInfoList;

	/**
	 * 用户信息
	 * @return
	 */
	public String userInfoAction() {
		userInfoList = new ArrayList<Map<String, Object>>();

		List<TUser> userList = searchesService.getTUserByLoginIdTop10(loginId);
		for (int i = 0; i < userList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			TUser user = userList.get(i);
			map.put("loginId", user.getLoginId());
			map.put("loginCount", user.getLoginCount());
			map.put("loginName", user.getName());
			SimpleDateFormat timeFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Timestamp date = user.getLoginTime();			
			String loginTime =date!=null?(timeFormat.format(date)):"";
			map.put("lastloginTime", loginTime);
			userInfoList.add(map);
		}

		return SUCCESS;
	}

	public List<Map<String, Object>> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<Map<String, Object>> userInfoList) {
		this.userInfoList = userInfoList;
	}

	private List<Object[]> docCountList;

	public List<Object[]> getDocCountList() {
		return docCountList;
	}

	/**
	 * 查询档案数，以柱状图显示	
	 * @return
	 */
	public String selectInsertDocPicAction() {
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("unit", unitId);
		if(this.getSearchType()!=null)
		docCountList = searchesService.selectInsertDocPic(map,this.getSearchType());
		

		if(docCountList.size() == 1 && docCountList.get(0)[0].toString().indexOf("-") != -1){
			
			Object[] objs=docCountList.get(0);
			String ymonth=(String)objs[0];
			
			//计算得出上个月
			Date date=DateUtils.StringToDate(ymonth, "yyyy-MM");
			Date lastDate=DateUtils.StringToDate(ymonth, "yyyy-MM");
			DateUtils.getEndMonthDate(lastDate);
			int days=(int) DateUtils.getDistanceDays(date, lastDate)+1;
			date=DateUtils.getDayAfterDays(lastDate, days*-1);
			
			docCountList.add(0,new Object[]{DateUtils.DateToString(date,"yyyy-MM"),0});
		}
		
		return SUCCESS;
	}

	private List<Object[]> projectandDocCount;

	private List<Object[]> newProjectCount;

	/**
	 * 查询工程数以饼形图显示 
	 * @return
	 */
	public String selectProjectDocCountAction() {
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("unit", unitId);
		if(this.getSearchType()!=null)
			newProjectCount = searchesService.selectInsertDocPic(map,this.getSearchType());
		return SUCCESS;
	}

	public List<Object[]> getNewProjectCount() {
		return newProjectCount;
	}

	public void setNewProjectCount(List<Object[]> newProjectCount) {
		this.newProjectCount = newProjectCount;
	}

	public List<Object[]> getProjectandDocCount() {
		return projectandDocCount;
	}
}
