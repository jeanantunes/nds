package br.com.abril.nds.client.listener;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import br.com.abril.nds.client.job.IntegracaoOperacionalDistribuidorJob;
import br.com.abril.nds.util.PropertiesUtil;
import br.com.abril.nds.util.QuartzUtil;

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
		
		this.agendarIntegracaoOperacionalDistribuidor();
	}
	
	/*
	 * Efetua o agendamento do serviço de integração operacional do distribuidor.
	 */
	private void agendarIntegracaoOperacionalDistribuidor() {
		
		try {
			
			String groupName = "integracaoGroup";
			
			QuartzUtil.removeJobsFromGroup(groupName);
			
			PropertiesUtil propertiesUtil = new PropertiesUtil("integracao-distribuidor.properties");
			
			String intervaloExecucaoIntegracaoOperacionalDistribuidor =
				propertiesUtil.getPropertyValue("intervalo.execucao.integracao.operacional");
			
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            
            JobDetail job = 
            	newJob(IntegracaoOperacionalDistribuidorJob.class)
            	    .withIdentity("integracaoOperacionalJob", groupName).build();
            
            CronTrigger cronTrigger = 
            	newTrigger().withIdentity("integracaoOperacionalTrigger", groupName)
            		.withSchedule(cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor)).build();

            scheduler.scheduleJob(job, cronTrigger);
            
            scheduler.start();

        } catch (SchedulerException se) {
        	
        	logger.fatal("Falha ao inicializar agendador do Quartz", se);
        	
            throw new RuntimeException(se);
        }
	}

}
