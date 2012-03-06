package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColunaLancamento;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class DiferencaEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DiferencaEstoqueRepositoryImpl diferencaEstoqueRepository;
	
	private Date dataMovimento;
	
	private BigDecimal quantidadeDiferenca;
	
	private TipoDiferenca tipoDiferenca;
	
	private long qtdeMovimentos;
	
	private int qtdResultadoPorPagina;
	
	@Before
	public void setup() {
		
		this.dataMovimento = Fixture.criarData(01, 2, 2012);
		
		this.quantidadeDiferenca = new BigDecimal("1.0");
		
		this.qtdeMovimentos = 30;
		
		this.qtdResultadoPorPagina = 15;
		
		ProdutoEdicao produtoEdicao = this.criarProdutoEdicao();
		
		TipoMovimento tipoMovimento = Fixture.tipoMovimentoFaltaEm();
		
		getSession().save(tipoMovimento);
		
		Usuario usuario = Fixture.usuarioJoao();
		
		getSession().save(usuario);
		
		this.tipoDiferenca = TipoDiferenca.FALTA_EM;
		
		StatusConfirmacao statusConfirmacao = StatusConfirmacao.CONFIRMADO;
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		
		getSession().save(tipoNotaFiscal);
		
		CFOP cfop = Fixture.cfop5102();
		
		Criteria criteria = getSession().createCriteria(CFOP.class);
		
		criteria.add(Restrictions.eq("codigo", cfop.getCodigo()));
		
		CFOP cfopDB = (CFOP) criteria.uniqueResult();
		
		if (cfopDB == null) {
			
			getSession().save(cfop);
			
		} else {
			
			cfop = cfopDB;
		}
		
		PessoaJuridica pessoaJuridica = Fixture.juridicaDinap();
		
		getSession().save(pessoaJuridica);
		
		Fornecedor fornecedor = Fixture.fornecedorDinap();
		
		getSession().save(fornecedor);
		
		NotaFiscalFornecedor notaFiscalFornecedor = 
			Fixture.notaFiscalFornecedor(cfop, pessoaJuridica, fornecedor, tipoNotaFiscal,
										 usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		
		getSession().save(notaFiscalFornecedor);

		ItemNotaFiscal itemNotaFiscal = 
			Fixture.itemNotaFiscal(produtoEdicao, usuario, notaFiscalFornecedor, new Date(), BigDecimal.TEN);
		
		getSession().save(itemNotaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		
		getSession().save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico =  
			Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal("1.0"));
		
		getSession().save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicao, quantidadeDiferenca);
		
		getSession().save(estoqueProduto);
		
		for (int i = 0; i < this.qtdeMovimentos; i++) {
			
			MovimentoEstoque movimentoEstoque =
				Fixture.movimentoEstoque(
					itemRecebimentoFisico, produtoEdicao, tipoMovimento, usuario, 
						estoqueProduto, dataMovimento, 
							quantidadeDiferenca.multiply(new BigDecimal(i)), StatusAprovacao.PENDENTE, "Pendente.");
			
			getSession().save(movimentoEstoque);
			
			Diferenca diferenca = 
				Fixture.diferenca(quantidadeDiferenca, usuario, produtoEdicao,
								  tipoDiferenca, statusConfirmacao, null, movimentoEstoque);
			
			diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
			
			getSession().save(diferenca);
		}		
	}
	
	@Test
	public void obterDiferencasLancamento() {
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		filtro.setDataMovimento(this.dataMovimento);
		filtro.setTipoDiferenca(this.tipoDiferenca);

		filtro.setOrdenacaoColuna(OrdenacaoColunaLancamento.VALOR_TOTAL_DIFERENCA);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.DESC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(this.qtdResultadoPorPagina);
		
		filtro.setPaginacao(paginacao);
		
		List<Diferenca> listaDiferencas = 
			this.diferencaEstoqueRepository.obterDiferencasLancamento(filtro);
		
		Assert.assertNotNull(listaDiferencas);
		
		Assert.assertEquals(this.qtdResultadoPorPagina, listaDiferencas.size());
		
		for (int i = 0; i < listaDiferencas.size(); i++) {
			
			Diferenca diferenca = listaDiferencas.get(i);
			
			Assert.assertEquals(
				this.dataMovimento, diferenca.getMovimentoEstoque().getDataInclusao());
			
			Assert.assertEquals(
				this.tipoDiferenca, diferenca.getTipoDiferenca());
		}
	}
	
	@Test
	@DirtiesContext
	public void obterTotalDiferencasLancamento() {
		
		FiltroLancamentoDiferencaEstoqueDTO filtro = new FiltroLancamentoDiferencaEstoqueDTO();
		
		filtro.setDataMovimento(this.dataMovimento);
		filtro.setTipoDiferenca(this.tipoDiferenca);
		
		long quantidadeTotal = this.diferencaEstoqueRepository.obterTotalDiferencasLancamento(filtro);
		
		Assert.assertNotNull(quantidadeTotal);
		
		Assert.assertEquals(this.qtdeMovimentos, quantidadeTotal);
	}
	
	@Test
	public void obterDiferencas() {
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.DESC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(10);
		
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		filtro.setCodigoProduto("1");
		filtro.setNumeroEdicao(1L);
		filtro.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		
		filtro.setPaginacao(paginacao);
		
		List<Diferenca> lista = diferencaEstoqueRepository.obterDiferencas(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void obterTotalDiferencas() {
		FiltroConsultaDiferencaEstoqueDTO filtro = new FiltroConsultaDiferencaEstoqueDTO();
		
		filtro.setCodigoProduto("1");
		filtro.setNumeroEdicao(1L);
		filtro.setTipoDiferenca(TipoDiferenca.FALTA_EM);
		
		long quantidadeTotal = diferencaEstoqueRepository.obterTotalDiferencas(filtro);
		
		Assert.assertNotNull(quantidadeTotal);
		
		Assert.assertTrue(quantidadeTotal != 0);
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		
		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.add(Restrictions.eq("codigo", produto.getCodigo()));
		
		Produto produtoBD = (Produto) criteria.uniqueResult();
		
		if (produtoBD == null) {
		
			getSession().save(produto);
			
		} else {
			
			produto = produtoBD;
		}
		
		Fornecedor fornecedor = Fixture.fornecedorDinap();
		
		getSession().save(fornecedor);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, produto);
		
		produtoEdicao.setFornecedor(fornecedor);
		
		getSession().save(produtoEdicao);
		
		return produtoEdicao;
	}
	
}
