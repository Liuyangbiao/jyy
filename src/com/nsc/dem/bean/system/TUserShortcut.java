package com.nsc.dem.bean.system;

import com.nsc.dem.bean.profile.TProfile;
import com.nsc.dem.bean.profile.TUser;

/**
 * TUserShortcut entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TUserShortcut implements java.io.Serializable {

	// Fields

	private Long id;
	private TProfile TProfile;
	private TUser TUser;
	private Long shortOrder;

	// Constructors

	/** default constructor */
	public TUserShortcut() {
	}

	/** minimal constructor */
	public TUserShortcut(TProfile TProfile, TUser TUser) {
		this.TProfile = TProfile;
		this.TUser = TUser;
	}

	/** full constructor */
	public TUserShortcut(TProfile TProfile, TUser TUser, Long shortOrder) {
		this.TProfile = TProfile;
		this.TUser = TUser;
		this.shortOrder = shortOrder;
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

	public Long getShortOrder() {
		return this.shortOrder;
	}

	public void setShortOrder(Long shortOrder) {
		this.shortOrder = shortOrder;
	}

}