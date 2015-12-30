package com.nsc.dem.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.nsc.base.conf.ConstConfig;
import com.nsc.base.hibernate.CurrentContext;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.util.log.LogManager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;

public class BaseAction extends ActionSupport{

	protected LogManager logger;
	
	
	public void setLogger(LogManager logger) {
		this.logger = logger;
	}

	public LogManager getLogManager() {
		return logger;
	}

	public void setService(IService baseService) {
		logger=baseService.getLogManager(this.getLoginUser());
		CurrentContext.putInUser(getLoginUser());
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5335734077097451352L;
	public static final String SUCCESS="success";
	
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Convenience method to get the request
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest() {
		ActionContext ct= ActionContext.getContext();
		return (HttpServletRequest)ct.get(ServletActionContext.HTTP_REQUEST);
		//return ServletActionContext.getRequest();
	}
	
	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	/**
	 * Convenience method to get the session. This will create a session if one
	 * doesn't exist.
	 * 
	 * @return the session from the request (request.getSession()).
	 */
	protected HttpSession getSession() {
		return getRequest().getSession(false);
	}
	

	protected TUser getLoginUser() {
		TUser user = (TUser) getRequest().getSession().getAttribute(   
        		ConstConfig.USER_KEY);
		Logger.getLogger(BaseAction.class).info("获取Session用户:"+getRequest().getSession().getId());
        return user;
    }   
   

    protected boolean isTimeout() {   
        if (getRequest().getSession().getAttribute(ConstConfig.USER_KEY) == null) {
            return true;   
        } else {   
            return false;   
        }   
    }   
   

    protected boolean isExistSession(String key) {   
        if (getRequest().getSession().getAttribute(key) != null) {   
            return true;   
        } else {   
            return false;   
        }   
    }   
   

    protected void setSession(String key, Object obj) {   
    	getRequest().getSession().setAttribute(key, obj);   
    }   
   

    protected Object getSession(String key) {   
        return getRequest().getSession().getAttribute(key);   
    }   
       

    protected void saveActionError(String key) {   
        super.addActionError(super.getText(key));   
    }   
       
   
    protected void saveActionMessage(String key) {   
        super.addActionMessage(super.getText(key));   
    }   
   
    protected String getRequestPath() {   
        return (String)ServletActionContext.getRequest().getServletPath();
    }
    
    protected String getRealPath(String folder){
    	return getRequest().getSession().getServletContext().getRealPath(folder);
    }
}
