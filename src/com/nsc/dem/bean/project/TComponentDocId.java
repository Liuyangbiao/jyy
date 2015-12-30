package com.nsc.dem.bean.project;

import com.nsc.dem.bean.archives.TDoc;

/**
 * TComponentDocId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TComponentDocId implements java.io.Serializable {

	// Fields

	private TComponent TComponent;
	private TDoc TDoc;

	// Constructors

	/** default constructor */
	public TComponentDocId() {
	}

	/** full constructor */
	public TComponentDocId(TComponent TComponent, TDoc TDoc) {
		this.TComponent = TComponent;
		this.TDoc = TDoc;
	}

	// Property accessors

	public TComponent getTComponent() {
		return this.TComponent;
	}

	public void setTComponent(TComponent TComponent) {
		this.TComponent = TComponent;
	}

	public TDoc getTDoc() {
		return this.TDoc;
	}

	public void setTDoc(TDoc TDoc) {
		this.TDoc = TDoc;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TComponentDocId))
			return false;
		TComponentDocId castOther = (TComponentDocId) other;

		return ((this.getTComponent() == castOther.getTComponent()) || (this
				.getTComponent() != null
				&& castOther.getTComponent() != null && this.getTComponent()
				.equals(castOther.getTComponent())))
				&& ((this.getTDoc() == castOther.getTDoc()) || (this.getTDoc() != null
						&& castOther.getTDoc() != null && this.getTDoc()
						.equals(castOther.getTDoc())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getTComponent() == null ? 0 : this.getTComponent()
						.hashCode());
		result = 37 * result
				+ (getTDoc() == null ? 0 : this.getTDoc().hashCode());
		return result;
	}

}