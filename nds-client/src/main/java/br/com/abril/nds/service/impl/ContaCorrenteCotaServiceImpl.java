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
	
	
	@Transactional
	public List<ViewContaCorrenteCota> obterListaConsolidadoPorCota(FiltroViewContaCorrenteCotaDTO filtro) {
		return viewContaCorrenteCotaRepository.getListaViewContaCorrenteCota(filtro);
		
	}
}
