package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.util.MapValueComparator;

/**
 * Estratégia de balanceamento de recolhimento por Editor.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoEditorStrategy extends AbstractBalanceamentoRecolhimentoStrategy {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BalanceamentoRecolhimentoDTO gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		TreeSet<Date> datasParaBalanceamento = dadosRecolhimento.getDatasRecolhimentoDisponiveis();
		
		this.processarProdutosRecolhimentoNaoBalanceaveis(
			matrizRecolhimentoBalanceada, dadosRecolhimento);

		List<ProdutoRecolhimentoDTO> produtosRecolhimento = 
			dadosRecolhimento.getProdutosRecolhimento();

		Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor = 
			this.obterMapaProdutosRecolhimentoEditor(produtosRecolhimento);
		
		Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalheEditor =
			this.obterMapaExpectativaEncalhePorQuantidadeEditores(
				mapaProdutosRecolhimentoEditor, datasParaBalanceamento);
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados = 
			this.alocarProdutosMatrizRecolhimento(
				matrizRecolhimentoBalanceada, mapaExpectativaEncalheEditor, 
					mapaProdutosRecolhimentoEditor, datasParaBalanceamento);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			super.gerarBalanceamentoRecolhimentoDTO(
				matrizRecolhimentoBalanceada, produtosRecolhimentoNaoBalanceados, dadosRecolhimento);
		
		return balanceamentoRecolhimento;
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
	 * Obtém o mapa de produtos de recolhimento por editor.
	 */
	private Map<Long, List<ProdutoRecolhimentoDTO>> obterMapaProdutosRecolhimentoEditor(
																List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor = 
			new HashMap<Long, List<ProdutoRecolhimentoDTO>>();

		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			Long idEditor = produtoRecolhimento.getIdEditor();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoEditor = mapaProdutosRecolhimentoEditor.get(idEditor);
			
			if (produtosRecolhimentoEditor == null) {
				
				produtosRecolhimentoEditor = new ArrayList<ProdutoRecolhimentoDTO>();
			}
			
			produtosRecolhimentoEditor.add(produtoRecolhimento);
			
			mapaProdutosRecolhimentoEditor.put(idEditor, produtosRecolhimentoEditor);
		}
		
		return mapaProdutosRecolhimentoEditor;
	}
	
	/*
	 * Obtém o mapa de expectativa de encalhe por editor.
	 */
	private Map<Long, TreeMap<Date, BigDecimal>> obterMapaExpectativaEncalhePorQuantidadeEditores(
							Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor,
							TreeSet<Date> datasParaBalanceamento) {
		
		Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalhePorQuantidadeEditores =
			new HashMap<Long, TreeMap<Date,BigDecimal>>();
		
		int quantidadeEditores = mapaProdutosRecolhimentoEditor.size();
		
		int quantidadeDiasParaBalanceamento = datasParaBalanceamento.size();
		
		int quantidadeEditoresPorDiaBalanceamento;
		
		if (quantidadeEditores < quantidadeDiasParaBalanceamento) {
			
			quantidadeEditoresPorDiaBalanceamento = 1;
			
		} else {
			
			quantidadeEditoresPorDiaBalanceamento = 
				quantidadeEditores / quantidadeDiasParaBalanceamento;
		}
		
		Map<Date, Integer> mapaQuantidadeEditoresPorData = new HashMap<>();
		
		for (Date dataBalanceamento : datasParaBalanceamento) {
			
			for (Map.Entry<Long, List<ProdutoRecolhimentoDTO>> entryProdutosRecolhimentoEditor 
					: mapaProdutosRecolhimentoEditor.entrySet()) {
				
				Long idEditor = entryProdutosRecolhimentoEditor.getKey();
				
				if (mapaExpectativaEncalhePorQuantidadeEditores.containsKey(idEditor)) {
					
					continue;
				}
				
				Integer quantidadeEditoresAlocadosNaData =
					mapaQuantidadeEditoresPorData.get(dataBalanceamento);
				
				if (quantidadeEditoresAlocadosNaData == null) {
					
					quantidadeEditoresAlocadosNaData = 0;
				}
				
				if (quantidadeEditoresAlocadosNaData == quantidadeEditoresPorDiaBalanceamento
						&& !datasParaBalanceamento.last().equals(dataBalanceamento)) {
					
					break;
				}
				
				if (quantidadeEditoresAlocadosNaData < quantidadeEditoresPorDiaBalanceamento
						|| datasParaBalanceamento.last().equals(dataBalanceamento)) {
				
					List<ProdutoRecolhimentoDTO> produtosRecolhimentoEditor = 
						entryProdutosRecolhimentoEditor.getValue();
					
					Map<Date, BigDecimal> mapaExpectativaEncalheDiaria = new HashMap<Date, BigDecimal>();
					
					MapValueComparator<Date, BigDecimal> mapValueComparator =
						new MapValueComparator<Date, BigDecimal>(mapaExpectativaEncalheDiaria, true);
					
					TreeMap<Date, BigDecimal> mapaExpectativaEncalheDiariaOrdenado = 
						new TreeMap<Date, BigDecimal>(mapValueComparator);
					
					for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimentoEditor) {
						
						BigDecimal expectativaEncalheDiaria = 
							mapaExpectativaEncalheDiaria.get(dataBalanceamento);
						
						BigDecimal expectativaEncalheProduto = 
							produtoRecolhimento.getExpectativaEncalhe();
						
						if (expectativaEncalheDiaria == null) {
							
							expectativaEncalheDiaria = expectativaEncalheProduto;
							
						} else if (expectativaEncalheProduto != null) {
							
							expectativaEncalheDiaria = 
								expectativaEncalheDiaria.add(expectativaEncalheProduto);
						}
						
						mapaExpectativaEncalheDiaria.put(dataBalanceamento, expectativaEncalheDiaria);
					}	
					
					mapaQuantidadeEditoresPorData.put(
						dataBalanceamento, ++quantidadeEditoresAlocadosNaData);
					
					mapaExpectativaEncalheDiariaOrdenado.putAll(mapaExpectativaEncalheDiaria);
					
					mapaExpectativaEncalhePorQuantidadeEditores.put(
						idEditor, mapaExpectativaEncalheDiariaOrdenado);
				}
			}
		}

		return mapaExpectativaEncalhePorQuantidadeEditores;
	}
	
	/*
	 * Obtém o mapa de expectativa de encalhe por editor.
	 * 
	 * DEPRECATED - Aguardando homologação do novo algoritmo 
	 * do método obterMapaExpectativaEncalhePorQuantidadeEditores
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private Map<Long, TreeMap<Date, BigDecimal>> obterMapaExpectativaEncalheEditor(
											Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor) {
		
		Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalheEditor =
			new HashMap<Long, TreeMap<Date,BigDecimal>>();
		
		for (Map.Entry<Long, List<ProdutoRecolhimentoDTO>> entryProdutosRecolhimentoEditor 
				: mapaProdutosRecolhimentoEditor.entrySet()) {
			
			Long idEditor = entryProdutosRecolhimentoEditor.getKey();
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoEditor = entryProdutosRecolhimentoEditor.getValue();
			
			Map<Date, BigDecimal> mapaExpectativaEncalheDiaria = new HashMap<Date, BigDecimal>();
			
			MapValueComparator<Date, BigDecimal> mapValueComparator =
				new MapValueComparator<Date, BigDecimal>(mapaExpectativaEncalheDiaria, true);
			
			TreeMap<Date, BigDecimal> mapaExpectativaEncalheDiariaOrdenado = 
				new TreeMap<Date, BigDecimal>(mapValueComparator);
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimentoEditor) {
				
				Date dataRecolhimento = produtoRecolhimento.getDataRecolhimentoDistribuidor();
				
				BigDecimal expectativaEncalheDiaria = mapaExpectativaEncalheDiaria.get(dataRecolhimento);
				
				BigDecimal expectativaEncalheProduto = produtoRecolhimento.getExpectativaEncalhe();
				
				if (expectativaEncalheDiaria == null) {
					
					expectativaEncalheDiaria = produtoRecolhimento.getExpectativaEncalhe();
					
				} else if (expectativaEncalheProduto != null) {
					
					expectativaEncalheDiaria = expectativaEncalheDiaria.add(expectativaEncalheProduto);
				}
				
				mapaExpectativaEncalheDiaria.put(dataRecolhimento, expectativaEncalheDiaria);
			}	
			
			mapaExpectativaEncalheDiariaOrdenado.putAll(mapaExpectativaEncalheDiaria);
			
			mapaExpectativaEncalheEditor.put(idEditor, mapaExpectativaEncalheDiariaOrdenado);
		}
		
		return mapaExpectativaEncalheEditor;
	}
	
	/*
	 * Aloca os produtos na matriz de recolhimento.
	 */
	private List<ProdutoRecolhimentoDTO> alocarProdutosMatrizRecolhimento(
													Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
													Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalheEditor,
													Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor,
													TreeSet<Date> datasRecolhimento) {
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados = new ArrayList<>();
		
		for (Map.Entry<Long, TreeMap<Date, BigDecimal>> entryExpectativaEncalheEditor 
				: mapaExpectativaEncalheEditor.entrySet()) {
			
			Long idEditor = entryExpectativaEncalheEditor.getKey();
			
			TreeMap<Date, BigDecimal> mapaExpectativaEncalheDiariaEditor = entryExpectativaEncalheEditor.getValue();
			
			Date dataRecolhimentoEditor = mapaExpectativaEncalheDiariaEditor.firstKey();
			
			Date dataBalanceamento = super.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimentoEditor);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoEditor = mapaProdutosRecolhimentoEditor.get(idEditor);
			
			if (dataBalanceamento != null) {
			
				List<ProdutoRecolhimentoDTO> produtosRecolhimentoAtuaisNaData = 
					matrizRecolhimento.get(dataBalanceamento);
				
				super.atualizarMatrizRecolhimento(
					matrizRecolhimento, produtosRecolhimentoAtuaisNaData, 
						produtosRecolhimentoEditor, dataBalanceamento);
				
			} else {
				
				produtosRecolhimentoNaoBalanceados.addAll(produtosRecolhimentoEditor);
			}
		}
		
		return produtosRecolhimentoNaoBalanceados;
	}

}
