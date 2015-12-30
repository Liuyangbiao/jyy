package com.nsc.dem.bean.profile;

/**
 * TUserProfileId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TUserProfileId implements java.io.Serializable {

	// Fields

	private TUser TUser;
	private TProfile TProfile;

	// Constructors

	/** default constructor */
	public TUserProfileId() {
	}

	/** full constructor */
	public TUserProfileId(TUser TUser, TProfile TProfile) {
		this.TUser = TUser;
		this.TProfile = TProfile;
	}

	// Property accessors

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public TProfile getTProfile() {
		return this.TProfile;
	}

	public void setTProfile(TProfile TProfile) {
		this.TProfile = TProfile;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TUserProfileId))
			return false;
		TUserProfileId castOther = (TUserProfileId) other;

		return ((this.getTUser() == castOther.getTUser()) || (this.getTUser() != null
				&& castOther.getTUser() != null && this.getTUser().equals(
				castOther.getTUser())))
				&& ((this.getTProfile() == castOther.getTProfile()) || (this
						.getTProfile() != null
						&& castOther.getTProfile() != null && this
						.getTProfile().equals(castOther.getTProfile())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTUser() == null ? 0 : this.getTUser().hashCode());
		result = 37 * result
				+ (getTProfile() == null ? 0 : this.getTProfile().hashCode());
		return result;
	}

}