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

import br.com.abril.nds.client.job.AjusteReparteJob;
import br.com.abril.nds.client.job.IntegracaoOperacionalDistribuidorJob;
import br.com.abril.nds.client.job.RankingFaturamentoJob;
import br.com.abril.nds.client.job.RankingSegmentoJob;
import br.com.abril.nds.client.job.RegiaoJob;
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

				logger.info(String.format("Desregistrando driver JDBC: %s",
						driver));

			} catch (SQLException e) {

				logger.error(String.format(
						"Erro desregistrando driver JDBC:  %s", driver), e);
			}

		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		this.agendarIntegracaoOperacionalDistribuidor();
		this.agendaExclusaoAjusteReparte();
		this.agendaExclusaoRegiao();
		this.agendarExclusaoDeEstudos();
		this.agendarGeracaoRankings();
		
		try {
			StdSchedulerFactory.getDefaultScheduler().start();
		} catch (SchedulerException e) {
			logger.fatal("Falha ao inicializar agendador do Quartz", e);

			throw new RuntimeException(e);
		}

	}

	/*
	 * Efetua o agendamento do serviço de integração operacional do
	 * distribuidor.
	 */
	private void agendarIntegracaoOperacionalDistribuidor() {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucaoIntegracaoOperacionalDistribuidor = propertiesUtil
					.getPropertyValue("intervalo.execucao.integracao.operacional");

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDetail job = newJob(IntegracaoOperacionalDistribuidorJob.class)
					.withIdentity("integracaoOperacionalJob", groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("integracaoOperacionalTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

			scheduler.start();

		} catch (SchedulerException se) {

			logger.fatal("Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
	
	private void agendarExclusaoDeEstudos() {

		try {

			String groupName = "exclusaoEstudoGroup";

			QuartzUtil.removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil("exclusao-estudos.properties");

			String intervaloExecucaoIntegracaoOperacionalDistribuidor = propertiesUtil
					.getPropertyValue("intervalo.execucao.exclusao.estudos");
			 
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDetail job = newJob(IntegracaoOperacionalDistribuidorJob.class)
					.withIdentity("exclusaoEstudosJob", groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("exclusaoEstudosTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

			scheduler.start();

		} catch (SchedulerException se) {

			logger.fatal("Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}

	
	private void agendarGeracaoRankings(){
		final String groupName = "gerarRankingGroup";
		
		QuartzUtil.removeJobsFromGroup(groupName);
		
		PropertiesUtil propertiesUtil = new PropertiesUtil(
				"integracao-distribuidor.properties");
		
		
		String intervaloExecucaoGeracaoRanking = propertiesUtil
				.getPropertyValue("intervalo.execucao.geracao.ranking");
		
		JobDetail jobRankingFaturamento = newJob(RankingFaturamentoJob.class)
				.withIdentity(RankingFaturamentoJob.class.getName(), groupName)
				.build();
		
		JobDetail jobRankingSegmento= newJob(RankingSegmentoJob.class)
				.withIdentity(RankingSegmentoJob.class.getName(), groupName)
				.build();
		
		
		CronTrigger cronTriggerRankingFaturamento = newTrigger()
				.withIdentity("gerarRankingFaturamentoTrigger", groupName)
				.withSchedule(
						cronSchedule(intervaloExecucaoGeracaoRanking))
				.build();
		
		CronTrigger cronTriggerRankingSegmento = newTrigger()
				.withIdentity("gerarRankingSegmentoTrigger", groupName)
				.withSchedule(
						cronSchedule(intervaloExecucaoGeracaoRanking))
				.build();
		
		Scheduler scheduler = null;
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.scheduleJob(jobRankingFaturamento, cronTriggerRankingFaturamento);
			scheduler.scheduleJob(jobRankingSegmento, cronTriggerRankingSegmento);
		} catch (SchedulerException e) {
			logger.fatal("Falha ao inicializar agendador do Quartz", e);

			throw new RuntimeException(e);
		}
		
		
	}

	
	/*
	 * Efetua o agendamento do serviço de exclusão de 
	 * ajuste de reparte com mais de 180 dias.
	 * 
	 */
	private void agendaExclusaoAjusteReparte() {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucao = propertiesUtil
					.getPropertyValue("intervalo.execucao.ajuste.reparte");

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDetail job = newJob(AjusteReparteJob.class)
					.withIdentity(AjusteReparteJob.class.getName(), groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("ajusteReparteTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucao))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

			scheduler.start();

		} catch (SchedulerException se) {

			logger.fatal("Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
	/*
	 * Efetua o agendamento do serviço de exclusão de Região não fixa
	 * com mais de 90 dias.
	 * 
	 */
	private void agendaExclusaoRegiao() {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucao = propertiesUtil
					.getPropertyValue("intervalo.execucao.regiao");

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDetail job = newJob(RegiaoJob.class)
					.withIdentity(RegiaoJob.class.getName(), groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("regiaoTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucao))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

			scheduler.start();

		} catch (SchedulerException se) {

			logger.fatal("Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
}
