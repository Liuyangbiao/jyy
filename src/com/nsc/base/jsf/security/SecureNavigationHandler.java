package com.nsc.base.jsf.security;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * 导航控制类
 * 
 * 当发生页面转换时，触发此类中的控制方法。此类作为页面访问控制器使用，进而进行权限控制。
 * 为使用该类功能，需要在配置文件中定义此类作为Action监听器。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:21:25 AM
 * @version
 */
public class SecureNavigationHandler extends NavigationHandler{
	private NavigationHandler handler;
	
	/**
	 * 构造函数
	 * @param handler
	 */
	public SecureNavigationHandler(NavigationHandler handler) {
		super();
		this.handler = handler;
	}
	
	/**
	 * 处理页面跳转
	 * 
	 * 如果该跳转页面不允许访问，则跳转到其他页面
	 */
	public void handleNavigation(FacesContext fc, String actionMethod, String actionView) {
		
		if(cancelActionByAuthen(getViewId(fc),actionMethod)){
			
		}else{
			handler.handleNavigation(fc, actionMethod, actionView);
		}
	}
	
	/**
	 * 检查是否无授权
	 * @param viewId
	 * @param action
	 * @return 是否无授权
	 */
	private boolean cancelActionByAuthen(String viewId,String action){
		return Authentication.getInstance().authenticate(viewId, action);
	}
	
	/**
	 * 取得页面ID
	 * @param facesContext
	 * @return 页面ID
	 */
	private String getViewId(FacesContext facesContext){
		if (facesContext!=null){
			UIViewRoot viewRoot = facesContext.getViewRoot();
			if (viewRoot!=null)
				return viewRoot.getViewId();
			}
		return null;
	}
}
