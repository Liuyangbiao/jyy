package com.nsc.dem.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.nsc.dem.action.bean.ServiceSetBean;



public class IntenterXmlUtils {
	private static XmlUtils util = XmlUtils.getInstance("intenterIp.xml");
	/**
	 * 创建intenter节点
	 * @param code 单位ID
	 * @param webServiceAdd webservice地址
	 * @param startIp   网段开始IP
	 * @param endIp     网段结束IP
	 */
	public static void createIntenterNode(String code, String unitName,String webServiceAdd, String startIp, String endIp){
		if(StringUtils.isBlank(code)){
			//logger.warn("单位ID不能为空，请重新配置，该参数影响webservice的调用");
			return;
		}
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//intenter[@code="+code+"]");
		if(element == null){
			element = DocumentHelper.createElement("intenter");
			element.addAttribute("code", code);
			document.getRootElement().add(element);
		}
		element.addAttribute("name", unitName);
		element.addAttribute("appIp", webServiceAdd);
		element.addAttribute("start", startIp);
		element.addAttribute("end", endIp);
		util.saveDocument(document);
	}
	
	/**
	 * 根据ID获取intenterIP.xml内容
	 * @param unitCode
	 * @return
	 */
	public static String getIntenterByCode(String unitCode){
		String intenterContext = "";
		if(StringUtils.isNotBlank(unitCode)){
			Document document = util.getDocument();
			Element element = (Element) document.selectSingleNode("//intenter[@code='"+unitCode+"']");
			if(null != element){
				//去除默认值
				element.addAttribute("default", null);
				//创建一个临时document
				Document documentTemp = DocumentHelper.createDocument();
				documentTemp.add(DocumentHelper.createElement("intenters"));
				documentTemp.getRootElement().add(element.detach());
				intenterContext = XmlUtils.document2String(documentTemp);
			}
		}
		return intenterContext;
	}
	
	/**
	 * 获取webservice地址
	 */
	public static String getWSURL(String unitCode){
		Document document = util.getDocument();
		Element intenterElement = (Element) document.selectSingleNode("//intenter[@code='"+unitCode+"']");
		if(null != intenterElement){
			return intenterElement.attributeValue("appIp");
		}
		return null;
	}
	
	
	/**
	 * 获取应用服务器地址
	 * @param unitCode
	 * @return
	 */
	public static String getAppServerAdd(String unitCode){
		Document document = util.getDocument();
		Element intenterElement = (Element) document.selectSingleNode("//intenter[@code='"+unitCode+"']");
		if(null != intenterElement){
			String appIp = intenterElement.attributeValue("appIp");
			return appIp.substring(0,appIp.lastIndexOf("/"));
		}
		return null;
	}
	
	/**
	 * 按CODE获取信息
	 * @param code 单位ID
	 * @param field 需要取值的子段
	 * @return
	 */
	public static String getInfoByCode(String code, String field){
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//intenter[@code='"+code+"']");
		if(element != null){
			return element.attributeValue(field);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Element> getStations(){
		Document document = util.getDocument();
		return document.selectNodes("//intenter"); 
	}
	
	public static boolean saveSynIntenterNode(String content){
		if(StringUtils.isBlank(content)){
			return false;
		}
		try{
			Document document = DocumentHelper.parseText(content);
			Element element = (Element) document.selectSingleNode("//intenter");
			String unitCode = element.attributeValue("code");
			//本地document
			Document localDocument = util.getDocument();
			Element elementTemp = (Element) localDocument.selectSingleNode("//intenter[@code = '"+unitCode+"']");
			//删除已经存在的节点
			if(null != elementTemp){
				localDocument.getRootElement().remove(elementTemp.detach());
			}
			localDocument.getRootElement().add(element.detach());
			util.saveDocument(localDocument);
			return true;
		}catch(Exception e){
			Logger.getLogger(IntenterXmlUtils.class).warn(e);
			return false;
		}
	}
	

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getAllProvinces(String unitCode){
		List<Map<String, String>> provinces = new ArrayList<Map<String, String>>();
		Document document = util.getDocument();
		List<Element> lists = document.selectNodes("//intenter");
		for(Element element : lists){
			Map<String,String> map = new HashMap<String, String>(); ;
			String code = element.attributeValue("code");
			if(code.length() == 8 && code.startsWith(unitCode)){
				map.put("code", element.attributeValue("code"));
				map.put("name", element.attributeValue("name"));
				provinces.add(map);
			}
		}
		return provinces;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getAllAreas(String unitCode){
		List<Map<String, String>> areas = new ArrayList<Map<String, String>>();
		Document document = util.getDocument();
		List<Element> lists = document.selectNodes("//intenter");
		for(Element element : lists){
			Map<String,String> map = new HashMap<String, String>(); ;
			String code = element.attributeValue("code");
			if(code.length() == 6  &&  code.startsWith(unitCode)){
				map.put("code", element.attributeValue("code"));
				map.put("name", element.attributeValue("name"));
				areas.add(map);
			}
		}
		return areas;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object[]> getAllInfo(){
		List<Object[]> allInfos = new ArrayList<Object[]>();
		Object[] objs = null ;
		Document document = util.getDocument();
		List<Element> allInfo = document.selectNodes("//intenter");
		for (Element elm : allInfo) {
			objs = new Object[2];
			objs[0] = elm.attributeValue("name");
			objs[1]= IntenterXmlUtils.getAppServerAdd(elm.attributeValue("code").toString());
			if(elm.attributeValue("code").toString().trim().length() != 6 ){
				allInfos.add(objs);
			}
		}
		return allInfos;
	}

	public static ServiceSetBean getInfoByCode(ServiceSetBean bean, String code) {
		Document document = util.getDocument();
		Element elm = (Element) document.selectSingleNode("//intenter[@code = '"+code+"']");
		if(null != elm){
			bean.setWsUrl(elm.attributeValue("appIp").substring(0,elm.attributeValue("appIp").lastIndexOf("/")));
			bean.setStartNetWay(elm.attributeValue("start"));
			bean.setEndNetWay(elm.attributeValue("end"));
		}
		return bean;
	}
}