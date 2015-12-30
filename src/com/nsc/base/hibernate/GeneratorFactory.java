package com.nsc.base.hibernate;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.impl.SessionImpl;

public abstract class GeneratorFactory {
	
	private static GeneratorFactory generator = null;
	
	
	/**
	 * 生成编码
	 * @param session (hibernate)
	 * @param obj  (hibernate实体)
	 * @return
	 */
	public static String getGeneratorCode(Serializable session, Object obj) {
		String entityFullName = obj.getClass().getName();
		String entityName = obj.getClass().getSimpleName();
		String generatorClass = entityFullName.substring(0, entityFullName
				.lastIndexOf(".") + 1)
				+ entityName + "Generator";
		Object code = null;
		try {
			generator = (GeneratorFactory)Class.forName(generatorClass).newInstance();
//			Method method = Reflections.getMethod(generator.getClass(),
//					"buildGeneratorCode");
//			code = Reflections
//							.invoke(method, generator, new Object[] { session, obj });
			
			code = generator.buildGeneratorCode((SessionImpl)session, obj);
		} catch (InstantiationException e) {
			Logger.getLogger(GeneratorFactory.class).warn("异常",e);
		} catch (IllegalAccessException e) {
			Logger.getLogger(GeneratorFactory.class).warn("非法访问",e);
		} catch (ClassNotFoundException e) {
			Logger.getLogger(GeneratorFactory.class).warn("类没有发现",e);
		} catch (Exception e) {
			Logger.getLogger(GeneratorFactory.class).warn("异常",e);
		} finally{
			generator = null;
		}	
		return code.toString();
	}
	
	/**
	 * 生成编码
	 * @param session (hibernate)
	 * @param obj  (hibernate实体)
	 * @return
	 * @throws Exception 
	 */
	protected abstract Object buildGeneratorCode(SessionImpl session, Object obj) throws Exception;
	
}
