package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.repository.AjusteReparteRepository;

public class AjusteReparteJob implements Job {

	private AjusteReparteRepository ajusteRepository;
	
	public AjusteReparteJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.ajusteRepository = 
			applicationContext.getBean(AjusteReparteRepository.class);
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		ajusteRepository.execucaoQuartz();		
	}
	
}
