package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;

public interface EntradaNFETerceirosService {
	
	List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTodasNFENotasRecebidas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroEntradaNFETerceiros filtro);
	
	Integer buscarTodasItensPorNota(FiltroEntradaNFETerceiros filtro);
	
}
