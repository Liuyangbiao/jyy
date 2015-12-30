package com.nsc.dem.bean.archives;

import java.util.Date;

import com.nsc.dem.bean.profile.TUser;

/**
 * TSynDoc entity. @author MyEclipse Persistence Tools
 */

public class TSynDoc implements java.io.Serializable {

	// Fields

	private String id;
	private TSynProject TSynProject;
	private String name;
	private String suffix;
	private String path;
	private String previewPath;
	private String storeLocation;
	
	private String docType;
	private FileInfo fi;
	private TUser user;
	private Date createDate;

	// Constructors

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public TUser getUser() {
		return user;
	}

	public void setUser(TUser user) {
		this.user = user;
	}

	public FileInfo getFi() {
		return fi;
	}

	public void setFi(FileInfo fi) {
		this.fi = fi;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	/** default constructor */
	public TSynDoc() {
	}

	/** minimal constructor */
	public TSynDoc(String id) {
		this.id = id;
	}

	/** full constructor */
	public TSynDoc(String id, TSynProject TSynProject, String name,
			String suffix, String path, String previewPath) {
		this.id = id;
		this.TSynProject = TSynProject;
		this.name = name;
		this.suffix = suffix;
		this.path = path;
		this.previewPath = previewPath;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TSynProject getTSynProject() {
		return this.TSynProject;
	}

	public void setTSynProject(TSynProject TSynProject) {
		this.TSynProject = TSynProject;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPreviewPath() {
		return this.previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	
}