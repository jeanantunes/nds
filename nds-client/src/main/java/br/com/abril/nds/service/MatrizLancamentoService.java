package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.seguranca.Usuario;

public interface MatrizLancamentoService {

	/**
	 * Obtém a matriz de balanceamento de lançamento e todas as informações refentes ao balanceamento.
	 * 
	 * @param filtro - filtro para realização do balanceamento
	 * @param configuracaoInicial - indicada se a matriz de lançamento deve ser sugerida de acordo com configuração inicial
	 * 
	 * @return {@link BalanceamentoLancamentoDTO}
	 */
	BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro, boolean configuracaoInicial);
	
	/**
	 * Confirma as matriz de balanceamento de lançamento.
	 * 
	 * @param matrizLancamento - matriz de balanceamento de lançamento
	 * @param datasConfirmadas - datas para confirmação
	 * @param usuario - usuário
	 * 
	 * @return {@link List<ProdutoLancamentoDTO>}
	 */
	List<ProdutoLancamentoDTO> confirmarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
								   						 List<Date> datasConfirmadas, Usuario usuario);
	
	
	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores);

	List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodo(Date dataInicial,
			List<Long> fornecedores);
	
}
