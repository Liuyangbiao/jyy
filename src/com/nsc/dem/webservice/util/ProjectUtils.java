package com.nsc.dem.webservice.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.Component;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.base.IService;


/**
 * 现在未使用此类
 * @author ycl
 *
 */
public class ProjectUtils {
	private static IService service = (IService) Component.getInstance(
			"baseService", Configurater.getInstance().getServletContext());

	/**
	 * 从评审计划中的工程名称判断工程类别
	 * 
	 * @param gcName
	 *            工程名称
	 * @return 字典对象
	 * @throws Exception
	 */
	public static TDictionary getProjectType(String gcName) throws Exception {
		TDictionary dic = new TDictionary();
		dic.setParentCode("GCFL");
		if (gcName.indexOf("输电") != -1 || gcName.indexOf("变电") != -1) {

			dic.setName("输变电工程");

		} else if (gcName.indexOf("变电站") != -1) {

			dic.setName("变电站");

		} else if (gcName.indexOf("通信站") != -1) {

			dic.setName("通信站");

		} else if (gcName.indexOf("串联补偿站") != -1) {

			dic.setName("串联补偿站");

		} else if (gcName.indexOf("换流站") != -1) {

			dic.setName("换流站");

		} else if (gcName.indexOf("线路") != -1 && gcName.indexOf("架空") != -1) {

			dic.setName("架空线路");
		} else if (gcName.indexOf("通信线路") != -1) {

			dic.setName("通信线路");
		} else if (gcName.indexOf("线路") != -1) {
			dic.setName("电缆线路");
		} else {
			dic.setName("输变电工程");
		}

		List<Object> typeList = service.EntityQuery(dic);

		if (typeList != null && typeList.size() > 0) {
			dic = (TDictionary) typeList.get(0);
		} else {
			Logger.getLogger(WsUtils.class).error("无法获取到工程分类:");
			throw new Exception("无法获取到工程分类！");
		}

		return dic;
	}

	/**
	 * 从评审计划中获取工程电压等级
	 * 
	 * @param projectInfo
	 *            工程类型
	 * @return 字典对象
	 * 
	 */
	public static String getVoltageLevelByProjectName(String projectInfo) {
		TDictionary dictionary = new TDictionary();
		dictionary.setParentCode("DYDJ");

		if (projectInfo.indexOf("±1000kV") != -1) {
			dictionary.setName("±1000kV");
		} else if (projectInfo.indexOf("1000kV") != -1) {
			dictionary.setName("1000kV");
		} else if (projectInfo.indexOf("±800kV") != -1) {
			dictionary.setName("±800kV");
		} else if (projectInfo.indexOf("750kV") != -1) {
			dictionary.setName("750kV");
		} else if (projectInfo.indexOf("±660kV") != -1) {
			dictionary.setName("±660kV");
		} else if (projectInfo.indexOf("500kV") != -1) {
			dictionary.setName("500kV");
		} else if (projectInfo.indexOf("±400kV") != -1) {
			dictionary.setName("±400kV");
		} else if (projectInfo.indexOf("330kV") != -1) {
			dictionary.setName("330kV");
		} else if (projectInfo.indexOf("220kV") != -1) {
			dictionary.setName("220kV");
		} else if (projectInfo.indexOf("110kV") != -1) {
			dictionary.setName("110kV");
		} else if (projectInfo.indexOf("66kV") != -1) {
			dictionary.setName("66kV");
		} else if (projectInfo.indexOf("35kV") != -1) {
			dictionary.setName("35kV");
		} else {
			return null;
		}
		List<Object> typeList = service.EntityQuery(dictionary);

		if (typeList != null && typeList.size() > 0) {
			dictionary = (TDictionary) typeList.get(0);
		} else {
			Logger.getLogger(WsUtils.class).error("无法获取到工程电压等级");
		}

		return dictionary.getCode();
	}

	/**
	 * 从评审计划中获取工程电压等级
	 * 
	 * @param projectInfo
	 *            工程类型
	 * @return 字典对象
	 * 
	 */
	public static String getVoltageLevelByProjectType(String projectInfo) {
		TDictionary dictionary = new TDictionary();
		dictionary.setParentCode("DYDJ");
			if (projectInfo.indexOf("±1000kV") != -1) {
				dictionary.setName("±1000kV");
			} else if (projectInfo.indexOf("1000kV") != -1) {
				dictionary.setName("1000kV");
			} else if (projectInfo.indexOf("±800kV") != -1) {
				dictionary.setName("±800kV");
			} else if (projectInfo.indexOf("750kV") != -1) {
				dictionary.setName("750kV");
			} else if (projectInfo.indexOf("±660kV") != -1) {
				dictionary.setName("±660kV");
			} else if (projectInfo.indexOf("500kV") != -1) {
				dictionary.setName("500kV");
			} else if (projectInfo.indexOf("±400kV") != -1) {
				dictionary.setName("±400kV");
			} else if (projectInfo.indexOf("330kV") != -1) {
				dictionary.setName("330kV");
			} else if (projectInfo.indexOf("220kV") != -1) {
				dictionary.setName("220kV");
			} else if (projectInfo.indexOf("110kV") != -1) {
				dictionary.setName("110kV");
			} else if (projectInfo.indexOf("66kV") != -1) {
				dictionary.setName("66kV");
			} else if (projectInfo.indexOf("35kV") != -1) {
				dictionary.setName("35kV");
			} else {
				return null;
			}
		List<Object> typeList = service.EntityQuery(dictionary);

		if (typeList != null && typeList.size() > 0) {
			dictionary = (TDictionary) typeList.get(0);
		} else {
			Logger.getLogger(WsUtils.class).error("无法获取到工程电压等级");

		}

		return dictionary.getCode();
	}
}
