package com.nsc.dem.webservice.client;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.Component;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.webservice.client.edm.EDMService;
import com.nsc.dem.webservice.client.edm.EDMService_Service;
import com.nsc.dem.webservice.client.edm.Exception_Exception;

/**
 * 
 */
public final class CXFClient {
	private IuserService userService = null;
	private URL wsdlURL = null;
	private EDMService service;
	private static CXFClient client = null;

	private CXFClient(String url) throws MalformedURLException {
		wsdlURL = new URL(url);
		EDMService_Service edmService = new EDMService_Service(wsdlURL);
		service = edmService.getEDMServiceSOAP();
		userService = (IuserService) Component.getInstance("userService",
				Configurater.getInstance().getServletContext());
	}

	/**
	 * 得到服务客户端
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public static CXFClient getCXFClient(String nameSpace, String key,
			String url) throws MalformedURLException {

		if (url != null) {

		} else {
			if (nameSpace == null) {
				url = Configurater.getInstance().getConfigValue(key);
			} else {
				url = Configurater.getInstance().getConfigValue(nameSpace, key);
			}
		}
		
			client = new CXFClient(url);
		
		return client;
	}

	public static CXFClient getCXFClient(String url) throws MalformedURLException {
		if(StringUtils.isBlank(url))
				return null;
		
		client = new CXFClient(url);
		Logger.getLogger(CXFClient.class).info("初始化");
		return client;
	}
	
	public EDMService getService() {
		return service;
	}

	/**
	 * 根据传来的sessionId查询用户信息
	 * 
	 * @param sessionId
	 *            sessionId
	 * @return 用户信息
	 * @throws Exception
	 */
	public TUser getTUserBySessionId(String param, String romoteLogo)
			throws Exception {
		String pwd = Configurater.getInstance().getConfigValue("wspwd");
		byte[] result = service.getUserInfo(param,pwd);
		TUser user = new TUser();
		String str = new String(result);
		InputSource is = new InputSource(new StringReader(str));
		Document document = (new SAXBuilder()).build(is);
		Element root = document.getRootElement();
		List<Element> pros = root.getChildren();
		TUnit unit = new TUnit();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String loginId = "";
		for (Element p : pros) {
			String pName = p.getName();
			String content = p.getTextTrim();
			if ("unitCode".equals(pName)) {
				unit.setCode(content);
			} else if ("unitName".equals(pName)) {
				unit.setName(content);
			} else if ("unitShortName".equals(pName)) {
				unit.setShortName(content);
			} else if ("unitAddress".equals(pName)) {
				unit.setAddress(content);
			} else if ("unitTelephoe".equals(pName)) {
				unit.setTelephone(content);
			} else if("unitIsUsable".equals(pName)){
				if (content.equals("1")) {
					unit.setIsUsable(true);
				} else {
					unit.setIsUsable(false);
				}
			}else if ("unitType".equals(pName)) {
				unit.setType(content);
			} else if ("unitProxyCode".equals(pName)) {
				unit.setProxyCode(content);
			}
		}
		for (Element p : pros) {
			String pName = p.getName();
			String content = p.getTextTrim();
			 if ("loginName".equals(pName)) {
				user.setName(content);
			} else if ("loginId".equals(pName)) {
				loginId = content + "_"+unit.getCode();
				user.setLoginId(loginId);
			}  else if ("creator".equals(pName)) {
				user.setCreator(content);
			} else if ("createDate".equals(pName)) {
				Timestamp timestamp = new Timestamp(format.parse(content)
						.getTime());
				user.setCreateDate(timestamp);
			} else if ("duty".equals(pName)) {
				user.setDuty(content);
			} else if ("telePhone".equals(pName)) {
				user.setTelephone(content);
			} else if ("password".equals(pName)) {
				user.setPassword(Long.valueOf(content));
			} 
		}
		
		Configurater con = Configurater.getInstance();
		String romote_roleId = Configurater.getInstance().getConfigValue("romote_roleId");
		TRole role = (TRole) userService.EntityQuery(TRole.class, romote_roleId);
		
		TUser interfaceuser = (TUser)userService.EntityQuery(TUser.class,"interface"); 
		unit.setCreateDate(new Date());
		unit.setTUser(interfaceuser);
		
		user.setTRole(role);
		user.setTUnit(unit);

		// 要跳到哪里
		user.setRomoteLogo(romoteLogo);
		user.setIsLocal("R");

		return user;
	}
}
