package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public interface ConsolidadoFinanceiroRepository extends Repository<ConsolidadoFinanceiroCota, Long> {
	
	List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer idCota);
	
	boolean verificarConsodidadoCotaPorData(Long idCota, Date data);
	
	List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(Integer numeroCota, Date dataConsolidado);

}
