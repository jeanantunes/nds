package br.com.abril.nds.strategy.devolucao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Estratégia de balanceamento de recolhimento por Valor Total de Produtos.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoValorStrategy extends AbstractBalanceamentoRecolhimentoStrategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		
		
		return matrizRecolhimentoBalanceada;
	}	

}
