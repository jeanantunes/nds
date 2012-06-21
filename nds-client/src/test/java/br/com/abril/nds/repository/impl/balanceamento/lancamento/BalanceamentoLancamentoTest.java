package br.com.abril.nds.repository.impl.balanceamento.lancamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

public class BalanceamentoLancamentoTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	private Lancamento lancamentoVeja;
	private Lancamento lancamentoQuatroRodas;
	private Lancamento lancamentoInfoExame;
	private Lancamento lancamentoCapricho;
	private Lancamento lancamentoCromoReiLeao;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	private Intervalo<Date> periodoLancamento;
	
	private List<Long> fornecedores;
	
	private BigDecimal repartePrevisto;
	
	@Before
	public void setUp() {
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);
		
		TipoProduto tipoRevista = Fixture.tipoRevista();
		tipoCromo = Fixture.tipoCromo();
		save(tipoRevista, tipoCromo);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);
		
		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		ProdutoEdicao veja1 = Fixture.produtoEdicao("1", 1L, 1, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQRSTU", 1L, veja, null, false);

		veja1.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao("1", 2L, 1, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPQRST", 2L,
				quatroRodas, null, false);

		quatroRoda2.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao infoExame3 = Fixture.produtoEdicao("1", 3L, 1, 30,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPQRS", 3L, infoExame, null, false);

		infoExame3.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao capricho1 = Fixture.produtoEdicao("1", 1L, 1, 15,
				new BigDecimal(0.12), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPQR", 4L, capricho, null, false);
		
		capricho1.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao("1", 1L, 1, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOPQ", 5L, cromoReiLeao, null, false);
		
		cromoReiLeao1.setExpectativaVenda(BigDecimal.TEN);
		
		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal1Veja, 
				new Date(),
				new Date(),
				TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		save(itemNotaFiscal1Veja);
		
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, new Date(),
				new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, new BigDecimal(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, 
				usuario,
				notaFiscal2Veja, 
				new Date(),
				new Date(),
				TipoLancamento.LANCAMENTO,
				new BigDecimal(50));
		
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, new Date(),
				new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, new BigDecimal(50));
		save(itemRecebimentoFisico2Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal4Rodas= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal4Rodas);

		ItemNotaFiscalEntrada itemNotaFiscal4Rodas = 
		
				Fixture.itemNotaFiscal(
						quatroRoda2, 
						usuario,
						notaFiscal4Rodas, 
						new Date(),
						new Date(), 
						TipoLancamento.LANCAMENTO,
						new BigDecimal(25));
		
		save(itemNotaFiscal4Rodas);
		
		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, new Date(),
				new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);

		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, new BigDecimal(25));
		save(itemRecebimentoFisico4Rodas);
		
		Date dataLancamento = new Date();
		
		repartePrevisto = BigDecimal.TEN;
		
		lancamentoVeja = Fixture.lancamento(TipoLancamento.LANCAMENTO, veja1,
				dataLancamento,
				new Date(),
				new Date(),
				new Date(),
				repartePrevisto,
				StatusLancamento.PLANEJADO, itemRecebimentoFisico1Veja, 1);
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda2,
				dataLancamento,
				new Date(),
				new Date(),
				new Date(),
				repartePrevisto,
				StatusLancamento.PLANEJADO, itemRecebimentoFisico4Rodas, 2);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame3,
				dataLancamento,
				new Date(), 
				new Date(),
				new Date(),
				repartePrevisto,
				StatusLancamento.PLANEJADO, null, 3);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho1,
				dataLancamento,
				new Date(),
				new Date(),
				new Date(),
				repartePrevisto,
				StatusLancamento.PLANEJADO, null, 4);
		
		dataLancamento = DateUtil.adicionarDias(dataLancamento, 1);
		
		lancamentoCromoReiLeao = Fixture.lancamento(TipoLancamento.LANCAMENTO, cromoReiLeao1,
				dataLancamento,
				new Date(),
				new Date(),
				new Date(),
				repartePrevisto,
				StatusLancamento.PLANEJADO, null, 5);
		
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame,
			 lancamentoCapricho, lancamentoCromoReiLeao);
		
		lancamentos.add(lancamentoVeja);
		lancamentos.add(lancamentoQuatroRodas);
		lancamentos.add(lancamentoInfoExame);
		lancamentos.add(lancamentoCapricho);
		lancamentos.add(lancamentoCromoReiLeao);
		
		Box box = Fixture.boxReparte300();

		save(box);
		
		Cota cotaDinap = Fixture.cota(50, fornecedorDinap.getJuridica(), SituacaoCadastro.ATIVO, box);
		
		save(cotaDinap);
		
