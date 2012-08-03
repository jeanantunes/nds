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
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
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
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class ChamadaAntecipadaEncalheRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaRepository cotaRepository;
	
	private static final String codigoProduto = "1";
	private static final Long numeroEdicao = 1L;

	private Box box1;

	private Box box2;

	private Fornecedor fornecedorDinap;

	private Fornecedor fornecedorFc;
	
	@Before
	public void setup() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);

		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
						"sys.discover@gmail.com", "Manoel da Silva");

		PessoaFisica jose = Fixture.pessoaFisica("123.456.789-01",
						"sys.discover@gmail.com", "Jose da Silva");
		
		PessoaFisica maria = Fixture.pessoaFisica("123.456.789-02",
						"sys.discover@gmail.com", "Maria da Silva");
		save(manoel,jose,maria);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorFc = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		save(fornecedorDinap, fornecedorFc);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		save(box1,box2);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		PDV pdv = Fixture.criarPDVPrincipal("Manoel", cotaManoel);
		save(pdv);
		
		Roteiro roteiro = Fixture.criarRoteiro("Pinheiros",box1,TipoRoteiro.NORMAL);
		save(roteiro);

		Rota rota = Fixture.rota("005", "Rota 005");
		rota.setRoteiro(roteiro);
		save(rota);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(pdv, rota,1);
		save(roteirizacao);
		
		Cota cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		save(cotaJose);
		
		pdv = Fixture.criarPDVPrincipal("Jose", cotaJose);
		save(pdv);
		
		roteiro = Fixture.criarRoteiro("Pinheiros",box1,TipoRoteiro.NORMAL);
		save(roteiro);

		rota = Fixture.rota("005", "Rota 005");
		rota.setRoteiro(roteiro);
		save(rota);
		
		roteirizacao = Fixture.criarRoteirizacao(pdv, rota,1);
		save(roteirizacao);
		
		Cota cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO,box2);
		save(cotaMaria);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);

		save(tipoProdutoRevista);
		
		pdv = Fixture.criarPDVPrincipal("Maria", cotaMaria);
		save(pdv);
		
	
		Editor editoraAbril = Fixture.editoraAbril();
		save(editoraAbril);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(editoraAbril);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja1);
		
		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao("1", 2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJOPA", 2L,
				produtoVeja, null, false);
		save(produtoEdicaoVeja2);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscalRecebimento);
		
		NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		save(notaFiscalFornecedor);

		ItemNotaFiscalEntrada itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigInteger.TEN);
		save(itemNotaFiscalFornecedor);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico);
			
		ItemRecebimentoFisico itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigInteger.TEN);
		save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigInteger.ZERO);
		save(estoqueProdutoVeja1);
		
		EstoqueProduto estoqueProdutoVeja2 = Fixture.estoqueProduto(produtoEdicaoVeja2, BigInteger.ZERO);
		save(estoqueProdutoVeja2);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaJose, BigInteger.TEN, BigInteger.ONE);
		save(estoqueProdutoCotaVeja1);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaManoel, BigInteger.TEN, BigInteger.ONE);
		save(estoqueProdutoCotaVeja2);
		
		TipoMovimentoEstoque tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		save(tipoMovimentoRecFisico);
		
		MovimentoEstoque movimentoRecFisicoVeja1 = Fixture.movimentoEstoque(
				itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				estoqueProdutoVeja1, new Date(), BigInteger.valueOf(10), StatusAprovacao.APROVADO, "Aprovado");
		
		save(movimentoRecFisicoVeja1);
		update(estoqueProdutoVeja1);
		
		TipoMovimentoEstoque tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		TipoMovimentoEstoque tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		save(tipoMovimentoFaltaDe,tipoMovimentoFaltaEm);
		
		MovimentoEstoque movimentoEstoque1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
									 StatusAprovacao.PENDENTE, null);
		save(movimentoEstoque1);
		
		MovimentoEstoque movimentoEstoque2 =
				Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
										 estoqueProdutoVeja1, new Date(), BigInteger.valueOf(1),
										 StatusAprovacao.PENDENTE, null);
		save(movimentoEstoque2);
		
		MovimentoEstoque movimentoEstoque3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(2),
									 StatusAprovacao.PENDENTE, null);
		save(movimentoEstoque3);
		
		MovimentoEstoque movimentoEstoque4 =
				Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
										 estoqueProdutoVeja2, new Date(), BigInteger.valueOf(2),
										 StatusAprovacao.PENDENTE, null);
		save(movimentoEstoque4);
		
		Lancamento lancamentoVeja1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),+5), new Date(),
						new Date(), BigInteger.TEN,  StatusLancamento.EXPEDIDO,
						itemRecebimentoFisico, 1);
		save(lancamentoVeja1);
		
		Lancamento lancamentoVeja2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),+5), new Date(),

						new Date(), BigInteger.TEN, StatusLancamento.EXPEDIDO,

						null, 1);
		save(lancamentoVeja2);
		
		Estudo estudoVeja1 = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		save(estudoVeja1);
		
		Estudo estudoVeja2 = Fixture
				.estudo(BigInteger.TEN, lancamentoVeja2.getDataLancamentoDistribuidor(), produtoEdicaoVeja2);
		save(estudoVeja2);
		
		EstudoCota estudoCotaVeja1Joao = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja1, cotaJose);
		save(estudoCotaVeja1Joao);
		
		EstudoCota estudoCotaVeja2Joao = Fixture.estudoCota(BigInteger.valueOf(10), BigInteger.valueOf(10), estudoVeja2, cotaManoel);
		save(estudoCotaVeja2Joao);
		
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(Fixture.criarData(28, Calendar.FEBRUARY, 2012),produtoEdicaoVeja1,TipoChamadaEncalhe.ANTECIPADA);
		
		save(chamadaEncalhe);
		
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(chamadaEncalhe,false,cotaManoel,BigInteger.TEN);
		save(chamadaEncalheCota);
	}
	
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhe() {
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		
		filtro.setCodigoProduto(codigoProduto);
		filtro.setNumeroEdicao(numeroEdicao);
		filtro.setBox(box1.getId());
		filtro.setFornecedor(fornecedorDinap.getId());
		
		Long antecipadaEncalheDTO = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
		
		Assert.assertNotNull(antecipadaEncalheDTO);
		
		Assert.assertTrue(antecipadaEncalheDTO!=0);
	}
	
	@Test
	public void obterQntExemplaresCotasSujeitasAntecipacoEncalhe() {
		
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		
		filtro.setCodigoProduto(codigoProduto);
		filtro.setNumeroEdicao(numeroEdicao);
		filtro.setNumeroCota(1234);
		
		BigInteger antecipadaEncalheDTO = cotaRepository.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);
		
		Assert.assertNotNull(antecipadaEncalheDTO);
		
		Assert.assertTrue(antecipadaEncalheDTO.compareTo(BigInteger.ZERO) > 0);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalhe() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColuna.QNT_EXEMPLARES);
		filtro.setPaginacao(paginacao);
		filtro.setCodigoProduto(codigoProduto);
		filtro.setNumeroEdicao(numeroEdicao);
		filtro.setBox(box1.getId());
		filtro.setFornecedor(fornecedorDinap.getId());
		
		List<ChamadaAntecipadaEncalheDTO> antecipadaEncalheDTO = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		
		Assert.assertNotNull(antecipadaEncalheDTO);
		
		Assert.assertTrue(!antecipadaEncalheDTO.isEmpty());
	}
	
	
	
}
