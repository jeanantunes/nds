package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;

public interface ContaCorrenteCotaService {
	
	List<ViewContaCorrenteCota> obterListaConsolidadoPorCota(FiltroViewContaCorrenteCotaDTO filtro);

	public abstract Long getQuantidadeViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro);

	List<DebitoCreditoCotaDTO> consultarDebitoCreditoCota(Long idConsolidado,
			Date data, String sortorder, String sortname);

	BigDecimal consultarJurosCota(Long idConsolidado, Date data);

	BigDecimal consultarMultaCota(Long idConsolidado, Date data);
}