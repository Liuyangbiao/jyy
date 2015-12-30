package com.nsc.dem.util.interceptor.impl;

import java.util.List;

import com.nsc.base.hibernate.CurrentContext;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.interceptor.AuditInterceptor;

public class TDocInterceptorImpl extends AuditInterceptor {

	public TDocInterceptorImpl() {
		super();
	}

	@Override
	public void onPostLoad(Object obj) {
		TUser user = super.user;
		if (user != null) {
			saveLog(null, obj, "SELECT", null, "L44");
		}
	}

	@Override
	public void onPostDelete(Object obj) {
		TUser user = super.user;
		if (user != null) {
			saveLog(null, obj, "DELETE", null, "L07");
		}
	}

	@Override
	public void onPostInsert(Object obj) {
		TUser user = super.user;
		if (user != null) {
			saveLog(null, obj, "INSERT", null, "L06");
		}
	}

	@Override
	public void onPostUpdate(Object obj) {
		TUser user = super.user;
		Object originality = CurrentContext.getOriginality();
		if (originality != null) {
			@SuppressWarnings("unchecked")
			List<String> list = super.audit.getUpdateColumns(originality, obj);

			String logType = super.getDocumentLogType(list, originality, obj);
			if (user != null) {
				saveLog(originality, obj, "UPDATE", list, logType);
			}
		}
	}

}
