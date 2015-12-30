package com.nsc.dem.bean.profile;

/**
 * TRoleProfileId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TRoleProfileId implements java.io.Serializable {

	// Fields

	private TRole TRole;
	private TProfile TProfile;

	// Constructors

	/** default constructor */
	public TRoleProfileId() {
	}

	/** full constructor */
	public TRoleProfileId(TRole TRole, TProfile TProfile) {
		this.TRole = TRole;
		this.TProfile = TProfile;
	}

	// Property accessors

	public TRole getTRole() {
		return this.TRole;
	}

	public void setTRole(TRole TRole) {
		this.TRole = TRole;
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
		if (!(other instanceof TRoleProfileId))
			return false;
		TRoleProfileId castOther = (TRoleProfileId) other;

		return ((this.getTRole() == castOther.getTRole()) || (this.getTRole() != null
				&& castOther.getTRole() != null && this.getTRole().equals(
				castOther.getTRole())))
				&& ((this.getTProfile() == castOther.getTProfile()) || (this
						.getTProfile() != null
						&& castOther.getTProfile() != null && this
						.getTProfile().equals(castOther.getTProfile())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTRole() == null ? 0 : this.getTRole().hashCode());
		result = 37 * result
				+ (getTProfile() == null ? 0 : this.getTProfile().hashCode());
		return result;
	}

}