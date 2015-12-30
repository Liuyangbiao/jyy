package com.nsc.dem.util.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.util.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.action.bean.ServiceSetBean;
import com.nsc.dem.bean.system.TServersInfo;
import com.nsc.dem.webservice.client.WSClient;

public class FtpXmlUtils {
	
	private static XmlUtils util = XmlUtils.getInstance("ftp.xml");
	
	/**
	 * 创建带unit的FTP节点
	 * @param name 单位名称
	 * @param hostName  FTP IP
	 * @param port   FTP 端口号
	 * @param username  FTP 用户名
	 * @param password  FTP 密码
	 * @throws Exception 
	 * @throws IOException
	 */
	public static void createFtpNodes(String unitCode, String name, String hostName, String port, String username, String password,boolean isDafault) {
		Document document = util.getDocument();
		String ftpUnit = "//ftp[@code=" + unitCode + "]";
		Element ftpNode = (Element) document.selectSingleNode(ftpUnit);
		
		//如果该节点存在，删除该节点
		if (ftpNode != null) {
			document.getRootElement().remove(ftpNode.detach());
		}
		
		Element ftpElement = DocumentHelper.createElement("ftp");
		ftpElement.addAttribute("code", unitCode);
		ftpElement.addAttribute("name", name);
		ftpElement.addAttribute("hostname", hostName);
		ftpElement.addAttribute("port", port);
		ftpElement.addAttribute("username", username);
		ftpElement.addAttribute("password", password);
		if(isDafault)
			ftpElement.addAttribute("default", "Y");
		//创建unit
		ftpElement = createUnitNodeByWs(ftpElement);
		document.getRootElement().add(ftpElement);
		util.saveDocument(document);
	}
	/**
	 * 创建带unit的FTP节点
	 * @param name 单位名称
	 * @param hostName  FTP IP
	 * @param port   FTP 端口号
	 * @param username  FTP 用户名
	 * @param password  FTP 密码
	 * @throws Exception 
	 * @throws IOException
	 */
	public static void createFtpNodes(String unitCode, String name, String hostName, String port, String username, String password,String agreement,String context ,boolean isDafault) {
		Document document = util.getDocument();
		String ftpUnit = "//ftp[@code=" + unitCode + "]";
		Element ftpNode = (Element) document.selectSingleNode(ftpUnit);
		//unitCode, unitname, ftpIp, ftpPort, ftpName, ftpPwd,proAgreement,proContext,true
		//如果该节点存在，删除该节点
		if (ftpNode != null) {
			document.getRootElement().remove(ftpNode.detach());
		}
		
		Element ftpElement = DocumentHelper.createElement("ftp");
		ftpElement.addAttribute("code", unitCode);
		ftpElement.addAttribute("name", name);
		ftpElement.addAttribute("hostname", hostName);
		ftpElement.addAttribute("port", port);
		if(username!=null)
		ftpElement.addAttribute("username", username);
		if(password!=null)
		ftpElement.addAttribute("password", password);
		ftpElement.addAttribute("agreement", agreement);
		ftpElement.addAttribute("context", context);
		if(isDafault)
			ftpElement.addAttribute("default", "Y");
		//创建unit
		ftpElement = createUnitNodeByWs(ftpElement);
		document.getRootElement().add(ftpElement);
		util.saveDocument(document);
	}
	/**
	 * 创建不带unit节点的ftp节点
	 * @param unitCode
	 * @param name
	 * @param hostName
	 * @param port
	 * @param username
	 * @param password
	 * @param isDafault
	 */
	public static void createFtpNodesNoUnit(String unitCode, String name, String hostName, String port, String username, String password,boolean isDafault) {
		Document document = util.getDocument();
		String ftpUnit = "//ftp[@code=" + unitCode + "]";
		Element ftpNode = (Element) document.selectSingleNode(ftpUnit);
		
		//如果该节点存在，删除该节点
		if (ftpNode != null) {
			document.getRootElement().remove(ftpNode.detach());
		}
		
		Element ftpElement = DocumentHelper.createElement("ftp");
		ftpElement.addAttribute("code", unitCode);
		ftpElement.addAttribute("name", name);
		ftpElement.addAttribute("hostname", hostName);
		ftpElement.addAttribute("port", port);
		ftpElement.addAttribute("username", username);
		ftpElement.addAttribute("password", password);
		if(isDafault)
			ftpElement.addAttribute("default", "Y");
		document.getRootElement().add(ftpElement);
		util.saveDocument(document);
	}

