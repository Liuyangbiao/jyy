package com.nsc.dem.service.archives.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.text.BadLocationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.hibernate.Session;

import com.nsc.base.conf.Configurater;
import com.nsc.base.index.AnalyzerFactory;
import com.nsc.base.index.DOCFIELDEnum;
import com.nsc.base.index.FileField;
import com.nsc.base.index.IIndexWriter;
import com.nsc.base.index.IndexFactory;
import com.nsc.base.util.ContinueFTP;
import com.nsc.base.util.DeleteFileStatus;
import com.nsc.base.util.DesUtil;
import com.nsc.base.util.DownloadStatus;
import com.nsc.base.util.FileUtil;
import com.nsc.base.util.UploadStatus;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.archives.TSynProject;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.project.TComponent;
import com.nsc.dem.bean.project.TComponentDoc;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.project.TDocProjectId;
import com.nsc.dem.bean.project.TPreDesgin;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.project.TRecordDrawing;
import com.nsc.dem.bean.project.TShopDoc;
import com.nsc.dem.bean.project.TShopDrawing;
import com.nsc.dem.bean.project.TTender;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.base.BaseService;
import com.nsc.dem.util.filestore.FileStoreLocation;
import com.nsc.dem.util.filestore.Pinyin4jUtil;
import com.nsc.dem.util.index.EXFIELDEnum;
import com.nsc.dem.util.index.FileDirUtils;
import com.nsc.dem.util.index.IndexSearchManager;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.util.xml.FtpXmlUtils;
import com.nsc.dem.util.xml.StoreFileReceiveID;

