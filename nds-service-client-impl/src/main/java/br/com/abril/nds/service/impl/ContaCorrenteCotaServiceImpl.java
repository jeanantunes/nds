package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.repository.ViewContaCorrenteCotaRepository;
import br.com.abril.nds.service.ContaCorrenteCotaService;

@Service
public class ContaCorrenteCotaServiceImpl implements ContaCorrenteCotaService {

	@Autowired
	private ViewContaCorrenteCotaRepository viewContaCorrenteCotaRepository;	
	

	@Transactional(readOnly=true)
	public List<ViewContaCorrenteCota> obterListaConsolidadoPorCota(FiltroViewContaCorrenteCotaDTO filtro) {
		return viewContaCorrenteCotaRepository.getListaViewContaCorrenteCota(filtro);
		
	}


	/**
	 * @param filtro
	 * @return
	 * @see br.com.abril.nds.repository.ViewContaCorrenteCotaRepository#getQuantidadeViewContaCorrenteCota(br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long getQuantidadeViewContaCorrenteCota(
			FiltroViewContaCorrenteCotaDTO filtro) {
		return viewContaCorrenteCotaRepository
				.getQuantidadeViewContaCorrenteCota(filtro);
	}
	
	
}
