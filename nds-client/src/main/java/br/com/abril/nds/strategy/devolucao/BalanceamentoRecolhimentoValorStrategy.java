package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
	protected TreeMap<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = dadosRecolhimento.getProdutosRecolhimento();
		
		BigDecimal valorTotalProdutosRecolhimento = 
			this.obterValorTotalProdutosRecolhimento(produtosRecolhimento);
		
		TreeSet<Date> datasRecolhimento = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		Integer qtdeDiasRecolhimento = datasRecolhimento.size();
		
		BigDecimal mediaValorTotalProdutosRecolhimento = 
			valorTotalProdutosRecolhimento.divide(new BigDecimal(qtdeDiasRecolhimento));
		
		this.processarProdutosRecolhimentoNaoBalanceaveis(matrizRecolhimentoBalanceada, dadosRecolhimento);

		TreeMap<Date, BigDecimal> mapaValorTotalProdutosRecolhimento = new TreeMap<Date, BigDecimal>();
		
		List<ProdutoRecolhimentoDTO> sobraProdutosRecolhimento =
			this.alocarProdutosRecolhimento(matrizRecolhimentoBalanceada, mapaValorTotalProdutosRecolhimento, 
				produtosRecolhimento, datasRecolhimento, mediaValorTotalProdutosRecolhimento);
		
		this.alocarSobraProdutosRecolhimentoPelaMediaValorTotalProduto(
			matrizRecolhimentoBalanceada, mapaValorTotalProdutosRecolhimento, 
				sobraProdutosRecolhimento, mediaValorTotalProdutosRecolhimento);
		
		this.alocarSobraProdutosRecolhimento(
			matrizRecolhimentoBalanceada, mapaValorTotalProdutosRecolhimento, sobraProdutosRecolhimento);
		
		return matrizRecolhimentoBalanceada;
	}
	
	/*
	 * Aloca os produtos de recolhimento na matriz e retorna as sobras.
	 */
	private List<ProdutoRecolhimentoDTO> alocarProdutosRecolhimento(
											TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada,
											TreeMap<Date, BigDecimal> mapaValorTotalProdutosRecolhimento,
											List<ProdutoRecolhimentoDTO> produtosRecolhimento,
											TreeSet<Date> datasRecolhimento,
											BigDecimal mediaValorTotalProdutosRecolhimento) {
		
		List<ProdutoRecolhimentoDTO> sobraProdutosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			Date dataRecolhimento = produtoRecolhimento.getDataRecolhimentoPrevista();
			
			Date dataBalanceamento = 
				super.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimento);
			
			BigDecimal valorTotalProduto = produtoRecolhimento.getValorTotal();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaDataBalanceamento =
				matrizRecolhimentoBalanceada.get(dataBalanceamento);
			
			if (produtosRecolhimentoNaDataBalanceamento == null) {
				
				produtosRecolhimentoNaDataBalanceamento = new ArrayList<ProdutoRecolhimentoDTO>();
			}
			
			BigDecimal valorTotalProdutosNaDataBalanceamento = 
				this.obterValorTotalProdutosRecolhimento(produtosRecolhimentoNaDataBalanceamento);
			
			if (valorTotalProdutosNaDataBalanceamento != null) {
				
				valorTotalProdutosNaDataBalanceamento = valorTotalProdutosNaDataBalanceamento.add(valorTotalProduto);
				
			} else {
				
				valorTotalProdutosNaDataBalanceamento = valorTotalProduto;
			}
			
			if (valorTotalProdutosNaDataBalanceamento.compareTo(mediaValorTotalProdutosRecolhimento) > 0) {
				
				sobraProdutosRecolhimento.add(produtoRecolhimento);
				
			} else {
				
				produtosRecolhimentoNaDataBalanceamento.add(produtoRecolhimento);
				
				mapaValorTotalProdutosRecolhimento.put(dataBalanceamento, valorTotalProdutosNaDataBalanceamento);
				
				matrizRecolhimentoBalanceada.put(dataBalanceamento, produtosRecolhimentoNaDataBalanceamento);
			}
		}
		
		return sobraProdutosRecolhimento;
	}
	
	/*
	 * Aloca as sobras de produtos de recolhimento na matriz utilizando a média
	 * de valor total dos produtos da matriz.
	 */
	private void alocarSobraProdutosRecolhimentoPelaMediaValorTotalProduto(
												 TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada,
												 TreeMap<Date, BigDecimal> mapaValorTotalProdutosRecolhimento,
												 List<ProdutoRecolhimentoDTO> sobraProdutosRecolhimento,
												 BigDecimal mediaValorTotalProdutosRecolhimento) {
		
		if (!sobraProdutosRecolhimento.isEmpty()) {
	
			for (Map.Entry<Date, BigDecimal> entryValorTotalProdutosRecolhimento
					: mapaValorTotalProdutosRecolhimento.entrySet()) {
				
				Date dataRecolhimento = entryValorTotalProdutosRecolhimento.getKey();
				
				BigDecimal valorTotalProdutosNaDataRecolhimento = entryValorTotalProdutosRecolhimento.getValue();
				
				List<ProdutoRecolhimentoDTO> produtosNaDataRecolhimento = 
					matrizRecolhimentoBalanceada.get(dataRecolhimento);
					
				for (int indice = 0; indice < sobraProdutosRecolhimento.size(); indice++) {
					
					ProdutoRecolhimentoDTO sobraProdutoRecolhimento = sobraProdutosRecolhimento.get(indice);
					
					BigDecimal valorTotalSobra = sobraProdutoRecolhimento.getValorTotal();
					
					valorTotalProdutosNaDataRecolhimento = 
						valorTotalProdutosNaDataRecolhimento.add(valorTotalSobra);
					
					if (valorTotalProdutosNaDataRecolhimento.compareTo(mediaValorTotalProdutosRecolhimento) <= 0) {
						
						produtosNaDataRecolhimento.add(sobraProdutosRecolhimento.remove(indice--));
						
					} else {
						
						break;
					}
				}

				matrizRecolhimentoBalanceada.put(dataRecolhimento, produtosNaDataRecolhimento);
			}
		}
	}
	
	/*
	 * Aloca as sobras de produtos de recolhimento na matriz ignorando a média
	 * de valor total dos produtos da matriz.
	 */
	private void alocarSobraProdutosRecolhimento(
											TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada,
											TreeMap<Date, BigDecimal> mapaValorTotalProdutosRecolhimento,
											List<ProdutoRecolhimentoDTO> sobraProdutosRecolhimento) {
		
		if (!sobraProdutosRecolhimento.isEmpty()) {
			
			TreeMap<Date, BigDecimal> mapaValorTotalProdutosRecolhimentoOrdenado = mapaValorTotalProdutosRecolhimento;

			mapaValorTotalProdutosRecolhimentoOrdenado =
				new TreeMap<Date, BigDecimal>(Collections.reverseOrder());
			 
			 mapaValorTotalProdutosRecolhimentoOrdenado.putAll(mapaValorTotalProdutosRecolhimento);
			 
			 Map.Entry<Date, BigDecimal> entryValorTotalProdutosRecolhimento =
				mapaValorTotalProdutosRecolhimentoOrdenado.firstEntry();
			 
			 Date dataRecolhimento = entryValorTotalProdutosRecolhimento.getKey();
			 
			 List<ProdutoRecolhimentoDTO> produtosNaDataRecolhimento = 
				matrizRecolhimentoBalanceada.get(dataRecolhimento);
			 
			 produtosNaDataRecolhimento.addAll(sobraProdutosRecolhimento);
			 
			 matrizRecolhimentoBalanceada.put(dataRecolhimento, produtosNaDataRecolhimento);
		}
	}
	
	/*
	 * Processa os produtos de recolhimento não balancéaveis.
	 */
	private void processarProdutosRecolhimentoNaoBalanceaveis(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
															  RecolhimentoDTO dadosRecolhimento) {
		
		for (Map.Entry<Date, BigDecimal> entryExpectativaEncalheTotalDiaria : 
				dadosRecolhimento.getMapaExpectativaEncalheTotalDiaria().entrySet()) {
			
			Date dataRecolhimentoPrevista = entryExpectativaEncalheTotalDiaria.getKey();
			
			super.processarProdutosRecolhimentoNaoBalanceaveis(
				matrizRecolhimento, dadosRecolhimento, dataRecolhimentoPrevista);
		}
	}
	
	/*
	 * Obtém o valor total dos produtos para recolhimento.
	 */
	private BigDecimal obterValorTotalProdutosRecolhimento(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		BigDecimal valorTotalProdutosRecolhimento = BigDecimal.ZERO;
			
		if (produtosRecolhimento != null) {
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (produtoRecolhimento.getValorTotal() != null) {
				
					valorTotalProdutosRecolhimento = 
						valorTotalProdutosRecolhimento.add(produtoRecolhimento.getValorTotal());
				}
			}
		}
		
		return valorTotalProdutosRecolhimento;
	}

}
