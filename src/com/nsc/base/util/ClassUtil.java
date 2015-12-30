package com.nsc.base.util;

/**
 * Class操作类
 * 
 * 此类用于获取Class的一般信息。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:30:26 AM
 * @version
 */
public class ClassUtil {

	/**
	 * 返回对象的名称（不包含包的名字）
	 * 
	 * @param c
	 * @return 对象的名称（不包含包的名字）
	 */
	public static String getClassName(Class<?> c) {
		String FQClassName = c.getName();
		int firstChar;
		firstChar = FQClassName.lastIndexOf('.') + 1;
		if (firstChar > 0) {
			FQClassName = FQClassName.substring(firstChar);
		}
		return FQClassName;
	}

	/**
	 * 返回该类的包和类名称
	 * 
	 * @param c
	 * @return 该类的包和类名称
	 */
	public static String getFullClassName(Class<?> c) {
		return c.getName();
	}

	/**
	 * 只返回该类所在包的名称，若没有包则返回空字符串
	 * 
	 * @param c
	 * @return 该类所在包的名称，若没有包则返回空字符串
	 */
	public static String getPackageName(Class<?> c) {
		String fullyQualifiedName = c.getName();
		int lastDot = fullyQualifiedName.lastIndexOf('.');
		if (lastDot == -1) {
			return "";
		}
		return fullyQualifiedName.substring(0, lastDot);
	}
}
