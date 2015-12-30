package com.nsc.dem.action.project;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.base.conf.Configurater;
import com.nsc.base.hibernate.GeneratorFactory;
import com.nsc.base.util.DateUtils;
import com.nsc.base.util.GetCh2Spell;
import com.nsc.dem.action.BaseAction;
import com.nsc.dem.action.bean.RowBean;
import com.nsc.dem.action.bean.TableBean;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.archives.IarchivesService;
import com.nsc.dem.service.project.IprojectService;

/**
 * 工程管理信息Action
 * 
 * @author ibm
 * 
 */
public class ProjectInfoManagerAction extends BaseAction {

	private static final long serialVersionUID = 1733079384712231376L;
	// 查询时传来的参数(工程编码、工程类型、电压等级、开工年份、单位编码、工程状态)
	private String projectNameId;

	public void setProjectNameId(String projectNameId) {
		this.projectNameId = projectNameId;
	}

	Object[] object;

	public Object[] getObject() {
		return object;
	}

	public void setObject(Object[] object) {
		this.object = object;
	}

	private TProject tproject;

	public TProject getTproject() {
		return tproject;
	}

	public void setTproject(TProject tproject) {
		this.tproject = tproject;
	}

	private String id;

	public void setId(String id) {
		this.id = id;
	}

	private String proType;
	private String voltageLevel;
	private String openYear;

	private String unitNameCode;
	private String proStatus;
	private List<Map<String, Object>> autoList;

	public List<Map<String, Object>> getAutoList() {
		return autoList;
	}

	public void setAutoList(List<Map<String, Object>> autoList) {
		this.autoList = autoList;
	}

	private String codes;

	public void setCodes(String codes) {
		this.codes = codes;
	}

	// 传来的页数和行数
	private int page;
	private int rows;
	String returnValue;

	public String getReturnValue() {
		return returnValue;
	}

	// tablebean
	TableBean tablebean = new TableBean();

	// 服务

	public TableBean getTablebean() {
		return tablebean;
	}

