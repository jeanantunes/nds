package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
	protected TreeMap<Date, List<ProdutoRecolhimentoDTO>> gerarMatrizRecolhimentoBalanceada(RecolhimentoDTO dadosRecolhimento) {
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoBalanceada =
			new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		this.processarProdutosRecolhimentoNaoBalanceaveis(matrizRecolhimentoBalanceada, dadosRecolhimento);

		List<ProdutoRecolhimentoDTO> produtosRecolhimento = dadosRecolhimento.getProdutosRecolhimento();

		Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor = 
			this.obterMapaProdutosRecolhimentoEditor(produtosRecolhimento);
		
		Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalheEditor =
			this.obterMapaExpectativaEncalheEditor(mapaProdutosRecolhimentoEditor);
		
		TreeSet<Date> datasRecolhimento = dadosRecolhimento.getDatasRecolhimentoFornecedor();
		
		this.alocarProdutosMatrizRecolhimento(
			matrizRecolhimentoBalanceada, mapaExpectativaEncalheEditor, 
				mapaProdutosRecolhimentoEditor, datasRecolhimento);
		
		return matrizRecolhimentoBalanceada;
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

			if (produtoRecolhimento.isPossuiChamada()) {
				
				continue;
			}
			
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
				
				Date dataRecolhimento = produtoRecolhimento.getDataRecolhimentoPrevista();
				
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
	private void alocarProdutosMatrizRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												  Map<Long, TreeMap<Date, BigDecimal>> mapaExpectativaEncalheEditor,
												  Map<Long, List<ProdutoRecolhimentoDTO>> mapaProdutosRecolhimentoEditor,
												  TreeSet<Date> datasRecolhimento) {
		
		for (Map.Entry<Long, TreeMap<Date, BigDecimal>> entryExpectativaEncalheEditor 
				: mapaExpectativaEncalheEditor.entrySet()) {
			
			Long idEditor = entryExpectativaEncalheEditor.getKey();
			
			TreeMap<Date, BigDecimal> mapaExpectativaEncalheDiariaEditor = entryExpectativaEncalheEditor.getValue();
			
			Date dataRecolhimentoEditor = mapaExpectativaEncalheDiariaEditor.firstKey();
			
			Date dataBalanceamento = super.obterDataRecolhimentoPermitida(datasRecolhimento, dataRecolhimentoEditor);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoEditor = mapaProdutosRecolhimentoEditor.get(idEditor);
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimentoAtuaisNaData = 
				matrizRecolhimento.get(dataBalanceamento);
			
			super.atualizarMatrizRecolhimento(
				matrizRecolhimento, produtosRecolhimentoAtuaisNaData, 
					produtosRecolhimentoEditor, dataBalanceamento);
		}
	}

}
