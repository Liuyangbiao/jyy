package com.nsc.dem.util.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.nsc.base.conf.Configurater;
import com.nsc.base.conf.ConstConfig;
import com.nsc.base.filter.AuthenticationBaseFilter;
import com.nsc.dem.bean.profile.TUser;

public class AuthenticationEDMFilter extends AuthenticationBaseFilter {

	@Override
	public boolean authenticate(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		HttpSession session = req.getSession();
		StringBuffer reqUrl = req.getRequestURL();
		String principal = req.getParameter("edm_principal");
		String principal2 = req.getParameter("portal_principal");
		// 如果是单点登录
		if (principal != null) {
			req.setAttribute("res", res);
			session.setAttribute("edm_principal", req);
			//依然没有该用户信息，单点登录失败
			
			if(session.getAttribute(ConstConfig.USER_KEY) == null){
				return false;
			}
			res.setHeader("P3P","CP=CAO PSA OUR");
			return true;
		//如果是江西博微单点登录
		} else if(principal2!=null){
			//依然没有该用户信息，单点登录失败
			if(session.getAttribute(ConstConfig.USER_KEY) == null){
				return false;
			}
			res.setHeader("P3P","CP=CAO PSA OUR");
			return true;
		} 
		
		//用户已经登录
		if(session.getAttribute(ConstConfig.USER_KEY) != null){
			TUser user = (TUser) session.getAttribute(ConstConfig.USER_KEY);
			Logger.getLogger(AuthenticationEDMFilter.class).info(
					"当前用户：" + user.getLoginId());
			return true;
		}else{//如果用户访问的地址在允许放行的地址 允许访问
			for(String url : super.allowPassUrl){
				if(reqUrl.indexOf(url) != -1){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 初始化，设置验证失败路径
	 */
	public void init(FilterConfig config) throws ServletException {
		super.loginUrl = config.getInitParameter("login"); 
		super.allowPassUrl.add(config.getInitParameter("loginAction"));
		super.allowPassUrl.add(config.getInitParameter("securityCode"));
		super.allowPassUrl.add(config.getInitParameter("checkSecurityCode"));
		super.allowPassUrl.add(config.getInitParameter("httpDownLoad"));
		super.allowPassUrl.add(config.getInitParameter("sendRedirectDownLoad"));
		super.allowPassUrl.add(config.getInitParameter("getStations"));
		super.allowPassUrl.add(loginUrl);
	}

	@Override
	public void doChain(FilterChain chain, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {

		String principal = req.getParameter("edm_principal");
		// 如果是单点登录
		if (principal != null) {
			TUser user=(TUser) req.getSession().getAttribute(ConstConfig.USER_KEY);
			userInfoSite(user,req, res);
			
			if(!res.isCommitted())
			chain.doFilter(req, res);
		} else {
			chain.doFilter(req, res);
		}
	}

	private void userInfoSite(TUser user, HttpServletRequest req,HttpServletResponse res)
			throws IOException, ServletException {
		// 请求页面
		String page = (String) req.getParameter("reqPage");
		Configurater con = Configurater.getInstance();
		
		//从哪里来
		String referer = (String) req.getAttribute("url");
		// 如果用户不为空时，将用户存在session中，重写向到新页面
		if (user != null) {
			
			// 如果传来的page标识出是哪个页面，则重定向到当前页面
			if (page != null) {
				String pageUrl = req.getContextPath() + "/" +con.getConfigValue(page);
				res.sendRedirect(pageUrl);
			// 如果传来的page未标识出哪个页面，则重定向到主页
			} else if(req.getServletPath().endsWith("sso.jsp")) {
				String pageUrl = req.getContextPath()+"/" +con.getConfigValue("Romate_default");
				res.sendRedirect(pageUrl);
			}
			// 如果当前用户为空，则返回传来的页面
		} else {
			res.sendRedirect(referer);
		}
	}
	
	public void destroy() {

	}
}
