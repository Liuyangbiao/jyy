package com.nsc.dem.bean.system;

import java.util.Date;

import com.nsc.dem.bean.profile.TUser;

/**
 * TReport entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TReport implements java.io.Serializable {

	// Fields

	private String name;
	private TUser TUser;
	private String type;
	private String description;
	private String path;
	private String templeteName;
	private Boolean isUsable;
	private String remark;
	private Date designDate;

	// Constructors

	/** default constructor */
	public TReport() {
	}

	/** minimal constructor */
	public TReport(String name, TUser TUser, String type, String description,
			String path, String templeteName, Boolean isUsable, Date designDate) {
		this.name = name;
		this.TUser = TUser;
		this.type = type;
		this.description = description;
		this.path = path;
		this.templeteName = templeteName;
		this.isUsable = isUsable;
		this.designDate = designDate;
	}

	/** full constructor */
	public TReport(String name, TUser TUser, String type, String description,
			String path, String templeteName, Boolean isUsable, String remark,
			Date designDate) {
		this.name = name;
		this.TUser = TUser;
		this.type = type;
		this.description = description;
		this.path = path;
		this.templeteName = templeteName;
		this.isUsable = isUsable;
		this.remark = remark;
		this.designDate = designDate;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTempleteName() {
		return this.templeteName;
	}

	public void setTempleteName(String templeteName) {
		this.templeteName = templeteName;
	}

	public Boolean getIsUsable() {
		return this.isUsable;
	}

	public void setIsUsable(Boolean isUsable) {
		this.isUsable = isUsable;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDesignDate() {
		return this.designDate;
	}

	public void setDesignDate(Date designDate) {
		this.designDate = designDate;
	}

}