	/**
	 * 创建不带unit节点的ftp节点
	 * @param unitCode
	 * @param name
	 * @param hostName
	 * @param port
	 * @param username
	 * @param password
	 * @param isDafault
	 */
	public static void createFtpNodesNoUnit(String unitCode, String name, String hostName, String port, String username, String password,String argeement,String context , boolean isDafault) {
		Document document = util.getDocument();
		String ftpUnit = "//ftp[@code=" + unitCode + "]";
		Element ftpNode = (Element) document.selectSingleNode(ftpUnit);
		
		//如果该节点存在，删除该节点
		if (ftpNode != null) {
			document.getRootElement().remove(ftpNode.detach());
		}
		
		Element ftpElement = DocumentHelper.createElement("ftp");
		ftpElement.addAttribute("code", unitCode);
		ftpElement.addAttribute("name", name);
		ftpElement.addAttribute("hostname", hostName);
		ftpElement.addAttribute("port", port);
		ftpElement.addAttribute("username", username);
		ftpElement.addAttribute("password", password);
		ftpElement.addAttribute("agreement", argeement);
		ftpElement.addAttribute("context", context);
		if(isDafault)
			ftpElement.addAttribute("default", "Y");
		document.getRootElement().add(ftpElement);
		util.saveDocument(document);
	}
	
	/**
	 * 根据ftp节点获取内容
	 * @param unitCode
	 * @throws Exception
	 */
	public static String getFtpNodeByUnitCode(String unitCode) throws Exception {
		if(StringUtils.isBlank(unitCode))
			return null;
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
		if(null != element){
			//去除默认值
			element.addAttribute("default", null);
			//创建一个临时document
			Document documentTemp = DocumentHelper.createDocument();
			documentTemp.add(DocumentHelper.createElement("ftps"));
			documentTemp.getRootElement().add(element.detach());
			return XmlUtils.document2String(documentTemp);
		}
		return null;
	}
	
	/**
	 * 创建unit
	 *   调用webService获取其他服务器信息
	 * @param ftpNode
	 */
	@SuppressWarnings("unchecked")
	public static Element createUnitNodeByWs(Element ftpNode){
		if(null == ftpNode){
			return ftpNode;
		}
		//先删除ftpNode下的所有unit节点
		List<Element> elementList = ftpNode.elements();
		for(Element element : elementList){
			ftpNode.remove(element);
		}
		Configurater config = Configurater.getInstance();
		//从国家电网获取所有公司的FTP IP地址及其他数据
		String url = config.getConfigValue("wsUrl");
		String pwd = config.getConfigValue("wspwd");
		//如果国家电网的WS不能连接，不用执行以下代码
		if(StringUtils.isNotBlank(url)){
			String serversInfo = null;
			try{
				WSClient client = WSClient.getClient(url);
				serversInfo = client.getService().findServersInfo(pwd);
			}catch(Exception e){
				Logger.getLogger(FtpXmlUtils.class).warn("国家电网webservice调用失败");
			}
			if(StringUtils.isNotBlank(serversInfo)){
				String[] servers = serversInfo.split("#");
				for (String server : servers) {
					//分隔收到的信息
					String[] ftpArray = server.split(",");
					String unitId = ftpArray[0];
					String unitName = ftpArray[1];
					String  ftpIp= ftpArray[2];
					String ftpPort = ftpArray[3];
					//创建unit节点
					ftpNode = createUnitNode(ftpNode, unitId, unitName, ftpIp, ftpPort);
				}
			}
		}
		return ftpNode;
	}
	

