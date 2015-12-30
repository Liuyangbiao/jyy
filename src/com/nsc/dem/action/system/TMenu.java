package com.nsc.dem.action.system;

import java.util.List;
import java.util.Map;
@SuppressWarnings("unchecked")
public class TMenu implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	private String id;	
	private String lable;
	private String url;
	private String image;
	private boolean isLeaf=false;
	private boolean isPopUp;
	private boolean isUse;
	private Map map;
	


	private List list;
	
	public TMenu(){};
	
	public TMenu(String lable, String url) {
		super();
		this.lable = lable;
		this.url = url;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public boolean isUse() {
		return isUse;
	}
	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public boolean isPopUp() {
		return isPopUp;
	}
	public void setPopUp(boolean isPopUp) {
		this.isPopUp = isPopUp;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
}
