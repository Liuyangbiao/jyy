package com.nsc.dem.action.bean;

/**
 * 接收服务器设置参数
 * 分为国家电网和省公司
 *    如果是国家电网公司：需要输入四个区域及国家电网公司的FTP信息及网段
 *    如果是省公司：需要输入省公司FTP信息及网段
 */
public class ServiceSetBean {
	private String id;                 //单位ID 
	private String name;               //单位名称
	private String wsUrl;              //webService地址
	private String ftpIP;
	private String ftpPort;
	private String ftpLoginName;
	private String protocol;
	private String ftpPwd;
	private String startNetWay;
	private String endNetWay;
	private String context;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWsUrl() {
		return wsUrl;
	}
	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}
	public String getFtpIP() {
		return ftpIP;
	}
	public void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}
	public String getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpLoginName() {
		return ftpLoginName;
	}
	public void setFtpLoginName(String ftpLoginName) {
		this.ftpLoginName = ftpLoginName;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getFtpPwd() {
		return ftpPwd;
	}
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	public String getStartNetWay() {
		return startNetWay;
	}
	public void setStartNetWay(String startNetWay) {
		this.startNetWay = startNetWay;
	}
	public String getEndNetWay() {
		return endNetWay;
	}
	public void setEndNetWay(String endNetWay) {
		this.endNetWay = endNetWay;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
}
