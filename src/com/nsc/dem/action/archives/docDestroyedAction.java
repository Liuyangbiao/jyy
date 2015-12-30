package com.nsc.dem.action.archives;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.nsc.base.util.DateUtils;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.util.index.IndexStoreUitls;

/**
 * 
 * 文档销毁action
 * 
 * @author ibm
 * 
 */
public class docDestroyedAction extends BaseAction {
	
	private static final long serialVersionUID = -2250891955554921838L;
	// 传来的参数(工程编码、保密、工程分类、工程阶段、开工年份)
	private String projectNameCode;
	private String baomi;
	private String proClass;
	private String proStatus;
	private String fileStatus;

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	

	// 页码和行数
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setProjectNameCode(String projectNameCode) {
		this.projectNameCode = projectNameCode;
	}

	public void setBaomi(String baomi) {
		this.baomi = baomi;
	}

	public void setProClass(String proClass) {
		this.proClass = proClass;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	public void setKaigongyear(String kaigongyear) {
		this.kaigongyear = kaigongyear;
	}

	private String kaigongyear;

	// 查询的服务
	IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	IarchivesService archivesService;

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	// 更改删除时传来的codes
	private String codes;

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	String returnValue;

	public String getReturnValue() {
		return returnValue;
	}

	/**
	 * 
	 * 查询归销文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectDocDestoryedAction() throws Exception {

		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResult = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		// 未归档
		map.put("filestatus", fileStatus);
		map.put("project", projectNameCode);// 工程code
		// 密级分类
		if (baomi == null || baomi.equals("")) {
			map.put("security", null);
		} else {
			map.put("security", baomi);
		}
		// 工程分类
		if (proClass == null || proClass.equals("")) {
			map.put("type", null);
		} else {
			map.put("type", proClass);
		}
		// 工程阶段
		if (proStatus == null || proStatus.equals("")) {
			map.put("status", null);
		} else {
			map.put("status", proStatus);
		}

		map.put("uncode", super.getLoginUser().getTUnit().getProxyCode());
		map.put("open_year", kaigongyear);
		list = searchesService.queryBasicList(map, firstResult, maxResult,
				tablebean);
		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TDoc tdoc = (TDoc) obj[0];
				TProject project = (TProject) obj[1];
				TDocType docType = (TDocType) obj[2];
				TUser tuser = (TUser) obj[3];

				TDictionary tbaomi = (TDictionary) obj[6];
				TDictionary docStatus = (TDictionary) obj[7];
				RowBean rowbean = new RowBean();

				// 档案状态
				String status = docStatus.getName();
				// 密级
				String baomi = tbaomi.getName();

				// 取时间
				String datetime = DateUtils.DateToString(tdoc.getCreateDate());
				rowbean.setCell(new Object[] {

						project.getName(),
						tdoc.getName(),
						docType.getName(),
						status,
						baomi,
						tdoc.getFormat(),
						tuser.getName(),
						datetime,
						"<a href='#'  onclick='showDocDetails(\""
								+ tdoc.getId() + "\")' >[详细]</a>"

				});
				// 当前的ID为1
				rowbean.setId(tdoc.getId());
				rowsList.add(rowbean);
			}
		}

		// 给单元格里存放数据
		tablebean.setRows(rowsList);
		if (tablebean.getRecords() == 0) {
			tablebean.setPage(0);
		} else {
			// 当前页是1页
			tablebean.setPage(this.page);

		}

		int records = tablebean.getRecords();
		// 总页数
		tablebean.setTotal(records % rows == 0 ? records / rows : records
				/ rows + 1);
		return SUCCESS;
	}

	/**
	 * 将归档状态，更新为销毁状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateDocDestroyAction() throws Exception {
		String[] codeList = codes.split(",");
		
		for (int i = 0; i < codeList.length; i++) {
			String eachCode = codeList[i];
			TDoc doc = (TDoc) archivesService.EntityQuery(TDoc.class, eachCode);
			doc.setMetaFlag(BigDecimal.ZERO);
			doc.setStatus("02");
			archivesService.updateEntity(doc);
			//根据文件ID工程
			String storeLocation = IndexStoreUitls.getStoreLocation(doc.getId(),this.getSession().getServletContext());
			archivesService.updateIndex("local", doc.getId(),storeLocation);
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 将销毁状态，更改为归档状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public String comeBackDocDestoryAction() throws Exception {
		String[] codeList = codes.split(",");
		for (int i = 0; i < codeList.length; i++) {
			String eachCode = codeList[i];
			TDoc doc = (TDoc) archivesService.EntityQuery(TDoc.class, eachCode);
			doc.setMetaFlag(BigDecimal.ONE);
			doc.setStatus("01");
			this.archivesService.updateEntity(doc);
			String storeLocation = IndexStoreUitls.getStoreLocation(doc.getId(), this.getSession().getServletContext());
			archivesService.updateIndex("local", doc.getId(), storeLocation);
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 将文档清空
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearDocDestoryAction() throws Exception {
		String[] codeList = codes.split(",");
		for (int i = 0; i < codeList.length; i++) {
//			TDoc doc = (TDoc) archivesService.EntityQuery(TDoc.class,
//					codeList[i]);
			// 删除已经建立的索引
			String storeLocation = IndexStoreUitls.getStoreLocation(codeList[i],this.getSession().getServletContext());
			this.archivesService.deleteArchiveIndex(storeLocation, "local", codeList[i]);
		}

		archivesService.delArchives(codeList);

		returnValue = "success";
		return SUCCESS;
	}
}