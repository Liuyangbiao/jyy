package com.nsc.base.jsf.util;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * 画面工具
 * 
 * 此类用于在后台代码中对页面元素进行控制时使用。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:25:04 AM
 * @version
 */
public class FacesUtils {
	
	/**
	 * 在UIComponent中寻找id为uid的子UIComponent
	 * @param uiComponent
	 * @param uId
	 * @return id为uid的UIComponent对象或者是null
	 */
	public static UIComponent getUIComponent(UIComponent uiComponent,String uId){
		for(UIComponent ui : uiComponent.getChildren()){
			if(ui.getId().equals(uId))
				return ui;
			else{
				UIComponent tui= getUIComponent(ui,uId);
				if (null!=tui) return tui;
			}
		}
		return null;
	}
	
	/**
	 * 按名称得到由FacesContext管理的java对象
	 * @param facesContext
	 * @param beanAlias
	 * @return java对象
	 */
	public static Object getManagedBean(FacesContext facesContext,String beanAlias){
		ELContext elContext = facesContext.getELContext();
		return FacesContext.getCurrentInstance()
							.getApplication()
							.getELResolver()
							.getValue(elContext, null, beanAlias);
	}
	
	public static String getBasePath(){
		String filePath;
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		filePath = session.getServletContext().getRealPath("/").replace("/", "\\");
		return filePath;
	}
}
