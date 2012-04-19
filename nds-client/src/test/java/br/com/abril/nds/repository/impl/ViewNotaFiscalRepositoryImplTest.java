package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO.OrdenacaoColuna;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ViewNotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class ViewNotaFiscalRepositoryImplTest extends AbstractRepositoryImplTest {

	
	@Before
	public void setUp() {
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorDinap);
		
		Cota cotaJohnyConsultaEncalhe = null;
		
		PessoaFisica johnyCE = Fixture.pessoaFisica(
				"352.855.474-00",
				"johny@discover.com.br", "Johny da Silva");
		save(johnyCE);
		
		cotaJohnyConsultaEncalhe = Fixture.cota(2593, johnyCE, SituacaoCadastro.ATIVO, box1);
		save(cotaJohnyConsultaEncalhe);
		
		
		ProdutoEdicao produtoEdicaoCE = null;
		ProdutoEdicao produtoEdicaoCE_2 = null;
		ProdutoEdicao produtoEdicaoCE_3 = null;
		
		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;
		
		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista);
		
		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);
		
		save(produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao(84L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), produtoCE);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao(85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), produtoCE_2);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao(86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), produtoCE_3);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		save(produtoEdicaoCE, produtoEdicaoCE_2, produtoEdicaoCE_3);
		
		NotaFiscalEntradaFornecedor notaFiscalProdutoCE = 
				Fixture.notaFiscalEntradaFornecedor(
						cfop5102, 
						fornecedorDinap.getJuridica(), 
						fornecedorDinap, 
						tipoNotaFiscal,
						usuarioJoao, 
						BigDecimal.TEN, 
						BigDecimal.ZERO, 
						BigDecimal.TEN);
		
		save(notaFiscalProdutoCE);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(itemNotaFiscalProdutoCE);
		
		
		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_2 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_2, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(itemNotaFiscalProdutoCE_2);
		
		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_3 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_3, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(itemNotaFiscalProdutoCE_3);

		
		
		
		///////////////////////
		
		
//		NotaFiscalEntradaCota notaFiscalEntradaCota = 
//				Fixture.notaFiscal(
//						cfop5102, 
//						fornecedorDinap.getJuridica(), 
//						fornecedorDinap, 
//						tipoNotaFiscal,
//						usuarioJoao, 
//						BigDecimal.TEN, 
//						BigDecimal.ZERO, 
//						BigDecimal.TEN);
//		
//		save(notaFiscalProdutoCE);
//
//		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE = 
//				Fixture.itemNotaFiscal(
//						produtoEdicaoCE, 
//						usuarioJoao,
//						notaFiscalProdutoCE, 
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						TipoLancamento.LANCAMENTO,
//						new BigDecimal(50));
//		
//		save(itemNotaFiscalProdutoCE);
//		
//		
//		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_2 = 
//				Fixture.itemNotaFiscal(
//						produtoEdicaoCE_2, 
//						usuarioJoao,
//						notaFiscalProdutoCE, 
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						TipoLancamento.LANCAMENTO,
//						new BigDecimal(50));
//		
//		save(itemNotaFiscalProdutoCE_2);
//		
//		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_3 = 
//				Fixture.itemNotaFiscal(
//						produtoEdicaoCE_3, 
//						usuarioJoao,
//						notaFiscalProdutoCE, 
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						Fixture.criarData(22, Calendar.FEBRUARY,2012),
//						TipoLancamento.LANCAMENTO,
//						new BigDecimal(50));
//		
//		save(itemNotaFiscalProdutoCE_3);
		
	}
	

	
	
	@Autowired
	private ViewNotaFiscalRepository viewNotaFiscalRepository;
	
	@Test
	public void test() {
		
		FiltroMonitorNfeDTO filtro = new FiltroMonitorNfeDTO();
		
		viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		
	}
	
	
	
	private FiltroMonitorNfeDTO obterFiltroMonitorNfeDTO() {
		
		FiltroMonitorNfeDTO filtro = new FiltroMonitorNfeDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(OrdenacaoColuna.EMISSAO);
		
		return filtro;
		
	}
	
}
