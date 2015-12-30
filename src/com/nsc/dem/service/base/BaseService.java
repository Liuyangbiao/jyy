package com.nsc.dem.service.base;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.hibernate.Session;

import com.nsc.base.conf.Configurater;
import com.nsc.base.hibernate.GeneralDAO;
import com.nsc.base.util.Component;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.log.LogManager;

public class BaseService<T> implements IService{

	protected GeneralDAO  generalDAO;
	protected LogManager logger;
	protected TUser currentUser;
	
	public void setGeneralDAO(GeneralDAO dao) {
		this.generalDAO = dao;
	}
	
	public Session getSession(){
		return this.generalDAO.getSessionFactory().getCurrentSession();
	}
	
	protected static Object getServiceObject(String alias){
		ServletContext cxt = Configurater.getInstance().getServletContext();
		Object object = Component.getInstance(alias, cxt);
		
		return object;
	}
	
	public LogManager getLogManager(TUser user){
		if(user==null) return null;
		this.currentUser=user;
		this.logger = new LogManager(user.getLoginId(),getSession());
		logger.getLogger(BaseService.class).info(this.hashCode());
		return this.logger;
	}
	
	public void EntityAdd(Object object){
		generalDAO.add(object);
	}
	
	public void EntityUpdate(Object object){		
		generalDAO.modify(object);
	}	
	
	public void EntityDel(Object object){
		generalDAO.delete(object);
	}	
	
	public void insertEntity(Object object){
		generalDAO.add(object);
		
	}
	
	public void updateEntity(Object object){		
		generalDAO.modify(object);
	}	
	
	public void delEntity(Object object){
		generalDAO.delete(object);
	}
	@SuppressWarnings("unchecked")
	public List<Object> EntityQuery(Object object){
		return generalDAO.getResult(object);
	}
	
	public Object EntityQuery(Class<?> clazz,Serializable id){
		return generalDAO.findByID(clazz, id);
	}
	
	
}
