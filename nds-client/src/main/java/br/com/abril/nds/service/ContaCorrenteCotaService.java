package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;

public interface ContaCorrenteCotaService {
	
	List<ViewContaCorrenteCota> obterListaConsolidadoPorCota(FiltroViewContaCorrenteCotaDTO filtro);

	public abstract Long getQuantidadeViewContaCorrenteCota(FiltroViewContaCorrenteCotaDTO filtro);

}
