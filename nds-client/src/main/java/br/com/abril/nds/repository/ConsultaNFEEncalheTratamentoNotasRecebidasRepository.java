package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaNFENotasRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNFEEncalheTratamento;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;

public interface ConsultaNFEEncalheTratamentoNotasRecebidasRepository extends Repository<NotaFiscalEntrada, Long> {
	
	List<ConsultaNFENotasRecebidasDTO> buscarNFNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
	Integer buscarTotalNotasRecebidas(FiltroConsultaNFEEncalheTratamento filtro);
	
	List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(FiltroConsultaNFEEncalheTratamento filtro, String limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroConsultaNFEEncalheTratamento filtro);
	
	Integer buscarTodasItensPorNota(FiltroConsultaNFEEncalheTratamento filtro);

}
