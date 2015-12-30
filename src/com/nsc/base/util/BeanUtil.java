package com.nsc.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Bean操作类
 * 
 * 此类用于JavaBean的一般操作。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:29:44 AM
 * @version
 */
public class BeanUtil {
	
	/**
	 * 取得对象实例指定属性的值
	 * @param fieldName 属性名称
	 * @param instance  对象实例
	 * @return 该对象属性的值
	 */
	public static Object getFieldValue(String fieldName,Object instance){
		boolean accessAble=true;
		
		Field field=Reflections.getField(instance.getClass(), fieldName);
		
		if(!field.isAccessible()){
			accessAble=false;
			field.setAccessible(true);
		}
		
		try{
			Object value=Reflections.get(field, instance);
			return value;
		}catch(Exception e){
			String sid="sys.bean.field.getvalue";
			throw new AppException(e,sid,null,new String[]{instance.getClass().getName(),fieldName});
		}finally{
			field.setAccessible(accessAble);
		}
	}
	/**
	 * 查看entity对象的属性值是否发生变化，若发生变化则修改其值并且返回TRUE
	 * @param entity
	 * @param params
	 * @return 是否变化
	 */
	public static boolean isChanged(Object entity,Map<String,Object> params){
		boolean isChanged=false;
		for(String key:params.keySet()){
			Object value=params.get(key);
			Object oldValue=getFieldValue(key,entity);
			
			if(oldValue==null && value==null) continue;
			if(value instanceof String && value.equals("") && oldValue==null) continue;
			try{
				if(oldValue!=null && !oldValue.equals(value)){
					isChanged=true;
					Method method=Reflections.getSetterMethod(entity.getClass(), key);
					Reflections.invoke(method, entity, new Object[]{value});
				}else if(value!=null && !value.equals(oldValue)){
					isChanged=true;
					Method method=Reflections.getSetterMethod(entity.getClass(), key);
					Reflections.invoke(method, entity, new Object[]{value});
				}
			}catch(Exception ex){
				String sid="sys.bean.field.ischanged";
				throw new AppException(ex,sid,null,new String[]{entity.getClass().getName(),key});
			}
		}
		return isChanged;
	}
}
