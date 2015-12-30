package com.nsc.dem.util.download;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.util.xml.XmlUtils;

/**
 * IP工具类
 *
 */
public class LoginLocationUtils {
	
	/**
	 * 计算用户登录的IP所在地
	 * @param ip
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getIpLocal(String ip){
		//把IP的点去除
		String ipStr = ip.replace(".", "");
		int ipInt = Integer.parseInt(ipStr);
		
		XmlUtils util = XmlUtils.getInstance("intenterIp.xml");
		Document document = util.getDocument();
		List<Element> intenterList = document.selectNodes("//intenter");
		for(Element intenter: intenterList){
			String startStr = intenter.attributeValue("start");
			String endStr = intenter.attributeValue("end");
			startStr = startStr.replace(".","");
			endStr = endStr.replace(".", "");
			long start = Integer.parseInt(startStr);
			long end = Integer.parseInt(endStr);
			if(ipInt >= start && ipInt <= end){
				return intenter.attributeValue("code");
			}
		}
		return null;
	}
	
	/**
	 * 判断是否在本地登录
	 * @param unitCode 用户登录所在地的编码
	 */
	public static boolean isLocationLogin(String unitCode){
		Configurater config = Configurater.getInstance();
		String systemType = config.getConfigValue("system_type");
		//国网
		if("1".equals(systemType)){
			String country = config.getConfigValue("country");
			//区域用户
			if(unitCode.length()== 6){
				return false;
			}else if (unitCode.equals(country.trim())){
				return true;
			}else{
				return false;
			}
		//省公司
		}else if("3".equals(systemType)){
			if(unitCode.equals(config.getConfigValue("unitCode"))){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
