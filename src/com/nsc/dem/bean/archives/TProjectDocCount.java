package com.nsc.dem.bean.archives;

import java.math.BigDecimal;
import java.util.Date;

import com.nsc.dem.bean.system.TUnit;

/**
 * TProjectDocCount entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TProjectDocCount implements java.io.Serializable {

	// Fields

	private Long id;
	private TUnit TUnit;
	private Long projectId;
	private String projectName;
	private String voltageLevel;
	private BigDecimal docCount;
	private String yearMonth;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public TProjectDocCount() {
	}

	/** minimal constructor */
	public TProjectDocCount(TUnit TUnit, Long projectId, String projectName,
			String voltageLevel, BigDecimal docCount, String yearMonth) {
		this.TUnit = TUnit;
		this.projectId = projectId;
		this.projectName = projectName;
		this.voltageLevel = voltageLevel;
		this.docCount = docCount;
		this.yearMonth = yearMonth;
	}

	/** full constructor */
	public TProjectDocCount(TUnit TUnit, Long projectId, String projectName,
			String voltageLevel, BigDecimal docCount, String yearMonth,
			Date updateTime) {
		this.TUnit = TUnit;
		this.projectId = projectId;
		this.projectName = projectName;
		this.voltageLevel = voltageLevel;
		this.docCount = docCount;
		this.yearMonth = yearMonth;
		this.updateTime = updateTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TUnit getTUnit() {
		return this.TUnit;
	}

	public void setTUnit(TUnit TUnit) {
		this.TUnit = TUnit;
	}

	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVoltageLevel() {
		return this.voltageLevel;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public BigDecimal getDocCount() {
		return this.docCount;
	}

	public void setDocCount(BigDecimal docCount) {
		this.docCount = docCount;
	}

	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}