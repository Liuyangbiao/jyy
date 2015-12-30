package com.nsc.base.util;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射类
 * 
 * 此类用于Java对象的底层操作，利用反射原理对对象进行调用。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:35:32 AM
 * @version
 */
@SuppressWarnings("all") 
public class Reflections {
	/**
	 * 使用target的method
	 * 
	 * @param method
	 *            方法
	 * @param target
	 *            对象实例
	 * @param args
	 *            方法参数
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(Method method, Object target, Object... args)
			throws Exception {
		try {
			return method.invoke(target, args);
		} catch (IllegalArgumentException iae) {
			String message = "Could not invoke method by reflection: "
					+ toString(method);
			if (args != null && args.length > 0) {
				message += " with parameters: (" + args.toString() + ')';
			}
			message += " on: " + target.getClass().getName();
			throw new IllegalArgumentException(message, iae);
		} catch (InvocationTargetException ite) {
			if (ite.getCause() instanceof Exception) {
				throw (Exception) ite.getCause();
			} else {
				throw ite;
			}
		}
	}

	/**
	 * 取得target属性field值
	 * 
	 * @param field
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public static Object get(Field field, Object target) throws Exception {
		try {
			return field.get(target);
		} catch (IllegalArgumentException iae) {
			String message = "Could not get field value by reflection: "
					+ toString(field) + " on: " + target.getClass().getName();
			throw new IllegalArgumentException(message, iae);
		}
	}

	/**
	 * 设定target属性field值为value
	 * 
	 * @param field
	 * @param target
	 * @param value
	 * @throws Exception
	 */
	public static void set(Field field, Object target, Object value)
			throws Exception {
		try {
			field.set(target, value);
		} catch (IllegalArgumentException iae) {
			// target may be null if field is static so use
			// field.getDeclaringClass() instead
			String message = "Could not set field value by reflection: "
					+ toString(field) + " on: "
					+ field.getDeclaringClass().getName();
			if (value == null) {
				message += " with null value";
			} else {
				message += " with value: " + value.getClass();
			}
			throw new IllegalArgumentException(message, iae);
		}
	}

