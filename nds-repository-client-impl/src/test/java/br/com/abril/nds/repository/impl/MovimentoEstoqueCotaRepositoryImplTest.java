package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO.OrdenacaoColunaDetalhe;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
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
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


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
	private TipoNotaFiscal tipoNotaFiscal;
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
		tipoNotaFiscal.setCfopEstado(cfop);
		tipoNotaFiscal.setCfopOutrosEstados(cfop);
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
				TipoLancamento.SUPLEMENTAR, 
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
				TipoLancamento.SUPLEMENTAR, 
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
				TipoLancamento.SUPLEMENTAR, 
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

		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();

		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);

		Assert.assertEquals(3, qtde.intValue());
	}
	
	@Test
	public void obterQtdConsultaEncalheNulo() {
		
		setUpForConsultaEncalhe();

		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));

		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);

		Assert.assertEquals(3, qtde.intValue());
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
	@SuppressWarnings("unused")
	public void obterQtdConsultaEncalhePorIdFornecedor() {
		setUpForConsultaEncalhe();
		

		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdFornecedor(1L);

		Integer qtde = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
	}

	@Test
	public void obterListaConsultaEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
		
		ConsultaEncalheDTO cEncalhe_1 = listaConsultaEncalhe.get(0);
		Assert.assertEquals((8*15), cEncalhe_1.getValor().intValue());
		
	}

	@Test
	public void obterListaConsultaEncalhePorIdCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdCota(1L);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalhePorFornecedor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdFornecedor(1L);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoCodigoProduto() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.CODIGO_PRODUTO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoNomeProduto() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.NOME_PRODUTO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoNumeroEdicao() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.NUMERO_EDICAO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoPrecoCapa() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.PRECO_CAPA);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoPrecoComDesconto() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.PRECO_COM_DESCONTO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoReparte() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.REPARTE);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.ENCALHE);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoFornecedor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.FORNECEDOR);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoValor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.VALOR);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoValorComDesconto() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.VALOR_COM_DESCONTO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenadoRecolhimento() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.RECOLHIMENTO);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheOrdenacaoDESC() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.CODIGO_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.DESC);

		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalhe);
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo() {
		
		Long idCota = 1L;
		Long idProdutoEdicao = 1L; 
		Date dataInicial = Fixture.criarData(01, Calendar.NOVEMBER, 2012);
		Date dataFinal = Fixture.criarData(31, Calendar.NOVEMBER, 2012);
		OperacaoEstoque operacaoEstoque = OperacaoEstoque.SAIDA;
		
		BigInteger QtdeMovimentoEstoqueCota = movimentoEstoqueCotaRepository.obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(idCota, idProdutoEdicao, dataInicial, dataFinal, operacaoEstoque);		
		
	}

	@Test
	@SuppressWarnings("unused")
	public void obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo() {
		
		Long idCota = 1L;
		Long idProdutoEdicao = 1L; 
		Date dataInicial = Fixture.criarData(01, Calendar.NOVEMBER, 2012);
		Date dataFinal = Fixture.criarData(31, Calendar.NOVEMBER, 2012);
		OperacaoEstoque operacaoEstoque = OperacaoEstoque.SAIDA;
		
		BigDecimal QtdeMovimentoEstoqueCota = movimentoEstoqueCotaRepository.obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(idCota, this.fornecedorDinap.getId(), idProdutoEdicao, dataInicial, dataFinal, operacaoEstoque);		
		
	}

	@Test
	public void obterListaConsultaEncalheDetalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
		int tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, listaConsultaEncalheDetalhe.size());
		
	}

	@Test
	public void obterListaConsultaEncalheDetalhePorCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());
		filtro.setIdCota(cotaManoel.getId());

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheDetalhePorFornecedor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());
		filtro.setIdFornecedor(fornecedorDinap.getId());

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheDetalheOrdenadoNumeroCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColunaDetalhe(OrdenacaoColunaDetalhe.NUMERO_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheDetalheOrdenadoNomeCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColunaDetalhe(OrdenacaoColunaDetalhe.NOME_COTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheDetalheOrdenadoObservacao() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setOrdenacaoColunaDetalhe(OrdenacaoColunaDetalhe.OBSERVACAO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterListaConsultaEncalheDetalheComPaginacao() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);

		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(listaConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterQtdeConsultaEncalheDetalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());

		Integer qtdeConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(qtdeConsultaEncalheDetalhe);
		
		Integer tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, qtdeConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterQtdeConsultaEncalheDetalhePorIdCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());
		filtro.setIdCota(cotaManoel.getId());

		Integer qtdeConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(qtdeConsultaEncalheDetalhe);
		
	}

	@Test
	public void obterQtdeConsultaEncalheDetalhePorIdFornecedor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		filtro.setDataMovimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdProdutoEdicao(veja1.getId());
		filtro.setIdFornecedor(fornecedorDinap.getId());

		Integer qtdeConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalheDetalhe(filtro);
		
		Assert.assertNotNull(qtdeConsultaEncalheDetalhe);
		
		Integer tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, qtdeConsultaEncalheDetalhe);
		
	}

	private FiltroConsultaEncalheDTO obterFiltroConsultaEncalhe() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdCota(cotaManoel.getId());
		filtro.setIdFornecedor(fornecedorDinap.getId());
		filtro.setOrdenacaoColuna(FiltroConsultaEncalheDTO.OrdenacaoColuna.RECOLHIMENTO);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		
		paginacao.setPaginaAtual(1);
		
		paginacao.setQtdResultadosPorPagina(1000);
		
		filtro.setPaginacao(paginacao);
		
		return filtro;
	}
	
	@Test
	public void testarObterListaContagemDevolucaoComQtdMovimentoParcial() {
		
		setUpForContagemDevolucao();
		
		List<ContagemDevolucaoDTO> retorno = 
				
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao(),
				true);
		
		
		Assert.assertEquals(2, retorno.size());
		
		ContagemDevolucaoDTO contagem = retorno.get(0);
		
		Assert.assertEquals(19, contagem.getQtdDevolucao().intValue());
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoNulo() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoPorIdFornecedor() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setIdFornecedor(1L);
		
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoComPagianacao() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(500);
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoCodigoProduto() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.CODIGO_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoNomeProduto() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.NOME_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoNumeroEdicao() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.NUMERO_EDICAO);
		filtro.setPaginacao(new PaginacaoVO());
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoPrecoCapa() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.PRECO_CAPA);
		filtro.setPaginacao(new PaginacaoVO());
		
		 
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoQuantidadeDevolucao() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.QTD_DEVOLUCAO);
		filtro.setPaginacao(new PaginacaoVO());
		
		 
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoQuantidadeNota() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.QTD_NOTA);
		filtro.setPaginacao(new PaginacaoVO());
		
		boolean indBuscaTotalParcial = true;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}

	
	@Test
	public void testarObterListaContagemDevolucaoOrdenadoDESC() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		filtro.setPeriodo(new Intervalo<Date>(Fixture.criarData(27, Calendar.FEBRUARY, 2012), Fixture.criarData(1, Calendar.MARCH, 2012)));
		filtro.setOrdenacaoColuna(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.CODIGO_PRODUTO);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		
		boolean indBuscaTotalParcial = false;
		
		List<ContagemDevolucaoDTO> retorno = 
				movimentoEstoqueCotaRepository.obterListaContagemDevolucao(filtro, indBuscaTotalParcial);
		
		Assert.assertNotNull(retorno);
		
	}

	@Test
	public void testarObterValorTotal() {
		
		setUpForContagemDevolucao();
		
		BigDecimal total = movimentoEstoqueCotaRepository.obterValorTotalGeralContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao());
		
		Assert.assertEquals(475, total.intValue());
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testarObterValorTotalNulo() {
		
		setUpForContagemDevolucao();
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = obterFiltroDigitacaoContagemDevolucao();
		filtro.setIdFornecedor(null);
		
		BigDecimal total = movimentoEstoqueCotaRepository.obterValorTotalGeralContagemDevolucao(
				filtro);
		
	}
	
	@Test
	public void testarObterListaContagemDevolucao() {
		
		setUpForContagemDevolucao();
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao(),
				false);
		
		Assert.assertNotNull(listaContagemDevolucao);
	}
	
	@Test
	public void testarObterQuantidadeContagemDevolucao() {
		
		setUpForContagemDevolucao();
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao());
		
		Assert.assertEquals(2, qtde.intValue());
	}
	
	private FiltroDigitacaoContagemDevolucaoDTO obterFiltroDigitacaoContagemDevolucao() {
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);

		Date dataInicial = Fixture.criarData(27, Calendar.FEBRUARY, 2012);
		Date dataFinal = Fixture.criarData(1, Calendar.MARCH, 2012);
		
	
		Intervalo<Date> periodo = new Intervalo<Date>(dataInicial, dataFinal);
		filtro.setPeriodo(periodo);
		
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);
		
		filtro.setIdFornecedor(fornecedorDinap.getId());
		
		return filtro;
		
	}
	
	
	
	
	@Test
	public void obterMovimentoPorTipo(){
		
		setUpForContagemDevolucao();
		
		Date data = Fixture.criarData(28, Calendar.FEBRUARY, 2012);
		
		PessoaFisica manoel = Fixture.pessoaFisica("223.556.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(45, "BX-045", TipoBox.LANCAMENTO);
		save(box1);
		
		
		List<MovimentoEstoqueCota> listaMovimento = movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cotaManoel.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		Assert.assertTrue(listaMovimento.size() == 1);
	}
	
	@Test
	public void obterDadosAbastecimentoSemFiltros() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "box"));
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);
	}	
	
	@Test
	public void obterDadosAbastecimentoComFiltros() {
		
		setUpForMapaAbastecimento();
		
		List<String> codigosProduto = new ArrayList<String>();
		
		codigosProduto.add(veja1.getProduto().getCodigo());
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "box"));
		filtro.setBox(box1.getId());
		filtro.setRota(rota1.getId());
		filtro.setCodigosProduto(codigosProduto);
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorIdEntregador() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setIdEntregador(1L);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorData() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setDataDate(new Date());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorBox() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setBox(box1.getId());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorRota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setRota(rota1.getId());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorRoteiro() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setRoteiro(1L);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoporCodigosProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setCodigosProduto(new ArrayList<String>());
		filtro.getCodigosProduto().add(veja1.getProduto().getCodigo());
		filtro.getCodigosProduto().add(quatroRoda2.getProduto().getCodigo());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorEdicaoProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setEdicaoProduto(1L);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoPorCodigoCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoExcluindoProdutoSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.setExcluirProdutoSemReparte(false);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoBox() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoTotalProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("totalProduto");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoTotalReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("totalReparte");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoPorTotalBox() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("totalBox");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoPorCodigoCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("codigoCota");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoPorNomeCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("nomeCota");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoPorReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("reparte");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoOrdenacaoPorMateiralPromocional() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("materialPromocional");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	public void obterDadosAbastecimentoComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("box");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertNotNull(listaMovimento);	
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterDadosAbastecimento() {
		
		setUpForMapaAbastecimento();
		
		List<String> codigosProduto = new ArrayList<String>();
		
		codigosProduto.add(veja1.getProduto().getCodigo());
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "box"));
		filtro.setBox(box1.getId());
		filtro.setRota(rota1.getId());
		filtro.setCodigosProduto(codigosProduto);
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		Long count = movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);

	}	
	
	@Test
	@SuppressWarnings("unused")
	public void countObterDadosAbastecimentoNulo() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
	
		Long count = movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);

	}	
	
	@Test
	@SuppressWarnings("unused")
	public void countObterDadosAbastecimentoSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
	
		Long count = movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);

	}	
	
	@Test
	public void obterDetalhesAbastecimento() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacaoDetalhes(new PaginacaoVO(1, 10, "asc", "codigoProduto"));
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("codigoProduto");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoCodigoProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("codigoProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoNomeProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("nomeProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoNumeroEdicao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("numeroEdicao");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("reparte");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoPrecoCapa() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("precoCapa");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);
		Assert.assertNotNull(listaMovimento);

	}
	
	@Test
	public void obterDetalhesAbastecimentoOrdenacaoTotal() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacaoDetalhes(new PaginacaoVO());
		filtro.getPaginacaoDetalhes().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacaoDetalhes().setSortColumn("total");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(box1.getId(), filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoBox");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxOrdenacaoCodigoBox() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoBox");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxOrdenacaoCodigoProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxOrdenacaoNomeProduto() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("nomeProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxOrdenacaoNumeroEdicao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("numeroEdicao");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxOrdenacaoPrecoCapa() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("precoCapa");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxRotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxRotaOrdenacaoCodigoCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);

		Assert.assertNotNull(listaMovimento);

	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxRotaOrdenacaoCodigoRota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoRota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxRotaOrdenacaoMaterialPromocional() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("materialPromocional");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorBoxRotaComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorBoxRota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorBoxRota(filtro);

	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorBoxRotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorBoxRota(filtro);

	}
	
	@Test
	public void obterMapaAbastecimentoPorProdutoEdicao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoBox");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorProdutoEdicaoSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoBox");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorProdutoEdicaoComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoBox");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorProdutoEdicao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorProdutoEdicaoSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

	}
	
	@Test
	public void obterMapaAbastecimentoPorCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorCotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaAbastecimentoPorCotaComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorCota(filtro);

	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaAbastecimentoPorCotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorCota(filtro);

	}
	
	@Test
	public void obterMapaDeImpressaoPorProdutoQuebrandoPorCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaDeImpressaoPorProdutoQuebrandoPorCotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaDeImpressaoPorProdutoQuebrandoPorCotaComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaDeImpressaoPorProdutoQuebrandoPorCota() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);

	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaDeImpressaoPorProdutoQuebrandoPorCotaSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);

	}
	
	@Test
	public void obterMapaDeAbastecimentoPorEntregador() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		filtro.setIdEntregador(1L);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeAbastecimentoPorEntregador(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaDeAbastecimentoPorEntregadorSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		filtro.setIdEntregador(1L);
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeAbastecimentoPorEntregador(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaDeAbastecimentoPorEntregadorComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoProduto");
		filtro.setIdEntregador(1L);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeAbastecimentoPorEntregador(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void countObterMapaDeAbastecimentoPorEntregador() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaDeAbastecimentoPorEntregador(filtro);

	}
	
	@SuppressWarnings("unused")
	public void countObterMapaDeAbastecimentoPorEntregadorSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setExcluirProdutoSemReparte(true);
		
		Long totalMovimento = 
				movimentoEstoqueCotaRepository.countObterMapaDeAbastecimentoPorEntregador(filtro);

	}
	
	@Test
	public void obterMapaDeImpressaoPorEntregador() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);

		Assert.assertNotNull(listaMovimento);
	}
	
	@Test
	public void obterMapaDeImpressaoPorEntregadorSemReparte() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		filtro.setExcluirProdutoSemReparte(true);
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);
		
		Assert.assertNotNull(listaMovimento);

	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterMapaDeImpressaoPorEntregadorComPaginacao() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setSortColumn("codigoCota");
		
		List<ProdutoAbastecimentoDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);

	}
	
	public void obterMovimentoCotasPorTipoMovimento() {
		
		Date data = Fixture.criarData(1, Calendar.NOVEMBER, 2012);
		List<Integer> numCotas = new ArrayList<Integer>();
		numCotas.add(1);
		GrupoMovimentoEstoque grupoMovimentoEstoque = GrupoMovimentoEstoque.ENVIO_ENCALHE;
		
		List<MovimentoEstoqueCotaDTO> listaMovimento = 
				movimentoEstoqueCotaRepository.obterMovimentoCotasPorTipoMovimento(data, numCotas, grupoMovimentoEstoque);
		
		Assert.assertNotNull(listaMovimento);

	}
	
	@Test
	public void obterMovimentoEstoqueCotaPorReparte() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		
		List<Long> listaProdutos =  new ArrayList<Long>();
		listaProdutos.add(veja1.getProduto().getId());
		
		List<Long> listaFornecedores =  new ArrayList<Long>();
		for (Fornecedor fornecedor: veja1.getProduto().getFornecedores()) {
			listaFornecedores.add(fornecedor.getId());
		}
		
		Intervalo<Date> periodo = new Intervalo<Date>();
		periodo.setDe(DateUtil.parseData("01/01/2012", "dd/MM/yyyy"));
		periodo.setAte(DateUtil.parseData("01/01/2013", "dd/MM/yyyy"));
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, periodo, listaFornecedores, listaProdutos);

		int tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, listaMovimentoEstoqueCota.size());
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorEncalhe() {
		
		setUpForConsultaEncalhe();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		
		Intervalo<Date> periodo = new Intervalo<Date>();
		periodo.setDe(DateUtil.parseData("01/01/2012", "dd/MM/yyyy"));
		periodo.setAte(DateUtil.parseData("01/01/2013", "dd/MM/yyyy"));
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, periodo, null, null);

		int tamanhoEsperado = 3;
		
		Assert.assertEquals(tamanhoEsperado, listaMovimentoEstoqueCota.size());
		
	}
	
	@Test
	public void obterMovimentoEstoqueCotaPorReparteEncalhe() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		
		Intervalo<Date> periodo = new Intervalo<Date>();
		periodo.setDe(DateUtil.parseData("01/01/2012", "dd/MM/yyyy"));
		periodo.setAte(DateUtil.parseData("01/01/2013", "dd/MM/yyyy"));
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, periodo, null, null);
		
		Assert.assertNotNull(listaMovimentoEstoqueCota);

	}

	@Test
	public void obterMovimentoEstoqueCotaPorPeriodo() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		Intervalo<Date> periodo = new Intervalo<Date>();
		periodo.setDe(DateUtil.parseData("01/01/2012", "dd/MM/yyyy"));
		periodo.setAte(DateUtil.parseData("01/01/2013", "dd/MM/yyyy"));

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorGruposMovimentoEstoque() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, 
						cotaManoel.getId(), GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorFornecedores() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<Long> listaFornecedores =  new ArrayList<Long>();
		for (Fornecedor fornecedor: veja1.getProduto().getFornecedores()) {
			listaFornecedores.add(fornecedor.getId());
		}
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor,
						cotaManoel.getId(), GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, null, null, listaFornecedores, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorPrimeiroDiaRecolhimento() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorSegundoDiaRecolhimento() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(),
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorTerceiroDiaRecolhimento() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorQuartoDiaRecolhimento() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(true);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorQuintoDiaRecolhimento() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(true);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, null, null, null, null);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterMovimentoEstoqueCotaPorProdutos() {
		
		setUpForNotaFiscal();
		
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = new ParametrosRecolhimentoDistribuidor();
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoPrimeiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoSegundo(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoTerceiro(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuarto(false);
		parametrosRecolhimentoDistribuidor.setDiaRecolhimentoQuinto(false);

		List<Long> listaProdutos =  new ArrayList<Long>();
		listaProdutos.add(veja1.getProduto().getId());
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, cotaManoel.getId(), 
						GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, null, null, null, listaProdutos);

		Assert.assertNotNull(listaMovimentoEstoqueCota);
		
	}

	@Test
	public void obterQuantidadeProdutoEdicaoMovimentadoPorCota() {
		
		setUpForMapaAbastecimento();
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		TipoMovimentoEstoque tipoMovimentoCota =
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		Long qtde = movimentoEstoqueCotaRepository.obterQuantidadeProdutoEdicaoMovimentadoPorCota(cotaManoel.getId(), veja1.getId(), tipoMovimentoCota.getId());
		
		Assert.assertTrue(qtde.equals(50L));
	}
	
	@Test
	public void obterValorTotalReparte() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));

		ConsultaEncalheRodapeDTO valoresTotais = movimentoEstoqueCotaRepository.obterValoresTotais(filtro);
		
		Assert.assertNotNull(valoresTotais);
		
	}
	
	@Test
	public void obterValorTotalRepartePorCota() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdCota(cotaManoel.getId());

		ConsultaEncalheRodapeDTO valoresTotais = movimentoEstoqueCotaRepository.obterValoresTotais(filtro);
		
		Assert.assertNotNull(valoresTotais);
		
	}
	
	@Test
	public void obterValorTotalRepartePorFornecedor() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimentoInicial(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setDataRecolhimentoFinal(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		filtro.setIdFornecedor(fornecedorDinap.getId());

		ConsultaEncalheRodapeDTO valoresTotais = movimentoEstoqueCotaRepository.obterValoresTotais(filtro);
		
		Assert.assertNotNull(valoresTotais);
		
	}
	
	public void setupFinanceiroReparteEncalhe(){
		
		valdomiro = Fixture.pessoaFisica("123.456.789-00",
				"valdomiro@mail.com", "Valdomiro");
		save(valdomiro);
		
		box = Fixture.criarBox(100, "BX-500", TipoBox.LANCAMENTO);
		save(box);
		
		cotaValdomiro = Fixture.cota(5637, valdomiro, SituacaoCadastro.ATIVO, box);
		save(cotaValdomiro);

		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(veja1, cotaValdomiro, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
	
		
		tipoMovimetnoEstoque1 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque1.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		save(tipoMovimetnoEstoque1);
	
		tipoMovimetnoEstoque2 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque2.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE);
		save(tipoMovimetnoEstoque2);
		
		tipoMovimetnoEstoque3 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque3.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);
		save(tipoMovimetnoEstoque3);
		
		tipoMovimetnoEstoque4 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque4.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		save(tipoMovimetnoEstoque4);
		
		tipoMovimetnoEstoque5 = Fixture.tipoMovimentoEnvioEncalhe();
		tipoMovimetnoEstoque5.setGrupoMovimentoEstoque(GrupoMovimentoEstoque.COMPRA_ENCALHE);
		save(tipoMovimetnoEstoque5);
		

		movimentoEstoque1 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque1);
		
		movimentoEstoque2 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque2);
		
		movimentoEstoque3 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque1, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque3);
		
		movimentoEstoque4 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque2, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque4);
		
		movimentoEstoque5 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque3, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque5);
		
		movimentoEstoque6 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque4, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque6);
		
		movimentoEstoque7 = Fixture.movimentoEstoqueCota(veja1, tipoMovimetnoEstoque5, usuario, estoqueProdutoCota, BigInteger.TEN, cotaValdomiro, StatusAprovacao.APROVADO, "");
		save(movimentoEstoque7);
		
		
		tipoMovimetnoFinanceiro = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();
		save(tipoMovimetnoFinanceiro);
		
		movimentoFinanceiro = Fixture.movimentoFinanceiroCota(cotaValdomiro, tipoMovimetnoFinanceiro, usuario, new BigDecimal(150), Arrays.asList(movimentoEstoque1), StatusAprovacao.APROVADO, new Date(), false);
		save(movimentoFinanceiro);

		movimentoEstoque1.setStatusEstoqueFinanceiro(StatusEstoqueFinanceiro.FINANCEIRO_PROCESSADO);
		save(movimentoEstoque1);
	}
	
	@Test
	public void obterMovimentosPendentesGerarFinanceiro(){
		
		this.setupFinanceiroReparteEncalhe();
		
		List<MovimentoEstoqueCota> movimentos = 
				this.movimentoEstoqueCotaRepository.obterMovimentosPendentesGerarFinanceiro(
						cotaValdomiro.getId(),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		
        Assert.assertNotNull(movimentos);
		
		Assert.assertEquals(4, movimentos.size());
	}
	
	@Test
	public void obterMovimentosEstornados(){
		
		this.setupFinanceiroReparteEncalhe();
		
		List<MovimentoEstoqueCota> movimentos = this.movimentoEstoqueCotaRepository.obterMovimentosEstornados(cotaValdomiro.getId());
		
        Assert.assertNotNull(movimentos);
		
        Assert.assertEquals(2, movimentos.size());
	}
		
	@Test
	public void testObterListaMovimentoEstoqueCotaDevolucaoJuramentada() {
		
		Date dataOperacao = Fixture.criarData(10, Calendar.JANUARY, 2012);
		
		movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaDevolucaoJuramentada(dataOperacao);
		
	}
	
	@Test
	public void test_obter_desconto_produto_edicao() {
		
		Integer numeroCota = 1;
		
		Long idProdutoEdicao = 1L;
		
		Date dataOperacao = new Date();
		
		ValoresAplicados valoresAplicados = movimentoEstoqueCotaRepository.obterValoresAplicadosProdutoEdicao(numeroCota, idProdutoEdicao, dataOperacao);
		
	}
	

}
