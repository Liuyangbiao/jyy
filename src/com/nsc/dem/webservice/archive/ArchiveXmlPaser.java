package com.nsc.dem.webservice.archive;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.FileUtil;
import com.nsc.base.util.Reflections;
import com.nsc.dem.action.bean.WholeSearchDoc;
import com.nsc.dem.bean.archives.FileInfo;
import com.nsc.dem.bean.project.ChildProject;
import com.nsc.dem.bean.project.MainProject;
import com.nsc.dem.util.xml.XmlUtils;
import com.nsc.dem.webservice.client.CXFClient;
import com.nsc.dem.webservice.client.edm.Exception_Exception;


/**
 * 
 * @author yangchenlong,解析webservice接口传回的字符串
 * 
 */
@SuppressWarnings("all")  
public class ArchiveXmlPaser {
	
	private List<MainProject> mproList;
	
	private Configurater config = null; 
	/**
	 * 构造ParseXml，并解析XML
	 * @param xmlStr
	 */
	public ArchiveXmlPaser(){
		//存放工程信息<工程ID,MainProject实例>
		mproList = new ArrayList<MainProject>(); 
		config = Configurater.getInstance();
	}
	
	
	/**
	 * 使用FTP下载时的 解析Xml
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public List<MainProject> parseXml(File xmlFile) throws JDOMException, IOException{
		Document document = (new SAXBuilder()).build(xmlFile);
		Element root = document.getRootElement();
		List<Element> list = root.getChildren("FIRST");
		
		for(int i=0;i<list.size();i++){
			Element firstNode = list.get(i);
			MainProject mainProject = new MainProject();
			
			mainProject.setFtpserver(root.getChildTextTrim("FTP_SERVER"));
			mainProject.setFtpuser(root.getChildTextTrim("FTP_USER"));
			mainProject.setFtppwd(root.getChildTextTrim("FTP_PWD"));
			mainProject.setFtpPort(root.getChildTextTrim("FTP_PORT"));
			//唯一标识作用
			Element el_id = firstNode.getChild("FIRST_ID");
			//Element el_pId = firstNode.getChild("FIRST_WBS");
			Element el_name = firstNode.getChild("FIRST_NAME");
			mainProject.setProjectId(el_id.getText().trim());
			mainProject.setProjectName(el_name.getText().trim());				
			
			List<Element>  first_files = firstNode.getChild("FIRST_FILE")
												  .getChildren("FILEPATH");
			List<FileInfo> files = new ArrayList<FileInfo>();
			
			for(Element el:first_files){
				FileInfo finfo = new FileInfo();
				String folderPath = el.getAttributeValue("FOLDERPATH");
				folderPath = folderPath==null?"未知文件夹":folderPath.replaceAll("%20", " ").replaceAll("\\*","");
				finfo.setFolderPath(folderPath);
				finfo.setFileType(el.getAttributeValue("FILETYPE")==null?"未标明文件类型":el.getAttributeValue("FILETYPE"));
				finfo.setFilename(el.getAttributeValue("FILENAME"));
				finfo.setFilePath(el.getText().trim());
				finfo.setFileId(el.getAttributeValue("FILEID"));
				files.add(finfo);
			}
			mainProject.setFileList(files);
			
			//取子项目FIRST_DIR
			Element first_dir = firstNode.getChild("FIRST_DIR");
			List<Element> seconds = new ArrayList<Element>();
			if(first_dir!=null){
				seconds = firstNode.getChild("FIRST_DIR").getChildren();
			}
			List<ChildProject> child_projects = new ArrayList();
			
			for(int k=0;k<seconds.size();k++){
				Element cp = seconds.get(k);
				ChildProject childProject = new ChildProject();  //单项工程bean对象
				childProject.setProjectId(cp.getChildTextTrim("SECOND_ID"));
				childProject.setProjectName(cp.getChildTextTrim("SECOND_NAME"));
				childProject.setDescription(cp.getChildTextTrim("SECOND_DES"));
				
				List<FileInfo> fileList = new ArrayList();
				List<Element> second_files = cp.getChild("SECOND_FILE").getChildren("FILEPATH");
				
				for(int j=0;j<second_files.size();j++){
					Element el = second_files.get(j);
					String folderPath = el.getAttributeValue("FOLDERPATH");
					
					FileInfo finfo = new FileInfo();
					finfo.setFolderPath(folderPath==null?"未文件夹":folderPath.replaceAll("%20", " ").replaceAll("\\*",""));
					finfo.setFileType(el.getAttributeValue("FILETYPE"));
					finfo.setFilename(el.getAttributeValue("FILENAME"));
					finfo.setFileId(el.getAttributeValue("FILEID"));
					finfo.setFilePath(el.getText().trim());
					fileList.add(finfo);

				}
				
				//单项工程文件的路径列表
				childProject.setFileList(fileList);
				
				child_projects.add(childProject);
			}
			
			
			mainProject.setChildProjects(child_projects);    
			mproList.add(mainProject);
		}
		
		return mproList;
	}
	
	
	
	/**
	 * 使用HTTP下载方式的解析
	 * @param list
	 * @return
	 */
	public List<MainProject> parseXmlByHttp(File xmlFile) throws JDOMException, IOException{
		Document document = (new SAXBuilder()).build(xmlFile);
		Element root = document.getRootElement();
		List<Element> list = root.getChildren("FIRST");
		Element address = root.getChild("FILESYSTEM_SERVER");
		
		for(int i=0;i<list.size();i++){
			Element firstNode = list.get(i);
			MainProject mainProject = new MainProject();
			//文件下载地址
			mainProject.setAddress(address.getText());
			
			//唯一标识作用
			Element el_id = firstNode.getChild("FIRST_ID");
			Element el_name = firstNode.getChild("FIRST_NAME");
			Element el_pId = firstNode.getChild("FIRST_WBS");
			
			mainProject.setProjectId(el_id.getText().trim());
			mainProject.setProjectName(el_name.getText().trim());
			mainProject.setCode(el_pId.getText().trim());
			
			List<Element>  first_files = firstNode.getChild("FIRST_FILE")
												  .getChildren("PRJOWNERCORP");
			List<FileInfo> files = new ArrayList<FileInfo>();
			
			for(Element el:first_files){
				FileInfo finfo = new FileInfo();
				String folderPath = el.getAttributeValue("FOLDERPATH");
				folderPath = folderPath==null?"未知文件夹":folderPath.replaceAll("%20", " ").replaceAll("\\*","");
				finfo.setFolderPath(folderPath);
				finfo.setFileType(el.getAttributeValue("FILETYPE")==null?"未标明文件类型":el.getAttributeValue("FILETYPE"));
				finfo.setFilename(el.getAttributeValue("FILENAME"));
				finfo.setArea(el.getText().trim());
				finfo.setFileId(el.getAttributeValue("FILEID"));
				files.add(finfo);
			}
			mainProject.setFileList(files);
			
			//取子项目FIRST_DIR
			Element first_dir = firstNode.getChild("FIRST_DIR");
			List<Element> seconds = new ArrayList<Element>();
			if(first_dir!=null){
				seconds = firstNode.getChild("FIRST_DIR").getChildren();
			}
			
			List<ChildProject> child_projects = new ArrayList();
			
			for(int k=0;k<seconds.size();k++){
				Element cp = seconds.get(k);
				ChildProject childProject = new ChildProject();  //单项工程bean对象
				childProject.setProjectId(cp.getChildTextTrim("SECOND_ID"));
				childProject.setProjectName(cp.getChildTextTrim("SECOND_NAME"));
				childProject.setCode(cp.getChildTextTrim("SECOND_WBS"));
				//childProject.setDescription(cp.getChildTextTrim("SECOND_DES"));
				
				List<FileInfo> fileList = new ArrayList();
				List<Element> second_files = cp.getChild("SECOND_FILE").getChildren("PRJOWNERCORP");
				
				for(int j=0;j<second_files.size();j++){
					Element el = second_files.get(j);
					String folderPath = el.getAttributeValue("FOLDERPATH");
					
					FileInfo finfo = new FileInfo();
					finfo.setFolderPath(folderPath==null?"未文件夹":folderPath.replaceAll("%20", " ").replaceAll("\\*",""));
					finfo.setFileType(el.getAttributeValue("FILETYPE"));
					finfo.setFilename(el.getAttributeValue("FILENAME"));
					finfo.setFileId(el.getAttributeValue("FILEID"));
					finfo.setArea(el.getText().trim());
					fileList.add(finfo);

				}
				
				//单项工程文件的路径列表
				childProject.setFileList(fileList);
				
				child_projects.add(childProject);
			}
			
			
			mainProject.setChildProjects(child_projects);    
			mproList.add(mainProject);
		}
		
		return mproList;
	}
	
