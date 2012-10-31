package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Classe de teste para a estratégia de balanceamento de recolhimento por editor.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoEditorStrategyTest {
	
	@Test
	public void efetuarBalanceamento() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.EDITOR);

		RecolhimentoDTO dadosRecolhimento = this.obterDadosRecolhimento();
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertEquals(
			dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor(), 
				balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = balanceamentoRecolhimento.getMatrizRecolhimento();
		
		Assert.assertNotNull(matrizRecolhimento);
		
		int tamanhoEsperado = 3;
		
		Assert.assertEquals(tamanhoEsperado, matrizRecolhimento.size());
		
		Date data07052012 = DateUtil.parseDataPTBR("07/05/2012");
		Date data10052012 = DateUtil.parseDataPTBR("10/05/2012");
		Date data11052012 = DateUtil.parseDataPTBR("11/05/2012");
		
		Assert.assertTrue(matrizRecolhimento.containsKey(data07052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data10052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data11052012));
		
		int qtdeProdutosRecolhimentoEsperada07052012 = 150;
		int qtdeProdutosRecolhimentoEsperada10052012 = 20;
		int qtdeProdutosRecolhimentoEsperada11052012 = 30;
		
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada07052012, matrizRecolhimento.get(data07052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada10052012, matrizRecolhimento.get(data10052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada11052012, matrizRecolhimento.get(data11052012).size());
		
		Map<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria =
			this.gerarMapaExpectativaEncalheTotalDiaria(matrizRecolhimento);
		
		BigDecimal expectativaEncalheEsperada07052012 = new BigDecimal("150");
		BigDecimal expectativaEncalheEsperada10052012 = new BigDecimal("20");
		BigDecimal expectativaEncalheEsperada11052012 = new BigDecimal("30");
		
		Assert.assertEquals(expectativaEncalheEsperada07052012, mapaExpectativaEncalheTotalDiaria.get(data07052012));
		Assert.assertEquals(expectativaEncalheEsperada10052012, mapaExpectativaEncalheTotalDiaria.get(data10052012));
		Assert.assertEquals(expectativaEncalheEsperada11052012, mapaExpectativaEncalheTotalDiaria.get(data11052012));
	}
	
	/*
	 * Obtém os dados de recolhimento.
	 */
	private RecolhimentoDTO obterDadosRecolhimento() {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal("100"));
		
		TreeSet<Date> datasRecolhimentoFornecedor = new TreeSet<Date>();
		
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("07/05/2012"));
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("10/05/2012"));
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("11/05/2012"));
		
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
		TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = new TreeMap<Date, BigDecimal>();
		
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("07/05/2012"), new BigDecimal("80"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("08/05/2012"), new BigDecimal("20"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("09/05/2012"), new BigDecimal("50"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("10/05/2012"), new BigDecimal("20"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("11/05/2012"), new BigDecimal("30"));
		
		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria(mapaExpectativaEncalheTotalDiaria);
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		
		for (int i = 1; i <= 80; i++) {
		
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("07/05/2012");
			
			produtoRecolhimento.setIdEditor(1L);
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("08/05/2012");
			
			produtoRecolhimento.setIdEditor(1L);
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 50; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("09/05/2012");
			
			produtoRecolhimento.setIdEditor(1L);
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("10/05/2012");
			
			produtoRecolhimento.setIdEditor(2L);
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 30; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("11/05/2012");
			
			produtoRecolhimento.setIdEditor(3L);
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}

		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		return dadosRecolhimento;
	}
	
	/*
	 * Gera o mapa de expectativa de encalhe total diária ordenado pela maior data
	 * de acordo com a matriz de recolhimento.
	 */
	private Map<Date, BigDecimal> gerarMapaExpectativaEncalheTotalDiaria(
														Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
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

}
