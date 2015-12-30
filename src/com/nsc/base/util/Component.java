package com.nsc.base.util;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nsc.base.jsf.listener.AppContextProvider;
import com.nsc.base.util.AppException;

/**
 * 组件获取类
 * 
 * 此类用于对JSF和Spring中定义的组件进行获取。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:32:52 AM
 * @version
 */
public class Component {

	/**
	 * 得到指定类的实例
	 * @param clazz
	 * @return 对象实例
	 */
	@SuppressWarnings("unchecked")
	public static Object getInstance(Class<?> clazz,ServletContext cxt) {

		Map<String,Object> map=WebApplicationContextUtils.getWebApplicationContext(cxt)
					.getBeansOfType(clazz);
		
		if(map==null || map.size()==0){
			throw new AppException(
					"No bean was defined in the ApplicationContext.xml with type: "
							+ clazz.getName(), "sys.bean.instance.get", null,
					new String[] { clazz.getName() });
		}
		
		return map.entrySet().iterator().next().getValue();
	}

	/**
	 * 按类的别名得到对象实例
	 * @param alias
	 * @return 对象实例
	 */
	public static Object getInstance(String alias,ServletContext cxt) {
		Object object = WebApplicationContextUtils.getWebApplicationContext(cxt).getBean(alias);
		
		if (object == null) {
			throw new AppException(
					"No bean was defined in the ApplicationContext.xml with name: "
							+ alias, "sys.bean.instance.get", null,
					new String[] { alias });
		}

		return object;
	}
	
	/**
	 * 得到指定类的别名
	 * @param clazz
	 * @return 别名
	 */
	@SuppressWarnings("unused")
	private static String getAlias(Class<?> clazz) {
		String alias;

		try {
			alias = (String) clazz.getField("ALIAS").get(null);
		} catch (NoSuchFieldException e) {
			String sid = "sys.bean.field.getvalue";
			throw new AppException(e, sid, null, new String[] {
					clazz.getName(), "ALIAS" });
		} catch (SecurityException e) {
			String sid = "sys.bean.field.getvalue";
			throw new AppException(e, sid, null, new String[] {
					clazz.getName(), "ALIAS" });
		} catch (IllegalAccessException e) {
			String sid = "sys.bean.field.getvalue";
			throw new AppException(e, sid, null, new String[] {
					clazz.getName(), "ALIAS" });
		}

		return alias;
	}
	
	public static Class<?> getType(String alias){
		return AppContextProvider.getCxt().getType(alias);
	}
}
