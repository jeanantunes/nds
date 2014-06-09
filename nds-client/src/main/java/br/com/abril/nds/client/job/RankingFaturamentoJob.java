package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.service.RankingFaturamentoService;

public class RankingFaturamentoJob implements Job {

	private RankingFaturamentoService rankingFaturamentoService;
	
	public RankingFaturamentoJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.rankingFaturamentoService = 
			applicationContext.getBean(RankingFaturamentoService.class);
	}

	@Override
	public void execute(JobExecutionContext jobExecContext) throws JobExecutionException {
		this.rankingFaturamentoService.executeJobGerarRankingFaturamento();
	}
	

}
