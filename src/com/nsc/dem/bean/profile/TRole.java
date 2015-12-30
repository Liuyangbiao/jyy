package com.nsc.dem.bean.profile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TRole entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TRole implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String security;
	private String description;
	private String parentId;
	private String creator;
	private Date createDate;
	private Set TRoleProfiles = new HashSet(0);
	private Set TUsers = new HashSet(0);
	private Set TRoleTrees = new HashSet(0);

	// Constructors

	/** default constructor */
	public TRole() {
	}

	/** minimal constructor */
	public TRole(String id, String name, String security, String creator,
			Date createDate) {
		this.id = id;
		this.name = name;
		this.security = security;
		this.creator = creator;
		this.createDate = createDate;
	}

	/** full constructor */
	public TRole(String id, String name, String security, String description,
			String parentId, String creator, Date createDate,
			Set TRoleProfiles, Set TUsers, Set TRoleTrees) {
		this.id = id;
		this.name = name;
		this.security = security;
		this.description = description;
		this.parentId = parentId;
		this.creator = creator;
		this.createDate = createDate;
		this.TRoleProfiles = TRoleProfiles;
		this.TUsers = TUsers;
		this.TRoleTrees = TRoleTrees;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecurity() {
		return this.security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getTRoleProfiles() {
		return this.TRoleProfiles;
	}

	public void setTRoleProfiles(Set TRoleProfiles) {
		this.TRoleProfiles = TRoleProfiles;
	}

	public Set getTUsers() {
		return this.TUsers;
	}

	public void setTUsers(Set TUsers) {
		this.TUsers = TUsers;
	}

	public Set getTRoleTrees() {
		return this.TRoleTrees;
	}

	public void setTRoleTrees(Set TRoleTrees) {
		this.TRoleTrees = TRoleTrees;
	}

}