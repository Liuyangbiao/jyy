package com.nsc.dem.bean.system;

import java.util.Date;

import com.nsc.dem.bean.profile.TUser;

/**
 * TTemplete entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TTemplete implements java.io.Serializable {

	// Fields

	private String name;
	private TUser TUser;
	private String description;
	private String type;
	private String path;
	private Date uploadDate;
	private String remark;

	// Constructors

	/** default constructor */
	public TTemplete() {
	}

	/** minimal constructor */
	public TTemplete(String name, TUser TUser, String description, String type,
			String path, Date uploadDate) {
		this.name = name;
		this.TUser = TUser;
		this.description = description;
		this.type = type;
		this.path = path;
		this.uploadDate = uploadDate;
	}

	/** full constructor */
	public TTemplete(String name, TUser TUser, String description, String type,
			String path, Date uploadDate, String remark) {
		this.name = name;
		this.TUser = TUser;
		this.description = description;
		this.type = type;
		this.path = path;
		this.uploadDate = uploadDate;
		this.remark = remark;
	}

	// Property accessors

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}