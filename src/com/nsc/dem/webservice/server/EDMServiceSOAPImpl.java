package com.nsc.dem.webservice.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.MainProject;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.util.download.DownLoadFileList;
import com.nsc.dem.util.download.MD5;
import com.nsc.dem.util.filestore.FileStoreLocation;
import com.nsc.dem.util.task.CreateTaskListTask;
import com.nsc.dem.util.task.DataSynTask;
import com.nsc.dem.util.task.DocImagingTask;
import com.nsc.dem.util.task.DocIndexingTask;
import com.nsc.dem.util.task.DownloadIndexTask;
import com.nsc.dem.util.task.FileReceiveTask;
import com.nsc.dem.util.task.SendServersInfoTask;
import com.nsc.dem.util.task.SuggestionTask;
import com.nsc.dem.util.task.UploadIndexTask;
import com.nsc.dem.util.task.UploadServerInfoTask;
import com.nsc.dem.util.xml.FailFileIDXMLUtils;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.IntenterXmlUtils;
import com.nsc.dem.util.xml.StoreFtpNode;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.WSClient;
import com.nsc.dem.webservice.project.ProjectXmlParser;
import com.nsc.dem.webservice.project.ReviewPlanService;
import com.nsc.dem.webservice.search.ArchivesSearch;
import com.nsc.dem.webservice.system.ServersSet;
import com.nsc.dem.webservice.util.ApplicationContext;
import com.nsc.dem.webservice.util.WsUtils;

@javax.jws.WebService(endpointInterface = "com.nsc.dem.webservice.server.EDMService", targetNamespace = "http://www.example.org/EDMService/", serviceName = "EDMService", portName = "EDMServiceSOAP")
public class EDMServiceSOAPImpl implements EDMService {
	@Resource
	private WebServiceContext context;

	/**
	 * O 电子评审平台调用接口-发送工程信息
	 * 
	 * @param stream
	 *            工程信息XML格式
	 * @return boolean
	 * @throws Exception
	 */
	public boolean publishReviewPlanInfo(String stream) throws Exception {

		ProjectXmlParser parser = new ProjectXmlParser();
		Document document = parser.getDocument(stream);
		List<MainProject> list = parser.parseXml(document, false);
		if (list == null || list.isEmpty()) {
			return false;
		} 
		
		ReviewPlanService review = new ReviewPlanService();
		MainProject mainProject = list.get(0);

		boolean isDown = true; //是否下载
		
		//110KV及以下电压发送到省公司
	 	if(Configurater.getInstance().getConfigValue("system_type").equals("1")){
			//省公司评审
			if (8 == mainProject.gettProject().getApproveUnit().getProxyCode().length()){
				isDown = false;
				String code = mainProject.gettProject().getTUnitByOwnerUnitId().getCode();
				String url = IntenterXmlUtils.getWSURL(code);
				if(StringUtils.isNotBlank(url)){
					//发到省
					try {
						WSClient client = WSClient.getClient(url);
						client.getService().publishReviewPlanInfo(stream);
					} catch (Exception e) {
						Logger.getLogger(EDMServiceSOAPImpl.class).error(e.getMessage());
					}
				}
			}
		}   
		if(isDown){
			parser.parseXml(document, true);
			String localPath = review.downLoadXmlFileByFtp(mainProject);
			File xmlFile = new File(localPath);
			if (xmlFile.exists()) {
				return true;
			}else{
				return false;
			}
		}
		return true;
		
	}

