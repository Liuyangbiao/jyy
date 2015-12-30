package com.nsc.dem.bean.project;

import java.util.Date;

import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.system.TUnit;

/**
 * TPreDesgin entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TPreDesgin implements java.io.Serializable {

	// Fields

	private String docId;
	private TDoc TDoc;
	private TUnit TUnit;
	private Date createDate;
	


	private String sjrm;
	private String xhrm;
	private String shrm;
	private String pzrm;
	private String ljrm;
	private String jcrm;
	private String sjjd;
	private String ajtm;
	private String tzmc;
	private String tzzs;
	private String flbh;
	private String andh;
	private String jnyh;
	
	

	// Constructors

	/** default constructor */
	public TPreDesgin() {
	}

	/** minimal constructor */
	public TPreDesgin(String docId, TDoc TDoc) {
		this.docId = docId;
		this.TDoc = TDoc;
	}

	/** full constructor */
	public TPreDesgin(String docId, TDoc TDoc, TUnit TUnit, Date createDate) {
		this.docId = docId;
		this.TDoc = TDoc;
		this.TUnit = TUnit;
		this.createDate = createDate;
	}

	// Property accessors

	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public TDoc getTDoc() {
		return this.TDoc;
	}

	public void setTDoc(TDoc TDoc) {
		this.TDoc = TDoc;
	}

	public TUnit getTUnit() {
		return this.TUnit;
	}

	public void setTUnit(TUnit TUnit) {
		this.TUnit = TUnit;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public String getSjrm() {
		return sjrm;
	}

	public void setSjrm(String sjrm) {
		this.sjrm = sjrm;
	}

	public String getXhrm() {
		return xhrm;
	}

	public void setXhrm(String xhrm) {
		this.xhrm = xhrm;
	}

	public String getShrm() {
		return shrm;
	}

	public void setShrm(String shrm) {
		this.shrm = shrm;
	}

	public String getPzrm() {
		return pzrm;
	}

	public void setPzrm(String pzrm) {
		this.pzrm = pzrm;
	}

	public String getLjrm() {
		return ljrm;
	}

	public void setLjrm(String ljrm) {
		this.ljrm = ljrm;
	}

	public String getJcrm() {
		return jcrm;
	}

	public void setJcrm(String jcrm) {
		this.jcrm = jcrm;
	}

	public String getSjjd() {
		return sjjd;
	}

	public void setSjjd(String sjjd) {
		this.sjjd = sjjd;
	}

	public String getAjtm() {
		return ajtm;
	}

	public void setAjtm(String ajtm) {
		this.ajtm = ajtm;
	}

	public String getTzmc() {
		return tzmc;
	}

	public void setTzmc(String tzmc) {
		this.tzmc = tzmc;
	}

	public String getTzzs() {
		return tzzs;
	}

	public void setTzzs(String tzzs) {
		this.tzzs = tzzs;
	}

	public String getFlbh() {
		return flbh;
	}

	public void setFlbh(String flbh) {
		this.flbh = flbh;
	}

	public String getAndh() {
		return andh;
	}

	public void setAndh(String andh) {
		this.andh = andh;
	}

	public String getJnyh() {
		return jnyh;
	}

	public void setJnyh(String jnyh) {
		this.jnyh = jnyh;
	}

}