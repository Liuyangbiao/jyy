package com.nsc.base.hibernate;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

import com.nsc.base.util.AuditTable;

public class PreOperateIntercepter implements PreUpdateEventListener {

	private static final long serialVersionUID = 1L;

	public boolean onPreUpdate(PreUpdateEvent event) {
		if (AuditTable.getInstance() != null) {
			SessionImplementor sfi = event.getSource();
			Object obj = event.getEntity();
			if (AuditTable.getInstance().isNeedAudit(obj, "UPDATE")) {
				Session session = sfi.getFactory().getCurrentSession();
				Serializable id = session.getIdentifier(obj);
				Session newSession = sfi.getFactory().openTemporarySession();
				Object originality =newSession.get(obj.getClass(), id);
				newSession.close();
				
				CurrentContext.putInOriginality(originality);
			}
		}
		return false;
	}

}
