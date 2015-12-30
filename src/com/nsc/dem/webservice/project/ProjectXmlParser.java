package com.nsc.dem.webservice.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.nsc.base.conf.Configurater;
import com.nsc.base.hibernate.CurrentContext;
import com.nsc.base.util.FileUtil;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.project.ChildProject;
import com.nsc.dem.bean.project.MainProject;
import com.nsc.dem.bean.project.TProject;
import com.nsc.dem.bean.system.TDictionary;
import com.nsc.dem.bean.system.TUnit;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.service.project.IprojectService;
import com.nsc.dem.service.project.impl.ProjectServiceImpl;
import com.nsc.dem.util.log.Logger;
import com.nsc.dem.webservice.archive.ArchiveUpdater;
import com.nsc.dem.webservice.util.ApplicationContext;
import com.nsc.dem.webservice.util.WsUtils;

@SuppressWarnings("all")
public class ProjectXmlParser {
	private List<MainProject> mproList;

	private IService service = null;
	
	private IprojectService projectService;

	private Logger logger = null;

	private TUser user = null;

	/**
	 * 构造ParseXml，并解析XML
	 * 
	 * @param xmlStr
	 */
	public ProjectXmlParser() {
		// 存放工程信息<工程ID,MainProject实例>
		mproList = new ArrayList<MainProject>();
		Configurater.getInstance();
		service = (IService) ApplicationContext.getInstance()
				.getApplictionContext().getBean("baseService");
		projectService = (IprojectService) ApplicationContext.getInstance()
		.getApplictionContext().getBean("projectService");
		
		try {
			user = WsUtils.getWsUser();
			logger = service.getLogManager(user)
					.getLogger(ArchiveUpdater.class);
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(ProjectXmlParser.class).warn(
					"异常:", e);
		}
		CurrentContext.putInUser(user);
	}

