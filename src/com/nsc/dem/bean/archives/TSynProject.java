package com.nsc.dem.bean.archives;

import java.util.HashSet;
import java.util.Set;

/**
 * TSynProject entity. @author MyEclipse Persistence Tools
 */

public class TSynProject implements java.io.Serializable {

	// Fields

	private String code;
	private String name;
	private String ownerUnitId;
	private String approveUnitid;
	private String giveoutUnitid;
	private Set TSynDocs = new HashSet(0);

	// Constructors

	/** default constructor */
	public TSynProject() {
	}

	/** minimal constructor */
	public TSynProject(String code) {
		this.code = code;
	}

	/** full constructor */
	public TSynProject(String code, String name, String ownerUnitId,
			String approveUnitid, String giveoutUnitid, Set TSynDocs) {
		this.code = code;
		this.name = name;
		this.ownerUnitId = ownerUnitId;
		this.approveUnitid = approveUnitid;
		this.giveoutUnitid = giveoutUnitid;
		this.TSynDocs = TSynDocs;
	}

	// Property accessors

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerUnitId() {
		return this.ownerUnitId;
	}

	public void setOwnerUnitId(String ownerUnitId) {
		this.ownerUnitId = ownerUnitId;
	}

	public String getApproveUnitid() {
		return this.approveUnitid;
	}

	public void setApproveUnitid(String approveUnitid) {
		this.approveUnitid = approveUnitid;
	}

	public String getGiveoutUnitid() {
		return this.giveoutUnitid;
	}

	public void setGiveoutUnitid(String giveoutUnitid) {
		this.giveoutUnitid = giveoutUnitid;
	}

	public Set getTSynDocs() {
		return this.TSynDocs;
	}

	public void setTSynDocs(Set TSynDocs) {
		this.TSynDocs = TSynDocs;
	}

}