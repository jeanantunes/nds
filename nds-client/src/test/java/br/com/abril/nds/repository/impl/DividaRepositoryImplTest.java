package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao.TipoOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DividaRepository;

public class DividaRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Before
	public void setUp() {
		
		
		Carteira carteiraRegistrada = Fixture.carteira(30, TipoRegistroCobranca.REGISTRADA);
	    
		save(carteiraRegistrada);
		
		Banco bancoHSBC = Fixture.banco(10L, true, carteiraRegistrada, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(bancoHSBC);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333.333.333.333", "distrib_acme@mail.com");
		
		save(juridicaDistrib);
		
		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
				  												BigDecimal.ONE, BigDecimal.ONE);
		save(formaBoleto);
		
		Distribuidor distribuidor = null; 
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mansagem");
		
		distribuidor = Fixture.distribuidor(juridicaDistrib, new Date(), politicaCobranca);
		distribuidor.getFormasCobranca().add(formaBoleto);
		
		save(distribuidor);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		ParametroCobrancaCota parametroCobrancaConta = 
				Fixture.parametroCobrancaCota(1, BigDecimal.TEN, cotaManoel, 1, 
											  formaBoleto, true, BigDecimal.TEN);
		
		save(parametroCobrancaConta);
		
		Rota rota = Fixture.rota("Rota1232");
		save(rota);
		
		Roteiro roteiro = Fixture.roteiro("Pinheiros");
		save(roteiro);
		
		RotaRoteiroOperacao rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaManoel, TipoOperacao.IMPRESSAO_DIVIDA);
		save(rotaRoteiroOperacao);
		
		TipoMovimentoFinanceiro tipoMovimentoFinenceiroReparte = Fixture.tipoMovimentoFinanceiroReparte();
		save(tipoMovimentoFinenceiroReparte);
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);		
				
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		save(produtoEdicaoVeja1);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), new Date());
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cotaManoel,
						new Date(), new BigDecimal(200));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cotaManoel, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		divida.setAcumulada(false);
		save(divida);
		
		
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cotaManoel, new Date(), new BigDecimal(10));
		save(consolidado1);
		
		Divida divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10));
		divida1.setAcumulada(false);
		save(divida1);
		
	    Boleto boleto = Fixture.boleto("5", "5", "5",
                					   new Date(), 
                					   new Date(), 
                					   new Date(), 
                					   BigDecimal.ZERO, 
                					   new BigDecimal(100.00), 
                					   "1", 
                					   "1",
                					   StatusCobranca.NAO_PAGO,
                					   cotaManoel,
                					   bancoHSBC,
                					   divida,0);
		save(boleto);		
	}
	
	@Test
	public void consultaDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		List<GeraDividaDTO> lista = dividaRepository.obterDividasGeradas(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void consultaQuantidadeDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}

	@Test
	public void consultaQuantidadeDividasGeradasPorData(){
		
		Long quantidade = dividaRepository.obterQunatidadeDividaGeradas(new Date());
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}
}
