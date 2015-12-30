package com.nsc.dem.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;

public class XmlUtils {
	private  SAXReader reader = new SAXReader();
	private  String fileName = getPath() + "task.xml";

	public static XmlUtils getInstance(String fileName){
		
		return new XmlUtils(fileName);
	}
	
	public static XmlUtils getInstance(){
		return new XmlUtils();
	}
	
	private XmlUtils(String fileName){
		File file = new File (getPath() + fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.getLogger(XmlUtils.class).warn(e);
			}
		}
		
		this.fileName = getPath() + fileName;
	}
	
	private XmlUtils(){
		reader = new SAXReader();
	}
	

	/**
	 * 获取Document对象
	 * 
	 * @return
	 */
	public  Document getDocument() {
		Document document = null;
		try {
			reader.setEncoding("UTF-8");
			document = reader.read(fileName);
		} catch (DocumentException e) {
			Logger.getLogger(XMLUtils.class).warn(e);
		}
		return document;
	}

	/**
	 * 保存Document对象
	 * 
	 * @param document
	 */
	public  void saveDocument(Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter write = new XMLWriter(new FileOutputStream(fileName),
					format);
			write.write(document);
			write.close();
		} catch (IOException e) {
			Logger.getLogger(XmlUtils.class).warn(e.getMessage());
		}
	}

	/**
	 * 获取web-info目录
	 * 
	 * @return
	 */
	public static String getPath() {
		String path = null;
		try {
			path = XmlUtils.class.getResource("").toURI().getPath();
			path = path.substring(0, path.indexOf("classes"));
			return path;
		} catch (URISyntaxException e) {
			Logger.getLogger(XMLUtils.class).warn(e);
		}
		return null;
	}

	
	
	
	@SuppressWarnings("unchecked")
	public  void deleteAllNode(Document document) {
		Element rootElement = document.getRootElement();
		List<Element> elements = rootElement.elements();
		for (Element element : elements) {
			rootElement.remove(element);
		}
		saveDocument(document);
	}

	/**
	 *将文件的内容转为字符串
	 * 
	 */
	public static String document2String(Document document) {
		ByteArrayOutputStream boos = new ByteArrayOutputStream();
		OutputFormat format = new OutputFormat("", true, "utf-8");
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(boos, format);
			writer.write(document);
			return boos.toString("utf-8");
		} catch (Exception e) {
			Logger.getLogger(XMLUtils.class).warn(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				Logger.getLogger(XMLUtils.class).warn(e);
			}
		}
		return null;
	}
	
	
}
