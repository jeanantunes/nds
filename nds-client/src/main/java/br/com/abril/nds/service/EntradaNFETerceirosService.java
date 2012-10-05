package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaNFENotasPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;

public interface EntradaNFETerceirosService {
	
	List<ConsultaEntradaNFETerceirosDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTodasNFENotasRecebidas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaNFENotasPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroEntradaNFETerceiros filtro);
	
	Integer buscarTodasItensPorNota(FiltroEntradaNFETerceiros filtro);
	
}
