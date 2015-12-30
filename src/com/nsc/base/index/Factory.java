package com.nsc.base.index;

import com.nsc.base.conf.Configurater;
import com.nsc.base.util.Component;
import com.nsc.base.util.Reflections;

public abstract class Factory<T> {
	
	@SuppressWarnings("unchecked")
	protected T getImplement(String config) throws ClassNotFoundException, 
															 InstantiationException, 
															 IllegalAccessException{
		
		String classPath=Configurater.getInstance().getConfigValue(config);
		
		if(null==classPath) return null;
		
		if(classPath.startsWith("%")){
			classPath=classPath.substring(1);
			
			if(classPath.endsWith("%")){
				classPath=classPath.substring(0, classPath.length()-1);
			}
			
			return (T)Component.getInstance(classPath,
											Configurater.getInstance().getServletContext());
		}else{
			Class<?> clazz=Reflections.classForName(classPath);
			
			return (T)clazz.newInstance();
		}
	}
}
