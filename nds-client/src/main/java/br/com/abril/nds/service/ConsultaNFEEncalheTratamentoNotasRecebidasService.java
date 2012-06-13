package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNFEEncalheTratamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;

public interface ConsultaNFEEncalheTratamentoNotasRecebidasService {
	
	List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
	Integer buscarTodasNFENotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro);
	
	List<ConsultaNFEEncalheTratamentoDTO> buscarNFNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
}
