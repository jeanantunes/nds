package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;

public interface LancamentoParcialService {

	/**
	 * Obter Lancamentos parciais filtrados
	 * 
	 * @param filtro - Dados do filtro
	 * @return Lista de ParcialDTO
	 */
	List<ParcialDTO> buscarLancamentosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna a quantidade de registros que a busca com filtro trará.
	 * 
	 * @param filtro 
	 * @return Quantidade de registros
	 */
	Integer totalBuscaLancamentosParciais(FiltroParciaisDTO filtro);

	
}
