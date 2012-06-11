package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNFEEncalheTratamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;

public interface ConsultaNFEEncalheTratamentoNotasRecebidasRepository extends Repository<NotaFiscalEntrada, Long> {
	
	List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar);

}
