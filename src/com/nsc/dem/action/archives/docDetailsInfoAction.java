package com.nsc.dem.action.archives;

import java.util.ArrayList;
import java.util.List;

import com.nsc.base.util.DateUtils;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.archives.TDocType;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TPreDesgin;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.service.searches.IsearchesService;
/**
 * 文档详细信息、工程信息、初设信息Action
 * 
 * @author ibm
 *
 */
public class docDetailsInfoAction extends BaseAction {

	private static final long serialVersionUID = -1110917411158741377L;
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	IsearchesService searchesService;

	public void setSearchesService(IsearchesService searchesService) {
		this.searchesService = searchesService;
	}

	public void setDocPreDetailsList(List<Object[]> docPreDetailsList) {
		this.docPreDetailsList = docPreDetailsList;
	}

	List<Object[]> docPreDetailsList;

	public List<Object[]> getDocPreDetailsList() {
		return docPreDetailsList;
	}

	Object[] docPreDetials;

	public Object[] getDocPreDetials() {
		return docPreDetials;
	}

	public void setDocPreDetials(Object[] docPreDetials) {
		this.docPreDetials = docPreDetials;
	}

	Object[] docDetials;

	public Object[] getDocDetials() {
		return docDetials;
	}

	Object[] proDetials;

	public Object[] getProDetials() {
		return proDetials;
	}

	Object[] preDesignDetials;

	IService baseService;

	public void setBaseService(IService baseService) {
		this.baseService = baseService;
	}

