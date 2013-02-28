package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.abril.nds.service.RankingSegmentoService;

public class RankingSegmentoJob implements Job {

	private RankingSegmentoService rankingSegmentoService; 
	@Override
	public void execute(JobExecutionContext jobExecContext) throws JobExecutionException {

		this.rankingSegmentoService.executeJobGerarRankingSegmento();
	}

}
