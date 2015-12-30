package com.nsc.dem.util.ftp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * jdom解析xml文件
 * 
 * @author ly *
 */
public class XmlParse {

	public XmlParse() {
	}

	/**
	 * 解析xml文件
	 * 
	 * @param xmlFile
	 */
	public static Document parseXml(String path) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(path));
		} catch (JDOMException e) {
			Logger.getLogger(XmlParse.class).warn(e.getMessage());
		} catch (IOException e) {
			Logger.getLogger(XmlParse.class).warn(e.getMessage());
		}
		return doc;

	}

	/**
	 * 根据出入的Document的key返回对应的value
	 * @param doc 
	 * @param key 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String[] getXmlInfo(Document doc,String key){
		String[] str=new String[4];
		try {
			List<Element> roots = (List) XPath.selectNodes(doc, "//unit[@code='"+key+"']/parent::*");
	        if(roots.size()>0){
	        	for (Element el : roots) {
		            str[0]=el.getAttributeValue("hostname");
		            str[1]=el.getAttributeValue("port");
		            str[2]=el.getAttributeValue("username");
		            str[3]=el.getAttributeValue("password");
		        }
	        }else{
	        	List<Element> defaultFtp = (List) XPath.selectNodes(doc, "//ftp[@default='Y']");
	        	for (Element el : defaultFtp) {
		            str[0]=el.getAttributeValue("hostname");
		            str[1]=el.getAttributeValue("port");
		            str[2]=el.getAttributeValue("username");
		            str[3]=el.getAttributeValue("password");
		        }
	        }
		} catch (JDOMException e) {
			Logger.getLogger(XmlParse.class).warn(e.getMessage());
		}
		return str;
	}
	
	
//	@SuppressWarnings("static-access")
//	public static void main(String[] args) throws JDOMException, IOException {
//		Xmlutil xml=new Xmlutil();
//		xml.parseXml("src/ftp.xml","08012901s");
//	}

    
	
}