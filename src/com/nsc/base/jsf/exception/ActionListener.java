package com.nsc.base.jsf.exception;

import java.io.IOException;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.nsc.base.jsf.security.Authentication;
import com.nsc.base.util.AppException;
import com.sun.faces.application.ActionListenerImpl;
import com.sun.faces.util.MessageUtils;

/**
 * Action监听类
 * 
 * 此类为Action事件的监听类。在Action处理中一旦发生异常则可被其捕获，并根据配置文件进行相应处理。
 * 为使用该类功能，需要在配置文件中定义此类作为Action监听器。
 * 
 * @author bs-team
 *
 * @date Oct 19, 2010 10:14:09 AM
 * @version
 */
public class ActionListener extends ActionListenerImpl {
	
	/**
	 * Action处理
	 * 
	 * @param event
	 */
	public void processAction(ActionEvent event) {
		try{
			UIComponent uIComponent=event.getComponent();
			if(uIComponent instanceof UICommand){ 
				UICommand uICommand=(UICommand)uIComponent;
				MethodExpression actionExpression=uICommand.getActionExpression();
				
				if(actionExpression!=null &&
				   !Authentication.getInstance().authenticate(getViewId(FacesContext.getCurrentInstance()), 
						actionExpression.getExpressionString())){
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(FacesMessage.SEVERITY_WARN,"无权操作",""));
					return;
				}
				
				/*javax.faces.event.ActionListener[] actionListeners=uICommand.getActionListeners();
				for(javax.faces.event.ActionListener listener:actionListeners){
					Authentication.instance().authenticate(getViewId(FacesContext.getCurrentInstance()), 
							listener.toString());
				}*/
			}
			
			super.processAction(event);
		} catch (Throwable ex) {
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			
			if(ex instanceof AppException){
				AppException appEx=(AppException)ex;
				if(appEx.getSysMId()!=null && appEx.getSysMId().length()>0){
					FacesMessage sysMessage=MessageUtils.getExceptionMessage(appEx.getSysMId(), appEx.getArguments());
					facesContext.addMessage("sys", sysMessage);
				}
				if(appEx.getUserMId()!=null && appEx.getUserMId().length()>0){
					FacesMessage userMessage=MessageUtils.getExceptionMessage(appEx.getSysMId(), appEx.getArguments());
					facesContext.addMessage("user", userMessage);
				}
				
				if(appEx.getOriginalM()!=null && appEx.getOriginalM().length()>0){
					FacesMessage refMessage=new FacesMessage(appEx.getOriginalM());
					facesContext.addMessage("ref", refMessage);
				}
				
			}else{
				facesContext.addMessage("sys", MessageUtils.getExceptionMessage("sys.error.uncheck.sys"));
				facesContext.addMessage("user", MessageUtils.getExceptionMessage("sys.error.uncheck.user"));
				facesContext.addMessage("ref", MessageUtils.getExceptionMessage("sys.error.uncheck.ref", new Object[]{ex.getMessage()}));
			}
			
			if(event.getComponent() instanceof HtmlCommandLink){
				this.redirect(facesContext, "/exception.xhtml");
			}else{
				facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext,null,"exception");
				facesContext.renderResponse();
			}
		}
	}
	
	/**
	 * 重定向
	 * @param context
	 * @param viewId
	 */
	private void redirect(FacesContext context,String viewId){
		String url = context.getApplication().getViewHandler().getActionURL(context, viewId);
	    ExternalContext externalContext = context.getExternalContext();
	    try
	      {
	        externalContext.redirect( externalContext.encodeActionURL(url));
	      }
	      catch (IOException ioe)
	      {
	         throw new AppException(ioe,"sys.redirect.io",null,new String[]{viewId});
	      }
	      context.responseComplete();
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

/**
 * 
 * UIComponent uIComponent=event.getComponent();
			HtmlDatascroller dataScroller;
			
			if(uIComponent instanceof HtmlDatascroller){
				dataScroller=(HtmlDatascroller)uIComponent;
				String forName=dataScroller.getFor();
				StringBuffer expressStr=null;
				String classAlias=null;
				Object bean;
				
				
				String page=dataScroller.getOnclick();
				UIComponent tableComponent=FacesUtils.getUIComponent(FacesContext.getCurrentInstance().getViewRoot(), forName);
				
				logger.info("ActionListener.processAction "+page);
						
				if(null!=tableComponent){
					ValueExpression valueExpression=tableComponent.getValueExpression("value");
					expressStr=new StringBuffer(valueExpression.getExpressionString());
					classAlias=expressStr.substring(2,expressStr.indexOf("."));
					ELContext elContext = FacesContext.getCurrentInstance().getELContext();
					bean=FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, classAlias);
					
					CapitalQuery capitalQuery=(CapitalQuery)bean;
					List<Object> list=capitalQuery.getCapitalList();
					for(Object l:list){
						((Zygcyearplan)l).setJskind("uyt");
						logger.info("ActionListener.processAction "+((Zygcyearplan)l).getJskind());
					}
				}
			}
 * 
 */
