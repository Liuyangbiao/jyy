package com.nsc.dem.service.project.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.action.project.ProNode;
import com.nsc.dem.action.project.UnitNode;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TDocProject;
import com.nsc.dem.bean.project.TDocProjectId;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.base.BaseService;
import com.nsc.dem.service.project.IprojectService;

@SuppressWarnings("unchecked")
public class ProjectServiceImpl extends BaseService implements IprojectService {

	/**
	 * 部件树状列表 含工程、业主等层次结构
	 * 
	 * @return
	 */
	public List<Object> partsTreeList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 生成工程编码
	 * 
	 * @param codeType
	 *            编码类型
	 * @return
	 */
	public String projectCode(String codeType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 工程树状列表 包括单独列出父工程，根据网省公司过滤，含业主
	 * 
	 * @param code
	 *            单位表主键
	 * @param type
	 *            工程类型
	 * @return
	 */
	public List<UnitNode> projectTreeList(String code, String type) {

		int codeLength = code.length() + 2;
		String sql = "";
		if (code == null) {
			sql = "SELECT CODE,NAME FROM T_UNIT WHERE LENGTH(CODE)='4' AND TYPE='C01'";
		} else {
			sql = "SELECT CODE,NAME FROM T_UNIT WHERE LENGTH(CODE)='"
					+ codeLength + "'" + " AND SUBSTR(CODE,1," + code.length()
					+ ")='" + code + "'" + " AND TYPE='C01'";
		}

		List<TUnit> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUnit.class);

		List<UnitNode> unitList = new ArrayList<UnitNode>();
		for (TUnit unit : list) {
			UnitNode node = new UnitNode();
			node.setCode(unit.getCode());
			node.setName(unit.getName());

			sql = "SELECT ID,CODE,NAME FROM T_PROJECT WHERE PARENT_ID IS NULL "
					+ "AND OWNER_UNIT_ID='" + unit.getCode() + "'"
					+ " AND TYPE='" + type + "'";
			List<TProject> list2 = super.generalDAO.queryByNativeSQLEntity(sql,
					TProject.class);

			List<ProNode> proList = new ArrayList<ProNode>();
			for (TProject tpro : list2) {

				ProNode proNode = new ProNode();
				proNode.setId(tpro.getId());
				proNode.setCode(tpro.getCode());
				proNode.setName(tpro.getName());

				TProject pro = new TProject();
				pro.setParentId(tpro.getId());
				List list3 = super.EntityQuery(pro);

				if (list3 != null && list3.size() != 0) {
					proNode.setLeaf(true);
				} else {
					proNode.setLeaf(false);
				}

				proList.add(proNode);
			}

			node.setList(proList);

			List list4 = this.unitChildTreeList(node.getCode());
			if (list4 != null && list4.size() != 0) {
				node.setLeaf(true);
			} else {
				node.setLeaf(false);
			}
			unitList.add(node);
		}
		return unitList;
	}

	/**
	 * 查询单位信息是否有子节点
	 * 
	 * @param code
	 * @return
	 */
	public List unitChildTreeList(String code) {

		int codeLength = code.length() + 2;
		String sql = "";

		sql = "SELECT CODE,NAME FROM T_UNIT WHERE LENGTH(CODE)='" + codeLength
				+ "'" + " AND SUBSTR(CODE,1," + code.length() + ")='" + code
				+ "'" + " AND TYPE='C01'";

		List<TUnit> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TUnit.class);

