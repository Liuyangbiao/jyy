package com.nsc.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.directwebremoting.util.Logger;

public class PropertyModify {

	
	//读文件   
	public static String readFile(String filePath, String parameterName) throws IOException, FileNotFoundException {   
	    String result = null;   
	    File file = new File(filePath);   
	    if (file.exists()) {   
	        FileInputStream fis = new FileInputStream(file);   
	        byte[] b = new byte[fis.available()];   
	        fis.read(b);   
	        result = new String(b, "UTF-8");   
	        fis.close();   
	    }   
	    return result;   
	}   
	//修改后存储   
	public static void saveFile(String content, String path, String fileName) throws IOException {
	    File f = new File(path);   
	    if (!f.exists()) {   
	        f.mkdirs();   
	    }   
	    File fn = new File(f, fileName);   
	    FileOutputStream fos = new FileOutputStream(fn);   
	    fos.write(content.getBytes());   
	    fos.close();   
	}   
	//删除旧文件   
	public static void deleteFile(String path) throws IOException {   
	    File f = new File(path);   
	    if (f.exists()) {   
	        f.delete();   
	} else {   
	        throw new IOException("未找到相关文件");   
	    }   
	}   
	//执行方法   
	public static boolean writeProperties(String filePath, String parameterName, String parameterValue) {   
	    boolean flag = false;   
	    try {   
	        //取得文件所有内容   
	        String all = PropertyModify.readFile(filePath, parameterName);   
	        String result = null;   
	        //如果配置文件里存在parameterName   
	        if (all.indexOf(parameterName) != -1) {   
	            //得到parameterName前的字节数   
	            int a=all.indexOf(parameterName);   
	            //取得以前parameterName的值   
	            String old = readProperties(filePath, parameterName);   
	            //得到parameterName值前的字节数   
	            int b=a+(parameterName.length()+"=".length());   
	            //新的properties文件所有内容为：旧的properties文件中内容parameterName+"="+新的parameterName值(parameterValue)+旧的properties文件中parameterName值之后的内容   
	            result=all.substring(0,a)+parameterName+"="+parameterValue+all.substring(b+ToUnicode.convert(old).length(),all.length());   
	        }   
	        //删除以前的properties文件   
	        PropertyModify.deleteFile(filePath);   
	        //存储新文件到以前位置   
//	        String[] arrPath = filePath.split("WEB-INF");   
//	        String path = arrPath[0]+"WEB-INF/configs";
	        String path=filePath.substring(0,filePath.lastIndexOf("/"));
	        String name=filePath.substring(filePath.lastIndexOf("/")+1);
	        Logger.getLogger(PropertyModify.class).info("filePath-====================="+filePath);
	        PropertyModify.saveFile(result, path,name);   
	        flag=true;   
	} catch (IOException e) {   
    	Logger.getLogger(PropertyModify.class).warn(e.getMessage());  
	        flag=false;   
	    }   
	    return flag;   
	}   
	//读properties文件，Properties方法   
	public static String readProperties(String filePath, String parameterName) {   
	    String value = "";   
	    Properties prop = new Properties();   
	    try {   
	        InputStream fis = new FileInputStream(filePath);   
	        prop.load(fis);   
	        value = prop.getProperty(parameterName);   
	    } catch (IOException e) {  
	    	Logger.getLogger(PropertyModify.class).warn(e.getMessage());
	    }   
	    return value;   
	}   
}
