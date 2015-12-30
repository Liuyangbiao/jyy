package com.nsc.dem.action.bean;

import java.sql.Timestamp;

/**
 * 日志备份bean
 * 
 * 
 * 
 * @author bs-team
 *
 * @date 2011-07-28 9:46:00 AM
 * @version
 */
public class LogFileBean {
	
	private Long id;
	private String target;//对象
	private String type;//日志分类
	
	public LogFileBean() {
		super();
	}
	private String content;//内容
	private String operate;//操作人
	private Timestamp operateTime;//操作时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
	
}
