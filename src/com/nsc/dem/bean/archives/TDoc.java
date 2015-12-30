package com.nsc.dem.bean.archives;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import com.nsc.base.annotation.AuditAnnotation;
import com.nsc.base.annotation.StringMessageAnnotation;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.base.annotation.IdentifierFieldAnnotation;

/**
 * TDoc entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("unchecked")
@IdentifierFieldAnnotation(identifier="id")
public class TDoc implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields

	private String id;
	private TUser TUser;
	private TDocType TDocType;
	private String name;
	private String format;
	private String suffix;
	private String path;
	private BigDecimal fileSize;
	private BigDecimal metaFlag;
	private String security;
	private String version;
	private String status;
	private Timestamp createDate;
	private Set TShopDrawings = new HashSet(0);
	private Set TDocProjects = new HashSet(0);
	private Set TShopDocs = new HashSet(0);
	private Set TComponentDocs = new HashSet(0);
	private Set TRecordDrawings = new HashSet(0);
	private Set TPreDesgins = new HashSet(0);
	private String previewPath;	
	private String remark;	
    private String docFileSize; //文件大小显示
    private String projectid;//工程ID， 用于删除文件时，写入日志
    private String storeLocation;
	// Constructors



	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	/** default constructor */
	public TDoc() {
	}

	/** minimal constructor */
	public TDoc(String id, TUser TUser, TDocType TDocType, String name,
			String format, String suffix, String path, BigDecimal fileSize,
			BigDecimal metaFlag, String security, String status,
			Timestamp createDate) {
		this.id = id;
		this.TUser = TUser;
		this.TDocType = TDocType;
		this.name = name;
		this.format = format;
		this.suffix = suffix;
		this.path = path;
		this.fileSize = fileSize;
		this.metaFlag = metaFlag;
		this.security = security;
		this.status = status;
		this.createDate = createDate;
	}

	/** full constructor */
	public TDoc(String id, TUser TUser, TDocType TDocType, String name,
			String format, String suffix, String path, BigDecimal fileSize,
			BigDecimal metaFlag, String security, String version, String status,
			Timestamp createDate, Set TShopDrawings, Set TDocProjects,
			Set TShopDocs, Set TComponentDocs, Set TRecordDrawings,
			Set TPreDesgins) {
		this.id = id;
		this.TUser = TUser;
		this.TDocType = TDocType;
		this.name = name;
		this.format = format;
		this.suffix = suffix;
		this.path = path;
		this.fileSize = fileSize;
		this.metaFlag = metaFlag;
		this.security = security;
		this.version = version;
		this.status = status;
		this.createDate = createDate;
		this.TShopDrawings = TShopDrawings;
		this.TDocProjects = TDocProjects;
		this.TShopDocs = TShopDocs;
		this.TComponentDocs = TComponentDocs;
		this.TRecordDrawings = TRecordDrawings;
		this.TPreDesgins = TPreDesgins;
	}

	// Property accessors
	@AuditAnnotation(isRequired=true)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@StringMessageAnnotation(nestedBeanValueMethod="getLoginId")
	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}
	@StringMessageAnnotation(nestedBeanValueMethod="getCode")
	public TDocType getTDocType() {
		return this.TDocType;
	}

	public void setTDocType(TDocType TDocType) {
		this.TDocType = TDocType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public BigDecimal getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	public BigDecimal getMetaFlag() {
		return this.metaFlag;
	}

	public void setMetaFlag(BigDecimal metaFlag) {
		this.metaFlag = metaFlag;
	}

	public String getSecurity() {
		return this.security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	@AuditAnnotation(isRequired=false)
	public Set getTShopDrawings() {
		return this.TShopDrawings;
	}
	
	public void setTShopDrawings(Set TShopDrawings) {
		this.TShopDrawings = TShopDrawings;
	}
	@AuditAnnotation(isRequired=false)
	public Set getTDocProjects() {
		return this.TDocProjects;
	}

	public void setTDocProjects(Set TDocProjects) {
		this.TDocProjects = TDocProjects;
	}
	@AuditAnnotation(isRequired=false)
	public Set getTShopDocs() {
		return this.TShopDocs;
	}

	public void setTShopDocs(Set TShopDocs) {
		this.TShopDocs = TShopDocs;
	}
	@AuditAnnotation(isRequired=false)
	public Set getTComponentDocs() {
		return this.TComponentDocs;
	}

	public void setTComponentDocs(Set TComponentDocs) {
		this.TComponentDocs = TComponentDocs;
	}
	@AuditAnnotation(isRequired=false)
	public Set getTRecordDrawings() {
		return this.TRecordDrawings;
	}

	public void setTRecordDrawings(Set TRecordDrawings) {
		this.TRecordDrawings = TRecordDrawings;
	}
	@AuditAnnotation(isRequired=false)
	public Set getTPreDesgins() {
		return this.TPreDesgins;
	}

	public void setTPreDesgins(Set TPreDesgins) {
		this.TPreDesgins = TPreDesgins;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public String getDocFileSize() {
		return docFileSize;
	}

	public void setDocFileSize(String docFileSize) {
		this.docFileSize = docFileSize;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	
}