	public byte[] getUserInfo(String param,String pwd) {
		if(!checking(pwd))
			return null;
		
		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		HttpSession session = (HttpSession) servletContext.getAttribute(param);
		TUser user = (TUser) (session.getAttribute("USER_KEY"));

		String loginId = user.getLoginId().split("_")[0];
		// 上次登录时间
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Element root = new Element("userInfo");
		Element node;
		// loginID
		node = new Element("loginId");
		node.setText(loginId);
		root.getChildren().add(node);
		// loginName
		node = new Element("loginName");
		node.setText(user.getName());
		root.getChildren().add(node);
		// 最后登录时间
		String lastLoginTime = timeFormat.format(user.getLastLoginTime());
		node = new Element("loastLoginTime");
		node.setText(lastLoginTime);
		root.getChildren().add(node);
		// 登录次数
		node = new Element("loginCount");
		node.setText(user.getLoginCount().toString());
		root.getChildren().add(node);
		// creator
		node = new Element("creator");
		node.setText(user.getCreator());
		root.getChildren().add(node);
		// 创建日期
		String createDate = timeFormat.format(user.getCreateDate());
		node = new Element("createDate");
		node.setText(createDate);
		root.getChildren().add(node);
		// 职务
		node = new Element("duty");
		node.setText(user.getDuty());
		root.getChildren().add(node);
		// 电话
		node = new Element("telePhone");
		node.setText(user.getTelephone());
		root.getChildren().add(node);
		// 密码
		node = new Element("password");
		node.setText(String.valueOf(user.getPassword()));
		root.getChildren().add(node);
		// 登录时间
		String loginTime = timeFormat.format(user.getLoginTime());
		node = new Element("loginTime");
		node.setText(loginTime);
		root.getChildren().add(node);
		if (user.getTUnit() != null) {
			TUnit unit = user.getTUnit();
			// 单位code
			node = new Element("unitCode");
			node.setText(unit.getCode());
			root.getChildren().add(node);
			// 单位名称
			node = new Element("unitName");
			node.setText(unit.getName());
			root.getChildren().add(node);

			// 单位简称
			node = new Element("unitShortName");
			node.setText(unit.getShortName());
			root.getChildren().add(node);

			// 单位地址
			node = new Element("unitAddress");
			node.setText(unit.getAddress());
			root.getChildren().add(node);

			// 联系电话
			node = new Element("unitTelephoe");
			node.setText(unit.getTelephone());
			root.getChildren().add(node);

			// 使用与否
			node = new Element("unitIsUsable");
			if (unit.getIsUsable() == true) {
				node.setText("1");
			} else {
				node.setText("0");
			}
			root.getChildren().add(node);

			// 单位类型
			node = new Element("unitType");
			node.setText(unit.getType());
			root.getChildren().add(node);

			// 代理编码
			node = new Element("unitProxyCode");
			node.setText(unit.getProxyCode());
			root.getChildren().add(node);
		}
		Document document = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		out.setFormat(format);
		String stream = out.outputString(document);

		String localhost = Configurater.getInstance().getConfigValue("site",
				"localhost");
		servletContext.setAttribute(localhost + "." + param, user);
		return stream.getBytes();
	}


	/**
	 * 跨域统计
	 * 
	 * @param 参数为String的xml
	 * @throws Exception
	 */
	public void crossdomainStatistics(String param, String pwd) throws Exception {
		if(!checking(pwd))
			return;
		
		IsearchesService service = (IsearchesService) ApplicationContext
				.getInstance().getApplictionContext()
				.getBean("searchesService");
		ArchivesSearch archives = new ArchivesSearch(service, WsUtils
				.getWsUser());
		Document document = WsUtils.getDocument(param);
		archives.siteCountAarchives(document);
	}


