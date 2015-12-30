package com.nsc.dem.bean.system;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;

/**
 * TNodeDef entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TNodeDef implements java.io.Serializable {

	// Fields

	private Long id;
	private TUser TUser;
	private String name;
	private String type;
	private String source;
	private String remark;
	private Date createDate;
	private Set TTreeDefs = new HashSet(0);
	
	private String params;	
	private String sqlPlace;

	/** default constructor */
	public TNodeDef() {
	}

	/** minimal constructor */
	public TNodeDef(Long id, TUser TUser, String name, String type,
			String source, String remark, Date createDate) {
		this.id = id;
		this.TUser = TUser;
		this.name = name;
		this.type = type;
		this.source = source;
		this.remark = remark;
		this.createDate = createDate;
	}

	/** full constructor */
	public TNodeDef(Long id, TUser TUser, String name, String type,
			String source, String remark, Date createDate, Set TTreeDefs) {
		this.id = id;
		this.TUser = TUser;
		this.name = name;
		this.type = type;
		this.source = source;
		this.remark = remark;
		this.createDate = createDate;
		this.TTreeDefs = TTreeDefs;
	}

	// Property accessors
	

	public String getSqlPlace() {
		return sqlPlace;
	}

	public void setSqlPlace(String sqlPlace) {
		this.sqlPlace = sqlPlace;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getTTreeDefs() {
		return this.TTreeDefs;
	}

	public void setTTreeDefs(Set TTreeDefs) {
		this.TTreeDefs = TTreeDefs;
	}

}