package com.nsc.dem.util.xml;

import java.util.Hashtable;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * ±£¥ÊFTP–≈œ¢
 */
public class StoreFtpNode {
	private static Hashtable<String, String> ftpNodes;
	private static StoreFtpNode instance;
	
	public static synchronized StoreFtpNode createInstance(){
		if(instance == null){
			instance  = new StoreFtpNode();
		}
		return instance;
	}
	
	private StoreFtpNode(){
		ftpNodes = new Hashtable<String,String>();
	}
	
	public void addFtpNode(String key, String value){
		if(StringUtils.isBlank(key) || StringUtils.isBlank(value))
			return;
		if(StringUtils.isNotBlank(ftpNodes.get(key)))
			ftpNodes.remove(key);
		ftpNodes.put(key, value);
	}
	
	public String getFtpNode(String key){
		if(ftpNodes == null || ftpNodes.isEmpty())
			return null;
		return ftpNodes.get(key);
	}
	
	public void deleteFtpNode(String key){
		if(StringUtils.isNotBlank(key))
			ftpNodes.remove(key);
	}
	
	public Hashtable<String,String> getAllFtpNode(){
		return ftpNodes;
	}
}
