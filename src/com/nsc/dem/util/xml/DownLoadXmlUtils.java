package com.nsc.dem.util.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.nsc.dem.action.bean.DownFileBean;
import com.nsc.dem.util.download.DownLoadFileList;

public class DownLoadXmlUtils {

	/**
	 * 产生下载文件清单
	 * @param downFileList   文件列表
	 * @param packageName    包名
	 * @return
	 */
	public static String createDownloadList(Collection<DownFileBean> downFileList){
		if(downFileList == null || downFileList.size() == 0)
			return null;
		
		String fileName = UUID.randomUUID().toString();
		
		Document document = DocumentHelper.createDocument();
		Element rootElement = (Element) DocumentHelper.createElement("files");
		for(DownFileBean downFile : downFileList){
			Element element = (Element) DocumentHelper.createElement("file");
			element.addAttribute("id",downFile.getDocid());
			element.addAttribute("name",downFile.getName());
			element.addAttribute("code",downFile.getCode());
			element.addAttribute("path",downFile.getPath());
			element.addAttribute("projectId",downFile.getProjectId());
			element.addAttribute("suffix",downFile.getSuffix());
			rootElement.add(element);
		}
		document.add(rootElement);
		String value = XmlUtils.document2String(document);
		DownLoadFileList.createDownLoadFileList().addDownLoadList(fileName, value);
		return fileName;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<DownFileBean> parserDownloadList(String downloadList) throws Exception{
		Document document = DocumentHelper.parseText(downloadList);
		List<DownFileBean>  downLoadFiles = new ArrayList<DownFileBean>();
		List<Element> allElement = document.selectNodes("//file");
		for(Element ele : allElement){
			DownFileBean bean = new DownFileBean();
			bean.setDocid(ele.attributeValue("id"));
			bean.setName(ele.attributeValue("name"));
			bean.setCode(ele.attributeValue("code"));
			bean.setPath(ele.attributeValue("path"));
			bean.setProjectId(ele.attributeValue("projectId"));
			bean.setSuffix(ele.attributeValue("suffix"));
			downLoadFiles.add(bean);
		}
		
		return downLoadFiles;
	}
	
	
	
}
