package com.nsc.dem.util.filestore;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

import com.nsc.base.conf.Configurater;

public class FileStoreLocation {
	
	public static String getStoreLocation(){
		Configurater config = Configurater.getInstance();
		String systemType = config.getConfigValue("system_type");
		if("1".equals(systemType)){
			return config.getConfigValue("country");
		}else if("3".equals(systemType)){
			return config.getConfigValue("unitCode");
		}
		return null;
	}
	
	/**
	 * 改变存储位置
	 * @param store
	 * @return
	 */
	public static String changStoreLocation(String store){
		String current = getStoreLocation();
		//如果存储位置为空，返回当前系统ID
		if(StringUtils.isBlank(store))
			return current;
		//查找当前系统ID是否在存储中
		if(isExist(store,current)){
			return store;
		}else{
			return store + "#" + current;
		}
	}
	
	/**
	 * 在字符串context中检查是否有unitCode存在
	 * @param context 文件存储位置，各ID以#为分隔
	 * @param unitCode 单位ID
	 * @return 存在返回true，不存在返回false
	 */
	public static boolean isExist(String context, String unitCode){
		if(StringUtils.isBlank(context) || StringUtils.isBlank(context))
			return false;
		
		String[] codes = context.split("#");
		for(String code : codes){
			if(code.trim().equals(unitCode.trim()))
				return true;
		}
		return false;
	}
	
	
	public static String delRepeated(String store){
		if(StringUtils.isBlank(store))
			return "";
		
		String[] strs = store.split("#");
		List<String> lists = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			if(lists.contains(strs[i])){
				continue;
			}
			lists.add(strs[i]);
		}
		
		StringBuffer buffer = new StringBuffer();
		for(String str : lists){
			buffer.append(str+"#");
		}
		
		if(buffer.length() <= 0)
			return "";
		
		//删除最后一个#
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
	}
}
