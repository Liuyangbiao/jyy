package com.nsc.dem.action.bean;

public class DocTypeDetailsBean {
	private String select;

	private String code;
	private String name;
	private String dftSecurity;
	private String username;
	private String speciality;
	private String createdate;
	private String href;
	private String privilege;
	private String codename;
	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	private String comFlag;
	private String remark;
	private String parentName;

	public String getComFlag() {
		return comFlag;
	}

	public void setComFlag(String comFlag) {
		this.comFlag = comFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	private String parentCode;

	public String getPrivilege() {
		return privilege;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDftSecurity() {
		return dftSecurity;
	}

	public void setDftSecurity(String dftSecurity) {
		this.dftSecurity = dftSecurity;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
