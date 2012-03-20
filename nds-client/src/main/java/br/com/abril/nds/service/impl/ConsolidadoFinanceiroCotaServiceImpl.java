package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsolidadoFinanceiroCotaDTO;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.service.ConsolidadoFinanceiroCotaService;

@Service
public class ConsolidadoFinanceiroCotaServiceImpl implements ConsolidadoFinanceiroCotaService {

	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Transactional
	public List<ConsolidadoFinanceiroCotaDTO> obterListaConsolidadoPorCota(Integer numeroCota) {
		return this.consolidadoFinanceiroRepository.buscarListaDeConsolidado(numeroCota);
		
	}
}
