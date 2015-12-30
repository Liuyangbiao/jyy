package com.nsc.dem.bean.system;

import java.util.Date;

import com.nsc.dem.bean.profile.TUser;

/**
 * TRoleTree entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TRoleTree implements java.io.Serializable {

	// Fields

	private TRoleTreeId id;
	private TUser TUser;
	private Boolean isUsable;
	private Date createDate;
	private String treeIdOrder;

	// Constructors



	/** default constructor */
	public TRoleTree() {
	}

	/** minimal constructor */
	public TRoleTree(TRoleTreeId id, TUser TUser, Boolean isUsable) {
		this.id = id;
		this.TUser = TUser;
		this.isUsable = isUsable;
	}

	/** full constructor */
	public TRoleTree(TRoleTreeId id, TUser TUser, Boolean isUsable,
			Date createDate,String treeIdOrder) {
		this.id = id;
		this.TUser = TUser;
		this.isUsable = isUsable;
		this.createDate = createDate;
		this.treeIdOrder = treeIdOrder;
	}

	// Property accessors

	public TRoleTreeId getId() {
		return this.id;
	}

	public void setId(TRoleTreeId id) {
		this.id = id;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public Boolean getIsUsable() {
		return this.isUsable;
	}

	public void setIsUsable(Boolean isUsable) {
		this.isUsable = isUsable;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getTreeIdOrder() {
		return treeIdOrder;
	}

	public void setTreeIdOrder(String treeIdOrder) {
		this.treeIdOrder = treeIdOrder;
	}

}