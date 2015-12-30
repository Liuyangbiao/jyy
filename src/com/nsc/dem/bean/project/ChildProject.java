package com.nsc.dem.bean.project;

import java.util.List;

import com.nsc.dem.bean.archives.FileInfo;


public class ChildProject {
	private String projectId;
	private String code;
	private String projectName;
	private String description;
	private TProject tProject;
	private List<FileInfo> fileList;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<FileInfo> getFileList() {
		return fileList;
	}
	public void setFileList(List<FileInfo> fileList) {
		this.fileList = fileList;
	}
	public TProject gettProject() {
		return tProject;
	}
	public void settProject(TProject tProject) {
		this.tProject = tProject;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
