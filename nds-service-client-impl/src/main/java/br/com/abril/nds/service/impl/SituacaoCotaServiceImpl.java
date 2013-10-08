package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
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
public class SituacaoCotaServiceImpl implements SituacaoCotaService {

	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public List<HistoricoSituacaoCota> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		if(filtro.getPeriodo() != null) {
			
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
		
		if(filtro.getPeriodo() != null) {
			
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
				|| historicoSituacaoCota.getCota().getId() == null) {
			
			throw new IllegalArgumentException("Parâmetros inválidos!");
		}

		historicoSituacaoCota.setDataEdicao(new Date());
		
		this.historicoSituacaoCotaRepository.adicionar(historicoSituacaoCota);
		
		Cota cota = this.cotaRepository.buscarPorId(historicoSituacaoCota.getCota().getId());
		
		if (cota == null) {
			
			throw new RuntimeException("Cota inexistente!");
		}
		
		if(dataDeOperacao != null && DateUtil.obterDiferencaDias(dataDeOperacao, historicoSituacaoCota.getDataInicioValidade()) == 0) {
		
			cota.setSituacaoCadastro(historicoSituacaoCota.getNovaSituacao());
			
			cotaRepository.alterar(cota); 
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#removerAgendamentosAlteracaoSituacaoCota(java.lang.Long)
	 */
	public void removerAgendamentosAlteracaoSituacaoCota(Long idCota) {
		
		if (idCota == null) {
			
			throw new IllegalArgumentException("ID da Cota nulo!");
		}
		
		QuartzUtil.doAgendador(this.schedulerFactoryBean.getScheduler())
			.removeJobsFromGroup(idCota.toString());
	}


}
