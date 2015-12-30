package com.nsc.dem.bean.archives;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nsc.base.annotation.AuditAnnotation;
import com.nsc.base.annotation.IdentifierFieldAnnotation;
import com.nsc.dem.bean.profile.TUser;

/**
 * TDocType entity. @author MyEclipse Persistence Tools
 */
@IdentifierFieldAnnotation(identifier="code")
@SuppressWarnings(value={"serial","unchecked"})
public class TDocType implements java.io.Serializable {
	private String code;
	private TUser TUser;
	private String name;
	private String remark;
	private String dftSecurity;
	private String privilege;
	private Date createDate;
	private String speciality;
	private Set TDocs = new HashSet(0);
	private String parentCode;
	private String comFlag;
	

	private List<TDocType> list;
	// Constructors




	/** default constructor */
	public TDocType() {
	}
	

	public String getComFlag() {
		return comFlag;
	}

	public void setComFlag(String comFlag) {
		this.comFlag = comFlag;
	}

	/** minimal constructor */
	public TDocType(String code, TUser TUser, String name, String remark,
			Date createDate, String speciality) {
		this.code = code;
		this.TUser = TUser;
		this.name = name;
		this.remark = remark;
		this.createDate = createDate;
		this.speciality = speciality;
	}

	/** full constructor */
	public TDocType(String code, TUser TUser, String name, String remark,
			String dftSecurity, String privilege, Date createDate,
			String speciality, Set TDocs) {
		this.code = code;
		this.TUser = TUser;
		this.name = name;
		this.remark = remark;
		this.dftSecurity = dftSecurity;
		this.privilege = privilege;
		this.createDate = createDate;
		this.speciality = speciality;
		this.TDocs = TDocs;
	}

	// Property accessors
	@AuditAnnotation(isRequired=true)
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDftSecurity() {
		return this.dftSecurity;
	}

	public void setDftSecurity(String dftSecurity) {
		this.dftSecurity = dftSecurity;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public Set getTDocs() {
		return this.TDocs;
	}

	public void setTDocs(Set TDocs) {
		this.TDocs = TDocs;
	}
	
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public List<TDocType> getList() {
		return list;
	}

	public void setList(List<TDocType> list) {
		this.list = list;
	}
}