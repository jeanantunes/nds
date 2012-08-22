package br.com.abril.nds.component.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.component.DescontoComponent;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

public class DescontoComponentImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DescontoComponent descontoComponent;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	private Fornecedor fornecedorACM;
	
	private Set<ProdutoEdicao> produtos;

	private Cota cota123;

	private Fornecedor fornecedorTreeLog;

	private Cota cota1234;
	
	@Before
	public void setUp() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333333333334", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Acme",
				"56003315000148", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		
		fornecedorACM = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedorACM);
		
		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("TreeLog",
				"56003315001148", "333333333338", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		
		fornecedorTreeLog = Fixture.fornecedor(pessoa2, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedorTreeLog);
		
		
		NCM ncm  = Fixture.ncm(1L, "", "");
		save(ncm);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncm);
		save(tipoProduto);
		
		Produto produtoBoaForma = Fixture.produtoBoaForma(tipoProduto);
		produtoBoaForma.setFornecedores(new HashSet<Fornecedor>());
		produtoBoaForma.getFornecedores().add(fornecedorACM);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProduto);
		produtoVeja.setFornecedores(new HashSet<Fornecedor>());
		produtoVeja.getFornecedores().add(fornecedorACM);
		
		save(produtoBoaForma,produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("COD_1", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"111", 2L, produtoVeja, null, false, "Veja 1");
		save(produtoEdicaoVeja1);

		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao("COD_2", 2L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"112", 3L, produtoVeja, null, false, "Veja 2");
		save(produtoEdicaoVeja2);

		ProdutoEdicao produtoEdicaoVeja3 = Fixture.produtoEdicao("COD_3", 3L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"113", 4L, produtoVeja, null, false, "Veja 3");
		save(produtoEdicaoVeja3);
		
		ProdutoEdicao produtoEdicaoBoaForma1 = Fixture.produtoEdicao("COD_9", 1L, 10, 30,
				new Long(100), new BigDecimal(12), new BigDecimal(15),
				"119", 10L, produtoBoaForma, null, false, "Boa Forma 1");
		save(produtoEdicaoBoaForma1);
				
		PessoaJuridica pessoaCota123 = Fixture.pessoaJuridica("Cotas SA",
				"56003315000182", "333333333113", "distrib_acme@mail.com", "99.999-9");
		save(pessoaCota123);
		
		PessoaJuridica pessoaCota1234 = Fixture.pessoaJuridica("Cotas SA A",
				"56003315002182", "733333333113", "distrib_acme@mail.com", "99.999-9");
		save(pessoaCota1234);
		
		Box boxCota = Fixture.boxReparte300();
		save(boxCota);
		
		cota123= Fixture.cota(123, pessoaCota123, SituacaoCadastro.ATIVO, boxCota);
		cota123.setFornecedores(new HashSet<Fornecedor>());
		cota123.getFornecedores().add(fornecedorACM);
		
		save(cota123);
		
		cota1234= Fixture.cota(1234, pessoaCota1234, SituacaoCadastro.ATIVO, boxCota);
		cota1234.setFornecedores(new HashSet<Fornecedor>());
		cota1234.getFornecedores().add(fornecedorACM);
		cota1234.getFornecedores().add(fornecedorTreeLog);
		
		save(cota1234);
		
		
		produtos = new HashSet<ProdutoEdicao>();
		
		this.produtos.add(produtoEdicaoVeja1);
		this.produtos.add(produtoEdicaoVeja2);
		this.produtos.add(produtoEdicaoVeja3);
		this.produtos.add(produtoEdicaoBoaForma1);
		
		DescontoProdutoEdicao descontoProdutoEdicao = Fixture.descontoProdutoEdicao(cota123, BigDecimal.ONE, fornecedorACM, produtoEdicaoVeja1, TipoDesconto.GERAL);
		save(descontoProdutoEdicao);
		
		DescontoProdutoEdicao descontoProdutoEdicao2 = Fixture.descontoProdutoEdicao(cota123, BigDecimal.ONE, fornecedorACM, produtoEdicaoVeja2, TipoDesconto.GERAL);
		save(descontoProdutoEdicao2);
		
		DescontoProdutoEdicao descontoProdutoEdicao3 = Fixture.descontoProdutoEdicao(cota123, BigDecimal.ONE, fornecedorACM, produtoEdicaoVeja3, TipoDesconto.ESPECIFICO);
		save(descontoProdutoEdicao3);
		
		DescontoProdutoEdicao descontoProdutoEdicao4 = Fixture.descontoProdutoEdicao(cota123, BigDecimal.ONE, fornecedorACM, produtoEdicaoBoaForma1, TipoDesconto.PRODUTO);
		save(descontoProdutoEdicao4);
		
		//
		DescontoProdutoEdicao descontoProdutoEdicao5 = Fixture.descontoProdutoEdicao(cota1234, BigDecimal.ONE, fornecedorACM, produtoEdicaoVeja1, TipoDesconto.GERAL);
		save(descontoProdutoEdicao5);
		
		DescontoProdutoEdicao descontoProdutoEdicao6 = Fixture.descontoProdutoEdicao(cota1234, BigDecimal.ONE, fornecedorTreeLog, produtoEdicaoVeja2, TipoDesconto.ESPECIFICO);
		save(descontoProdutoEdicao6);
		
		DescontoProdutoEdicao descontoProdutoEdicao7 = Fixture.descontoProdutoEdicao(cota1234, BigDecimal.ONE, fornecedorTreeLog, produtoEdicaoVeja3, TipoDesconto.ESPECIFICO);
		save(descontoProdutoEdicao7);
		
		DescontoProdutoEdicao descontoProdutoEdicao8 = Fixture.descontoProdutoEdicao(cota1234, BigDecimal.ONE, fornecedorTreeLog, produtoEdicaoBoaForma1, TipoDesconto.PRODUTO);
		save(descontoProdutoEdicao8);
		
	}
	
	@Test
	public void persistirDescontoComTipoDescontoGeral(){
		
		persistirDesconto(TipoDesconto.GERAL);
	}
	
	@Test
	public void persistirDescontoComTipoDescontoEspecifico(){
		
		persistirDesconto(TipoDesconto.ESPECIFICO);
	}
	
	@Test
	public void persistirDescontoComTipoDescontoProduto(){
		
		persistirDesconto(TipoDesconto.PRODUTO);
		
	}
	
	@Test
	public void filtrarProdutosDescontoDistribuidor(){
		
		Set<ProdutoEdicao> produtosCandidatosDesconto = descontoComponent.filtrarProdutosPassiveisDeDesconto(TipoDesconto.GERAL, fornecedorACM, null,produtos);
		
		Set<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedorACM);
		
		for(DescontoProdutoEdicao dec : descontos){
			
			if(produtosCandidatosDesconto.contains(dec.getProdutoEdicao())){
				
				Assert.assertTrue(TipoDesconto.GERAL.equals(dec.getTipoDesconto()));
			}
			else{
				Assert.assertTrue(TipoDesconto.PRODUTO.equals(dec.getTipoDesconto())
						|| TipoDesconto.ESPECIFICO.equals(dec.getTipoDesconto()));
			}
		}
		
	}
	
	@Test
	public void filtrarProdutosDescontoCota(){
		
		Set<ProdutoEdicao> produtosCandidatosDesconto = descontoComponent.filtrarProdutosPassiveisDeDesconto(TipoDesconto.ESPECIFICO, fornecedorACM, cota123 ,produtos);
		
		Set<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedorACM,cota123);
		
		Set<DescontoProdutoEdicao> descontosCandidatosAlteracao = new HashSet<DescontoProdutoEdicao>();
		
		for(DescontoProdutoEdicao dec : descontos){
			
			if(produtosCandidatosDesconto.contains(dec.getProdutoEdicao())){
				descontosCandidatosAlteracao.add(dec);	
			}
		}
		
		for(DescontoProdutoEdicao dec : descontosCandidatosAlteracao){
				
				Assert.assertTrue(TipoDesconto.GERAL.equals(dec.getTipoDesconto())
						|| TipoDesconto.ESPECIFICO.equals(dec.getTipoDesconto()));
		}
	}
	
	@Test
	public void filtrarProdutosDescontoProduto(){
		
		Set<ProdutoEdicao> produtosCandidatosDesconto = descontoComponent.filtrarProdutosPassiveisDeDesconto(TipoDesconto.PRODUTO, fornecedorACM, cota123 ,produtos);
		
		for(ProdutoEdicao proEdicao : produtosCandidatosDesconto){
			Assert.assertTrue(produtos.contains(proEdicao));
		}
	}
	
	private void persistirDesconto(TipoDesconto tipoDesconto) {
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		descontoComponent.persistirDesconto(tipoDesconto, this.fornecedorACM, this.cota123, this.produtos, valorDesconto);
		
		DescontoProdutoEdicao desconto = null;
		
		for(ProdutoEdicao prod : this.produtos){
			
			desconto = descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(null, fornecedorACM, cota123, prod);
			
			Assert.assertTrue(desconto.getDesconto().equals(valorDesconto));
			Assert.assertTrue(desconto.getTipoDesconto().equals(tipoDesconto));
		}
	}
}
