package br.com.abril.nds.strategy.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Classe de teste para a estratégia abstrata de balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class AbstractBalanceamentoRecolhimentoStrategyTest {

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
		
		TreeSet<Date> datasRecolhimentoFornecedor = new TreeSet<Date>();
		
		datasRecolhimentoFornecedor.add(new Date());
		
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
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
