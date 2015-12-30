package com.nsc.dem.webservice.system;

import java.util.List;

import com.nsc.dem.bean.system.TServersInfo;
import com.nsc.dem.service.system.IuserService;
import com.nsc.dem.webservice.util.ApplicationContext;

/**
 * 
 * 服务器设置
 *
 */
public class ServersSet {
	private IuserService userService;

	public ServersSet() {
		userService = (IuserService) ApplicationContext.getInstance()
				.getApplictionContext().getBean("userService");
	}

	/**
	 * 插入或更新用的ftp
	 * 
	 * @param unitCode
	 *            单位 code
	 * @param ftpIp
	 *            ftp IP地址
	 * @param ftpName
	 *            ftp 用户名
	 * @param ftpPwd
	 *            ftp 密码
	 * @param ftpPort
	 *            ftp 端口号
	 * @param webIp
	 *            应用服务器的 IP
	 */
	public boolean saveServersInfo(String unitCode, String ftpIp, String ftpName,
			String ftpPwd, String ftpPort, String wsAdd, String unitname) {
		TServersInfo ftp = new TServersInfo();
		ftp.setUnitCode(unitCode);
		ftp.setFtpIp(ftpIp);
		ftp.setFtpName(ftpName);
		ftp.setUnitName(unitname);
		ftp.setFtpPort(ftpPort);
		ftp.setFtpPwd(ftpPwd);
		ftp.setWsAdd(wsAdd);
		
		//先查找，如果查找成功，修改，否则增加
		List<TServersInfo> ftps = userService.getServersInfoByCode(unitCode);
		
		if(null !=ftps && ftps.size() == 1){
			ftp.setId(ftps.get(0).getId());
			userService.updateEntity(ftp);
		}else{
			userService.insertEntity(ftp);
		}
		return true;
	}

	/**
	 * 查询单位的编码、名称、FTP服务器IP、FTP服务器端口号
	 * @return
	 */
	public String findAllServersInfo() {
		StringBuffer buffer = new StringBuffer();
		List serversInfo = userService.findAllServersInfo();
		for (int i = 0; i < serversInfo.size(); i++) {
			TServersInfo ftp = (TServersInfo) serversInfo.get(i);
			buffer.append(ftp.getUnitCode()+ "," + ftp.getUnitName() + ","+ftp.getFtpIp() + ","+ftp.getFtpPort());
			buffer.append("#");
		}
		buffer.deleteCharAt(buffer.length()-1);
		return buffer != null ? buffer.toString():null;
	}
	
	public List<TServersInfo> getServersInfoByCode(String unitCode) {
		return userService.getServersInfoByCode(unitCode);
	}
}
