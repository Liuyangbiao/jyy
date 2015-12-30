package com.nsc.dem.action.bean;

/*
 * 存放ID和一个Object[]
 * */
public class RowBean {
	// 行ID
	private String id;
	// 当前行的所有单元格
	private Object[] cell;

	private Object bean;

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}

	public Object getCell() {
		return cell == null ? bean : cell;
	}

	public void setCell(Object[] cell) {
		this.cell = cell;
	}

	public void setCell(Object bean) {
		this.bean = bean;
	}
}
