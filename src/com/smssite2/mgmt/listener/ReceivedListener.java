package com.smssite2.mgmt.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ReceivedListener implements ServletContextListener{	private ReceivedThread rt = null;
	public void contextDestroyed(ServletContextEvent arg0) {
		if(rt!=null)rt.stop();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		rt = new ReceivedThread();
		rt.start();
		
	}

}
