package com.nsc.dem.util.index;


import javax.servlet.ServletContext;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.Component;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.service.project.IprojectService;

/**
 * 检索文件存储管理工具类
 *
 */
public class IndexStoreUitls {
	
	/**
	 * 获取存储位置
	 */
	public static String getStoreLocation(String docId, ServletContext context){
		IprojectService projectService = (IprojectService) Component.getInstance(
				"projectService", context);
		TDoc doc = new TDoc();
		doc.setId(docId);
		TProject tPro = projectService.getProjectByDoc(doc);
		if(tPro == null)
			return null;
		String location = tPro.getApproveUnit().getProxyCode();
		String companyId = tPro.getTUnitByOwnerUnitId().getCode();
		return getStoreLocation(companyId, location);
	}
	
	
	/**
	 * 获取存储位置
	 * @param companyId 业主单位
	 * @param location  评审地点
	 */
	public static String getStoreLocation(String companyId, String location){
		Configurater config = Configurater.getInstance();
		/*
		 * 国网用户：如果评审单位是国网，保存到国网local
		 *           如果评审单位是区域，保存到业主单位所属区域
		 */
		if("1".equals(config.getConfigValue("system_type").trim())){
			if(location.length() == 4){
				return config.getConfigValue("country");
			}else if(location.trim().length() == 6){
				return companyId.trim().substring(0,6);
			}else{
				return null;
			}
		//省公司的所有操作直接保存在本地的local
		}else if("3".equals(config.getConfigValue("system_type").trim())){//省公司
			return config.getConfigValue("unitCode");
		}else{
			return null;
		}
	}
	
}
