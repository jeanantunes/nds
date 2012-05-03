package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Interface que define serviços referentes ao recolhimento.
 * 
 * @author Discover Technology
 *
 */
public interface RecolhimentoService {
	
	/**
	 * Obtém os dados do resumo do período do balanceamento.
	 * 
	 * @param dataInicial - data inicial do período
	 * @param listaIdsFornecedores - lista com os id's dos fornecedores
	 * 
	 * @return {@link ResumoPeriodoBalanceamentoDTO}
	 */
	List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodoBalanceamento(Date dataInicial, List<Long> listaIdsFornecedores);
	
	/**
	 * Obtém os dados do balanceamento de recolhimento.
	 * 
	 * @param dataInicial - data inicial do período
	 * 
	 * @return {@link RecolhimentoDTO}
	 */
	List<RecolhimentoDTO> obterDadosBalanceamentoRecolhimento(Date dataInicial);
	
	BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
														  List<Long> listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento);

}
