package br.com.abril.nds.client.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * Job responsável pela atualização da situação das cotas.
 * 
 * @author Discover Technology
 *
 */
public class StatusCotaJob implements Job {
	
	public static final String HISTORICO_SITUACAO_COTA_DATA_KEY = "historicoSituacaoCota";
	
	public static final String FIM_PERIODO_VALIDADE_SITUACAO_COTA_DATA_KEY = "fimPeriodoValidadeSituacaoCota";
	
	private SituacaoCotaService situacaoCotaService;
	
	private DistribuidorRepository distribuidorRepository;
	
	private CotaService cotaService;
	
	/**
	 * Construtor.
	 */
	public StatusCotaJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
			
		this.situacaoCotaService = applicationContext.getBean(SituacaoCotaService.class);
		
		this.distribuidorRepository = applicationContext.getBean(DistribuidorRepository.class);
		
		this.cotaService = applicationContext.getBean(CotaService.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		HistoricoSituacaoCota historicoSituacaoCota = 
			(HistoricoSituacaoCota) jobExecutionContext.getMergedJobDataMap().get(HISTORICO_SITUACAO_COTA_DATA_KEY);
		
		boolean fimPeriodoSituacaoCota = 
			jobExecutionContext.getMergedJobDataMap().getBooleanValue(FIM_PERIODO_VALIDADE_SITUACAO_COTA_DATA_KEY);
		
		if (fimPeriodoSituacaoCota) {
			
			historicoSituacaoCota.setDataInicioValidade(historicoSituacaoCota.getDataFimValidade());
			historicoSituacaoCota.setDataFimValidade(null);
			historicoSituacaoCota.setNovaSituacao(historicoSituacaoCota.getSituacaoAnterior());
			historicoSituacaoCota.setDescricao(null);
			historicoSituacaoCota.setMotivo(null);
			
			Cota cota = this.cotaService.obterPorId(historicoSituacaoCota.getCota().getId());
			
			if (cota != null) {
				
				historicoSituacaoCota.setSituacaoAnterior(cota.getSituacaoCadastro());
			}
		}
		
		Date dataDeOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();	

		this.situacaoCotaService.atualizarSituacaoCota(historicoSituacaoCota, dataDeOperacao);
	}

}