	/**
	 * 解析Xml字符串
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Document getDocument(String xmlStr) throws JDOMException,
			IOException {
		InputSource is = new InputSource(new StringReader(xmlStr));
		Document document = (new SAXBuilder()).build(is);
		return document;
	}

	/**
	 * 解析Xml文件
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Document getDocument(File xmlFile) throws JDOMException, IOException {
		Document document = (new SAXBuilder()).build(xmlFile);
		return document;
	}

	/**
	 * 解析Xml文档对像
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MainProject> parseXml(Document document, boolean isNeedSave)
			throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//logger.warn("无法解析XML字符串,请检查格式是否有误");
		Configurater config = Configurater.getInstance();
		Element root = document.getRootElement();
		List<Element> list = root.getChildren("MAIN");
		List<Element> fileList = root.getChildren("FILE_INFO");

		for (int i = 0; i < list.size(); i++) {
			Element mainNode = list.get(i);
			MainProject mainProject = new MainProject();
			//FTP方式下载解析
			
			mainProject.setFtpserver(root.getChildTextTrim("FTP_SERVER"));
			mainProject.setFtpuser(root.getChildTextTrim("FTP_USER"));
			mainProject.setFtppwd(root.getChildTextTrim("FTP_PWD"));
			mainProject.setFtpPort("21");
			mainProject.setXmlPath(root.getChildTextTrim("FILE_PATH"));
			
			//HTTP方式下载解析
//			mainProject.setFileId(fileList.get(0).getChildTextTrim("FILE_ID"));
//			mainProject.setFileName(fileList.get(0).getChildTextTrim("FILE_NAME"));
//			mainProject.setArea(fileList.get(0).getChildTextTrim("AREA"));
//			mainProject.setAddress(root.getChildTextTrim("FILESYSTEM_SERVER"));
			
			mainProject.setProjectName(mainNode.getAttributeValue("NAME"));

			TProject tProject = new TProject();
			tProject.setCode(mainNode.getAttributeValue("WBS_CODE"));
			tProject.setName(mainNode.getAttributeValue("NAME"));

			TUnit ownUnit = null;
			String beginDate = null;
			String endDate = null;

			ownUnit = this.getTUnit(mainNode.getAttributeValue("OWNER_UNIT")
					.trim(), "C01");
			tProject.setTUnitByOwnerUnitId(ownUnit);

			TUnit designUnit = this.getTUnit(mainNode.getAttributeValue(
					"DESIGN_UNIT").trim(), "C02");
			tProject.setTUnitByDesignUnitId(designUnit);

			tProject.setType(this.getProjectType(mainNode.getAttributeValue(
					"PROJ_TYPE").trim()));

			// 电压等级
			String voltage = mainNode.getAttributeValue("VOLTAGE_LEVEL").trim();
			tProject.setVoltageLevel(this.getVoltageLevel(voltage).getCode());
			
			//评审单位
			String approve = mainNode.getAttributeValue("REVIEW_UNIT").trim();
			TUnit unit = new TUnit();
			unit.setName(approve);
			unit = (TUnit) service.EntityQuery(unit).get(0);
			tProject.setApproveUnit(unit);
			
			String to_unit_id = "" ;
			String country = config.getConfigValue("country");
			
			//在省公司评审的情况
			if(unit.getProxyCode().length() == 8){
				to_unit_id = ownUnit.getCode();
			}else{
				String zone = ownUnit.getCode().substring(0,6);
				to_unit_id = country + "#" + zone + "#" + ownUnit.getCode();
			}
			tProject.setGiveoutUnitId(to_unit_id);
			
			tProject.setStatus("14");

			beginDate = mainNode.getAttributeValue("BEGIN_DATE").trim();
			endDate = mainNode.getAttributeValue("END_DATE").trim();

			tProject.setPreDesignYear(format.parse(beginDate));
			tProject.setOpenYear(format.parse(endDate));
			tProject.setTUser(user);
			tProject.setCreateDate(new Date());

			mainProject.settProject(tProject);

			List<ChildProject> child_projects = new ArrayList<ChildProject>();
			List<Element> childList = mainNode.getChildren("CHILD");
			for (int k = 0; k < childList.size(); k++) {
				Element childNode = childList.get(k);
				ChildProject childProject = new ChildProject();
				
				childProject
						.setProjectName(childNode.getAttributeValue("NAME"));

				TProject tProject_Child = new TProject();
				tProject_Child.setCode(childNode.getAttributeValue("WBS_CODE"));
				tProject_Child.setName(childNode.getAttributeValue("NAME"));

				// 获取网省公司
				tProject_Child.setTUnitByOwnerUnitId(ownUnit);

				TUnit designUnit_Child = this.getTUnit(childNode
						.getAttributeValue("DESIGN_UNIT").trim(), "C02");
				tProject_Child.setTUnitByDesignUnitId(designUnit_Child);
				
				tProject_Child.setGiveoutUnitId(tProject.getGiveoutUnitId());
				tProject_Child.setApproveUnit(tProject.getApproveUnit());
				
				tProject_Child.setType(this.getProjectType(childNode
						.getAttributeValue("PROJ_TYPE").trim()));
				tProject_Child.setVoltageLevel(this.getVoltageLevel(childNode
						.getAttributeValue("VOLTAGE_LEVEL").trim()).getCode());
				tProject_Child.setStatus("14");
				
				tProject_Child.setPreDesignYear(format.parse(beginDate));
				tProject_Child.setOpenYear(format.parse(endDate));
				tProject_Child.setTUser(user);
				tProject_Child.setCreateDate(new Date());
				childProject.settProject(tProject_Child);
				child_projects.add(childProject);
			}
			mainProject.setChildProjects(child_projects);
			mproList.add(mainProject);
			if (isNeedSave) {
				this.saveXmlFile(document, tProject.getCode());
			}
		}

		return mproList;
	}

	private TUnit getTUnit(String unitName, String type) throws Exception {
		TUnit tUnit = new TUnit();
		tUnit.setName(unitName);
		tUnit.setType(type);
		List<Object> unitList = service.EntityQuery(tUnit);
		if (unitList != null && unitList.size() > 0) {
			tUnit = (TUnit) unitList.get(0);
		} else if (!type.equals("C01")) {
			tUnit = new TUnit();
			tUnit.setName(unitName);
			tUnit.setType(type);
			tUnit.setShortName(unitName);
			tUnit.setIsUsable(true);
			tUnit.setCreateDate(new Date());
			tUnit.setTUser(user);
			try {
				service.insertEntity(tUnit);
			} catch (Exception e) {
				logger.warn("工程信息解析时,单位:" + tUnit.getName() + "创建失败!");
				throw new Exception("单位:" + tUnit.getName() + "创建失败!"
						+ e.getMessage());
			}
		} else {
			logger.warn("网省单位:" + tUnit.getName() + "不存在!");
			throw new Exception("网省单位:" + tUnit.getName() + "不存在!");
		}

		return tUnit;
	}

	private String getProjectType(String typeName) throws Exception {
		TDictionary dic = new TDictionary();
		// '变电站新建' '变电站扩建' '变电站扩建间隔'
		if (typeName.indexOf("变电站") != -1) {
			typeName = "变电站";
		}
		// 架空大跨越段线路,架空一般线路
		if (typeName.indexOf("架空") != -1 && typeName.indexOf("线路") != -1) {
			typeName = "架空线路";
		}

		dic.setName(typeName);
		dic.setParentCode("GCFL");
		List<Object> list = service.EntityQuery(dic);
		if (list == null || list.size() == 0) {
			logger.warn("工程信息解析时,工程类型:" + dic.getName() + "获取失败!");
			throw new Exception("工程信息解析时,工程类型:" + dic.getName() + "获取失败!");
		} else {
			dic = (TDictionary) list.get(0);
			return dic.getCode();
		}

	}

	private TDictionary getVoltageLevel(String voltageLevel) throws Exception {
		
		TDictionary dic = new TDictionary();
		if(voltageLevel.indexOf("kV") != -1){
			voltageLevel = voltageLevel.substring(0, voltageLevel.indexOf("kV")+2);
		}
		List<TDictionary> list = projectService.getVoltageLevelByName(voltageLevel);
		if (list == null || list.size() == 0) {
			logger.warn("工程信息解析时,电压等级:" + dic.getName() + "获取失败!");
			throw new Exception("工程信息解析时,电压等级:" + dic.getName() + "获取失败!");
		} else {
			dic = (TDictionary) list.get(0);
			return dic;
		}

	}

	

	private void saveXmlFile(Document document, String code)
			throws FileNotFoundException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String localPath = FileUtil.getWebRootPath() + File.separator + "temp";
		localPath = localPath.replaceAll("%20", " ");
		File folder = new File(localPath);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		localPath = localPath + File.separator + "proj_" + code + "_"
				+ format.format(new Date()) + ".xml";

		File file = new File(localPath);
		Format ft = Format.getPrettyFormat();
		ft.setEncoding("UTF-8");
		XMLOutputter out = new XMLOutputter(ft);
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			out.output(document, writer);
		} catch (FileNotFoundException e) {
			logger.warn("保存XML文件到本地时出现异常!" + localPath);
			throw new FileNotFoundException("保存XML文件到本地时出现异常!" + localPath);
		} catch (IOException e) {
			logger.warn("保存XML文件到本地时出现异常!");
			throw new FileNotFoundException("保存XML文件到本地时出现异常!");
		}
	}
	

}
