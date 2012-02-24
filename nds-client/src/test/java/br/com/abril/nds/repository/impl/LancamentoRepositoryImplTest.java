package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
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
	
	private Lancamento lancamentoVeja;
	private Lancamento lancamentoQuatroRodas;
	private Lancamento lancamentoInfoExame;
	private Lancamento lancamentoCapricho;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;

	@Before
	public void setUp() {
		fornecedorFC = Fixture.fornecedorFC();
		fornecedorDinap = Fixture.fornecedorDinap();
		save(fornecedorFC, fornecedorDinap);

		TipoProduto tipoRevista = Fixture.tipoRevista();
		save(tipoRevista);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorFC);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorFC);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorFC);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorFC);
		save(veja, quatroRodas, infoExame, capricho);

		ProdutoEdicao veja001 = Fixture.produtoEdicao(1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, veja);

		ProdutoEdicao quatroRoda001 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN,
				quatroRodas);

		ProdutoEdicao infoExame001 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, infoExame);

		ProdutoEdicao capricho001 = Fixture.produtoEdicao(1L, 10, 15,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, capricho);
		save(veja001, quatroRoda001, infoExame001, capricho001);
		
		lancamentoVeja = Fixture.lancamento(TipoLancamento.LANCAMENTO, veja001,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda001,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012));
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame001,
				Fixture.criarData(24, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012));
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho001,
				Fixture.criarData(27, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012));
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame, lancamentoCapricho);
	}

	@Test
	@Ignore
	public void obterLancamentosBalanceamentoMatriz() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(21, Calendar.FEBRUARY, 2012),
						Fixture.criarData(27, Calendar.FEBRUARY, 2012));
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(4, lancamentos.size());
		
		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoCapricho.getId(), lancamentos.get(1).getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2).getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(3).getId());
	}
	
	@Test
	@Ignore
	public void obterLancamentosBalanceamentoMatrizSemLancamentosPeriodo() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(29, Calendar.FEBRUARY, 2012),
						fornecedorFC.getId(), fornecedorDinap.getId());
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}
	
	@Test
	@Ignore
	public void obterLancamentosBalanceamentoMatrizSemLancamentosFornecedor() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(
						Fixture.criarData(21, Calendar.FEBRUARY, 2012),
						Fixture.criarData(23, Calendar.FEBRUARY, 2012), fornecedorDinap.getId());
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}

}
