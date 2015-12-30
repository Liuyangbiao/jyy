package com.nsc.dem.service.project;

import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.project.UnitNode;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.base.IService;

public interface IprojectService extends IService {

	/**
	 * 生成工程编码
	 * 
	 * @param codeType
	 *            编码类型
	 * @return
	 */
	public String projectCode(String codeType);

	/**
	 * 生成单位编码
	 * 
	 * @param codeType
	 *            编码类型
	 * @return
	 */
	public String unitCode(String codeType);

	/**
	 * 单位树状列表 业主单位可按网省公司过滤
	 * 
	 * @param company
	 *            网省公司
	 * @return
	 */
	public List<Object> unitTreeList(String company);

	/**
	 * 工程树状列表 包括单独列出父工程，根据网省公司过滤，含业主
	 * 
	 * @return
	 */
	public List<UnitNode> projectTreeList(String code, String type);

	/**
	 * 部件树状列表 含工程、业主等层次结构
	 * 
	 * @return
	 */
	public List<Object> partsTreeList();

	public List<TProject> tProjectList(String code, String tcode);

	public List<TProject> tProjectListDoc(String code);

	/**
	 * 查询单位信息编码和项目分类获取工程信息
	 * 
	 * @param code
	 *            业主单位编码
	 * @param type
	 *            文档分类编码的前两位 -- 项目分类
	 * @return
	 */
	public List<TProject> unitProList(String code, String type);

	/**
	 * 根据文档查询关联工程
	 * 
	 * @param doc
	 * @return
	 */
	public TProject getProjectByDoc(TDoc doc);

	/**
	 * 根据工程ID删除工程，并删除工程下的所有文档
	 * 
	 * @param id
	 *            工程ID
	 * @param archives
	 *            文档服务接口
	 * @throws Exception
	 */
	public void deleteProjectWithDoc(Long id, IarchivesService archives)
			throws Exception;

	/**
	 * 工程信息分页查询
	 * 
	 * @param map
	 * @param firstResult
	 *            开始记录行数
	 * @param maxResults
	 *            结束记录行数
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> queryProjectInfoList(Map<String, Object> map,
			int firstResult, int maxResults, TableBean table) throws Exception;

	/**
	 * 初始化所属工程
	 * 
	 * @return
	 */
	public List<TProject> parentNameList();

	/**
	 * 根据单位code查询所有工程存在的初设单位
	 * 
	 * @param unitCode
	 * @return
	 */
	public List<TProject> getProjectByDesCode(String unitCode);

	/**
	 * 根据单位code查询所有工程存在的业主单位
	 * 
	 * @param unitCode
	 * @return
	 */
	public List<TProject> getProjectByOwenCode(String unitCode);

	/**
	 * 根据工程id，文档创建日期， 查询文档数
	 * 
	 * @param pid
	 *            工程id
	 * @param cdate
	 *            文档创建日期
	 * @return 文档数
	 */
	public int getDocCountByProjectId(String pid, String cdate);
	
	/**
	 * 查询没有产生索引的工程
	 * @return
	 */
	public List<Object[]> getProjectByNoCreateIndex();
	
	
	/**
	 * 根据电压名称查询
	 * @param levelName
	 * 					电压名称
	 * @return
	 * 			TDictionary对象
	 */
	public List<TDictionary> getVoltageLevelByName(String levelName);
}
