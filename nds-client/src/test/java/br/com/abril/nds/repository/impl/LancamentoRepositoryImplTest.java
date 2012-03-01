package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.vo.PeriodoVO;

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
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.RECEBIDO, null);
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda001,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				new BigDecimal(25),
				StatusLancamento.RECEBIDO, null);
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame001,
				Fixture.criarData(24, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012), 
				new Date(),
				new Date(),
				new BigDecimal(40),
				StatusLancamento.RECEBIDO, null);
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho001,
				Fixture.criarData(27, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				BigDecimal.TEN,
				StatusLancamento.RECEBIDO, null);
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame, lancamentoCapricho);
		getSession().flush();
	}

	@Test
	public void obterLancamentosBalanceamentoMatriz() {
		PeriodoVO periodo = new PeriodoVO(Fixture.criarData(21,
				Calendar.FEBRUARY, 2012), Fixture.criarData(27,
				Calendar.FEBRUARY, 2012));
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO();
		filtro.setPeriodo(periodo);
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(4, lancamentos.size());

		Assert.assertEquals(lancamentoVeja.getId(), lancamentos.get(0).getId());
		Assert.assertEquals(lancamentoCapricho.getId(), lancamentos.get(1)
				.getId());
		Assert.assertEquals(lancamentoInfoExame.getId(), lancamentos.get(2)
				.getId());
		Assert.assertEquals(lancamentoQuatroRodas.getId(), lancamentos.get(3)
				.getId());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosPeriodo() {
		PeriodoVO periodo = new PeriodoVO(Fixture.criarData(28,
				Calendar.FEBRUARY, 2012), Fixture.criarData(29,
				Calendar.FEBRUARY, 2012));
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO();
		filtro.setPeriodo(periodo);
		filtro.getIdsFornecedores().add(fornecedorDinap.getId());
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}
	
	@Test
	public void obterLancamentosBalanceamentoMatrizSemLancamentosFornecedor() {
		PeriodoVO periodo = new PeriodoVO(Fixture.criarData(21,
				Calendar.FEBRUARY, 2012), Fixture.criarData(23,
				Calendar.FEBRUARY, 2012));
		FiltroLancamentoDTO filtro = new FiltroLancamentoDTO();
		filtro.setPeriodo(periodo);
		filtro.getIdsFornecedores().add(fornecedorDinap.getId());
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(filtro);
		Assert.assertNotNull(lancamentos);
		Assert.assertTrue(lancamentos.isEmpty());
	}

}
