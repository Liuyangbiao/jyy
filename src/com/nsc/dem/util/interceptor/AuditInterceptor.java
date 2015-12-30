package com.nsc.dem.util.interceptor;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.nsc.base.hibernate.CurrentContext;
import com.nsc.base.util.AuditTable;
import com.nsc.dem.bean.archives.TDoc;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.bean.system.TOperateLog;
import com.nsc.dem.service.base.IService;
import com.nsc.dem.webservice.util.ApplicationContext;

public abstract class AuditInterceptor {
	protected TUser user;

	protected AuditTable audit;

	protected IService service = null;

	public AuditInterceptor() {
		user = CurrentContext.getCurrentUser();
		audit = AuditTable.getInstance();
		service = (IService) ApplicationContext.getInstance()
				.getApplictionContext().getBean("baseService");
	}

	public abstract void onPostLoad(Object obj);

	public abstract void onPostInsert(Object obj);

	public abstract void onPostDelete(Object obj);

	public abstract void onPostUpdate(Object obj);

	/**
	 * 保存日志信息到数据库
	 * 
	 * @param session
	 * @param originality
	 * @param newInstance
	 * @param operate
	 * @param columnList
	 */
	@SuppressWarnings("unchecked")
	protected void saveLog(Object originality, Object newInstance,
			String operate, List columnList, String logType) {
		String content = audit.getLogContent(originality, newInstance, operate,
				columnList);
		TOperateLog tLog = new TOperateLog();
		tLog.setTUser(user);
		tLog.setContent(content);
		tLog.setOperateTime(new Timestamp(System.currentTimeMillis()));
		tLog.setTarget(newInstance.getClass().getSimpleName());
		tLog.setType(logType);

		Session session = service.getSession().getSessionFactory()
				.openSession();
		try {
			session.beginTransaction();
			session.save(tLog);
			session.flush();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			Logger.getLogger(AuditInterceptor.class).warn("Hibernate异常:", e);
		} finally {
			session.close();
		}
	}

	/**
	 * 获取日志类型
	 * 
	 * @param columns
	 *            更新的列名
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("all")
	public String getDocumentLogType(List columns, Object originality,
			Object obj) {
		TDoc doc = (TDoc) obj;
		TDoc oDoc = (TDoc) originality;
		for (int i = 0; i < columns.size(); i++) {
			String colName = columns.get(i).toString();
			if (colName.equalsIgnoreCase("DOC_TYPE_CODE")) {
				return "L05";
			} else if (colName.equalsIgnoreCase("STATUS")
					&& doc.getStatus().equals("02")) {
				return "L03";
			} else if (colName.equalsIgnoreCase("STATUS")
					&& doc.getStatus().equals("01")) {
				// 02->01恢复 03->01 归档
				return oDoc.getStatus().equals("02") ? "L08" : "L09";
			} else {
				return "L04";
			}
		}
		return "L43";
	}
}
