package com.nsc.dem.action.searches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.util.GetCh2Spell;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IdictionaryService;

public class DecmBasicAction  extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private List<Map<String,Object>> list;
	
	IsearchesService searchesService;
	
	IdictionaryService dictionaryService;
	
	IarchivesService archivesService;
	
	IprojectService projectService;
	
	public void setProjectService(IprojectService projectService) {
		this.projectService = projectService;
	}

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	public List<Map<String,Object>> getList() {
		return list;
	}

	public void setList(List<Map<String,Object>> list) {
		this.list = list;
	}
	
	/**
	 * 初始化单位,即设计院
	 * @return
	 */
	public String unit(){
		TUnit tUnit=new TUnit();
		 list=new ArrayList<Map<String,Object>>();
		List<Object> tUnitList=searchesService.EntityQuery(tUnit);
		for(int i=0;i<tUnitList.size();i++){
			tUnit=(TUnit)tUnitList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id", tUnit.getCode());
			map.put("name", tUnit.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tUnit.getName()));
			map.put("other", tUnit.getType());
			list.add(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化单位,档案维护专用
	 * @return
	 */
	public String unitDoc(){
		TUnit tUnit=new TUnit();
		tUnit.setType("C01");
		 list=new ArrayList<Map<String,Object>>();
		List<Object> tUnitList=searchesService.EntityQuery(tUnit);
		for(int i=0;i<tUnitList.size();i++){
			tUnit=(TUnit)tUnitList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id", tUnit.getCode());
			map.put("name", tUnit.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tUnit.getName()));
			map.put("other", tUnit.getType());
			list.add(map);
		}
		return SUCCESS;
	}
	
    /**
     * 初始化工程名称
     */
	public String projectName(){
		String code=getRequest().getParameter("id");
		String tcode=getRequest().getParameter("tid");
		list=new ArrayList<Map<String,Object>>();
		List<TProject> tProjectList=projectService.tProjectList(code,tcode);
		if(tProjectList.size()==0){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id","-1");
			map.put("name","没有数据");
			map.put("spell",GetCh2Spell.getBeginCharacter("没有数据"));
			list.add(map);
		}else{
			for(int i=0;i<tProjectList.size();i++){
				TProject tProject=tProjectList.get(i);
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("id", tProject.getCode());
				map.put("name", tProject.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tProject.getName()));
				map.put("other", tProject.getType());
				list.add(map);
			}
		}
		
		return SUCCESS;
	}
	
	  /**
     * 初始化工程名称  档案维护专用
     */
	public String projectNameDoc(){
		String code=getRequest().getParameter("id");
		list=new ArrayList<Map<String,Object>>();
		List<TProject> tProjectList=projectService.tProjectListDoc(code);
		if(tProjectList.size()==0){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id","-1");
			map.put("name","没有数据");
			map.put("spell",GetCh2Spell.getBeginCharacter("没有数据"));
			list.add(map);
		}else{
			for(int i=0;i<tProjectList.size();i++){
				TProject tProject=tProjectList.get(i);
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("id", tProject.getCode());
				map.put("name", tProject.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tProject.getName()));
				map.put("other", tProject.getType());
				list.add(map);
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 初始化文档分类
	 */
	public String doc(){
		String id=getRequest().getParameter("id");
		list=new ArrayList<Map<String,Object>>();
		List<TDocType> tDocTypeList=archivesService.docTypeList(id,"");
		for(int i=0;i<tDocTypeList.size();i++){
			TDocType tDocType=tDocTypeList.get(i);
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id", tDocType.getCode());
			map.put("name", tDocType.getName());
			map.put("spell", GetCh2Spell.getBeginCharacter(tDocType.getName()));
			map.put("other", tDocType.getSpeciality());
			list.add(map);
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化档案分类
	 */
	public String recordType(){
		String code=getRequest().getParameter("id");
		list=new ArrayList<Map<String,Object>>();
		List<TDocType> tDocTypeList=archivesService.docTypeList(code);
		if(tDocTypeList.size()==0){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("id","-1");
			map.put("name","没有数据");
			map.put("spell",GetCh2Spell.getBeginCharacter("没有数据"));
			list.add(map);
		}else{
			for(int i=0;i<tDocTypeList.size();i++){
				TDocType tDocType=tDocTypeList.get(i);
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("id", tDocType.getCode());
				map.put("name", tDocType.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tDocType.getName()));
				map.put("other", tDocType.getSpeciality());
				list.add(map);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 初始化档案分类,结果页
	 */
	public String recordTypeResult(){
		String code=getRequest().getParameter("id");
		String parentCode=getRequest().getParameter("tid");
		list=new ArrayList<Map<String,Object>>();
		List<TDocType> tDocTypeList=archivesService.docTypeResultList(code,parentCode);
		
			for(int i=0;i<tDocTypeList.size();i++){
				TDocType tDocType=tDocTypeList.get(i);
				Map<String,Object> map= new HashMap<String,Object>();
				map.put("id", tDocType.getCode());
				map.put("name", tDocType.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tDocType.getName()));
				map.put("other", tDocType.getSpeciality());
				list.add(map);
			}
		
		return SUCCESS;
	}
	

}
