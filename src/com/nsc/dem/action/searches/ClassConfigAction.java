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
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TNodeDef;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IdictionaryService;
import com.nsc.dem.service.system.IsortSearchesService;

public class ClassConfigAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private List<Map<String,Object>> list;
	
	private String typeName;
	private String source;
	private String remark;
	private String params;
	IsearchesService searchesService;
	IsortSearchesService sortSearchesService;
	IdictionaryService dictionaryService;
	
	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}
	
	public void setSortSearchesService(IsortSearchesService sortSearchesService) {
		this.sortSearchesService = sortSearchesService;
	}
    
	/**
	 * 添加或者更新分类定义节点
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String add() throws UnsupportedEncodingException{
		TNodeDef tnd=new TNodeDef();
		tnd.setName(typeName);
		tnd.setSource(source);
		tnd.setRemark(remark);
		tnd.setParams(params);
		tnd.setTUser(getLoginUser());tnd.setCreateDate(new Date());
		tnd.setSqlPlace("02");
		String nodeId=getRequest().getParameter("nodeId");
		if("".equals(nodeId)|| nodeId==null){
			searchesService.insertEntity(tnd);
		}else{
			tnd.setId(Long.parseLong(nodeId));
			searchesService.updateEntity(tnd);
		}
	
		return "list";
	}
	
	/**
	 * 对分类定义表做 更新操作
	 */
	public String edit(){
		String id=getRequest().getParameter("id");
		TNodeDef tNodeDef=(TNodeDef)sortSearchesService.EntityQuery(TNodeDef.class, Long.parseLong(id));
		typeName=tNodeDef.getName();
		source=tNodeDef.getSource();
		remark=tNodeDef.getRemark();
		params=tNodeDef.getParams();
		getRequest().setAttribute("nodeId", id);
		return "edit";
	}
	
	/**
	 * 对分类定义表做删除操作
	 */
	public String del(){
		String ids=getRequest().getParameter("id");
		String[] sIds=ids.split(",");
		for(int i=0;i<sIds.length;i++){
			TNodeDef tnd=(TNodeDef)sortSearchesService.EntityQuery(TNodeDef.class, Long.parseLong(sIds[i]));
			sortSearchesService.delEntity(tnd);
		}
		return "del";
	}
	
	/**
	 * 查询
	 */
	public String show(){
		
	      return "del"	;
	}
	
	/**
	 * 判断输入的名字是否存在!
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public String existence() throws UnsupportedEncodingException{
	    String name=getRequest().getParameter("id");
	    list=new ArrayList<Map<String,Object>>();
	    if(!("".equals(name) || name==null)){
	    	TNodeDef tNodeDef=new TNodeDef();
	    	List<Object> tNodeDefList=sortSearchesService.EntityQuery(tNodeDef);
	    	for(Object tnd:tNodeDefList){	
	    		TNodeDef td=(TNodeDef)tnd;
	    		String nodeName=td.getName();
	    		if(nodeName.equals(name)){
	    			Map<String,Object> map= new HashMap<String,Object>();
	    			map.put("message", "该名称已存在,请重新输入");
	    			list.add(map);
	    		}
	    	}
	    }
		return "list";
	}
	
	/**
	 * 查询数据字典里所有parentCode为空的数据
	 * 作为数据字典的查询条件在页面显示
	 * @return
	 */
	public String queryDictionary(){
		List<Object> tList=dictionaryService.dictionaryInfoList();
		list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<tList.size();i++){
			TDictionary tDictionary=(TDictionary)tList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("code", tDictionary.getCode());
			map.put("name", tDictionary.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tDictionary.getName()));
			list.add(map);
		}
		return "list";
	}
	
	/**
	 *  查询所有parentCode是XX的数据字典数据
	 *  用于列表显示
	 * @return
	 */
	public String resultDictionary(){
		String code=getRequest().getParameter("id");
		list=new ArrayList<Map<String,Object>>();
		TDictionary td=(TDictionary) dictionaryService.EntityQuery(TDictionary.class, code);
		List<TDictionary> tDictionaryList=dictionaryService.dictionaryList(td.getName());
		for(int i=0;i<tDictionaryList.size();i++){
			TDictionary tDictionary=tDictionaryList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("code", tDictionary.getCode());
			map.put("name", tDictionary.getName());
			map.put("auth", tDictionary.getAuthControl());
			map.put("createDate", DateUtils.DateToString(tDictionary.getCreateDate()));
			map.put("creator", tDictionary.getTUser().getName());
			map.put("parentCode", tDictionary.getParentCode());
			map.put("remark", tDictionary.getRemark());
			list.add(map);
		}
		return "list";
	}
	
	/**
	 * 数据预览画面
	 * @return
	 * @throws Exception
	 */
	public String preview(){
		String source=getRequest().getParameter("id");
		list=new ArrayList<Map<String,Object>>();
		List<Object[]> ll = null;
		try {
			ll=dictionaryService.dicSourceInfoList(source);
			for(int i=0;i<ll.size();i++){
				Map<String,Object> map= new HashMap<String,Object>();
				String code=ll.get(i)[0]+"";
				String name=ll.get(i)[1]+"";
				map.put("code", code);
				map.put("name", name);
				list.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "list";
	}
	
	/**
	 * 查询结果分页显示
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String display() throws Exception {
        
		// 起始位置为：(页码-1)*rows
		int firstResult = (page - 1) * rows;
		// 终止位置为：页码*行数-1
		int maxResults = page * rows ;
		typeName=java.net.URLDecoder.decode(getRequest().getParameter("name"),"UTF-8");
		 List<Object[]> list=sortSearchesService.querySortList(new Object[]{"%"+typeName+"%"}, firstResult, maxResults, tablebean);
	//	 List<Object[]> list=sortSearchesService.querySortList(new Object[]{"%"}, firstResult, maxResults, tablebean);
		List rowsList = new ArrayList();
		for (Object[] objs : list) {
			TNodeDef tn=(TNodeDef)objs[0];
			RowBean rowbean = new RowBean();
			rowbean
					.setCell(new Object[] {
							tn.getName(),
							tn.getType(),
							tn.getSource(),
							tn.getTUser().getName(),
							DateUtils.DateToString(tn.getCreateDate()),
							"<a href='#' id='dialog_link' onclick='edit("+tn.getId()+")'>更新</a>" }); 
			// 当前的ID为1
			rowbean.setId(tn.getId()+"");
			rowsList.add(rowbean);
		}
		// 给单元格里存放数据
		tablebean.setRows(rowsList);

		// 当前页是1页
		tablebean.setPage(this.page);
		int records=tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records%rows==0?records/rows:records/rows+1);
		return "tab";
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

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
