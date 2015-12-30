package com.nsc.dem.bean.system;

import java.util.Date;

import com.nsc.dem.bean.profile.TUser;

/**
 * TDictionary entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TDictionary implements java.io.Serializable {

	// Fields

	private String code;
	private TUser TUser;
	private String authControl;
	private String name;
	private Date createDate;
	private String remark;
	private String parentCode;

	// Constructors

	/** default constructor */
	public TDictionary() {
	}

	/** minimal constructor */
	public TDictionary(String code, TUser TUser, String name, Date createDate) {
		this.code = code;
		this.TUser = TUser;
		this.name = name;
		this.createDate = createDate;
	}

	/** full constructor */
	public TDictionary(String code, TUser TUser, String authControl,
			String name, Date createDate, String remark, String parentCode) {
		this.code = code;
		this.TUser = TUser;
		this.authControl = authControl;
		this.name = name;
		this.createDate = createDate;
		this.remark = remark;
		this.parentCode = parentCode;
	}

	// Property accessors

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public String getAuthControl() {
		return this.authControl;
	}

	public void setAuthControl(String authControl) {
		this.authControl = authControl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

}