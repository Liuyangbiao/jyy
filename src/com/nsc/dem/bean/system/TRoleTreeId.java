package com.nsc.dem.bean.system;

import com.nsc.dem.bean.profile.TRole;

/**
 * TRoleTreeId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TRoleTreeId implements java.io.Serializable {

	// Fields

	private TRole TRole;
	private TTreeDef TTreeDef;

	// Constructors

	/** default constructor */
	public TRoleTreeId() {
	}

	/** full constructor */
	public TRoleTreeId(TRole TRole, TTreeDef TTreeDef) {
		this.TRole = TRole;
		this.TTreeDef = TTreeDef;
	}

	// Property accessors

	public TRole getTRole() {
		return this.TRole;
	}

	public void setTRole(TRole TRole) {
		this.TRole = TRole;
	}

	public TTreeDef getTTreeDef() {
		return this.TTreeDef;
	}

	public void setTTreeDef(TTreeDef TTreeDef) {
		this.TTreeDef = TTreeDef;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TRoleTreeId))
			return false;
		TRoleTreeId castOther = (TRoleTreeId) other;

		return ((this.getTRole() == castOther.getTRole()) || (this.getTRole() != null
				&& castOther.getTRole() != null && this.getTRole().equals(
				castOther.getTRole())))
				&& ((this.getTTreeDef() == castOther.getTTreeDef()) || (this
						.getTTreeDef() != null
						&& castOther.getTTreeDef() != null && this
						.getTTreeDef().equals(castOther.getTTreeDef())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTRole() == null ? 0 : this.getTRole().hashCode());
		result = 37 * result
				+ (getTTreeDef() == null ? 0 : this.getTTreeDef().hashCode());
		return result;
	}

}