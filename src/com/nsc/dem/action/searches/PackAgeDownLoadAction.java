package com.nsc.dem.action.searches;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.action.bean.DownFileBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.util.download.DownloadAddessUtils;
import com.nsc.dem.util.download.LoginLocationUtils;
import com.nsc.dem.util.download.MD5;
import com.nsc.dem.util.xml.DownLoadXmlUtils;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.IntenterXmlUtils;

/**
 * 打包下载Action
 * 
 * 
 */
public class PackAgeDownLoadAction extends DownloadAction {
	private static final long serialVersionUID = 5304532811867020924L;
	String[] eachCheckbox;
	String eachCheckboxVals; // 文档id#工程id#公司id#标题,文档id#工程id#公司id#标题
	private String[] name;
	private Collection<DownFileBean> downFileList;
	private String downType; //下载类型：isLocal：本地、 单位编码：非本地
	private String[] loginLocationInfo; //0：登录所在地ID，1：登录所在名称
	private String filename; //包名
	
	/**
	 * 获取下载地址
	 */
	public String getDownLoadAddress() {
		loginLocationInfo = new String[4];
		//下载地址
		allDocsObj = new ArrayList<DownFileBean>();
		List<DownFileBean> downloadFiles = getDownloadFileParameter();
		if(downloadFiles.size() <= 0)
			return null;
		String userCode = super.getLoginUser().getTUnit().getCode();
		
		//判断用户登录所在地
		String loginIp = super.getRequest().getRemoteAddr();
		String loginLocation = LoginLocationUtils.getIpLocal(loginIp);
		//如果不能判断用户区域，则视为用户在所属单位范围登录
		if(null != loginLocation){
			this.loginLocationInfo[0] = loginLocation;
			boolean isLocalLogin = LoginLocationUtils.isLocationLogin(loginLocation);
			String unitName = FtpXmlUtils.getUnitName(loginLocation);
			String message = "";
			if(!isLocalLogin){
				message = "推荐您使用"+unitName+"服务器下载!";
				this.loginLocationInfo[2] = "N"; //转跳
			}else{
				message="请使用本地下载！";
				this.loginLocationInfo[2] = "Y"; //本地下载
			}
			this.loginLocationInfo[1] = super.getLoginUser().getName() + 
			                "您好！您当前位置是：" + unitName +  "范围！" + message;
			this.loginLocationInfo[3] = unitName;
		}else{
			Configurater config = Configurater.getInstance();
			if("1".equals(config.getConfigValue("system_type"))){
				loginLocation = config.getConfigValue("country");
			}else{
				loginLocation = config.getConfigValue("unitCode");
			}
			this.loginLocationInfo[0] = loginLocation;
			this.loginLocationInfo[1] = super.getLoginUser().getName() + 
	                           "您好！您当前位置是：未知区域！请使用本地下载！";
			this.loginLocationInfo[2] = "Y"; //本地下载
			this.loginLocationInfo[3] = "";
		}
		
		//用户是否在本地登录
		boolean isLocalLogin = LoginLocationUtils.isLocationLogin(loginLocation);
		
		String systemType = Configurater.getInstance().getConfigValue("system_type");
		
		for(DownFileBean doc : downloadFiles){
			
			//查找本地数据库是否存在
			TDocProject tDocProject = searchesService.searchLocalDoc(doc.getDocid(),doc.getProjectId());
			if(tDocProject != null){
				TDoc tdoc = (TDoc) searchesService.EntityQuery(TDoc.class, doc.getDocid());
				String to_unit_ids = tdoc.getStoreLocation();
				//如果分发单位为空，则该文档的下载地址只有业主单位存在
				if (to_unit_ids == null) {
					to_unit_ids = doc.getCode();
				}
				    DownFileBean downDocBean = new DownFileBean();
					downDocBean.setDocid(doc.getDocid());
					downDocBean.setName(doc.getName());
					downDocBean.setPath(tDocProject.getId().getTDoc().getPath());
					downDocBean.setSuffix(tDocProject.getId().getTDoc().getSuffix());
					List<String[]> downAddress = DownloadAddessUtils.getDownloadAddress(
							loginLocation, to_unit_ids, isLocalLogin,userCode);
					downDocBean.setDownAddress(downAddress);
					allDocsObj.add(downDocBean);
					continue;
				}
			//省公司
			if("3".equals(systemType)){
				TSynDoc synDoc = searchesService.searchSynDoc(doc.getDocid(),doc.getProjectId());
				if(synDoc != null){
					String to_unit_ids = synDoc.getStoreLocation();
					if(to_unit_ids == null){
						to_unit_ids = doc.getCode();
					}
					DownFileBean downDocBean = new DownFileBean();
					downDocBean.setDocid(doc.getDocid());
					downDocBean.setName(doc.getName());
					downDocBean.setPath(synDoc.getPath());
					downDocBean.setSuffix(synDoc.getSuffix());
					List<String[]> downAddress = DownloadAddessUtils.getDownloadAddress(
							loginLocation, to_unit_ids, isLocalLogin,userCode);
					downDocBean.setDownAddress(downAddress);
					allDocsObj.add(downDocBean);
					continue;
				}
			}
			//如果国网本地不存在该文件，返回业主单位为下载地址
			if("1".equals(systemType)){
				DownFileBean downDocBean = new DownFileBean();
				downDocBean.setDocid(doc.getDocid());
				downDocBean.setName(doc.getName());
				downDocBean.setPath(doc.getPath());
				downDocBean.setSuffix(doc.getSuffix());
				downDocBean.setProjectId(doc.getProjectId());
				String name = FtpXmlUtils.getUnitName(doc.getCode());
				String[] address = new String[]{doc.getCode(),name,""};
				List<String[]> downAddress = new ArrayList<String[]>();
				downAddress.add(address);
				downDocBean.setDownAddress(downAddress);
				allDocsObj.add(downDocBean);
				continue;
			}
		}
		return SUCCESS;	
	}

