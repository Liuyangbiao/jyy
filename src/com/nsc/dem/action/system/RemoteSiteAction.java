package com.nsc.dem.action.system;

import java.io.IOException;

import com.nsc.dem.action.BaseAction;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.xml.IntenterXmlUtils;

public class RemoteSiteAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String returValue;

	public String getReturValue() {
		return returValue;
	}

	private String remoteSite;

	public void setRemoteSite(String remoteSite) {
		this.remoteSite = remoteSite;
	}

	public void setRemotePage(String remotePage) {
		this.remotePage = remotePage;
	}

	private String remotePage;

	public String remoteSiteAction() throws IOException {
		TUser user = super.getLoginUser();
		
		super.getSession().getServletContext().setAttribute(user.getLoginId(),
				super.getSession());
		
		//取得远端服务器http地址
		
		String appIp = IntenterXmlUtils.getAppServerAdd(remoteSite);
		String urlT = appIp + "/main/sso.jsp?edm_principal=" + user.getLoginId() + "&reqPage=" + remotePage;
		super.getResponse().sendRedirect(urlT);
		returValue = "true";
		return null;
	}

}