	/**
	 * 接收同步文件清单接口
	 * 
	 * @param dataHandler 附件
	 */
	public boolean receiveFileList(DataHandler dataHandler,String pwd) {
		if(!checking(pwd))
			return false;
		if(dataHandler == null){
			return false;
		}
		InputStream inStream = null;
		OutputStream outStream = null;
		
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String fileName = format.format(new Date()).toString(); 
			String path = XmlUtils.getPath() + Configurater.getInstance().getConfigValue("mytask");
			File file = new File(path, File.separator + fileName + ".xml");
			
			inStream = new BufferedInputStream(dataHandler.getInputStream(),1024);
			outStream = new BufferedOutputStream(new FileOutputStream(file));
			int bytesRead;
			byte[] buffer = new byte[1024];
			while((bytesRead = inStream.read(buffer, 0, buffer.length)) != -1){
				outStream.write(buffer,0, bytesRead);
			}
		}catch(Exception e){
			Logger.getLogger(EDMServiceSOAPImpl.class).error(e);
		}finally{
			try {
				if(inStream != null)
					outStream.close();
				if(outStream != null)
					outStream.close();
			} catch (IOException e) {
				Logger.getLogger(EDMServiceSOAPImpl.class).error(e);
			}
		}
		return true;
	}

	/**
	 * 保存省公司发送的服务服务器信息
	 * @param unitCode 单位ID
	 * @param unitname 单位名称
	 * @param ftpIp  IP
	 * @param ftpLonginName 用户名
	 * @param ftpLoginPass  密码
	 * @param ftpPort  端口号
	 * @param wsAdd    webService地址
	 * @return
	 */
	public boolean saveServersInfo(String unitCode, String unitname,
			String ftpIp, String ftpLonginName, String ftpLoginPass,
			String ftpPort, String wsAdd,String pwd) throws Exception {
		if(!checking(pwd))
			return false;
		
		 ServersSet ftpManager = new ServersSet();
		 ftpManager.saveServersInfo(unitCode, ftpIp, ftpLonginName, ftpLoginPass, ftpPort,
				wsAdd, unitname);
		return true;
	}

	
	/**
	 * 查询服务器信息
	 */
	public String findServersInfo(String pwd) throws Exception {
		if(!checking(pwd))
			return "非法访问!";
		ServersSet serversSet = new ServersSet();
		return serversSet.findAllServersInfo();
	}



	
	/**
	 * 接收国家电网发送的服务器信息
	 */
	public void receiveAllServersInfo(String ftpContent, String intenterContent,String pwd)
			throws Exception {
		if(!checking(pwd))
			return ;
		
		org.dom4j.Document documentTemp = null;
		if(StringUtils.isNotBlank(ftpContent)){
			//先删除本地的ftp.xml除根节点以外的其他所有节点
			XmlUtils util = XmlUtils.getInstance("ftp.xml");
			org.dom4j.Document document = util.getDocument();
			util.deleteAllNode(document);
			
			//解析国家电网发送的ftp.xml
			documentTemp = DocumentHelper.parseText(ftpContent);
			//设置本地的ftp节点为默认节点
			String code = Configurater.getInstance().getConfigValue("unitCode");
			org.dom4j.Element element = (org.dom4j.Element) documentTemp.selectSingleNode("//ftp[@code='"+code+"']");
			if(null != element){
				element.addAttribute("default","Y");
			}
			document = documentTemp;
			util.saveDocument(document);
		}
		
		if(StringUtils.isNotBlank(intenterContent)){
			//删除本地的intenterIp.xml除根节点以外的其他所有节点
			XmlUtils util = XmlUtils.getInstance("intenterIp.xml");
			org.dom4j.Document document = util.getDocument();
			util.deleteAllNode(document);
			documentTemp = DocumentHelper.parseText(intenterContent);
			document = documentTemp;
			util.saveDocument(document);
		}
	}
	
	
	
	/**
	 * 接收省公司或者发送的部分服务器信息
	 * @param ftpContent ftp.xml
	 * @param intenterContent intenterIp.mxl
	 */
	public void receivePartServersInfo(String ftpContent, String intenterContent,String pwd)
			throws Exception {
		if(!checking(pwd))
			return ;
		
		/**
		 * 如果是省公司或区域定时发送的PING结果，缓存到内存中
		 * 解决并发问题
		 * 等到国网向下全部发送时，再进行合并
		 */
		if(StringUtils.isNotBlank(ftpContent) && StringUtils.isBlank(intenterContent)){
			String key = FtpXmlUtils.getUnitCode(ftpContent);
			StoreFtpNode.createInstance().addFtpNode(key, ftpContent);
		//第一次配置
		}else if(StringUtils.isNotBlank(ftpContent) && StringUtils.isNotBlank(intenterContent)){
			//接收ftp.xml
			FtpXmlUtils.saveSynFtpNode(ftpContent);
			//接收网段
			IntenterXmlUtils.saveSynIntenterNode(intenterContent);
		}
	}

	/**
	 * 检查用户信息，并把用户需要下载的文件发送给调用者
	 */
	public String getDownLoadFile(String username, String password,
			String fileName,String pwd) throws Exception {
		if(!checking(pwd))
			return "非法访问";
		
		if(StringUtils.isBlank(username))
			return null;
		if(StringUtils.isBlank(password))
			return null;
		if(StringUtils.isBlank(fileName))
			return null;
		IuserService userService = (IuserService) ApplicationContext.getInstance()
		.getApplictionContext().getBean("userService");
		TUser user = (TUser) userService.EntityQuery(TUser.class, username);
		if(user != null){
			//密码正确
			if(password.equals(MD5.MD5Encode(user.getPassword().toString()))){
				return DownLoadFileList.createDownLoadFileList().getDownLoadList(fileName);
			}
		}
		return null;
	}

	
	public String doExceuteTask(String methodname,String pwd) throws Exception {
		if(!checking(pwd))
			return "非法访问";
		
		ServletContext sc = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);;
		if("A".equals(methodname.trim())){
			CreateTaskListTask task = new CreateTaskListTask("CreateTaskListTask",sc,0);
			task.doTask();
		} else if("B".equals(methodname.trim())){
			DataSynTask dsc = new DataSynTask("",sc,0);
			dsc.doTask();
		} else if("C".equals(methodname.trim())){
			UploadServerInfoTask sitt = new UploadServerInfoTask("UploadServerInfoTask",sc,0);
			sitt.doTask();
		} else if("D".equals(methodname.trim())){
			SendServersInfoTask ssitpt = new SendServersInfoTask("SendServersInfoTask",sc,0);
			ssitpt.doTask();
		} else if("E".equals(methodname.trim())){
			UploadIndexTask uit = new UploadIndexTask("UploadIndexTask",sc,0);
			uit.doTask();
		} else if("F".equals(methodname.trim())){
			DocIndexingTask dit = new DocIndexingTask("DocIndexingTask",sc,0);
			dit.doTask();
		} else if("G".equals(methodname.trim())){
			DownloadIndexTask dstt = new DownloadIndexTask("DownloadIndexTask",sc,0);
			dstt.doTask();
		}else if("H".equals(methodname.trim())){
			DocImagingTask task = new DocImagingTask("DocIndexingTask",sc,0);
			task.doTask();
		}else if("I".equals(methodname.trim())){
			FileReceiveTask task = new FileReceiveTask("FileReceiveTask",sc,0);
			task.doTask();
		}else if("J".equals(methodname.trim())){
			SuggestionTask task = new SuggestionTask("SuggestionTask",sc,0);
			task.doTask();
		}
		return null;
	}

	
	/**
	 * 接收同步成功和失败的文件ID
	 *    省公司发送的成功文件清单，改变国网数据库中相庆的字段即可
	 *    省公司/区域发送的失败的文件清单，下次同步时，重新按插入文件再次发送
	 *    --------------------------------------------------------------
	 *    区域发送的成功文件清单
	 *       改变国家网数据库中相应的字段
	 *       发送到相应的省公司
	 * @param successFileID 成功的文件ID
	 * @param failFileID    失败的文件ID
	 * @param unitCode      单位ID
	 * @return
	 */
	public boolean receiveSynFileID(String successFileID, String failFileID,String unitCode, String pwd) {
		if(!checking(pwd))
			return false;
		
		if(StringUtils.isBlank(unitCode))
			return false;
		
		Configurater config = Configurater.getInstance();
		String country = config.getConfigValue("country");
		IarchivesService service = (IarchivesService) ApplicationContext
		.getInstance()
		.getApplictionContext()
		.getBean("archivesService");
		
		String systemType = config.getConfigValue("system_type");
		String[] ids = successFileID.split(",");
		
		if("1".equals(systemType.trim())){
			if(ids.length > 0){
				//处理成功的ID
				for(String id : ids){
					TDoc doc = (TDoc) service.EntityQuery(TDoc.class, id);
					if(doc != null){
						String storeLocation = doc.getStoreLocation();
						if(FileStoreLocation.isExist(storeLocation, unitCode)){
							continue;
						}
						storeLocation += "#"+unitCode;
						doc.setStoreLocation(storeLocation);
						service.updateEntity(doc);
					}
				}
			}
			String to = config.getConfigValue("country");
		
			//保存接收或迁移失败的文件ID，下次发送同步清单时，再次发送
			if(StringUtils.isNotBlank(failFileID)){
				FailFileIDXMLUtils.saveFailFileID(failFileID, unitCode, to);
			}
			
			//如果区域，再转发给省公司
			Map<String, StringBuffer> map = new HashMap<String,StringBuffer>(); 
			if(unitCode.length() == 6){
				if(ids.length > 0){
					//把ID按工程分类，这样才能知道发送到那些省公司
					for(String id : ids){
						StringBuffer buffer = new StringBuffer();
						TProject project = service.getTProjectBydocId(id);
						if(project != null){
							String[] tounitIds = project.getGiveoutUnitId().split("#");
							for(String str : tounitIds){
								//除国网和自己不发送
								if(!country.trim().equals(str) && !unitCode.equals(str)){
									if(map.get(str) != null){
										buffer = map.get(str);
										buffer.append(","+id);
									}else{
										buffer.append(id);
									}
									map.put(str, buffer);
								}
							}
						}
					}
				}
			}
			for(Map.Entry<String, StringBuffer> entry: map.entrySet()){
				String context = entry.getValue().toString();
				String wsUtils = IntenterXmlUtils.getWSURL(entry.getKey());
				if(StringUtils.isNotBlank(wsUtils)){
					//查看XML文件中是否有需要发送的失败ID
					String failIDS = FailFileIDXMLUtils.getFailFileIDByCode(unitCode,entry.getKey());
					if(StringUtils.isNotBlank(failIDS)){
						context += "," + failIDS;
					}
					try{
						WSClient client = WSClient.getClient(wsUtils);
						client.getService().receiveSynFileID(context, null, unitCode, pwd);
					}catch(Exception e){
						Logger.getLogger(EDMServiceSOAPImpl.class).error(e);
						FailFileIDXMLUtils.saveFailFileID(failFileID, unitCode,entry.getKey());
					}
				}else{//保存起来，下次发送
					FailFileIDXMLUtils.saveFailFileID(context, unitCode,entry.getKey());
				}
			} 
		
		//省公司	
		}else if ("3".equals(systemType.trim())){
			if(ids.length > 0){
				for(String id : ids){
					TSynDoc doc = (TSynDoc) service.EntityQuery(TSynDoc.class, id);
					if(doc != null){
						String storeLocation = doc.getStoreLocation();
						//判断该属性中是否有该单位
						if(FileStoreLocation.isExist(storeLocation, unitCode)){
							continue;
						}
						storeLocation += "#"+unitCode;
						doc.setStoreLocation(storeLocation);
						service.updateEntity(doc);
					}else{//如果数据库还不存在该文件，先保存其存储位置
						doc = new TSynDoc();
						doc.setId(id);
						doc.setStoreLocation(unitCode);
						service.insertEntity(doc);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 用户验证
	 * @param pwd
	 * @return
	 */
	private boolean checking(String pwd){
		if(StringUtils.isBlank(pwd)){
			return false;
		}
		String password = Configurater.getInstance().getConfigValue("wspwd");
		if(pwd.equals(password))
			return true;
		return false;
	}

	
}