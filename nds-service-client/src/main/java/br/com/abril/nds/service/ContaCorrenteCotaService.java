package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DebitoCreditoCotaDTO;

public interface ContaCorrenteCotaService {
	
	List<DebitoCreditoCotaDTO> consultarDebitoCreditoCota(Long idConsolidado,
			Date data, Integer numeroCota, String sortorder, String sortname);

	BigDecimal consultarJurosCota(Long idConsolidado, Date data, Integer numeroCota);

	BigDecimal consultarMultaCota(Long idConsolidado, Date data, Integer numeroCota);
}