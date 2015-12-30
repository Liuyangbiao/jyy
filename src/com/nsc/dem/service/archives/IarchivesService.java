package com.nsc.dem.service.archives;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.BadLocationException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

import com.nsc.base.index.FileField;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.archives.TSynProject;
import com.nsc.dem.bean.profile.TRole;
import com.nsc.dem.bean.project.TComponent;
import com.nsc.dem.bean.project.TComponentDoc;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.project.TPreDesgin;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.project.TRecordDrawing;
import com.nsc.dem.bean.project.TShopDoc;
import com.nsc.dem.bean.project.TShopDrawing;
import com.nsc.dem.bean.project.TTender;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.base.IService;

public interface IarchivesService extends IService {

	/**
	 * 查询档案基本信息列表
	 * 
	 * @param 档案实体类
	 * @return
	 */
	public List<Object> archivesInfoList();

	/**
	 * 查看档案详细信息
	 * 
	 * @param 档案主键
	 * @return 档案实体类
	 */
	public List<Object> archivesInfo();

	/**
	 * 录入档案 将版本信息置为1，初始化档案信息，调用档案信息存储方法。 对档案信息进行批量存储。调用生成预览图方法生成预览图
	 * 
	 * @param 档案实体类
	 */

	public void insertArchives(TDoc tdoc, TDocProject tdocPro,
			TPreDesgin tpreDesgin, TProject tpro, TComponent tcom,
			TComponentDoc tcomp, TDocType docType, String login_id, String local)
			throws Exception;

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
	 */
	public void addArchives(TDoc tdoc, TDocProject tdocPro,
			TPreDesgin tpreDesgin, TProject tpro, TComponent tcom,
			TComponentDoc tcomp, String login_id, String local)
			throws Exception;

	/**
	 * 变更档案状态 设置文件状态
	 * 
	 * @param 档案实体类
	 */

	public void updateArcState();

	/**
	 * 更新档案 调用档案信息存储方法存储文件信息，若上传文件则更新文件版本， 进行文件ftp存储，调用生成预览图方法生成预览图
	 * 
	 * @param tdoc
	 *            文件表实体类
	 * @param code
	 *            文档分类表主键
	 * @param proCode
	 *            工程表主键
	 * @param local
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
			String login_id, String storeLaction, String isLocal) throws Exception;

	/**
	 * 根据文件查看版本列表 提供当前该文件的版本演变历史，包括版本号，上传人，上传时间，简介
	 * 
	 * @param 文件编码
	 * @return
	 */

	public List<Object> fileVersionList();

	/**
	 * 迁移文件 转移文件分类位置，并更改文件所属目录为新分类位置目录
	 * 
	 * @param 文件编码
	 *            、文件分类位置
	 * @return
	 */

	public void delMoveFile(String storeLocation, String isLocal, String fileCode[], String code) throws Exception;

	/**
	 * 永久删除文件 文件的物理删除，将文件信息从数据库中删除，并删除已上传实体。
	 * 
	 * @param 文件编码数组
	 * @return
	 */

	public void delFile(String fileCode[]);

	/**
	 * 删除档案信息 删除档案表信息，文件信息，档案附加信息，档案部件关联信息，ftp文件内容
	 * 
	 * @param 文件编码数组
	 * @return
	 * @throws Exception
	 */

	public void delArchives(String fileCode[]) throws Exception;

	/**
	 * 文档分类树状列表 T_Doc_Type 能够根据工程类型，工程阶段进行过滤
	 * 
	 * @param projectType
	 *            工程类型
	 * @param projectMoment
	 *            工程阶段
	 * @return
	 */

	public List<Object> tdocTypeList(String projectType, String projectMoment);

	/**
	 * 根据文件分类生成文件路径
	 * 
	 * @param 文件分类位置
	 * @return
	 */
	public List<Object> filePathProduce(String fileType);

	/**
	 * 生成文件预览图 根据不同的文件格式生成预览图
	 * 
	 * @param 文件文件格式
	 * @return
	 */

	public OutputStream filePreview(String fileFormat);

	/**
	 * 获取预览图 根据文件编码获取预览图文件地址
	 * 
	 * @param 文件编码
	 * @return
	 */

	public String filePreviewPath(String fileCode);

