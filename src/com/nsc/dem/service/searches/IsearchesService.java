package com.nsc.dem.service.searches;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.LockObtainFailedException;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.bean.WholeSearchDoc;
import com.nsc.dem.action.system.TMenu;
import com.nsc.dem.bean.archives.TSynDoc;
import com.nsc.dem.bean.archives.TUserQuery;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.base.IService;

public interface IsearchesService extends IService {

	/**
	 * 文件检索 根据参数（包含节点信息或查询参数等）查询并返回文件概要信息列表 （可包含已定义的附加属性信息）
	 * 
	 * @param T_Doc
	 *            文件实体类
	 * @return
	 */
	public List<Object> fileSearches();

	/**
	 * 全文检索 通过关键字进行全文检索，返回检索结果列表 （包含文档结构层次，分类层次信息，上传者，文件片段（关键字高亮）
	 * 
	 * @param keyword
	 *            关键字
	 * @return
	 */
	public List<Object> wholeSearches(String keyword);

	/**
	 * 指标检索 通过指标参数查询并返回文件概要信息列表
	 * 
	 * @param T_Doc
	 *            文件实体类
	 * @return 文件实体类
	 */
	public List<Object> targetSearches();

	/**
	 * 获取文件下载地址 根据文件id数组获取待下载文件ftp地址列表
	 * 
	 * @param id
	 * @return
	 */
	public List<Object> fileDownloadList(String[] id);

	/**
	 * 通过树ID获取子节点 sign=true 代表是目录树的根节点
	 * 
	 * @param id
	 * @param sign
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List treeDefList(Long id, boolean sign, Map<String, Object> map);

	public List<Object[]> queryBasicList(Map<String, Object> map, int firstResult,
			int maxResults, TableBean count) throws Exception;

	/**
	 * 全文检索文件
	 * 
	 * @param file
	 * @param params
	 * @param roleId
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public List<WholeSearchDoc> searchArchive(String queryStr,
			Map<Enum<?>, Object> params, Map<?, Object> filters, TableBean tb,
			boolean highLight, String untiId) throws URISyntaxException,
			CorruptIndexException, LockObtainFailedException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException, ParseException,
			InvalidTokenOffsetsException, InvocationTargetException,
			NoSuchMethodException;
	
	public List<String> searchSuggest(String queryStr,
			Map<Enum<?>, Object> params, Map<?, Object> filters,
			boolean highLight, String untiId) throws URISyntaxException,
			CorruptIndexException, LockObtainFailedException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException, ParseException,
			InvalidTokenOffsetsException, InvocationTargetException,
			NoSuchMethodException;

	public List<Object[]> documentInfo(String id);

	public List<TMenu> quarryUserTreeList(String userId,String roleId);

	public List<TUserQuery> queryByLoginId(String loginId);

	public List<TUserQuery> queryByLoginIdAndName(String loginId, String name);



	/**
	 * 根据用户ID查询该用 户距上次登录时间新增的档案数
	 * @param loginId 登录ID
	 * @param unitId 单位ID
	 * @return 档案数
	 */
	public int selectInsertDocCount(String beginTime, String unitId);

	/**
	 * 根据用户ID查询该用户距上次登录时间新增的工程数
	 * @param loginId 登录ID
	 * @param unitId 单位ID
	 * @return 工程数
	 */
	public int selectInsertProjectCount(String beginTime, String unitId);

	/**
	 * 根据用户ID查询最近浏览的文件
	 * @param loginId 登录ID
	 * @return 最近浏览的文件
	 */
	public List<TOperateLog> selectBrowseOperateLog(String loginId);
	
	/**
	 * 根据用户ID查询登录系统的前十名用户
	 * @param loginid 登录ID
	 * @return
	 */
	public List<TUser> getTUserByLoginIdTop10(String loginId);
	
	/**
	 * 查询新增的档案数，以统计图的形式显示出来 
	 * @param searchType 
	 * @param unitId 单位ID
	 * @return 
	 */
	public List<Object[]> selectInsertDocPic(HashMap<String,Object> map, String searchType) ;
	
	
	/**
	 * 查询工程下的档案数，并以饼图的形式显示出来
	 * @param unitId 单位ID
	 * @param searchType 
	 * @return
	 */
	public List<Object[]> selectProjectandDocCountPic(String unitId, String searchType);
	
	/**
	 * 是否插入跨域统计表
	 * @param pid 工程id
	 * @param ymonth 月份
	 * @return
	 */
	public int isInsertTProjectDocCount(String pid,String ymonth);
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public List<Object[]> countByArea(String unitId);
	

	/**
	 * 查询本地文档
	 * @return List<TDocProject>
	 * @throws Exception
	 */
	public TDocProject searchLocalDoc(String docid,String projectid);
	
	/**
	 * 查询同步文档
	 * @return List<TSynDoc>
	 * @throws Exception
	 */
	public TSynDoc searchSynDoc(String docid,String projectid);

	public int selectInsertDocCountBySyn(String object, String unitId);
	
	/**
	 * 根据区域ID查询所有下属省公司
	 * @param areaId
	 * @return code,name
	 */
	public List<Object[]> getProvincesByAreaId(String areaId);
}
