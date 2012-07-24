package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.server.model.Distribuidor;
import br.com.abril.nds.service.IntegracaoOperacionalDistribuidorService;

/**
 * Job responsável pela integração operacional do distribuidor.
 * 
 * @author Discover Technology
 *
 */
public class IntegracaoOperacionalDistribuidorJob implements Job {

	private IntegracaoOperacionalDistribuidorService integracaoOperacionalDistribuidorService;
	
	/**
	 * Construtor.
	 */
	public IntegracaoOperacionalDistribuidorJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.integracaoOperacionalDistribuidorService = 
			applicationContext.getBean(IntegracaoOperacionalDistribuidorService.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		Distribuidor distribuidorServer = 
			this.integracaoOperacionalDistribuidorService.obterInformacoesOperacionais();
		
		this.integracaoOperacionalDistribuidorService.integrarInformacoesOperacionais(distribuidorServer);
	}

}
