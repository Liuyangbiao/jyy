package com.nsc.base.recource;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 资源加载类
 * 
 * 此类用于资源的加载，将在工程运行环境查找指定的资源，并加载成字节流。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:27:51 AM
 * @version
 */
public class ResourceLoader {
	
	/**
	 * 取得资源字节流
	 * @param resource
	 * @param servletContext
	 * @return 资源字节流
	 */
	public static InputStream getResourceAsStream(String resource,
												  ServletContext servletContext) {
		String stripped = resource.startsWith("/") ? resource.substring(1)
				: resource;
		InputStream stream = null;

		if (servletContext != null) {
			stream = servletContext.getResourceAsStream(resource);
		}

		if (stream == null) {
			stream = getResourceAsStream(stripped);
		}

		return stream;
	}
	
	/**
	 * 取得资源字节流
	 * @param path
	 * @return 字节流
	 */
	public static InputStream getResourceAsStream(String path){
		ClassLoader classLoader=getDefaultClassLoader();
		
		return classLoader.getResourceAsStream(path);
	}
	
	/**
	 * 取得默认类加载工具
	 * @return 类加载工具
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ResourceLoader.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	  * 获取项目根目录的绝对路径
	  * 
	  * @return 项目根目.例如<br/> F:\tomcat\webapps\J2EEUtil\
	  * */
	 public static String getWebRootPath(
	   HttpServletRequest request) {
	  return request.getSession().getServletContext().getRealPath("/");
	 }
}
