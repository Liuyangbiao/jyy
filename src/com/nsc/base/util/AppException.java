package com.nsc.base.util;


/**
 * 应用异常类
 * 
 * 此类用于保存在业务逻辑处理中发生的所有异常。此类扩展了RuntimeException，并且增加了系统自定义的异常内容。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:28:45 AM
 * @version
 */
public class AppException extends RuntimeException {	
	static final long serialVersionUID = -7034897190745791228L;
	
	private String sysMId;
	private String userMId;
	private String originalM;
	private Object[] arguments;
	
	/**
	 * 构造函数
	 * @param ex 异常
	 * @param sid 系统消息ID
	 * @param uid 用户消息ID
	 * @param arguments 参数信息
	 */
	public AppException(Exception ex,String sid,String uid,Object[] arguments){
		super(ex);
		sysMId=sid;
		userMId=uid;
		originalM=ex.getMessage();
		this.arguments=arguments;
	}
	
	/**
	 * 构造函数
	 * @param msg 错误消息
	 * @param sid 系统消息ID
	 * @param uid 用户消息ID
	 * @param arguments 参数信息
	 */
	public AppException(String msg,String sid,String uid,Object[] arguments){
		super();
		sysMId=sid;
		userMId=uid;
		originalM=msg;
		this.arguments=arguments;
	}

	/**
	 * 取得系统消息标识
	 * @return 系统消息标识
	 */
	public String getSysMId() {
		return sysMId;
	}

	/**
	 * 取得用户消息标识
	 * @return 用户消息标识
	 */
	public String getUserMId() {
		return userMId;
	}

	/**
	 * 取得原始消息
	 * @return 原始消息
	 */
	public String getOriginalM() {
		return originalM;
	}

	/**
	 * 取得参数信息
	 * @return 参数数组
	 */
	public Object[] getArguments() {
		return arguments;
	}
}
