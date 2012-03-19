package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsolidadoFinanceiroCotaDTO;

public interface ConsolidadoFinanceiroCotaService {
	
	List<ConsolidadoFinanceiroCotaDTO> obterListaConsolidadoPorCota(Integer idCota);

}
