package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaNFENotasRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;

public interface ConsultaNFEEncalheTratamentoNotasRecebidasService {
	
	List<ConsultaNFENotasRecebidasDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
	Integer buscarTodasNFENotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro);
	
	List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroConsultaNFEEncalheTratamento filtro);
	
}
