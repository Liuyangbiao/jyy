package com.nsc.dem.bean.project;

/**
 * TComponentDoc entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TComponentDoc implements java.io.Serializable {

	// Fields

	private TComponentDocId id;

	// Constructors

	/** default constructor */
	public TComponentDoc() {
	}

	/** full constructor */
	public TComponentDoc(TComponentDocId id) {
		this.id = id;
	}

	// Property accessors

	public TComponentDocId getId() {
		return this.id;
	}

	public void setId(TComponentDocId id) {
		this.id = id;
	}

}