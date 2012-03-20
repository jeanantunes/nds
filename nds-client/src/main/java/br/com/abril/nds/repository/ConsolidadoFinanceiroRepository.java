package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsolidadoFinanceiroCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public interface ConsolidadoFinanceiroRepository extends Repository<ConsolidadoFinanceiroCota, Long> {
	
	List<ConsolidadoFinanceiroCotaDTO> buscarListaDeConsolidado(Integer idCota);

}
