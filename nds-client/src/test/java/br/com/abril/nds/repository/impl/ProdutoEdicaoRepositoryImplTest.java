package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class ProdutoEdicaoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ProdutoEdicaoRepositoryImpl produtoEdicaoRepository;

	@Before
	public void setUp() {
		
		
		TipoProduto tipoProduto = Fixture.tipoProduto("Revista", GrupoProduto.REVISTA, "99000642");
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL, tipoProduto);
		getSession().save(produto);

		ProdutoEdicao produtoEdicao =
				Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), produto,
						Fixture.fornecedorFC());
		getSession().save(produtoEdicao);
		
		Lancamento lancamento = 
				Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, 
						new Date(), new Date(), new Date(), new Date(), BigDecimal.TEN, StatusLancamento.RECEBIDO, null);
		getSession().save(lancamento);
		
		Estudo estudo = 
				Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicao);
		getSession().save(estudo);
		
		ParametroSistema parametroSistema = 
				Fixture.parametroSistema(1L, TipoParametroSistema.PATH_IMAGENS_CAPA, "");
		getSession().save(parametroSistema);
	}
	
	@Test
	public void obterProdutoEdicaoPorNomeProduto() {
		List<ProdutoEdicao> listaProdutoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto("Veja");
		
		Assert.assertTrue(!listaProdutoEdicao.isEmpty());
	}
	
	@Test
	public void obterProdutoEdicaoPorCodProdutoNumEdicao() {
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao("1", 1L);
		
		Assert.assertTrue(produtoEdicao != null);
	}
	
	@Test
	public void obterProdutoEdicaoPorCodigoEdicaoDataLancamento(){
		FuroProdutoDTO furoProdutoDTO = 
				produtoEdicaoRepository.obterProdutoEdicaoPorCodigoEdicaoDataLancamento("1", null, 1L, new Date());
		
		Assert.assertTrue(furoProdutoDTO != null);
	}

	protected void flushClear() {
		getSession().flush();
		getSession().clear();
	}
	
	@Test
	public void obterListaProdutoEdicao() {
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setNumeroEdicao(1L);
		
		
		@SuppressWarnings("unused")
		List<ProdutoEdicao> listaProdutoEdicao = 
				produtoEdicaoRepository.obterListaProdutoEdicao(produto, produtoEdicao);
		
	}
	
}
