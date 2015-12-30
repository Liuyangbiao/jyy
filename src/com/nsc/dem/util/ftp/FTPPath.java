package com.nsc.dem.util.ftp;

import com.nsc.dem.bean.archives.TDoc;

public class FTPPath {
	
	public static String getFTPPath(TDoc tdoc){
		String filePath = tdoc.getPath();
		
		//回去文档的所属业主单位
		String unitName=filePath.replaceAll("/","\\\\");
		unitName=unitName.substring(unitName.indexOf("\\")+1);
		unitName=unitName.substring(unitName.indexOf("\\")+1);
		unitName=unitName.substring(0,unitName.indexOf("\\"));
		
		return unitName;
	}

}
