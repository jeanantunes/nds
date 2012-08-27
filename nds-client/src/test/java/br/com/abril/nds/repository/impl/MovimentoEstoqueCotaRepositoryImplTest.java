package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
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
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
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
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;


public class MovimentoEstoqueCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MovimentoEstoqueCotaRepositoryImpl movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepositoryImpl tipoMovimentoEstoqueRepository;
	
	private Lancamento lancamentoVeja;
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	private Cota cotaManoel;
	
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

		veja1 = Fixture.produtoEdicao("1", 1L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 1L, veja, null, false);


		quatroRoda2 = Fixture.produtoEdicao("1", 2L, 15, 30,
				new Long(100), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA", 2L,
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao("1", 3L, 5, 30,
				new Long(100), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPB", 3L, infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao("1", 1L, 10, 15,
				new Long(120), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPC", 4L, capricho, null, false);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao("1", 1L, 100, 60,
				new Long(10), BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOP", 5L, cromoReiLeao, null, false);
		
		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
		
		cfop = Fixture.cfop5102();
		save(cfop);
		
		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
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
		
		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		itemRecebimentoFisico1Veja = 
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
			
		itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);
		
	}
	
	
	public void setUpForContagemDevolucao() {
		
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
		
		Roteiro roteiro1 = Fixture.criarRoteiro("", box1, TipoRoteiro.NORMAL);
		Roteiro roteiro2 = Fixture.criarRoteiro("", box2, TipoRoteiro.NORMAL);
		save(roteiro1,roteiro2);
		
		rota1 = Fixture.rota("ROTA01", "Rota 1");
		rota2 = Fixture.rota("ROTA01", "Rota 1");
		rota1.setRoteiro(roteiro1);
		rota2.setRoteiro(roteiro2);
		save(rota1,rota2);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
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
			
		PDV pdv = Fixture.criarPDVPrincipal("Meu PDV", cotaManoel );
		save(pdv);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(pdv, rota1, 0);
		save(roteirizacao);
		
		PDV pdv2 = Fixture.criarPDVPrincipal("Meu PDV", cotaManoel );
		save(pdv2);
		
		Roteirizacao roteirizacao2 = Fixture.criarRoteirizacao(pdv2, rota2, 0);
		save(roteirizacao2);
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
	public void testObterQtdProdutoEdicaoEncalhePrimeiroDia() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, false);
		
		Assert.assertNotNull(qtde);
		
		Assert.assertEquals(1, qtde.intValue());
		
	}

	@Test
	public void testObterQtdItemProdutoEdicaoEncalhePrimeiroDia() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		
		BigDecimal qtde = movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, false);
		
		Assert.assertEquals(8, qtde.intValue());
		
	}

	@Test
	public void testObterQtdProdutoEdicaoEncalheAposPrimeiroDia() {

		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		filtro.setDataRecolhimento(Fixture.criarData(1, Calendar.MARCH, 2012));
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdProdutoEdicaoEncalhe(filtro, true);
		
		Assert.assertNotNull(qtde);
		
		Assert.assertEquals(1, qtde.intValue());
	}

	@Test
	public void testObterQtdItemProdutoEdicaoEncalheAposPrimeiroDia() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		
		filtro.setDataRecolhimento(Fixture.criarData(2, Calendar.MARCH, 2012));
		
		BigDecimal qtde = movimentoEstoqueCotaRepository.obterQtdItemProdutoEdicaoEncalhe(filtro, true);
		
		Assert.assertEquals(45, qtde.intValue());
	}
	
	@Test
	public void testObterQtdConsultaEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro);
		
		Assert.assertEquals(1, qtde.intValue());
	}
	
	@Test
	public void testObterListaConsultaEncalhe() {
		
		setUpForConsultaEncalhe();
		
		FiltroConsultaEncalheDTO filtro = obterFiltroConsultaEncalhe();
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);

		Assert.assertNotNull(listaConsultaEncalhe);
		
		
		ConsultaEncalheDTO cEncalhe_1 = listaConsultaEncalhe.get(0);
		Assert.assertEquals((8*15), cEncalhe_1.getTotal().intValue());
		
