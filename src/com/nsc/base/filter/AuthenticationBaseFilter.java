package com.nsc.base.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationBaseFilter implements Filter {

	protected String loginUrl = null;
	protected List<String> allowPassUrl = new ArrayList<String>();

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String realLoginUrl=req.getContextPath() + loginUrl;
		
		if(req.getServletPath().equals(loginUrl)){
			doChain(chain,req, res);
		// 进行权限检查
		}else if (authenticate(req, res)) {
			doChain(chain,req, res);
		} else {
			res.sendRedirect(realLoginUrl);
		}
	}

	public abstract void doChain(FilterChain chain, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException;

	public abstract boolean authenticate(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException;
}
