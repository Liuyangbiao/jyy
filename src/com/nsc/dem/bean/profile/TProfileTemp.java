package com.nsc.dem.bean.profile;

import java.sql.Timestamp;

/**
 * TProfileTemp entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TProfileTemp implements java.io.Serializable {

	// Fields

	private Long id;
	private TProfile TProfile;
	private TUser TUser;
	private String userId;
	private String grantPrivilege;
	private String role;
	private Timestamp startTime;
	private Timestamp endTime;
	private Long permitCount;
	private Long prcsCount;
	private Timestamp grantTime;

	// Constructors

	/** default constructor */
	public TProfileTemp() {
	}

	/** minimal constructor */
	public TProfileTemp(Long id, TUser TUser, String userId,
			Timestamp startTime, Timestamp endTime, Long permitCount,
			Long prcsCount, Timestamp grantTime) {
		this.id = id;
		this.TUser = TUser;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.permitCount = permitCount;
		this.prcsCount = prcsCount;
		this.grantTime = grantTime;
	}

	/** full constructor */
	public TProfileTemp(Long id, TProfile TProfile, TUser TUser, String userId,
			String grantPrivilege, String role, Timestamp startTime,
			Timestamp endTime, Long permitCount, Long prcsCount,
			Timestamp grantTime) {
		this.id = id;
		this.TProfile = TProfile;
		this.TUser = TUser;
		this.userId = userId;
		this.grantPrivilege = grantPrivilege;
		this.role = role;
		this.startTime = startTime;
		this.endTime = endTime;
		this.permitCount = permitCount;
		this.prcsCount = prcsCount;
		this.grantTime = grantTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TProfile getTProfile() {
		return this.TProfile;
	}

	public void setTProfile(TProfile TProfile) {
		this.TProfile = TProfile;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGrantPrivilege() {
		return this.grantPrivilege;
	}

	public void setGrantPrivilege(String grantPrivilege) {
		this.grantPrivilege = grantPrivilege;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Long getPermitCount() {
		return this.permitCount;
	}

	public void setPermitCount(Long permitCount) {
		this.permitCount = permitCount;
	}

	public Long getPrcsCount() {
		return this.prcsCount;
	}

	public void setPrcsCount(Long prcsCount) {
		this.prcsCount = prcsCount;
	}

	public Timestamp getGrantTime() {
		return this.grantTime;
	}

	public void setGrantTime(Timestamp grantTime) {
		this.grantTime = grantTime;
	}

}