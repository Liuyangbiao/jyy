package com.nsc.dem.bean.system;

import java.sql.Timestamp;

import com.nsc.dem.bean.profile.TUser;

/**
 * TLogFile entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TLogFile implements java.io.Serializable {

	// Fields

	private Long id;
	private TUser TUserByDeleteOperator;
	private TUser TUserByBackupOperator;
	private String fileName;
	private String backupPath;
	private String type;
	private Timestamp backupTime;
	private Timestamp operateTime;

	// Constructors

	/** default constructor */
	public TLogFile() {
	}

	/** full constructor */
	public TLogFile(Long id, TUser TUserByDeleteOperator,
			TUser TUserByBackupOperator, String fileName, String backupPath,
			String type, Timestamp backupTime, Timestamp operateTime) {
		this.id = id;
		this.TUserByDeleteOperator = TUserByDeleteOperator;
		this.TUserByBackupOperator = TUserByBackupOperator;
		this.fileName = fileName;
		this.backupPath = backupPath;
		this.type = type;
		this.backupTime = backupTime;
		this.operateTime = operateTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TUser getTUserByDeleteOperator() {
		return this.TUserByDeleteOperator;
	}

	public void setTUserByDeleteOperator(TUser TUserByDeleteOperator) {
		this.TUserByDeleteOperator = TUserByDeleteOperator;
	}

	public TUser getTUserByBackupOperator() {
		return this.TUserByBackupOperator;
	}

	public void setTUserByBackupOperator(TUser TUserByBackupOperator) {
		this.TUserByBackupOperator = TUserByBackupOperator;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBackupPath() {
		return this.backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getBackupTime() {
		return this.backupTime;
	}

	public void setBackupTime(Timestamp backupTime) {
		this.backupTime = backupTime;
	}

	public Timestamp getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

}