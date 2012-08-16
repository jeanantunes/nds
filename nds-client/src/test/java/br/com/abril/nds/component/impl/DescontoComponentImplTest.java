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
	
	private Fornecedor fornecedor;
	
	private Set<ProdutoEdicao> produtos;

	private Cota cota;
	
	@Before
	public void setUp() {
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000148", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		
		fornecedor = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedor);
		
		NCM ncm  = Fixture.ncm(1L, "", "");
		save(ncm);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncm);
		save(tipoProduto);
		
		Produto produtoBoaForma = Fixture.produtoBoaForma(tipoProduto);
		produtoBoaForma.setFornecedores(new HashSet<Fornecedor>());
		produtoBoaForma.getFornecedores().add(fornecedor);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProduto);
		produtoVeja.setFornecedores(new HashSet<Fornecedor>());
		produtoVeja.getFornecedores().add(fornecedor);
		
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
				
		PessoaJuridica pessoaCota = Fixture.pessoaJuridica("Cotas SA",
				"56003315000182", "333333333113", "distrib_acme@mail.com", "99.999-9");
		save(pessoaCota);
		
		Box boxCota = Fixture.boxReparte300();
		save(boxCota);
		
		cota= Fixture.cota(123, pessoaCota, SituacaoCadastro.ATIVO, boxCota);
		cota.setFornecedores(new HashSet<Fornecedor>());
		cota.getFornecedores().add(fornecedor);
		
		save(cota);
		
		produtos = new HashSet<ProdutoEdicao>();
		
		this.produtos.add(produtoEdicaoVeja1);
		this.produtos.add(produtoEdicaoVeja2);
		this.produtos.add(produtoEdicaoVeja3);
		this.produtos.add(produtoEdicaoBoaForma1);
		
		DescontoProdutoEdicao descontoProdutoEdicao = Fixture.descontoProdutoEdicao(cota, BigDecimal.ONE, fornecedor, produtoEdicaoVeja1, TipoDesconto.GERAL);
		save(descontoProdutoEdicao);
		
		DescontoProdutoEdicao descontoProdutoEdicao2 = Fixture.descontoProdutoEdicao(cota, BigDecimal.ONE, fornecedor, produtoEdicaoVeja2, TipoDesconto.GERAL);
		save(descontoProdutoEdicao2);
		
		DescontoProdutoEdicao descontoProdutoEdicao3 = Fixture.descontoProdutoEdicao(cota, BigDecimal.ONE, fornecedor, produtoEdicaoBoaForma1, TipoDesconto.GERAL);
		save(descontoProdutoEdicao3);
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
	
	private void persistirDesconto(TipoDesconto tipoDesconto) {
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		descontoComponent.persistirDesconto(tipoDesconto, this.fornecedor, this.cota, this.produtos, valorDesconto);
		
		DescontoProdutoEdicao desconto = null;
		
		for(ProdutoEdicao prod : this.produtos){
			
			desconto = descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(fornecedor, cota, prod);
			
			Assert.assertTrue(desconto.getDesconto().equals(valorDesconto));
			Assert.assertTrue(desconto.getTipoDesconto().equals(tipoDesconto));
		}
	}
}
