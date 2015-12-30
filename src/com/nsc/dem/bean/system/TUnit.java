package com.nsc.dem.bean.system;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;

/**
 * TUnit entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value = { "serial", "unchecked" })
public class TUnit implements java.io.Serializable {

	// Fields

	private String code;
	private TUser TUser;
	private String name;
	private String shortName;
	private String address;
	private String telephone;
	private Boolean isUsable;
	private String type;
	private Date createDate;
	private Set TShopDrawings = new HashSet(0);
	private Set TProjectsForDesignUnitId = new HashSet(0);
	private Set TProjectsForOwnerUnitId = new HashSet(0);
	private Set TUsers = new HashSet(0);
	private Set TTenders = new HashSet(0);
	private Set TRecordDrawings = new HashSet(0);
	private Set TPreDesgins = new HashSet(0);
	private Set TShopDocs = new HashSet(0);
	private String proxyCode;

	// Constructors

	public String getProxyCode() {
		if (proxyCode == null || proxyCode.equals("")) {
			return this.code;
		} else {
			return this.proxyCode;
		}

	}

	public void setProxyCode(String proxyCode) {
		this.proxyCode = proxyCode;
	}

	/** default constructor */
	public TUnit() {
	}

	/** minimal constructor */
	public TUnit(String code, TUser TUser, String name, Boolean isUsable,
			String type) {
		this.code = code;
		this.TUser = TUser;
		this.name = name;
		this.isUsable = isUsable;
		this.type = type;
	}

	/** full constructor */
	public TUnit(String code, TUser TUser, String name, String shortName,
			String address, String telephone, Boolean isUsable, String type,
			Date createDate, Set TShopDrawings, Set TProjectsForDesignUnitId,
			Set TProjectsForOwnerUnitId, Set TUsers, Set TTenders,
			Set TRecordDrawings, Set TPreDesgins, Set TShopDocs) {
		this.code = code;
		this.TUser = TUser;
		this.name = name;
		this.shortName = shortName;
		this.address = address;
		this.telephone = telephone;
		this.isUsable = isUsable;
		this.type = type;
		this.createDate = createDate;
		this.TShopDrawings = TShopDrawings;
		this.TProjectsForDesignUnitId = TProjectsForDesignUnitId;
		this.TProjectsForOwnerUnitId = TProjectsForOwnerUnitId;
		this.TUsers = TUsers;
		this.TTenders = TTenders;
		this.TRecordDrawings = TRecordDrawings;
		this.TPreDesgins = TPreDesgins;
		this.TShopDocs = TShopDocs;
	}

	// Property accessors

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Boolean getIsUsable() {
		return this.isUsable;
	}

	public void setIsUsable(Boolean isUsable) {
		this.isUsable = isUsable;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Set getTProjectsForDesignUnitId() {
		return this.TProjectsForDesignUnitId;
	}

	public void setTProjectsForDesignUnitId(Set TProjectsForDesignUnitId) {
		this.TProjectsForDesignUnitId = TProjectsForDesignUnitId;
	}

	public Set getTProjectsForOwnerUnitId() {
		return this.TProjectsForOwnerUnitId;
	}

	public void setTProjectsForOwnerUnitId(Set TProjectsForOwnerUnitId) {
		this.TProjectsForOwnerUnitId = TProjectsForOwnerUnitId;
	}

	public Set getTUsers() {
		return this.TUsers;
	}

	public void setTUsers(Set TUsers) {
		this.TUsers = TUsers;
	}

	public Set getTTenders() {
		return this.TTenders;
	}

	public void setTTenders(Set TTenders) {
		this.TTenders = TTenders;
	}

	public Set getTRecordDrawings() {
		return this.TRecordDrawings;
	}

	public void setTRecordDrawings(Set TRecordDrawings) {
		this.TRecordDrawings = TRecordDrawings;
	}

	public Set getTPreDesgins() {
		return this.TPreDesgins;
	}

	public void setTPreDesgins(Set TPreDesgins) {
		this.TPreDesgins = TPreDesgins;
	}

	public Set getTShopDocs() {
		return this.TShopDocs;
	}

	public void setTShopDocs(Set TShopDocs) {
		this.TShopDocs = TShopDocs;
	}

}