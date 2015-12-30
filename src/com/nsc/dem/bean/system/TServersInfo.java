package com.nsc.dem.bean.system;

/**
 * TFtp entity. @author MyEclipse Persistence Tools
 */

public class TServersInfo implements java.io.Serializable {

	// Fields

	private String id;
	private String unitCode;
	private String wsAdd; //webservice address
	private String ftpName;
	private String ftpPwd;
	private String ftpPort;
	private String ftpIp;
	private String unitName;

	// Constructors

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/** default constructor */
	public TServersInfo() {
	}

	


	public TServersInfo(String id, String unitCode, String wsAdd,
			String ftpName, String ftpPwd, String ftpPort, String ftpIp,
			String unitName) {
		super();
		this.id = id;
		this.unitCode = unitCode;
		this.wsAdd = wsAdd;
		this.ftpName = ftpName;
		this.ftpPwd = ftpPwd;
		this.ftpPort = ftpPort;
		this.ftpIp = ftpIp;
		this.unitName = unitName;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	

	public String getWsAdd() {
		return wsAdd;
	}

	public void setWsAdd(String wsAdd) {
		this.wsAdd = wsAdd;
	}

	public String getFtpName() {
		return this.ftpName;
	}

	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}

	public String getFtpPwd() {
		return this.ftpPwd;
	}

	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

	public String getFtpPort() {
		return this.ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	

}