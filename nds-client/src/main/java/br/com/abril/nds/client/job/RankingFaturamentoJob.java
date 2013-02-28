package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.abril.nds.service.RankingFaturamentoService;

public class RankingFaturamentoJob implements Job {

	private RankingFaturamentoService rankingFaturamentoService;
	
	@Override
	public void execute(JobExecutionContext jobExecContext) throws JobExecutionException {
		this.rankingFaturamentoService.executeJobGerarRankingFaturamento();
	}
	

}