//		ConsultaEncalheDTO cEncalhe_2 = listaConsultaEncalhe.get(1);
//		Assert.assertEquals((50*15), cEncalhe_2.getTotal().intValue());
//		
//		ConsultaEncalheDTO cEncalhe_3 = listaConsultaEncalhe.get(2);
//		Assert.assertEquals((45*15), cEncalhe_3.getTotal().intValue());
		
		
	}

	
	
	private FiltroConsultaEncalheDTO obterFiltroConsultaEncalhe() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		filtro.setDataRecolhimento(Fixture.criarData(28, Calendar.FEBRUARY, 2012));
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
				obterTipoMovimento(),
				true);
		
		
		Assert.assertEquals(2, retorno.size());
		
		ContagemDevolucaoDTO contagem = retorno.get(0);
		
		Assert.assertEquals(19, contagem.getQtdDevolucao().intValue());
		
	}
	
	@Test
	public void testarObterValorTotal() {
		
		setUpForContagemDevolucao();
		
		BigDecimal total = movimentoEstoqueCotaRepository.obterValorTotalGeralContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao(), 
				obterTipoMovimento());
		
		Assert.assertEquals(475, total.intValue());
	}
	
	@Test
	public void testarObterListaContagemDevolucao() {
		
		setUpForContagemDevolucao();
		
		List<ContagemDevolucaoDTO> listaContagemDevolucao = movimentoEstoqueCotaRepository.obterListaContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao(), 
				obterTipoMovimento(),
				false);
		
		Assert.assertNotNull(listaContagemDevolucao);
	}
	
	@Test
	public void testarObterQuantidadeContagemDevolucao() {
		
		setUpForContagemDevolucao();
		
		Integer qtde = movimentoEstoqueCotaRepository.obterQuantidadeContagemDevolucao(
				obterFiltroDigitacaoContagemDevolucao(), obterTipoMovimento());
		
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
		
	
		PeriodoVO periodo = new PeriodoVO();
		periodo.setDataInicial(dataInicial);
		periodo.setDataFinal(dataFinal);
		filtro.setPeriodo(periodo);
		
		filtro.setOrdenacaoColuna(OrdenacaoColuna.CODIGO_PRODUTO);
		
		filtro.setIdFornecedor(fornecedorDinap.getId());
		
		return filtro;
		
	}
	
	
	private TipoMovimentoEstoque obterTipoMovimento() {
		TipoMovimentoEstoque tipoMovimentoEstoque = 
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		return tipoMovimentoEstoque;
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

		Assert.assertTrue(listaMovimento.size() == 2);
	}	
	
	@Test
	public void obterDadosAbastecimentoComFiltros() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "box"));
		filtro.setBox(box1.getId());
		filtro.setRota(rota1.getId());
		filtro.setCodigoProduto(veja1.getCodigo());
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		List<AbastecimentoDTO> listaMovimento = movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);

		Assert.assertTrue(listaMovimento.size() == 1);	
	}
	
	@Test
	public void countObterDadosAbastecimento() {
		
		setUpForMapaAbastecimento();
		
		FiltroMapaAbastecimentoDTO filtro = new FiltroMapaAbastecimentoDTO();
		filtro.setDataDate(new Date());
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "box"));
		filtro.setBox(box1.getId());
		filtro.setRota(rota1.getId());
		filtro.setCodigoProduto(veja1.getCodigo());
		filtro.setCodigoCota(cotaManoel.getNumeroCota());
		
		Long count = movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);

		Assert.assertTrue(count.equals(1L));
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

		Assert.assertTrue(listaMovimento.size() == 2);	
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

		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		
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
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(distribuidor, cotaManoel.getId(), GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, periodo, listaFornecedores, listaProdutos);

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

		Distribuidor distribuidor = new Distribuidor();
		distribuidor.setParametrosRecolhimentoDistribuidor(parametrosRecolhimentoDistribuidor);
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		
		Intervalo<Date> periodo = new Intervalo<Date>();
		periodo.setDe(DateUtil.parseData("01/01/2012", "dd/MM/yyyy"));
		periodo.setAte(DateUtil.parseData("01/01/2013", "dd/MM/yyyy"));
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(distribuidor, cotaManoel.getId(), GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO, listaGrupoMovimentoEstoques, periodo, null, null);

		int tamanhoEsperado = 3;
		
		Assert.assertEquals(tamanhoEsperado, listaMovimentoEstoqueCota.size());
		
	}

}
