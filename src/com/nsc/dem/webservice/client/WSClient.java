package com.nsc.dem.webservice.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.nsc.dem.webservice.client.edm.EDMService;

public class WSClient {
	
	private static WSClient client = null;
	private EDMService service;
	
	private WSClient(String url){
		JaxWsProxyFactoryBean client = new JaxWsProxyFactoryBean();
		client.setAddress(url);
		client.setServiceClass(EDMService.class);
		service = client.create(EDMService.class);
		
	}
	
	public static WSClient getClient(String url){
	   client = new WSClient(url);	
	   return client;
	}

	public EDMService getService() {
		return service;
	}
	
	
}
