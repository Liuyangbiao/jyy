package com.nsc.dem.bean.profile;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TProfile entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value = { "serial", "unchecked" })
public class TProfile implements java.io.Serializable {

	// Fields

	private String id;
	private TUser TUser;
	private String type;
	private String name;
	private String description;
	private Date createDate;
	private Set TProfileTemps = new HashSet(0);
	private Set TRoleProfiles = new HashSet(0);
	private Set TUserProfiles = new HashSet(0);

	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;
	private Long sortNo;

	// Constructors

	/** default constructor */
	public TProfile() {
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	/** minimal constructor */
	public TProfile(String id, TUser TUser, String type, String name,
			Date createDate) {
		this.id = id;
		this.TUser = TUser;
		this.type = type;
		this.name = name;
		this.createDate = createDate;
	}

	/** full constructor */
	public TProfile(String id, TUser TUser, String type, String name,
			String description, Date createDate, Set TProfileTemps,
			Set TRoleProfiles, Set TUserProfiles) {
		this.id = id;
		this.TUser = TUser;
		this.type = type;
		this.name = name;
		this.description = description;
		this.createDate = createDate;
		this.TProfileTemps = TProfileTemps;
		this.TRoleProfiles = TRoleProfiles;
		this.TUserProfiles = TUserProfiles;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getTProfileTemps() {
		return this.TProfileTemps;
	}

	public void setTProfileTemps(Set TProfileTemps) {
		this.TProfileTemps = TProfileTemps;
	}

	public Set getTRoleProfiles() {
		return this.TRoleProfiles;
	}

	public void setTRoleProfiles(Set TRoleProfiles) {
		this.TRoleProfiles = TRoleProfiles;
	}

	public Set getTUserProfiles() {
		return this.TUserProfiles;
	}

	public void setTUserProfiles(Set TUserProfiles) {
		this.TUserProfiles = TUserProfiles;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

}