	/**
	 * 打包下载	
	 * @throws Exception
	 */
	public void downloadFile() throws Exception{
		List<File> packageDownFileList = null; //打包下载列表
		SimpleDateFormat format = new SimpleDateFormat(
		                          "yyyy年MM月dd日HH时mm分ss秒");
		//非本地下载需要转跳
		if(!"isLocal".equals(downType)){
			super.getResponse().setCharacterEncoding("UTF-8");
			String downFileName = DownLoadXmlUtils.createDownloadList(downFileList);
			String appServerAdd = IntenterXmlUtils.getAppServerAdd(downType.trim());
			if(StringUtils.isBlank(downFileName))
				return;
			if(StringUtils.isBlank(appServerAdd))
				return;
			String userName = super.getLoginUser().getLoginId();
			//密码MD5加密
			String pwd = String.valueOf(super.getLoginUser().getPassword());
			pwd = MD5.MD5Encode(pwd);
			
			String unitCode = "";
			Configurater config = Configurater.getInstance();
			String action = config.getConfigValue("sendRedirect");
			String systemType = config.getConfigValue("system_type");
			if("1".equals(systemType.trim())){
				unitCode = config.getConfigValue("country");
			}else if("3".equals(systemType.trim())){
				unitCode = config.getConfigValue("unitCode");
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append(appServerAdd + "/")
			.append(action+"?")
			.append("username=")
			.append(userName+"&")
			.append("password=")
			.append(pwd+"&")
			.append("unitCode=")
			.append(unitCode+"&")
			.append("filename=")
			.append(downFileName+"&")
			.append("packageName=")   
			.append(filename);
			
			for(DownFileBean downFB : downFileList){
				TOperateLog tlog = new TOperateLog();
				tlog.setOperateTime(new Timestamp(System
						.currentTimeMillis()));
				tlog.setTarget(TDoc.class.getSimpleName());
				tlog.setTUser(super.getLoginUser());
				tlog.setType("L02");
				tlog.setContent(" 用户:" + super.getLoginUser().getName()+ "," + format.format(new Date()) + "转跳到:"+
						downType +" 下载文档："
						+ downFB.getName()+"下载地址："+downFB.getCode());
				baseService.insertEntity(tlog);
			}
			
			super.getResponse().sendRedirect(buffer.toString());
			return;
		}else{//本地下载
			String filename = URLDecoder.decode(this.filename, "UTF-8");
			Configurater config = Configurater.getInstance();
			packageDownFileList = new ArrayList<File>();
			// 以zip的格式下载
			super.getResponse().setContentType("application/zip");
			super.getResponse().setHeader("Content-Disposition",
														"attachment;filename=\"" + new String(filename.getBytes(), "ISO-8859-1") + ".rar\""); //下载名
			
			String unitCode = config.getConfigValue("unitCode");
			
			for(DownFileBean downFB : downFileList){
				String protocol = FtpXmlUtils.getProtocol(downFB.getCode());
				
				//如果下载地址等于本地单位编码，那么本地一定存在该文件
				if(downFB.getCode().equals(unitCode)){
					protocol = "FTP";
				}
				
				TOperateLog tlog = new TOperateLog();
				tlog.setOperateTime(new Timestamp(System
						.currentTimeMillis()));
				tlog.setTarget(TDoc.class.getSimpleName());
				tlog.setTUser(super.getLoginUser());
				tlog.setType("L02");
				tlog.setContent("公司："+super.getLoginUser().getTUnit().getName()+" 用户:" + super.getLoginUser().getName()
						+ "," + format.format(new Date()) + "下载文档："
						+ downFB.getName()+"下载地址："+downFB.getCode());
				
				baseService.insertEntity(tlog);
				
				//其他文件根据使用协议下载
				if("FTP".equals(protocol)){
					try{
						ftpDownload(packageDownFileList, downFB);
					}catch(Exception e){
						logger.getLogger(PackAgeDownLoadAction.class).warn(e.getMessage());
					}	
					continue;
				}else{
					try{
						httpDownload(packageDownFileList, downFB);
					}catch(Exception e){
						logger.getLogger(PackAgeDownLoadAction.class).warn(e.getMessage());
					}	
				}
			}
			
			//把文件打包返回给客户端
			FileUtil.getPackAgeDownLoad(packageDownFileList.toArray(new File[0]),
					super.getResponse());
		}
	}
		

	/**
	 * 封装下载文件参数
	 * 文件ID<>工程ID<>业主单位<>文件<>下载路径<>后缀名:.....
	 *  分隔符只能使用WINDOWS禁用的文件名特别符号
	 * @return
	 */
	private List<DownFileBean> getDownloadFileParameter(){
		// 下载参数集合
		List<DownFileBean> downParamList = new ArrayList<DownFileBean>();
		if (eachCheckboxVals != null && eachCheckboxVals.trim().length() > 0) {
			String[] arry = eachCheckboxVals.split(":");
			if (arry.length > 0) {
				for (String str : arry) {
					if (str.indexOf("<>") != -1) {
						String[] param = str.split("<>");
						DownFileBean doc = new DownFileBean();
						doc.setDocid(param[0]);
						doc.setProjectId(param[1]);
						doc.setCode(param[2]);
						doc.setName(param[3].replaceAll("<[.[^<]]*>",""));
 						doc.setPath(param[4]!= null?param[4]:null);
						doc.setSuffix(param[5]!= null?param[5]:null);
						downParamList.add(doc);
					}
				} 
			}
		}
		return downParamList;
	}

	
	
	
	public String getDownType() {
		return downType;
	}

	public void setDownType(String downType) {
		this.downType = downType;
	}

	public Collection<DownFileBean> getDownFileList() {
		return downFileList;
	}

	public void setDownFileList(Collection<DownFileBean> downFileList) {
		this.downFileList = downFileList;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public void setEachCheckboxVals(String eachCheckboxVals) {
		this.eachCheckboxVals = eachCheckboxVals;
	}

	
	private List<DownFileBean> allDocsObj;

	public void setAllDocsObj(List<DownFileBean> allDocsObj) {
		this.allDocsObj = allDocsObj;
	}

	@JSON
	public List<DownFileBean> getAllDocsObj() {
		return allDocsObj;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setEachCheckbox(String[] eachCheckbox) {
		this.eachCheckbox = eachCheckbox;
	}

	IService baseService;

	private IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public void setBaseService(IService baseService) {
		this.baseService = baseService;
	}

	private String returnValue;

	public String getReturnValue() {
		return returnValue;
	}
	
	@JSON
	public String[] getLoginLocationInfo() {
		return loginLocationInfo;
	}
}
