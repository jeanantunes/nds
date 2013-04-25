package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.repository.FixacaoReparteRepository;

public class FixacaoReparteJob implements Job {

	private FixacaoReparteRepository fixacaoReparteRepository;
	
public FixacaoReparteJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.fixacaoReparteRepository = applicationContext.getBean(FixacaoReparteRepository.class);
	}
	
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		fixacaoReparteRepository.execucaoQuartz();		
	}

}
