package com.nsc.dem.util.log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.Reflections;
import com.nsc.dem.bean.archives.TDoc;
@SuppressWarnings("all") 
public class AuditTable {
	private static Configurater config = Configurater.getInstance();
	
	private static String[] auditables;
	
	private static String[] auditOperates;
	
	static{
		try {
			auditables = config.getConfigValue("audit_table").split(",");
		} catch (Exception e) {
			auditables = new String[]{"T_DOC"};
		}
		try {
			auditOperates = config.getConfigValue("audit_operate").split(",");
		} catch (Exception e) {
			auditOperates = new String[]{"INSERT","UPDATE","DELETE"};
		}
	}
	/**
	 *  判断是否是要审计的表
	 * @param obj 实体
	 * @return 
	 */
	public static boolean isAuditables(Object obj){
		String name = obj.getClass().getSimpleName();
		for(int i=0;i<auditables.length;i++){
			if(auditables[i].replaceAll("_", "").equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	/**
	 *  判断是否是要审计的操作
	 * @param operate
	 * @return
	 */
	public static boolean isAuditOperates(String operate){
		for(int i=0;i<auditOperates.length;i++){
			if(auditOperates[i].equalsIgnoreCase(operate)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否需要审计
	 */
	public static boolean isNeedAudit(Object obj,String operate){
		return isAuditables(obj)&&isAuditOperates(operate);
	}
	
	public static List<String> getUpdateColumns(Object originality,Object newInstance) {
		List<String> list = null;
		if(!originality.getClass().getName().equals(newInstance.getClass().getName())){
			LogManager log = new LogManager("AUDIT",null);
			log.getLogger(AuditTable.class).warn(AuditTable.class.getName()+":获取更新列时，两个对象类不同");
		}else{
			list = new ArrayList<String>();
			List<Method> methods = Reflections.getGetterMethods(originality.getClass());
			for(Method method:methods){
				try {
					String fieldName = method.getName().substring(3); 
					Object result1 = method.invoke(originality, null);
					Object result2 = method.invoke(newInstance, null);
					boolean isEqual = compareFieldValue(originality.getClass(),fieldName,result1, result2);
					if(!isEqual){
						list.add(fieldName);
					}
				} catch (IllegalArgumentException e) {
					Logger.getLogger(AuditTable.class).warn("获取更新列时参数类型不匹配异常:",e);
				} catch (IllegalAccessException e) {
					Logger.getLogger(AuditTable.class).warn("获取更新列时非法访问异常:",e);
				} catch (InvocationTargetException e) {
					Logger.getLogger(AuditTable.class).warn("获取更新列时异常:",e);
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
	@SuppressWarnings("unchecked")
	private static boolean compareFieldValue(Class c,String fieldName,Object value1,Object value2){
		if(value1!=null&&value2!=null){
			Field field = Reflections.getField(c, fieldName);
			
			Class clasz = field.getType();
			if(clasz.getName().equals("java.lang.String")){
				return value1.equals(value2);
			}else if(clasz.getName().equals("java.util.Date")){
				return ((Date)value1).getTime()==((Date)value2).getTime();
			}else if(clasz.getName().equals("java.sql.Timestamp")){
				return ((Timestamp)value1).getTime()==((Timestamp)value2).getTime();
			}else if(clasz.getName().equals("java.math.BigDecimal")){
				return ((BigDecimal)value1).compareTo((BigDecimal)value2)==0;
			}else if(clasz.getName().equals("java.lang.Long")){
				return ((Long)value1).longValue()==((Long)value2).longValue();
			}else{
				try {
					return compareFieldValue(value1,value2);
				} catch (Exception e) {
					return false;
				}
			}
		}else{
			return value1==value2;
		}
	}
	
	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return 相等返回true
	 * @throws Exception 
	 */
	private static boolean compareFieldValue(Object entity1,Object entity2) throws Exception{
		if(!entity1.getClass().getName().equals(entity2.getClass().getName())){
			LogManager log = new LogManager("AUDIT",null);
			log.getLogger(AuditTable.class).warn(AuditTable.class.getName()+":获取更新列时，两个对象类不同");
			return false;
		}
		List<Field> list = Reflections.getFields(entity1.getClass());
		for(Field field:list){
			String fieldName = field.getName();
			Method method = Reflections.getGetterMethod(entity1.getClass(), fieldName);
			Object returnValue1 = method.invoke(entity1, null);
			Object returnValue2 = method.invoke(entity2, null);
			boolean isEqual = compareFieldValue(entity1.getClass(),field.getName(),returnValue1,returnValue2);
			if(!isEqual){
				return false;
			}
		}
		return true;
	}
	/**
	 * 获取日志类型
	 * @param columns 更新的列名
	 * @param obj 
	 * @return 
	 */
	public static String getDocumentLogType(List<String> columns,Object obj){
		TDoc doc = (TDoc)obj;
		for(int i=0;i<columns.size();i++){
			String colName = columns.get(i).toString();
			if(colName.equalsIgnoreCase("DOC_TYPE_CODE")){
				return "L05";
			}else if(colName.equalsIgnoreCase("STATUS")&&doc.getStatus().equals("02")){
				return "L03";
			}else if(colName.equalsIgnoreCase("STATUS")&&doc.getStatus().equals("01")){
				return "L08";
			}else{
				return "L04";
			}
		}
		return "L43";
	}
	
	public static String getLogContent(Object originality,Object newInstance,String operate,List<String> list){
		StringBuffer buffer = new StringBuffer(operate);
		buffer.append(" ");
		if(operate.equalsIgnoreCase("update")){
			for(int i=0;i<list.size();i++){
				String fieldName = list.get(i).toString();
				
				Method method = Reflections.getGetterMethod(originality.getClass(), fieldName);
				try {
					Object old = method.invoke(originality, null);
					Object the_new = method.invoke(newInstance, null);
					String oldValue = old==null?"null":String.valueOf(old);
					String newValue = the_new==null?"null":String.valueOf(the_new);
					
					buffer.append(fieldName);
					buffer.append(":");
					buffer.append(oldValue);
					buffer.append("=>");
					buffer.append(newValue);
					buffer.append(",\r\n ");
				} catch (Exception e) {
					Logger.getLogger(AuditTable.class).warn("获取表更新记录发生异常:",e);
				}
			}
		}else{
			List<Method> methods = Reflections.getGetterMethods(newInstance.getClass());
			for(Method method:methods){
				try {
					String fieldName = method.getName().substring(3);
					Object obj = method.invoke(newInstance, null);
					String value = obj==null?"null":String.valueOf(obj);
					
					buffer.append(fieldName);
					buffer.append(":");
					buffer.append(value);
					buffer.append(",\r\n ");
				} catch (Exception e) {
					Logger.getLogger(AuditTable.class).warn("获取表更新记录发生异常:",e);
				} 
			}
		}
		return buffer.toString();
	}
}
