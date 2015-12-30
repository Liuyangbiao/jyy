package com.nsc.dem.bean.project;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TUnit;

/**
 * TTender entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TTender implements java.io.Serializable {

	// Fields

	private String id;
	private TProject TProject;
	private TUnit TUnit;
	private TUser TUser;
	private String name;
	private String type;
	private String spvsUnitId;
	private Date createDate;
	private Set TShopDrawings = new HashSet(0);
	private Set TShopDocs = new HashSet(0);
	private Set TRecordDrawings = new HashSet(0);
	private Set TComponentsForConstrTenderId = new HashSet(0);
	private Set TComponentsForDesignTenderId = new HashSet(0);

	// Constructors

	/** default constructor */
	public TTender() {
	}

	/** minimal constructor */
	public TTender(String id, TProject TProject, TUnit TUnit, TUser TUser,
			String name, String type, String spvsUnitId, Date createDate) {
		this.id = id;
		this.TProject = TProject;
		this.TUnit = TUnit;
		this.TUser = TUser;
		this.name = name;
		this.type = type;
		this.spvsUnitId = spvsUnitId;
		this.createDate = createDate;
	}

	/** full constructor */
	public TTender(String id, TProject TProject, TUnit TUnit, TUser TUser,
			String name, String type, String spvsUnitId, Date createDate,
			Set TShopDrawings, Set TShopDocs, Set TRecordDrawings,
			Set TComponentsForConstrTenderId, Set TComponentsForDesignTenderId) {
		this.id = id;
		this.TProject = TProject;
		this.TUnit = TUnit;
		this.TUser = TUser;
		this.name = name;
		this.type = type;
		this.spvsUnitId = spvsUnitId;
		this.createDate = createDate;
		this.TShopDrawings = TShopDrawings;
		this.TShopDocs = TShopDocs;
		this.TRecordDrawings = TRecordDrawings;
		this.TComponentsForConstrTenderId = TComponentsForConstrTenderId;
		this.TComponentsForDesignTenderId = TComponentsForDesignTenderId;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TProject getTProject() {
		return this.TProject;
	}

	public void setTProject(TProject TProject) {
		this.TProject = TProject;
	}

	public TUnit getTUnit() {
		return this.TUnit;
	}

	public void setTUnit(TUnit TUnit) {
		this.TUnit = TUnit;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSpvsUnitId() {
		return this.spvsUnitId;
	}

	public void setSpvsUnitId(String spvsUnitId) {
		this.spvsUnitId = spvsUnitId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getTShopDrawings() {
		return this.TShopDrawings;
	}

	public void setTShopDrawings(Set TShopDrawings) {
		this.TShopDrawings = TShopDrawings;
	}

	public Set getTShopDocs() {
		return this.TShopDocs;
	}

	public void setTShopDocs(Set TShopDocs) {
		this.TShopDocs = TShopDocs;
	}

	public Set getTRecordDrawings() {
		return this.TRecordDrawings;
	}

	public void setTRecordDrawings(Set TRecordDrawings) {
		this.TRecordDrawings = TRecordDrawings;
	}

	public Set getTComponentsForConstrTenderId() {
		return this.TComponentsForConstrTenderId;
	}

	public void setTComponentsForConstrTenderId(Set TComponentsForConstrTenderId) {
		this.TComponentsForConstrTenderId = TComponentsForConstrTenderId;
	}

	public Set getTComponentsForDesignTenderId() {
		return this.TComponentsForDesignTenderId;
	}

	public void setTComponentsForDesignTenderId(Set TComponentsForDesignTenderId) {
		this.TComponentsForDesignTenderId = TComponentsForDesignTenderId;
	}

}