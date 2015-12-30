package com.nsc.dem.util.listener;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;

public interface IListenerSet {
	public void addAttribute(HttpSessionBindingEvent even,ServletContext application)throws IOException;
	
	public void changeAttribute(HttpSessionBindingEvent even,ServletContext application)throws IOException;
}
