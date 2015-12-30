package com.nsc.dem.webservice.client;

import org.apache.axis2.AxisFault;
import org.directwebremoting.util.Logger;

public class EDMServiceCall {

	/**
	 * @param args
	 * @throws AxisFault 
	 */
	public static void main(String[] args) throws AxisFault {
		RPCClient serviceClient = RPCClient.getRPCClient("attribute", "serviceUrl");
		Object result = serviceClient.wholeSearch("ц╩сп", new Boolean(true), new Integer(1), new Integer(10));
		Logger.getLogger(EDMServiceCall.class).info("The calling result is "+new String((byte[])result));
	}

}
