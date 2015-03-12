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

import br.com.abril.nds.client.component.BloqueioConferenciaEncalheComponent;
import br.com.abril.nds.client.job.AjusteReparteJob;
import br.com.abril.nds.client.job.AtualizaEstoqueJob;
import br.com.abril.nds.client.job.FixacaoReparteJob;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextListener.class);
    private static final Marker FATAL = MarkerFactory.getMarker("FATAL");

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

		Enumeration<Driver> drivers = DriverManager.getDrivers();

		while (drivers.hasMoreElements()) {

			Driver driver = drivers.nextElement();

			try {

				DriverManager.deregisterDriver(driver);

                LOGGER.info(String.format("Desregistrando driver JDBC: %s",
						driver));

			} catch (SQLException e) {

                LOGGER.error(String.format(
						"Erro desregistrando driver JDBC:  %s", driver), e);
			}

		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		try {
			
			final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
			
			BloqueioConferenciaEncalheComponent bloqueioConferenciaEncalheComponent = springContext.getBean(BloqueioConferenciaEncalheComponent.class);
			bloqueioConferenciaEncalheComponent.limparSessionsConferenciaCotaUsuario();
			
			SchedulerFactoryBean schedulerFactoryBean =	springContext.getBean(SchedulerFactoryBean.class);
			 
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			
//			this.agendarIntegracaoOperacionalDistribuidor(scheduler);
//			this.agendaExeclusaoAjusteReparte(scheduler);
//			this.agendarExclusaoDeEstudos(scheduler);
			this.agendarGeracaoRankings(scheduler);
//			this.agendaExclusaoFixacaoReparte();
//			this.agendaExclusaoRegiao();
			
//			this.agendarAtualizacaoEstoqueProdutoConf(scheduler);
			
			scheduler.start();
			
		} catch (SchedulerException e) {
			
           LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", e);

			throw new RuntimeException(e);
		}
		
	}

	private void agendarAtualizacaoEstoqueProdutoConf(Scheduler scheduler) throws SchedulerException {
	    
	    final String groupName = "epcJobGroupName";
	    final String identityTrigger = "epcTriggerIdentity";
	    final String frequencia = "0 0/10 * * * ?";
	    
	    QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);
	    
	    JobDetail job = newJob(AtualizaEstoqueJob.class)
                .withIdentity(AtualizaEstoqueJob.class.getName(), groupName)
                .build();

        CronTrigger cronTrigger = newTrigger()
                .withIdentity(identityTrigger, groupName)
                .withSchedule(
                        cronSchedule(frequencia))
                .build();

        scheduler.scheduleJob(job, cronTrigger);
        
    }

    /*
     * Efetua o agendamento do serviço de integração operacional do
     * distribuidor.
     */
	private void agendarIntegracaoOperacionalDistribuidor(Scheduler scheduler) {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucaoIntegracaoOperacionalDistribuidor = propertiesUtil
					.getPropertyValue("intervalo.execucao.integracao.operacional");

			JobDetail job = newJob(IntegracaoOperacionalDistribuidorJob.class)
					.withIdentity("integracaoOperacionalJob", groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("integracaoOperacionalTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

		} catch (SchedulerException se) {

            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
	
	private void agendarExclusaoDeEstudos(Scheduler scheduler) {

		try {

			String groupName = "exclusaoEstudoGroup";

			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil("exclusao-estudos.properties");

			String intervaloExecucaoIntegracaoOperacionalDistribuidor = propertiesUtil
					.getPropertyValue("intervalo.execucao.exclusao.estudos");

			JobDetail job = newJob(IntegracaoOperacionalDistribuidorJob.class)
					.withIdentity("exclusaoEstudosJob", groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("exclusaoEstudosTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucaoIntegracaoOperacionalDistribuidor))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

		} catch (SchedulerException se) {

            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}

	
	private void agendarGeracaoRankings(Scheduler scheduler) {
		
		final String groupName = "gerarRankingGroup";
		
		QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);
		
		PropertiesUtil propertiesUtil = new PropertiesUtil(
				"integracao-distribuidor.properties");
		
		
		String intervaloExecucaoGeracaoRankingFaturamento = propertiesUtil
				.getPropertyValue("intervalo.execucao.geracao.ranking.faturamento");
		
		String intervaloExecucaoGeracaoRankingSegmento = propertiesUtil
				.getPropertyValue("intervalo.execucao.geracao.ranking.segmento");
		
		
		JobDetail jobRankingFaturamento = newJob(RankingFaturamentoJob.class)
				.withIdentity(RankingFaturamentoJob.class.getName(), groupName)
				.build();
		
		JobDetail jobRankingSegmento= newJob(RankingSegmentoJob.class)
				.withIdentity(RankingSegmentoJob.class.getName(), groupName)
				.build();
		
		
		CronTrigger cronTriggerRankingFaturamento = newTrigger()
				.withIdentity("gerarRankingFaturamentoTrigger", groupName)
				.withSchedule(
						cronSchedule(intervaloExecucaoGeracaoRankingFaturamento))
				.build();
		
		CronTrigger cronTriggerRankingSegmento = newTrigger()
				.withIdentity("gerarRankingSegmentoTrigger", groupName)
				.withSchedule(
						cronSchedule(intervaloExecucaoGeracaoRankingSegmento))
				.build();

		try {
			scheduler.scheduleJob(jobRankingFaturamento, cronTriggerRankingFaturamento);
			scheduler.scheduleJob(jobRankingSegmento, cronTriggerRankingSegmento);
		} catch (SchedulerException e) {
            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", e);

			throw new RuntimeException(e);
		}
	}

	
	        /*
     * Efetua o agendamento do serviço de exclusão de ajuste de reparte.
     */
	private void agendaExeclusaoAjusteReparte(Scheduler scheduler) {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucao = propertiesUtil
					.getPropertyValue("intervalo.execucao.ajuste.reparte");

			JobDetail job = newJob(AjusteReparteJob.class)
					.withIdentity(AjusteReparteJob.class.getName(), groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("ajusteReparteTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucao))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

		} catch (SchedulerException se) {

            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
	        /*
     * Efetua o agendamento do serviço de exclusão de fixacao por reparte.
     */
	private void agendaExclusaoFixacaoReparte(Scheduler scheduler) {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucao = propertiesUtil
					.getPropertyValue("intervalo.execucao.fixacao.reparte");

			JobDetail job = newJob(FixacaoReparteJob.class)
					.withIdentity(FixacaoReparteJob.class.getName(), groupName)
					.build();

			CronTrigger cronTrigger = newTrigger()
					.withIdentity("fixacaoReparteTrigger", groupName)
					.withSchedule(
							cronSchedule(intervaloExecucao))
					.build();

			scheduler.scheduleJob(job, cronTrigger);

			scheduler.start();

		} catch (SchedulerException se) {

            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
	        /*
     * Efetua o agendamento do serviço de exclusão de Regiões.
     */
	private void agendaExclusaoRegiao(Scheduler scheduler) {

		try {

			String groupName = "integracaoGroup";

			QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(groupName);

			PropertiesUtil propertiesUtil = new PropertiesUtil(
					"integracao-distribuidor.properties");

			String intervaloExecucao = propertiesUtil
					.getPropertyValue("intervalo.execucao.regiao");

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

            LOGGER.error(FATAL, "Falha ao inicializar agendador do Quartz", se);

			throw new RuntimeException(se);
		}
	}
	
}
