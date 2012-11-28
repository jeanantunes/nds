package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.html.parser.DTD;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO.SortColumn;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
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
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class LancamentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	private Lancamento lancamentoVeja;
	private Lancamento proximoLancamentoVeja;
	private Lancamento ultimoLancamentoVeja;
	private Lancamento lancamentoQuatroRodas;
	private Lancamento lancamentoInfoExame;
	private Lancamento lancamentoCapricho;
	private Lancamento lancamentoCromoReiLeao;

	private ProdutoEdicao veja1;

	private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;

	private ChamadaEncalhe chamadaEncalhe;
	private ChamadaEncalhe chamadaEncalhe1;
	private ChamadaEncalhe chamadaEncalhe2;

	private Editor abril;
	private NCM ncmRevistas;
	private TipoProduto tipoRevista;

	private Produto veja;

	private Produto quatroRodas;

	@Before
	public void setUp() {
		abril = Fixture.editoraAbril();
		save(abril);

		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l, "CROMO", "KG");
		save(ncmCromo);

		tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);

		veja = Fixture.produtoVeja(tipoRevista);
		veja.setEditor(abril);
		veja.addFornecedor(fornecedorDinap);

		quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.setEditor(abril);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.setEditor(abril);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.setEditor(abril);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);

		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.setEditor(abril);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100), BigDecimal.TEN,
				new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", veja, null, false);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30, new Long(
				100), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA",
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30, new Long(
				100), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPC",
				infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15, new Long(
				120), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPD",
				capricho, null, false);

		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60,
				new Long(10), BigDecimal.ONE, new BigDecimal(1.5),
				"ABCDEFGHIJKLMNOPE", cromoReiLeao, null, false);

		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);

		CFOP cfop = Fixture.cfop5102();
		save(cfop);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);
		save(tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal1Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);

		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);

		ItemRecebimentoFisico itemRecebimentoFisico1Veja = Fixture
				.itemRecebimentoFisico(itemNotaFiscal1Veja,
						recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);

		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal2Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));

		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);

		ItemRecebimentoFisico itemRecebimentoFisico2Veja = Fixture
				.itemRecebimentoFisico(itemNotaFiscal2Veja,
						recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);

		NotaFiscalEntradaFornecedor notaFiscal4Rodas = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal4Rodas);

		ItemNotaFiscalEntrada itemNotaFiscal4Rodas =

		Fixture.itemNotaFiscal(quatroRoda2, usuario, notaFiscal4Rodas,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(25));

		save(itemNotaFiscal4Rodas);

		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);

		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = Fixture
				.itemRecebimentoFisico(itemNotaFiscal4Rodas,
						recebimentoFisico4Rodas, BigInteger.valueOf(25));
		save(itemRecebimentoFisico4Rodas);

		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisico1Veja, 1);
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);

		proximoLancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR,
				veja1, Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		ultimoLancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR,
				veja1, Fixture.criarData(24, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.CONFIRMADO, null, 1);

		lancamentoQuatroRodas = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				quatroRoda2, Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.MARCH, 2012), new Date(),
				new Date(), BigInteger.valueOf(25),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisico4Rodas, 1);

		lancamentoInfoExame = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				infoExame3, Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.MARCH, 2012), new Date(),
				new Date(), BigInteger.valueOf(40),
				StatusLancamento.CONFIRMADO, null, 1);

		lancamentoCapricho = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				capricho1, Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(12, Calendar.MARCH, 2012), new Date(),
				new Date(), BigInteger.TEN, StatusLancamento.CONFIRMADO, null,
				1);

		lancamentoCromoReiLeao = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				cromoReiLeao1, Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(23, Calendar.APRIL, 2012), new Date(),
				new Date(), BigInteger.valueOf(10000),
				StatusLancamento.CONFIRMADO, null, 1);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, proximoLancamentoVeja, ultimoLancamentoVeja,
				lancamentoQuatroRodas, lancamentoInfoExame, lancamentoCapricho,
				lancamentoCromoReiLeao, estudo);

		Calendar calendar = Calendar.getInstance();

		chamadaEncalhe = new ChamadaEncalhe();

		chamadaEncalhe
				.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		chamadaEncalhe.setDataRecolhimento(calendar.getTime());

		chamadaEncalhe.setProdutoEdicao(veja1);

		save(chamadaEncalhe);

		chamadaEncalhe1 = new ChamadaEncalhe();

		chamadaEncalhe1.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);

		calendar.add(Calendar.DAY_OF_MONTH, 5);

		chamadaEncalhe1.setDataRecolhimento(calendar.getTime());

		chamadaEncalhe1.setProdutoEdicao(capricho1);

		save(chamadaEncalhe1);

		chamadaEncalhe2 = new ChamadaEncalhe();

		chamadaEncalhe2.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);

		calendar.add(Calendar.DAY_OF_MONTH, 5);

		chamadaEncalhe2.setDataRecolhimento(calendar.getTime());

		chamadaEncalhe2.setProdutoEdicao(veja1);

		save(chamadaEncalhe2);

		Box box = Fixture.boxReparte300();

		Cota cota = Fixture.cota(50, fornecedorDinap.getJuridica(),
				SituacaoCadastro.ATIVO, box);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				capricho1, BigInteger.TEN, cota, null);

		save(box, cota, estudo, estoqueProdutoCota);

		LancamentoParcial lancamentoParcial = Fixture.criarLancamentoParcial(
				capricho1, lancamentoCapricho.getDataLancamentoPrevista(),
				lancamentoCapricho.getDataRecolhimentoPrevista(),
				StatusLancamentoParcial.PROJETADO);

		PeriodoLancamentoParcial parcial = Fixture.criarPeriodoLancamentoParcial(
				lancamentoCapricho, 
				lancamentoParcial,  
				StatusLancamentoParcial.PROJETADO,
				TipoLancamentoParcial.FINAL, 1);

		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.TEN,
				BigInteger.ONE, estudo, cota);

		save(lancamentoParcial, parcial, estudoCota);

		Cota cota2 = Fixture.cota(55, fornecedorFC.getJuridica(),
				SituacaoCadastro.ATIVO, box);

		EstoqueProdutoCota estoqueProdutoCota2 = Fixture.estoqueProdutoCota(
				infoExame3, BigInteger.TEN, cota2, null);

		save(cota2, estoqueProdutoCota2);
		
		LancamentoParcial lancamentoParcial2 = Fixture.criarLancamentoParcial(infoExame3,
																			  lancamentoInfoExame.getDataLancamentoPrevista(), 
																			  lancamentoInfoExame.getDataRecolhimentoPrevista(),
																			  StatusLancamentoParcial.PROJETADO);

		PeriodoLancamentoParcial parcial2 = Fixture.criarPeriodoLancamentoParcial(
				lancamentoInfoExame, 
				lancamentoParcial2, 
				StatusLancamentoParcial.RECOLHIDO, 
				TipoLancamentoParcial.PARCIAL,
				1);


		Estudo estudo2 = Fixture.estudo(BigInteger.valueOf(180),
				Fixture.criarData(12, Calendar.MARCH, 2012), infoExame3);

		EstudoCota estudoCota2 = Fixture.estudoCota(BigInteger.TEN,
				BigInteger.ONE, estudo2, cota2);

		save(estudo2, lancamentoParcial2, parcial2, estudoCota2);
	}

	@Test
	public void totalLancamentosBalanceamentoMatriz() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		SumarioLancamentosDTO sumario = lancamentoRepository
				.sumarioBalanceamentoMatrizLancamentos(data,
						Collections.singletonList(fornecedorDinap.getId()));
		Assert.assertNotNull(sumario);
		Assert.assertEquals(Long.valueOf(3), sumario.getTotalLancamentos());
		Assert.assertEquals(CurrencyUtil.formatarValor(new BigDecimal(2230)),
				CurrencyUtil.formatarValor(sumario.getValorTotalLancamentos()));
	}

	@Test
	public void totalLancamentosBalanceamentoMatrizNenhum() {
		Date data = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		SumarioLancamentosDTO sumario = lancamentoRepository
				.sumarioBalanceamentoMatrizLancamentos(data,
						Collections.singletonList(fornecedorFC.getId()));
		Assert.assertNotNull(sumario);
		Assert.assertEquals(Long.valueOf(0), sumario.getTotalLancamentos());
		Assert.assertNull(sumario.getValorTotalLancamentos());
	}

	@Test
	public void atualizarLancamentoIdLancamento() {
		Long idLancamento = 1L;

		lancamentoRepository.atualizarLancamento(idLancamento, null);
	}

	@Test
	public void atualizarLancamentoNovaDataLancamento() {
		Date data = Fixture.criarData(31, Calendar.OCTOBER, 2012);

		lancamentoRepository.atualizarLancamento(null, data);
	}

	@Test
	public void buscarResumosPeriodo() {

		Date data22022012 = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		Date data23022012 = Fixture.criarData(23, Calendar.FEBRUARY, 2012);
		List<Date> datas = Arrays.asList(data22022012, data23022012);
		List<ResumoPeriodoBalanceamentoDTO> resumos = lancamentoRepository
				.buscarResumosPeriodo(datas,
						Collections.singletonList(fornecedorDinap.getId()),
						GrupoProduto.CROMO);
		
		Assert.assertNotNull(resumos);
	}

	@Test
	public void obterLancamentosNaoExpedidosIdFornecedor() {

		Long idFornecedor = 1L;

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(null, null, idFornecedor, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosEstudo() {

		Boolean estudo = true;

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(null, null, null, estudo);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosData() {

		Date data = Fixture.criarData(31, Calendar.OCTOBER, 2012);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(null, data, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoDtEntrada() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.DATA_ENTRADA.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoCodProduto() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.CODIGO_PRODUTO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoNomeProduto() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.NOME_PRODUTO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoEdicao() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.EDICAO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoClassificacaoProduto() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.CLASSIFICACAO_PRODUTO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoPrecoProduto() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.PRECO_PRODUTO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoQntPacotePadrão() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.QTDE_PACOTE_PADRAO.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoQntReparte() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.QTDE_REPARTE.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPagincaoDataChamada() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.DATA_CHAMADA.getProperty());
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void obterLancamentosNaoExpedidosPaginacaoOrdenacao() {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(SortColumn.DATA_CHAMADA.getProperty());
		paginacao.setOrdenacao(Ordenacao.DESC);
		paginacao.setQtdResultadosPorPagina(1);
		paginacao.setPaginaAtual(1);

		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosNaoExpedidos(paginacao, null, null, null);

		Assert.assertNotNull(lancamentos);
	}

	@Test
	public void existeMatrizBalanceamentoConfirmado() {
		Date data = Fixture.criarData(01, Calendar.OCTOBER, 2012);

		Boolean existeLancamento = lancamentoRepository
				.existeMatrizBalanceamentoConfirmado(data);

		Assert.assertFalse(existeLancamento);
	}

	@Test
	public void obterTotalLancamentosNaoExpedidosData() {
		Date data = Fixture.criarData(01, Calendar.OCTOBER, 2012);

		lancamentoRepository
				.obterTotalLancamentosNaoExpedidos(data, null, null);
	}

	@Test
	public void obterTotalLancamentosNaoExpedidosIdFornecedor() {
		Long idFornecedor = 1L;

		lancamentoRepository.obterTotalLancamentosNaoExpedidos(null,
				idFornecedor, null);
	}

	@Test
	public void obterTotalLancamentosNaoExpedidosEstudoTrue() {
		Boolean estudo = true;

		lancamentoRepository.obterTotalLancamentosNaoExpedidos(null, null,
				estudo);
	}

	@Test
	public void obterTotalLancamentosNaoExpedidosEstudoFalse() {
		Boolean estudo = false;

		lancamentoRepository.obterTotalLancamentosNaoExpedidos(null, null,
				estudo);
	}

	@Test
	public void obterLancamentoPorItensRecebimentoFisicoDataPrevista() {
		Long idProdutoEdicao = 1L;
		Date dataPrevista = Fixture.criarData(01, Calendar.NOVEMBER, 2012);

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoPorItensRecebimentoFisico(dataPrevista, null,
						idProdutoEdicao);
	}

	@Test
	public void obterLancamentoPorItensRecebimentoFisicoTipoLancamento() {
		Long idProdutoEdicao = 1L;
		TipoLancamento tipo = TipoLancamento.PARCIAL;

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoPorItensRecebimentoFisico(null, tipo,
						idProdutoEdicao);
	}

	@Test
	public void obterDataRecolhimentoPrevistaCodProduto() {
		String codigoProduto = "1a";

		Date date = lancamentoRepository.obterDataRecolhimentoPrevista(
				codigoProduto, null);

	}

	@Test
	public void obterDataRecolhimentoPrevistaNumEdicao() {
		Long numeroEdicao = 1L;

		Date date = lancamentoRepository.obterDataRecolhimentoPrevista(null,
				numeroEdicao);

	}

	@Test
	public void obterBalanceamentoRecolhimento() {
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>();
		periodoRecolhimento
				.setDe(Fixture.criarData(01, Calendar.OCTOBER, 2012));
		periodoRecolhimento.setAte(Fixture.criarData(01, Calendar.NOVEMBER,
				2012));

		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);

		GrupoProduto grupoCromo = GrupoProduto.CROMO;

		List<ProdutoRecolhimentoDTO> produtoRecolhimentoDTOs = lancamentoRepository
				.obterBalanceamentoRecolhimento(periodoRecolhimento,
						fornecedores, grupoCromo);

		Assert.assertNotNull(produtoRecolhimentoDTOs);

	}

	@Test
	public void obterBalanceamentoRecolhimentoPorEditorData() {
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>();
		periodoRecolhimento
				.setDe(Fixture.criarData(01, Calendar.OCTOBER, 2012));
		periodoRecolhimento.setAte(Fixture.criarData(01, Calendar.NOVEMBER,
				2012));

		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);

		GrupoProduto grupoCromo = GrupoProduto.CROMO;

		List<ProdutoRecolhimentoDTO> produtoRecolhimentoDTOs = lancamentoRepository
				.obterBalanceamentoRecolhimentoPorEditorData(
						periodoRecolhimento, fornecedores, grupoCromo);

		Assert.assertNotNull(produtoRecolhimentoDTOs);

	}

	@Test
	public void obterExpectativasEncalhePorData() {
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>();
		periodoRecolhimento
				.setDe(Fixture.criarData(01, Calendar.OCTOBER, 2012));
		periodoRecolhimento.setAte(Fixture.criarData(01, Calendar.NOVEMBER,
				2012));

		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);

		GrupoProduto grupoCromo = GrupoProduto.CROMO;

		Map<Date, BigDecimal> expectativaEncalhe = lancamentoRepository
				.obterExpectativasEncalhePorData(periodoRecolhimento,
						fornecedores, grupoCromo);

		Assert.assertNotNull(expectativaEncalhe);

	}

	@Test
	public void obterUltimoLancamentoDaEdicao() {

		Lancamento lancamento = lancamentoRepository
				.obterUltimoLancamentoDaEdicao(veja1.getId());
	}

	@Test
	public void obterLancamentosPorId() {

		Set<Long> idsLancamento = new TreeSet<Long>();

		idsLancamento.add(lancamentoVeja.getId());
		idsLancamento.add(lancamentoInfoExame.getId());
		idsLancamento.add(lancamentoQuatroRodas.getId());

		List<Lancamento> listaLancamento = lancamentoRepository
				.obterLancamentosPorIdOrdenados(idsLancamento);

		Assert.assertNotNull(listaLancamento);

		Assert.assertTrue(listaLancamento.size() == idsLancamento.size());
	}

	@Test
	public void obterLancamentoInformeRecolhimento() {
		List<InformeEncalheDTO> result = lancamentoRepository
				.obterLancamentoInformeRecolhimento(1L, Calendar.getInstance(),
						Calendar.getInstance(), "precoDesconto", Ordenacao.ASC,
						0, 15);

		Assert.assertNotNull(result);
	}

	@Test
	public void obterLancamentoInformeRecolhimentoIdFornecedorNulo() {
		List<InformeEncalheDTO> result = lancamentoRepository
				.obterLancamentoInformeRecolhimento(null,
						Calendar.getInstance(), Calendar.getInstance(),
						"precoDesconto", Ordenacao.ASC, 0, 15);

		Assert.assertNotNull(result);
	}

	@Test
	public void obterLancamentoInformeRecolhimentoInitialResultNulo() {
		List<InformeEncalheDTO> result = lancamentoRepository
				.obterLancamentoInformeRecolhimento(1L, Calendar.getInstance(),
						Calendar.getInstance(), "precoDesconto", Ordenacao.ASC,
						null, 15);

		Assert.assertNotNull(result);
	}

	@Test
	public void obterLancamentoInformeRecolhimentoMaxResultNulo() {
		List<InformeEncalheDTO> result = lancamentoRepository
				.obterLancamentoInformeRecolhimento(1L, Calendar.getInstance(),
						Calendar.getInstance(), "precoDesconto", Ordenacao.ASC,
						0, null);

		Assert.assertNotNull(result);
	}

	@Test
	public void obterLancamentoInformeRecolhimentoOrdenacaoDesc() {
		List<InformeEncalheDTO> result = lancamentoRepository
				.obterLancamentoInformeRecolhimento(1L, Calendar.getInstance(),
						Calendar.getInstance(), "precoDesconto",
						Ordenacao.DESC, 0, 15);

		Assert.assertNotNull(result);
	}

	@Test
	public void quantidadeLancamentoInformeRecolhimento() {
		Long quantidade = lancamentoRepository
				.quantidadeLancamentoInformeRecolhimento(null,
						Calendar.getInstance(), Calendar.getInstance());
	}

	@Test
	public void quantidadeLancamentoInformeRecolhimentoIdFornecedor() {
		Long quantidade = lancamentoRepository
				.quantidadeLancamentoInformeRecolhimento(1L,
						Calendar.getInstance(), Calendar.getInstance());
	}

	@Test
	public void obterDataUltimoLancamentoParcialIdProdutoEdicao() {

		Long idProdutoEdicao = 1L;

		Date data = lancamentoRepository.obterDataUltimoLancamentoParcial(
				idProdutoEdicao, null);
	}

	@Test
	public void obterDataUltimoLancamentoParcialDataoperacao() {

		Date dataOperacao = Fixture.criarData(01, Calendar.NOVEMBER, 2012);

		Date data = lancamentoRepository.obterDataUltimoLancamentoParcial(null,
				dataOperacao);
	}

	@Test
	public void obterDataUltimoLancamentoIdProdutoEdicao() {

		Long idProdutoEdicao = 1L;

		Date data = lancamentoRepository.obterDataUltimoLancamento(
				idProdutoEdicao, null);
	}

	@Test
	public void obterDataUltimoLancamentoDataOperacao() {

		Date dataOperacao = Fixture.criarData(01, Calendar.NOVEMBER, 2012);

		Date data = lancamentoRepository.obterDataUltimoLancamento(null,
				dataOperacao);
	}

	@Test
	public void obterBalanceamentoLancamento() {

		Intervalo<Date> periodoDistribuicao = new Intervalo<Date>();
		periodoDistribuicao
				.setDe(Fixture.criarData(01, Calendar.OCTOBER, 2012));
		periodoDistribuicao.setAte(Fixture.criarData(01, Calendar.NOVEMBER,
				2012));

		List<Long> fornecedores = new ArrayList<Long>();
		fornecedores.add(1L);

		List<ProdutoLancamentoDTO> produtoLancamentoDTOs = lancamentoRepository
				.obterBalanceamentoLancamento(periodoDistribuicao, fornecedores);

		Assert.assertNotNull(produtoLancamentoDTOs);
	}

	@Test
	public void buscarUltimoBalanceamentoLancamentoRealizadoDia() {

		Date dataOperacao = Fixture.criarData(01, Calendar.NOVEMBER, 2012);

		Date data = lancamentoRepository
				.buscarUltimoBalanceamentoLancamentoRealizadoDia(dataOperacao);

	}

	@Test
	public void buscarDiaUltimoBalanceamentoLancamentoRealizado() {

		Date data = lancamentoRepository
				.buscarDiaUltimoBalanceamentoLancamentoRealizado();
	}

	@Test
	public void buscarUltimoBalanceamentoRecolhimentoRealizadoDia() {

		Date dataOperacao = Fixture.criarData(01, Calendar.NOVEMBER, 2012);

		Date data = lancamentoRepository
				.buscarUltimoBalanceamentoRecolhimentoRealizadoDia(dataOperacao);

	}

	@Test
	public void buscarDiaUltimoBalanceamentoRecolhimentoRealizado() {

		Date data = lancamentoRepository
				.buscarDiaUltimoBalanceamentoRecolhimentoRealizado();
	}

	@Test
	public void obterLancamentoProdutoPorDataLancamentoOuDataRecolhimentoDtLctoPrevista() {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		Date dataLancamentoPrevista = Fixture.criarData(05, Calendar.NOVEMBER,
				2012);

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(
						produtoEdicao, dataLancamentoPrevista, null);
	}

	@Test
	public void obterLancamentoProdutoPorDataLancamentoOuDataRecolhimentoDtRecolhimentoPrevista() {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		Date dataRecolhimentoPrevista = Fixture.criarData(05,
				Calendar.NOVEMBER, 2012);

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoProdutoPorDataLancamentoOuDataRecolhimento(
						produtoEdicao, null, dataRecolhimentoPrevista);
	}

	@Test
	public void obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidorDtLctoPrevista() {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		Date dataLancamentoPrevista = Fixture.criarData(05, Calendar.NOVEMBER,
				2012);

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
						produtoEdicao, dataLancamentoPrevista, null);
	}

	@Test
	public void obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidorDtLctoDistribuidor() {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		Date dataLancamentoDistribuidor = Fixture.criarData(05,
				Calendar.NOVEMBER, 2012);

		Lancamento lancamento = lancamentoRepository
				.obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
						produtoEdicao, null, dataLancamentoDistribuidor);
	}

	@Test
	public void obterQuantidadeLancamentos() {

		StatusLancamento status = StatusLancamento.BALANCEADO;

		lancamentoRepository.obterQuantidadeLancamentos(status);
	}

	@Test
	public void obterConsignadoDia() {

		StatusLancamento status = StatusLancamento.CALCULADO;

		lancamentoRepository.obterConsignadoDia(status);
	}

	@Test
	public void obterLancamentosCanceladosPor() {

		Intervalo<Date> periodo = new Intervalo<Date>();
		List<Long> idFornecedores = new ArrayList<Long>();

		periodo.setDe(Fixture.criarData(05, Calendar.NOVEMBER, 2012));
		periodo.setAte(Fixture.criarData(25, Calendar.NOVEMBER, 2012));

		idFornecedores.add(1L);

		List<ProdutoLancamentoCanceladoDTO> produtoLancamentoCanceladoDTOs = lancamentoRepository
				.obterLancamentosCanceladosPor(periodo, idFornecedores);

		Assert.assertNotNull(produtoLancamentoCanceladoDTOs);

	}

	@Test
	public void obterProximoLancamento() {

		Lancamento proximoLancamentoObtido = this.lancamentoRepository
				.obterProximoLancamento(lancamentoVeja);

		Assert.assertNotNull(proximoLancamentoObtido);

		Assert.assertTrue(proximoLancamentoObtido.getDataLancamentoPrevista()
				.after(lancamentoVeja.getDataLancamentoPrevista()));

		Assert.assertEquals(proximoLancamentoObtido.getId(),
				proximoLancamentoVeja.getId());
	}

}
