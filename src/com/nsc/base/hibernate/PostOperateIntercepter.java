package com.nsc.base.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;

import com.nsc.base.util.AuditTable;
import com.nsc.base.util.Reflections;
@SuppressWarnings("all")  
public class PostOperateIntercepter implements PostLoadEventListener,
		PostInsertEventListener, PostDeleteEventListener,
		PostUpdateEventListener {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8135651965599247828L;

	/**
	 * 审计查询操作
	 */
	public void onPostLoad(PostLoadEvent event) {
		Object obj = event.getEntity();
		if(preAudit(obj, "SELECT")){
			String className = AuditTable.getInstance().getAuditClass(obj);
			try {
				Class clasz = Class.forName(className);
				Object audit = clasz.newInstance();
				Method method = Reflections.getMethod(clasz, "onPostLoad", new Class[]{Object.class});
				method.invoke(audit, new Object[]{obj});
			} catch (ClassNotFoundException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("类没有发现",e);
			} catch (InstantiationException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (IllegalAccessException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("非法访问",e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			}
		}
	}
	/**
	 * 审计插入操作
	 */
	public void onPostInsert(PostInsertEvent event) {
		Object obj = event.getEntity();
		if(preAudit(obj, "INSERT")){
			String className = AuditTable.getInstance().getAuditClass(obj);
			try {
				Class clasz = Class.forName(className);
				Object audit = clasz.newInstance();
				Method method = Reflections.getMethod(clasz, "onPostInsert", new Class[]{Object.class});
				method.invoke(audit, new Object[]{obj});
			} catch (ClassNotFoundException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("类没有发现",e);
			} catch (InstantiationException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (IllegalAccessException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("非法访问",e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			}
		}
	}
	/**
	 * 审计删除操作
	 */
	public void onPostDelete(PostDeleteEvent event) {
		Object obj = event.getEntity();
		if(preAudit(obj, "DELETE")){
			String className = AuditTable.getInstance().getAuditClass(obj);
			try {
				Class clasz = Class.forName(className);
				Object audit = clasz.newInstance();
				Method method = Reflections.getMethod(clasz, "onPostDelete", new Class[]{Object.class});
				method.invoke(audit, new Object[]{obj});
			} catch (ClassNotFoundException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("类没有发现",e);
			} catch (InstantiationException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (IllegalAccessException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("非法访问",e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			}	
		}
	}
	/**
	 * 审计更新操作
	 */
	public void onPostUpdate(PostUpdateEvent event) {
		Object obj = event.getEntity();
		if(preAudit(obj, "UPDATE")){
			String className = AuditTable.getInstance().getAuditClass(obj);
			try {
				Class clasz = Class.forName(className);
				Object audit = clasz.newInstance();
				Method method = Reflections.getMethod(clasz, "onPostUpdate", new Class[]{Object.class});
				method.invoke(audit, new Object[]{obj});
			} catch (ClassNotFoundException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("类没有发现",e);
			} catch (InstantiationException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (IllegalAccessException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("非法访问",e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(PostOperateIntercepter.class).warn("",e);
			}	
		}
	}
	
	private boolean preAudit(Object obj,String operate){
		if(AuditTable.getInstance()==null){
			return false;
		}else{
			return AuditTable.getInstance().isNeedAudit(obj,operate);
		}
	}
	
}
