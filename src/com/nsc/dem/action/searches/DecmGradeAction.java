package com.nsc.dem.action.searches;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.DocNode;
import com.nsc.dem.service.searches.IsearchesService;

public class DecmGradeAction extends BaseAction {

	private Long nodeId;
	private boolean sign;
	private String options;

	public String getOptions() {
		return options;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	List<DocNode> list;
	LinkedHashMap<String, Object> jsonData = new LinkedHashMap<String, Object>();
	IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public List<DocNode> getList() {
		return list;
	}

	private static final long serialVersionUID = 5304532811867020924L;

	/**
	 * 获取节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNode() {
		HashMap map=new HashMap();
		if(options!=null){
			String[] optionsArray = options.split(";");
			for (int i = 0; i < optionsArray.length; i++) {
			  String[] tempString = optionsArray[i].split("=");
			  String key = tempString[0];
			  String value= tempString[1];
			  map.put(key, value);
			}
		}
		
		list = searchesService.treeDefList(nodeId, sign, map);
		return SUCCESS;
	}
}
