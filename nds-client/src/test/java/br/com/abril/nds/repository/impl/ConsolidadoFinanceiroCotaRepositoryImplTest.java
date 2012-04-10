package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;

public class ConsolidadoFinanceiroCotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	Cota cotaManoel;
	Date dataAtual = new Date();
	List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
	EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
	MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
	
	@Before
	public void setUp() {
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
				save(manoel);
				
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		TipoMovimentoEstoque tipoMovimento = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimento);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(tipoProduto);
		
		
		Produto produto = Fixture.produtoBravo(tipoProduto);
		save(produto);
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(234L,12 , 1, new BigDecimal(9), new BigDecimal(8), 
				new BigDecimal(2), produto);
		save(produtoEdicao);
		
		estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao, new BigDecimal(23), cotaManoel, listaMovimentoEstoqueCota);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota movimentoEstoqueCota = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "MOTIVO A");
		save(movimentoEstoqueCota);
		
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimento, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(movimento);
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = Fixture.tipoMovimentoFinanceiroReparte();
		save(tipoMovimentoFinanceiro);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiro, usuario, 
				new BigDecimal(33), listaMovimentoEstoqueCota, dataAtual);
		save(movimentoFinanceiroCota);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedorPublicacao();
		
		Fornecedor fornecedor = Fixture.fornecedorDinap(tipoFornecedor);
		save(fornecedor);
	
		
	}
	
	
	@Ignore
	@Test
	public void obterEncalhedaCota(){
		
		List<EncalheCotaDTO> lista = consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaEncalhe(cotaManoel.getNumeroCota(), dataAtual);
		
		Assert.assertTrue(lista.size() >0);
		
		//TODO - Implementar
		
	}

}