	/**
	 * 获取 工程的详细信息
	 * @return
	 */
	public String getProjectDetailsAction() {

		List<Object[]> listobject = searchesService.documentInfo(id);

		for (int i = 0; i < listobject.size(); i++) {
			// 将对象取出值
			TDoc tdoc = (TDoc) listobject.get(i)[0];// 文档表
			TDocType tdoctype = (TDocType) listobject.get(i)[1];// 文档分类表
			TProject tproject = (TProject) listobject.get(i)[2];// 工程表
			TUnit tunit = (TUnit) listobject.get(i)[3];// 单位表
			TUser tuser = (TUser) listobject.get(i)[4];// 用户表
			TDictionary tbaomi = (TDictionary) listobject.get(i)[5];
			TDictionary tdocStatus = (TDictionary) listobject.get(i)[6];
			TDictionary tzhuanYe = (TDictionary) listobject.get(i)[7];
			TDictionary tdianYa = (TDictionary) listobject.get(i)[8];
			TDictionary tprojectClass = (TDictionary) listobject.get(i)[9];
			TDictionary tprojectStatus = (TDictionary) listobject.get(i)[10];
			TDictionary ttypeClass = (TDictionary) listobject.get(i)[11];
			// 文件信息(文件名称、文件格式、文件后缀、文档密级、文件状态、上传时间、上传者、文件大小)
			// 档案信息(文档类型分类、档案分类、专业、档案版本)
			// 文件信息 档案信息
			String docName = tdoc.getName();// 名称
			String docFormat = tdoc.getFormat();// 格式
			FileUtil fileUtil = new FileUtil();

			String docFileSize = fileUtil.getHumanSize(tdoc.getFileSize()
					.longValue())
					+ "(" + tdoc.getFileSize() + "[byte])";// 大小
			String docVersion = tdoc.getVersion();// 版本号
			String docCreateDate = DateUtils.DateToString(tdoc.getCreateDate());// 创建时间
			String docClass = tdoctype.getName();// 分类名称
			String docCreate = tuser.getName();// 创建者
			String suffix = tdoc.getSuffix();// 文件后缀名
			String sheJiName = tunit.getName();
			// 密级如果档案表中没有密级，则取文档分类表中的密级
			String baomi = "";
			if (tdoc.getSecurity() != null) {

				baomi = tbaomi.getName();
			} else if (tdoctype.getSpeciality() != null) {
				baomi = tbaomi.getName();
			}
			// 文件状态
			String docStatus = tdocStatus.getName();

			// 文档类型分类
			String zongHeFile = "";
			if (tdoc.getTDocType() != null) {
				// String tempCode = tdoc.getTDocType().getCode();
				// String zhcode = tempCode.substring(4, 6);

				zongHeFile = ttypeClass.getName();
			}
			// 专业
			String zhuanye = "";
			if (tzhuanYe != null) {
				zhuanye = tzhuanYe.getName();
			}

			// 查询初设表信息
			TPreDesgin preDesign = (TPreDesgin) searchesService.EntityQuery(
					TPreDesgin.class, tdoc.getId());
			if (preDesign != null && preDesign.getTUnit().getCode() != null) {

				TUnit unit = (TUnit) searchesService.EntityQuery(TUnit.class,
						preDesign.getTUnit().getCode());

				preDesignDetials = new Object[] {
						unit.getName(),
						DateUtils.DateToString(preDesign.getCreateDate(),
								"yyyy-MM-dd"), preDesign.getAjtm(),
						preDesign.getAndh(), preDesign.getFlbh(),
						preDesign.getJcrm(), preDesign.getJnyh(),
						preDesign.getLjrm(), preDesign.getPzrm(),
						preDesign.getShrm(), preDesign.getSjjd(),
						preDesign.getSjrm(), preDesign.getTzmc(),
						preDesign.getTzzs(), preDesign.getXhrm() };

			}

			// 存放文档信息的Object数组
			docDetials = new Object[] { docName, docFormat, docFileSize,
					docVersion, docCreateDate, docClass, docCreate, suffix,
					baomi, docStatus, zongHeFile, zhuanye, sheJiName };

			// 工程信息(工程名称、业主单位、网省公司、工程分类、工程阶段、电压等级、监理单位、施工单位、初设单位)
			String proCode = tproject == null ? "" : tproject.getCode();// 工程编码
			String proName = tproject == null ? "" : tproject.getName();// 工程名称
			// 电压等级
			String proVoltageLevel = tdianYa.getName();

			String proOpenYear = tproject == null ? "" : DateUtils
					.DateToString(tproject.getOpenYear());// 开工年份

			// 判断的父ID是否为空如果不为空 属所工程
			String proParent = "";
			if (tproject != null && tproject.getParentId() != null) {
				TProject newTproject = (TProject) baseService.EntityQuery(
						TProject.class, tproject.getParentId());
				proParent = newTproject.getName();
			}

			// 工程分类
			String projectType = tprojectClass.getName();

			// 工程阶段
			String projectStatus = tprojectStatus.getName();

			// 网省公司
			String wangSheng = "";
			// 业主单位
			String ownerCode = "";
			if (tproject.getTUnitByOwnerUnitId() != null) {
				TUnit td = (TUnit) searchesService.EntityQuery(TUnit.class,
						tproject.getTUnitByOwnerUnitId().getCode());
				ownerCode = td.getName();
				wangSheng = td.getName();
			}
			// 初设单位
			String designCode = "";

			if (tproject.getTUnitByDesignUnitId() != null) {
				TUnit tu = (TUnit) searchesService.EntityQuery(TUnit.class,
						tproject.getTUnitByDesignUnitId().getCode());

				designCode = tu.getName();
			}

			// 存放工程信息的Object数组
			proDetials = new Object[] { proCode, proName, proVoltageLevel,
					proOpenYear, ownerCode, proParent, projectType,
					projectStatus, wangSheng, designCode };
			if (preDesignDetials != null) {
			docPreDetials = new Object[] { docDetials, proDetials,
					preDesignDetials };
			// 将文档信息和工程信息放在一个list集合中
			docPreDetailsList = new ArrayList<Object[]>();
			docPreDetailsList.add(docPreDetials);
			} else {
				docPreDetials = new Object[] { docDetials, proDetials };
				// 将文档信息和工程信息放在一个list集合中
				docPreDetailsList = new ArrayList<Object[]>();
				docPreDetailsList.add(docPreDetials);
			}

		}
		return SUCCESS;
	}

	public Object[] getPreDesignDetials() {
		return preDesignDetials;
	}

	public void setPreDesignDetials(Object[] preDesignDetials) {
		this.preDesignDetials = preDesignDetials;
	}
}