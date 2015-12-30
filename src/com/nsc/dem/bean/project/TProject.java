package com.nsc.dem.bean.project;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TUnit;

/**
 * TProject entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings(value={"serial","unchecked"})
public class TProject implements java.io.Serializable {

	// Fields

	private Long id;
	private TUnit TUnitByOwnerUnitId;
	private TUser TUser;
	private TUnit TUnitByDesignUnitId;
	private String code;
	private String name;
	private String type;
	private String voltageLevel;
	private Date preDesignYear;
	private Date openYear;
	private Date closeYear;
	private String status;
	private Date createDate;
	private Long parentId;
	private Set TDocProjects = new HashSet(0);
	private Set TTenders = new HashSet(0);
	private TUnit approveUnit;
	private String giveoutUnitId;
	// Constructors

	public TUnit getApproveUnit() {
		return approveUnit;
	}

	public void setApproveUnit(TUnit approveUnit) {
		this.approveUnit = approveUnit;
	}

	public String getGiveoutUnitId() {
		return giveoutUnitId;
	}

	public void setGiveoutUnitId(String giveoutUnitId) {
		this.giveoutUnitId = giveoutUnitId;
	}

	/** default constructor */
	public TProject() {
	}

	/** minimal constructor */
	public TProject(Long id, TUnit TUnitByOwnerUnitId, TUser TUser,
			TUnit TUnitByDesignUnitId, String code, String name, String type,
			String voltageLevel, String status, Date createDate) {
		this.id = id;
		this.TUnitByOwnerUnitId = TUnitByOwnerUnitId;
		this.TUser = TUser;
		this.TUnitByDesignUnitId = TUnitByDesignUnitId;
		this.code = code;
		this.name = name;
		this.type = type;
		this.voltageLevel = voltageLevel;
		this.status = status;
		this.createDate = createDate;
	}

	/** full constructor */
	public TProject(Long id, TUnit TUnitByOwnerUnitId, TUser TUser,
			TUnit TUnitByDesignUnitId, String code, String name, String type,
			String voltageLevel, Date preDesignYear, Date openYear,
			Date closeYear, String status, Date createDate, Long parentId,
			Set TDocProjects, Set TTenders) {
		this.id = id;
		this.TUnitByOwnerUnitId = TUnitByOwnerUnitId;
		this.TUser = TUser;
		this.TUnitByDesignUnitId = TUnitByDesignUnitId;
		this.code = code;
		this.name = name;
		this.type = type;
		this.voltageLevel = voltageLevel;
		this.preDesignYear = preDesignYear;
		this.openYear = openYear;
		this.closeYear = closeYear;
		this.status = status;
		this.createDate = createDate;
		this.parentId = parentId;
		this.TDocProjects = TDocProjects;
		this.TTenders = TTenders;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TUnit getTUnitByOwnerUnitId() {
		return this.TUnitByOwnerUnitId;
	}

	public void setTUnitByOwnerUnitId(TUnit TUnitByOwnerUnitId) {
		this.TUnitByOwnerUnitId = TUnitByOwnerUnitId;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	public TUnit getTUnitByDesignUnitId() {
		return this.TUnitByDesignUnitId;
	}

	public void setTUnitByDesignUnitId(TUnit TUnitByDesignUnitId) {
		this.TUnitByDesignUnitId = TUnitByDesignUnitId;
	}

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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVoltageLevel() {
		return this.voltageLevel;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public Date getPreDesignYear() {
		return this.preDesignYear;
	}

	public void setPreDesignYear(Date preDesignYear) {
		this.preDesignYear = preDesignYear;
	}

	public Date getOpenYear() {
		return this.openYear;
	}

	public void setOpenYear(Date openYear) {
		this.openYear = openYear;
	}

	public Date getCloseYear() {
		return this.closeYear;
	}

	public void setCloseYear(Date closeYear) {
		this.closeYear = closeYear;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Set getTDocProjects() {
		return this.TDocProjects;
	}

	public void setTDocProjects(Set TDocProjects) {
		this.TDocProjects = TDocProjects;
	}

	public Set getTTenders() {
		return this.TTenders;
	}

	public void setTTenders(Set TTenders) {
		this.TTenders = TTenders;
	}

}