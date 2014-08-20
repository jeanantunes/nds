package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.repository.RegiaoRepository;

public class RegiaoJob implements Job {

	private RegiaoRepository regiaoRepository;
	
public RegiaoJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.regiaoRepository = applicationContext.getBean(RegiaoRepository.class);
	}
	
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		regiaoRepository.execucaoQuartz();		
	}

}
