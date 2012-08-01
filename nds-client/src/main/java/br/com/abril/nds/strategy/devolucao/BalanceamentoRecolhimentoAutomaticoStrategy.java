package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;

/**
 * Estratégia de balanceamento de recolhimento automático.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoAutomaticoStrategy extends AbstractBalanceamentoRecolhimentoStrategy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TreeMap<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		List<ProdutoRecolhimentoDTO> todosProdutosRecolhimentoNaoBalanceados = new ArrayList<ProdutoRecolhimentoDTO>();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = 
			dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria();
		
		TreeSet<Date> datasRecolhimento = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaOrdenado =
			super.ordenarMapaExpectativaEncalhePorDatasRecolhimento(
				mapaExpectativaEncalheTotalDiaria, datasRecolhimento);
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				mapaExpectativaEncalheTotalDiariaOrdenado.entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			BigDecimal expectativaEncalheABalancear = entryExpectativaEncalheTotalDiaria.getValue();

			super.processarProdutosRecolhimentoNaoBalanceaveis(
				matrizRecolhimentoBalanceada, dadosRecolhimento, dataRecolhimentoPrevista);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados =
				this.balancearProdutosRecolhimento(matrizRecolhimentoBalanceada, dataRecolhimentoPrevista, 
					datasRecolhimento, dadosRecolhimento, expectativaEncalheABalancear);
			
			if (produtosRecolhimentoNaoBalanceados != null 
					&& ! produtosRecolhimentoNaoBalanceados.isEmpty()) {
				
				todosProdutosRecolhimentoNaoBalanceados.addAll(produtosRecolhimentoNaoBalanceados);
			}
		}
		
		this.gerenciarProdutosRecolhimentoNaoBalanceados(
			matrizRecolhimentoBalanceada, todosProdutosRecolhimentoNaoBalanceados, dadosRecolhimento);
		
		return matrizRecolhimentoBalanceada;
	}
	
	/*
	 * Aloca os produtos na matriz de recolhimento.
	 */
	@SuppressWarnings("unchecked")
	private List<ProdutoRecolhimentoDTO> alocarProdutosMatrizRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
																		    BigDecimal capacidadeManuseio,
																		    List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis,
																		    List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData,
																		    BigDecimal expectativaEncalheTotalAtualNaData,
																		    Date dataBalanceamento,
																		    boolean permiteExcederCapacidadeManuseio) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("dataRecolhimentoDistribuidor"), true);
		comparatorChain.addComparator(new BeanComparator("expectativaEncalhe"));
		
		Collections.sort(produtosRecolhimentoBalanceaveis, comparatorChain);
		
		BigDecimal expectativaEncalheASerPreenchida = 
			capacidadeManuseio.subtract(expectativaEncalheTotalAtualNaData);
		
		BigDecimal expectativaEncalheElegivel = BigDecimal.ZERO;
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoElegiveis = 
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		for (int indice = 0; indice < produtosRecolhimentoBalanceaveis.size(); indice++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimentoBalanceavel = produtosRecolhimentoBalanceaveis.get(indice);
			
			expectativaEncalheElegivel = 
				expectativaEncalheElegivel.add(produtoRecolhimentoBalanceavel.getExpectativaEncalhe());
			
			if (!permiteExcederCapacidadeManuseio
					&& (expectativaEncalheElegivel.doubleValue() > expectativaEncalheASerPreenchida.doubleValue())) {
				
				break;
			}
			
			produtosRecolhimentoElegiveis.add(produtoRecolhimentoBalanceavel);
			
			produtosRecolhimentoBalanceaveis.remove(indice--);
		}
		
		super.atualizarMatrizRecolhimento(
			matrizRecolhimento, produtosRecolhimentoNaData, produtosRecolhimentoElegiveis, dataBalanceamento);
		
		return produtosRecolhimentoBalanceaveis;
	}
	
	/*
	 * Efetua o balanceamento de produtos de recolhimento.
	 */
	private List<ProdutoRecolhimentoDTO> balancearProdutosRecolhimento(
												Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada,
												Date dataRecolhimentoPrevista, TreeSet<Date> datasRecolhimento,
												RecolhimentoDTO dadosRecolhimento,
												BigDecimal expectativaEncalheABalancear) {

		Date dataBalanceamento = super.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimentoPrevista);

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados = null;

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = matrizRecolhimentoBalanceada.get(dataBalanceamento);

		BigDecimal capacidadeManuseio = dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor();

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoBalanceaveis = 
			super.obterProdutosRecolhimentoBalanceaveisPorData(
				dadosRecolhimento, dataRecolhimentoPrevista);

		BigDecimal expectativaEncalheTotalAtualNaData = super.obterExpectativaEncalheTotal(produtosRecolhimentoNaData);

		BigDecimal excessoExpectativaEncalhe = 
			super.calcularExcessoExpectativaEncalhe(
				expectativaEncalheTotalAtualNaData, capacidadeManuseio, expectativaEncalheABalancear);

		if (super.validarLimiteCapacidadeRecolhimentoDistribuidor(excessoExpectativaEncalhe)) {

			super.atualizarMatrizRecolhimento(
				matrizRecolhimentoBalanceada, produtosRecolhimentoNaData,
				produtosRecolhimentoBalanceaveis, dataBalanceamento);

		} else {

			produtosRecolhimentoNaoBalanceados = 
				this.alocarProdutosMatrizRecolhimento(
					matrizRecolhimentoBalanceada, capacidadeManuseio, produtosRecolhimentoBalanceaveis,
						produtosRecolhimentoNaData, expectativaEncalheTotalAtualNaData, dataBalanceamento, false);
		}

		return produtosRecolhimentoNaoBalanceados;
	}
	
	/*
	 * Gerencia os produtos do recolhimento que não foram balanceados por excederem a capacidade de manuseio.
	 */
	private void gerenciarProdutosRecolhimentoNaoBalanceados(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
															 List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados,
															 RecolhimentoDTO dadosRecolhimento) {
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaAtual = 
			this.gerarMapaExpectativaEncalheTotalDiariaOrdenadoPelaMaiorData(matrizRecolhimento);
		
		this.alocarSobrasMatrizRecolhimento(
			matrizRecolhimento, mapaExpectativaEncalheTotalDiariaAtual, 
				produtosRecolhimentoNaoBalanceados, dadosRecolhimento, false);
		
		mapaExpectativaEncalheTotalDiariaAtual = 
			this.gerarMapaExpectativaEncalheTotalDiariaOrdenadoPelaMaiorData(matrizRecolhimento);
		
		this.alocarSobrasMatrizRecolhimento(
			matrizRecolhimento, mapaExpectativaEncalheTotalDiariaAtual, 
				produtosRecolhimentoNaoBalanceados, dadosRecolhimento, true);
	}
	
	/*
	 * Faz a alocação dos produtos de recolhimento em sobra no balanceamento (excedentes da capacidade de
	 * manuseio do distribuidor nos dias previstos).
	 */
	private List<ProdutoRecolhimentoDTO> alocarSobrasMatrizRecolhimento(
											    Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiariaAtual,
											    List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados,
											    RecolhimentoDTO dadosRecolhimento,
											    boolean permiteExcederCapacidadeManuseioDistribuidor) {
		
		if (produtosRecolhimentoNaoBalanceados.isEmpty()) {
			
			return produtosRecolhimentoNaoBalanceados;
		}
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiariaAtual :
				mapaExpectativaEncalheTotalDiariaAtual.entrySet()) {
			
			if (produtosRecolhimentoNaoBalanceados.isEmpty()) {
				
				break;
			}
			
			Date dataBalanceamento = entryExpectativaEncalheTotalDiariaAtual.getKey();
			
			if (!dadosRecolhimento.getDatasRecolhimentoFornecedor().contains(dataBalanceamento)) {
				
				continue;
			}
			
			BigDecimal expectativaEncalheTotalAtualNaData = entryExpectativaEncalheTotalDiariaAtual.getValue();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaData = matrizRecolhimento.get(dataBalanceamento);
			
			produtosRecolhimentoNaoBalanceados =
				this.alocarProdutosMatrizRecolhimento(
					matrizRecolhimento, dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor(),
						produtosRecolhimentoNaoBalanceados, produtosRecolhimentoNaData, 
							expectativaEncalheTotalAtualNaData, dataBalanceamento, 
								permiteExcederCapacidadeManuseioDistribuidor);
		}
		
		return produtosRecolhimentoNaoBalanceados;
	}

}
