package com.nsc.base.jsf.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppContextProvider implements ApplicationContextAware {
	
	private static ApplicationContext cxt;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		
		cxt=arg0;
	}

	public static ApplicationContext getCxt() {
		return cxt;
	}
}
