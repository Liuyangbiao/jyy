package com.nsc.dem.action.project;

public class ProNode {
	
	//工程id
	private Long id;
	//工程编码
	private String code;
	//工程名称
	private String name;
	//是否有子节点
	private boolean leaf;
	//标志位
	private String flag="1";
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}
