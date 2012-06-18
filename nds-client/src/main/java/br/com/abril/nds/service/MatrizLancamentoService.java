package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;

public interface MatrizLancamentoService {

	/**
	 * Obtém a matriz de balanceamento de lançamento e todas as informações refentes ao balanceamento.
	 * 
	 * @param filtro - filtro para realização do balanceamento
	 * 
	 * @return {@link BalanceamentoLancamentoDTO}
	 */
	BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro);
	
	/**
	 * Confirma as matriz de balanceamento de lançamento.
	 * 
	 * @param matrizLancamento - matriz de balanceamento de lançamento
	 */
	void confirmarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento);
	
	List<LancamentoDTO> buscarLancamentosBalanceamento(FiltroLancamentoDTO filtro);
	
	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores);

	List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodo(Date dataInicial,
			List<Long> fornecedores);
	
}
