package com.nsc.dem.util.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ProjectParse {
	private static SAXReader reader = new SAXReader();

	public static void remove() throws DocumentException, IOException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Document document = getDocument(pro.getAbsolutePath());
				Element element = (Element) document.selectSingleNode("//MAIN");
				String unit = element.attributeValue("OWNER_UNIT");
				File temp = new File("D:\\project\\"+unit);
				if(!temp.exists()){
					temp.mkdirs();
				}
				
				File newfile = new File(temp, pro.getName());
				if(!newfile.exists())
					newfile.createNewFile();
				FileInputStream fis = new FileInputStream(pro);
				FileOutputStream out = new FileOutputStream(newfile);
				int bytes = 0;
				int bytesum = 0;
				int byteread = 0;
				byte[] buffer = new byte[1444];
				while ((bytes = fis.read(buffer)) != -1) {
					bytesum += byteread;
					out.write(buffer, 0, bytes);
				}
				fis.close();
				out.close();
			}
		}
	}
	
	
	public static void updateFileDownLoad() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Document document = getDocument(pro.getAbsolutePath());
				Element elementIP = (Element) document.selectSingleNode("//FTP_SERVER");
				elementIP.setText("192.168.3.125");
				Element elementName = (Element) document.selectSingleNode("//FTP_USER");
				elementName.setText("IUSR_NSC-EDM");
				Element elementPass = (Element) document.selectSingleNode("//FTP_PWD");
				elementPass.setText("edm");
				saveDocument(document,pro.getAbsoluteFile());
			}
		}
	}
	
	public static void updateDownload() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Document document = getDocument(pro.getAbsolutePath());
				Element elementIP = (Element) document.selectSingleNode("//FTP_SERVER");
				elementIP.setText("192.168.3.125");
				Element elementName = (Element) document.selectSingleNode("//FTP_USER");
				elementName.setText("IUSR_NSC-EDM");
				Element elementPass = (Element) document.selectSingleNode("//FTP_PWD");
				elementPass.setText("edm");
				Element elementMain = (Element) document.selectSingleNode("//MAIN");
				String voltage = elementMain.attributeValue("VOLTAGE_LEVEL");
				if(voltage.indexOf("kV") != -1){
					String vol = voltage.substring(0, voltage.indexOf("kV"));
					int volint = Integer.parseInt(vol);
					String location = "";
					if(volint >= 500 ){
						location = "国家电网公司";
					}
					elementMain.addAttribute("LOCATION", location);
				}
				saveDocument(document,pro.getAbsoluteFile());
			}
		}
				
	}
		
	public static void modifyUnit() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Logger.getLogger(ProjectParse.class).info(pro.getAbsolutePath());
				Document document = getDocument(pro.getAbsolutePath());
				Element elementIP = (Element) document.selectSingleNode("//MAIN");
				elementIP.addAttribute("OWNER_UNIT", "甘肃省电力公司");
				saveDocument(document,pro.getAbsoluteFile());
			}
		}
	}
	
	
	public static void modifyReview() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Logger.getLogger(ProjectParse.class).info(pro.getAbsolutePath());
				Document document = getDocument(pro.getAbsolutePath());
				Element elementIP = (Element) document.selectSingleNode("//MAIN");
				elementIP.addAttribute("REVIEW_UNIT", "甘肃省电力公司");
				saveDocument(document,pro.getAbsoluteFile());
			}
		}
	}
	
	
	public static void outputProjectInfo() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Document document = getDocument(pro.getAbsolutePath());
				Element element = (Element) document.selectSingleNode("//MAIN");
				Logger.getLogger(ProjectParse.class).info(pro.getName()+",");
				Logger.getLogger(ProjectParse.class).info(element.attributeValue("OWNER_UNIT")+",");
				Logger.getLogger(ProjectParse.class).info(element.attributeValue("NAME")+",");
				Logger.getLogger(ProjectParse.class).info(element.attributeValue("VOLTAGE_LEVEL")+",");
				Logger.getLogger(ProjectParse.class).info(element.attributeValue("REVIEW_UNIT"));
				/*
				Element element = (Element) document.selectSingleNode("//FILE_PATH");
				String path = element.getText();
				path = path.replace("/", "\\");
				//path = path.substring(0, path.lastIndexOf("//"));
				*/
			}
		}
	}
	
	
	
	public static Document getDocument(String fileName) throws DocumentException{
		reader.setEncoding("UTF-8");
		Document document = reader.read(fileName);
		return document;
	}
	
	public static  void saveDocument(Document document, File file) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter write = new XMLWriter(new FileOutputStream(file),
					format);
			write.write(document);
			write.close();
		} catch (IOException e) {
			Logger.getLogger(ProjectParse.class).warn(e.getMessage());
		}
	}
	
	
	
	public static void outputProjectInfoDataSynList() throws DocumentException{
		File file = new File("D:\\project");
		File[] lists = file.listFiles();
		
		for(File pro : lists){
			if(pro.getName().endsWith(".xml")){
				Document document = getDocument(pro.getAbsolutePath());
				List<Element> eles = document.selectNodes("//project");
				for(Element ele : eles){
					Logger.getLogger(ProjectParse.class).info(ele.attributeValue("owerUnitName"));
				}
				
			}
		}
	}
}
