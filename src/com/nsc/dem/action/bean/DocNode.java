package com.nsc.dem.action.bean;

/*
 * 节点类
 * */
public class DocNode {
	//名称
	private String title;
	//是否有子节点
	private boolean leaf;
	//参数和值
	private Key key;
	
	public String getTitle() {
		return title;
	}

	public boolean isIsLazy() {
		return !this.leaf;
	}

	public boolean isIsFolder() {
		return !this.leaf;
	}
	
	public Key getKey() {
		return key;
	}
	
	public DocNode(Long id,String lable, String parm, String value, boolean leaf) {
		this.title = lable;
		this.leaf = leaf;
		
		this.key=new Key();
		this.key.id=id;
		this.key.parm = parm;
		this.key.value = value;
	}
	
	public class Key{
		private Long id;
		public Long getId() {
			return id;
		}
		public String getParm() {
			return parm;
		}
		public String getValue() {
			return value;
		}
		private String parm;
		private String value;
	}
}
