package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.util.DateUtil;

public class EstoqueProdutoCotaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	private static ProdutoEdicao produtoEdicaoVeja1;
	
	@Before
	public void setup() {
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		produtoEdicaoVeja1 =
			Fixture.produtoEdicao(1L, 10, 14, new BigDecimal(0.1),
								  BigDecimal.TEN, new BigDecimal(20), produtoVeja);
		save(produtoEdicaoVeja1);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 =
			Fixture.estoqueProdutoCota(produtoEdicaoVeja1, cotaManoel,
									   BigDecimal.TEN, BigDecimal.ONE);
		save(estoqueProdutoCotaVeja1);
		
		Lancamento lancamentoVeja1 =
			Fixture.lancamentoExpedidos(TipoLancamento.LANCAMENTO, produtoEdicaoVeja1, new Date(),
										DateUtil.adicionarDias(new Date(), 1), new Date(), new Date(),
										BigDecimal.TEN, StatusLancamento.EXPEDIDO, null, null, 1);
		save(lancamentoVeja1);
	}
	
	@Test
	public void buscarEstoqueProdutoCotaPorIdProdutEdicao() {
		
		List<EstoqueProdutoCota> listaEstoqueProdutoCota = 
			this.estoqueProdutoCotaRepository.buscarEstoqueProdutoCotaPorIdProdutEdicao(
				produtoEdicaoVeja1.getId());
		
		Assert.assertNotNull(listaEstoqueProdutoCota);
		
		Assert.assertTrue(!listaEstoqueProdutoCota.isEmpty());
	}
	
}