//		LancamentoParcial lancamentoParcialCapricho = Fixture.criarLancamentoParcial(capricho1,
//																			 lancamentoCapricho.getDataLancamentoPrevista(), 
//																			 lancamentoCapricho.getDataRecolhimentoPrevista(),
//																			 StatusLancamentoParcial.PROJETADO);
//
//		lancamentoParcialCapricho.setStatus(StatusLancamentoParcial.PROJETADO);
//		
//		save(lancamentoParcialCapricho);
//		
//		PeriodoLancamentoParcial parcialCapricho = Fixture.criarPeriodoLancamentoParcial(
//				lancamentoCapricho, 
//				lancamentoParcialCapricho, 
//				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
//
//		save(parcialCapricho);
		
		Estudo estudoCapricho = Fixture.estudo(new BigDecimal(180),
				lancamentoCapricho.getDataLancamentoDistribuidor(), capricho1);
		
		save(estudoCapricho);
		
		EstudoCota estudoCotaCapricho = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudoCapricho, cotaDinap);

		save(estudoCotaCapricho);
		
		Box box301 = Fixture.criarBox("357", "Box 301", TipoBox.RECOLHIMENTO, false);
		box301.setPostoAvancado(false);
		
		save(box301);
		
		Cota cotaFC = Fixture.cota(55, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box301);
		
		save(cotaFC);
				
//		LancamentoParcial lancamentoParcialQuatroRodas = Fixture.criarLancamentoParcial(quatroRoda2,
//																					    lancamentoQuatroRodas.getDataLancamentoPrevista(), 
//																					    lancamentoQuatroRodas.getDataRecolhimentoPrevista(),
//																					    StatusLancamentoParcial.PROJETADO);
//		
//		lancamentoParcialQuatroRodas.setStatus(StatusLancamentoParcial.PROJETADO);
//		
//		save(lancamentoParcialQuatroRodas);
//		
//		PeriodoLancamentoParcial parcialQuatroRodas = Fixture.criarPeriodoLancamentoParcial(
//				lancamentoQuatroRodas, 
//				lancamentoParcialQuatroRodas, 
//				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
//		
//		save(parcialQuatroRodas);
		
		Estudo estudoQuatroRodas = Fixture.estudo(new BigDecimal(180),
				lancamentoQuatroRodas.getDataLancamentoDistribuidor(), quatroRoda2);
		
		save(estudoQuatroRodas);
		
		EstudoCota estudoCotaQuatroRodas = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudoQuatroRodas, cotaFC);
		
		save(estudoCotaQuatroRodas);
		
		Cota cotaManoel = Fixture.cota(60, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box);

		save(cotaManoel);

//		LancamentoParcial lancamentoParcialVeja = Fixture.criarLancamentoParcial(veja1,
//																				 lancamentoVeja.getDataLancamentoPrevista(), 
//																				 lancamentoVeja.getDataRecolhimentoPrevista(),
//																				 StatusLancamentoParcial.PROJETADO);
//
//		lancamentoParcialVeja.setStatus(StatusLancamentoParcial.PROJETADO);
//		
//		save(lancamentoParcialVeja);
//		
//		PeriodoLancamentoParcial parcialVeja = Fixture.criarPeriodoLancamentoParcial(
//				lancamentoVeja, 
//				lancamentoParcialVeja, 
//				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
//
//		save(parcialVeja);
		
		Estudo estudoVeja = Fixture.estudo(new BigDecimal(180),
				lancamentoVeja.getDataLancamentoDistribuidor(), veja1);
		
		save(estudoVeja);
		
		EstudoCota estudoCotaVeja = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudoVeja, cotaManoel);

		save(estudoCotaVeja);
		
		Box box303 = Fixture.criarBox("359", "Box 303", TipoBox.RECOLHIMENTO, false);
		box303.setPostoAvancado(false);
		
		save(box303);
		
		Cota cotaJurandir = Fixture.cota(59, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box303);
		
		save(cotaJurandir);
		
