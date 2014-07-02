package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;

public interface ContaCorrenteCotaService {
	
	List<DebitoCreditoCota> consultarDebitoCreditoCota(Long idConsolidado,
			Date data, Integer numeroCota, String sortorder, String sortname);

	BigDecimal consultarJurosCota(Long idConsolidado, Date data, Integer numeroCota);

	BigDecimal consultarMultaCota(Long idConsolidado, Date data, Integer numeroCota);

	List<MovimentoFinanceiroDTO> consultarValorVendaDia(Integer numeroCota, Long idConsolidado, Date data);
}