	/**
	 * 创建unit节点
	 *    通过调用本地数据库获服务器信息创建
	 * @param ftpNode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Element createUnitNodeByLocal(Element ftpNode,List<TServersInfo> serversInfoList){
		if(null != ftpNode){
			//先删除ftpNode下的所有unit节点
			List<Element> elementList = ftpNode.elements();
			for(Element element : elementList){
				ftpNode.remove(element);
			}
			for(TServersInfo server : serversInfoList){
				String unitId = server.getUnitCode();
				String unitName = server.getUnitName();
				String ftpIp = server.getFtpIp();
				String ftpPort = server.getFtpPort();
				createUnitNode(ftpNode, unitId, unitName, ftpIp, ftpPort);
			}
		}
		return ftpNode;
	}
	
	
	/**
	 * 创建unit节点
	 *    通过调用本地数据库获服务器信息创建
	 * @param ftpNode
	 * @return
	 */
	public static Element createUnitNodeByLocal(String unitCode, List<TServersInfo> serversInfoList){
		Document document = util.getDocument();
		Element ftpNode = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
		ftpNode = createUnitNodeByLocal(ftpNode,serversInfoList);
		return ftpNode;
	}
	
	
	/**
	 * 创建unit节点
	 * @param ftpNode
	 * @param unitId
	 * @param unitName
	 * @param ftpIp
	 * @param ftpPort
	 */
	private static Element createUnitNode(Element ftpNode, String unitId,
			String unitName, String ftpIp, String ftpPort) {
		Element unitElement = DocumentHelper.createElement("unit");
		unitElement.addAttribute("code", unitId);
		unitElement.addAttribute("name", unitName);
		unitElement.addAttribute("value", Ping.getTime(ftpIp,Integer.parseInt(ftpPort)));
		ftpNode.add(unitElement);
		return ftpNode;
	}

	/**
	 * 保存
	 * @param element
	 */
	public static void saveFtpFile(Element element) {
		if(null != element){
			Document document = element.getDocument();
			util.saveDocument(document);
		}
	}
	
	/**
	 * 获取ftp信息
	 * 0 : FTP地址
	 * 1 ：FTP 端口号
	 * 2 ：FTP 登录名
	 * 3 ：FTP 登录密码
	 * @param unitCode
	 * @return
	 */
	public static  String[] getFTPInfo(String unitCode){
		if(StringUtils.isNotBlank(unitCode)){
			Document document = util.getDocument();
			Element element = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
			if(null != element){
				String[] str = new String[4];
				str[0] = element.attributeValue("hostname");
				str[1] = element.attributeValue("port");
				str[2] = element.attributeValue("username");
				str[3] = element.attributeValue("password");
				return str;
			}
		}
		return null;
	}
	
	
	
	/**
	 * 根据ID获取单位名称
	 * @param unitCode
	 * @return
	 */
	public static String getUnitName(String unitCode){
		Document document = util.getDocument();
		if(unitCode.length() == 2)
			unitCode = "0801" ;
		Element element = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
		if(null != element){
			return element.attributeValue("name");
		}
		return null;
	}
	
	/**
	 * 获取协议
	 * @param unitCode
	 * @return
	 */
	public static String getProtocol(String unitCode){
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
		if(element != null){
			return element.attributeValue("agreement");
		}
		return null;
	}
	
