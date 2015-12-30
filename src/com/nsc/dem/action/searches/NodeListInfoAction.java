package com.nsc.dem.action.searches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.util.URIUtil;

import com.nsc.base.util.DateUtils;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TTreeDef;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IprofileService;

/**
 * 快速定位
 * 
 * @author ibm
 * 
 */
public class NodeListInfoAction extends BaseAction {
	private static final long serialVersionUID = 5304532811867020924L;
	// 取出节点的参数和值
	private String options;

	public void setOptions(String options) {
		this.options = options;
	}

	// 默认的页数
	private int page;

	public void setPage(int page) {
		this.page = page;
	}

	private int rows;

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	private String id;

	public void setId(String id) {
		this.id = id;
	}
	
	String treeName;

	public String getTreeName() {
		return treeName;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	IprofileService profileService;

	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	/**
	 * 档案查询 并将结果分页显示
	 */
	@SuppressWarnings("unchecked")
	public String getNodeListInFormation() throws Exception {
		String menuId = super.getRequest().getParameter("menuId");
		if (menuId != null) {
			menuId = "7";
		}
		HashMap map = new HashMap();
		if (options != null) {
			String[] eachoption = options.split(";");
			for (int i = 0; i < eachoption.length; i++) {
				String[] paramvalue = eachoption[i].split("=");
				String param = paramvalue[0];
				String value = paramvalue[1];
				map.put(param, value);
			}
		}

		TUser user = super.getLoginUser();


		boolean isPreview = profileService.getProfileByauthControl(user,
				menuId, "预览");
		boolean isOnLiSee = profileService.getProfileByauthControl(user,
				menuId, "在线查看预览");

		// 起始位置为：(页码-1)*rows
		int firstResult = (page - 1) * rows;
		// 终止位置为：页码*行数-1
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		map.put("uncode", super.getLoginUser().getTUnit().getProxyCode());
		map.put("filestatus", "01");
		list = searchesService.queryBasicList(map, firstResult, maxResults,
				tablebean);

		List rowsList = new ArrayList();

		for (Object[] obj : list) {
			TDoc doc = (TDoc) obj[0];
			TProject project = (TProject) obj[1];
			TDocType docType = (TDocType) obj[2];
			FileUtil fileUtil = new FileUtil();

			String docFileSize = fileUtil.getHumanSize(doc.getFileSize()
					.longValue());// 大小
			RowBean rowbean = new RowBean();
			String preview = "";
			if (isPreview == true) {
				if (doc.getPreviewPath() != null) {
					preview = "<a href='#'  onclick='previewImage("
							+ doc.getId() + ")'>[预览]</a>";
				}

			} else {
				preview = "";
			}
			String onLineSee = "";
			if (isOnLiSee == true) {
				String str = "\""+doc.getId()+"\",\""+ project.getId()+"\",\""+project.getTUnitByOwnerUnitId().getCode()
			       +"\",\""+doc.getName()+"\",\""+URIUtil.encodePath(doc.getPath(),"UTF-8")+"\",\""+doc.getSuffix()+"\"";
				onLineSee = "<a href='#' onclick='onlineDown(" + str
						+ ")' style='cursor:hand'>" + doc.getName() + "</a>";
				if (doc.getPreviewPath() != null) {
					preview = "<a href='#' onclick='previewImage("
							+ doc.getId() + ")'>[预览]</a>";
				}

			} else {
				onLineSee = doc.getName();
			}

			rowbean.setCell(new Object[] {
					onLineSee,
					docType == null ? "" : docType.getName(),
					project == null ? "" : project.getName(),
					doc.getFormat(),
					DateUtils.DateToString(doc.getCreateDate()),
					docFileSize,
					preview + "<a href='#'  onclick='showDocDetails(\""
							+ doc.getId() + "\")' >[详细]</a>" });
			// 当前的ID为1
			rowbean.setId(doc.getId() + "<>" + project.getId() + "<>"
					+ project.getTUnitByOwnerUnitId().getCode() + "<>" + doc.getName()
					+ "<>" + doc.getPath() + "<>" + doc.getSuffix());
			rowsList.add(rowbean);
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
	 * 根据nodeId，返回按***定位的名称
	 * @return
	 */
	public String selectIdGetNameAction() {

		TTreeDef tree = (TTreeDef) searchesService.EntityQuery(TTreeDef.class,
				Long.valueOf(id));
		treeName = tree.getName();

		return SUCCESS;
	}
}
