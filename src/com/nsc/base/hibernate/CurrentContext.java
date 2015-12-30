package com.nsc.base.hibernate;

import com.nsc.dem.bean.profile.TUser;

public class CurrentContext {
	private static ThreadLocal<TUser> threadUser = new ThreadLocal<TUser>();
	
	private static ThreadLocal<Object> thread = new ThreadLocal<Object>();
	
	public static void putInUser(TUser user){
		if(user!=null){
			threadUser.set(user);
		}
	}
	public static TUser getCurrentUser(){
		TUser user = threadUser.get();
		return user;
	}
	
	public static void putInOriginality(Object originality){
		if(originality!=null){
			thread.set(originality);
		}
	}
	public static Object getOriginality(){
		Object originality = thread.get();
		return originality;
	}
}