	/**
	 *  获取文件服务器地址
	 *  @param unitCode 
	 */
	public static String getFileServerAdd(String unitCode){
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//ftp[@code='"+unitCode+"']");
		if(element != null){
			String protocol = element.attributeValue("agreement");
			String hostName = element.attributeValue("hostname");
			String port = element.attributeValue("port");
			String context = element.attributeValue("context");
			return protocol + "://" + hostName + ":" + port + "/" + context + "/";
		}
		return null;
	}
	
	
	/**
	 * 获取所有省的信息
	 */
	@SuppressWarnings("unchecked")
	public static List<ServiceSetBean> getAllProvinceInfo(){
		Document document = util.getDocument();
		List<Element> elements = document.selectNodes("//ftp");
		List<ServiceSetBean> allProvinces = new ArrayList<ServiceSetBean>();
		for(Element ele : elements){
			String code = ele.attributeValue("code");
			if(code.length() == 8 ){
				ServiceSetBean bean = new ServiceSetBean();
				bean.setName(ele.attributeValue("name"));//省公司名称
				bean.setProtocol(ele.attributeValue("agreement"));//协议
				bean.setFtpPort(ele.attributeValue("port"));//端口号
				bean.setFtpIP(ele.attributeValue("hostname"));//IP地址
				bean.setContext(ele.attributeValue("context"));//上下文
				bean.setFtpLoginName(ele.attributeValue("username"));//FTP用户名
				bean.setFtpPwd(ele.attributeValue("password"));//FTP密码
				bean.setEndNetWay(IntenterXmlUtils.getInfoByCode(code, "end"));//结束地址 
				bean.setStartNetWay(IntenterXmlUtils.getInfoByCode(code, "start"));//开始地址
				bean.setWsUrl(IntenterXmlUtils.getInfoByCode(code, "appIp"));//webservice地址
			allProvinces.add(bean);
			}
		}
		return allProvinces;
	}
	
	
	/**
	 * 保存省公司或者区域发送到国网的ftp信息
	 * @param context ftp节点信息
	 * @return
	 */
	public static boolean saveSynFtpNode(String context){
		if(StringUtils.isBlank(context))
			return false;
		try{
			Document document = DocumentHelper.parseText(context);
			Element ftpNode = (Element) document.selectSingleNode("//ftp");
			String code = ftpNode.attributeValue("code");
			// 本地的document
			Document localDocument = util.getDocument();
			org.dom4j.Element ftpNodeTemp = (Element) localDocument.selectSingleNode("//ftp[@code='" +code+"']");
			if (ftpNodeTemp != null) {
				localDocument.getRootElement().remove(ftpNodeTemp.detach());
			}
			localDocument.getRootElement().add(ftpNode.detach());
			util.saveDocument(localDocument);
			return true;
		}catch (Exception e){
			Logger.getLogger(FtpXmlUtils.class).error(e.getMessage());
			return false;
		}
	}
	

	/**
	 * 从省公司或者区域发送的ftp信息中获取
	 *  公司编码
	 */
	public static String getUnitCode(String context){
		try{
			Document document = DocumentHelper.parseText(context);
			Element ftpNode = (Element) document.selectSingleNode("//ftp");
			String code = ftpNode.attributeValue("code");
			return code;
		}catch(Exception e){
			Logger.getLogger(FtpXmlUtils.class).error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 从省公司或者区域发送的ftp信息中获取
	 *  节点信息
	 */
	public static Element getSynFtpNode(String context){
		try{
			Document document = DocumentHelper.parseText(context);
			Element ftpNode = (Element) document.selectSingleNode("//ftp");
			return ftpNode;
		}catch(Exception e){
			Logger.getLogger(FtpXmlUtils.class).error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 返回code
	 * @param hostName
	 * @return
	 */
	public static String getUnitCodeByHostName(String hostName){
		Document document = util.getDocument();
		Element element = (Element) document.selectSingleNode("//ftp[@hostname='"+hostName+"']");
		if(element != null){
			return element.attributeValue("code");
		}
		return null;
	}
	
	
	public static ServiceSetBean getInfoByCode(ServiceSetBean bean, String code) {
		
		Document doc = util.getDocument();
		Element elm = (Element) doc.selectSingleNode("//ftp[@code='"+code+"']");
		if(null != elm){
			bean.setProtocol(elm.attributeValue("agreement"));
			bean.setContext(elm.attributeValue("context"));
			bean.setFtpPort(elm.attributeValue("port"));
			bean.setFtpIP(elm.attributeValue("hostname"));
			bean.setFtpLoginName(elm.attributeValue("username"));
			bean.setFtpPwd(elm.attributeValue("password"));
		}
		return bean;
	}
	
}
