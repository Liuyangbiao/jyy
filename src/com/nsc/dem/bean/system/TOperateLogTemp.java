package com.nsc.dem.bean.system;

import java.util.Date;

/**
 * TOperateLogTemp entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TOperateLogTemp implements java.io.Serializable {

	// Fields

	private Long id;
	private String target;
	private String type;
	private String content;
	private String operator;
	private Date operateTime;

	// Constructors

	/** default constructor */
	public TOperateLogTemp() {
	}

	/** full constructor */
	public TOperateLogTemp(String target, String type, String content,
			String operator, Date operateTime) {
		this.target = target;
		this.type = type;
		this.content = content;
		this.operator = operator;
		this.operateTime = operateTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

}