		return list;
	}

	/**
	 * 查询工程信息子节点
	 * 
	 * @param code
	 * @return
	 */
	public List proChildTreeList(Long id) {

		TProject pro = new TProject();
		pro.setParentId(id);
		List<TProject> list = super.EntityQuery(pro);
		List<ProNode> proList = new ArrayList<ProNode>();
		for (TProject tpro : list) {
			ProNode proNode = new ProNode();
			proNode.setId(tpro.getId());
			proNode.setCode(tpro.getCode());
			proNode.setName(tpro.getName());

			TProject proj = new TProject();
			proj.setParentId(tpro.getId());
			List<?> list3 = super.EntityQuery(proj);

			if (list3 != null && list3.size() != 0) {
				proNode.setLeaf(true);
			} else {
				proNode.setLeaf(false);
			}

			proList.add(proNode);
		}

		return list;
	}

	/**
	 * 查询单位信息编码和项目分类获取工程信息
	 * 
	 * @param code
	 *            业主单位编码
	 * @param type
	 *            文档分类编码的前两位 -- 项目分类
	 * @return
	 */
	public List<TProject> unitProList(String code, String type) {

		String sql = "";

		if (type.equals("") || type == null) {

			return null;
		}

		sql = "SELECT P.* FROM T_UNIT U,T_PROJECT P WHERE U.CODE=P.OWNER_UNIT_ID"
				+ " AND U.CODE like '"
				+ code
				+ "'"
				+ " AND P.TYPE='"
				+ type
				+ "'";

		List<TProject> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProject.class);

		return list;
	}

	/**
	 * 生成单位编码
	 * 
	 * @param codeType
	 *            编码类型
	 * @return
	 */
	public String unitCode(String codeType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 单位树状列表 业主单位可按网省公司过滤
	 * 
	 * @param company
	 *            网省公司
	 * @return
	 */
	public List<Object> unitTreeList(String company) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取工程列表
	 */
	public List tProjectList(String code, String tcode) {
		String ucode = super.currentUser.getTUnit().getProxyCode();
		String hql = "";
		if (("".equals(code) || code == null)
				&& ("".equals(tcode) || tcode == null)) {
			hql = "from TProject p where 1=1";// 当工程阶段和工程分类都为空时使用
		} else if ((!("".equals(code) || code == null))
				&& ("".equals(tcode) || tcode == null)) {
			hql = "from TProject p where p.status = '" + code + "'";// 当工程阶段为空和工程分类不为空时使用
		} else if ((!("".equals(tcode) || tcode == null))
				&& ("".equals(code) || code == null)) {
			hql = "from TProject p  where p.type = '" + tcode + "'";// 当工程阶段不为空和工程分类为空时使用
		} else {
			hql = "from TProject p where p.type = '" + tcode
					+ "'  and p.status = '" + code + "' ";// 都不为空时使用
		}
		hql = hql + " and p.TUnitByOwnerUnitId.code like '" + ucode + "%'";
		List<TProject> list = generalDAO.queryByHQL(hql);
		return list;
	}

	/**
	 * 根据文档取得关联工程
	 * 
	 * @param doc
	 * @return
	 */
	public TProject getProjectByDoc(TDoc doc) {
		String hql = "select p from TProject p,TDocProject dp where p.id=dp.id.TProject.id and dp.id.TDoc.id="
				+ doc.getId();

		List<TProject> list = generalDAO.queryByHQL(hql);

		return list.isEmpty() ? null : list.get(0);
	}

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
			throws Exception {
		TProject project = (TProject) super.EntityQuery(TProject.class, id);

		TDocProject tdocProject = new TDocProject();
		TDocProjectId docProjectId = new TDocProjectId();
		docProjectId.setTProject(project);
		tdocProject.setId(docProjectId);

		String hql = "from TDocProject t where t.id.TProject.id="
				+ tdocProject.getId().getTProject().getId();
		List<TDocProject> docProject = generalDAO.queryByHQL(hql);

		String[] fileCode = new String[docProject.size()];
		for (int i = 0; i < docProject.size(); i++) {

			TDocProject tpro = (TDocProject) docProject.get(i);
			fileCode[i] = tpro.getId().getTDoc().getId();

		}
		archives.delArchives(fileCode);
		super.EntityDel(project);

	}

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
	public List queryProjectInfoList(Map<String, Object> map, int firstResult,
			int maxResults, TableBean table) throws Exception {
		Long count = super.generalDAO.getResultCount("proInfoSearch", map);

		table.setRecords(count.intValue());

		return count.intValue() == 0 ? null : super.generalDAO.getResult(
				"proInfoSearch", map, firstResult, maxResults);

	}

	/**
	 * 查询初设单位
	 * 
	 * @param code
	 * @return
	 */
	public List<TProject> parentNameList() {

		String ucode = super.currentUser.getTUnit().getProxyCode();
		// String sql =
		// "select t.*, u.* from t_unit t  ,t_user u where t.code like '"
		// + ucode + "%'";
		String sql = "SELECT * FROM t_project p WHERE parent_id is null and p.owner_unit_id like '"
				+ ucode + "%'";

		List<TProject> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProject.class);

		return list;
	}

	/**
	 * 获取工程列表 档案维护专用
	 */
	public List<TProject> tProjectListDoc(String code) {
		String hql = "from TProject p ";
		if (!"".equals(code) && code != null) {
			hql = hql + " where p.TUnitByOwnerUnitId.code like '" + code + "%'";
		}
		List<TProject> list = generalDAO.queryByHQL(hql);
		return list;
	}

	/**
	 * 根据单位code查询所有工程存在的初设单位
	 * 
	 * @param unitCode
	 * @return
	 */
	public List<TProject> getProjectByDesCode(String unitCode) {
		String sql = "select t.* from t_project t where t.design_unit_id= '"
				+ unitCode + "'";
		List<TProject> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProject.class);
		return list;
	}

	/**
	 * 根据单位code查询所有工程存在的业主单位
	 * 
	 * @param unitCode
	 * @return
	 */
	public List<TProject> getProjectByOwenCode(String unitCode) {
		String sql = "select t.* from t_project t where t.owner_unit_id= '"
				+ unitCode + "'";
		List<TProject> list = super.generalDAO.queryByNativeSQLEntity(sql,
				TProject.class);
		return list;
	}

	/**
	 * 根据工程id，文档创建日期， 查询文档数
	 * 
	 * @param pid
	 *            工程id
	 * @param cdate
	 *            文档创建日期
	 * @return 文档数
	 */
	public int getDocCountByProjectId(String pid, String cdate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("cdate", cdate);
		List<Object[]> list = super.generalDAO.getResult("docCountByProjectId",
				map);
		return list.size();
	}

	/**
	 * 查询没有产生索引的工程
	 */
	public List<Object[]> getProjectByNoCreateIndex() {
		return super.generalDAO.getResult("findProjectByNoIndex");
	}

	/**
	 * 根据电压名称查询                                                                                                                                                                                                                                                                                                                           
	 * 
	 * @param levelName
	 *            电压名称
	 * @return TDictionary对象
	 */
	public List<TDictionary> getVoltageLevelByName(String levelName) {
		String sql = "select * from t_dictionary t where upper(t.name) like '%'||upper('"+levelName+"')||'%'";
		List<TDictionary> list = super.generalDAO.queryByNativeSQLEntity(sql,TDictionary.class);
		return list;
	}

}
