package com.nsc.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.nsc.base.annotation.AuditAnnotation;
import com.nsc.base.annotation.IdentifierFieldAnnotation;
import com.nsc.base.annotation.StringMessageAnnotation;
import com.nsc.base.conf.Configurater;
import com.nsc.dem.util.log.LogManager;

@SuppressWarnings("all")
public class AuditTable {
	private Configurater config = null;

	private String[] auditConditions;

	private String[] auditables;

	private String[] auditOperates;

	private String[] auditClass;

	private static AuditTable instance = null;

	private static Object lock = new Object();
	
	private Logger logger;

	private AuditTable() throws Exception {
		config = Configurater.getInstance();
		logger = Logger.getLogger(AuditTable.class);
		String auditConfig = config.getConfigValue("audit_condition");
		if (auditConfig == null || auditConfig.trim().equals("")) {
			logger.warn("global.properties中没有审计条件的配置!");
			instance = null;
			return;
		} else {
			if (auditConfig.indexOf("@") == -1
					|| auditConfig.indexOf(":") == -1) {
				logger.warn("global.properties中审计条件的配置不正确!");
				throw new Exception("global.properties中审计条件的配置不正确!");
			}
			auditConditions = config.getConfigValue("audit_condition").split(
					";");
		}
		if (auditConditions != null || auditConditions.length > 0) {
			auditables = new String[auditConditions.length];
			auditClass = new String[auditConditions.length];
			auditOperates = new String[auditConditions.length];
			for (int i = 0; i < auditConditions.length; i++) {
				auditables[i] = auditConditions[i].split(":")[0].split("@")[0];
				auditClass[i] = auditConditions[i].split(":")[0].split("@")[1];
				auditOperates[i] = auditConditions[i].split(":")[1];
			}
		}

	}