	public String BuildXmlString(List<WholeSearchDoc> list){
		Element root = new Element("files");
		for(WholeSearchDoc wsd:list){
			Element file = new Element("file");
			PropertyDescriptor[] descriptors = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(wsd);
			for(int i=0;i<descriptors.length;i++){
				String fieldName = config.getConfigValue("attribute", "whole."+descriptors[i].getName());
				if(fieldName==null||fieldName.trim().equals("")){
					fieldName = descriptors[i].getName();
				}
				Element element = new Element(fieldName);
				Method method = Reflections.getGetterMethod(wsd.getClass(), descriptors[i].getName());
				Object[] args = null;
				try {
					Object obj = method.invoke(wsd, args);
					if(obj==null){
						obj = "";
					}
					element.setText(String.valueOf(obj));
					file.addContent(element);
				} catch (IllegalArgumentException e) {
					Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
				} catch (IllegalAccessException e) {
					Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
				} catch (InvocationTargetException e) {
					Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
				}
			}
			root.addContent(file);
		}
		Document document = new Document(root);
		XMLOutputter out = new XMLOutputter();
		String xmlStr = out.outputString(document);
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("c:/test.xml"),"utf-8"));
			out.setFormat(Format.getPrettyFormat());
			out.output(document, writer);   
            writer.flush();   
            writer.close();   
            
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
		} catch (FileNotFoundException e) {
			Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
		} catch (IOException e) {
			Logger.getLogger(ArchiveXmlPaser.class).warn("解析webservice接口传回的字符串异常:",e);
		}
		return xmlStr;
	}
}