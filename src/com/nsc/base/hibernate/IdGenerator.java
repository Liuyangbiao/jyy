package com.nsc.base.hibernate;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;

@SuppressWarnings("all")  
public class IdGenerator implements IdentifierGenerator, Configurable{
	
	private String sql;
	
	public Serializable generate(SessionImplementor session, Object obj)
			throws HibernateException {
		return GeneratorFactory.getGeneratorCode(session, obj);
	}

	public void configure(Type type, Properties params, Dialect d)
			throws MappingException {
		String table = params.getProperty("table");
		if(table==null){
			table = params.getProperty(PersistentIdentifierGenerator.TABLE);
		}
		
		String column = params.getProperty("column");
		if(column==null){
			column = params.getProperty(PersistentIdentifierGenerator.PK);
		}
		
		String schema = params.getProperty(PersistentIdentifierGenerator.SCHEMA);
		String catalog = params.getProperty(PersistentIdentifierGenerator.CATALOG);
		Class returnClass = type.getReturnedClass();
		
		sql = "SELECT COUNT(*)+1 FROM "+(schema==null?table:schema+"."+table);
	}
	
	
}
