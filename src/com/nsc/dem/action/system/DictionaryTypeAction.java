package com.nsc.dem.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.system.IdictionaryService;

public class DictionaryTypeAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;
	IdictionaryService dictionaryService;
	
	public void setDictionaryService(IdictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	
	//保密
	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	private String paramValue;
	
	

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	/**
	 * 初始化数据字典分类
	 * 
	 * @return
	 */
	public String dictionaryTypeAction() {
		list = new ArrayList<Map<String, Object>>();
		List<TDictionary> tDictionaryList = dictionaryService
				.dictionaryList(paramValue);
		for (int i = 0; i < tDictionaryList.size(); i++) {
			TDictionary tDictionary = tDictionaryList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", tDictionary.getCode());
			map.put("name", tDictionary.getName());
			list.add(map);
		}
		return SUCCESS;
	}
}
