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
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = null;
		
		if (!dadosRecolhimento.isSemanaRecolhimento()) {
			
			matrizRecolhimento = this.gerarMatrizRecolhimentoBalanceada(dadosRecolhimento);
			
			this.configurarMatrizRecolhimento(matrizRecolhimento);
			
		} else {
			
			matrizRecolhimento = this.carregarMatrizRecolhimentoSalva(dadosRecolhimento);
		}
		
		balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
		
		balanceamentoRecolhimento.setCapacidadeRecolhimentoDistribuidor(
			dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		balanceamentoRecolhimento.setSemanaRecolhimento(dadosRecolhimento.isSemanaRecolhimento());
		
		return balanceamentoRecolhimento;
	}
	
	/**
	 * Gera a matriz de recolhimento balanceada de acordo com a estratégia escolhida.
	 * 
	 * @param dadosRecolhimento - dados do recolhimento
	 * 
	 * @return Matriz de recohlimento balanceada
	 */
	protected abstract TreeMap<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento);
	
	/*
	 * Balanceia os produtos do recolhimento que possuem chamada (antecipada ou chamadão) na matriz.
	 */
	protected void processarProdutosRecolhimentoNaoBalanceaveis(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
														   		RecolhimentoDTO dadosRecolhimento,
														   		Date dataRecolhimentoPrevista) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceaveis = 
			this.obterProdutosRecolhimentoNaoBalanceaveisPorData(dadosRecolhimento, dataRecolhimentoPrevista);
		
		if (!produtosRecolhimentoNaoBalanceaveis.isEmpty()) {
			
			matrizRecolhimento.put(dataRecolhimentoPrevista, produtosRecolhimentoNaoBalanceaveis);
		}
	}
	
	/*
	 * Obtém os produtos de recolhimento não balanceáveis (possuem chamada antecipada ou chamadão) de uma determinada data.
	 */
	protected List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoNaoBalanceaveisPorData(RecolhimentoDTO dadosRecolhimento, 
																		  				   Date dataRecolhimentoDesejada) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceaveis = new ArrayList<ProdutoRecolhimentoDTO>();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = dadosRecolhimento.getProdutosRecolhimento();
		
		if (produtosRecolhimento == null 
				|| produtosRecolhimento.isEmpty()
				|| dataRecolhimentoDesejada == null) {
			
			return produtosRecolhimentoNaoBalanceaveis;
		}
		
		for (int indice = 0; indice < produtosRecolhimento.size(); indice++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = produtosRecolhimento.get(indice);
			
			if (produtoRecolhimento.getDataRecolhimentoDistribuidor().equals(dataRecolhimentoDesejada)) {
				
				if (this.isProdutoNaoBalanceavel(dadosRecolhimento.isConfiguracaoInicial(), produtoRecolhimento)) {
					
					produtosRecolhimentoNaoBalanceaveis.add(produtosRecolhimento.remove(indice--));
				}
			}
		}
		
		return produtosRecolhimentoNaoBalanceaveis;
	}

	/**
	 * Verifica se o produto de recolhimento pode ser balanceado ou não.
	 */
	private boolean isProdutoNaoBalanceavel(boolean isConfiguracaoInicial,
										 	ProdutoRecolhimentoDTO produtoRecolhimento) {
		
		return produtoRecolhimento.isPossuiChamada()
				|| produtoRecolhimento.isBalanceamentoConfirmado()
				|| (produtoRecolhimento.isBalanceamentoSalvo() && !isConfiguracaoInicial);
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
	protected List<ProdutoRecolhimentoDTO> obterProdutosRecolhimentoBalanceaveisPorData(RecolhimentoDTO dadosRecolhimento, 
																		  			    Date dataRecolhimentoDesejada) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoFiltrados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = dadosRecolhimento.getProdutosRecolhimento();
		
		if (produtosRecolhimento == null 
				|| produtosRecolhimento.isEmpty()
				|| dataRecolhimentoDesejada == null) {
			
			return produtosRecolhimentoFiltrados;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if (this.isProdutoNaoBalanceavel(dadosRecolhimento.isConfiguracaoInicial(), produtoRecolhimento)) {
				
				continue;
			}
			
			if (produtoRecolhimento.getDataRecolhimentoPrevista().equals(dataRecolhimentoDesejada)) {
				
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
		
		if (produtosRecolhimentoNovos == null || produtosRecolhimentoNovos.isEmpty()) {
			
			return;
		}
		
		if (produtosRecolhimentoAtuais != null) {
			
			produtosRecolhimentoAtuais.addAll(produtosRecolhimentoNovos);
			
		} else {
			
			produtosRecolhimentoAtuais = produtosRecolhimentoNovos;
		}
		
		if (produtosRecolhimentoAtuais != null && !produtosRecolhimentoAtuais.isEmpty()) {
		
			matrizRecolhimento.put(dataBalanceamento, produtosRecolhimentoAtuais);
		}
	}
	
	/*
	 * Carrega a matriz de recolhimento salva.
	 */
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>> carregarMatrizRecolhimentoSalva(
																		RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = 
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = dadosRecolhimento.getProdutosRecolhimento();
		
		if (produtosRecolhimento == null
				|| produtosRecolhimento.isEmpty()) {
			
			return matrizRecolhimento;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			Date dataRecolhimento = produtoRecolhimento.getDataRecolhimentoDistribuidor();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceados = 
					matrizRecolhimento.get(dataRecolhimento);
			
			if (produtosRecolhimentoBalanceados == null) {
				
				produtosRecolhimentoBalanceados = new ArrayList<ProdutoRecolhimentoDTO>();
			}
			
			produtosRecolhimentoBalanceados.add(produtoRecolhimento);
			
			matrizRecolhimento.put(dataRecolhimento, produtosRecolhimentoBalanceados);
		}
		
		return matrizRecolhimento;
	}
	
	/*
	 * Configura a sequência, a data de recolhimento e a nova data dos produtos da matriz.
	 */
	private void configurarMatrizRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Integer sequencia = 1;
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entryMatrizRecolhimento 
				: matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entryMatrizRecolhimento.getKey();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entryMatrizRecolhimento.getValue();
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
				produtoRecolhimento.setNovaData(dataRecolhimento);
				produtoRecolhimento.setSequencia(sequencia++);
			}
		}
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
