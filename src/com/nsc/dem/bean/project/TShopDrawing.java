package com.nsc.dem.bean.project;

import java.util.Date;

import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.system.TUnit;

/**
 * TShopDrawing entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TShopDrawing implements java.io.Serializable {

	// Fields

	private String docId;
	private TTender TTender;
	private TDoc TDoc;
	private TUnit TUnit;
	private Date createDate;

	// Constructors

	/** default constructor */
	public TShopDrawing() {
	}

	/** minimal constructor */
	public TShopDrawing(String docId, TTender TTender, TDoc TDoc) {
		this.docId = docId;
		this.TTender = TTender;
		this.TDoc = TDoc;
	}

	/** full constructor */
	public TShopDrawing(String docId, TTender TTender, TDoc TDoc, TUnit TUnit,
			Date createDate) {
		this.docId = docId;
		this.TTender = TTender;
		this.TDoc = TDoc;
		this.TUnit = TUnit;
		this.createDate = createDate;
	}

	// Property accessors

	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public TTender getTTender() {
		return this.TTender;
	}

	public void setTTender(TTender TTender) {
		this.TTender = TTender;
	}

	public TDoc getTDoc() {
		return this.TDoc;
	}

	public void setTDoc(TDoc TDoc) {
		this.TDoc = TDoc;
	}

	public TUnit getTUnit() {
		return this.TUnit;
	}

	public void setTUnit(TUnit TUnit) {
		this.TUnit = TUnit;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}