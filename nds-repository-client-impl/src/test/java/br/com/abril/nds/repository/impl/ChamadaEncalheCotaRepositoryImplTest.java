package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ChamadaEncalheCotaRepositoryImplTest extends
		AbstractRepositoryImplTest {

	private Lancamento lancamentoVeja;
	private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	private Cota cotaManoel;

	private ItemRecebimentoFisico itemRecebimentoFisico1Veja;
	private ItemRecebimentoFisico itemRecebimentoFisico2Veja;

	private ProdutoEdicao veja1;
	private ProdutoEdicao quatroRoda2;

	private CFOP cfop;
	private TipoNotaFiscal tipoNotaFiscal;
	private Usuario usuario;
	private Date dataRecebimento;

	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;

	@Before
	public void setUpGeral() {
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l, "CROMO", "KG");
		save(ncmCromo);

		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
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

		veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100),
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 
				veja, null, false);

		quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30, new Long(100),
				BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKNOPA", 
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30,
				new Long(100), BigDecimal.TEN, new BigDecimal(12),
				"ABCDGHIJKLMNOPB", infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15,
				new Long(120), BigDecimal.TEN, BigDecimal.TEN,
				"ABCDEFGHIJKLMNU", capricho, null, false);

		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60,
				new Long(10), BigDecimal.ONE, new BigDecimal(1.5), "ABCTU", 
				cromoReiLeao, null, false);

		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);

		usuario = Fixture.usuarioJoao();
		save(usuario);

		cfop = Fixture.cfop5102();
		save(cfop);

		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);
		tipoNotaFiscal.setCfopEstado(cfop);
		tipoNotaFiscal.setCfopOutrosEstados(cfop);
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

		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);

		itemRecebimentoFisico1Veja = Fixture.itemRecebimentoFisico(
				itemNotaFiscal1Veja, recebimentoFisico1Veja,
				BigInteger.valueOf(50));
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

		itemRecebimentoFisico2Veja = Fixture.itemRecebimentoFisico(
				itemNotaFiscal2Veja, recebimentoFisico2Veja,
				BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);

		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisico1Veja, 1);

		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		estoqueProdutoCota = Fixture.estoqueProdutoCota(quatroRoda2,
				cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture
				.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), veja1,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalhe);

		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, false, cotaManoel, BigInteger.TEN);
		save(chamadaEncalheCota);

		/**
		 * CONTROLE CONFERENCIA ENCALHE
		 */
		ControleConferenciaEncalhe controleConferenciaEncalhe = Fixture
				.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);

		/**
		 * CONTROLE CONFERENCIA ENCALHE COTA
		 */
		controleConferenciaEncalheCota = Fixture
				.controleConferenciaEncalheCota(controleConferenciaEncalhe,
						cotaManoel,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						StatusOperacao.CONCLUIDO, usuarioJoao, box1);

		save(controleConferenciaEncalheCota);
		/**
		 * MOVIMENTOS DE ENVIO ENCALHE ABAIXO
		 */
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(8), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				BigInteger.valueOf(8), BigInteger.valueOf(8), veja1);
		save(conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(1, Calendar.MARCH, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(50), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(1, Calendar.MARCH, 2012),
				BigInteger.valueOf(50), BigInteger.valueOf(50), veja1);
		save(conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(2, Calendar.MARCH, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(45), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(2, Calendar.MARCH, 2012),
				BigInteger.valueOf(45), BigInteger.valueOf(45), veja1);
		save(conferenciaEncalhe);

	}

	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;

	@Test
	public void testObterQtdListaChamaEncalheCota() {

		Integer numeroCota = 123;
		Date dataOperacao = Fixture.criarData(28, Calendar.FEBRUARY, 2012);
		Long idProdutoEdicao = veja1.getId();
		boolean indPesquisaCEFutura = true;
		boolean conferido = false;
		boolean postergado = false;

		Long qtde = chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(
				numeroCota, dataOperacao, idProdutoEdicao, indPesquisaCEFutura,
				conferido, postergado);

		Assert.assertEquals(1, qtde.intValue());

	}

	// TESTES SEM USO DE MASSA--------------------------

	@Test
	public void testarObterReparteDaChamaEncalheCota() {

		Integer numeroCota = 1;

		Calendar now = Calendar.getInstance();
		Date dataOperacao = now.getTime();
		
		boolean conferido = false;
		boolean postergado = false;

		chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(
			numeroCota, dataOperacao,
			conferido, postergado);

		Assert.assertTrue(true);
	}

	@Test
	public void testarBuscarPorChamadaEncalheECota() {

		ChamadaEncalheCota chamadaEncalheCota;

		Long idChamadaEncalhe = 1L;
		Long idCota = 1L;

		chamadaEncalheCota = chamadaEncalheCotaRepository
				.buscarPorChamadaEncalheECota(idChamadaEncalhe, idCota);

		Assert.assertNull(chamadaEncalheCota);

	}

	@Test
	public void testarObterQntExemplaresComProgramacaoAntecipadaEncalheCota() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	@Test
	public void testarObterQntCotasProgramadaParaAntecipacoEncalhe() {

		Long qntCotas;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();

		qntCotas = chamadaEncalheCotaRepository
				.obterQntCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(qntCotas);

	}

	@Test
	public void testarObterCotasProgramadaParaAntecipacoEncalhe() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// ObterCotasProgramadaParaAntecipacoEncalhe() teste de parametros
	// NumeroCota
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheNumeroCota() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setNumeroCota(1);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// Fornecedor
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheFornecedor() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setFornecedor(1L);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// Box
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheBox() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setBox(1L);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// Rota
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheRota() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRota(1L);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// Roteiro
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheRoteiro() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRoteiro(1L);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// CodMunicipio
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheCodMunicipio() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodMunicipio(1);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// CodTipoPontoPDV
	@Test
	public void testarGetParametrosCotasProgramadaParaAntecipacaoEncalheCodTipoPontoPDV() {

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodTipoPontoPDV(1L);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// ObterCotasProgramadaParaAntecipacoEncalhe() teste OrderBy
	// OrdenacaoColuna - BOX
	@Test
	public void testarGetOrderByCotasProgramadaParaAntecipacaoEncalheOrdenacaoColunaBOX() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cota.numeroCota");

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.BOX);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// OrdenacaoColuna - NOME_COTA
	@Test
	public void testarGetOrderByCotasProgramadaParaAntecipacaoEncalheOrdenacaoColunaNOMECOTA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cota.numeroCota");

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_COTA);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// OrdenacaoColuna - NUMERO_COTA
	@Test
	public void testarGetOrderByCotasProgramadaParaAntecipacaoEncalheOrdenacaoColunaNUMEROCOTA() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cota.numeroCota");

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_COTA);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// OrdenacaoColuna - QNT_EXEMPLARES
	@Test
	public void testarGetOrderByCotasProgramadaParaAntecipacaoEncalheOrdenacaoColunaQNTEXEMPLARES() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "cota.numeroCota");

		List<ChamadaAntecipadaEncalheDTO> cotasProgramada;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColuna.QNT_EXEMPLARES);

		cotasProgramada = chamadaEncalheCotaRepository
				.obterCotasProgramadaParaAntecipacoEncalhe(filtro);

		Assert.assertNotNull(cotasProgramada);

	}

	// Chamada SQL testes de parametro
	// Fornecedor
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheFornecedor() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setFornecedor(1L);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// CodMunicipio
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheCodMunicipio() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodMunicipio(1);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// NumeroCota
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheNumeroCota() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setNumeroCota(1);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// Box
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheBox() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setBox(1L);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// Rota
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheRota() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRota(1L);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// Roteiro
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheRoteiro() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRoteiro(1L);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	// CodTipoPontoPDV
	@Test
	public void testarGetSqlFromEWhereCotasProgramadaParaAntecipacaoEncalheCodTipoPontoPDV() {

		BigDecimal qntExemplares;

		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodTipoPontoPDV(1L);

		qntExemplares = chamadaEncalheCotaRepository
				.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);

		// Assert.assertNull(qntExemplares);

	}

	@Test
	public void testarObterChamadaEncalheCota() {

		Long obterChamada;

		Long idChamadaEncalhe = 1L;

		obterChamada = chamadaEncalheCotaRepository
				.obterQntChamadaEncalheCota(idChamadaEncalhe);

		Assert.assertNotNull(obterChamada);

	}
	
	@Test
	public void testarObterListaChamadaEncalheCota() {
	
		Long idCota = cotaManoel.getId();
		
		Long idProdutoEdicao = veja1.getId();
		
		chamadaEncalheCotaRepository.obterListaChamadaEncalheCota(idCota, idProdutoEdicao);
		
	}

}