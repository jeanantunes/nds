package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;

public interface PeriodoLancamentoParcialService {

	/**
	 * Retorna DTOs de periodos parciais de acordo com o filtro
	 * 
	 * @param filtro
	 */
	List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna COUNT total da pesquisa "obterPeriodosParciais"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro);
	
	PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento);

}
