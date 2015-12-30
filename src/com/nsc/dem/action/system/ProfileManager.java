package com.nsc.dem.action.system;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.nsc.base.conf.Configurater;
import com.nsc.base.conf.ConstConfig;
import com.nsc.base.util.FileUtil;
import com.nsc.base.util.PropertyReader;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.PropertiesDEM;

public class ProfileManager extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private Map<String,List<PropertiesDEM>> codeList;
    private String key;
    private String value;
    private String notes;
    private String namespace;
	
    /**
     * 配置文件读取
     * @return
     * @throws URISyntaxException
     */
	public String reader() throws URISyntaxException{
		codeList=new TreeMap<String,List<PropertiesDEM>>();
		String configuersValue=PropertyReader.getValue(ConstConfig.globalProperties, ConstConfig.CONFIGUERS_KEY);
		String[] configuers=configuersValue.split(",");
		List<PropertiesDEM> propertiesList;
		
		Properties  p;
		for(int i=0;i<configuers.length;i++){
			String[] configuer= configuers[i].split(":");
			String code=configuer[0];
			String valueFile=configuer[1];
			propertiesList=new ArrayList<PropertiesDEM>();
			//遍历所有
			p=PropertyReader.getProperties(FileUtil.relPath(valueFile));
			for(Object key:p.keySet()){  
				String value=p.getProperty(key.toString());
				PropertiesDEM pDEM=new PropertiesDEM();
				pDEM.setKey(key.toString());
				pDEM.setValue(value);
				propertiesList.add(pDEM);
		     }  
			codeList.put(code, propertiesList);
		}
		
		String code="global";
		String valueFile=ConstConfig.globalProperties;
		propertiesList=new ArrayList<PropertiesDEM>();
		//遍历所有
		p=PropertyReader.getProperties(FileUtil.relPath(valueFile));
		for(Object key:p.keySet()){  
			String value=p.getProperty(key.toString());
			PropertiesDEM pDEM=new PropertiesDEM();
			pDEM.setKey(key.toString());
			pDEM.setValue(value);
			propertiesList.add(pDEM);
	     }  
		codeList.put(code, propertiesList);
		
		return "display";
	}

	/**
	 * 配置文件添加 和 修改
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	public String save() throws URISyntaxException, IOException{
		
		//添加属性
		HashMap<String,String> ht=new HashMap<String,String>();
		ht.put(key, value);
		String configuersValue=PropertyReader.getValue(ConstConfig.globalProperties, ConstConfig.CONFIGUERS_KEY);
		configuersValue=configuersValue+",global:global.properties";
		String[] configuers=configuersValue.split(",");
		for(int i=0;i<configuers.length;i++){
			String[] configuer= configuers[i].split(":");
			String code=configuer[0];
			String valueFile=configuer[1];
			if(namespace.equals(code)){
				 PropertyReader.setValueAndStore(FileUtil.relPath(valueFile),ht,key+" : "+notes);	
				 //重新加载 配置文件
				 PropertyReader.reloadPropertyFile(FileUtil.relPath(valueFile));
				 
				 Configurater.getInstance().loadConfigure(namespace, valueFile);
			}
		}
		
		return "list";
	}

	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, List<PropertiesDEM>> getCodeList() {
		return codeList;
	}

	public void setCodeList(Map<String, List<PropertiesDEM>> codeList) {
		this.codeList = codeList;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
}
