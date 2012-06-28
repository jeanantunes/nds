package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;

/**
 * 
 * Job responsável por criar movimentos financeiros 
 * para cotas que tem servico de entrega
 * 
 * @author Discover Technology
 *
 */
public class MovimentoFinanceiroServicoEntregaJob implements Job {

	public static final String MOVIMENTO_FINANCEIRO_SERVICO_DATA_KEY = "movimentoFinanceiroServico";
	
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	public MovimentoFinanceiroServicoEntregaJob(){
		
		ClassPathXmlApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.movimentoFinanceiroCotaService = applicationContext.getBean(MovimentoFinanceiroCotaService.class);
	}
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		MovimentoFinanceiroCotaDTO dto = (MovimentoFinanceiroCotaDTO) 
				jobExecutionContext.getMergedJobDataMap().get(MOVIMENTO_FINANCEIRO_SERVICO_DATA_KEY);
		
		if (dto != null){
			
			this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(dto);
		}
	}

}