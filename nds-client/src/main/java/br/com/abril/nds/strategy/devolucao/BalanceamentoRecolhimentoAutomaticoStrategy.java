package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = 
			this.gerarMatrizRecolhimentoBalanceada(dadosRecolhimento);
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		balanceamentoRecolhimento.setCapacidadeRecolhimentoDistribuidor(
			dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		return balanceamentoRecolhimento;
	}
	
	/*
	 * Gera a matriz de recolhimento balanceada nas datas determinadas.
	 */
	private Map<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		TreeSet<Date> datasRecolhimentoFornecedor = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiaria.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheABalancear = entryExpectativaEncalheTotalDiaria.getValue();

			this.balancearProdutosRecolhimentoComChamada(
				matrizRecolhimentoBalanceada, dadosRecolhimento, dataRecolhimentoPrevista);

			this.balancearProdutosRecolhimento(matrizRecolhimentoBalanceada, dataRecolhimentoPrevista, 
				datasRecolhimentoFornecedor, dadosRecolhimento, expectativaEncalheABalancear);
		}
		
		return matrizRecolhimentoBalanceada;
	}
	
	/*
	 * Balanceia os produtos do recolhimento na matriz.
	 */
	private void balancearProdutosRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada, 
						   					   Date dataRecolhimentoPrevista,
						   					   TreeSet<Date> datasRecolhimento,
											   RecolhimentoDTO dadosRecolhimento,
											   BigDecimal expectativaEncalheABalancear) {
		
		Date dataBalanceamento = 
			this.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimentoPrevista);
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = matrizRecolhimentoBalanceada.get(dataBalanceamento);
		
		BigDecimal capacidadeManuseio = dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis = 
			this.obterProdutosRecolhimentoBalanceaveisPorData(
				dadosRecolhimento.getProdutosRecolhimento(), dataRecolhimentoPrevista);
		
		if (!validarLimiteCapacidadeRecolhimentoDistribuidor(
				produtosRecolhimentoNaData, capacidadeManuseio, expectativaEncalheABalancear)) {
			
			// Separar quantidade de produtos-edição para não quebrar entre os dias
			
			// Verificar qual quantidade de produto melhor se encaixa em cada dia com capacidade disponível
			// (ordenar pela menor quantidade mas verificar qual melhor completa o total da capacidade)
			// OBS: Verificar se há chamada (chamadão ou antecipada) e não pode mover
			
			// Gerenciar sobras (jogar nos dias que não excedem a capacidade de manuseio)
			
			// Quando todas capacidades estiverem excedidas dar preferência para maior PEB 
			// (verificar dia de recolhimento previsto do produto)
			
			Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaAtual = 
				this.gerarMapaExpectativaEncalheTotalDiaria(matrizRecolhimentoBalanceada);
			
			for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiariaAtual :
					mapaExpectativaEncalheTotalDiariaAtual.entrySet()) {
				
				
			}
						
		} else {
			
			if (produtosRecolhimentoNaData != null) {
				
				produtosRecolhimentoNaData.addAll(produtosRecolhimentoBalanceaveis);
				
			} else {
				
				produtosRecolhimentoNaData = produtosRecolhimentoBalanceaveis;
			}
			
			matrizRecolhimentoBalanceada.put(dataBalanceamento, produtosRecolhimentoNaData);
		}
	}
	
	/*
	 * Balanceia os produtos do recolhimento que possuem chamada (antecipada ou chamadão) na matriz.
	 */
	private void balancearProdutosRecolhimentoComChamada(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
														 RecolhimentoDTO dadosRecolhimento,
														 Date dataRecolhimentoPrevista) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceaveis = 
			this.obterProdutosRecolhimentoNaoBalanceaveisPorData(
				dadosRecolhimento.getProdutosRecolhimento(), dataRecolhimentoPrevista);
		
		if (!produtosRecolhimentoNaoBalanceaveis.isEmpty()) {
			
			matrizRecolhimento.put(dataRecolhimentoPrevista, produtosRecolhimentoNaoBalanceaveis);
		}
	}
	
	/*
	 * Valida se a capacidade de manuseio do distribuidor é excedida com o encalhe a ser balanceado.
	 */
	private boolean validarLimiteCapacidadeRecolhimentoDistribuidor(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
															  		BigDecimal capacidadeManuseio,
															  		BigDecimal expectativaEncalheABalancear) {
		
		if (produtosRecolhimento != null) {

			BigDecimal expectativaEncalheTotalAtualNaData = this.obterExpectativaEncalheTotal(produtosRecolhimento);
			
			return (expectativaEncalheTotalAtualNaData.add(expectativaEncalheABalancear).compareTo(capacidadeManuseio) <= 0);
		}
		
		return true;
	}
	
	/*
	 * Obtém a expectativa de encalhe total dos produtos para recolhimento.
	 */
	private BigDecimal obterExpectativaEncalheTotal(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		BigDecimal expectativaEncalheTotal = BigDecimal.ZERO;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if (produtoRecolhimento.getExpectativaEncalhe() != null) {
			
				expectativaEncalheTotal = 
					expectativaEncalheTotal.add(produtoRecolhimento.getExpectativaEncalhe());
			}
		}
		
		return expectativaEncalheTotal;
	}
	
	/*
	 * Obtém os produtos de recolhimento não balanceáveis (possuem chamada antecipada ou chamadão) de uma determinada data.
	 */
	private List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoNaoBalanceaveisPorData(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
																		  				 Date dataRecolhimentoDesejada) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoFiltrados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		if (produtosRecolhimento == null 
				|| produtosRecolhimento.isEmpty()
				|| dataRecolhimentoDesejada == null) {
			
			return produtosRecolhimentoFiltrados;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {

			if (produtoRecolhimento.isPossuiChamada() 
					&& produtoRecolhimento.getDataRecolhimentoDistribuidor().equals(dataRecolhimentoDesejada)) {
				
				produtosRecolhimentoFiltrados.add(produtoRecolhimento);
			}
		}
		
		return produtosRecolhimentoFiltrados;
	}
	
	/*
	 * Obtém os produtos de recolhimento balanceáveis (não possuem chamadão ou chamada antecipada) de uma determinada data.
	 */
	private List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoBalanceaveisPorData(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
																		  			  Date dataRecolhimentoDesejada) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoFiltrados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		if (produtosRecolhimento == null 
				|| produtosRecolhimento.isEmpty()
				|| dataRecolhimentoDesejada == null) {
			
			return produtosRecolhimentoFiltrados;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if (produtoRecolhimento.isPossuiChamada()) {
				
				continue;
			}
			
			if (produtoRecolhimento.getDataRecolhimentoDistribuidor().equals(dataRecolhimentoDesejada)) {
				
				produtosRecolhimentoFiltrados.add(produtoRecolhimento);
			}
		}
		
		return produtosRecolhimentoFiltrados;
	}
	
	/*
	 * Gera o mapa de expectativa de encalhe total diária de acordo com a matriz de recolhimento.
	 */
	private Map<Date, BigDecimal> gerarMapaExpectativaEncalheTotalDiaria(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = new TreeMap<Date, BigDecimal>();
		
		if (matrizRecolhimento == null || matrizRecolhimento.isEmpty()) {
			
			return mapaExpectativaEncalheTotalDiaria;
		}

		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entryMatrizRecolhimento : matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entryMatrizRecolhimento.getKey();
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entryMatrizRecolhimento.getValue();
			
			BigDecimal expectativaEncalheTotalDiaria = BigDecimal.ZERO;
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (produtoRecolhimento.getExpectativaEncalhe() != null) {
				
					expectativaEncalheTotalDiaria = 
						expectativaEncalheTotalDiaria.add(produtoRecolhimento.getExpectativaEncalhe());
				}
			}
			
			mapaExpectativaEncalheTotalDiaria.put(dataRecolhimento, expectativaEncalheTotalDiaria);
		}
		
		return mapaExpectativaEncalheTotalDiaria;
	}
	
	/*
	 *  Obtém uma data de recolhimento de acordo as datas de recolhimento permitidas, efetuando
	 *  a aproximação de datas se necessário.
	 */
	private Date obterDataRecolhimentoPermitida(TreeSet<Date> datasRecolhimentoPermitidas, Date dataRecolhimentoPrevista) {

		Date dataRecolhimentoEscolhida = null;
		
		if (!datasRecolhimentoPermitidas.contains(dataRecolhimentoPrevista)) {

			for (Date dataRecolhimentoPermitida : datasRecolhimentoPermitidas) {
				
				if (dataRecolhimentoPrevista.compareTo(dataRecolhimentoPermitida) <= 0) {
					
					dataRecolhimentoEscolhida = dataRecolhimentoPermitida;
					
					break;
				}
			}
			
			if (dataRecolhimentoEscolhida == null) {

				for (Date dataRecolhimentoPermitida : datasRecolhimentoPermitidas.descendingSet()) {
					
					if (dataRecolhimentoPrevista.compareTo(dataRecolhimentoPermitida) >= 0) {
						
						dataRecolhimentoEscolhida = dataRecolhimentoPermitida;
						
						break;
					}
				}
				
				if (dataRecolhimentoEscolhida == null) {
				
					throw new RuntimeException(
						"Data de recolhimento fora da semana de recolhimento: " + dataRecolhimentoPrevista);
				}
			}
		} else {
			
			dataRecolhimentoEscolhida = dataRecolhimentoPrevista;
		}
			
		return dataRecolhimentoEscolhida;
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
