package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface EntradaNFETerceirosService {
	
	List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTodasNFNotas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroEntradaNFETerceiros filtro);
	
	Integer buscarTodasItensPorNota(FiltroEntradaNFETerceiros filtro);

	public abstract List<ItemNotaFiscalEntrada> obtemItemNotaFiscalEntradaPorControleConferenciaEncalheCota(
			long idControleConferencia, String orderBy, Ordenacao ordenacao, Integer firstResult, Integer maxResults);

	public abstract Long quantidadeItemNotaFiscalEntradaPorControleConferenciaEncalheCota(
			long idControleConferencia);
	
}
