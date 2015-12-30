package com.nsc.dem.bean.system;

import java.sql.Timestamp;

import com.nsc.dem.bean.profile.TUser;

/**
 * TOperateLog entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial") 
public class TOperateLog implements java.io.Serializable {

	// Fields

	private Long id;
	private TUser TUser;
	private String target;
	private String type;
	private String content;
	private Timestamp operateTime;
	


	// Constructors

	/** default constructor */
	public TOperateLog() {
	}

	/** full constructor */
	public TOperateLog(Long id, TUser TUser, String target, String type,
			String content, Timestamp operateTime) {
		this.id = id;
		this.TUser = TUser;
		this.target = target;
		this.type = type;
		this.content = content;
		this.operateTime = operateTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
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

	public Timestamp getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}



}