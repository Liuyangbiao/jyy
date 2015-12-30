package com.nsc.dem.bean.project;

import java.util.List;

import com.nsc.dem.bean.archives.FileInfo;


public class MainProject {
	//FTP方式下载
	private String ftpserver;
	private String ftpuser;
	private String ftppwd;
	private String ftpPort;
	private String xmlPath;
	
	//http方式下载
	private String fileId;
	private String fileName;
	private String area;
	private String address;
	
	private String projectId;
	private String projectName;
	private String code;
	private TProject tProject;
	//private String description;
	private List<FileInfo> fileList;
	private List<ChildProject> childProjects;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public List<FileInfo> getFileList() {
		return fileList;
	}
	public void setFileList(List<FileInfo> fileList) {
		this.fileList = fileList;
	}
	public List<ChildProject> getChildProjects() {
		return childProjects;
	}
	public void setChildProjects(List<ChildProject> childProjects) {
		this.childProjects = childProjects;
	}
	public TProject gettProject() {
		return tProject;
	}
	public void settProject(TProject tProject) {
		this.tProject = tProject;
	}
	public String getFtpserver() {
		return ftpserver;
	}
	public void setFtpserver(String ftpserver) {
		this.ftpserver = ftpserver;
	}
	public String getFtpuser() {
		return ftpuser;
	}
	public void setFtpuser(String ftpuser) {
		this.ftpuser = ftpuser;
	}
	public String getFtppwd() {
		return ftppwd;
	}
	public void setFtppwd(String ftppwd) {
		this.ftppwd = ftppwd;
	}
	public String getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getXmlPath() {
		return xmlPath;
	}
	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
