package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;

public class RateioDiferencaRepositoryImplTest extends AbstractRepositoryImplTest{

	@Autowired
	private RateioDiferencaRepositoryImpl rateioDiferencaRepositoryImpl;
	
	private Diferenca diferenca;
	
	@Before
	public void setUp(){
		Box box300Reparte = Fixture.boxReparte300();
		save(box300Reparte);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		Produto produto = Fixture.produto("jkgfhfhjgh", "descricao", "nome", PeriodicidadeProduto.ANUAL, tipoProduto);
		save(produto);
				
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(1L, 1, 1, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, produto);
		save(produtoEdicao);
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("razaoSocial", "cnpj", "ie", "email");
		save(pessoaJuridica);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor fornecedor = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		save(fornecedor);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		NotaFiscal notaFiscal = 
				Fixture.notaFiscalFornecedor(cfop, pessoaJuridica, fornecedor, tipoNotaFiscal, 
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal);
				
		ItemNotaFiscal itemNotaFiscal = 
				
				Fixture.itemNotaFiscal(
						produtoEdicao, 
						usuario, 
						notaFiscal, 
						new Date(), 
						new Date(),
						TipoLancamento.LANCAMENTO,
						BigDecimal.ONE);
		
		save(itemNotaFiscal);
		
		NotaFiscalFornecedor notaFiscalFornecedor = 
				Fixture.notaFiscalFornecedor(cfop, pessoaJuridica, fornecedor, tipoNotaFiscal, 
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscalFornecedor);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigDecimal.TEN);
		save(itemRecebimentoFisico);
		
		TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoFaltaDe();
		save(tipoMovimento);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicao, BigDecimal.TEN);
		save(estoqueProduto);
		
		MovimentoEstoque movimentoEstoque = 
				Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicao, tipoMovimento, usuario, estoqueProduto, StatusAprovacao.APROVADO, "motivo");
		save(movimentoEstoque);
		
		diferenca = 
				Fixture.diferenca(BigDecimal.TEN, usuario, produtoEdicao, TipoDiferenca.FALTA_DE, StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoque, true);
		
		save(diferenca);
		
		Cota cota = Fixture.cota(1, pessoaJuridica, SituacaoCadastro.ATIVO, box300Reparte);
		save(cota);
		
		Estudo estudo = Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicao);
		save(estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.TEN, estudo, cota);
		save(estudoCota);
		
		RateioDiferenca rateioDiferenca = Fixture.rateioDiferenca(BigDecimal.TEN, cota, diferenca, estudoCota);
		save(rateioDiferenca);
	}
	
	@Test
	@DirtiesContext
	@Ignore //TODO: corrigir
	public void verificarExistenciaRateioDiferencaTest(){
		boolean test = this.rateioDiferencaRepositoryImpl.verificarExistenciaRateioDiferenca(diferenca.getId());
		
		Assert.assertTrue(test);
	}
	
	@Test
	@DirtiesContext
	public void obterRateioDiferencaPorDiferencaTest(){
		RateioDiferenca rateioDiferenca = this.rateioDiferencaRepositoryImpl.obterRateioDiferencaPorDiferenca(diferenca.getId());
		
		Assert.assertNotNull(rateioDiferenca);
	}
}
