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
 * Classe de teste para a estratégia de balanceamento de recolhimento automático.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoAutomaticoStrategyTest {

	@Test
	public void efetuarBalanceamentoSemExcederCapacidadeManuseio() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);

		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(this.obterDadosRecolhimentoLimiteCapacidadeManuseio());
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertFalse(balanceamentoRecolhimento.isMatrizFechada());
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = balanceamentoRecolhimento.getMatrizRecolhimento();
		
		Assert.assertNotNull(matrizRecolhimento);
		
		int tamanhoEsperado = 2;
		
		Assert.assertEquals(tamanhoEsperado, matrizRecolhimento.size());
		
		Date data08052012 = DateUtil.parseDataPTBR("08/05/2012");
		Date data10052012 = DateUtil.parseDataPTBR("10/05/2012");
		
		Assert.assertTrue(matrizRecolhimento.containsKey(data08052012));
		Assert.assertTrue(matrizRecolhimento.containsKey(data10052012));
		
		int qtdeProdutosRecolhimentoEsperada08052012 = 100;
		
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada08052012, matrizRecolhimento.get(data08052012).size());
		
		int qtdeProdutosRecolhimentoEsperada10052012 = 100;
		
		Assert.assertEquals(qtdeProdutosRecolhimentoEsperada10052012, matrizRecolhimento.get(data10052012).size());
	}
	
	private RecolhimentoDTO obterDadosRecolhimentoLimiteCapacidadeManuseio() {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal("100"));
		
		TreeSet<Date> datasRecolhimentoDistribuidor = new TreeSet<Date>();
		
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("07/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("08/05/2012"));
		datasRecolhimentoDistribuidor.add(DateUtil.parseDataPTBR("09/05/2012"));
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
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(DateUtil.parseDataPTBR("07/05/2012"));
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(DateUtil.parseDataPTBR("08/05/2012"));
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 50; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(DateUtil.parseDataPTBR("09/05/2012"));
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 20; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(DateUtil.parseDataPTBR("10/05/2012"));
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}
		
		for (int i = 1; i <= 30; i++) {
			
			ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(DateUtil.parseDataPTBR("11/05/2012"));
			produtoRecolhimento.setExpectativaEncalhe(new BigDecimal(1));
			
			produtosRecolhimento.add(produtoRecolhimento);
		}

		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		return dadosRecolhimento;
	}
	
	@Test
	public void validarDadosRecolhimentoInexistentes() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
	@Test
	public void validarDadosRecolhimentoApenasComCapacidadeRecolhimento() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(new BigDecimal("100"));
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
	@Test
	public void validarDadosRecolhimentoApenasComDatasRecolhimentoDistribuidor() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		TreeSet<Date> datasRecolhimentoDistribuidor = new TreeSet<Date>();
		
		datasRecolhimentoDistribuidor.add(new Date());
		
		dadosRecolhimento.setDatasRecolhimentoDistribuidor(datasRecolhimentoDistribuidor);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
	@Test
	public void validarDadosRecolhimentoApenasComDatasRecolhimentoFornecedor() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		TreeSet<Date> datasRecolhimentoFornecedor = new TreeSet<Date>();
		
		datasRecolhimentoFornecedor.add(new Date());
		
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
	@Test
	public void validarDadosRecolhimentoApenasComMapaExpectativaEncalheTotalDiaria() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria = new TreeMap<Date, BigDecimal>();
		
		mapaExpectativaEncalheTotalDiaria.put(new Date(), BigDecimal.TEN);
		
		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria(mapaExpectativaEncalheTotalDiaria);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
	@Test
	public void validarDadosRecolhimentoApenasComProdutosRecolhimento() {
		
		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = 
			BalanceamentoRecolhimentoFactory.getStrategy(TipoBalanceamentoRecolhimento.AUTOMATICO);
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		
		ProdutoRecolhimentoDTO produtoRecolhimento = new ProdutoRecolhimentoDTO();
		
		produtosRecolhimento.add(produtoRecolhimento);
		
		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);
		
		Assert.assertNotNull(balanceamentoRecolhimento);
		
		Assert.assertNull(balanceamentoRecolhimento.getMatrizRecolhimento());
	}
	
}
