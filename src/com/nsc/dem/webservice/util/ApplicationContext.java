package com.nsc.dem.webservice.util;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContext { 
	
	Logger logger = Logger.getLogger(ApplicationContext.class);

    private static ApplicationContext instance = null;

    private AbstractApplicationContext appContext;

    public synchronized static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private ApplicationContext() {
        try {
            this.appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        } catch (Exception e) {
        	logger.error("ApplicationContext ≥ı ºªØ", e);
        }
    }

    public AbstractApplicationContext getApplictionContext() {
        return appContext;
    }

}
