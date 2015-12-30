package com.nsc.dem.bean.project;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;

/**
 * TComponent entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TComponent implements java.io.Serializable {

	// Fields

	private String id;
	private TTender TTenderByConstrTenderId;
	private TTender TTenderByDesignTenderId;
	private TUser TUser;
	private String name;
	private String parentId;
	private Long constrNo;
	private String designNo;
	private Date createDate;
	private Set TComponentDocs = new HashSet(0);

	// Constructors

	/** default constructor */
	public TComponent() {
	}

	/** minimal constructor */
	public TComponent(String id, TTender TTenderByConstrTenderId,
			TTender TTenderByDesignTenderId, TUser TUser, String name,
			String parentId, Date createDate) {
		this.id = id;
		this.TTenderByConstrTenderId = TTenderByConstrTenderId;
		this.TTenderByDesignTenderId = TTenderByDesignTenderId;
		this.TUser = TUser;
		this.name = name;
		this.parentId = parentId;
		this.createDate = createDate;
	}

	/** full constructor */
	public TComponent(String id, TTender TTenderByConstrTenderId,
			TTender TTenderByDesignTenderId, TUser TUser, String name,
			String parentId, Long constrNo, String designNo, Date createDate,
			Set TComponentDocs) {
		this.id = id;
		this.TTenderByConstrTenderId = TTenderByConstrTenderId;
		this.TTenderByDesignTenderId = TTenderByDesignTenderId;
		this.TUser = TUser;
		this.name = name;
		this.parentId = parentId;
		this.constrNo = constrNo;
		this.designNo = designNo;
		this.createDate = createDate;
		this.TComponentDocs = TComponentDocs;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TTender getTTenderByConstrTenderId() {
		return this.TTenderByConstrTenderId;
	}

	public void setTTenderByConstrTenderId(TTender TTenderByConstrTenderId) {
		this.TTenderByConstrTenderId = TTenderByConstrTenderId;
	}

	public TTender getTTenderByDesignTenderId() {
		return this.TTenderByDesignTenderId;
	}

	public void setTTenderByDesignTenderId(TTender TTenderByDesignTenderId) {
		this.TTenderByDesignTenderId = TTenderByDesignTenderId;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Long getConstrNo() {
		return this.constrNo;
	}

	public void setConstrNo(Long constrNo) {
		this.constrNo = constrNo;
	}

	public String getDesignNo() {
		return this.designNo;
	}

	public void setDesignNo(String designNo) {
		this.designNo = designNo;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getTComponentDocs() {
		return this.TComponentDocs;
	}

	public void setTComponentDocs(Set TComponentDocs) {
		this.TComponentDocs = TComponentDocs;
	}

}