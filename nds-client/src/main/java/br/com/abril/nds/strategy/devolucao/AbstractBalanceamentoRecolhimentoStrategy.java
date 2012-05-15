package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Classe abstrata para estratégias de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public abstract class AbstractBalanceamentoRecolhimentoStrategy implements BalanceamentoRecolhimentoStrategy {
	
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
	
	/**
	 * Gera a matriz de recolhimento balanceada de acordo com a estratégia escolhida.
	 * 
	 * @param dadosRecolhimento - dados do recolhimento
	 * 
	 * @return Matriz de recohlimento balanceada
	 */
	protected abstract Map<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento);
	
	/*
	 * Balanceia os produtos do recolhimento que possuem chamada (antecipada ou chamadão) na matriz.
	 */
	protected void balancearProdutosRecolhimentoComChamada(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
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
	 * Obtém os produtos de recolhimento não balanceáveis (possuem chamada antecipada ou chamadão) de uma determinada data.
	 */
	protected List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoNaoBalanceaveisPorData(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
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
	 * Efetua a ordenação do mapa de expectativa de encalhe de acordo com as datas
	 * de recolhimento passadas como parâmetro.
	 */
	protected Map<Date, BigDecimal> ordenarMapaExpectativaEncalhePorDatasRecolhimento(Map<Date, BigDecimal> mapaExpectativaEncalhe, 
											   					   					  TreeSet<Date> datasRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheOrdenado = new LinkedHashMap<Date, BigDecimal>();
		
		for (Date dataRecolhimento : datasRecolhimento) {

			BigDecimal expectativaEncalhe = mapaExpectativaEncalhe.get(dataRecolhimento);
			
			if (expectativaEncalhe != null) {
				
				mapaExpectativaEncalheOrdenado.put(
					dataRecolhimento, mapaExpectativaEncalheOrdenado.remove(dataRecolhimento));
			}
		}
		
		mapaExpectativaEncalheOrdenado.putAll(mapaExpectativaEncalhe);
		
		return mapaExpectativaEncalheOrdenado;
	}
	
	/*
	 * Gera o mapa de expectativa de encalhe total diária ordenado pela maior data
	 * de acordo com a matriz de recolhimento.
	 */
	protected Map<Date, BigDecimal> gerarMapaExpectativaEncalheTotalDiariaOrdenadoPelaMaiorData(
														Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = 
			new TreeMap<Date, BigDecimal>(Collections.reverseOrder());

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
	protected Date obterDataRecolhimentoPermitida(TreeSet<Date> datasRecolhimentoPermitidas, Date dataRecolhimentoPrevista) {

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
	 * Obtém os produtos de recolhimento balanceáveis (não possuem chamadão ou chamada antecipada) de uma determinada data.
	 */
	protected List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoBalanceaveisPorData(List<ProdutoRecolhimentoDTO> produtosRecolhimento, 
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
	 * Obtém a expectativa de encalhe total dos produtos para recolhimento.
	 */
	protected BigDecimal obterExpectativaEncalheTotal(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		BigDecimal expectativaEncalheTotal = BigDecimal.ZERO;
		
		if (produtosRecolhimento != null) {
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (produtoRecolhimento.getExpectativaEncalhe() != null) {
				
					expectativaEncalheTotal = 
						expectativaEncalheTotal.add(produtoRecolhimento.getExpectativaEncalhe());
				}
			}
		}
		
		return expectativaEncalheTotal;
	}
	
	/*
	 * Valida se a capacidade de manuseio do distribuidor é excedida com o encalhe a ser balanceado.
	 */
	protected boolean validarLimiteCapacidadeRecolhimentoDistribuidor(BigDecimal excessoExpectativaEncalhe) {
		
		return excessoExpectativaEncalhe.equals(BigDecimal.ZERO);
	}
	
	/*
	 * Calcula o excesso de expectativa de encalhe na data de acordo com a capacidade de manuseio diária.
	 */
	protected BigDecimal calcularExcessoExpectativaEncalhe(BigDecimal expectativaEncalheTotalAtualNaData, 
	  													   BigDecimal capacidadeManuseio,
	  													   BigDecimal expectativaEncalheABalancear) {
		
		BigDecimal excessoExpectativaEncalhe = BigDecimal.ZERO;
		
		if (expectativaEncalheTotalAtualNaData != null) {

			expectativaEncalheABalancear = expectativaEncalheTotalAtualNaData.add(expectativaEncalheABalancear);
		}
		
		if (expectativaEncalheABalancear.compareTo(capacidadeManuseio) > 0) {
			
			excessoExpectativaEncalhe = expectativaEncalheABalancear.subtract(capacidadeManuseio);
		}
		
		return excessoExpectativaEncalhe;
	}
	
	/*
	 * Atualiza a matriz de recolhimento com novos dados na data desejada.
	 */
	protected void atualizarMatrizRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											   List<ProdutoRecolhimentoDTO> produtosRecolhimentoAtuais,
											   List<ProdutoRecolhimentoDTO> produtosRecolhimentoNovos,
											   Date dataBalanceamento) {
		
		if (produtosRecolhimentoAtuais != null) {
			
			produtosRecolhimentoAtuais.addAll(produtosRecolhimentoNovos);
			
		} else {
			
			produtosRecolhimentoAtuais = produtosRecolhimentoNovos;
		}
		
		matrizRecolhimento.put(dataBalanceamento, produtosRecolhimentoAtuais);
	}
	
	/*
	 * Valida os dados de recolhimento.
	 */
	protected boolean validarDadosRecolhimento(RecolhimentoDTO dadosRecolhimento) {
		
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