	/**
	 * 调用取得方法
	 * @param field
	 * @param target
	 * @return
	 */
	public static Object getAndWrap(Field field, Object target) {
		try {
			return get(field, target);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new IllegalArgumentException("exception setting: "
						+ field.getName(), e);
			}
		}
	}

	/**
	 * 调用设置方法
	 * @param field
	 * @param target
	 * @param value
	 */
	public static void setAndWrap(Field field, Object target, Object value) {
		try {
			set(field, target, value);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new IllegalArgumentException("exception setting: "
						+ field.getName(), e);
			}
		}
	}

	/**
	 * 调用方法
	 * @param method
	 * @param target
	 * @param args
	 * @return 调用方法的返回值
	 */
	public static Object invokeAndWrap(Method method, Object target,
									   Object... args) {
		try {
			return invoke(method, target, args);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException("exception invoking: "
						+ method.getName(), e);
			}
		}
	}

	/**
	 * 将方法的名称转换成字符串
	 * 
	 * @param method
	 * @return
	 */
	private static String toString(Method method) {
		return method.getDeclaringClass().getName() + '.' + method.getName()
				+ '(' + method.getDeclaringClass().getName() + ')';
	}

	/**
	 * 将属性的名称转换成字符串
	 * 
	 * @param field
	 * @return
	 */
	private static String toString(Field field) {
		return field.getDeclaringClass().getName() + '.' + field.getName();
	}

	/**
	 * 生成一个以name字符串为名称的类的实例
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String name)
			throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(
					name);
		} catch (Exception e) {
			return Class.forName(name);
		}
	}

	/**
	 * Return's true if the class can be loaded using Reflections.classForName()
	 */
	public static boolean isClassAvailable(String name) {
		try {
			classForName(name);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public static Class<?> getCollectionElementType(Type collectionType) {
		if (!(collectionType instanceof ParameterizedType)) {
			throw new IllegalArgumentException(
					"collection type not parameterized");
		}
		Type[] typeArguments = ((ParameterizedType) collectionType)
				.getActualTypeArguments();
		if (typeArguments.length == 0) {
			throw new IllegalArgumentException(
					"no type arguments for collection type");
		}
		Type typeArgument = typeArguments.length == 1 ? typeArguments[0]
				: typeArguments[1]; // handle Maps
		if (!(typeArgument instanceof Class)) {
			throw new IllegalArgumentException("type argument not a class");
		}
		return (Class<?>) typeArgument;
	}

	/**
	 * 
	 * @param collectionType
	 * @return
	 */
	public static Class<?> getMapKeyType(Type collectionType) {
		if (!(collectionType instanceof ParameterizedType)) {
			throw new IllegalArgumentException(
					"collection type not parameterized");
		}
		Type[] typeArguments = ((ParameterizedType) collectionType)
				.getActualTypeArguments();
		if (typeArguments.length == 0) {
			throw new IllegalArgumentException(
					"no type arguments for collection type");
		}
		Type typeArgument = typeArguments[0];
		if (!(typeArgument instanceof Class)) {
			throw new IllegalArgumentException("type argument not a class");
		}
		return (Class<?>) typeArgument;
	}

	/**
	 * 返回clazz类中字符串name属性set方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getSetterMethod(Class<?> clazz, String name) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				if (Introspector.decapitalize(methodName.substring(3)).equals(
						name)) {
					return method;
				}
			}
		}
		throw new IllegalArgumentException("no such setter method: "
				+ clazz.getName() + '.' + name);
	}

	/**
	 * 返回clazz类中字符串name属性get方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getGetterMethod(Class<?> clazz, String name) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.matches("^(get|is).*")
					&& method.getParameterTypes().length == 0) {
				if (Introspector.decapitalize(methodName.substring(3)).equalsIgnoreCase(
						name)) {
					return method;
				}
			}
		}
		throw new IllegalArgumentException("no such getter method: "
				+ clazz.getName() + '.' + name);
	}

	/**
	 * 按标注得到所有的Getter方法
	 * @param clazz
	 * @param annotation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Method> getGetterMethods(Class<?> clazz, Class annotation) {
		List<Method> methods = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		return methods;
	}

	/**
	 * 得到所有的Getter方法
	 * @param clazz
	 * @return
	 */
	public static List<Method> getGetterMethods(Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.getName().matches("^(get|is).*")
					&& method.getParameterTypes().length == 0) {
				methods.add(method);
			}
		}
		return methods;
	}
	
	/**
	 * 得到本类所有的Getter方法,不包括继承来的
	 * @param clazz
	 * @return
	 */
	public static List<Method> getCurrentClassGetterMethods(Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().startsWith("get")
					&& method.getParameterTypes().length == 0) {
				methods.add(method);
			}
		}
		return methods;
	}
	
	/**
	 * 返回clazz类中以字符串name内容为名称的属性
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Field getField(Class<?> clazz, String name) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field:fields){
					if(field.getName().equalsIgnoreCase(name)){
						return  field;
					}
				}
			} catch (Exception nsfe) {
			}
		}
		throw new IllegalArgumentException("no such field: " + clazz.getName()
				+ '.' + name);
	}
	
	/**
	 * 返回clazz类中字符串name名称的方法(不带参数）
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String name) {
		return getMethod(clazz,name,null);
	}

	/**
	 * 返回clazz类中字符串name名称的方法
	 * @param clazz
	 * @param name
	 * @param types
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String name,Class<?>[] types) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(name,types);
			} catch (NoSuchMethodException nsme) {
			}
		}
		throw new IllegalArgumentException("no such method: " + clazz.getName()
				+ '.' + name);
	}

	/**
	 * 根据标注得到制定类的所有字段
	 * @param clazz
	 * @param annotation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Field> getFields(Class<?> clazz, Class annotation) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			for (Field field : superClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(annotation)) {
					fields.add(field);
				}
			}
		}
		return fields;
	}

	/**
	 * 得到指定类的所有字段
	 * @param clazz
	 * @return
	 * @deprecated 此方法慢
	 */
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			for (Field field : superClass.getDeclaredFields()) {
				fields.add(field);
			}
		}
		return fields;
	}

	/**
	 * 按名称得到方法
	 * @param annotation
	 * @param name
	 * @return
	 */
	public static Method getMethod(Annotation annotation, String name) {
		try {
			return annotation.annotationType().getMethod(name);
		} catch (NoSuchMethodException nsme) {
			return null;
		}
	}

	/**
	 * 判断clazz的类型是否是字符串name内容的的类型
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static boolean isInstanceOf(Class<?> clazz, String name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
			if (name.equals(c.getName())) {
				return true;
			}
		}
		for (Class<?> c : clazz.getInterfaces()) {
			if (name.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}
}
