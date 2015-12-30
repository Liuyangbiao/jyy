package com.nsc.dem.bean.archives;

import java.util.Date;

/**
 * TUserQuery entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TUserQuery implements java.io.Serializable {

	// Fields

	private TUserQueryId id;
	private String queryParams;
	private Date createDate;

	// Constructors

	/** default constructor */
	public TUserQuery() {
	}

	/** minimal constructor */
	public TUserQuery(TUserQueryId id, String queryParams) {
		this.id = id;
		this.queryParams = queryParams;
	}

	/** full constructor */
	public TUserQuery(TUserQueryId id, String queryParams, Date createDate) {
		this.id = id;
		this.queryParams = queryParams;
		this.createDate = createDate;
	}

	// Property accessors

	public TUserQueryId getId() {
		return this.id;
	}

	public void setId(TUserQueryId id) {
		this.id = id;
	}

	public String getQueryParams() {
		return this.queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}