	public void setTablebean(TableBean tablebean) {
		this.tablebean = tablebean;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public void setVoltageLevel(String voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public void setOpenYear(String openYear) {
		this.openYear = openYear;
	}

	public void setUnitNameCode(String unitNameCode) {
		this.unitNameCode = unitNameCode;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	IprojectService projectService;

	public void setProjectService(IprojectService projectService) {
		this.projectService = projectService;
	}

	IarchivesService archivesService;

	public void setArchivesService(IarchivesService archivesService) {
		this.archivesService = archivesService;
	}

	private String insertUnitNameCode;
	private String designUnitNameCode;

	/**
	 * 查询工程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectProjectInfoAction() throws Exception {
		// 设置起码位置
		int firstResult = (page - 1) * rows;
		// 设置终止位置
		int maxResults = page * rows;

		List<Object[]> list = new ArrayList<Object[]>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pid", projectNameId);
		if (proType == null || proType.equals("")) {
			map.put("ptype", null);
		} else {
			map.put("ptype", proType);
		}
		if (voltageLevel == null || voltageLevel.equals("")) {
			map.put("pvoltage", null);
		} else {
			map.put("pvoltage", voltageLevel);
		}
		map.put("openyear", openYear);
		map.put("ncode", unitNameCode);
		if (proStatus == null || proStatus.equals("")) {
			map.put("pstatus", null);
		} else {
			map.put("pstatus", proStatus);
		}
		map.put("ucode", super.getLoginUser().getTUnit().getProxyCode());

		list = projectService.queryProjectInfoList(map, firstResult,
				maxResults, tablebean);

		List<RowBean> rowsList = new ArrayList<RowBean>();
		if (list != null) {
			for (Object[] obj : list) {
				TUser user = (TUser) obj[0];
				// TUnit unit = (TUnit) obj[1];
				TProject project = (TProject) obj[2];
				TDictionary projectstatus = (TDictionary) obj[3];
				TDictionary proejcttype = (TDictionary) obj[4];
				TDictionary tdianYa = (TDictionary) obj[5];
				RowBean rowbean = new RowBean();
				String type = "";
				if (project.getType() != null) {

					type = proejcttype.getName();
				}
				String dian = "";
				if (project.getVoltageLevel() != null) {

					dian = tdianYa.getName();
				}

				// 判断的父ID是否为空如果不为空 属所工程
				String proParent = "";
				if (project.getParentId() != null) {
					TProject newTproject = (TProject) projectService
							.EntityQuery(TProject.class, project.getParentId());
					proParent = newTproject.getName();
				}
				// 业主单位
				String ownerUnit = "";
				if (project.getTUnitByOwnerUnitId() != null) {
					TUnit tu = (TUnit) projectService.EntityQuery(TUnit.class,
							project.getTUnitByOwnerUnitId().getCode());
					ownerUnit = tu.getName();
				}
				// 初设单位
				String designUnit = "";
				if (project.getTUnitByDesignUnitId() != null) {
					TUnit tu = (TUnit) projectService.EntityQuery(TUnit.class,
							project.getTUnitByDesignUnitId().getCode());
					designUnit = tu.getName();
				}
				// 初设年份
				String proDesign = DateUtils.DateToString(project
						.getPreDesignYear());

				// 工程状态
				String projectStatus = "";
				if (project.getStatus() != null) {

					projectStatus = projectstatus.getName();
				}
				// 开工年份
				String openYear = DateUtils.DateToString(project.getOpenYear());

				// 竣工年份
				String closeYear = DateUtils.DateToString(project
						.getCloseYear());

				// 创建日期
				String createDate = DateUtils.DateToString(project
						.getCreateDate());

				rowbean.setCell(new Object[] {

						project.getCode(),
						project.getName(),
						proParent,

						ownerUnit,
						designUnit,
						type,
						dian,
						projectStatus,
						proDesign,

						openYear,
						closeYear,
						user.getName(),
						createDate,
						"<a href='#'  onclick='insertProjectInfo(\""
								+ project.getId()
								+ "\")' >更新</a>" });
				rowbean.setId(project.getId().toString());
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

	public void setInsertUnitNameCode(String insertUnitNameCode) {
		this.insertUnitNameCode = insertUnitNameCode;
	}

	public void setDesignUnitNameCode(String designUnitNameCode) {
		this.designUnitNameCode = designUnitNameCode;
	}

	/**
	 * 插入和更新工程信息
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String updateProjectInfoAction() throws UnsupportedEncodingException {

		// 业主单位
		TUnit owenUnit = (TUnit) projectService.EntityQuery(TUnit.class,
				insertUnitNameCode);
		if (owenUnit != null) {
			tproject.setTUnitByOwnerUnitId(owenUnit);
		}
		// 初设单位
		TUnit designUnit = (TUnit) projectService.EntityQuery(TUnit.class,
				designUnitNameCode);
		if (designUnit != null) {
			tproject.setTUnitByDesignUnitId(designUnit);
		}

		// 当前用户
		tproject.setTUser(super.getLoginUser());
		if (tproject.getCode() == null || tproject.getCode().equals("")) {
			// 当前日期
			tproject.setCreateDate(new Date());

			String code = GeneratorFactory.getGeneratorCode(projectService
					.getSession().getSessionFactory().openSession(), tproject);
			
			
			tproject.setCode(code);
			
			Configurater config = Configurater.getInstance();
			String unitCode = config.getConfigValue("unitCode");
			
			
			String[] codes = unitCode.split(",");
			//省公司
			if(codes != null && codes.length == 1 ){
				tproject.setApproveUnit(owenUnit);
				tproject.setGiveoutUnitId(owenUnit.getCode());
			}else{//国家电网
				String country = config.getConfigValue("country");
				TUnit unit = (TUnit) projectService.EntityQuery(TUnit.class, country);
				tproject.setApproveUnit(unit);
				String ownUnitId = owenUnit.getCode();
				String to_unit_id = country+"#";
				if(ownUnitId.length() == 8){
					String zone = ownUnitId.substring(0,6);
					to_unit_id += zone+"#";
				}
				to_unit_id += ownUnitId;
				tproject.setGiveoutUnitId(to_unit_id);
			}
			
			
			projectService.insertEntity(tproject);
		} else {
			tproject.setCreateDate(new Date());
			projectService.updateEntity(tproject);

		}

		returnValue = tproject.getCode();

		return SUCCESS;
	}

	/**
	 * 
	 * 删除工程
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteProjectInfoAction() throws Exception {

		String[] codeList = codes.split(",");

		for (int i = 0; i < codeList.length; i++) {
			Long eachCode = Long.valueOf(codeList[i]);

			TProject proj = new TProject();
			proj.setParentId(eachCode);
			List<Object> list = projectService.EntityQuery(proj);
			if (list != null && list.size() > 0) {
				for (int k = 0; k < list.size(); k++) {
					TProject project = (TProject) list.get(k);
					projectService.deleteProjectWithDoc(project.getId(),
							archivesService);

				}
			} else {

				projectService.deleteProjectWithDoc(eachCode, archivesService);
			}
		}
		returnValue = "success";
		return SUCCESS;
	}

	/**
	 * 初始化工程名称
	 */
	public String projectNameAction() {
		String code = getRequest().getParameter("id");
		String tcode = getRequest().getParameter("tid");
		autoList = new ArrayList<Map<String, Object>>();
		List<TProject> tProjectList = projectService.tProjectList(code, tcode);
		if (tProjectList.size() == 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "-1");
			map.put("name", "没有数据");
			map.put("spell", GetCh2Spell.getBeginCharacter("没有数据"));
			autoList.add(map);
		} else {
			for (int i = 0; i < tProjectList.size(); i++) {
				TProject tProject = tProjectList.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tProject.getId());
				map.put("code", tProject.getCode());
				map.put("name", tProject.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tProject
						.getName()));
				map.put("other", tProject.getType());
				autoList.add(map);
			}
		}

		return SUCCESS;
	}

