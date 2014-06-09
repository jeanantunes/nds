package br.com.abril.nds.factory.devolucao;

import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoAutomaticoStrategy;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoEditorStrategy;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoValorStrategy;
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
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = null;

		switch (tipoBalanceamentoRecolhimento) {
		
			case AUTOMATICO:
				
				balanceamentoRecolhimentoStrategy = new BalanceamentoRecolhimentoAutomaticoStrategy();
				
				break;
				
			case EDITOR:
				
				balanceamentoRecolhimentoStrategy = new BalanceamentoRecolhimentoEditorStrategy();
				
				break;
	
			case VALOR:
				
				balanceamentoRecolhimentoStrategy = new BalanceamentoRecolhimentoValorStrategy();
				
				break;
				
			default:
				
				throw new UnsupportedOperationException("Tipo de balanceamento não suportado!");
		}
		
		return balanceamentoRecolhimentoStrategy;
	}
	
}
