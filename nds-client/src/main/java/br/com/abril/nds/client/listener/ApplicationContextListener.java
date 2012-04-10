package br.com.abril.nds.client.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

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
                
                logger.info(String.format("Desregistrando driver JDBC: %s", driver));
                
            } catch (SQLException e) {
            	
            	logger.error(String.format("Erro desregistrando driver JDBC:  %s", driver), e);
            }

        }
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		try {
			
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();

        } catch (SchedulerException se) {
        	
        	logger.fatal("Falha ao inicializar agendador do Quartz", se);
        	
            throw new RuntimeException(se);
        }
	}

}
