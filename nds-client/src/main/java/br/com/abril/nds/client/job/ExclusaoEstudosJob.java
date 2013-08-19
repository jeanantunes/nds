package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.service.EstudoService;

/**
 * Job responsável pela exclusão de estudos anualmente
 * 
 * 
 *
 */
public class ExclusaoEstudosJob implements Job {

	private EstudoService estudoService;
	
	/**
	 * Construtor.
	 */
	public ExclusaoEstudosJob() {
		
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.estudoService = (EstudoService)applicationContext.getBean(Estudo.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		
		
	}

}
