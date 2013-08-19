package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.service.RankingSegmentoService;

public class RankingSegmentoJob implements Job {

	private RankingSegmentoService rankingSegmentoService;
	
	public RankingSegmentoJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.rankingSegmentoService = 
			applicationContext.getBean(RankingSegmentoService.class);
	}

	@Override
	public void execute(JobExecutionContext jobExecContext) throws JobExecutionException {

		this.rankingSegmentoService.executeJobGerarRankingSegmento();
	}

}
