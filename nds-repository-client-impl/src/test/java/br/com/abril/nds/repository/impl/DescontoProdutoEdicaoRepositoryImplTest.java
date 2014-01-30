package br.com.abril.nds.repository.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.util.EntityUtil;

public class DescontoProdutoEdicaoRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	private Fornecedor fornecedor;

	private Cota cota;
	
	private ProdutoEdicao produtoEdicaoVeja1;
	
	
	@Before
	public void setup() {
			
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedor);
		
		PessoaJuridica pessoa1 = Fixture.pessoaJuridica("Distribuidor Acme",
				"56003315000148", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(pessoa1);
		
		fornecedor = Fixture.fornecedor(pessoa1, SituacaoCadastro.ATIVO, tipoFornecedor,null);
		
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
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao("COD_1", 1L, 10, 14,
				Long.valueOf(100), BigDecimal.TEN, new BigDecimal(20),
				"111", produtoVeja, null, false, "Veja 1");
		save(produtoEdicaoVeja1);

		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao("COD_2", 2L, 10, 14,
				Long.valueOf(100), BigDecimal.TEN, new BigDecimal(20),
				"112", produtoVeja, null, false, "Veja 2");
		save(produtoEdicaoVeja2);

		ProdutoEdicao produtoEdicaoVeja3 = Fixture.produtoEdicao("COD_3", 3L, 10, 14,
				Long.valueOf(100), BigDecimal.TEN, new BigDecimal(20),
				"113", produtoVeja, null, false, "Veja 3");
		save(produtoEdicaoVeja3);
		
		ProdutoEdicao produtoEdicaoBoaForma1 = Fixture.produtoEdicao("COD_9", 1L, 10, 30,
				Long.valueOf(100), new BigDecimal(12), new BigDecimal(15),
				"119", produtoBoaForma, null, false, "Boa Forma 1");
		save(produtoEdicaoBoaForma1);
				
		PessoaJuridica pessoaCota = Fixture.pessoaJuridica("Cotas SA",
				"56003315000182", "333333333113", "distrib_acme@mail.com", "99.999-9");
		save(pessoaCota);
		
		Box boxCota = Fixture.boxReparte300();
		save(boxCota);
		
		cota= Fixture.cota(123, pessoaCota, SituacaoCadastro.ATIVO, boxCota);
		cota.setFornecedores(new HashSet<Fornecedor>());
		cota.getFornecedores().add(fornecedor);
		
		cota = merge(cota);
		
		Distribuidor distribuidor = Fixture.distribuidor(10, pessoaCota, new Date(), null);
		save(distribuidor);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		Desconto desconto1 = Fixture.desconto(usuario, TipoDesconto.ESPECIFICO);
		Desconto desconto2 = Fixture.desconto(usuario, TipoDesconto.ESPECIFICO);
		Desconto desconto3 = Fixture.desconto(usuario, TipoDesconto.ESPECIFICO);
		Desconto desconto4 = Fixture.desconto(usuario, TipoDesconto.ESPECIFICO);
		save(desconto1, desconto2, desconto3, desconto4);
		
		DescontoCotaProdutoExcessao descontoProdutoEdicao = Fixture.descontoProdutoEdicao(cota, desconto1, distribuidor, fornecedor, produtoEdicaoVeja1, TipoDesconto.GERAL);
		descontoProdutoEdicao.setUsuario(usuario);
		save(descontoProdutoEdicao);
		
		DescontoCotaProdutoExcessao descontoProdutoEdicao2 = Fixture.descontoProdutoEdicao(cota, desconto2, distribuidor, fornecedor, produtoEdicaoVeja2, TipoDesconto.GERAL);
		descontoProdutoEdicao2.setUsuario(usuario);
		save(descontoProdutoEdicao2);
		
		DescontoCotaProdutoExcessao descontoProdutoEdicao3 = Fixture.descontoProdutoEdicao(cota, desconto3, distribuidor, fornecedor, produtoEdicaoVeja3, TipoDesconto.ESPECIFICO);
		descontoProdutoEdicao3.setUsuario(usuario);
		save(descontoProdutoEdicao3);
		
		DescontoCotaProdutoExcessao descontoProdutoEdicao4 = Fixture.descontoProdutoEdicao(cota, desconto4, distribuidor, fornecedor, produtoEdicaoBoaForma1, TipoDesconto.PRODUTO);
		descontoProdutoEdicao4.setUsuario(usuario);
		save(descontoProdutoEdicao4);
	}
	
	
	@Test
	public void buscarDescontoProdutoEdicaoTipoDesconto() {
		
		TipoDesconto tipo = TipoDesconto.ESPECIFICO;
		
		DescontoProdutoEdicao descontoProdutoEdicao = 
				descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(tipo, null, null, null);
		
	}
	
	@Test
	public void buscarDescontoProdutoEdicaoFornecedor() {
		
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		
		DescontoProdutoEdicao descontoProdutoEdicao = 
				descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(null, fornecedor, null, null);
	}
	
	@Test
	public void buscarDescontoProdutoEdicaoCota() {
		
		Cota cota = new Cota();
		cota.setId(1L);
		
		DescontoProdutoEdicao descontoProdutoEdicao = 
				descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(null, null, cota, null);
	}
	
	@Test
	public void buscarDescontoProdutoEdicaoProdutoEdicao() {
		
		ProdutoEdicao produto = new ProdutoEdicao();
		produto.setId(1L);
		
		DescontoProdutoEdicao descontoProdutoEdicao = 
				descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(null, null, null, produto);
	}
	
	@Test
	public void obterDescontoProdutoEdicaoSemTipoDescontoParaFornecedor() {
		
		List<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.GERAL, fornecedor);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.GERAL);
		
		descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.ESPECIFICO, fornecedor);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.ESPECIFICO);
		
		descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.PRODUTO, fornecedor);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.PRODUTO);
	}
	
	@Test
	public void obterDescontoProdutoEdicaoSemTipoDescontoParaFornecedorECota() {
		
		List<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.GERAL, fornecedor,cota);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.GERAL);
		
		descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.ESPECIFICO, fornecedor,cota);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.ESPECIFICO);
		
		descontos = descontoProdutoEdicaoRepository.obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto.PRODUTO, fornecedor,cota);
		
		this.testeRetornoDesconto(descontos, TipoDesconto.PRODUTO);
		
	}
	
	@Test
	public void obterDescontoProdutoEdicaoFornecedor() {
		
		Set<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedor);
	
		Assert.assertNotNull(descontos);
		
		for(DescontoProdutoEdicao desconto : descontos){
			
			Assert.assertTrue(fornecedor.equals(desconto.getFornecedor()));
		}
	}
	
	@Test
	public void obterDescontoProdutoEdicaoFornecedorCota() {
		
		Set<DescontoProdutoEdicao> descontos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(fornecedor,cota);
	
		Assert.assertNotNull(descontos);
		
		for(DescontoProdutoEdicao desconto : descontos){
			
			Assert.assertTrue(fornecedor.equals(desconto.getFornecedor()));
			Assert.assertTrue(cota.equals(desconto.getCota()));
		}
	}
	
	@Test
	public void obterDescontoProdutoEdicaoCota() {
		
		Set<DescontoProdutoEdicao> descontos = 
			this.descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(this.cota);
	
		Assert.assertNotNull(descontos);
		
		for (DescontoProdutoEdicao desconto : descontos) {
			
			Assert.assertTrue(this.cota.equals(desconto.getCota()));
		}
	}
	
	@Test
	public void obterDescontoProdutoEdicaoProdutoEdicao() {
		
		ProdutoEdicao produto = new ProdutoEdicao(); 
		produto.setId(1L);
		
		Set<DescontoProdutoEdicao> descontoProdutoEdicaos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao(produto);
		
		Assert.assertNotNull(descontoProdutoEdicaos);
		
	}
	
	@Test
	public void obterDescontoProdutoEdicaoProdutoTipoDesconto() {
		
		TipoDesconto tipo = TipoDesconto.ESPECIFICO;
		
		Set<DescontoProdutoEdicao> descontoProdutoEdicaos = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(tipo, null, null);
		
		Assert.assertNotNull(descontoProdutoEdicaos);
		
	}
	
	@Test
	public void obterDescontoPorCotaProdutoEdicaoIdCota() {
		
		descontoProdutoEdicaoRepository.obterDescontoPorCotaProdutoEdicao(null, cota.getId(), produtoEdicaoVeja1);
				
	}
	
	@Test
	public void obterDescontoPorCotaProdutoEdicaoIdProdutoEdicao() {
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(1L);
		
		Produto produto = new Produto();
		produto.setId(1L);
		
		TipoProduto tipoProduto = new TipoProduto();
		tipoProduto.setGrupoProduto(GrupoProduto.OUTROS);
		
		produto.setTipoProduto(tipoProduto);

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		produto.addFornecedor(fornecedor);
		
		produtoEdicao.setProduto(produto);

		descontoProdutoEdicaoRepository.obterDescontoPorCotaProdutoEdicao(null, null, produtoEdicao);
	}
	
	@Test
	public void salvarListaDescontoProdutoEdicao() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		
		ProdutoEdicao produtoEdicao1 = EntityUtil.clonarSemID(produtoEdicaoVeja1);
		produtoEdicao1.setNumeroEdicao(100L);
		save(produtoEdicao1);
		
		DescontoProdutoEdicao descontoProdutoEdicao = new DescontoProdutoEdicao();
		descontoProdutoEdicao.setCota(cota);
		descontoProdutoEdicao.setFornecedor(fornecedor);
		descontoProdutoEdicao.setProdutoEdicao(produtoEdicao1);
		descontoProdutoEdicao.setTipoDesconto(TipoDesconto.ESPECIFICO);
		descontoProdutoEdicao.setDesconto(BigDecimal.ONE);
		
		List<DescontoProdutoEdicao> lista = new ArrayList<DescontoProdutoEdicao>(); 
		lista.add(descontoProdutoEdicao);
		
		
		descontoProdutoEdicaoRepository.salvarListaDescontoProdutoEdicao(lista);
				
	}
	
	private void testeRetornoDesconto(List<DescontoProdutoEdicao> descontos, TipoDesconto tipoDesconto){
		
		Assert.assertNotNull(descontos);
	}
}
