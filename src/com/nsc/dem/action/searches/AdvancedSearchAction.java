package com.nsc.dem.action.searches;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.util.DateUtils;
import com.nsc.base.util.GetCh2Spell;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.archives.TUserQuery;
import com.nsc.dem.bean.archives.TUserQueryId;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IdictionaryService;
import com.nsc.dem.service.system.IprofileService;

public class AdvancedSearchAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String menuId;
	public String getMenuId() {
		return menuId;
	}
	IprofileService profileService; 
	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	IdictionaryService dictionaryService;
	IsearchesService searchesService;
	private List<Map<String,Object>> list;
	private String format;
	
	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	
	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}


	/**
	 * 初始化查询条件
	 * @return
	 */
	public String queryName(){
		String loginId=super.getLoginUser().getLoginId();
		list=new ArrayList<Map<String,Object>>();
		@SuppressWarnings("unchecked")
		List tUserQueryList=searchesService.queryByLoginId(loginId);
	
		for(int i=0;i<tUserQueryList.size();i++){
			//得到查询条件名称,由于要去掉重复的,所以查询时 只查询了queryName,loginId
			//两个字段,因此这里获取名字时 有点 不一样
			String name=((Object[])tUserQueryList.get(i))[0].toString();
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id", name);
			map.put("name", name);
			map.put("spell", GetCh2Spell.getBeginCharacter(name));
			list.add(map);
		}
		return "list";
	}
	
	/**
	 * 查询条件设置
	 * @return
	 */
	public String query(){
		@SuppressWarnings("unchecked")
		Map<String,Object> map=getRequest().getParameterMap();
		
		Map<String,Object> mapCondition=new HashMap<String,Object>();
		
		for(String t:map.keySet()){  
			String[] params=(String[]) map.get(t);
			if(!"".equals(params[0])){
				getRequest().setAttribute(t, params[0]);
				mapCondition.put(t, params[0]);
				//下面主要是要与  查询语句中的字段对应
				//如果是文件格式
				if("format".equals(t)){
					mapCondition.put("format", params);
					String fm="";
					for(int i=0;i<params.length;i++){
						fm=fm+params[i]+",";
					}
					getRequest().setAttribute("format", fm);
				}
				//如果是文件大小单位
				if("fileSize".equals(t)){
					String[] fsj=(String[]) map.get("fileSizeJudge");
					String[] fsu=(String[]) map.get("fileSizeUnits");
					Double fileSize=Double.parseDouble(params[0]);
					if("K".equals(fsu[0])){
						fileSize=fileSize*1024;
					}else if("M".equals(fsu[0])){
						fileSize=fileSize*1024*1024;
					}
					if("<".equals(fsj[0])){
						mapCondition.put("max_file_size", fileSize);
					}else if(">".equals(fsj[0])){
						mapCondition.put("min_file_size", fileSize);
					}
				}
				if("fileName".equals(t)){ //文件名
					mapCondition.put("dname", '%'+params[0]+'%');
				}
				if("statussCode".equals(t)){//工程阶段
					mapCondition.put("status", params[0]);
				}
				if("projectTypeCode".equals(t)){//工程分类
					mapCondition.put("type", params[0]);
				}
				if("voltageLevelCode".equals(t)){//电压等级
					mapCondition.put("voltage_level", params[0]);
				}
				if("projectName".equals(t)){//工程名称
					mapCondition.put("pname", '%'+params[0]+'%');
				}
				if("unname".equals(t)){ //业主单位
					mapCondition.put("unname", '%'+params[0]+'%');
				}
				if("creator".equals(t)){//创建者
					mapCondition.put("uname", '%'+params[0]+'%');
				}
				if("proTypeCode".equals(t)){//专业
					mapCondition.put("speciality", params[0]);
				}
				if("from".equals(t)){
					mapCondition.put("begin_create_date", params[0]);
				}
				if("to".equals(t)){
					mapCondition.put("end_create_date", params[0]);
				}
				if("docTypeCode".equals(t)){//文档类型
					mapCondition.put("doc_type", params[0]);
				}
				if("recordTypeCode".equals(t)){//档案分类
					mapCondition.put("child_doc_type", params[0]);
				}
			}
	     }  
		getRequest().getSession().setAttribute("condition", mapCondition);
		return "result";
	}
	
	/**
	 * 保存查询条件
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String saveQuery() throws UnsupportedEncodingException{
		//查询条件名称
		String queryName=java.net.URLDecoder.decode(getRequest().getParameter("queryName"),"UTF-8");
		String loginId=super.getLoginUser().getLoginId();
		@SuppressWarnings("unchecked")
		Map<String,Object> map=getRequest().getParameterMap();
		List<TUserQuery> tuqList=searchesService.queryByLoginIdAndName(loginId, queryName);
		if(tuqList==null || tuqList.size()==0){
			for(String t:map.keySet()){  
				String value=((String[])map.get(t))[0];
				if(value.indexOf("%")>=0){
					value=java.net.URLDecoder.decode(value,"UTF-8");
				}
				//如果文件格式  则判断选择了哪几个
				if("format".equals(t)){
					String[] ff=(String[]) map.get("format");
					value="";
					if(ff.length>0){
						for(int i=0;i<ff.length ;i++){
							value=value+ff[i]+",";
						}
					}
				}
				
				if(!"".equals(value)){
					TUserQuery tuq=new TUserQuery();
					tuq.setCreateDate(new Date());
					tuq.setQueryParams(value);
					TUserQueryId tuqId=new TUserQueryId();
					tuqId.setLoginId(loginId);
					tuqId.setQueryKey(t.toString());
					tuqId.setQueryName(queryName);
					tuq.setId(tuqId);
					searchesService.insertEntity(tuq);
				}
		     }  
		}else{
			for(TUserQuery tuq:tuqList){
				searchesService.delEntity(tuq);
			}
			for(Object t:map.keySet()){  
				String value=((String[])map.get(t))[0];
				//如果文件格式  则判断选择了哪几个
				if("format".equals(t)){
					String[] ff=(String[]) map.get("format");
					value="";
					if(ff.length>0){
						for(int i=0;i<ff.length ;i++){
							value=value+ff[i]+",";
						}
					}
				}
				if(!"".equals(value)){
					TUserQuery tuq=new TUserQuery();
					tuq.setCreateDate(new Date());
					tuq.setQueryParams(value);
					TUserQueryId tuqId=new TUserQueryId();
					tuqId.setLoginId(loginId);
					tuqId.setQueryKey(t.toString());
					tuqId.setQueryName(queryName);
					tuq.setId(tuqId);
					searchesService.insertEntity(tuq);
				}
		     } 
		}
	     return "list";
	}
	
	/**
	 * 删除查询条件
	 */
	public String deleteQuery(){
		String queryName=getRequest().getParameter("queryName");
		List<TUserQuery> tuqList=(List<TUserQuery>)searchesService.queryByLoginIdAndName(super.getLoginUser().getLoginId(), queryName);
		for(TUserQuery tuq:tuqList){
			searchesService.delEntity(tuq);
		}
		return "list";
	}
	
	/**
	 * 查询条件载入
	 */
	public String loading(){
		String queryName=getRequest().getParameter("queryName");
		List<TUserQuery> tuqList=(List<TUserQuery>)searchesService.queryByLoginIdAndName(super.getLoginUser().getLoginId(), queryName);
		list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<tuqList.size();i++){
			TUserQuery tUserQuery=(TUserQuery)tuqList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("queryKey", tUserQuery.getId().getQueryKey());
			map.put("queryParams", tUserQuery.getQueryParams());
			list.add(map);
		}
		return "list";
	}
	
	/**
	 * 查询结果展示
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String show() throws Exception{

        Map<String,Object> map=(Map<String,Object>)getRequest().getSession().getAttribute("condition");
        map.put("uncode", super.getLoginUser().getTUnit().getProxyCode());

		// 起始位置为：(页码-1)*rows
		int firstResult = (page - 1) * rows;
		// 终止位置为：页码*行数-1
		int maxResults = page * rows ;

		List<Object[]> list = new ArrayList<Object[]>();
		
		list = searchesService.queryBasicList(map, firstResult, maxResults,tablebean);

		List rowsList = new ArrayList();
		
		for (Object[] objs : list) {
			TDoc tdoc = (TDoc) objs[0];
			TProject project = (TProject) objs[1];
			TDocType docType = (TDocType) objs[2];
			RowBean rowbean = new RowBean();
			rowbean
					.setCell(new Object[] {
							tdoc.getName(),
							docType == null ? "" : docType.getName(),
							project == null ? "" : project.getName(),
							tdoc.getFormat(),
							DateUtils.DateToString(tdoc.getCreateDate()),
							tdoc.getFileSize(),
							"查看|<a href='#' id='dialog_link' onclick='previewImage("+tdoc.getId()+")'>预览</a>" }); 
			// 当前的ID为1
			rowbean.setId(tdoc.getId() + "<>" + project.getId() + "<>"
					+ project.getTUnitByOwnerUnitId().getCode() + "<>" + tdoc.getName()
					+ "<>" + tdoc.getPath() + "<>" + tdoc.getSuffix());
			rowsList.add(rowbean);
		}
		// 给单元格里存放数据
		tablebean.setRows(rowsList);

		// 当前页是1页
		tablebean.setPage(this.page);

		// 查询出的记录数为100
		//tablebean.setRecords(records.intValue());
		
		int records=tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records%rows==0?records/rows:records/rows+1);

		return "tab";
	}
	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
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

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
