package br.com.abril.nds.server.listener;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.abril.nds.server.job.IntegracaoOperacionalDistribuidorJob;
import br.com.abril.nds.util.PropertiesUtil;
import br.com.abril.nds.util.QuartzUtil;

/**
 * Listener do contexto da aplicação.
 * 
 * @author Discover Technology
 * 
 */
public class ApplicationContextListener implements ServletContextListener {
	
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationContextListener.class);
    
    private final static Marker FATAL = MarkerFactory.getMarker("FATAL");
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		
        while (drivers.hasMoreElements()) {
        	
            Driver driver = drivers.nextElement();
            
            try {
            	
                DriverManager.deregisterDriver(driver);
                
                LOGGER.info(String.format("Desregistrando driver JDBC: %s", driver));
                
            } catch (SQLException e) {
            	
                LOGGER.error(String.format("Erro desregistrando driver JDBC:  %s", driver), e);
            }

        }
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
		
		SchedulerFactoryBean schedulerFactoryBean = springContext.getBean(SchedulerFactoryBean.class);
		 
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		this.agendarIntegracaoOperacionalDistribuidor(scheduler);
		
	}
	
	    /*
     * Efetua o agendamento do serviço de integração operacional do
     * distribuidor.
     */
	private void agendarIntegracaoOperacionalDistribuidor(Scheduler scheduler) {
		
		try {
			
			String groupName = "integracaoGroup";
			
			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);
			
			PropertiesUtil propertiesUtil = new PropertiesUtil("integracao-distribuidor.properties");
			
			String intervaloExecucaoIntegracaoOperacionalDistribuidor =
				propertiesUtil.getPropertyValue("intervalo.execucao.integracao.operacional");
            
            JobDetail job = 
            	newJob(IntegracaoOperacionalDistribuidorJob.class)
            	    .withIdentity("integracaoOperacionalJob", groupName).build();
            
            CronTrigger cronTrigger = 
            	newTrigger().withIdentity("integracaoOperacionalTrigger", groupName)
            		.withSchedule(cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor)).build();

            scheduler.scheduleJob(job, cronTrigger);
            
            scheduler.start();

        } catch (SchedulerException se) {
        	
            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);
        	
            throw new RuntimeException(se);
        }
	}

}
