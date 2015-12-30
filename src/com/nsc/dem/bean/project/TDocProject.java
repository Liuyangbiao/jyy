package com.nsc.dem.bean.project;

/**
 * TDocProject entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TDocProject implements java.io.Serializable {

	// Fields

	private TDocProjectId id;
	private String remark;

	// Constructors

	/** default constructor */
	public TDocProject() {
	}

	/** minimal constructor */
	public TDocProject(TDocProjectId id) {
		this.id = id;
	}

	/** full constructor */
	public TDocProject(TDocProjectId id, String remark) {
		this.id = id;
		this.remark = remark;
	}

	// Property accessors

	public TDocProjectId getId() {
		return this.id;
	}

	public void setId(TDocProjectId id) {
		this.id = id;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}