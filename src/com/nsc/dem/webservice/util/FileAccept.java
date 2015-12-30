package com.nsc.dem.webservice.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileAccept implements FilenameFilter {
	
	private String filter = null;
	
	public FileAccept(String str){
		this.filter = str;
	}
	public boolean accept(File dir, String name) {
		return name.startsWith(filter);
	}

}
