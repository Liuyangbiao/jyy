package com.nsc.dem.util.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.xwork.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class FailFileIDXMLUtils {
	
	private static XmlUtils util = XmlUtils.getInstance("failFileID.xml");
	
	/**
	 * 国网保存省公司/区域接收失败的ID
	 *   以及区域到省公司发送失败的ID
	 * @param failIDS
	 * @param unitCode
	 */
	public static void saveFailFileID(String failIDS, String from, String to){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Document document = util.getDocument();
		Element element = DocumentHelper.createElement("fail");
		element.addAttribute("from", from);
		element.addAttribute("to", to);
		element.addAttribute("time", format.format(new Date()));
		element.setText(failIDS);
		document.getRootElement().add(element);
		util.saveDocument(document);
	}	
	
	/**
	 * 国网获取所有省/区域接收失败的ID
	 * @param unitCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getFailFileIDByCode(String countryId){
		if(StringUtils.isBlank(countryId))
			return "";
		Document document = util.getDocument();
		List<Element> lists = document.selectNodes("//fail[@to='"+countryId+"']");
		if(lists.size() <= 0){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(Element element : lists){
			buffer.append(element.getTextTrim());
			buffer.append(",");
			document.getRootElement().remove(element.detach());
		}
		if(buffer.length() > 1){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		util.saveDocument(document);
		
		return deleteRepeatID(buffer);
	}
	
	
	/**
	 * 获取区域到省公司发送失败的ID
	 * @param unitCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getFailFileIDByCode(String from, String to){
		if(StringUtils.isBlank(from) || StringUtils.isBlank(to))
			return "";
		
		Document document = util.getDocument();
		List<Element> lists = document.selectNodes("//fail[@from='"+from+"' and @to='"+ to +"']");
		if(lists.size() <= 0){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(Element element : lists){
			buffer.append(element.getTextTrim());
			buffer.append(",");
			document.getRootElement().remove(element.detach());
		}
		if(buffer.length() > 1){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		util.saveDocument(document);
		
		return deleteRepeatID(buffer);
	}

	
	//去掉重复的ID
	public static String deleteRepeatID(StringBuffer buffer) {
		String[] ids = buffer.toString().split(",");
		List<String> allID = new ArrayList<String>();
		if(ids.length <= 0 )
			return "";
		
		for(String id : ids){
			if(allID.contains(id))
				continue;
			allID.add(id);
		}
		buffer = new StringBuffer();
		for(String id : allID){
			buffer.append(id+",");
		}
		
		if(buffer.length() > 1){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		
		return buffer.toString();
	}
	
	/**
	 * 保存上传失败的文件ID
	 */
	public static void saveUploadFileFailIDs(String flag, String id, String from, String to){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Document document = util.getDocument();
		Element element = DocumentHelper.createElement(flag);
		element.addAttribute("from", from);
		element.addAttribute("to", to);
		element.addAttribute("time", format.format(new Date()));
		element.setText(id);
		document.getRootElement().add(element);
		util.saveDocument(document);
	}
	
	

	@SuppressWarnings("unchecked")
	public static String getSuccessFileIDByCode(String from, String to){
		if(StringUtils.isBlank(from) || StringUtils.isBlank(to))
			return "";
		
		Document document = util.getDocument();
		List<Element> lists = document.selectNodes("//success[@from='"+from+"' and @to='"+ to +"']");
		if(lists.size() <= 0){
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for(Element element : lists){
			buffer.append(element.getTextTrim());
			buffer.append(",");
			document.getRootElement().remove(element.detach());
		}
		if(buffer.length() > 1){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		util.saveDocument(document);
		
		return deleteRepeatID(buffer);
	}
}