	/**
	 * 创建检索文件
	 * 
	 * @param files
	 * @param folder
	 *            设置文件夹，则将创建的索引输出到指定文件夹中，否则统一存放到默认路径下
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws URISyntaxException
	 * @throws BadLocationException
	 * @throws InterruptedException 
	 */
	public Set<File> addArchiveIndex(Map<File, Map<Enum<?>, FileField>> files,
			String storeLocation, String isLocal) throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, URISyntaxException,
			BadLocationException, InterruptedException;

	/**
	 * 获取文档分类树状列表
	 */
	public List<TDocType> docTypeList(String docType);

	public List<TDocType> docTypeList(String id, String flag);

	/**
	 * 获取文档分类子类列表,在结果页
	 */
	public List<TDocType> docTypeResultList(String code, String parentCode);

	/**
	 * 根据文件分类生成文件路径
	 * 
	 * @param 文件分类位置
	 * @return
	 */
	// public String filePathProduce(String docType,Long id);
	public String filePathProduce(String docTypeName, TProject tpro);

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
			int maxResults, TableBean table) throws Exception;

	// 查询角色信息
	public List<TRole> TRoleList();

	/**
	 * 全文解索设置
	 * 
	 * @param tdoc
	 * @param tpro
	 * @param local
	 * @param tunit
	 *            业主单位
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Map<Enum<?>, FileField> setArchivesIndex(TDoc tdoc, TProject tpro,
			String local, TUnit tunit) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException;

	/**
	 * 
	 * @param queryString
	 * @param clazz
	 * @return
	 */
	public Date getDocLastUpdate(String loginId, String logType);

	public List<Object[]> creatIndexingTDoc(Object[] obj);

	public void deleteArchiveIndex(String storeLocation, String isLocation, String docid)
			throws URISyntaxException, CorruptIndexException,
			LockObtainFailedException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IOException,
			InvocationTargetException, NoSuchMethodException;

	public List<Object> batchQueryTDoc(String flag);

	// 须要生成缩略图的文档
	public List<Object[]> creatThumbnailsTDoc(Object[] obj, String flag);

	/**
	 * 根据分类编码查询doc
	 * 
	 * @param docTypeCode
	 *            分类编码
	 * @return
	 */
	public List<TDoc> getDocByDocTypeCode(String docTypeCode);

	/**
	 * 删除分类
	 * 
	 * @param codeList
	 * @return
	 */
	public String removeDocType(String[] codeList);

	/**
	 * 查询文档
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryDoc(Map<String, Object> map);

	/**
	 * 查询已归档文档
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryDocIsArchives(Map<String,Object> map);
	
	/**
	 * 根据文档ID查找工程
	 * 
	 * @param docId
	 * @return
	 */
	public TProject getTProjectBydocId(String docId);

	/**
	 * 更新索引状态
	 * 
	 * @param folder
	 * @param docids
	 */
	public void updateIndex(String isLocal, String docid, String storeLocation)
			throws Exception;

	public void updateIndex(String isLocal, TDoc tdoc, TProject tpro,
			String storeLocation, boolean isIndex) throws Exception;

	public void tranArchives(TDocType docType, TProject tpro, TDoc tdoc,
			String local) throws Exception;
	
	/*
	 * 保存或者更新同步文档
	 */
	public void saveOrUpdateSynDoc(TSynDoc doc) throws Exception ;
	
	public void saveOrUpdateSynProject(TSynProject project);
	
	/**
	 * 按ID查询文档
	 * @param ids
	 * @return
	 */
	public List<TDoc> queryDocByIds(String[] ids);

	/**
	 * 删除文件
	 * @param fid
	 */
	public void deleteSynDoc(String docId);

	public void removeIndex(String isLocal, String oldPath, String newPath,
			TDoc doc, TProject pro, String storeLocation, boolean isIndex) throws Exception;

	public void archivesIndex(String unit, String tempDir, TDoc doc,
			TProject pro,  String storeLocation,
			String isLocal, boolean isIndex) throws Exception;
	
	/**
	 * 按没有产生索引的工程查询文档
	 * @return
	 */
	public List<TDoc> getDocByNoIndexProject(Object[] projectIds);
}
