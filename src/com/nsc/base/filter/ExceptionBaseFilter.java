package com.nsc.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public abstract class ExceptionBaseFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse httpResp=(HttpServletResponse)response;
		try {
			chain.doFilter(request, response);
		}catch (Exception e) {
			
			this.doException(e,request);
			
			httpResp.sendError(500);
		}catch (Throwable e) {
			
			this.doException(e,request);
			
			httpResp.sendError(500);
		}
	}

	/**
	 * 异常处理过滤器
	 * 
	 * @param e
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void doException(Exception e,ServletRequest request);
	
	/**
	 * 异常处理过滤器
	 * 
	 * @param e
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void doException(Throwable e,ServletRequest request);

}
