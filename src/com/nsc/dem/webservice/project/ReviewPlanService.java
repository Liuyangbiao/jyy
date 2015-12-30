package com.nsc.dem.webservice.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpStatus;

import booway.jssys.encrypt.EdrpFileEncrypt;

import com.nsc.base.conf.Configurater;
import com.nsc.base.hibernate.CurrentContext;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DownloadStatus;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.ChildProject;
import com.nsc.dem.bean.project.MainProject;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.util.log.LogManager;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.webservice.archive.ArchiveXmlPaser;
import com.nsc.dem.webservice.util.ApplicationContext;
import com.nsc.dem.webservice.util.WsUtils;

public class ReviewPlanService {

	private IService service = null;

	private TUser user = null;
	@SuppressWarnings("all") 
	private Configurater config = Configurater.getInstance();
	
	private Logger logger = null;

	public ReviewPlanService() {
		service = (IService) ApplicationContext.getInstance()
				.getApplictionContext().getBean("baseService");
		try {
			user = WsUtils.getWsUser();
			logger = new LogManager(user.getLoginId(), null).getLogger(this.getClass());
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(ReviewPlanService.class).warn("异常:",e);
		}
		CurrentContext.putInUser(user);
	}

	public String downLoadXmlFileByFtp(MainProject project) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String localPath = FileUtil.getWebRootPath() + File.separator+"temp";
		localPath = localPath.replaceAll("%20", " ");
		
		File folder = new File(localPath);
		if(!folder.isDirectory()){
			folder.mkdirs();
		}
		localPath = localPath+ File.separator+"file_"+ format.format(new Date()) + ".xml";
		String remotePath = project.getXmlPath();
		File localFile = new File(localPath);
		
		try {
			ContinueFTP ftp = ContinueFTP.getDownLoadInstance(project
					.getFtpserver(), Integer.parseInt(project.getFtpPort()),
					project.getFtpuser(), project.getFtppwd());
			DownloadStatus status = ftp.download(remotePath, localPath);
			
			EdrpFileEncrypt.decrypt(localFile);
			logger.info("下载远程XML文件到"+localPath+":"+status);
		} catch (NumberFormatException e) {
			logger.warn("数字转换异常:",e);
			throw new NumberFormatException("FTP端口不是数字");
		} catch (IOException e) {
			logger.warn("输入输出异常:",e);
			throw new IOException("下载XML文件到本地时出现异常!");
		}
		
		//如果解析错误，说明文件错误，删除该文件
		try{
			ArchiveXmlPaser px = new ArchiveXmlPaser();
			px.parseXml(localFile);
		}catch(Exception ex){
			logger.warn("文件列表xml解析错误", ex);
			localFile.delete();
		}
		
		return localPath;
	}

	/**
	 * 保存工程信息至数据库
	 * 
	 * @param List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveProject2Database(List<MainProject> list) {
		for (MainProject mainProject : list) {
			TProject tMain = mainProject.gettProject();
			TProject project = new TProject();
			project.setName(tMain.getName());
			List mainList = service.EntityQuery(project);
			if(mainList==null||mainList.size()==0){
				service.insertEntity(tMain);
			}else{
				tMain = (TProject)mainList.get(0);
			}
			List<ChildProject> childList = mainProject.getChildProjects();
			for (ChildProject childProject : childList) {
				TProject tChild = childProject.gettProject();
				project = new TProject();
				project.setParentId(tMain.getId());
				project.setName(childProject.getProjectName());
				List  childs= service.EntityQuery(project);
				if(childs==null||childs.size()==0){
					tChild.setParentId(tMain.getId());
					service.insertEntity(tChild);
				}
				
			}
		}
	}
	
	
	/**
	 * 下载文件
	 * @return
	 */
	public  String downLoadXmlFileByHttp(MainProject project) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String localPath = FileUtil.getWebRootPath() + File.separator+"temp";
		localPath = localPath.replaceAll("%20", " ");
		
		File folder = new File(localPath);
		if(!folder.isDirectory()){
			folder.mkdirs();
		}
		
		localPath = localPath+ File.separator+"file_"+ format.format(new Date()) + ".xml";
		File localFile = new File(localPath);
		
		InputStream bis = null;
		OutputStream out = null;
		PostMethod postMethod = null;
		try {
			if ("".equals(project.getFileId()) || project.getFileId() == null) return null;
			HttpClient client = new HttpClient();
			StringBuffer address = new StringBuffer(project.getAddress());
			address.append("from=client&");
			address.append("fileId="+project.getFileId()+"&");
			address.append("fileName="+URIUtil.encodePath(project.getFileName(),"UTF-8")+"&");
			address.append("area="+URIUtil.encodePath(project.getArea(), "UTF-8"));
			postMethod = new PostMethod(address.toString());
			
			// 设置最大的连接超时时间
			client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
			int status = client.executeMethod(postMethod);
			if(status != HttpStatus.SC_OK){
				logger.warn("文件获取失败，连接出错");
				return null;
			}
			// 返回的文件流
			bis = postMethod.getResponseBodyAsStream();
			out = new FileOutputStream(localFile);	
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bis.read(buffer)) != -1){
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			
			//如果下载的file解析失败，那么该文件不能使用
			try{
				ArchiveXmlPaser px = new ArchiveXmlPaser();
				px.parseXmlByHttp(localFile);
				logger.info("在博微下载远程文件："+project.getFileName()+"成功！");
				return localPath;
			}catch(Exception ex){
				logger.warn("文件列表xml解析错误", ex);
				localFile.delete();
				return null;
			}
		} catch (Exception e) {
			logger.warn(e);
			return null;
		}finally {
			try {
				if(bis != null) bis.close();
				if(postMethod != null) postMethod.releaseConnection();
			} catch (IOException e) {
				logger.warn(e);
			}
		}
	}

}