package com.nsc.dem.util.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.util.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.DataSynStatus;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.project.TProject;

public class DataSynXmlUtils {
	
	//工程，子工程，文件节点
	public final static String PROJECT="project";
	public final static String SUBPROJECT="sub_project";
	public final static String FILE="file";
	
	/**
	 * 根据ID查询元素是否存在
	 * @param node ：元素名称，使用常量
	 * @param id   ：元素ID，唯一
	 * @return
	 */
	public static boolean findElementById(String node, String id){
		Document document = XmlUtils.getInstance().getDocument();
		return findElementById(node,id,document);
	}
	
	/**
	 * 根据ID查询元素是否存在
	 * @param node ：元素名称，使用常量
	 * @param id   ：元素ID，唯一
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean findElementById(String node, String id, Document document){
		List<Element> list = document.selectNodes("//"+node+"[@id='"+id+"']");
		if(list !=null && list.size() == 1)
			return true;
		return false;
	}
	
	
	/**
	 * 创建工程元素
	 * @param project
	 * @param document
	 */
	public static void createProjectNode(TProject project,Document document){
		Configurater config = Configurater.getInstance();
		String systemType = config.getConfigValue("system_type");
		String from = "";
		if("1".equals(systemType)){
			from = config.getConfigValue("country");
		}else if("3".equals(systemType)){
			from = config.getConfigValue("unitCode");
		}
		if(!findElementById(PROJECT, project.getId()+"",document)){  
			Element element = DocumentHelper.createElement(PROJECT);
			element.addAttribute("id", project.getId()+"");
			//去除国网
			String to_unitIds = project.getGiveoutUnitId().replaceAll(from+"#", "");
			element.addAttribute("from_unit_id", from);
			element.addAttribute("location", project.getApproveUnit().getProxyCode());//评审地点
			element.addAttribute("to_unit_id",to_unitIds);
			element.addAttribute("project", project.getName());
			element.addAttribute("owerUnitUnitId", project.getTUnitByOwnerUnitId().getCode());
			element.addAttribute("owerUnitName", project.getTUnitByOwnerUnitId().getName());
			element.addAttribute("designerUnitId", project.getTUnitByDesignUnitId().getCode());
			element.addAttribute("designerName", project.getTUnitByDesignUnitId().getName());
			element.addAttribute("protype", project.getType());
			element.addAttribute("pharase", project.getStatus());
			element.addAttribute("voltage", project.getVoltageLevel());
			Element rootElement = document.getRootElement();
			rootElement.add(element);
			XmlUtils.getInstance().saveDocument(document);
		}
	} 
	 
	

	/**
	 * 创建工程元素
	 * @param project
	 * @param document
	 */
	public static void createProjectNode(TProject project){
		Document document =  XmlUtils.getInstance().getDocument();
		createProjectNode(project,document);
	}
	
	/**
	 * 创建子工程
	 * @param parentProject：父节点
	 * @param project：子节点
	 * @param document
	 */
	@SuppressWarnings("unchecked")
	public static void createSubProjectNode(TProject parentProject, TProject project, Document document){
		//查询该节点是否存在
		if(!findElementById(SUBPROJECT, project.getId()+"",document)){
			//查询父节点
			if(!findElementById(PROJECT, parentProject.getId()+"",document)){
				createProjectNode(parentProject,document);
			}
			//获取到父节点
			List<Element> parentElementList = document.selectNodes("//"+PROJECT+"[@id="+parentProject.getId()+"]");
			if(parentElementList != null && parentElementList.size() == 1){
				Element parentElement = parentElementList.get(0);
				Element element = DocumentHelper.createElement(SUBPROJECT);
				element.addAttribute("id", project.getId()+"");
				element.addAttribute("project", project.getName());
				element.addAttribute("protype", project.getType());
				element.addAttribute("owerUnitUnitId", project.getTUnitByOwnerUnitId().getCode());
				element.addAttribute("owerUnitName", project.getTUnitByOwnerUnitId().getName());
				element.addAttribute("designerUnitId", project.getTUnitByDesignUnitId().getCode());
				element.addAttribute("designerName", project.getTUnitByDesignUnitId().getName());
				element.addAttribute("pharase", project.getStatus());
				parentElement.add(element);
				XmlUtils.getInstance().saveDocument(document);
			}
		}
	}
	
	
	/**
	 * 创建子工程
	 * @param parentProject：父节点
	 * @param project：子节点
	 * @param document
	 */
	public static void createSubProjectNode(TProject parentProject, TProject project){
		Document document = XmlUtils.getInstance().getDocument();
		createSubProjectNode(parentProject,project,document);
	}
	
