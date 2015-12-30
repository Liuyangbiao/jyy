package com.nsc.dem.action.archives;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FilenameUtils;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.FileField;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DateUtils;
import com.nsc.base.util.DesUtil;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.searches.IsearchesService;
import com.nsc.dem.service.system.IprofileService;
import com.nsc.dem.util.index.IndexStoreUitls;

/**
 * 
 *文档归档Action
 * 
 * @author ibm
 * 
 */
public class docDisposalAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 800932349702874399L;
	// 接收查询的参数(工程名称、录入开始时间、录入结束时间、录入单位、创建者、文件格式、密级分类、文档分类、文件名称)
	private String projectNameId;

	public void setProjectNameId(String projectNameId) {
		this.projectNameId = projectNameId;
	}

	IprofileService profileService;

	public void setProfileService(IprofileService profileService) {
		this.profileService = profileService;
	}

	String returnValue;

	public String getReturnValue() {
		return returnValue;
	}

	private String editorFormDate;
	private String editorToDate;
	private String unitName;
	private String creatorName;
	private String fileFormat;
	private String baomi;
	private String docClassName;
	private String fileName;
	private String fileStatus;

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	// 页码和行数
	private int page;
	private int rows;

	private String doccodes;
	private String doccodeType;

	public void setDoccodes(String doccodes) {
		this.doccodes = doccodes;
	}

	public void setDoccodeType(String doccodeType) {
		this.doccodeType = doccodeType;
	}

	public List<Object[]> getDocPreDetailsList() {
		return docPreDetailsList;
	}

	public void setDocPreDetailsList(List<Object[]> docPreDetailsList) {
		this.docPreDetailsList = docPreDetailsList;
	}

	public Object[] getDocPreDetials() {
		return docPreDetials;
	}

	public void setDocPreDetials(Object[] docPreDetials) {
		this.docPreDetials = docPreDetials;
	}

	public Object[] getDocDetials() {
		return docDetials;
	}

	public void setDocDetials(Object[] docDetials) {
		this.docDetials = docDetials;
	}

	public Object[] getFileDetials() {
		return fileDetials;
	}

	public void setFileDetials(Object[] fileDetials) {
		this.fileDetials = fileDetials;
	}

	List<Object[]> docPreDetailsList;
	Object[] docPreDetials;
	Object[] docDetials;
	Object[] fileDetials;
	Object[] proDetials;

	public Object[] getProDetials() {
		return proDetials;
	}

	public void setProDetials(Object[] proDetials) {
		this.proDetials = proDetials;
	}

	public List<TDocType> getListDocType() {
		return listDocType;
	}

	public void setListDocType(List<TDocType> listDocType) {
		this.listDocType = listDocType;
	}

	private List<TDocType> listDocType;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 删除时传来的codes
	private String codes;

	public void setCodes(String codes) {
		this.codes = codes;
	}

	TableBean tablebean = new TableBean();

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	// 服务方法
	IarchivesService archivesService;

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	/**
	 * 
	 * 查询档案信息的action
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String selectDocListAction() throws Exception {

		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResult = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();

		String username = "%" + creatorName + "%";
		String file = "%" + fileName + "%";
		String uname = "%" + unitName + "%";
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 未归档
		map.put("filestatus", "03");
		String pronameId = projectNameId.length() == 0 ? null : projectNameId;
		map.put("project", pronameId);// 工程code
		String beginDate = editorFormDate.length() == 0 ? null : editorFormDate;
		map.put("begin_create_date", beginDate);// 录入开始时间

		String endDate = editorToDate.length() == 0 ? null : editorToDate;
		map.put("end_create_date", endDate);// 录入结束时间

		map.put("unname", uname);// 录入单位
		map.put("uname", username); // 创建者名称
		// 归档
		map.put("filestatus", fileStatus);
		// 文件格式
		if (fileFormat == null || fileFormat.equals("")) {
			map.put("format", null);
		} else {
			map.put("format", fileFormat);
		}
		// 密级分类
		if (baomi == null || baomi.equals("")) {
			map.put("security", null);
		} else {
			map.put("security", baomi);
		}
		// 文档分类
		if (docClassName == null || docClassName.equals("")) {
			map.put("doc_type", null);
		} else {
			map.put("doc_type", docClassName);
		}
		map.put("dname", file);// 文件名称
		map.put("uncode", super.getLoginUser().getTUnit().getProxyCode());
		list = searchesService.queryBasicList(map, firstResult, maxResult,
				tablebean);
		TUser user = super.getLoginUser();

		boolean isPreView = profileService.getProfileByauthControl(user,
				profileId, "预览");
		boolean isOnLineSee = profileService.getProfileByauthControl(user,
				profileId, "在线查看预览");

		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TDoc tdoc = (TDoc) obj[0];
				TProject project = (TProject) obj[1];
				TDocType docType = (TDocType) obj[2];
				TUser tuser = (TUser) obj[3];

				TDictionary scDic = (TDictionary) obj[6];
				TDictionary stDic = (TDictionary) obj[7];

				RowBean rowbean = new RowBean();

				String preview = "";
				if (isPreView == true) {
					if (tdoc.getPreviewPath() != null) {
						preview = "<a href='#'  onclick='previewImage("
								+ tdoc.getId() + ")'>[预览]</a>";
					}

				} else {
					preview = "";
				}
				String onLineSee = "";
				if (isOnLineSee == true) {
					String str = "\""+tdoc.getId()+"\",\""+ project.getId()+"\",\""+project.getTUnitByOwnerUnitId().getCode()
				       +"\",\""+tdoc.getName()+"\",\""+URIUtil.encodePath(tdoc.getPath(),"UTF-8")+"\",\""+tdoc.getSuffix()+"\"";
					onLineSee = "<a href='#' onclick='onlineDown(" + str
							+ ")' style='cursor:hand'>" + tdoc.getName() + "</a>";
					if (tdoc.getPreviewPath() != null) {
						preview = "<a href='#' onclick='previewImage("
								+ tdoc.getId() + ")'>[预览]</a>";
					}

				} else {
					onLineSee = tdoc.getName();
				}

				// 取时间
				String datetime = DateUtils.DateToString(tdoc.getCreateDate());

				rowbean.setCell(new Object[] {

						project.getName(),
						onLineSee,
						docType.getName(),
						stDic.getName(),
						scDic.getName(),
						tdoc.getFormat(),
						tuser.getName(),
						datetime,
						preview + "<a href='#'  onclick='showDocDetails(\""
								+ tdoc.getId() + "\")'>[详细]</a>",
						project.getType()

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
	 * 删除文件信息action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteDocInfoAction() throws Exception {

		String[] codeList = codes.split(",");

		archivesService.delArchives(codeList);
		returnValue = "success";

		return SUCCESS;
	}

	/**
	 * 
	 * 将未归档文件，改为归档状态
	 * 
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public String updateDocStatusAction() throws Exception {

		String[] codeList = codes.split(",");

		String tempDir = Configurater.getInstance().getConfigValue("temp");

		tempDir = super.getRealPath(tempDir);

		File tempFolder = new File(tempDir);

		if (!tempFolder.isDirectory()) {
			tempFolder.mkdirs();
		}

		ContinueFTP ftpUtil = ContinueFTP.getInstance();

		Map<File, Map<Enum<?>, FileField>> files = new HashMap<File, Map<Enum<?>, FileField>>();
		Map<File, TDoc> docs = new HashMap<File, TDoc>();

		for (int i = 0; i < codeList.length; i++) {
			String eachCode = codeList[i];
			TDoc doc = (TDoc) archivesService.EntityQuery(TDoc.class, eachCode);
			// 查工程
			TProject tPro = archivesService.getTProjectBydocId(doc.getId());
			if (tPro == null)
				continue;
			String remotePath = doc.getPath();

			String local = tempFolder.getAbsolutePath() + File.separator
					+ doc.getName() + "." + doc.getSuffix();
			File file = new File(local);
			ftpUtil.download(remotePath, local);

			if (file.exists() && file.canRead()) {

				String mimeType = Configurater.getInstance().getConfigValue(
						"mime", FilenameUtils.getExtension(file.getName()));
				if (mimeType == null) {
					mimeType = Configurater.getInstance().getConfigValue(
							"mime", "*");
				}

				String dest = local;

				// 非图片需要解密
				if (mimeType.indexOf("image") == -1) {
					dest = Configurater.getInstance().getConfigValue("decrypt");

					File destPathFolder = new File(file.getParentFile(), dest);
					if (!destPathFolder.isDirectory())
						destPathFolder.mkdirs();

					dest = destPathFolder.getAbsolutePath() + File.separator
							+ file.getName();

					DesUtil.decrypt(local, dest);
				}

				String code = doc.getTUser().getTUnit().getCode();
				TUnit unit = (TUnit) archivesService.EntityQuery(TUnit.class,
						code);
				File f = new File(dest);
				doc.setStatus("01");

				docs.put(f, doc);
				files.put(f, archivesService.setArchivesIndex(doc, tPro, dest,
						unit));
				// 不是同一文件，即存在解密文件，清除未解密文件
				if (!file.equals(f))
					file.delete();
			}

			try {
				String storeLocation = super.getLoginUser().getTUnit().getCode();
				archivesService.addArchiveIndex(files, storeLocation, "local");
			} finally {
				for (File file1 : files.keySet()) {
					if (file1.exists() && file1.canRead())
						file1.delete();

					TDoc doc1 = docs.get(file1);
					// 标识是否建立索引，1是有索引，0是没有索引
					doc1.setMetaFlag(BigDecimal.ONE);
					doc1.setStatus("01");
					this.archivesService.updateEntity(doc1);
				}
			}
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 初始化文档分类列表
	 * 
	 * @return
	 */
	public String initTreeAction() {

		tDocTypeList = archivesService.docTypeList("", "");
		for (int i = 0; i < tDocTypeList.size(); i++) {
			TDocType tDocType = tDocTypeList.get(i);

			List<TDocType> listChild = archivesService.docTypeList(tDocType
					.getCode());
			tDocType.setList(listChild);
		}

		return "initTree";
	}

	/**
	 * 文件迁移
	 * 
	 * @return
	 * @throws Exception
	 */
	public String moveDocAction() throws Exception {
		String[] codeList = doccodes.split(",");

		for (int i = 0; i < codeList.length; i++) {
			String storeLocation = IndexStoreUitls.getStoreLocation(codeList[i],this.getSession().getServletContext());
			
			archivesService.delMoveFile(storeLocation,"local",new String[] { codeList[i] },
					doccodeType);
			
			archivesService.updateIndex("local", codeList[i], storeLocation);
		}

		returnValue = "success";
		return SUCCESS;
	}

	private List<TDocType> tDocTypeList;

	public List<TDocType> gettDocTypeList() {
		return tDocTypeList;
	}

	public void settDocTypeList(List<TDocType> tDocTypeList) {
		this.tDocTypeList = tDocTypeList;
	}

	public void setEditorFormDate(String editorFormDate) {
		this.editorFormDate = editorFormDate;
	}

	public void setEditorToDate(String editorToDate) {
		this.editorToDate = editorToDate;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void setBaomi(String baomi) {
		this.baomi = baomi;
	}

	public void setDocClassName(String docClassName) {
		this.docClassName = docClassName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String profileId;

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	// 保密
	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	/**
	 * 查询文档大分类
	 * 
	 * @return
	 */
	public String docTypeListAction() {
		list = new ArrayList<Map<String, Object>>();
		List<TDocType> listDoc = archivesService.docTypeList(null, null);
		for (int i = 0; i < listDoc.size(); i++) {
			TDocType doctype = listDoc.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", doctype.getCode());
			map.put("name", doctype.getName());
			list.add(map);
		}
		return SUCCESS;
	}

}
