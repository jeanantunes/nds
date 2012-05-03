package br.com.abril.nds.strategy.devolucao;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Interface de estratégia de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public interface BalanceamentoRecolhimentoStrategy {
	
	/**
	 * Efetua o balanceamento de recolhimento.
	 * 
	 * @param dadosRecolhimento - dados de recolhimento
	 * 
	 * @return {@link BalanceamentoRecolhimentoDTO}
	 */
	BalanceamentoRecolhimentoDTO balancear(RecolhimentoDTO dadosRecolhimento);

}
