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
 * Classe de teste para a estratégia de balanceamento de recolhimento por Valor Total do Produto.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoValorStrategyTest {

	@Test
	public void efetuarBalanceamentoPoucosDados() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.VALOR);

		RecolhimentoDTO dadosRecolhimento = this.obterPoucosDadosRecolhimento();
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		//Assert.assertFalse(balanceamentoRecolhimento.isMatrizFechada());
		
		Assert.assertEquals(
			dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor(), 
				balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = balanceamentoRecolhimento.getMatrizRecolhimento();
		
		Assert.assertNotNull(matrizRecolhimento);
		
		int tamanhoEsperado = 4;
		
		Assert.assertEquals(tamanhoEsperado, matrizRecolhimento.size());
		
		Date data07052012 = DateUtil.parseDataPTBR("07/05/2012");
		Date data08052012 = DateUtil.parseDataPTBR("08/05/2012");
		Date data10052012 = DateUtil.parseDataPTBR("10/05/2012");
		Date data11052012 = DateUtil.parseDataPTBR("11/05/2012");
		
		Assert.assertTrue(matrizRecolhimento.containsKey(data07052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data08052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data10052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data11052012));
		
		int qtdeProdutosRecolhimentoEsperada07052012 = 3;
		int qtdeProdutosRecolhimentoEsperada08052012 = 3;
		int qtdeProdutosRecolhimentoEsperada10052012 = 3;
		int qtdeProdutosRecolhimentoEsperada11052012 = 4;
		
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada07052012, matrizRecolhimento.get(data07052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada08052012, matrizRecolhimento.get(data08052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada10052012, matrizRecolhimento.get(data10052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada11052012, matrizRecolhimento.get(data11052012).size());
		
		Map<Date, BigDecimal> mapaValorProdutoTotalDiario =
			this.gerarMapaValorProdutoTotalDiario(matrizRecolhimento);
		
		BigDecimal valorTotalEsperado07052012 = new BigDecimal("3");
		BigDecimal valorTotalEsperado08052012 = new BigDecimal("3");
		BigDecimal valorTotalEsperado10052012 = new BigDecimal("3");
		BigDecimal valorTotalEsperado11052012 = new BigDecimal("4");
		
		Assert.assertEquals(valorTotalEsperado07052012, mapaValorProdutoTotalDiario.get(data07052012));
		Assert.assertEquals(valorTotalEsperado08052012, mapaValorProdutoTotalDiario.get(data08052012));
		Assert.assertEquals(valorTotalEsperado10052012, mapaValorProdutoTotalDiario.get(data10052012));
		Assert.assertEquals(valorTotalEsperado11052012, mapaValorProdutoTotalDiario.get(data11052012));
	}
	
	@Test
	public void efetuarBalanceamentoMuitosDados() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.VALOR);

		RecolhimentoDTO dadosRecolhimento = this.obterDadosRecolhimento();
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		//Assert.assertFalse(balanceamentoRecolhimento.isMatrizFechada());
		
		Assert.assertEquals(
			dadosRecolhimento.getCapacidadeRecolhimentoDistribuidor(), 
				balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = balanceamentoRecolhimento.getMatrizRecolhimento();
		
		Assert.assertNotNull(matrizRecolhimento);
		
		int tamanhoEsperado = 4;
		
		Assert.assertEquals(tamanhoEsperado, matrizRecolhimento.size());
		
		Date data07052012 = DateUtil.parseDataPTBR("07/05/2012");
		Date data08052012 = DateUtil.parseDataPTBR("08/05/2012");
		Date data10052012 = DateUtil.parseDataPTBR("10/05/2012");
		Date data11052012 = DateUtil.parseDataPTBR("11/05/2012");
		
		Assert.assertTrue(matrizRecolhimento.containsKey(data07052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data08052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data10052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data11052012));
		
		int qtdeProdutosRecolhimentoEsperada07052012 = 50;
		int qtdeProdutosRecolhimentoEsperada08052012 = 50;
		int qtdeProdutosRecolhimentoEsperada10052012 = 50;
		int qtdeProdutosRecolhimentoEsperada11052012 = 50;
		
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada07052012, matrizRecolhimento.get(data07052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada08052012, matrizRecolhimento.get(data08052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada10052012, matrizRecolhimento.get(data10052012).size());
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada11052012, matrizRecolhimento.get(data11052012).size());
		
		Map<Date, BigDecimal> mapaValorProdutoTotalDiario =
			this.gerarMapaValorProdutoTotalDiario(matrizRecolhimento);
		
		BigDecimal valorTotalEsperado07052012 = new BigDecimal("50");
		BigDecimal valorTotalEsperado08052012 = new BigDecimal("50");
		BigDecimal valorTotalEsperado10052012 = new BigDecimal("50");
		BigDecimal valorTotalEsperado11052012 = new BigDecimal("50");
		
		Assert.assertEquals(valorTotalEsperado07052012, mapaValorProdutoTotalDiario.get(data07052012));
		Assert.assertEquals(valorTotalEsperado08052012, mapaValorProdutoTotalDiario.get(data08052012));
		Assert.assertEquals(valorTotalEsperado10052012, mapaValorProdutoTotalDiario.get(data10052012));
		Assert.assertEquals(valorTotalEsperado11052012, mapaValorProdutoTotalDiario.get(data11052012));
	}
	
	/*
	 * Obtém os dados de recolhimento.
	 */
	private RecolhimentoDTO obterPoucosDadosRecolhimento() {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		//dadosRecolhimento.setBalancearMatriz(true);
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal("100"));
		
		TreeSet<Date> datasRecolhimentoDistribuidor = new TreeSet<Date>();
		
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("07/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("10/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("11/05/2012"));
		
		dadosRecolhimento.setDatasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		
		TreeSet<Date> datasRecolhimentoFornecedor = new TreeSet<Date>();
		
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("10/05/2012"));
		
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
		TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = new TreeMap<Date, BigDecimal>();
		
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("07/05/2012"), new BigDecimal("2"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("08/05/2012"), new BigDecimal("2"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("09/05/2012"), new BigDecimal("3"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("10/05/2012"), new BigDecimal("5"));
		mapaExpectativaEncalheTotalDiaria.put(DateUtil.parseDataPTBR("11/05/2012"), new BigDecimal("1"));
		
		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria(mapaExpectativaEncalheTotalDiaria);
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		
		for (int i = 1; i <= 2; i++) {
		
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("07/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 2; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("08/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 3; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("09/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 5; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("10/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 1; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("11/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}

		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		return dadosRecolhimento;
	}
	
	/*
	 * Obtém os dados de recolhimento.
	 */
	private RecolhimentoDTO obterDadosRecolhimento() {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		//dadosRecolhimento.setBalancearMatriz(true);
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal("100"));
		
		TreeSet<Date> datasRecolhimentoDistribuidor = new TreeSet<Date>();
		
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("07/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("10/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("11/05/2012"));
		
		dadosRecolhimento.setDatasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		
		TreeSet<Date> datasRecolhimentoFornecedor = new TreeSet<Date>();
		
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoFornecedor.add(DateUtil.parseDataPTBR("10/05/2012"));
		
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
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();

			Date dataRecolhimento = DateUtil.parseDataPTBR("08/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 50; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("09/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("10/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 30; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			Date dataRecolhimento = DateUtil.parseDataPTBR("11/05/2012");
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(dataRecolhimento);
			produtoRecolhimento.setDataRecolhimentoPrevista(dataRecolhimento);
			produtoRecolhimento.setValorTotal(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}

		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		return dadosRecolhimento;
	}
	
	/*
	 * Gera o mapa de expectativa de encalhe total diária ordenado pela maior data
	 * de acordo com a matriz de recolhimento.
	 */
	private Map<Date, BigDecimal> gerarMapaValorProdutoTotalDiario(
														Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Map<Date, BigDecimal> mapaValorProdutoTotalDiario = new TreeMap<Date, BigDecimal>();

		if (matrizRecolhimento == null || matrizRecolhimento.isEmpty()) {
			
			return mapaValorProdutoTotalDiario;
		}

		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entryMatrizRecolhimento : matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entryMatrizRecolhimento.getKey();
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entryMatrizRecolhimento.getValue();
			
			BigDecimal valorProdutoTotalDiario = BigDecimal.ZERO;
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (produtoRecolhimento.getValorTotal() != null) {
				
					valorProdutoTotalDiario = 
						valorProdutoTotalDiario.add(produtoRecolhimento.getValorTotal());
				}
			}
			
			mapaValorProdutoTotalDiario.put(dataRecolhimento, valorProdutoTotalDiario);
		}
		
		return mapaValorProdutoTotalDiario;
	}
	
}
