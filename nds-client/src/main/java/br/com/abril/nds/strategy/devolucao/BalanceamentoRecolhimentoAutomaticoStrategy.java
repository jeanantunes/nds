package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
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
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = new BalanceamentoRecolhimentoDTO();
		
		if (!validarDadosRecolhimento(dadosRecolhimento)) {
			
			return balanceamentoRecolhimento; 
		}
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = this.obterMatrizRecolhimento(dadosRecolhimento);
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * Obtém a matriz de recolhimento
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> obterMatrizRecolhimento(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();

		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaBalanceado = 
			this.balancearMapaExpectativaEncalheTotalDiaria(dadosRecolhimento);
		
		// Verificar se as quantidades excedem a capacidade diária de manuseio
		
		// Separar quantidade de produtos-edição para não quebrar entre os dias
		
		// Verificar qual quantidade de produto melhor se encaixa em cada dia com capacidade disponível
		// (ordenar pela menor quantidade mas verificar qual melhor completa o total da capacidade)
		
		// Gerenciar sobras (jogar nos dias que não excedem a capacidade de manuseio)
		
		// Quando todas capacidades estiverem excedidas dar preferência para maior PEB 
		// (verificar dia de recolhimento previsto do produto)
		
		return matrizRecolhimento;
	}
	
	/*
	 * Efetua o balanceamento da expectativa de encalhe dos dias fora das datas de recolhimento para as datas possíveis.
	 */
	private Map<Date, BigDecimal> balancearMapaExpectativaEncalheTotalDiaria(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaBalanceado = new TreeMap<Date, BigDecimal>();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		Set<Date> datasRecolhimentoFornecedor = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiaria.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheTotalPrevista = entryExpectativaEncalheTotalDiaria.getValue();
			
			Date dataBalanceamento = 
				this.obterDataRecolhimentoPermitida(datasRecolhimentoFornecedor, dataRecolhimentoPrevista);
			
			BigDecimal expectativaEncalheTotalDiaria = 
				mapaExpectativaEncalheTotalDiariaBalanceado.get(dataBalanceamento);
			
			expectativaEncalheTotalDiaria = expectativaEncalheTotalDiaria.add(expectativaEncalheTotalPrevista);
			
			mapaExpectativaEncalheTotalDiariaBalanceado.put(dataBalanceamento, expectativaEncalheTotalDiaria);
		}
		
		return mapaExpectativaEncalheTotalDiariaBalanceado;
	}
	
	/*
	 *  Obtém uma data de recolhimento de acordo as datas de recolhimento permitidas, efetuando
	 *  a aproximação de datas se necessário.
	 */
	private Date obterDataRecolhimentoPermitida(Set<Date> datasRecolhimentoPermitidas, Date dataRecolhimentoPrevista) {

		Date dataRecolhimentoPermitida = null;
		
		if (!datasRecolhimentoPermitidas.contains(dataRecolhimentoPrevista)) {
			
			for (Date dataRecolhimentoFornecedor : datasRecolhimentoPermitidas) {
				
				if (dataRecolhimentoPrevista.compareTo(dataRecolhimentoFornecedor) < 0) {
					
					dataRecolhimentoPermitida = dataRecolhimentoFornecedor;
					
					break;
				}
			}
			
			if (dataRecolhimentoPermitida == null) {
				
				throw new RuntimeException(
					"Data de recolhimento fora da semana de recolhimento: " + dataRecolhimentoPrevista);
			}
			
		} else {
			
			dataRecolhimentoPermitida = dataRecolhimentoPrevista;
		}
			
		return dataRecolhimentoPermitida;
	}
	
	/*
	 * Valida os dados de recolhimento.
	 */
	private boolean validarDadosRecolhimento(RecolhimentoDTO dadosRecolhimento) {
		
		return !(dadosRecolhimento == null
					|| dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor() == null
					|| dadosRecolhimento.getDatasRecolhimentoDistribuidor() == null
					|| dadosRecolhimento.getDatasRecolhimentoFornecedor() == null
					|| dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria() == null
					|| dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria().isEmpty()
					|| dadosRecolhimento.getProdutosRecolhimento() == null 
					|| dadosRecolhimento.getProdutosRecolhimento().isEmpty());
	}

}
