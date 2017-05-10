package br.com.abril.nds.client.job;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.service.GerarArquivosMicroservicosService;

public class Ems0108MatrizJob implements Job {
	
	private GerarArquivosMicroservicosService gerarArquivoMatrizService; 
	
	public Ems0108MatrizJob() {
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");
			
			this.gerarArquivoMatrizService = 
				applicationContext.getBean(GerarArquivosMicroservicosService.class);
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		Calendar dataAtual = Calendar.getInstance();
		
		this.gerarArquivoMatrizService.gerarArquivoMatriz(dataAtual.getTime());
	}

}
