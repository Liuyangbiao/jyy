package com.nsc.dem.util.log;

import org.apache.log4j.MDC;
import org.hibernate.Session;

import com.nsc.base.conf.ConstConfig;

public class Logger {

	org.apache.log4j.Logger logger;
	String operator;
	Session session;

	public Logger(String operator, Session session,Class<?> clazz) {
		this.operator = operator;
		this.session = session;
		this.logger = org.apache.log4j.Logger.getLogger(clazz);
	}

	public void debug(Object msg) {
		this.logger.info(msg);
	}

	public void debug(Object message, Throwable t) {
		this.logger.debug(message, t);
	}

	public void info(Object msg) {
		this.logger.info(msg);
	}

	public void info(Object message, Throwable t) {
		this.logger.info(message, t);
	}

	public void warn(Object msg) {
		this.logger.warn(msg);
	}

	public void warn(Object message, Throwable t) {
		this.logger.warn(message, t);
	}

	public void fatal(Object msg) {

		this.logger.fatal(msg);
	}

	public void fatal(Object message, Throwable t) {
		this.logger.fatal(message, t);
	}

	public void trace(Object message, Throwable t) {
		this.logger.trace(message, t);
	}

	public void error(String target, String type, Object msg) {

		MDC.put(ConstConfig.TARGET_KEY, target);
		MDC.put(ConstConfig.TYPE_KEY, type);
		MDC.put(ConstConfig.TARGET_KEY, operator);
		MDC.put(ConstConfig.SESSION_KEY, session);

		this.logger.error(msg);
	}
	
	public void error(String target, String type, Object msg, Throwable t) {

		MDC.put(ConstConfig.TARGET_KEY, target);
		MDC.put(ConstConfig.TYPE_KEY, type);
		MDC.put(ConstConfig.TARGET_KEY, operator);
		MDC.put(ConstConfig.SESSION_KEY, session);

		this.logger.error(msg,t);
	}
}
