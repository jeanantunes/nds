package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;

public interface EntradaNFETerceirosRepository extends Repository<NotaFiscalEntrada, Long> {
	
	List<ConsultaEntradaNFETerceirosRecebidasDTO> consultarNotasRecebidas(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer buscarTotalNotas(FiltroEntradaNFETerceiros filtro);
	
	List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro, boolean limitar);
	
	Integer qtdeNotasRecebidas(FiltroEntradaNFETerceiros filtro);

	Integer qtdeNotasPendentesRecebimento(FiltroEntradaNFETerceiros filtro);

	List<ConsultaEntradaNFETerceirosPendentesDTO> consultaNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro, boolean limitar);

	Integer qtdeNotasPendentesEmissao(FiltroEntradaNFETerceiros filtro);

}
