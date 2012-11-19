package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.ViewDesconto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.RelatorioVendasRepository;

public class RelatorioVendasRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RelatorioVendasRepository relatorioVendasRepository;
	
	FiltroCurvaABCDistribuidorDTO filtro;
	
	@Before
	public void setUp (){
		
		NCM ncmRevista = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevista);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoProduto("Revistas", GrupoProduto.REVISTA, ncmRevista, "4902.90.00", 001L);
		save(tipoProdutoRevista);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save( tipoFornecedorPublicacao);
				
		Fornecedor fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorDinap.setCodigoInterface(9999999);
		fornecedorDinap.setResponsavel("Maria");
		fornecedorDinap.setOrigem(Origem.MANUAL);
		fornecedorDinap.setEmailNfe("maria@email.com");
		save(fornecedorDinap);
		
		Editor editoraAbril = Fixture.editoraAbril();
		save(editoraAbril);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(editoraAbril);
		produtoVeja.setTributacaoFiscal(TributacaoFiscal.TRIBUTADO);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("COD_1", 1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"111", produtoVeja, null, false, "Veja 1");
		save(produtoEdicaoVeja1);

		PessoaFisica guilherme = Fixture.pessoaFisica("99933355511", "sys.discover@gmail.com", "Guilherme de Morais Leandro");
		save(guilherme);
		
		Box box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		save(box2);
		
		Cota cotaGuilherme = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO,box2);
		cotaGuilherme.setSugereSuspensao(true);
		save(cotaGuilherme);
		
		
		Endereco enderecoMococa1 = Fixture.criarEndereco(
			TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", "50", "Centro", "Mococa", "SP",3530508);
		save(enderecoMococa1);
		
		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cotaGuilherme);
		enderecoCota.setEndereco(enderecoMococa1);
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);	
		save(enderecoCota);
		
		EstoqueProdutoCota estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaGuilherme, BigInteger.valueOf(100), BigInteger.TEN);
		save(estoqueProdutoCotaVeja1);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		MovimentoEstoqueCota movimentoEstoqueCota1 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigInteger.TEN, cotaGuilherme, StatusAprovacao.PENDENTE, null);
		movimentoEstoqueCota1.setData(Fixture.criarData(8, Calendar.NOVEMBER, 2012));
		save(movimentoEstoqueCota1);
		
		
		ViewDesconto viewDesconto = new ViewDesconto();
		viewDesconto.setId(1L);
		viewDesconto.setDesconto(BigDecimal.ONE);
		viewDesconto.setCotaId(cotaGuilherme.getId());
		viewDesconto.setProdutoEdicaoId(produtoEdicaoVeja1.getId());
		viewDesconto.setFornecedorId(fornecedorDinap.getId());
		save(viewDesconto);
	}
		
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroEdicaoProduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setEdicaoProduto(new ArrayList<Long>());
		filtro.getEdicaoProduto().add(1L);
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroCodigoFornecedor(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setCodigoFornecedor("45");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroCodigoProduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setCodigoProduto("45.5");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroNomeProduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setNomeProduto("produtoTeste");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroCodigoEditor(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setCodigoEditor("45");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroCodigoCota(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setCodigoCota(1);
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroNomeCota(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setNomeCota("cotaTest");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorTotalFiltroMunicipio(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setMunicipio("municipioTeste");
		
		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = 
					relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtro);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidorEdicaoproduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setEdicaoProduto(new ArrayList<Long>());
		filtro.getEdicaoProduto().add(1L);
		
		List<RegistroCurvaABCDistribuidorVO> registroCurvaABCDistribuidorVOs =  
					relatorioVendasRepository.obterCurvaABCDistribuidor(filtro);
		
		Assert.assertNotNull(registroCurvaABCDistribuidorVOs);
		
	}
	
	@Test
	public void obterCurvaABCDistribuidor(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		filtro.setDataDe(Fixture.criarData(1, Calendar.NOVEMBER, 2012));
		filtro.setDataAte(Fixture.criarData(1, Calendar.DECEMBER, 2012));
		
		List<RegistroCurvaABCDistribuidorVO> registroCurvaABCDistribuidorVOs =  
					relatorioVendasRepository.obterCurvaABCDistribuidor(filtro);
		
		Assert.assertNotNull(registroCurvaABCDistribuidorVOs);
		
	}

}
