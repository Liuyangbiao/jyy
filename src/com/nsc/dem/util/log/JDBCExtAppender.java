package com.nsc.dem.util.log;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.MDC;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.Session;

import com.nsc.base.conf.ConstConfig;

public class JDBCExtAppender extends JDBCAppender{
	
	
	public JDBCExtAppender(){
		super();
	}
	
    protected void closeConnection(Connection con){
 
    }
    
    public void close(){
        flushBuffer();
        this.closed = true;
    }
    
    protected Connection getConnection() throws SQLException{
    	try {
	    	    		
//    		SessionFactory factory = (SessionFactory)MDC.get("sessionFactory");
//    		return factory.getCurrentSession().connection();
    		
    		Session session = (Session)MDC.get(ConstConfig.SESSION_KEY);
 		
	        return session.connection();
			
		} catch (Throwable ex) {
			//创建SessionFactory失败信息
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
    }
    
    public String getLogStatement(LoggingEvent event){
        StringBuffer sbuf=new StringBuffer();
        sbuf.append(layout.format(event)); 
        if (layout.ignoresThrowable()){
            sbuf.delete(sbuf.length()-2,sbuf.length() );
            String[] s = event.getThrowableStrRep();
            if (s != null){
                for (int j = 0; j < s.length; j++){
                    sbuf.append("\r\n ");
                    sbuf.append(s[j]);
                }
            }
            sbuf.append("')");
        }
        
        return sbuf.toString() ;
    }
}