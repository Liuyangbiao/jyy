package com.nsc.dem.util.xml;

import java.util.ArrayList;
import java.util.List;


/**
 * 存储失败和成功接收的文件
 * @author tsq
 *
 */
public class StoreFileReceiveID {
	private static List<String> successFileID;
	private static List<String> failFileID;
	private static StoreFileReceiveID instance;
	
	public static StoreFileReceiveID createInstance(){
		if(instance == null)
			instance = new StoreFileReceiveID();
		return instance;
	}
	
	private StoreFileReceiveID(){
		successFileID = new ArrayList<String>();
		failFileID = new ArrayList<String>();
	}
	
	public String getSuccessFileID(){
		StringBuffer buffer = new StringBuffer();
		for(String str : successFileID){
			buffer.append(str);
			buffer.append(",");
		}
		if(buffer.lastIndexOf(",") != -1){
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		}
		successFileID.clear();
		return buffer.toString();
	}
	
	public String getFailFileID(){
		StringBuffer buffer = new StringBuffer();
		for(String str : failFileID){
			buffer.append(str);
			buffer.append(",");
		}
		if(buffer.lastIndexOf(",") != -1){
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		}
		failFileID.clear();
		return buffer.toString();
	}
	
	public void addSuccessFileID(String id){
		successFileID.add(id);
	}
	
	public void addFailFileID(String id){
		failFileID.add(id);
	}
}
