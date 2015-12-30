package com.nsc.dem.util.listener;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEventListener;
import com.nsc.base.conf.Configurater;
import com.nsc.base.conf.ConstConfig;
import com.nsc.base.index.SearchFactory;
import com.nsc.base.listener.BaseListener;
import com.nsc.base.util.AuditTable;
import com.nsc.dem.bean.profile.TUser;
import com.nsc.dem.util.index.IndexSearchManager;
import com.nsc.dem.webservice.client.WSClient;
@SuppressWarnings("all")
public class RepeatLoginListener extends BaseListener {

	private ServletContext application = null;

	private IListenerSet set = null;

	/**
	 * 此方法是监听器初始化方法
	 */
	public void contextInitialized(ServletContextEvent sce) {
		application = sce.getServletContext();

		// 初始化配置工具
		Configurater config = Configurater.getInstance(application);
		AuditTable.getInstance();
		String system_type = config.getInstance().getConfigValue("system_type");
		String url = config.getInstance().getConfigValue("wsUrl");
		try {
			//区域不用加载索引库
			if(!"2".equals(system_type)){
				IndexSearchManager.getInstance();
			}else{
				Logger.getLogger(RepeatLoginListener.class).info("区域不用加载索引库");
			}
			if(StringUtils.isNotBlank(url)){
				WSClient.getClient(url);
			}else{
				Logger.getLogger(RepeatLoginListener.class).warn("请您配置国网的webService地址");
			}
			
			String className = config.getConfigValue("listener_class");
			Class<?> c = Class.forName(className);
			set = (IListenerSet)c.newInstance();
		} catch (ClassNotFoundException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("类没有发现异常:",e);
		} catch (InstantiationException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("初始化异常:",e);
		} catch (IllegalAccessException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("非法表述异常:",e);
		} catch (URISyntaxException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("获取目录出错:",e);
		} catch (IOException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("数据同步出错:",e);
		}
		
	}
	
	/**
	 * 监听环境销毁
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
				IndexSearchManager.getInstance().releaseAllSearch();
		} catch (URISyntaxException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("释放检索库失败:",e);
		} catch (IOException e) {
			Logger.getLogger(RepeatLoginListener.class).warn("释放检索库失败:",e);
		}
	}

	/**
	 * 当添加session属性时触发此方法
	 * @throws IOException 
	 */
	public void attributeAdded(HttpSessionBindingEvent even) {
		try {
			set.addAttribute(even, application);
		} catch (IOException e) {
			Logger.getLogger(RepeatLoginListener.class).warn(e.getMessage());
		}
	}
	
	public void attributeReplaced(HttpSessionBindingEvent even) {
		try {
			set.changeAttribute(even, application);
		} catch (IOException e) {
			Logger.getLogger(RepeatLoginListener.class).warn(e.getMessage());
		}
	}

	/**
	 * 当session里的属性被移除时触发此方法
	 */
	public void attributeRemoved(HttpSessionBindingEvent sbe) {
		if (application != null) {
			TUser tUser = null;
			Object obj = sbe.getValue();
			if (obj instanceof TUser) {
				tUser = (TUser) obj;
			}

			if (tUser != null) {
				application.removeAttribute(tUser.getLoginId());
			}
		}
	}

	/**
	 * 当session被销毁时触发此方法
	 */
	public void sessionDestroyed(HttpSessionEvent sbe) {

		TUser tUser = null;
		Object obj = sbe.getSession().getAttribute(ConstConfig.USER_KEY);
		if (obj instanceof TUser) {
			tUser = (TUser) obj;
		}
		if (tUser != null) {
			application.removeAttribute(tUser.getLoginId());
		}
	}
	
	private void addLoadListeners(PostLoadEventListener[] listeners){
		Configuration cfg = new Configuration();
		cfg.getEventListeners().setPostLoadEventListeners(listeners);
	}
	
	private void addInsertListeners(PostInsertEventListener[] listeners){
		Configuration cfg = new Configuration();
		cfg.getEventListeners().setPostInsertEventListeners(listeners);
	}
	
	private void addDeleteListeners(PostDeleteEventListener[] listeners){
		Configuration cfg = new Configuration();
		cfg.getEventListeners().setPostDeleteEventListeners(listeners);
	}
	private void addUpdateListeners(PostUpdateEventListener[] listeners){
		Configuration cfg = new Configuration();
		cfg.getEventListeners().setPostUpdateEventListeners(listeners);
	}
}
