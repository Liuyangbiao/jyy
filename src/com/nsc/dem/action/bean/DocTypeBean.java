package com.nsc.dem.action.bean;

import com.nsc.dem.bean.archives.TDocType;

/*
 * µµ∞∏≤È—ØBean
 * */
public class DocTypeBean {

	private TDocType tdocType;
	private String createFormDate;
	private String createToDate;

	public TDocType getTdocType() {
		return tdocType;
	}

	public void setTdocType(TDocType tdocType) {
		this.tdocType = tdocType;
	}

	public String getCreateFormDate() {
		return createFormDate;
	}

	public void setCreateFormDate(String createFormDate) {
		this.createFormDate = createFormDate;
	}

	public String getCreateToDate() {
		return createToDate;
	}

	public void setCreateToDate(String createToDate) {
		this.createToDate = createToDate;
	}
}