@SuppressWarnings("unchecked")
public class ArchivesServiceImpl extends BaseService implements
		IarchivesService {

	/**
	 * 查询档案基本信息列表
	 * 
	 * @param 档案实体类
	 * @return
	 */
	public List archivesInfoList() {
		return null;
	}

	/**
	 * 查看档案详细信息
	 * 
	 * @param 档案主键
	 * @return 档案实体类
	 */
	public List archivesInfo() {

		return null;
	}

	/**
	 * 录入档案 将版本信息置为1，初始化档案信息，调用档案信息存储方法。 对档案信息进行批量存储。调用生成预览图方法生成预览图
	 * 
	 * @param 档案实体类
	 */

	public void insertArchives(TDoc tdoc, TDocProject tdocPro,
			TPreDesgin tpreDesgin, TProject tpro, TComponent tcom,
			TComponentDoc tcomp, TDocType docType, String login_id, String local)
			throws Exception {

		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);

		try {
			this.tranArchives(docType, tpro, tdoc, local);

			addArchives(tdoc, tdocPro, tpreDesgin, tpro, tcom, tcomp, login_id,
					local);

			logger.info("档案录入信息成功");

		} catch (IOException e) {
			logger.warn("上传文件失败:", e);
			logger.error("上传文件失败:", "06", "上传文件:" + tdoc.getName() + " 失败");
			throw new Exception("上传文件:" + tdoc.getName() + " 失败"
					+ e.getMessage());
		} catch (Exception e) {
			logger.error("档案录入信息失败:", "06", "档案录入信息失败:" + tdoc.getName()
					+ " 失败", e);
			throw new Exception("档案录入信息失败：" + e.getMessage());
		}

	}

	/**
	 * 对文件 进行加密 生成缩略图 上传到服务器 并删除已上传文件
	 * 
	 * @param docType
	 *            文档类型
	 * @param tpro
	 *            工程
	 * @param tdoc
	 *            文档
	 * @param local
	 *            本地路径
	 * @throws Exception
	 */
	public void tranArchives(TDocType docType, TProject tpro, TDoc tdoc,
			String local) throws Exception {
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);

		// 上传到FTP文件服务器的地址
		String remotePath = this.filePathProduce(docType.getName(), tpro);
		
		String fileNameTemp = tdoc.getName();
		
		//转为拼音
		String pathLanguage = Configurater.getInstance().getConfigValue("pathlanguage");
		if(pathLanguage.toUpperCase().equals("PY")){
			fileNameTemp = Pinyin4jUtil.getPinYin(fileNameTemp);
		}
		
		remotePath = remotePath + File.separator
				+ this.fileGuidName(fileNameTemp + "." + tdoc.getSuffix());

		tdoc.setPath(remotePath);
		tdoc.setMetaFlag(new BigDecimal(0));

		String fileName = DesUtil.getFileName(local);

		String ftpPath = File.separator + "archives" + remotePath;

		String mimeType = Configurater.getInstance().getConfigValue("mime",
				tdoc.getSuffix().toLowerCase());
		
		// 非图片做加密处理
		if (mimeType == null || mimeType.indexOf("image") == -1) {

			String encryptPath = DesUtil.getFilePath(local);
			encryptPath = encryptPath
					+ Configurater.getInstance().getConfigValue("encrypt");

			encryptPath = encryptPath + File.separator + fileName;

			// 加密文件
			DesUtil.encrypt(local, encryptPath);

			UploadStatus status = ContinueFTP.getInstance().upload(
					encryptPath, ftpPath);
			logger.info("上传状态:" + status);
			if (!encryptPath.equals("")) {
				FileUtil.deleteFile(encryptPath);
				FileUtil.deleteFile(local);
			}
		} else {
			ContinueFTP.getInstance().upload(local, ftpPath);
			FileUtil.deleteFile(local);
		}
		tdoc.setPath(ftpPath); 
	}

	
	
	
	/**
	 * 全文解索设置
	 */
	public Map<Enum<?>, FileField> setArchivesIndex(TDoc tdoc, TProject tpro,
			String local, TUnit tunit) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException {

		Map<Enum<?>, FileField> params = new HashMap<Enum<?>, FileField>();
		if (tdoc != null) {
			DOCFIELDEnum author = DOCFIELDEnum.author;
			author.setValue(tdoc.getTUser().getName());
			params.put(author, author.getF());

			DOCFIELDEnum title = DOCFIELDEnum.title;
			title.setValue(tdoc.getName());
			params.put(title, title.getF());

			DOCFIELDEnum cdate = DOCFIELDEnum.cdate;
			cdate.setValue(tdoc.getCreateDate().toString());
			params.put(cdate, cdate.getF());

			DOCFIELDEnum docid = DOCFIELDEnum.docid;
			docid.setValue(tdoc.getId());
			params.put(docid, docid.getF());

			DOCFIELDEnum doctype = DOCFIELDEnum.doctype;
			doctype.setValue(tdoc.getTDocType().getName());
			params.put(doctype, doctype.getF());

			DOCFIELDEnum version = DOCFIELDEnum.version;
			version.setValue(tdoc.getVersion());
			params.put(version, version.getF());

			// 文档索引的状态
			DOCFIELDEnum status = DOCFIELDEnum.status;
			status.setValue(tdoc.getStatus());
			params.put(status, status.getF());
			
			DOCFIELDEnum url = DOCFIELDEnum.url;
			url.setValue(tdoc.getPath());
			params.put(url, url.getF());
			
			DOCFIELDEnum suffix = DOCFIELDEnum.suffix;
			suffix.setValue(tdoc.getSuffix());
			params.put(suffix, suffix.getF());
		}

		if (tpro != null) {
			TDictionary dic = (TDictionary) super.EntityQuery(
					TDictionary.class, tpro.getType());

			EXFIELDEnum proType = EXFIELDEnum.proType;
			proType.setValue(dic.getName());
			params.put(proType, proType.getF());

			TDictionary tdic = (TDictionary) super.EntityQuery(
					TDictionary.class, tpro.getStatus());

			EXFIELDEnum pharase = EXFIELDEnum.pharase;
			pharase.setValue(tdic.getName());
			params.put(pharase, pharase.getF());

			EXFIELDEnum project = EXFIELDEnum.project;
			project.setValue(tpro.getName());
			params.put(project, project.getF());

			EXFIELDEnum projectid = EXFIELDEnum.projectid;
			projectid.setValue(tpro.getId().toString());
			params.put(projectid, projectid.getF());

			EXFIELDEnum predesign = EXFIELDEnum.designer;
			predesign.setValue(tpro.getTUnitByDesignUnitId().getCode());
			params.put(predesign, predesign.getF());

		}

		if (tunit != null) {
			EXFIELDEnum company = EXFIELDEnum.company;
			company.setValue(tunit.getName());
			params.put(company, company.getF());

			EXFIELDEnum companyid = EXFIELDEnum.companyid;
			companyid.setValue(tunit.getProxyCode());
			params.put(companyid, companyid.getF());

		}

		return params;
	}

	/**
	 * 存储档案信息 存储档案信息、文件附加信息、档案部件关联信息
	 * 
	 * @param tdoc
	 *            文件表实体类
	 * @param tdocPro
	 *            档案工程表实体类
	 * @param tpreDesgin
	 *            初设档案表实体类
	 * @param tpro
	 *            工程表实体类
	 * @param tcom
	 *            部件表实体类
	 * @param tcomp
	 *            部件档案关联表实体类
	 * @throws Exception
	 */
	public void addArchives(TDoc tdoc, TDocProject tdocPro,
			TPreDesgin tpreDesgin, TProject tpro, TComponent tcom,
			TComponentDoc tcomp, String login_id, String local)
			throws Exception {
		if (tdoc == null || tdocPro == null || tpreDesgin == null
				|| tpro == null) {

			throw new Exception("输入参数实体类不能为空");

		}

		// 得到文件的当前最高版本
		Integer vesion = this.getFileVersion(tdoc, tpro);
		vesion = vesion + 1;
		tdoc.setVersion(vesion.toString());
		tdoc.setMetaFlag(new BigDecimal(0));
		Timestamp now = new Timestamp(System.currentTimeMillis());
		tdoc.setCreateDate(now);

		// 保持文件表信息
		super.EntityAdd(tdoc);

		String id = tdoc.getId();
		// //初设档案表信息
		tpreDesgin.setDocId(id);
		tpreDesgin.setTDoc(tdoc);

		TUnit designUnit = (TUnit) super.EntityQuery(TUnit.class, tpro
				.getTUnitByDesignUnitId().getCode());

		tpreDesgin.setTUnit(designUnit);
		super.EntityAdd(tpreDesgin);

		// 保存档案工程表信息
		TDocProjectId tDocProjectId = new TDocProjectId();
		tDocProjectId.setTDoc(tdoc);
		tDocProjectId.setTProject(tpro);
		tdocPro.setId(tDocProjectId);

		super.EntityAdd(tdocPro);
	}

	/**
	 * 得到文件的当前最高版本
	 * 
	 * @param tdoc
	 * @return
	 */
	public Integer getFileVersion(TDoc tdoc, TProject tpro) {

		String sql = "select max(to_number(doc.version)) maxid from T_DOC doc,T_Doc_Project pro,T_Project proj "
				+ " where doc.id=pro.doc_id and pro.project_id=proj.id"
				+ " and doc.doc_type_code='"
				+ tdoc.getTDocType().getCode()
				+ "'"
				+ " and doc.name='"
				+ tdoc.getName()
				+ "'"
				+ " and proj.id='" + tpro.getId() + "'";

		Integer value = super.generalDAO.queryByNativeSQLMAX(sql, "maxid");

		if (value == null) {

			value = 0;
		}

		return value;
	}

	/**
	 * 变更档案状态 设置文件状态
	 * 
	 * @param 档案实体类
	 */

	public void updateArcState() {

	}

	/**
	 * 更新档案 调用档案信息存储方法存储文件信息，若上传文件则更新文件版本， 进行文件ftp存储，调用生成预览图方法生成预览图
	 * 
	 * @param tdoc
	 *            文件表实体类
	 * @param code
	 *            文档分类表主键
	 * @param proCode
	 *            工程表主键
	 * @param location
	 *            上传文件的WEB路径
	 * @param preD
	 *            初设表实体类
	 * @param shopDraw
	 *            施工图档案表
	 * @param shopDoc
	 *            施工档案表
	 * @param recordDraw
	 *            竣工图档案表
	 * @param tdocPro
	 *            档案工程表
	 * @param tcom
	 *            部件表
	 * @param tcomp
	 *            部件档案关联表
	 * @param login_id
	 *            登录ID
	 * @throws Exception
	 */
	public void updateArchives(TDoc tdoc, String code, String proCode,
			String location, TPreDesgin preD, TShopDrawing shopDraw,
			TShopDoc shopDoc, TRecordDrawing recordDraw, TDocProject tdocPro,
			TComponent tcom, TComponentDoc tcomp, TTender tender,
			String login_id, String storeLaction, String isLocal) throws Exception {
		TProject tpro = new TProject();
		tpro = (TProject) super.EntityQuery(TProject.class, new Long(proCode));

		TDocType docType = new TDocType();
		docType = (TDocType) super.EntityQuery(TDocType.class, code);

		if (tdoc.getStatus().equals("03")) {
			if (location != null) {
				String[] doc = new String[] { tdoc.getId() };
				this.delFile(doc);

				this.tranArchives(docType, tpro, tdoc, location);
				this.tranArchivesUpdate(tdoc, preD, shopDraw, shopDoc,
						recordDraw, tdocPro, tcom, tcomp, tender);

				String sql = "select u.* from t_Project p,T_Unit u where p.owner_unit_id=u.code and p.owner_unit_id='"
						+ tpro.getTUnitByOwnerUnitId().getCode() + "'";

				List<TUnit> list = super.generalDAO.queryByNativeSQLEntity(sql,
						TUnit.class);

				TUnit tunit = list.get(0);

				Map<File, Map<Enum<?>, FileField>> files = new HashMap<File, Map<Enum<?>, FileField>>();
				files.put(new File(location), this.setArchivesIndex(tdoc, tpro,
						location, tunit));
			} else {
				this.tranArchivesUpdate(tdoc, preD, shopDraw, shopDoc,
						recordDraw, tdocPro, tcom, tcomp, tender);

			}
		}

		if (tdoc.getStatus().equals("01")) {
			if (location != null) {
				// 得到文件的当前最高版本
				Integer vesion = this.getFileVersion(tdoc, tpro);
				vesion = vesion + 1;
				tdoc.setVersion(vesion.toString());

				this.tranArchives(docType, tpro, tdoc, location);

				super.EntityAdd(tdoc);
				addArchives(tdoc, tdocPro, preD, tpro, tcom, tcomp, login_id,
						location);
			} else {
				this.tranArchivesUpdate(tdoc, preD, shopDraw, shopDoc,
						recordDraw, tdocPro, tcom, tcomp, tender);
			}
		}
	}

	/**
	 * 文件更新调用此方法 大体与文件路径类似
	 * 
	 * @param tdoc
	 * @param preD
	 * @param shopDraw
	 * @param shopDoc
	 * @param recordDraw
	 * @param tdocPro
	 * @param tcom
	 * @param tcomp
	 * @param tender
	 */
	private void tranArchivesUpdate(TDoc tdoc, TPreDesgin preD,
			TShopDrawing shopDraw, TShopDoc shopDoc, TRecordDrawing recordDraw,
			TDocProject tdocPro, TComponent tcom, TComponentDoc tcomp,
			TTender tender) {

		if (tdoc != null) {
			Session se = super.getSession();
			se.clear();
			super.EntityUpdate(tdoc);
		}
		if (preD != null) {
			super.EntityUpdate(preD);
		}

		if (shopDraw != null) {
			super.EntityUpdate(shopDraw);
		}

		if (shopDoc != null) {
			super.EntityUpdate(shopDoc);
		}

		if (recordDraw != null) {
			super.EntityUpdate(recordDraw);
		}
	}

	/**
	 * 根据文件查看版本列表 提供当前该文件的版本演变历史，包括版本号，上传人，上传时间，简介
	 * 
	 * @param 文件编码
	 * @return
	 */

	public List fileVersionList() {

		return null;
	}

	/**
	 * 迁移文件 转移文件分类位置，并更改文件所属目录为新分类位置目录
	 * 
	 * @param 文件编码
	 * @return
	 */
	public void delMoveFile(String storeLocation, String isLocal, String fileCode[], String code) throws Exception {
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);
		String fileContent = "";
		try {
			TDocType docType = new TDocType();

			docType = (TDocType) super.EntityQuery(TDocType.class, code);

			for (int i = 0; i < fileCode.length; i++) {

				TDoc doc = (TDoc) super.EntityQuery(TDoc.class, fileCode[i]);				

				String docTypeName = doc.getTDocType().getName();

				String path = doc.getPath();
				String previewPath = doc.getPreviewPath();

				if (previewPath != null) {
					String imageFileName = DesUtil.getFileName(previewPath);
					String fileName = DesUtil.getFileName(path);

					String newPath = path.substring(0, path
							.lastIndexOf(docTypeName))
							+ docType.getName() + File.separator + fileName;
					String newPreviewPath = previewPath.substring(0,
							previewPath.lastIndexOf(docTypeName))
							+ docType.getName()
							+ File.separator
							+ imageFileName;

					newPath = newPath.replaceAll("\\\\", "/");
					newPreviewPath = newPreviewPath.replaceAll("\\\\", "/");

					if (newPath.contains("/")) {
						// 创建服务器远程目录结构
						ContinueFTP ftp = ContinueFTP.getInstance();
						ftp.CreateDirecroty(DesUtil.getFilePath(newPath),
								ftp.ftpClient);
						ftp.rename(path, newPath);
						ftp.disconnect();
					}

					if (newPreviewPath.contains("/")) {
						// 创建服务器远程目录结构
						ContinueFTP ftpImage = ContinueFTP
								.getInstance();
						ftpImage.CreateDirecroty(DesUtil
								.getFilePath(newPreviewPath),
								ftpImage.ftpClient);
						ftpImage.rename(previewPath, newPreviewPath);

						ftpImage.disconnect();
					}
					doc.setTDocType(docType);
					doc.setPath(newPath);
					doc.setPreviewPath(newPreviewPath);

					super.EntityUpdate(doc);

					if (fileContent.equals("")) {
						fileContent = fileContent + doc.getName();
					} else {
						fileContent = fileContent + ";" + doc.getName();
					}
				} else {

					String fileName = DesUtil.getFileName(path);

					String newPath = path.substring(0, path
							.lastIndexOf(docTypeName))
							+ docType.getName() + File.separator + fileName;

					newPath = newPath.replaceAll("\\\\", "/");

					if (newPath.contains("/")) {
						// 创建服务器远程目录结构
						ContinueFTP ftp = ContinueFTP.getInstance();
						ftp.CreateDirecroty(DesUtil.getFilePath(newPath),
								ftp.ftpClient);
						ftp.rename(path, newPath);
						ftp.disconnect();
					}
					doc.setTDocType(docType);
					doc.setPath(newPath);
					
					//如果文件成功，删除其他地方的存储位置，该文件只有本地保存
					doc.setStoreLocation(FileStoreLocation.getStoreLocation());
					super.EntityUpdate(doc);

					if (fileContent.equals("")) {
						fileContent = fileContent + doc.getName();
					} else {
						fileContent = fileContent + ";" + doc.getName();
					}
				}
				
				
				

				// 涉及索引中对文件分类的描述更新问题
				Map<Enum<?>, FileField> map = this.setArchivesIndex(doc, null,
						null, null);
				this.updateArchiveIndex(storeLocation, isLocal, map);

			}
		} catch (Exception e) {
			logger.error("迁移文件失败:", "05", "文件:" + fileContent + "迁移文件失败", e);
			throw new Exception("迁移文件失败:" + e.getMessage());
		}
	}

	/**
	 * 永久删除文件 文件的物理删除，将文件信息从数据库中删除，并删除已上传实体。
	 * 
	 * @param 文件编码数组
	 * @return
	 */
	public void delFile(String fileCode[]) {
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);
		for (int i = 0; i < fileCode.length; i++) {

			TDoc doc = (TDoc) super.EntityQuery(TDoc.class, fileCode[i]);

			String path = doc.getPath();
			String previewPath = doc.getPreviewPath();

			ContinueFTP ftp = null;
			try {
				ftp = ContinueFTP.getInstance();

				ftp.deleteFile(path);
				ftp.deleteFile(previewPath);

			} catch (NumberFormatException e) {
				logger.warn(e);
			} catch (IOException e) {
				logger.warn(e);
			} finally {
				try {
					if (ftp != null)
						ftp.disconnect();
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			}

			super.EntityDel(doc);

		}
	}

	/**
	 * 删除档案信息 删除档案表信息，文件信息，档案附加信息，档案部件关联信息，ftp文件内容
	 * 
	 * @param 文件编码数组
	 * @return
	 */
	public void delArchives(String fileCode[]) throws Exception {

		for (int i = 0; i < fileCode.length; i++) {
			TDoc doc = (TDoc) EntityQuery(TDoc.class, fileCode[i]);
			// 添加删除ftp缩略图
			// 获取文档的所属业主单位
			TProject project = getTProjectBydocId(doc.getId());
			doc.setProjectid(project.getId() + "");
			DeleteFileStatus dfs = ContinueFTP.getInstance()
					.deleteFile(doc.getPreviewPath());
			if (DeleteFileStatus.Delete_Remote_Success.name()
					.equals(dfs.name())) {
				super.logger.getLogger(ArchivesServiceImpl.class).warn(
						"缩略图删除成功!");
			} else {
				super.logger.getLogger(ArchivesServiceImpl.class).warn(
						"缩略图删除失败!");
			}

			Set<TDocProject> tdoc = doc.getTDocProjects();

			Iterator<TDocProject> it = tdoc.iterator();
			while (it.hasNext()) {// hasNext()判断有没有下一个元素
				TDocProject st = it.next();
				super.EntityDel(st);
			}

			TPreDesgin preD = (TPreDesgin) EntityQuery(TPreDesgin.class,
					fileCode[i]);
			if (preD != null) {
				super.EntityDel(preD);
			}

			TShopDrawing shopDraw = (TShopDrawing) EntityQuery(
					TShopDrawing.class, fileCode[i]);
			if (shopDraw != null) {
				super.EntityDel(shopDraw);
			}

			TShopDoc shopDoc = (TShopDoc) EntityQuery(TShopDoc.class,
					fileCode[i]);
			if (shopDoc != null) {
				super.EntityDel(shopDoc);
			}

			TRecordDrawing recordDraw = (TRecordDrawing) EntityQuery(
					TRecordDrawing.class, fileCode[i]);
			if (recordDraw != null) {
				super.EntityDel(recordDraw);
			}
		}
		this.delFile(fileCode);
	}

	/**
	 * 文档分类树状列表 T_Doc_Type 能够根据工程类型，工程阶段进行过滤
	 * 
	 * @param projectType
	 *            工程类型
	 * @param projectMoment
	 *            工程阶段
	 * @return
	 */

	public List tdocTypeList(String projectType, String projectMoment) {

		return null;
	}

	/**
	 * 根据文件分类生成文件路径
	 * 
	 * @param 文件分类位置
	 * @return
	 */
	public String filePathProduce(String docTypeName, TProject tpro) {
		Configurater config = Configurater.getInstance();
		String pathLanguage = config.getConfigValue("pathlanguage");
		
		Long parentId = tpro.getParentId();
		String childName = tpro.getName();

		String unitId = tpro.getTUnitByOwnerUnitId().getCode();

		TUnit unit = (TUnit) super.EntityQuery(TUnit.class, unitId);

		String unitName = unit.getName();

		TProject proj = new TProject();
		String parentName = "";
		if (parentId != null) {
			proj = (TProject) super.EntityQuery(TProject.class, parentId);
			parentName = proj.getName();
		}
		String path = "";
		
		//汉字路径
		if(pathLanguage.toUpperCase().equals("HZ")){
			if (parentName.equals("")) {
				path = File.separator + FileUtil.folderPathFilter(unitName)
						+ FileUtil.folderPathFilter(parentName) + File.separator
						+ FileUtil.folderPathFilter(childName) + File.separator
						+ FileUtil.folderPathFilter(docTypeName);
			} else {
				path = File.separator + FileUtil.folderPathFilter(unitName)
						+ File.separator + FileUtil.folderPathFilter(parentName)
						+ File.separator + FileUtil.folderPathFilter(childName)
						+ File.separator + FileUtil.folderPathFilter(docTypeName);
			}
		//拼音路径
		}else if (pathLanguage.toUpperCase().equals("PY")){
			if (parentName.equals("")) {
				path = File.separator + FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(unitName))
						+ FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(parentName)) + File.separator
						+ FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(childName)) + File.separator
						+ FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(docTypeName));
			} else {
				path = File.separator + FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(unitName))
						+ File.separator + FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(parentName))
						+ File.separator + FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(childName))
						+ File.separator + FileUtil.folderPathFilter(Pinyin4jUtil.getPinYin(docTypeName));
			}
		}
		return path;
	}

	/**
	 * 根据guid路径
	 * 
	 * @param 文件分类位置
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	private String fileGuidName(String fileName)
			throws UnsupportedEncodingException {

		UUID uuid = UUID.randomUUID();

		String strUuid = uuid.toString();

		fileName = strUuid
				+ Configurater.getInstance().getConfigValue("fileNameSplit")
				+ fileName;

		return fileName;
	}

	/**
	 * 生成文件预览图 根据不同的文件格式生成预览图
	 * 
	 * @param 文件文件格式
	 * @return
	 */

	public OutputStream filePreview(String fileFormat) {

		return null;
	}

	/**
	 * 获取预览图 根据文件编码获取预览图文件地址
	 * 
	 * @param 文件编码
	 * @return
	 */

	public String filePreviewPath(String fileCode) {

		return null;
	}

	/**
	 * 获取文档分类子类列表
	 */
	public List<TDocType> docTypeList(String parentCode) {
		String sql = "";
		if ("".equals(parentCode) || parentCode == null) {
			sql = "SELECT * FROM T_DOC_TYPE WHERE PARENT_CODE IS NOT NULL";
		} else {
			sql = "SELECT * FROM T_DOC_TYPE WHERE PARENT_CODE='" + parentCode
					+ "'";
		}

		List<TDocType> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDocType.class);

		return list;
	}

	/**
	 * 获取文档分类子类列表,在结果页
	 */
	public List<TDocType> docTypeResultList(String code, String parentCode) {
		String sql = "";
		if ("".equals(parentCode) || parentCode == null) {
			sql = "SELECT * FROM T_DOC_TYPE WHERE CODE LIKE  '" + code
					+ "%' and  PARENT_CODE IS NOT NULL ";
		} else {
			sql = "SELECT * FROM T_DOC_TYPE WHERE CODE LIKE  '" + code
					+ "%' and  PARENT_CODE = '" + parentCode + "'";
		}

		List<TDocType> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDocType.class);

		return list;
	}

	/**
	 * 检索文件
	 * @throws InterruptedException 
	 */
	public Set<File> addArchiveIndex(Map<File, Map<Enum<?>, FileField>> files,
			String storeLocation, String isLocal) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, URISyntaxException, BadLocationException, InterruptedException {

		return this.indexingArchive(files, storeLocation, isLocal, false);
	}

	/**
	 * 更新检索文件
	 * @throws InterruptedException 
	 */
	private void updateArchiveIndex(Map<File, Map<Enum<?>, FileField>> files,
			String storeLocation, String isLocal) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, URISyntaxException, BadLocationException, InterruptedException {

		this.indexingArchive(files, storeLocation, isLocal, true);
	}

	/**
	 * 更新检索文件参数
	 * 
	 * @param map
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws LockObtainFailedException
	 * @throws CorruptIndexException
	 */
	public void updateArchiveIndex(String storeLocation, String isLocal, Map<Enum<?>, FileField> params)
			throws URISyntaxException, CorruptIndexException,
			LockObtainFailedException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, Exception {
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);

		String indexDir = FileDirUtils.getRealPathByUnitId("doc_write_Dir", storeLocation, isLocal);
		// 检索文件存放目录

		boolean reload = false;
		IIndexWriter writer = null;
		File iFolder = new File(indexDir);

		try {
			writer = IndexFactory.getInstance().getIndexWriter(iFolder,
					AnalyzerFactory.getInstance().getAnalyzer());

			logger.info("对文档的属性文件进行索引更新 "
					+ params.get(DOCFIELDEnum.title).getContent());
			writer.updateDocument(params);
		} finally {
			// 关闭索引文件
			if (writer != null)
				IndexFactory.getInstance().close(iFolder);

			// 是否实时加载检索文件
			String realTime = Configurater.getInstance().getConfigValue(
					"realtime_Reaload");
			if (reload && "true".equals(realTime)) {
				File readDir = FileDirUtils.getReadFileByWriteFile(iFolder);
				IndexSearchManager.getInstance().releaseSearch(readDir);
				if(IndexSearchManager.getInstance().initReadFolder(iFolder, readDir)){
					IndexSearchManager.getInstance().reloadSingleFile(readDir);
				}
			}
		}
	}

	public void deleteArchiveIndex(String storeLocation, String isLocation, String docid)
			throws URISyntaxException, CorruptIndexException,
			LockObtainFailedException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
			InvocationTargetException, NoSuchMethodException {
		String indexDir = FileDirUtils.getRealPathByUnitId("doc_write_Dir", storeLocation, isLocation);
		boolean reload = false;
		IIndexWriter writer = null;
		File iFolder = new File(indexDir);
		
		try {
			writer = IndexFactory.getInstance().getIndexWriter(iFolder,
					AnalyzerFactory.getInstance().getAnalyzer());

			DOCFIELDEnum id = DOCFIELDEnum.docid;
			id.setValue(docid);
			writer.deleteDocument(id);
			reload = true;
		} finally {
			// 关闭索引文件
			if (writer != null)
				writer.closeWriter();

			// 是否实时加载检索文件
			String realTime = Configurater.getInstance().getConfigValue(
					"realtime_Reaload");
			if (reload && "true".equals(realTime)) {
				File readDir = FileDirUtils.getReadFileByWriteFile(iFolder);
				IndexSearchManager.getInstance().releaseSearch(readDir);
				if(IndexSearchManager.getInstance().initReadFolder(iFolder, readDir)){
					IndexSearchManager.getInstance().reloadSingleFile(readDir);
				}
			}
		}
	}

	/**
	 * 检索文件
	 * 
	 * @param files
	 * @param unitCode
	 *            存储位置
	 * @param isLocal
	 * @param update
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws URISyntaxException
	 * @throws BadLocationException
	 * @throws InterruptedException 
	 */
	private Set<File> indexingArchive(Map<File, Map<Enum<?>, FileField>> files,
			String storeLocation, String isLocal, boolean update)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, URISyntaxException, BadLocationException, InterruptedException {

		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);

		String docDir = FileDirUtils.getRealPathByUnitId("doc_write_Dir",
				storeLocation, isLocal);
		String contentDir = FileDirUtils.getRealPathByUnitId("doc_content_Dir",
				storeLocation, isLocal);
		String readDir = FileDirUtils.getRealPathByUnitId("doc_read_Dir",
				storeLocation, isLocal);

		boolean reload = false;
		IIndexWriter writer = null;
		File iFolder = new File(docDir);
		Set<File> falses = new java.util.HashSet<File>();

		try {
			writer = IndexFactory.getInstance().getIndexWriter(iFolder,
					AnalyzerFactory.getInstance().getAnalyzer());

			File contents = new File(contentDir);
			File readFile = new File(docDir);
			File writeFile = new File(readDir);
			
			if (!contents.exists())
				contents.mkdirs();
			
			if (!readFile.exists())
				contents.mkdirs();
			
			if (!writeFile.exists())
				contents.mkdirs();

			Set<File> keys = files.keySet();

			for (File file : keys) {
				Map<Enum<?>, FileField> params = files.get(file);

				try {
					if (update) {
						writer.updateDocument(file, contents, params);
					} else {
						writer.addDocument(file, contents, params);
					}
 
					reload = true;
				} catch (Exception ex) {
					logger.warn("将文档增加到索引中失败或者根据文档ID更新文档失败:", ex);
					falses.add(file);
				}
			}
		} finally {
			// 关闭索引文件
			if (writer != null)
				IndexFactory.getInstance().close(iFolder);

			// 是否实时加载检索文件
			String realTime = Configurater.getInstance().getConfigValue(
					"realtime_Reaload");
			if (reload && "true".equals(realTime)) {
				// 释放read
				IndexSearchManager.getInstance().releaseSearch(
						new File(readDir));
				if (IndexSearchManager.getInstance().initReadFolder(iFolder,
						new File(readDir))) {
					IndexSearchManager.getInstance().reloadSingleFile(
							new File(readDir));
				}
			}
			for (File file : falses) {
				files.remove(file);
			}
		}

		return falses;
	}

	/**
	 * 获取文档分类大类列表
	 */
	public List<TDocType> docTypeList(String id, String flag) {
		String sql = "";

		if ("".equals(id) || id == null) {
			sql = "SELECT * FROM T_DOC_TYPE WHERE PARENT_CODE IS NULL";
		} else {
			sql = "SELECT * FROM T_DOC_TYPE WHERE CODE LIKE  '" + id
					+ "%'  and PARENT_CODE IS NULL";
		}

		List<TDocType> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDocType.class);

		return list;
	}

	/**
	 * 基本检索分页查询
	 * 
	 * @param map
	 * @param firstResult
	 *            开始记录行数
	 * @param maxResults
	 *            结束记录行数
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> queryTdocTypeList(Object[] obj, int firstResult,
			int maxResults, TableBean table) throws Exception {

		Long count = super.generalDAO.getResultCount("archieveTdocType", obj);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"archieveTdocType", obj, firstResult, maxResults);

	}

	public List filePathProduce(String fileType) {
		// TODO Auto-generated method stub
		return null;
	}

	// 查询角色信息
	public List<TRole> TRoleList() {

		String sql = "SELECT * FROM T_ROLE ";

		List<TRole> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TRole.class);

		return list;
	}

	/**
	 * 得到文件最后修改时间
	 */
	public Date getDocLastUpdate(String loginId, String logType) {
		String sql = "select * from t_operate_log t where t.type='" + logType
				+ "' and operator='" + loginId
				+ "' and rownum=1 order by t.operate_time desc";
		List list = super.generalDAO.queryByNativeSQLEntity(sql,
				TOperateLog.class);
		if (list.size() == 0) {
			return null;
		} else {
			return ((TOperateLog) list.get(0)).getOperateTime();
		}
	}

	// 重建索引 查询
	public List<Object[]> creatIndexingTDoc(Object[] obj) {
		List<Object[]> list = super.generalDAO.getResult("archieveIndexing",
				obj);

		return list;
	}

	// 批量查询TDoc
	public List<Object> batchQueryTDoc(String flag) {
		String sql = "select * from t_doc t where t.meta_flag=" + flag;
		List<Object> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDoc.class);
		return list;
	}

	// 生成缩略图 查询
	public List<Object[]> creatThumbnailsTDoc(Object[] obj, String flag) {
		List<Object[]> list = null;
		if ("1".equals(flag)) {
			list = super.generalDAO.getResult("archieveThumbnails1", obj);
		} else {
			list = super.generalDAO.getResult("archieveThumbnails2", obj);
		}
		return list;
	}

	/**
	 * 根据分类编码查询doc
	 * 
	 * @param docTypeCode
	 *            分类编码
	 * @return
	 */
	public List<TDoc> getDocByDocTypeCode(String docTypeCode) {
		String sql = "select * from t_doc t where t.doc_type_code='"
				+ docTypeCode + "'";
		List<TDoc> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TDoc.class);
		return list;
	}

	public String removeDocType(String[] codeList) {
		String returnValue = "";
		for (int i = 0; i < codeList.length; i++) {
			String eachCode = codeList[i];
			TDocType type = new TDocType();
			type.setParentCode(eachCode);
			List<Object> listdoc = super.EntityQuery(type);
			if (listdoc.size() != 0) {
				returnValue = "true";
			} else {
				TDocType doctype = (TDocType) super.EntityQuery(TDocType.class,
						eachCode);
				super.delEntity(doctype);
				returnValue = "success";

			}
		}
		return returnValue;
	}

	/**
	 * 查询文档
	 */
	public List queryDoc(Map<String, Object> map) {
		return super.generalDAO.getResult("docSearch", map);
	}

	/**
	 * 根据文档ID查找工程
	 * 
	 * @param docId
	 * @return
	 */
	public TProject getTProjectBydocId(String docId) {
		List<TProject> list = super.generalDAO.getResult("updateArchivesIndex",
				new Object[] { docId });
		if(null != list && list.size() > 0){
			list.get(0).getTUnitByOwnerUnitId().getName();
			list.get(0).getTUnitByDesignUnitId().getName();
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新索引状态
	 * 
	 * @param folder
	 * @param docids
	 * @throws Exception
	 */
	public void updateIndex(String isLocal, String docid, String storeLocation)
			throws Exception {
		TDoc tdoc = (TDoc) super.EntityQuery(TDoc.class, docid);
		TProject tpro = this.getTProjectBydocId(tdoc.getId());

		this.downloadFileToIndexUpdate(isLocal, tdoc, tpro, storeLocation,true);
	}

	/**
	 * 同步调用更新索引状态
	 * 
	 * @param isLocal
	 * @param tdoc
	 * @param tpro
	 * @param unit
	 * @throws Exception
	 */
	public void updateIndex(String isLocal, TDoc tdoc, TProject tpro,
			String storeLocation,boolean isIndex) throws Exception {
		this.downloadFileToIndexUpdate(isLocal, tdoc, tpro, storeLocation, isIndex);
	}

	/**
	 * 
	 * @param filePath
	 *            文件路径
	 * @param tdoc
	 *            文件实体
	 * @param tpro
	 *            工程实体
	 * @param unit
	 * @param unitCode
	 * @param isLocal
	 * @throws Exception
	 * @throws IOException
	 */
	private void downloadFileToIndexUpdate(String isLocal, TDoc tdoc,
			TProject tpro, String storeLocation, boolean isIndex) throws Exception, IOException {
		Map<File, Map<Enum<?>, FileField>> files = new HashMap<File, Map<Enum<?>, FileField>>();
		String tempDir = Configurater.getInstance().getConfigValue("temp");

		File tempFolder = new File(tempDir);

		if (!tempFolder.isDirectory()) {
			tempFolder.mkdirs();
		}

		ContinueFTP ftpUtil = ContinueFTP.getInstance();
		String remotePath = tdoc.getPath();

		String local = tempFolder.getAbsolutePath() + File.separator
				+ tdoc.getName() + "." + tdoc.getSuffix();
		File file = new File(local);
		ftpUtil.download(remotePath, local);

		if (file.exists() && file.canRead()) {

			String mimeType = Configurater.getInstance().getConfigValue("mime",
					FilenameUtils.getExtension(file.getName()));
			if (mimeType == null) {
				mimeType = Configurater.getInstance().getConfigValue("mime",
						"*");
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
			if(isIndex){
				files.put(new File(dest), this.setArchivesIndex(tdoc, tpro, null,
						tpro.getTUnitByOwnerUnitId()));
				this.updateArchiveIndex(files, storeLocation, isLocal);
			}
		}
	}

	public List queryDocIsArchives(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return super.generalDAO.getResult("docSearchIsArchives", map);
	}

	public void saveOrUpdateSynDoc(TSynDoc doc) throws Exception {
		TSynDoc docTemp = (TSynDoc) super.generalDAO.findByID(TSynDoc.class,
				doc.getId());
		if (null != docTemp) {
			docTemp.setName(doc.getName());
			docTemp.setPath(doc.getPath());
			docTemp.setPreviewPath(doc.getPreviewPath());
			docTemp.setSuffix(doc.getSuffix());
			docTemp.setTSynProject(doc.getTSynProject());
			//去除重复值
			String store = FileStoreLocation.delRepeated(doc.getStoreLocation());
			docTemp.setStoreLocation(store);
			super.EntityUpdate(docTemp);
		} else {
			super.EntityAdd(doc);
		}
	}

	public void saveOrUpdateSynProject(TSynProject project) {
		TSynProject projectTemp = (TSynProject) super.generalDAO.findByID(
				TSynProject.class, project.getCode());
		if (null != projectTemp) {
			projectTemp.setApproveUnitid(project.getApproveUnitid());
			projectTemp.setGiveoutUnitid(project.getGiveoutUnitid());
			projectTemp.setOwnerUnitId(project.getOwnerUnitId());
			projectTemp.setTSynDocs(project.getTSynDocs());
			projectTemp.setName(project.getName());
			super.EntityUpdate(projectTemp);
		} else {
			super.EntityAdd(project);
		}
	}

	public List<TDoc> queryDocByIds(String[] ids) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("in", ids);
		return (List<TDoc>) super.generalDAO.getResult("docSearcherByIds",
				param);
	}

	public void deleteSynDoc(String docId) {
		TSynDoc docTemp = (TSynDoc) super.generalDAO.findByID(TSynDoc.class,
				docId);
		if (docTemp != null)
			super.EntityDel(docTemp);
	}

	/**
	 * 按没有产生索引的工程查询文档
	 * 
	 * @return
	 */
	public List<TDoc> getDocByNoIndexProject(Object[] projectIds) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("in", projectIds);
		return (List<TDoc>) super.generalDAO.getResult(
				"getDocByNoIndexProject", param);
	}

	/************************************* 数据同步 ***************************************************/

	/**
	 * 迁移索引, 数据同步时使用
	 * 
	 * @param oldPath
	 *            旧路径
	 * @param newPath
	 *            新路径
	 * @param doc
	 *            doc对象
	 * @param fid
	 *            文件id
	 * @param pro
	 *            工程对象
	 * @param storeLocation
	 *            索引存储位置
	 * @param tempDir
	 *            临时目录
	 * @throws Exception 
	 */
	public void removeIndex(String isLocal, String oldPath, String newPath,
		TDoc doc,TProject pro, String storeLocation, boolean isIndex) throws Exception{
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);
		try{
			doc.setStatus("01");			
			doc.setPath(oldPath);
			newPath = newPath.replaceAll("\\\\", "/");
			if (newPath.contains("/")) {
				// 创建服务器远程目录结构
				ContinueFTP ftpm = ContinueFTP.getInstance();
				ftpm.CreateDirecroty(DesUtil.getFilePath(newPath),
						ftpm.ftpClient);
				ftpm.rename(oldPath, newPath);
				ftpm.disconnect();
				
			}
			StoreFileReceiveID.createInstance().addSuccessFileID(doc.getId());
			doc.setPath(newPath);   
			this.updateIndex(isLocal,doc, pro, storeLocation,isIndex);
		}catch(Exception e){
			StoreFileReceiveID.createInstance().addSuccessFileID(doc.getId());
			logger.error("迁移文件失败:", "05", "文件:" + doc.getName() + "迁移文件失败", e);
			throw new Exception("迁移文件失败:" + e.getMessage());
		}
	}

	/**
	 * 创建索引 数据同步时使用
	 * 
	 * @param unit
	 *            要传到的用户单位 ，String 对象
	 * @param ftpDoc
	 *            ftp的document对象
	 * @param tempDir
	 *            临时目录
	 * @param doc
	 *            doc对象
	 * @param pro
	 *            projec对象
	 * @param doctype
	 *            doctype对象
	 * @param file
	 *            文件
	 * @param userUnit
	 *            用户所在的单位
	 * @param tofileName
	 * @throws Exception
	 */
	public void archivesIndex(String unit, String tempDir, TDoc doc,
			TProject pro, String storeLocation,
			String isLocal, boolean isIndex) throws Exception {
		Logger logger = super.logger.getLogger(ArchivesServiceImpl.class);
		Configurater config = Configurater.getInstance();
		String[] ftpInfo = FtpXmlUtils.getFTPInfo(unit);
		ContinueFTP ftp = ContinueFTP.getDownLoadInstance(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
		if (ftp != null) {
			File tempFolder = new File(tempDir);
			if (!tempFolder.isDirectory()) {
				tempFolder.mkdirs();
			}
			Map<File, Map<Enum<?>, FileField>> files = new HashMap<File, Map<Enum<?>, FileField>>();
			Map<File, TDoc> docs = new HashMap<File, TDoc>();

			String remotePath = doc.getPath();
			String local = tempFolder.getAbsolutePath() + File.separator
					+ doc.getName() + "." + doc.getSuffix();
			File sourceFile = new File(local);
			File decryptFile = null;
			
			/**
			 * 如果文件出现下列三种情况，做失败处理
			 * 远程文件不存在
			 * 断点续传不存在
			 * 下载新文件失败
			 */
			DownloadStatus downStatus = ftp.download(remotePath, local);
			if(downStatus.equals(DownloadStatus.Remote_File_Noexist) ||
					downStatus.equals(DownloadStatus.Download_From_Break_Failed) ||
					downStatus.equals(DownloadStatus.Download_New_Failed)){
				StoreFileReceiveID.createInstance().addFailFileID(doc.getId());
				return;
			}
			
			if (sourceFile.exists() && sourceFile.canRead()) {
				String mimeType = Configurater.getInstance().getConfigValue(
						"mime", doc.getSuffix());
				if (mimeType == null) {
					mimeType = config.getConfigValue(
							"mime", "*");
				}
				String dest = local;
				// 非图片需要解密
				if (mimeType.indexOf("image") == -1) {
					dest = Configurater.getInstance().getConfigValue("decrypt");
					File destPathFolder = new File(sourceFile.getParentFile(), dest);
					if (!destPathFolder.isDirectory())
						destPathFolder.mkdirs();
					dest = destPathFolder.getAbsolutePath() + File.separator
							+ sourceFile.getName();
					DesUtil.decrypt(local, dest);
				}
				
				decryptFile = new File(dest);
				doc.setStatus("01");
				docs.put(decryptFile, doc);
				files.put(decryptFile, this.setArchivesIndex(doc, pro, dest, pro.getTUnitByOwnerUnitId()));
				
				// 上传到本地服务器
				try{
					ContinueFTP.getInstance().upload(local, doc.getPath());
					StoreFileReceiveID.createInstance().addSuccessFileID(doc.getId());
				}catch(Exception e){
					logger.warn(e.getMessage());
					StoreFileReceiveID.createInstance().addFailFileID(doc.getId());
				}
			}
			if(isIndex){
			    this.addArchiveIndex(files, storeLocation,isLocal);
			}
			// 不是同一文件，即存在解密文件，清除未解密文件
			if (!sourceFile.equals(decryptFile)){
				sourceFile.delete();
			}
			if(decryptFile.exists()){
				decryptFile.delete();
			}
		}else{
			logger.warn("国网FTP连接失败！");
		}
	}
	
	

}
