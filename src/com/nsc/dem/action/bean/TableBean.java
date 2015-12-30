package com.nsc.dem.action.bean;

import java.util.List;

/*
 * page类
 * */
public class TableBean {
	//包含 实际数据的数组
	private List<RowBean> rows;

	public List<RowBean> getRows() {
		return rows;
	}

	public void setRows(List<RowBean> rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}
	//当前面
	private int page;
	//总页数
	private int total;
	//查询出的记录数
	private int records;

}
