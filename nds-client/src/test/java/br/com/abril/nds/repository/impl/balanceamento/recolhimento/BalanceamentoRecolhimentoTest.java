package br.com.abril.nds.repository.impl.balanceamento.recolhimento;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

public class BalanceamentoRecolhimentoTest extends AbstractRepositoryImplTest {
	
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
	
	@Before
	public void setUp() {

		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		PessoaJuridica juridicaFc = fornecedorFC.getJuridica();
		
		Editor globo = Fixture.criarEditor("Globo", 687L, juridicaFc, true);
		save(globo);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.setEditor(abril);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.setEditor(abril);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.setEditor(globo);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.setEditor(abril);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);
		
		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.setEditor(abril);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		ProdutoEdicao veja1 = Fixture.produtoEdicao("1", 1L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 1L, veja, null, false);

		veja1.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao("1", 2L, 15, 30,
				new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA", 2L,
				quatroRodas, null, false);

		quatroRoda2.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao infoExame3 = Fixture.produtoEdicao("1", 3L, 5, 30,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPB", 3L, infoExame, null, false);

		infoExame3.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao capricho1 = Fixture.produtoEdicao("1", 1L, 10, 15,
				new BigDecimal(0.12), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPC", 4L, capricho, null, false);
		
		capricho1.setExpectativaVenda(BigDecimal.TEN);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao("1", 1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOPD", 5L, cromoReiLeao, null, false);
		
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
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);
		
		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, 
				usuario,
				notaFiscal2Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012), 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50));
		
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, BigInteger.valueOf(50));
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
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(25));
		
		save(itemNotaFiscal4Rodas);
		
		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);

		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, BigInteger.valueOf(25));
		save(itemRecebimentoFisico4Rodas);
		
		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.EXPEDIDO, itemRecebimentoFisico1Veja, 1);
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO, quatroRoda2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(25),
				StatusLancamento.EXPEDIDO, itemRecebimentoFisico4Rodas, 2);
		
		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO, infoExame3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012), 
				new Date(),
				new Date(),
				BigInteger.valueOf(40),
				StatusLancamento.EXPEDIDO, null, 3);
		
		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO, capricho1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012),
				new Date(),
				new Date(),
				BigInteger.TEN,
				StatusLancamento.EXPEDIDO, null, 4);
		
		lancamentoCromoReiLeao = Fixture.lancamento(TipoLancamento.LANCAMENTO, cromoReiLeao1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.APRIL, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(10000),
				StatusLancamento.BALANCEADO, null, 5);
		
		save(lancamentoVeja, lancamentoQuatroRodas, lancamentoInfoExame,
				lancamentoCapricho, lancamentoCromoReiLeao);
		
		ChamadaEncalhe chamadaEncalheVeja = new ChamadaEncalhe();
		
		chamadaEncalheVeja.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		chamadaEncalheVeja.setDataRecolhimento(lancamentoVeja.getDataRecolhimentoPrevista());
		
		chamadaEncalheVeja.setProdutoEdicao(veja1);
		
