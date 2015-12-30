package com.nsc.dem.webservice.client;


import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.directwebremoting.util.Logger;

import com.nsc.base.conf.Configurater;

public class RPCClient {
	private EndpointReference targetEPR;
	private RPCServiceClient serviceClient;
	private Options options;
	private  static RPCClient client = null;

	/**
	 * 
	 * @param url
	 */
	private RPCClient(String url) {
		try {
			serviceClient = new RPCServiceClient();
			options = serviceClient.getOptions();

			targetEPR = new EndpointReference(url);
			options.setTo(targetEPR);
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class).error("RPCClient初始化", e);
		}
	}

	/**
	 * 得到服务客户端
	 * 
	 * @return
	 */
	public static RPCClient getRPCClient(String nameSpace, String key) {
		String url = null;
		if (nameSpace == null) {
			url = Configurater.getInstance().getConfigValue(key);
		} else {
			url = Configurater.getInstance().getConfigValue(nameSpace, key);
		}
		client = new  RPCClient(url);
		return client;
	}

	/**
	 * 得到服务客户端
	 * 
	 * @return
	 */
	public static RPCClient getRPCClient(String url) {
		client = new  RPCClient(url);
		return client;
	}
	
	
	
	/**
	 * 执行方法
	 * @param methodName 方法名
	 * @param params     参数
	 * @param paramsType 参数类型
	 * @return
	 */
	public Object[] executeMethod(String methodName,Object[] params, Class[] paramsType){

		Object[] result = null;
		
		QName opAddEntry = new QName("http://www.example.org/EDMService/",methodName);

		try {
			result = serviceClient.invokeBlocking(opAddEntry, params, paramsType);
			return result;
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class).error("insertTFtp", e);
		}
		return result;
	}
	
	
	
	/**
	 * 获取会议结果
	 * 
	 * @param date
	 * @return
	 */
	public String getReviewMeetingResult(String date, String meettingNo) {
		Object[] result = null;
		Object[] args = new Object[] { meettingNo };
		Class<?>[] classes = new Class[] { String.class };
		String nameSpace = Configurater.getInstance().getConfigValue(
				"nameSpace");
		QName opAddEntry = new QName(nameSpace, "getReviewMeetingResult");
		try {
			serviceClient.getOptions().setTimeOutInMilliSeconds(1000 * 60 * 60);
			result = serviceClient.invokeBlocking(opAddEntry, args, classes);
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class)
					.error("getReviewMeetingResult", e);
			return null;
		}
		return result == null ? null : result[0].toString();
	}

	/**
	 * 传给远程接口未接收成功的文件
	 * 
	 * @param errorFilesXmlInfo
	 */
	public void updateErrorFilesFlag(String errorFilesXmlInfo) {
		Object[] args = new Object[] { errorFilesXmlInfo };
		Class<?>[] classes = new Class[] { String.class };
		String nameSpace = Configurater.getInstance().getConfigValue(
				"nameSpace");
		QName opAddEntry = new QName(nameSpace, "updateErrorFilesFlag");
		try {
			serviceClient.getOptions().setTimeOutInMilliSeconds(1000 * 60 * 60);
			serviceClient.invokeBlocking(opAddEntry, args, classes);
		} catch (Exception e) {
			Logger.getLogger(RPCClient.class).error("updateErrorFilesFlag", e);
		}
	}

	/**
	 * 全文检索
	 */
	public Object wholeSearch(String para, boolean withFragment, int start,
			int pageSize) {
		Object[] result = null;
		Object[] args = new Object[] { para, withFragment, start, pageSize };
		Class<?>[] classes = new Class[] { byte[].class };
		QName opAddEntry = new QName("http://www.example.org/EDMService/",
				"wholeSearch");

		try {
			result = serviceClient.invokeBlocking(opAddEntry, args, classes);
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class).error("wholeSearch", e);
		}
		return result[0];
	}

	public String remoteFtp(String unitCode, String ftpIp, String ftpName,
			String ftpPwd, String ftpPort, String webIp, String unitname) {

		Object[] result = null;
		Object[] args = new Object[] { unitCode, ftpIp, ftpName, ftpPwd,
				ftpPort, webIp, unitname };
		Class<?>[] classes = new Class[] { String.class };
		QName opAddEntry = new QName("http://www.example.org/EDMService/",
				"insertTFtp");

		try {
			result = serviceClient.invokeBlocking(opAddEntry, args, classes);
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class).error("insertTFtp", e);
		}
		return result[0].toString();
	}
	
	
	public boolean receiveFileList(String content, String unitId) {

		Object[] result = null;
		Object[] args = new Object[] {"12", unitId};
		Class<?>[] classes = new Class[] { Boolean.class };
		QName opAddEntry = new QName("http://www.example.org/EDMService/",
				"receiveFileList");

		try {
			serviceClient.getOptions().setTimeOutInMilliSeconds(1000 * 60 * 60);
			result = serviceClient.invokeBlocking(opAddEntry, args, classes);
		} catch (AxisFault e) {
			Logger.getLogger(RPCClient.class).error("receiveFileList", e);
		}
		return (Boolean)result[0];
	}

	
}