	/**
	 * 创建文件节点
	 * @param doc 文档对象
	 * @param document 
	 * @param operate 操作类型
	 * @param parentNodeId 父节点ID
	 * @param parentNodeType 父节点类型
	 */
	@SuppressWarnings("unchecked")
	public static void createFileNode(TDoc doc, String operate,String parentNodeId, String parentNodeType,String updateTime,Document document){
		XmlUtils util = XmlUtils.getInstance();
		List<Element> parentElementList = document.selectNodes("//"+parentNodeType+"[@id="+parentNodeId+"]");
		if(parentElementList != null && parentElementList.size() == 1 ){
			//得到父节点
			Element parentElement = parentElementList.get(0);
			Element element = DocumentHelper.createElement("file");
			element.addAttribute("id", doc.getId());
			element.addAttribute("operate", operate);
			element.addAttribute("update_time", updateTime+"");
			element.addAttribute("path", doc.getPath());
			if(!operate.trim().equals(DataSynStatus.DELETE.toString())){
				element.addAttribute("title", doc.getName());
				element.addAttribute("doctype", doc.getTDocType().getName());
				element.addAttribute("doctypeCode", doc.getTDocType().getCode());
				element.addAttribute("doctypeCodeName", doc.getTDocType().getName());
				element.addAttribute("format", doc.getFormat());
				element.addAttribute("previewpath", doc.getPreviewPath());
				element.addAttribute("author", doc.getTUser().getName());
				element.addAttribute("userCode", doc.getTUser().getTUnit().getCode());
				element.addAttribute("suffix", doc.getSuffix());
				element.addAttribute("version", doc.getVersion());
				element.addAttribute("storeLocation", doc.getStoreLocation());
				util.saveDocument(document);
			}
			parentElement.add(element);
			util.saveDocument(document);
			
		}
	}
	
	/**
	 * 创建文件节点
	 * @param doc 文档对象
	 * @param document 
	 * @param operate 操作类型
	 * @param parentNodeId 父节点ID
	 * @param parentNodeType 父节点类型
	 */
	public static void createFileNode(TDoc doc, String operate,String parentNodeId, String parentNodeType, String updateTime){
		Document document = XmlUtils.getInstance().getDocument();
		createFileNode(doc,operate,parentNodeId,parentNodeType,updateTime,document);
	}
	
	/**
	 * 修改文件节点
	 * @param doc DOC文档
	 * @param operate 操作类型
	 * @param parentNodeId 父节点
	 * @param parentNodeType 
	 * @param document
	 */
	public static void modifyFileNode(TDoc doc, String operate,String updateTime,Document document){
		try {
			//文件节点存在
			if(findElementById(FILE, doc.getId(),document)){
				//获取到文件节点
				Element element = (Element) document.selectNodes("//file[@id="+doc.getId()+"]").get(0);
				System.err.println("更新了一次"+doc.getId());
				//比较日期，如果当前日期是最新日期，修改
				if(compareDate(element.attribute("update_time").getText(), updateTime)){
					element.addAttribute("id", doc.getId());
					element.addAttribute("operate", operate);
					element.addAttribute("update_time", updateTime+"");
					element.addAttribute("path", doc.getPath());
					if(!operate.trim().equals("L07")){
						element.addAttribute("title", doc.getName());
						element.addAttribute("doctype", doc.getTDocType().getName());
						element.addAttribute("format", doc.getFormat());
						element.addAttribute("previewpath", doc.getPreviewPath());
						element.addAttribute("author", doc.getTUser().getName());
						XmlUtils.getInstance().saveDocument(document);
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(DataSynXmlUtils.class).warn(e.getMessage());
		}
	} 
	
	/**
	 * 修改文件节点
	 * @param doc
	 * @param operate
	 * @param parentNodeId
	 * @param parentNodeType
	 */
	public static void modifyFileNode(TDoc doc, String operate,String updateTime){
		Document document = XmlUtils.getInstance().getDocument();
		modifyFileNode(doc,operate,updateTime,document);
	}
	
	
	/**
	 * 比较两个日期
	 * @param xmlDate：XML文档的中的update_time
	 * @param Dbdate: 数据库中查询的日志执行时间
	 * 如果DbDate日期是最新日期，返回true,否则，返回false
	 */
	public static boolean compareDate(String xmlDate, String Dbdate){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date xml = format.parse(xmlDate);
			Date db = format.parse(Dbdate);
			Calendar xmlCalendar = Calendar.getInstance();
			xmlCalendar.setTime(xml);
			Calendar dbCalendar = Calendar.getInstance();
			dbCalendar.setTime(db);
			if(xmlCalendar.compareTo(dbCalendar) < 0){
				return true;
			}
		} catch (ParseException e) {
			Logger.getLogger(DataSynXmlUtils.class).warn(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 根据ID查找某一结点
	 * @param node
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Element getElement(String node,String id){
		if(StringUtils.isNotBlank(node) && StringUtils.isNotBlank(id)){
			Document document = XmlUtils.getInstance().getDocument();
			List<Element> list = document.selectNodes("//"+node+"[@id="+id+"]");
			if(list != null && list.size() > 0){
				return list.get(0);
			}
		}
		return null;
	}	
	
	/**
	 * 获取所有文件节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getAllFileNode(){
		Document document = XmlUtils.getInstance().getDocument();
		List<Element> fileElementList = document.selectNodes("//"+FILE);
		if(fileElementList != null && fileElementList.size() > 0){
			return fileElementList;
		}
		return null;
	}
	
}
