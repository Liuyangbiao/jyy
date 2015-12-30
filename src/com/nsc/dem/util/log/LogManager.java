package com.nsc.dem.util.log;

import org.hibernate.Session;

public class LogManager {
	String operator;
	Session session;

	public LogManager(String operator, Session session) {
		this.operator = operator;
		this.session = session;
	}

	public Logger getLogger(Class<?> clazz) {

		return new Logger(operator, session, clazz);
	}
}
