package com.nsc.dem.util.download;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.nsc.base.conf.Configurater;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.XmlUtils;

public class DownloadAddessUtils {

	/**
	 * 组合下载地址
	 * 
	 * @param loginLocation  登录所在地ID
	 * @param toUintIds      文档在哪几个省有
	 * @param isLocalLogin   是否本地登录
	 * @param userCode       用户所属单位
	 * @return
	 */
	public static List<String[]> getDownloadAddress(String loginLocation,
			String toUintIds, boolean isLocalLogin, String userCode) {
		/**
		 * 如果用户本地登录 
		 *       文件本地存在 本地下载地址优先 
		 *       如果文件不存在 由计算机分配 如果
		 * 用户在外地登录 
		 *       文件在该区域存在 该区域下载地址优先
		 *       不存在，由计算机分配
		 */
		// 本地登录
		if (isLocalLogin) {
			return localLogin(toUintIds, userCode);
		}else{
			return otherAreaLogin(loginLocation, toUintIds);
		}
	}
	
	
	/**
	 * 用户在外地登录
	 * @param loginLocation
	 * @param toUintIds
	 * @return
	 */
	private static List<String[]> otherAreaLogin(String loginLocation,
			String toUintIds) {
		String firstAddress = findLocalIsHaveFile(loginLocation, toUintIds);
		// 如果文件在本地存在
		if (StringUtils.isNotBlank(firstAddress)) {
			toUintIds = reCreateToUnitIds(firstAddress, toUintIds);
			List<String[]> address = computerPlotDownLoadAddress(
					loginLocation, toUintIds);
			String[] str = new String[] { firstAddress,
					FtpXmlUtils.getUnitName(firstAddress) };
			address.add(0, str);
			return address;
		} else {
			return computerPlotDownLoadAddress(loginLocation,
					toUintIds);
		}
		
	}


	/**
	 * 本地登录
	 * @param loginLocation
	 * @param toUintIds
	 * @param userCode
	 * @return
	 */
	private static List<String[]> localLogin(String toUintIds, String userCode) {
		Configurater config = Configurater.getInstance();
		String systemType = config.getConfigValue("system_type");
		// 如果是登录国网
		if ("1".equals(systemType)) {
			// 国网用户(省公司用户如果在国网范围登录，作为国网用户处理)
			String country = config.getConfigValue("country");
			if (userCode.length() <= 4 || userCode.length() == 8) {
				String firstAddress = findLocalIsHaveFile(country,
						toUintIds);
				// 如果文件在本地存在
				if (StringUtils.isNotBlank(firstAddress)) {
					toUintIds = reCreateToUnitIds(firstAddress, toUintIds);
					List<String[]> address = computerPlotDownLoadAddress(
							country, toUintIds);
					String[] str = new String[] { firstAddress,
							FtpXmlUtils.getUnitName(firstAddress) };
					address.add(0, str);
					return address;
				} else {
					return computerPlotDownLoadAddress(country,
							toUintIds);
				}
				// 区域用户
			} else {
				String firstAddress = findLocalIsHaveFile(userCode,
						toUintIds);
				// 如果文件在本地存在
				if (StringUtils.isNotBlank(firstAddress)) {
					toUintIds = reCreateToUnitIds(firstAddress, toUintIds);
					List<String[]> address = computerPlotDownLoadAddress(
							userCode, toUintIds);
					String[] str = new String[] { firstAddress,
							FtpXmlUtils.getUnitName(firstAddress) };
					address.add(0, str);
					return address;
				} else {
					return computerPlotDownLoadAddress(userCode,
							toUintIds);
				}
			}
		//省公司	
		}else if("3".equals(systemType)){
			String firstAddress = findLocalIsHaveFile(userCode,
					toUintIds);
			// 如果文件在本地存在
			if (StringUtils.isNotBlank(firstAddress)) {
				toUintIds = reCreateToUnitIds(firstAddress, toUintIds);
				List<String[]> address = computerPlotDownLoadAddress(
						userCode, toUintIds);
				String[] str = new String[] { firstAddress,
						FtpXmlUtils.getUnitName(firstAddress) };
				address.add(0, str);
				return address;
			} else {
				return computerPlotDownLoadAddress(userCode,
						toUintIds);
			}
		}
		return null;
	}

	/**
	 * 查找本地是否有该文件
	 * 
	 * @param unitCode
	 *            单位ID
	 * @param toUnitIds
	 *            文件分配到哪些单位
	 * @return
	 */

	private static String findLocalIsHaveFile(String unitCode, String toUnitIds) {
		String[] allDownAdds = toUnitIds.split("#");
		List<String> addDownAddList = new ArrayList<String>();
		for (String str : allDownAdds) {
			addDownAddList.add(str);
		}
		String firstDownLoadAdd = "";
		for (String str : addDownAddList) {
			if (str.equals(unitCode)) {
				firstDownLoadAdd = str;
				return firstDownLoadAdd;
			}
		}
		return null;
	}

	/**
	 * 在resourceStr中删除deleteStr
	 * 
	 * @param args
	 */
	public static String reCreateToUnitIds(String deleteStr, String resourceStr) {
		String[] allDownAdds = resourceStr.split("#");
		List<String> addDownAddList = new ArrayList<String>();
		for (String str : allDownAdds) {
			addDownAddList.add(str);
		}
		addDownAddList.remove(deleteStr);
		String buffer = "";
		for (String str : addDownAddList) {
			buffer += str + "#";
		}
		return buffer;
	}

	/**
	 * 由计算机分配下载地址
	 * 
	 * @param loginLocation
	 * @param toUintIds
	 * @return
	 */
	public static List<String[]> computerPlotDownLoadAddress(
			String loginLocation, String toUintIds) {
		XmlUtils util = XmlUtils.getInstance("ftp.xml");
		List<String[]> lists = new ArrayList<String[]>();
		// 档案分发单位
		String[] ids = toUintIds.split("#");
		Document document = util.getDocument();
		for (String id : ids) {
			Element element = (Element) document
					.selectSingleNode("//ftp[@code='" + loginLocation
							+ "']/unit[@code='" + id + "']");
			if (null != element) {
				String value = element.attributeValue("value");
				// 如果value小于0，IP有问题
				if (Integer.parseInt(value) >= 0) {
					String[] unit = new String[3];
					unit[0] = element.attributeValue("code");
					unit[1] = element.attributeValue("name");
					unit[2] = value;
					lists.add(unit);
				}
			}
		}
		// 排序
		java.util.Collections.sort(lists, new Comparator<String[]>() {
			public int compare(String[] o1, String[] o2) {
				return Integer.parseInt(o1[2]) - Integer.parseInt(o2[2]);
			}
		});

		return lists;
	}

}
