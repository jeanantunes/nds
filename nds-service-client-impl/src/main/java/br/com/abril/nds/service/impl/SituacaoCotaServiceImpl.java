package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.quartz.impl.StdScheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.QuartzUtil;

/**
 * Classe de implementação de serviços referentes a situação de cota.
 * 
 * @author Discover Technology
 *
 */
@Service
public class SituacaoCotaServiceImpl implements SituacaoCotaService, ApplicationContextAware {

	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	private ApplicationContext applicationContext;

	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public List<HistoricoSituacaoCotaVO> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		if(filtro.getNumeroCota() == null) {
			
			return this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
		}
		
		return this.historicoSituacaoCotaRepository.obterUltimoHistoricoStatusCota(filtro);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterTotalHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		if(filtro.getNumeroCota() == null) {
			
			return this.historicoSituacaoCotaRepository.obterTotalHistoricoStatusCota(filtro);
		}
		
		return this.historicoSituacaoCotaRepository.obterTotalUltimoHistoricoStatusCota(filtro);
	
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#atualizarSituacaoCota(br.com.abril.nds.model.cadastro.HistoricoSituacaoCota)
	 */
	@Transactional
	public void atualizarSituacaoCota(HistoricoSituacaoCota historicoSituacaoCota, Date dataDeOperacao) {	
		
		if (historicoSituacaoCota == null 
				|| historicoSituacaoCota.getCota() == null
				|| historicoSituacaoCota.getCota().getId() == null
				|| dataDeOperacao == null) {
			
			throw new IllegalArgumentException("Parâmetros inválidos!");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(historicoSituacaoCota.getCota().getId());
		
		if (cota == null) {
			
			throw new RuntimeException("Cota inexistente!");
		}

		if (DateUtil.obterDiferencaDias(
				dataDeOperacao, historicoSituacaoCota.getDataInicioValidade()) == 0) {
		
			historicoSituacaoCota.setProcessado(true);
			
			cota.setSituacaoCadastro(historicoSituacaoCota.getNovaSituacao());
			
			this.cotaRepository.alterar(cota);
			
		} else {
			
			historicoSituacaoCota.setProcessado(false);
		}
		
		historicoSituacaoCota.setDataEdicao(new Date());

		historicoSituacaoCota.setRestaurado(false);
		
		this.historicoSituacaoCotaRepository.adicionar(historicoSituacaoCota);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#removerAgendamentosAlteracaoSituacaoCota(java.lang.Long)
	 */
	public void removerAgendamentosAlteracaoSituacaoCota(Long idCota) {
		
		if (idCota == null) {
			
			throw new IllegalArgumentException("ID da Cota nulo!");
		}
		
		StdScheduler scheduler = (StdScheduler) applicationContext.getBean("schedulerFactoryBean");

		QuartzUtil.doAgendador(scheduler).removeJobsFromGroup(idCota.toString());
	}
	
	@Override
	@Transactional(readOnly=true)
	public SituacaoCadastro obterSituacaoCadastroCota(Integer numeroCota){
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número de cota é obrigatório.");
		}
		
		return this.cotaRepository.obterSituacaoCadastroCota(numeroCota);
	}
}