	public static AuditTable getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					try {
						instance = new AuditTable();
					} catch (Exception e) {
						Logger.getLogger(AuditTable.class).warn("异常:",e);
					}
				}
			}
		}
		return instance;
	}

	/**
	 * 是否需要审计
	 */
	public boolean isNeedAudit(Object obj, String operate) {
		String name = obj.getClass().getSimpleName();
		for (int i = 0; auditables != null && i < auditables.length; i++) {
			if (auditables[i].replaceAll("_", "").equalsIgnoreCase(name)) {
				if (auditOperates[i].toUpperCase().indexOf(
						operate.toUpperCase()) != -1) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 获取审计类
	 */
	public String getAuditClass(Object obj) {
		String name = obj.getClass().getSimpleName();
		for (int i = 0; i < auditables.length; i++) {
			if (auditables[i].replaceAll("_", "").equalsIgnoreCase(name)) {
				return auditClass[i];
			}
		}

		return null;
	}

	public List getUpdateColumns(Object originality, Object newInstance) {
		List list = null;
		if (originality != null
				&& newInstance != null
				&& (!originality.getClass().getName().equals(
						newInstance.getClass().getName()))) {
			logger.warn(AuditTable.class.getName() + ":获取更新列时，两个对象类不同");
		} else {
			list = new ArrayList();
			List<Method> methods = Reflections
					.getCurrentClassGetterMethods(originality.getClass());
			for (Method method : methods) {
				try {
					String fieldName = method.getName().substring(3);
					Object result1 = method.invoke(originality, null);
					Object result2 = method.invoke(newInstance, null);
					boolean isEqual = compareFieldValue(originality.getClass(),
							fieldName, result1, result2);
					if (!isEqual) {
						list.add(fieldName);
					}
				} catch (IllegalArgumentException e) {
					Logger.getLogger(AuditTable.class).warn("",e);
				} catch (IllegalAccessException e) {
					Logger.getLogger(AuditTable.class).warn("",e);
				} catch (InvocationTargetException e) {
					Logger.getLogger(AuditTable.class).warn("",e);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return 相等返回true
	 */
	private boolean compareFieldValue(Class c, String fieldName, Object value1,
			Object value2) {
		if (value1 != null && value2 != null) {
			Field field = Reflections.getField(c, fieldName);
			Class clasz = field.getType();
			if (clasz.getName().equals("java.lang.String")) {
				return value1.equals(value2);
			} else if (clasz.getName().equals("java.util.Date")) {
				return ((Date) value1).getTime() == ((Date) value2).getTime();
			} else if (clasz.getName().equals("java.sql.Timestamp")) {
				return ((Timestamp) value1).getTime() == ((Timestamp) value2)
						.getTime();
			} else if (clasz.getName().equals("java.math.BigDecimal")) {
				return ((BigDecimal) value1).compareTo((BigDecimal) value2) == 0;
			} else if (clasz.getName().equals("java.lang.Long")) {
				return ((Long) value1).longValue() == ((Long) value2)
						.longValue();
			} else if (clasz.getName().equals("java.util.Set")) {
				return true;
			} else {
				try {
					return compareFieldValue(clasz.getName(), value1, value2);
				} catch (Exception e) {
					return false;
				}
			}
		} else {
			return value1 == value2;
		}
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return 相等返回true
	 * @throws Exception
	 */
	private boolean compareFieldValue(String className, Object entity1,
			Object entity2) throws Exception {
		if (!entity1.getClass().getName().equals(entity2.getClass().getName())) {
			logger.warn(AuditTable.class.getName() + ":获取更新列时，两个对象类不同");
			return false;
		}
		Object obj = Class.forName(className).newInstance();
		IdentifierFieldAnnotation anno = obj.getClass().getAnnotation(
				IdentifierFieldAnnotation.class);
		// entity2.getClass().getSimpleName();
		String fieldName = "id";
		if (anno != null) {
			fieldName = anno.identifier();
		}
		Method method = Reflections.getGetterMethod(entity1.getClass(),
				fieldName);
		Object returnValue1 = method.invoke(entity1, null);
		Object returnValue2 = method.invoke(entity2, null);
		return String.valueOf(returnValue1)
				.equals(String.valueOf(returnValue2));
	}

	public String getLogContent(Object originality, Object newInstance,
			String operate, List list) {
		StringBuffer buffer = new StringBuffer(operate);
		buffer.append(" ");
		if (operate.equalsIgnoreCase("update")) {
			for (int i = 0; i < list.size(); i++) {
				String fieldName = list.get(i).toString();

				Method method = Reflections.getGetterMethod(originality
						.getClass(), fieldName);
				try {
					AuditAnnotation auditAnno = method
							.getAnnotation(AuditAnnotation.class);
					if (auditAnno == null
							|| (auditAnno != null && auditAnno.isRequired())) {
						Object old = method.invoke(originality, null);
						Object the_new = method.invoke(newInstance, null);

						Object oldNestedValue = this.getNestedBeanValue(method,
								old);
						Object newNestedValue = this.getNestedBeanValue(method,
								the_new);

						String oldValue = oldNestedValue == null ? String
								.valueOf(old) : String.valueOf(oldNestedValue);

						String newValue = newNestedValue == null ? String
								.valueOf(the_new) : String
								.valueOf(newNestedValue);

						buffer.append(fieldName);
						buffer.append(":");
						buffer.append(oldValue);
						buffer.append("=>");
						buffer.append(newValue);
						if (i != list.size() - 1) {
							buffer.append(",");
						}
						buffer.append("\r\n ");
					}
				} catch (Exception e) {
					Logger.getLogger(AuditTable.class).warn("得到日志内容时异常:",e);
				}
			}

			try {
				IdentifierFieldAnnotation anno = originality.getClass()
						.getAnnotation(IdentifierFieldAnnotation.class);
				String id = anno.identifier();
				Method method = Reflections.getGetterMethod(originality
						.getClass(), id);
				Object the_id = method.invoke(originality, null);
				buffer.append("WHERE ");
				buffer.append(id + "=");
				buffer.append(String.valueOf(the_id));
			} catch (Exception e) {
				Logger.getLogger(AuditTable.class).warn("得到日志内容时异常:",e);
			}
		} else {
			List<Method> methods = Reflections.getGetterMethods(newInstance
					.getClass());

			for (Method method : methods) {
				try {
					String fieldName = method.getName().substring(3);

					AuditAnnotation auditAnno = method
							.getAnnotation(AuditAnnotation.class);
					if (auditAnno == null
							|| (auditAnno != null && auditAnno.isRequired())) {
						Object obj = method.invoke(newInstance, null);
						Object nestedValue = getNestedBeanValue(method, obj);

						String value = nestedValue == null ? String
								.valueOf(obj) : String.valueOf(nestedValue);

						buffer.append(fieldName);
						buffer.append(":");
						buffer.append(value);
						buffer.append(",\r\n ");
					}
				} catch (Exception e) {
					Logger.getLogger(AuditTable.class).warn("得到日志内容时异常:",e);
				}
			}
		}
		return buffer.toString();
	}

	public Object getNestedBeanValue(Method method, Object nestedObj) {
		Object nestedValue = null;
		StringMessageAnnotation str = method
				.getAnnotation(StringMessageAnnotation.class);
		if (str != null) {
			String nestedMethodName = str.nestedBeanValueMethod();
			Method nestedMethod = Reflections.getMethod(nestedObj.getClass(),
					nestedMethodName);
			try {
				nestedValue = nestedMethod.invoke(nestedObj, null);
			} catch (Exception e) {
				return null;
			}
		}
		return nestedValue;
	}

	public static void main(String[] args) {
		String regex = "^\\w+@\\w+:[A-Za-z,]$";
		System.out
				.println("t_doc@com.nsc.dem.interceptor.impl.TDocInterceptorImpl:update,insert,delete"
						.matches(regex));
	}
}
