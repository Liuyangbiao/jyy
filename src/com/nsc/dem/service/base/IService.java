package com.nsc.dem.service.base;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.log.LogManager;

public interface IService {

	public void EntityAdd(Object object);

	public void EntityUpdate(Object object);

	public void EntityDel(Object object);

	public void insertEntity(Object object);
	
	public void updateEntity(Object object);
	
	public void delEntity(Object object);
	
	public List<Object> EntityQuery(Object object);

	public Session getSession();

	public LogManager getLogManager(TUser user);

	public Object EntityQuery(Class<?> clazz, Serializable id);
}
