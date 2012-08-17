package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.DescontoService;

public class DescontoServiceImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	private Set<Fornecedor> fornecedores;
	
	private Set<Cota> cotas;
	
	private Set<ProdutoEdicao> produtos;
	
	private Cota cotaManoel;
	
	private Cota cotaJoao;
	
	@Before
	public void setUp() {
		
		// FORNECEDORES
		
		fornecedores = new HashSet<Fornecedor>();
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000147", "333333333334", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), null);
		save(distribuidor);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000148", "333333333335", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		

		PessoaJuridica pessoa2 = Fixture.pessoaJuridica("Distribuidor Acme 1",
				"56003315000149", "333333333336", "distrib_acme@mail.com", "99.999-9");
		save(pessoa2);
		

		PessoaJuridica pessoa3 = Fixture.pessoaJuridica("Distribuidor Acm 2",
				"56003315000145", "333333333337", "distrib_acme@mail.com", "99.999-9");
		save(pessoa3);
		

		PessoaJuridica pessoa4 = Fixture.pessoaJuridica("Distribuidor Acme 3",
				"56003315000142", "333333333338", "distrib_acme@mail.com", "99.999-9");
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
		
		this.produtos = new HashSet<ProdutoEdicao>();
		
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
		
		this.produtos.add(produtoEdicaoVeja);
		
		// COTAS
		
		this.cotas = new HashSet<Cota>();
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		
		save(box1);
		
		PessoaFisica manoel =
			Fixture.pessoaFisica("123.456.789-00", "sys.discover@gmail.com", "Manoel da Silva");
		
		save(manoel);
		
		PessoaFisica joao =
			Fixture.pessoaFisica("124.456.789-00", "sys2.discover@gmail.com", "João da Silva");
			
		save(joao);
		
		this.cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);

		this.cotaManoel.getFornecedores().addAll(this.fornecedores);
		
		this.cotaJoao = Fixture.cota(1234, joao, SituacaoCadastro.ATIVO, box1);
		
		this.cotaJoao.getFornecedores().addAll(this.fornecedores);
		
		save(this.cotaManoel, this.cotaJoao);
		
		this.cotas.add(cotaManoel);
		this.cotas.add(cotaJoao);
	}
	
	@Test
	public void processarDescontoDistribuidorMultiplosFornecedores(){
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		this.descontoService.processarDescontoDistribuidor(this.fornecedores, valorDesconto);
		
		this.processarDescontoDistribuidor(this.fornecedores, valorDesconto);
	}
	
	@Test
	public void processarDescontoDistribuidorTodosFornecedores(){
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		this.descontoService.processarDescontoDistribuidor(valorDesconto);
		
		List<Fornecedor> fornecedores = this.fornecedorRepository.obterFornecedoresAtivos();
		
		this.processarDescontoDistribuidor(new HashSet<Fornecedor>(fornecedores), valorDesconto);
	}
	
	@Test
	public void processarDescontoCotaMultiplosFornecedores() {
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		this.descontoService.processarDescontoCota(this.cotaManoel, this.fornecedores, valorDesconto);
		
		this.processarDescontoCota(this.fornecedores, valorDesconto);
	}
	
	@Test
	public void processarDescontoCotaTodosFornecedores() {
		
		BigDecimal valorDesconto = BigDecimal.TEN;
		
		List<Fornecedor> fornecedores = this.fornecedorRepository.obterFornecedoresAtivos();
		
		this.descontoService.processarDescontoCota(this.cotaManoel, valorDesconto);
		
		this.processarDescontoCota(new HashSet<Fornecedor>(fornecedores), valorDesconto);
	}
	
	@Test
	public void processarDescontoMultiplosProdutos() {
		
		BigDecimal valorDesconto = BigDecimal.TEN;

		this.descontoService.processarDescontoProduto(this.produtos, this.cotas, valorDesconto);
	}
	
	private void processarDescontoCota(Set<Fornecedor> fornecedores, 
			   						   BigDecimal valorDesconto) {

		Set<DescontoProdutoEdicao> descontosProdutoEdicao = 
			this.descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(this.cotaManoel);
		
		Assert.assertNotNull(descontosProdutoEdicao);
		
		int tamanhoEsperado = fornecedores.size();
		
		Assert.assertEquals(tamanhoEsperado, descontosProdutoEdicao.size());
		
		for (DescontoProdutoEdicao descontoProdutoEdicao : descontosProdutoEdicao) {
			
			Assert.assertEquals(valorDesconto, descontoProdutoEdicao.getDesconto());
		}
	}
	
	private void processarDescontoDistribuidor(Set<Fornecedor> fornecedores, 
											   BigDecimal valorDesconto) {

		for (Fornecedor fornecedor : fornecedores) {
		
			Set<DescontoProdutoEdicao> descontosProdutoEdicao = 
				this.descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedor);
			
			Assert.assertNotNull(descontosProdutoEdicao);
			
			int tamanhoEsperado = 2;
			
			Assert.assertEquals(tamanhoEsperado, descontosProdutoEdicao.size());
			
			for (DescontoProdutoEdicao descontoProdutoEdicao : descontosProdutoEdicao) {
				
				Assert.assertEquals(valorDesconto, descontoProdutoEdicao.getDesconto());
			}
		}
	}

}
