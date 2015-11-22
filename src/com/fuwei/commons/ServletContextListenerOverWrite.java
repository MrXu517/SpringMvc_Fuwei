package com.fuwei.commons;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ServletContextListenerOverWrite implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			SystemCache.init();
			SystemSettings.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