	/**
	 * 更新 工程信息
	 * 
	 * @return
	 */
	public String updateProjectInfoSelectAction() {

		TProject tp = (TProject) projectService.EntityQuery(TProject.class,
				Long.valueOf(id));
		// 业主单 位
		String unitName = "";
		String unitCode = "";
		if (tp.getTUnitByOwnerUnitId().getCode() != null) {
			TUnit unit = (TUnit) projectService.EntityQuery(TUnit.class, tp
					.getTUnitByOwnerUnitId().getCode());
			unitName = unit.getName();
			unitCode = unit.getCode();
		}
		// 所属工程
		String parentName = "";
		String parentId = "";
		if (tp.getParentId() != null) {
			TProject pro = (TProject) projectService.EntityQuery(
					TProject.class, tp.getParentId());
			parentName = pro.getName();
			parentId = pro.getId().toString();
		}
		// 初设单位
		String designName = "";
		String designCode = "";
		if (tp.getTUnitByDesignUnitId().getCode() != null) {
			TUnit unit = (TUnit) projectService.EntityQuery(TUnit.class, tp
					.getTUnitByDesignUnitId().getCode());
			designName = unit.getName();
			designCode = unit.getCode();
		}

		// 初设年份
		String designyear = "";
		if (tp.getPreDesignYear() != null) {

			designyear = DateUtils.DateToString(tp.getPreDesignYear());

		}
		// 开工年份
		String openYear = "";
		if (tp.getOpenYear() != null) {

			openYear = DateUtils.DateToString(tp.getOpenYear());

		}
		// 竣工年份
		String closeYear = "";
		if (tp.getCloseYear() != null) {

			closeYear = DateUtils.DateToString(tp.getCloseYear());

		}
		object = new Object[] { tp.getCode(), unitName, unitCode, tp.getName(),
				parentName, parentId, tp.getType(), designName, designCode,
				tp.getVoltageLevel(), tp.getStatus(), designyear, openYear,
				closeYear, tp.getId()

		};

		return SUCCESS;
	}

	/**
	 * 初始化所属工程名称
	 */
	public String projectParentNameAction() {

		autoList = new ArrayList<Map<String, Object>>();
		List<TProject> tProjectList = projectService.parentNameList();
		if (tProjectList.size() == 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", "-1");
			map.put("name", "没有数据");
			map.put("spell", GetCh2Spell.getBeginCharacter("没有数据"));
			autoList.add(map);
		} else {
			for (int i = 0; i < tProjectList.size(); i++) {
				TProject tProject = tProjectList.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tProject.getId());
				map.put("code", tProject.getCode());
				map.put("name", tProject.getName());
				map.put("spell", GetCh2Spell.getBeginCharacter(tProject
						.getName()));
				map.put("other", tProject.getType());
				autoList.add(map);
			}
		}

		return SUCCESS;
	}
}