//		save(chamadaEncalheVeja);

		ChamadaEncalhe chamadaEncalheCapricho = new ChamadaEncalhe();
		
		chamadaEncalheCapricho.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		chamadaEncalheCapricho.setDataRecolhimento(lancamentoCapricho.getDataRecolhimentoPrevista());
		
		chamadaEncalheCapricho.setProdutoEdicao(capricho1);
		
		save(chamadaEncalheCapricho);

		ChamadaEncalhe chamadaEncalheQuatroRodas = new ChamadaEncalhe();
		
		chamadaEncalheQuatroRodas.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
		
		chamadaEncalheQuatroRodas.setDataRecolhimento(lancamentoQuatroRodas.getDataRecolhimentoPrevista());
		
		chamadaEncalheQuatroRodas.setProdutoEdicao(quatroRoda2);
		
		save(chamadaEncalheQuatroRodas);
		
		Box box = Fixture.boxReparte300();
		
		Cota cotaDinap = Fixture.cota(50, fornecedorDinap.getJuridica(), SituacaoCadastro.ATIVO, box);
		
		EstoqueProdutoCota estoqueProdutoCotaCapricho = Fixture.estoqueProdutoCota(capricho1, cotaDinap, BigInteger.valueOf(110), BigInteger.TEN);
		
		save(box, cotaDinap, estoqueProdutoCotaCapricho);

		LancamentoParcial lancamentoParcialCapricho = Fixture.criarLancamentoParcial(capricho1,
																			 lancamentoCapricho.getDataLancamentoPrevista(), 
																			 lancamentoCapricho.getDataRecolhimentoPrevista(),
																			 StatusLancamentoParcial.PROJETADO);

		lancamentoParcialCapricho.setStatus(StatusLancamentoParcial.PROJETADO);
		
		PeriodoLancamentoParcial parcialCapricho = Fixture.criarPeriodoLancamentoParcial(
				lancamentoCapricho, 
				lancamentoParcialCapricho, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);

		Estudo estudoCapricho = Fixture.estudo(BigInteger.valueOf(180),
				lancamentoCapricho.getDataLancamentoDistribuidor(), capricho1);
		
		EstudoCota estudoCotaCapricho = Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoCapricho, cotaDinap);

		save(estudoCapricho, lancamentoParcialCapricho, parcialCapricho, estudoCotaCapricho);
		
		Box box301 = Fixture.criarBox(357, "Box 301", TipoBox.ENCALHE);
		
		Cota cotaFC = Fixture.cota(55, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box301);
		
		EstoqueProdutoCota estoqueProdutoCotaQuatroRodas = Fixture.estoqueProdutoCota(quatroRoda2, cotaFC, BigInteger.valueOf(110), BigInteger.TEN);
		
		save(box301, cotaFC, estoqueProdutoCotaQuatroRodas);
		
		LancamentoParcial lancamentoParcialQuatroRodas = Fixture.criarLancamentoParcial(quatroRoda2,
																					    lancamentoQuatroRodas.getDataLancamentoPrevista(), 
																					    lancamentoQuatroRodas.getDataRecolhimentoPrevista(),
																					    StatusLancamentoParcial.PROJETADO);
		
		lancamentoParcialQuatroRodas.setStatus(StatusLancamentoParcial.PROJETADO);
		
		PeriodoLancamentoParcial parcialQuatroRodas = Fixture.criarPeriodoLancamentoParcial(
				lancamentoQuatroRodas, 
				lancamentoParcialQuatroRodas, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
		
		Estudo estudoQuatroRodas = Fixture.estudo(BigInteger.valueOf(180),
				lancamentoQuatroRodas.getDataLancamentoDistribuidor(), quatroRoda2);
		
		EstudoCota estudoCotaQuatroRodas = Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoQuatroRodas, cotaFC);
		
		save(estudoQuatroRodas, lancamentoParcialQuatroRodas, parcialQuatroRodas, estudoCotaQuatroRodas);
		
		Cota cotaManoel = Fixture.cota(60, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box);

		EstoqueProdutoCota estoqueProdutoCotaVeja= Fixture.estoqueProdutoCota(veja1, cotaManoel, BigInteger.valueOf(110), BigInteger.TEN);
		
		save(cotaManoel, estoqueProdutoCotaVeja);

		LancamentoParcial lancamentoParcialVeja = Fixture.criarLancamentoParcial(veja1,
																				 lancamentoVeja.getDataLancamentoPrevista(), 
																				 lancamentoVeja.getDataRecolhimentoPrevista(),
																				 StatusLancamentoParcial.PROJETADO);

		lancamentoParcialVeja.setStatus(StatusLancamentoParcial.PROJETADO);
		
		PeriodoLancamentoParcial parcialVeja = Fixture.criarPeriodoLancamentoParcial(
				lancamentoVeja, 
				lancamentoParcialVeja, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);

		Estudo estudoVeja = Fixture.estudo(BigInteger.valueOf(180),
				lancamentoVeja.getDataLancamentoDistribuidor(), veja1);
		
		EstudoCota estudoCotaVeja = Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoVeja, cotaManoel);

		save(estudoVeja, lancamentoParcialVeja, parcialVeja, estudoCotaVeja);
		
		Box box303 = Fixture.criarBox(359, "Box 303", TipoBox.ENCALHE);
		
		Cota cotaJurandir = Fixture.cota(59, fornecedorFC.getJuridica(), SituacaoCadastro.ATIVO, box303);
		
		EstoqueProdutoCota estoqueProdutoCotaInfoExame = Fixture.estoqueProdutoCota(infoExame3, cotaJurandir, BigInteger.valueOf(110), BigInteger.TEN);
		
		save(box303, cotaJurandir, estoqueProdutoCotaInfoExame);
		
		LancamentoParcial lancamentoParcialInfoExame = Fixture.criarLancamentoParcial(infoExame3,
																					  lancamentoInfoExame.getDataLancamentoPrevista(), 
																				      lancamentoInfoExame.getDataRecolhimentoPrevista(),
																				      StatusLancamentoParcial.PROJETADO);
		
		lancamentoParcialInfoExame.setStatus(StatusLancamentoParcial.PROJETADO);
		
		PeriodoLancamentoParcial parcialInfoExame = Fixture.criarPeriodoLancamentoParcial(
				lancamentoInfoExame, 
				lancamentoParcialInfoExame, 
				StatusLancamentoParcial.PROJETADO, TipoLancamentoParcial.FINAL);
		
		Estudo estudoInfoExame = Fixture.estudo(BigInteger.valueOf(180),
				lancamentoInfoExame.getDataLancamentoDistribuidor(), infoExame3);
		
		EstudoCota estudoCotaInfoExame = Fixture.estudoCota(BigInteger.TEN, BigInteger.ONE, estudoInfoExame, cotaJurandir);
		
		save(estudoInfoExame, lancamentoParcialInfoExame, parcialInfoExame, estudoCotaInfoExame);
	}

	@Test
	public void buscarBalanceamentoPeriodoSucesso() {

		Date data22022012 = Fixture.criarData(22,
				Calendar.FEBRUARY, 2011);
		Date data23032012 = Fixture.criarData(23,
				Calendar.MARCH, 2012);
		Intervalo<Date> periodo = new Intervalo<Date>(data22022012, data23032012);

		List<ProdutoRecolhimentoDTO> resumos = lancamentoRepository
				.obterBalanceamentoRecolhimento(periodo,
						Collections.singletonList(fornecedorDinap.getId()), GrupoProduto.CROMO);

		Assert.assertEquals(4, resumos.size());

		ProdutoRecolhimentoDTO produtoRecolhimento = resumos.get(0);
		
		Assert.assertFalse(produtoRecolhimento.isPossuiChamada());
		
		produtoRecolhimento = resumos.get(1);
		
		Assert.assertTrue(produtoRecolhimento.isPossuiChamada());
		
		boolean ordenacaoPorDataCorreta = !DateUtil.isDataInicialMaiorDataFinal(resumos.get(0).getDataRecolhimentoDistribuidor(), 
																				resumos.get(1).getDataRecolhimentoDistribuidor());
		
		Assert.assertTrue(ordenacaoPorDataCorreta);
		
		ordenacaoPorDataCorreta = !DateUtil.isDataInicialMaiorDataFinal(resumos.get(0).getDataRecolhimentoDistribuidor(), 
																		resumos.get(1).getDataRecolhimentoDistribuidor());
		
		Assert.assertTrue(ordenacaoPorDataCorreta);
		
		ordenacaoPorDataCorreta = !DateUtil.isDataInicialMaiorDataFinal(resumos.get(0).getDataRecolhimentoDistribuidor(), 
																		resumos.get(1).getDataRecolhimentoDistribuidor());
		
		Assert.assertTrue(ordenacaoPorDataCorreta);
		
		ordenacaoPorDataCorreta = !DateUtil.isDataInicialMaiorDataFinal(resumos.get(0).getDataRecolhimentoDistribuidor(), 
																		resumos.get(1).getDataRecolhimentoDistribuidor());
		Assert.assertTrue(ordenacaoPorDataCorreta);
	}
}
