package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;


public class MovimentoEstoqueCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepositoryImpl tipoMovimentoEstoqueRepository;
	
	private Lancamento lancamentoVeja;
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	private Cota cotaManoel;
	private Cota cotaValdomiro;
	
	private Box box1;
	private Box box2;
	
	private Rota rota1;
	private Rota rota2;
	
	private ItemRecebimentoFisico itemRecebimentoFisico1Veja;
	private ItemRecebimentoFisico itemRecebimentoFisico2Veja;
	
	private ProdutoEdicao veja1;
	private ProdutoEdicao quatroRoda2;
	
	
	private CFOP cfop;
	private NaturezaOperacao tipoNotaFiscal;
	private Usuario usuario;
	private Date dataRecebimento;
	

	private PessoaFisica valdomiro;
	private Box box;
	private EstoqueProdutoCota estoqueProdutoCota;

	private TipoMovimentoEstoque tipoMovimetnoEstoque1;
	private TipoMovimentoEstoque tipoMovimetnoEstoque2;
	private TipoMovimentoEstoque tipoMovimetnoEstoque3;
	private TipoMovimentoEstoque tipoMovimetnoEstoque4;
	private TipoMovimentoEstoque tipoMovimetnoEstoque5;

	private MovimentoEstoqueCota movimentoEstoque1;
	private MovimentoEstoqueCota movimentoEstoque2;
	private MovimentoEstoqueCota movimentoEstoque3;
	private MovimentoEstoqueCota movimentoEstoque4;
	private MovimentoEstoqueCota movimentoEstoque5;
	private MovimentoEstoqueCota movimentoEstoque6;
	private MovimentoEstoqueCota movimentoEstoque7;
	
	private TipoMovimentoFinanceiro tipoMovimetnoFinanceiro;
	private MovimentoFinanceiroCota movimentoFinanceiro;

	@Before
	public void setUpGeral() {
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
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
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", veja, null, false);


		quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30, new Long(100),
				BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA", quatroRodas,
				null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30, new Long(100),
				BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPB", infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15, new Long(120),
				BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPC", capricho, null, false);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60, new Long(10),
				BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOP", cromoReiLeao, null, false);
		
		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
		
		cfop = Fixture.cfop5102();
		save(cfop);
		
		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);
		tipoNotaFiscal.setCfopEstado("5102");
		tipoNotaFiscal.setCfopOutrosEstados("5102");
		save(tipoNotaFiscal);
		
		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal1Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);
		
		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
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
			
		itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);
		
	}
	
	
	public void setUpForContagemDevolucao() {
		
		NotaFiscalEntradaFornecedor notaFiscal4Rodas= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
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
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, 
				veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, itemRecebimentoFisico1Veja, 1);
		
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
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(
				quatroRoda2, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);

		
		TipoMovimentoEstoque tipoMovJorn = Fixture.tipoMovimentoEnvioJornaleiro();
		save(tipoMovJorn);
		
		MovimentoEstoqueCota mecJorn = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovJorn, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(12), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mecJorn);
		
		/**
		 * CHAMADA ENCALHE
		 */
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalhe);
		
		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, 
				false, 
				cotaManoel, 
				BigInteger.TEN);
		save(chamadaEncalheCota);
		
		/**
		 * CONTROLE CONFERENCIA ENCALHE 
		 */
		ControleConferenciaEncalhe controleConferenciaEncalhe = 
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);
		
		
		/**
		 * CONTROLE CONFERENCIA ENCALHE COTA
		 */
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalhe, 
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
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(12), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		
		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(12),BigInteger.valueOf(12), veja1);
		
		
		
		save(conferenciaEncalhe);
		

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(25), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(25),BigInteger.valueOf(25), veja1);
		save(conferenciaEncalhe);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(14), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota
				,Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(14),BigInteger.valueOf(14), veja1);
		save(conferenciaEncalhe);

		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(19),BigInteger.valueOf(19), veja1);
		save(conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(1, Calendar.MARCH, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(19), BigInteger.valueOf(19), veja1);
		save(conferenciaEncalhe);

		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(1, Calendar.MARCH, 2012),
				quatroRoda2,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(19),BigInteger.valueOf(19), quatroRoda2);
		save(conferenciaEncalhe);

		
		ControleContagemDevolucao controleContagemDevolucao = Fixture.controleContagemDevolucao(
				StatusOperacao.CONCLUIDO, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1);

		save(controleContagemDevolucao);		
	}
	

	public void setUpForMapaAbastecimento() {		
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		PessoaFisica pedro = Fixture.pessoaFisica("124.456.789-00",
				"pedro@mail.com", "Pedro Alvares");
		save(pedro);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		save(box2);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		PDV pdv = Fixture.criarPDVPrincipal("Meu PDV", cotaManoel);
		save(pdv);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(box1);
		save(roteirizacao);
		
		PDV pdv2 = Fixture.criarPDVPrincipal("Meu PDV", cotaManoel);
		save(pdv2);
		
		Roteirizacao roteirizacao2 = Fixture.criarRoteirizacao(box2);
		save(roteirizacao2);
		
		Roteiro roteiro1 = Fixture.criarRoteiro("",roteirizacao, TipoRoteiro.NORMAL);
		Roteiro roteiro2 = Fixture.criarRoteiro("",roteirizacao2, TipoRoteiro.NORMAL);
		save(roteiro1,roteiro2);
		
		rota1 = Fixture.rota("Rota 1", roteiro1);
		rota1.addPDV(pdv, 1, box1);
		rota2 = Fixture.rota("Rota 1", roteiro2);
		rota2.addPDV(pdv2, 1, box2);
		rota1.setRoteiro(roteiro1);
		rota2.setRoteiro(roteiro2);
		save(rota1,rota2);
		
		Cota cotaPedro = Fixture.cota(124, pedro, SituacaoCadastro.ATIVO, box2);
		save(cotaPedro);
		
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(
				quatroRoda2, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
	
		
		EstoqueProdutoCota estoqueProdutoPedro = Fixture.estoqueProdutoCota(
				veja1, cotaPedro, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoPedro);
		
		estoqueProdutoPedro = Fixture.estoqueProdutoCota(
				quatroRoda2, cotaPedro, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoPedro);
		
		
		TipoMovimentoEstoque tipoMovRecebReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovRecebReparte);
		
		MovimentoEstoqueCota mec0 = Fixture.movimentoEstoqueCota(veja1, tipoMovRecebReparte, usuarioJoao, 
				estoqueProdutoCota, BigInteger.valueOf(50), cotaManoel, StatusAprovacao.APROVADO, "");
		save(mec0);
		
		MovimentoEstoqueCota mec1 = Fixture.movimentoEstoqueCota(quatroRoda2, tipoMovRecebReparte, usuarioJoao, 
				estoqueProdutoCota, BigInteger.valueOf(50), cotaManoel, StatusAprovacao.APROVADO, "");
		save(mec1);
		
		
		MovimentoEstoqueCota mec2 = Fixture.movimentoEstoqueCota(veja1, tipoMovRecebReparte, usuarioJoao, 
				estoqueProdutoCota, BigInteger.valueOf(100), cotaPedro, StatusAprovacao.APROVADO, "");
		save(mec2);
		
		MovimentoEstoqueCota mec3 = Fixture.movimentoEstoqueCota(quatroRoda2, tipoMovRecebReparte, usuarioJoao, 
				estoqueProdutoCota, BigInteger.valueOf(100), cotaPedro, StatusAprovacao.APROVADO, "");
		save(mec3);
			
	}
	
	
	public void setUpForConsultaEncalhe() {
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, 
				veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, itemRecebimentoFisico1Veja, 1);
		
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		save(lancamentoVeja);
		
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		chamadaEncalhe.setLancamentos(new HashSet<Lancamento>());
		chamadaEncalhe.getLancamentos().add(lancamentoVeja);
		
		
		save(chamadaEncalhe);
		
