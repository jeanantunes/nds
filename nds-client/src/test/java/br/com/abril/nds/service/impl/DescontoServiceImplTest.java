package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.DescontoService;

public class DescontoServiceImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	private Set<Fornecedor> fornecedores;
	
	@Before
	public void setUp() {
		
		// FORNECEDORES
		
		fornecedores = new HashSet<Fornecedor>();
		
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
		

		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("Distribuidor Acme 1",
				"56003315000149", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		

		PessoaJuridica pessoa3 = Fixture.pessoaJuridica("Distribuidor Acm 2",
				"56003315000145", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa3);
		

		PessoaJuridica pessoa4 = Fixture.pessoaJuridica("Distribuidor Acme 3",
				"56003315000142", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa4);
		
		Fornecedor fornecedor = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor1 = Fixture.fornecedor(pessoa2, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor2 = Fixture.fornecedor(pessoa3, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		Fornecedor fornecedor3 = Fixture.fornecedor(pessoa4, SituacaoCadastro.ATIVO, false, tipoFornecedor,null);
		
		save(fornecedor, fornecedor1, fornecedor2,fornecedor3);
		
		this.fornecedores.add(fornecedor);
		this.fornecedores.add(fornecedor1);
		this.fornecedores.add(fornecedor2);
		this.fornecedores.add(fornecedor3);
		
		// PRODUTOS
		
		NCM ncm = Fixture.ncm(1L, "NCM", "un");
		
		save(ncm);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncm);
		
		save(tipoProduto);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProduto);
		
		produtoVeja.setFornecedores(this.fornecedores);
		
		save(produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja = 
			Fixture.produtoEdicao("1", 1L, 10, 14, new Long(100), 
				BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L, produtoVeja, null, false);
		
		save(produtoEdicaoVeja);
		
		// COTAS
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		
		save(manoel);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);

		cotaManoel.getFornecedores().addAll(this.fornecedores);
		
		save(cotaManoel);
	}
	
	@Test
	public void processarDescontoDistribuidorMultiplosFornecedores(){
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		this.descontoService.processarDescontoDistribuidor(this.fornecedores, valorDesconto);
		
		for (Fornecedor fornecedor : this.fornecedores) {
		
			Set<DescontoProdutoEdicao> descontosProdutoEdicao = 
				this.descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedor);
			
			Assert.assertNotNull(this.fornecedores);
			
			//Assert.assertEquals(this.fornecedores.size(), descontosProdutoEdicao.size());
			
			for (DescontoProdutoEdicao descontoProdutoEdicao : descontosProdutoEdicao) {
				
				Assert.assertEquals(valorDesconto, descontoProdutoEdicao.getDesconto());
			}
		}
	}
	
	@Test
	public void processarDescontoDistribuidorTodosFornecedores(){
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		this.descontoService.processarDescontoDistribuidor(valorDesconto);
	}


}
