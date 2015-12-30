package com.nsc.dem.bean.profile;

import java.sql.Timestamp;

/**
 * TUserProfile entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TUserProfile implements java.io.Serializable {

	// Fields

	private TUserProfileId id;
	private TUser TUserByGrantUserId;
	private String grantPrivilege;
	private Timestamp grantTime;

	// Constructors

	/** default constructor */
	public TUserProfile() {
	}

	/** full constructor */
	public TUserProfile(TUserProfileId id, TUser TUserByGrantUserId,
			String grantPrivilege, Timestamp grantTime) {
		this.id = id;
		this.TUserByGrantUserId = TUserByGrantUserId;
		this.grantPrivilege = grantPrivilege;
		this.grantTime = grantTime;
	}

	// Property accessors

	public TUserProfileId getId() {
		return this.id;
	}

	public void setId(TUserProfileId id) {
		this.id = id;
	}

	public TUser getTUserByGrantUserId() {
		return this.TUserByGrantUserId;
	}

	public void setTUserByGrantUserId(TUser TUserByGrantUserId) {
		this.TUserByGrantUserId = TUserByGrantUserId;
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