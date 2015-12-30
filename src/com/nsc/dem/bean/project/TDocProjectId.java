package com.nsc.dem.bean.project;

import com.nsc.dem.bean.archives.TDoc;

/**
 * TDocProjectId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TDocProjectId implements java.io.Serializable {

	// Fields

	private TDoc TDoc;
	private TProject TProject;

	// Constructors

	/** default constructor */
	public TDocProjectId() {
	}

	/** full constructor */
	public TDocProjectId(TDoc TDoc, TProject TProject) {
		this.TDoc = TDoc;
		this.TProject = TProject;
	}

	// Property accessors

	public TDoc getTDoc() {
		return this.TDoc;
	}

	public void setTDoc(TDoc TDoc) {
		this.TDoc = TDoc;
	}

	public TProject getTProject() {
		return this.TProject;
	}

	public void setTProject(TProject TProject) {
		this.TProject = TProject;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TDocProjectId))
			return false;
		TDocProjectId castOther = (TDocProjectId) other;

		return ((this.getTDoc() == castOther.getTDoc()) || (this.getTDoc() != null
				&& castOther.getTDoc() != null && this.getTDoc().equals(
				castOther.getTDoc())))
				&& ((this.getTProject() == castOther.getTProject()) || (this
						.getTProject() != null
						&& castOther.getTProject() != null && this
						.getTProject().equals(castOther.getTProject())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTDoc() == null ? 0 : this.getTDoc().hashCode());
		result = 37 * result
				+ (getTProject() == null ? 0 : this.getTProject().hashCode());
		return result;
	}

}