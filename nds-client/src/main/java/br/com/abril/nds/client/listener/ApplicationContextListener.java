package br.com.abril.nds.client.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * Listener do contexto da aplicação.
 * 
 * @author Discover Technology
 *
 */
public class ApplicationContextListener implements ServletContextListener {
	
	private Logger logger = Logger.getLogger(ApplicationContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		
        while (drivers.hasMoreElements()) {
        	
            Driver driver = drivers.nextElement();
            
            try {
            	
                DriverManager.deregisterDriver(driver);
                
                logger.info(String.format("Deregistering jdbc driver: %s", driver));
                
            } catch (SQLException e) {
            	
            	logger.error(String.format("Error deregistering driver %s", driver), e);
            }

        }
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) { }

}
