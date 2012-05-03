package br.com.abril.nds.factory.devolucao;

import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Fábrica de estratégias de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoFactory {

	/**
	 * Construtor privado.
	 */
	private BalanceamentoRecolhimentoFactory() {
		
	}
	
	/**
	 * Obtém a estratégia de balanceamento de recolhimento de acordo com o tipo.
	 * 
	 * @param tipoBalanceamentoRecolhimento - tipo de balanceamento de recolhimento
	 * 
	 * @return {@link BalanceamentoRecolhimentoStrategy}
	 */
	public static BalanceamentoRecolhimentoStrategy getStrategy(TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento) {
		
		return null;
	}
	
}
