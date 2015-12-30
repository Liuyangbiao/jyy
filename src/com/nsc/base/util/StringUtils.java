package com.nsc.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串操作类
 * 
 * 此类用于字符串的常用操作，比如编码、数字判断等。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:36:18 AM
 * @version
 */
public class StringUtils {
	/**
	 * 将字符串按newCoding方式进行编码
	 * @param url
	 * @param oldCoding 原编码方式
	 * @param newCoding 
	 * @return 编码后的字符串
	 */
	public static String codingUrl(String url,String oldCoding,String newCoding){
		try{
			return new String(url.getBytes(oldCoding),newCoding);
		}catch(Exception ex){
			throw new AppException(ex,"app.string.decoding",null,new String[]{});
		}
	}
	/**
	 * 判断字符串中是否有数字
	 * @param digital
	 * @return 是否数字
	 */
	public static boolean isNumberic(String digital){		
		for (int i = 0; i < digital.length(); i++) {
            //If we find a non-digit character we return false.
            if (!Character.isDigit(digital.charAt(i)))
                return false;
        }
		
        return true;
	}
	/**
	 * 将str字符串按slipt内容截取成一个字符串数组
	 * @param str
	 * @param slipt
	 * @return 字符串数组
	 */
	public static String[] getHierarchy(String str,String slipt){
		String[] hierarchy=new String[0];
		StringBuffer originalStr=new StringBuffer(str);
		List<String> list=new ArrayList<String>();
		while(originalStr.indexOf(slipt)!=-1){
			list.add(originalStr.substring(0, str.indexOf(slipt)));
			originalStr.delete(0, originalStr.indexOf(slipt)+1);
		}
		list.add(originalStr.toString());
		
		return list.toArray(hierarchy);
	}
	
	/**
	 * 用于页面内容输出
	 * 可自定义当输出对象为空时的值.
	 * */
	public static Object isNull(Object obj,String nullRetStr){
		Object retStr;
		if(obj!=null&&!"".equals(obj)){
			retStr = obj;
		}
		else{
			retStr = nullRetStr;
		}
		return retStr;
	}
	
	/**
	 * 把对象转换成字符串
	 * @param obj 被转换对象
	 * @return
	 */
	public static String toString(Object obj) {
		String str = "";
		if (obj != null) {
			str = obj.toString();
		}
		return str;
	}

	/**
	 * 把对象转换成字符
	 * @param obj  被转换对象
	 * @param outStr 对象为空时候，默认输出值
	 * @return
	 */
	public static String toString(Object obj, String outStr) {
		String str = "";
		str = outStr != null ? outStr : "";
		if (obj != null) {
			str = obj.toString();
		} else {

		}
		return str;
	}
}
