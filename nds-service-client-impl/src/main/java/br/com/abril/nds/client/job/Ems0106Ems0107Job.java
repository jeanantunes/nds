package br.com.abril.nds.client.job;

import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.service.GerarArquivosMicroDistribuicaoService;

public class Ems0106Ems0107Job implements Job{

	private GerarArquivosMicroDistribuicaoService gerarArquivosEms0106Ems0107Service;
	
	public Ems0106Ems0107Job() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");
			
			this.gerarArquivosEms0106Ems0107Service = 
				applicationContext.getBean(GerarArquivosMicroDistribuicaoService.class);
	}
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		Calendar dataAtual = Calendar.getInstance();
		//this.gerarArquivosEms0106Ems0107Service.gerarArquivoDeajo(dataAtual.getTime());
		//this.gerarArquivosEms0106Ems0107Service.gerarArquivoDeapr(dataAtual.getTime());
		
	}

}
