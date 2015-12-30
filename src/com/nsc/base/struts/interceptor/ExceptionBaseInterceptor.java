package com.nsc.base.struts.interceptor;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.nsc.base.util.BeanUtil;
import com.nsc.dem.action.BaseAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.StaticParametersInterceptor;

public abstract class ExceptionBaseInterceptor extends
		StaticParametersInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6024878512005046709L;

	/**
	 * 异常拦截
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		Object obj = invocation.getAction();

		try {
			PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(obj);

			for (PropertyDescriptor prop : props) {
				String name = prop.getName();
				if (name.indexOf("Service") > 1) {
					BaseAction baseAction = (BaseAction) obj;
					//Method method = prop.getReadMethod();
					
					//if(method==null) continue;
					Object service=BeanUtil.getFieldValue(name, obj);
					
					//MethodUtils.invokeMethod(obj,method.getName(), null);
					
					if(service==null) continue;
					MethodUtils.invokeMethod(baseAction,"setService",service);
				}
			}

			return invocation.invoke();

		} catch (Exception e) {
			ActionSupport actionSupport = (ActionSupport) obj;
			exception(e, actionSupport);

			return "error";// Action.ERROR;

		} catch(Throwable e){
			ActionSupport actionSupport = (ActionSupport) obj;
			exception(e, actionSupport);

			return "error";// Action.ERROR;
		}

	}

	/**
	 * 异常处理
	 * 
	 * @param e
	 * @param actionSupport
	 */
	protected abstract void exception(Exception e, ActionSupport actionSupport);
	
	protected abstract void exception(Throwable e, ActionSupport actionSupport);

}
