package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class LancamentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private LancamentoRepositoryImpl lancamentoRepository;
	
	private Lancamento lancamento1;
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;

	@Before
	public void setUp() {
		fornecedorFC = Fixture.fornecedorFC();
		fornecedorDinap = Fixture.fornecedorDinap();
		
		TipoProduto tipoRevista = Fixture.tipoRevista();
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorFC);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorFC);
		
		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorFC);
		
		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorFC);
						
		ProdutoEdicao veja001 = Fixture.produtoEdicao(1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, veja);
		
		ProdutoEdicao quatroRoda001 = Fixture.produtoEdicao(1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, quatroRodas);
		
		lancamento1 = Fixture.lancamento(TipoLancamento.LANCAMENTO, veja001,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(fornecedorFC, fornecedorDinap, tipoRevista, veja, veja001, lancamento1);
	}

	@Test
	public void obterLancamentosBalanceamentoMatriz() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(21, Calendar.FEBRUARY, 2012),
						Fixture.criarData(23, Calendar.FEBRUARY, 2012));
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(1, lancamentos.size());
		
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosPeriodo() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(23, Calendar.FEBRUARY, 2012),
						Fixture.criarData(27, Calendar.FEBRUARY, 2012),
						fornecedorFC.getId(), fornecedorDinap.getId());
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosFornecedor() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(21, Calendar.FEBRUARY, 2012),
						Fixture.criarData(23, Calendar.FEBRUARY, 2012), fornecedorDinap.getId());
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}

}
