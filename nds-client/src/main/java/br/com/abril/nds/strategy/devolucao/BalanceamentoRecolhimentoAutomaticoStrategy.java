package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Estratégia de balanceamento de recolhimento automático.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoAutomaticoStrategy implements BalanceamentoRecolhimentoStrategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BalanceamentoRecolhimentoDTO balancear(RecolhimentoDTO dadosRecolhimento) {
		
		// Validações!!!
		
		// Balancear a quantidade dos dias fora das datas de recolhimento para as datas possíveis
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = 
			dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaBalanceamento = new TreeMap<Date, BigDecimal>();
		
		Set<Date> datasRecolhimentoFornecedor = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiaria.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheTotalPrevista = entryExpectativaEncalheTotalDiaria.getValue();
			
			Date dataBalanceamento = null;
			
			// Verificar qual data utilizar (aproximação)
			
			if (!datasRecolhimentoFornecedor.contains(dataRecolhimentoPrevista)) {
				
				for (Date dataRecolhimentoFornecedor : datasRecolhimentoFornecedor) {
					
					if (dataRecolhimentoPrevista.compareTo(dataRecolhimentoFornecedor) < 0) {
						
						dataBalanceamento = dataRecolhimentoFornecedor;
						
						break;
					}
				}
			}
			
			BigDecimal expectativaEncalheTotalDiaria = 
				mapaExpectativaEncalheTotalDiariaBalanceamento.get(dataBalanceamento);
			
			expectativaEncalheTotalDiaria = expectativaEncalheTotalDiaria.add(expectativaEncalheTotalPrevista);
			
			mapaExpectativaEncalheTotalDiariaBalanceamento.put(dataBalanceamento, expectativaEncalheTotalDiaria);
		}
		
		// Verificar se as quantidades excedem a capacidade diária de manuseio
		
		// Separar quantidade de produtos-edição para não quebrar entre os dias
		
		// Verificar qual quantidade de produto melhor se encaixa em cada dia com capacidade disponível
		// (ordenar pela menor quantidade mas verificar qual melhor completa o total da capacidade)
		
		// Gerenciar sobras (jogar nos dias que não excedem a capacidade de manuseio)
		
		// Quando todas capacidades estiverem excedidas dar preferência para maior PEB 
		//(verificar dia de recolhimento previsto do produto)
		
		return null;
	}

}
