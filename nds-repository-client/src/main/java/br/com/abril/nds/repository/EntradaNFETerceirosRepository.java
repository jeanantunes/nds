package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface EntradaNFETerceirosRepository extends Repository<NotaFiscalEntrada, Long> {
	
	List<ConsultaEntradaNFETerceirosRecebidasDTO> buscarNFNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTotalNotas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaEntradaNFETerceirosPendentesDTO> buscarNFNotasPendentes(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	List<ItemNotaFiscalPendenteDTO> buscarItensPorNota(Long idConferenciaCota,String  orderBy,Ordenacao ordenacao, Integer firstResult, Integer maxResults);
	
	Integer buscarTodasItensPorNota(Long idConferenciaCota);

}
