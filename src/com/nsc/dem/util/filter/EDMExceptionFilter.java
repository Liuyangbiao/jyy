package com.nsc.dem.util.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nsc.base.filter.ExceptionBaseFilter;

public class EDMExceptionFilter extends ExceptionBaseFilter{

	@Override
	public void doException(Exception e,ServletRequest request) {

		Log logger = LogFactory.getLog(getClass());
		//组织日志信息
		logger.error(e.getMessage(), e.getCause());
		
		//组织界面信息
		request.setAttribute("userMsg", "系统发生了不可预知的错误，该错误已经被记录，请等待或通知系统管理员解决。");
		request.setAttribute("errMsg", e.getMessage());
	}

	public void destroy() {
		//do nothing
	}

	public void init(FilterConfig arg0) throws ServletException {
		//do nothing
	}

	@Override
	public void doException(Throwable e, ServletRequest request) {
		Log logger = LogFactory.getLog(getClass());
		
		//组织日志信息
		logger.error(e.getMessage(), e.getCause());
		
		//组织界面信息
		request.setAttribute("userMsg", "系统发生了不可预知的错误，该错误已经被记录，请等待或通知系统管理员解决。");
		request.setAttribute("errMsg", e.getMessage());
		
	}
}
