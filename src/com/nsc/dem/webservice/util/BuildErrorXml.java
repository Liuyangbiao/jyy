package com.nsc.dem.webservice.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class BuildErrorXml {
	@SuppressWarnings("unchecked")
	public static String buildXml(List<Map> errorList){
		
		Document document = null;
		Element root = new Element("Error");
		
		for(Map<String, List<String>> map:errorList){
			Set set =  map.entrySet();
			Iterator it = set.iterator();
			while(it.hasNext()){
				Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>)it.next();
				String projectId = entry.getKey();
				Element mainProjId = new Element("SbdPrjId");
				mainProjId.setText(projectId);
				root.addContent(mainProjId);
				
				for(String fileId:entry.getValue()){
					Element errorId = new Element("ErrorFileId");
					errorId.setText(fileId);
					root.addContent(errorId);
				}
			}
		}
		List elements = root.getChildren("ErrorFileId");
		if(elements==null||elements.size()==0){
			return null;
		}
		document = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		out.setFormat(format);
		String stream = out.outputString(document);
		return stream;
	}
}
