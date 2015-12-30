package com.nsc.dem.action.bean;

import java.util.List;

public class DownFileBean {
	private String name;//文件名
	private String path;//下载路径
	private String code; //业主单位，也保存最终用户选择的下载地址
	private String docid;  //文档ID
	private String suffix;//文档后缀
	private String projectId;//工程ID
	private boolean isonline;//true在线查看，false下载
	private List<String[]> downAddress;//下载地址
	
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.replaceAll("<[.[^<]]*>","");
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<String[]> getDownAddress() {
		return downAddress;
	}
	public void setDownAddress(List<String[]> downAddress) {
		this.downAddress = downAddress;
	}
	public boolean isIsonline() {
		return isonline;
	}
	public void setIsonline(boolean isonline) {
		this.isonline = isonline;
	}
	
	
	
	
}
