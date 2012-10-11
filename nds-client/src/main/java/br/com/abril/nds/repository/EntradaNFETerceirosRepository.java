package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;

public interface EntradaNFETerceirosRepository extends Repository<NotaFiscalEntrada, Long> {
	
	List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTotalNotas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(FiltroEntradaNFETerceiros filtro);
	
	Integer buscarTodasItensPorNota(FiltroEntradaNFETerceiros filtro);

}
