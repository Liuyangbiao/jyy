package com.nsc.dem.util.download;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * 存储转跳下载的文件清单
 *
 */

public class DownLoadFileList {
	
	private static Map<String, String> downloadFiles;
	private static DownLoadFileList downLoadFileList;
	
	private DownLoadFileList(){
		downloadFiles = new HashMap<String,String>();
	}
	
	public static synchronized DownLoadFileList createDownLoadFileList(){
		if(null == downLoadFileList){
			downLoadFileList = new DownLoadFileList();
		}
		return downLoadFileList;
	}
	
	
	public void addDownLoadList(String key, String value){
		downloadFiles.put(key, value);
	}
	
	public String getDownLoadList(String key){
		if(StringUtils.isNotBlank(key)){
			if(downloadFiles.get(key) != null){
				String value = downloadFiles.get(key);
				downloadFiles.remove(key);
				return value;
				
			}
		}
		return null;
	}
	
	
}
