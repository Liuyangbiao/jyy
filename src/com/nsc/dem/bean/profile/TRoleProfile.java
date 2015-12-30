package com.nsc.dem.bean.profile;

import java.sql.Timestamp;

/**
 * TRoleProfile entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TRoleProfile implements java.io.Serializable {

	// Fields

	private TRoleProfileId id;
	private TUser TUser;
	private String grantPrivilege;
	private Timestamp grantTime;

	// Constructors

	/** default constructor */
	public TRoleProfile() {
	}

	/** full constructor */
	public TRoleProfile(TRoleProfileId id, TUser TUser, String grantPrivilege,
			Timestamp grantTime) {
		this.id = id;
		this.TUser = TUser;
		this.grantPrivilege = grantPrivilege;
		this.grantTime = grantTime;
	}

	// Property accessors

	public TRoleProfileId getId() {
		return this.id;
	}

	public void setId(TRoleProfileId id) {
		this.id = id;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public String getGrantPrivilege() {
		return this.grantPrivilege;
	}

	public void setGrantPrivilege(String grantPrivilege) {
		this.grantPrivilege = grantPrivilege;
	}

	public Timestamp getGrantTime() {
		return this.grantTime;
	}

	public void setGrantTime(Timestamp grantTime) {
		this.grantTime = grantTime;
	}

}