//		LancamentoParcial lancamentoParcialInfoExame = Fixture.criarLancamentoParcial(infoExame3,
//																					  lancamentoInfoExame.getDataLancamentoPrevista(), 
//																				      lancamentoInfoExame.getDataRecolhimentoPrevista(),
//																				      StatusLancamentoParcial.PROJETADO);
//		
//		lancamentoParcialInfoExame.setStatus(StatusLancamentoParcial.PROJETADO);
//		
//		save(lancamentoParcialInfoExame);
//		
//		PeriodoLancamentoParcial parcialInfoExame = Fixture.criarPeriodoLancamentoParcial(
//				lancamentoInfoExame, 
//				lancamentoParcialInfoExame, 
//				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
//		
//		save(parcialInfoExame);
		
		Estudo estudoInfoExame = Fixture.estudo(new BigDecimal(180),
				lancamentoInfoExame.getDataLancamentoDistribuidor(), infoExame3);
		
		save(estudoInfoExame);
		
		EstudoCota estudoCotaInfoExame = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.ONE, estudoInfoExame, cotaJurandir);
		
		save(estudoCotaInfoExame);
	}

	@Test
	public void obterBalanceamentoLancamento() {

		montarParametrosConsulta();
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			lancamentoRepository.obterBalanceamentoLancamento(periodoLancamento, fornecedores);

		Assert.assertEquals(lancamentos.size(), produtosLancamento.size());

		ProdutoLancamentoDTO produtoRecolhimentoVeja = produtosLancamento.get(0);
		
		ProdutoLancamentoDTO produtoRecolhimentoQuatroRodas = produtosLancamento.get(1);
		
		ProdutoLancamentoDTO produtoRecolhimentoCromoReiLeao = produtosLancamento.get(4);
		
		Assert.assertTrue(produtoRecolhimentoVeja.isPossuiEstudo());
		
		Assert.assertFalse(produtoRecolhimentoCromoReiLeao.isPossuiEstudo());
		
		Assert.assertTrue(produtoRecolhimentoVeja.isPossuiRecebimentoFisico());
		
		Assert.assertTrue(produtoRecolhimentoQuatroRodas.isPossuiRecebimentoFisico());
		
		Assert.assertFalse(produtoRecolhimentoCromoReiLeao.isPossuiRecebimentoFisico());
		
		boolean ordenacaoPorDataCorreta =
			!DateUtil.isDataInicialMaiorDataFinal(produtoRecolhimentoVeja.getDataLancamentoDistribuidor(), 
												  produtoRecolhimentoQuatroRodas.getDataLancamentoDistribuidor());
		
		Assert.assertTrue(ordenacaoPorDataCorreta);
	}
	
	@Test
	public void obterExpectativasRepartePorData() {
		
		montarParametrosConsulta();
		
		TreeMap<Date, BigDecimal> expectativaReparte =
			lancamentoRepository.obterExpectativasRepartePorData(periodoLancamento, fornecedores);
		
		Assert.assertEquals(lancamentos.size(), expectativaReparte.size());
		
		for (Map.Entry<Date, BigDecimal> entry : expectativaReparte.entrySet()) {

			boolean condition = entry.getValue().compareTo(repartePrevisto) == 0;
			
			Assert.assertTrue(condition);
		}
	}
	
	private void montarParametrosConsulta() {
		
		Date dataLancamentoInicial = new Date();
		
		Date dataLancamentoFinal = DateUtil.adicionarDias(dataLancamentoInicial, 6);
		
		periodoLancamento = new Intervalo<Date>(DateUtil.removerTimestamp(dataLancamentoInicial),
												DateUtil.removerTimestamp(dataLancamentoFinal));
		
		fornecedores = Collections.singletonList(fornecedorDinap.getId());
	}
	
}
