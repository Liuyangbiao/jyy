package com.nsc.base.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.nsc.base.recource.ResourceLoader;
/**
 * 配置文件获取类
 * 
 * <p>
 * 通过此类加载给定路径下的配置文件。 此类采用单实例模式，通过<b>getInstance()</b>方法获得。 目前仅支持properties文件的加载。
 * <p/>
 * 
 * @author bs-team
 * 
 * @date Oct 19, 2010 9:44:15 AM
 * @version
 */
public class Configurater {

	private static Configurater config;
	private Map<String, Properties> configList;
	private ServletContext servletContext;

	private Configurater(ServletContext servletContext) {
		this.servletContext = servletContext;
		configList = new HashMap<String, Properties>();
	}

	/**
	 * 取得ServletContext
	 * 
	 * @return
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 构造函数
	 * 
	 * @return
	 */
	public static Configurater getInstance() {
		return getInstance(null);
	}

	/**
	 * 构造函数
	 * 
	 * @param servletContext
	 * @return
	 */
	public static synchronized Configurater getInstance(
			ServletContext servletContext) {

		if (config == null) {
			config = new Configurater(servletContext);

			try {
				config.loadConfigure(ConstConfig.GLOBAL_PROPERTIES,
						"global.properties");

				String configuers = config.getConfigValue(
						ConstConfig.GLOBAL_PROPERTIES,
						ConstConfig.CONFIGUERS_KEY);
				// 加载其他配置文件
				if (configuers != null) {
					String[] configs = configuers.split(",");

					for (String configPath : configs) {
						String[] names = configPath.split(":");
						if (names != null && names.length == 2) {
							String suffix = names[1].substring(names[1]
									.indexOf(".") + 1);
							if (suffix.equals("properties")) {
								config.loadConfigure(names[0], names[1]);
							} else if (suffix.equals("xml")) {
								// 加载xml配置文件
								// config.loadXml(names[0], names[1]);
							}
						}
					}
				}
			} catch (IOException e) {
				Logger.getLogger(Configurater.class).warn(e.getMessage());
			}
		}

		return config;
	}

	/**
	 * 加载配置文件(Properties文件)
	 * 
	 * @param srcPath
	 *            资源全路径
	 * @return 配置文件对象
	 * @throws IOException
	 */
	public Properties loadConfigure(String nameSpace, String srcPath)
			throws IOException {

		InputStream is = ResourceLoader.getResourceAsStream(srcPath.trim());

		Properties properties = new Properties();

		properties.load(is);

		this.configList.remove(nameSpace.trim().toUpperCase());

		this.configList.put(nameSpace.trim().toUpperCase(), properties);

		Logger.getLogger(Configurater.class).info(
				"配置文件: " + nameSpace + "加载成功！");

		return properties;
	}

	/**
	 * 根据命名空间和键取值
	 * 
	 * @param nameSpace
	 * @param key
	 * @return
	 */
	public String getConfigValue(String nameSpace, String key) {

		if (nameSpace != null && nameSpace.length() > 0) {
			if (configList.get(nameSpace.toUpperCase()) instanceof Properties) {
				Properties properties = (Properties) configList.get(nameSpace
						.toUpperCase());
				return properties == null ? null : properties.getProperty(key);
			}
		}
		for (String name : configList.keySet()) {
			if (configList.get(name) instanceof Properties) {
				Properties properties = (Properties)configList.get(name);
				if (properties.containsKey(key))
					return properties.getProperty(key);
			}
		}

		return null;
	}

	/**
	 * 根据命名空间取得properties
	 * @param nameSpace
	 * @return
	 */
	public Properties getConfig(String nameSpace){
		if (nameSpace != null && nameSpace.length() > 0) {
			if (configList.get(nameSpace.toUpperCase()) instanceof Properties) {
				Properties properties = (Properties) configList.get(nameSpace
						.toUpperCase());
				return properties;
			}
		}
		return null;
	}
	/**
	 * 根据键取值
	 * 
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key) {
		return getConfigValue(null, key);
	}

	/**
	 * 根据键取值
	 * 
	 * @param key
	 * @return
	 */
	public Object getConfigValueXml(String key) {
		return configList.get(key);
	}

	// public void loadXml(String name, String path) {
	// String xmlPath = ResourceLoader.getDefaultClassLoader().getResource(
	// path).getPath();
	// Document doc = XmlParse.parseXml(xmlPath);
	// configList.put(name, doc);
	// Logger.getLogger(Configurater.class).info("配置文件: " + name + "加载成功！");
	// }

	/**
	 * 根据键正则匹配取值
	 * 
	 * @param regExp
	 *            key的正则表达示
	 * @return
	 */
	public List<String> like(String regExp) {
		List<String> valueList = new ArrayList<String>();

		// 循环出key，根据正则匹配key
		for (String name : configList.keySet()) {
			Properties properties =  configList.get(name);
			for (Iterator<Object> iter = properties.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				if (key.startsWith(regExp)) {
					valueList.add(getConfigValue(key));
				}
			}
		}
		return valueList;
	}
}
