package com.nsc.dem.bean.system;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;

/**
 * TTreeDef entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TTreeDef implements java.io.Serializable {

	// Fields

	private Long id;
	private TUser TUser;
	private TNodeDef TNodeDef;
	private String name;
	private String remark;
	private Date createDate;
	private Long parentId;
	private Set TRoleTrees = new HashSet(0);
	private String imageUrl;


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/** default constructor */
	public TTreeDef() {
	}

	/** minimal constructor */
	public TTreeDef(Long id, TUser TUser, TNodeDef TNodeDef, String name,
			Date createDate, Long parentId) {
		this.id = id;
		this.TUser = TUser;
		this.TNodeDef = TNodeDef;
		this.name = name;
		this.createDate = createDate;
		this.parentId = parentId;
	}

	/** full constructor */
	public TTreeDef(Long id, TUser TUser, TNodeDef TNodeDef, String name,
			String remark, Date createDate, Long parentId, Set TRoleTrees) {
		this.id = id;
		this.TUser = TUser;
		this.TNodeDef = TNodeDef;
		this.name = name;
		this.remark = remark;
		this.createDate = createDate;
		this.parentId = parentId;
		this.TRoleTrees = TRoleTrees;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public TNodeDef getTNodeDef() {
		return this.TNodeDef;
	}

	public void setTNodeDef(TNodeDef TNodeDef) {
		this.TNodeDef = TNodeDef;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Set getTRoleTrees() {
		return this.TRoleTrees;
	}

	public void setTRoleTrees(Set TRoleTrees) {
		this.TRoleTrees = TRoleTrees;
	}

}