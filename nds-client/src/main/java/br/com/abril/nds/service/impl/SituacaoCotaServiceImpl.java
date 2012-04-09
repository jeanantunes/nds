package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.service.SituacaoCotaService;

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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public List<HistoricoSituacaoCota> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {

		return this.historicoSituacaoCotaRepository.obterHistoricoStatusCota(filtro);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.SituacaoCotaService#obterTotalHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@Transactional(readOnly = true)
	public Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro) {

		return this.historicoSituacaoCotaRepository.obterTotalHistoricoStatusCota(filtro);
	}

}
