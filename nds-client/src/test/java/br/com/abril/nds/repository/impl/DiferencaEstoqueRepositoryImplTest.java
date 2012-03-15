package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
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
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@DirtiesContext
public class DiferencaEstoqueRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DiferencaEstoqueRepositoryImpl diferencaEstoqueRepository;
	
	private Date dataMovimento;
	
	private BigDecimal quantidadeDiferenca;
	
	private TipoDiferenca tipoDiferenca;
	
	private long qtdeMovimentos;
	
	private int qtdResultadoPorPagina;

	private TipoFornecedor tipoFornecedorPublicacao;
	
	@Before
	public void setup() {
		
		this.dataMovimento = Fixture.criarData(01, 2, 2012);
		
		this.quantidadeDiferenca = new BigDecimal("1.0");
		
		this.qtdeMovimentos = 30;
		
		this.qtdResultadoPorPagina = 15;
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);

		ProdutoEdicao produtoEdicao = this.criarProdutoEdicao();
		
		TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoFaltaEm();
		
		save(tipoMovimento);
		
		Usuario usuario = Fixture.usuarioJoao();
		
		save(usuario);
		
		this.tipoDiferenca = TipoDiferenca.FALTA_EM;
		
		StatusConfirmacao statusConfirmacao = StatusConfirmacao.PENDENTE;
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		
		save(tipoNotaFiscal);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
	
		
		PessoaJuridica pessoaJuridica = Fixture.juridicaDinap();
		
		save(pessoaJuridica);
		
		
		Fornecedor fornecedor = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		
		save(fornecedor);
		
		NotaFiscalFornecedor notaFiscalFornecedor = 
			Fixture.notaFiscalFornecedor(cfop, pessoaJuridica, fornecedor, tipoNotaFiscal,
										 usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		
		save(notaFiscalFornecedor);

		ItemNotaFiscal itemNotaFiscal = 
			Fixture.itemNotaFiscal(produtoEdicao, usuario, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		
		save(itemNotaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		
		save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico =  
			Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal("1.0"));
		
		save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicao, quantidadeDiferenca);
		
		save(estoqueProduto);
		
		Lancamento lancamentoVeja =
			Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
							   DateUtil.adicionarDias(new Date(), 1), DateUtil.adicionarDias(new Date(),
							   produtoEdicao.getPeb()), new Date(),
							   new Date(), BigDecimal.TEN,  StatusLancamento.RECEBIDO,
							   itemRecebimentoFisico);
		save(lancamentoVeja);
		
		for (int i = 0; i < this.qtdeMovimentos; i++) {
			
			MovimentoEstoque movimentoEstoque =
				Fixture.movimentoEstoque(
					itemRecebimentoFisico, produtoEdicao, tipoMovimento, usuario, 
						estoqueProduto, dataMovimento, 
							quantidadeDiferenca.multiply(new BigDecimal(i)), StatusAprovacao.PENDENTE, "Pendente.");
			
			save(movimentoEstoque);
			
			Diferenca diferenca = 
				Fixture.diferenca(quantidadeDiferenca, usuario, produtoEdicao,
								  tipoDiferenca, statusConfirmacao, null, movimentoEstoque, true);
			
			diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
			
			save(diferenca);
		}		
	}
	
	@Test
	@DirtiesContext
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
	@DirtiesContext
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
	@DirtiesContext
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
		
		save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		save(produto);
		
		Fornecedor fornecedor = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		
		save(fornecedor);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, produto);
		
		save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	@Test
	@Ignore//TODO: corrigir
	public void buscarStatusDiferencaLancadaAutomaticamente(){
		Boolean flag = null;
		
		flag = this.diferencaEstoqueRepository.buscarStatusDiferencaLancadaAutomaticamente(1L);
		
		Assert.assertNotNull(flag);
	}
	
}