//		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
//				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

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
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(
				quatroRoda2, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);
		
		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, 
				false, 
				cotaManoel, 
				BigInteger.TEN);
		save(chamadaEncalheCota);
		
		/**
		 * CONTROLE CONFERENCIA ENCALHE 
		 */
		ControleConferenciaEncalhe controleConferenciaEncalhe = 
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);
		
		
		/**
		 * CONTROLE CONFERENCIA ENCALHE COTA
		 */
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalhe, 
				cotaManoel, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				StatusOperacao.CONCLUIDO, 
				usuarioJoao, 
				box1);
		
		save(controleConferenciaEncalheCota);		
		/**
		 * MOVIMENTOS DE ENVIO ENCALHE ABAIXO
		 */
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(8), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),BigInteger.valueOf(8),BigInteger.valueOf(8), veja1);
		
		conferenciaEncalhe.setObservacao("Nova observ");
		
		save(conferenciaEncalhe);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(1, Calendar.MARCH, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(50), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(1, Calendar.MARCH, 2012), BigInteger.valueOf(50), BigInteger.valueOf(50), veja1);
		save(conferenciaEncalhe);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(2, Calendar.MARCH, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(45), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
	
		conferenciaEncalhe = Fixture.conferenciaEncalhe(
				mec, 
				chamadaEncalheCota, 
				controleConferenciaEncalheCota,
				Fixture.criarData(2, Calendar.MARCH, 2012),
				BigInteger.valueOf(45), BigInteger.valueOf(45), veja1);
		save(conferenciaEncalhe);
			
	}
	
	public void setUpForNotaFiscal() {
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.LANCAMENTO, 
				veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, itemRecebimentoFisico1Veja, 1);
		
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		
		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(1000, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(100),
				BigInteger.valueOf(22), estudo, cotaManoel);
		save(estudoCota);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(
				quatroRoda2, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoEnvioEncalhe);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(8), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		mec.setLancamento(lancamentoVeja);
		save(mec);
		
	}
	
	@Test
	public void obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe() {
		
		Long idControleConferenciaEncalheCota = 1L;
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas = movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(idControleConferenciaEncalheCota);
		
		Assert.assertNotNull(listaMovimentoEstoqueCotas);
		
	}
	
	@Test
	public void obterQtdConsultaEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = null; //obterFiltroConsultaEncalhe();
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
		Assert.assertTrue(qtde > 0);
	}
	
	
	@Test
	public void testObterValorEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = null; //obterFiltroConsultaEncalhe();
		
		BigDecimal valorEncalhe = movimentoEstoqueCotaRepository.obterValorTotalEncalhe(filtro);
		
		Assert.assertNotNull(valorEncalhe);
	}
	
	
	
	@Test
	public void obterQtdConsultaEncalheNulo() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
		Assert.assertNotNull(qtde > 0);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterQtdConsultaEncalhePorIdCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdCota(1L);
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
	}
	
	@Test
	public void testObterConsultaEncalhe() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		Date dataInicial = new GregorianCalendar(2013, 10, 14).getTime();
		Date dataFinal   = new GregorianCalendar(2013, 10, 19).getTime();
		
		filtro.setDataRecolhimentoInicial(dataInicial);
		filtro.setDataRecolhimentoFinal(dataFinal);
		
		List<ConsultaEncalheDTO> consultaEncalhe = 
				this.movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(consultaEncalhe);
	}

}
