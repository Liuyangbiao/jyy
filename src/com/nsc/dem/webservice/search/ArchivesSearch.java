package com.nsc.dem.webservice.search;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import com.nsc.base.hibernate.CurrentContext;
import com.nsc.dem.bean.archives.TProjectDocCount;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.searches.IsearchesService;

public class ArchivesSearch {
	private IsearchesService searchesService;

	public ArchivesSearch(IsearchesService service, TUser user) {
		searchesService = service;
		searchesService.getLogManager(user);
		CurrentContext.putInUser(user);
	}

	/**
	 * 跨域统计
	 * 
	 * @param document
	 */
	public void siteCountAarchives(Document document) {
		Element root = document.getRootElement();
		List<Element> archieves = root.getChildren("count");
		for (Element ele : archieves) {
			// 解析count
			// 取code编码
			String code = ele.getAttributeValue("code");
			// 取出创建时间
			String updatetime = ele.getAttributeValue("create_time");
			// 解析yearmonth
			List<Element> yearmonths = ele.getChildren("year_month");
			for (Element year : yearmonths) {
				// 取出时间
				String month = year.getAttributeValue("value");
				// 取出档案数
				String docsCount = year.getAttributeValue("docsCount");

				// 解析工程id、工程名称、电压等级
				Element pidElement = year.getChild("project_id");
				Element pnameElement = year.getChild("project_name");
				Element plevelElement = year.getChild("voltage_level");
				TProjectDocCount prdoc = new TProjectDocCount();
				// 工程Id
				prdoc.setProjectId(Long.valueOf(pidElement.getTextTrim()));
				// 工程名称
				prdoc.setProjectName(pnameElement.getTextTrim());
				// 电压等级
				prdoc.setVoltageLevel(plevelElement.getTextTrim());

				TUnit unit = (TUnit) searchesService.EntityQuery(TUnit.class,
						code);
				// 单位
				prdoc.setTUnit(unit);
				// 文档总数
				prdoc.setDocCount(BigDecimal.valueOf(Long.valueOf(docsCount)));

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");

				Date udate;
				try {
					udate = simpleDateFormat.parse(updatetime);
					prdoc.setUpdateTime(udate);

				} catch (ParseException e) {
					Logger.getLogger(ArchivesSearch.class).error(e);
				}
				prdoc.setYearMonth(month);
				int isInsert = searchesService.isInsertTProjectDocCount(
						pidElement.getTextTrim(), month);
				if (isInsert == 0) {
					searchesService.insertEntity(prdoc);
				} else {
					prdoc.setId(new Long((long) isInsert));
					searchesService.updateEntity(prdoc);
				}

			}

		}

	}
}
