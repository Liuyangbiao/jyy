package com.nsc.dem.action.system;




import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.dom4j.Element;

import com.esri.aims.mtier.model.service.Services;
import com.nsc.base.conf.Configurater;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.ServiceSetBean;
import com.nsc.dem.bean.system.TServersInfo;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.IntenterXmlUtils;
import com.nsc.dem.webservice.client.WSClient;
import com.nsc.dem.webservice.system.ServersSet;


@SuppressWarnings("serial")
public class ServersSetAction extends BaseAction{
	private String returnValue = "success";
	private IService baseService;
	private IuserService userService;
	private String serverType;
	private Collection<ServiceSetBean> allInfos;

	public void setAllInfos(Collection<ServiceSetBean> allInfos) {
		this.allInfos = allInfos;
	}

	public Collection<ServiceSetBean> getAllInfos() {
		return allInfos;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public void setBaseService(IService baseService) {
		this.baseService = baseService;
	}

	public void setUserService(IuserService userService) {
		this.userService = userService;
	}

	/**
	 * 保存数据
	 * @return
	 * @throws Exception 
	 */
	public String save() throws Exception{
		
		HttpServletRequest request = super.getRequest();
		//webservices地址
		String wsAdd = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/";
		wsAdd += "EDMServiceSOAP?wsdl";
		Configurater config = Configurater.getInstance();
		String country = config.getConfigValue("country");
		boolean isDefault = false;
		
		//如果是国网
		if("country".equals(serverType)){
			ServersSet serversSet = new ServersSet();
			for(ServiceSetBean bean : allInfos){
				if(country.equals(bean.getId())){
					bean.setWsUrl(wsAdd);
					isDefault = true;
				}else{
					String webservice = bean.getWsUrl();
					if(webservice.indexOf("EDMServiceSOAP?wsdl") == -1){
						webservice += "/"+"EDMServiceSOAP?wsdl";
						bean.setWsUrl(webservice);
					}
				}
				serversSet.saveServersInfo(bean.getId(), bean.getFtpIP(), bean.getFtpLoginName(), bean.getFtpPwd(), bean.getFtpPort(),bean.getWsUrl(),bean.getName());
				FtpXmlUtils.createFtpNodesNoUnit(bean.getId(), bean.getName(), bean.getFtpIP(), bean.getFtpPort(), bean.getFtpLoginName(), bean.getFtpPwd(),bean.getProtocol(),bean.getContext(),isDefault);
				IntenterXmlUtils.createIntenterNode(bean.getId(),bean.getName(),bean.getWsUrl(),bean.getStartNetWay(), bean.getEndNetWay());
				isDefault = false;
			}
			
			List<TServersInfo> serversInfoList = userService.findAllServersInfo();
			//创建unit
			for(ServiceSetBean c : allInfos){
				Element element = FtpXmlUtils.createUnitNodeByLocal(c.getId(),serversInfoList);
				FtpXmlUtils.saveFtpFile(element);
			}
			returnValue = "success";
		}else{
			ServiceSetBean bean = (ServiceSetBean) allInfos.toArray()[0];
			FtpXmlUtils.createFtpNodes(bean.getId(), bean.getName(), bean.getFtpIP(),bean.getFtpPort(),
					bean.getFtpLoginName(), bean.getFtpPwd(), bean.getProtocol(), bean.getContext(), true);
			IntenterXmlUtils.createIntenterNode(bean.getId(),bean.getName(),wsAdd,bean.getStartNetWay(), bean.getEndNetWay());
			String ftpContent = FtpXmlUtils.getFtpNodeByUnitCode(bean.getId());
			String intenterContent = IntenterXmlUtils.getIntenterByCode(bean.getId());
			String wsUrl = config.getConfigValue("wsUrl");
			if(StringUtils.isNotBlank(wsUrl)){
				try{
					WSClient client = WSClient.getClient(wsUrl);
					String pwd = config.getConfigValue("wspwd");
					client.getService().saveServersInfo(bean.getId(), bean.getName(), bean.getFtpIP(), bean.getFtpLoginName(), bean.getFtpPwd(),bean.getFtpPort(), wsAdd, pwd);
					client.getService().receivePartServersInfo(ftpContent, intenterContent,pwd);
				}catch(Exception e){
					logger.getLogger(ServersSetAction.class).warn(e.getMessage());
					returnValue = "fail";
				}
			}
		}
		return returnValue;
	}

	/**
	 * 信息回显
	 * @return
	 */
	public String returnUI(){
		Configurater config = Configurater.getInstance();
		String systemType = config.getConfigValue("system_type");
		
		List<ServiceSetBean> infos = new ArrayList<ServiceSetBean>();
		
		if("1".equals(systemType)){
			serverType = "country" ;
		}else if ("3".equals(systemType)){
			serverType = "province" ;
		}
		
		String[] codes = config.getConfigValue("unitCode").split(","); 
		for(String code : codes){
			ServiceSetBean bean = new ServiceSetBean();
			bean.setId(code);
			TUnit unit = (TUnit) baseService.EntityQuery(TUnit.class, code);
			if(unit != null){
				bean.setName(unit.getName());
			}
			//从配置文件读取FTP信息信息
			bean = FtpXmlUtils.getInfoByCode(bean, code);
			//从配置文件读取网段信息
			bean = IntenterXmlUtils.getInfoByCode(bean,code);
			infos.add(bean);
		}
		super.getRequest().setAttribute("serverType", serverType);
		super.getRequest().setAttribute("infos", infos);
		return SUCCESS;
	}
	
	/**
	 * 得到所有省公司服务器配置信息
	 * @return
	 */
	public String getAllProvinceInfo(){
		List<ServiceSetBean> returnList = new ArrayList<ServiceSetBean>();
		returnList = FtpXmlUtils.getAllProvinceInfo();
		returnUI();
		super.getRequest().setAttribute("returnList", returnList);
		super.getRequest().setAttribute("serverType", "country");
		return SUCCESS;
	}

	public String getReturnValue() {
		return returnValue;
	}
}
