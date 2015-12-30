package com.nsc.dem.action.searches;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.action.bean.DownFileBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.util.download.DownloadAddessUtils;
import com.nsc.dem.util.download.LoginLocationUtils;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 在线查看Action
 *
 */
public class OnLineSeeAction extends DownloadAction implements ModelDriven<DownFileBean> {

	private static final long serialVersionUID = 7420046762889339041L;
	private DownFileBean bean = new DownFileBean();
	
	private IsearchesService searchesService;
	
	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}
	

	/**
	 * 在线查看 单个文件下载
	 * @throws Exception
	 */
	public void getDownloadFile() throws Exception {
		
		String userCode = super.getLoginUser().getTUnit().getCode();
		
		//判断用户登录所在地
		String loginIp = super.getRequest().getRemoteAddr();
		String loginLocation = LoginLocationUtils.getIpLocal(loginIp);
		
		Configurater config = Configurater.getInstance();
		//如果不能判断用户区域，则视为用户在所属单位范围登录
		if(StringUtils.isBlank(loginLocation)){
			if("1".equals(config.getConfigValue("system_type"))){
				loginLocation = config.getConfigValue("country");
			}else{
				loginLocation = config.getConfigValue("unitCode");
			}
		} 
		
		//用户是否在本地登录
		boolean isLocalLogin = LoginLocationUtils.isLocationLogin(loginLocation);
		
		String systemType = config.getConfigValue("system_type");
		
		
		//查找本地数据库是否存在该文件
		TDocProject tDocProject = searchesService.searchLocalDoc(bean.getDocid(),bean.getProjectId());
		String toUintIds = "";
		if(tDocProject != null){
			TDoc tdoc = (TDoc) searchesService.EntityQuery(TDoc.class, bean.getDocid());
			toUintIds = tdoc.getStoreLocation();
		}
		
		//查找省公司同步数据库是存在
		if("3".equals(systemType.trim()) && tDocProject == null){
			TSynDoc synDoc = searchesService.searchSynDoc(bean.getDocid(),bean.getProjectId());
			if(synDoc != null){
				toUintIds = synDoc.getStoreLocation();
			}
		}
		
	
		//如果文件在syn和local的数据库没有，则在业主单位下载
		if(StringUtils.isBlank(toUintIds)){
			toUintIds = bean.getCode();
		}
		
		List<String[]> downAdd = DownloadAddessUtils.getDownloadAddress(loginLocation, 
				          toUintIds, isLocalLogin, userCode);
		if(downAdd == null || downAdd.isEmpty()){
			return ;
		}
		String address = "";
		if(downAdd != null && !downAdd.isEmpty()){
			if(downAdd.get(0)[0] != null){
			    address = downAdd.get(0)[0];
			    bean.setCode(address);
			}else{
				address = bean.getCode();
			}
		}
		
		String protocol = FtpXmlUtils.getProtocol(address);
		if("1".equals(systemType)){
			if(address.equals(config.getConfigValue("country"))){
				protocol = "FTP";
			}
		}else if("3".equals(systemType)){
			if(address.equals(config.getConfigValue("unitCode"))){
				protocol = "FTP";
			}
		}
		
		List<File> packageDownFileList = new ArrayList<File>();
		
		if("FTP".equals(protocol)){
			try{
				ftpDownload(packageDownFileList,bean);
			}catch(Exception e){
				logger.getLogger(OnLineSeeAction.class).warn(e.getMessage());
			}
		}else{
			try{
				//DOTA 如果是文件的HTTP方式下载，直接转跳
				httpDownload(packageDownFileList, bean);
			}catch(Exception e){
				logger.getLogger(OnLineSeeAction.class).warn(e.getMessage());
			}
		}
		
		//如果文件没有下载成功
		if(packageDownFileList == null || packageDownFileList.isEmpty()){
			return;
		}
		
		String dest = "";
		try{
			String mimeType = config.getConfigValue("mime", bean.getSuffix().toLowerCase());
			if (mimeType == null) {
				mimeType = config.getConfigValue("mime", "*");
			}
			dest = packageDownFileList.get(0).getAbsolutePath();
			
			//保存日志，不能随意修改，影响首页的在线查看
			SimpleDateFormat format = new SimpleDateFormat(
			"yyyy年MM月dd日 HH时mm分ss秒");

			TOperateLog tlog = new TOperateLog();
			tlog.setOperateTime(new Timestamp(System.currentTimeMillis()));
			tlog.setTarget(TDoc.class.getSimpleName());
			tlog.setTUser(super.getLoginUser());
			tlog.setType("L01");
			//String docName = bean.getName()+"."+bean.getSuffix();
			
			tlog.setContent("用户:" +super.getLoginUser().getName() + ","
					+ format.format(new Date()) + ";在线查看文档 ,文档ID:"+bean.getDocid()
					+";工程:"+bean.getProjectId()+";业主单位:"+bean.getCode()+";文件名:"
					+bean.getName()+";路径:"+bean.getPath()+";后缀:"+bean.getSuffix());
			
			
			baseService.insertEntity(tlog);
			
			FileUtil.download(bean.getName() + "." + bean.getSuffix(),dest, mimeType,!bean.isIsonline(),super.getRequest(),super.getResponse());
		}finally{
			FileUtil.deleteFile(dest);
		}
	}
	
	
	public DownFileBean getModel() {
		return bean;
	}
}
