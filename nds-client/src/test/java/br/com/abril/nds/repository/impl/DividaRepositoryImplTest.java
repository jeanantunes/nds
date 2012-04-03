package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao.TipoOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
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
		
		
		//CRIA UM OBJETO PESSOA NA SESSAO PARA TESTES
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH", "01.001.001/001-00", "000.000.000.00", "lh@mail.com");
		save(pessoaJuridica);
		
		//CRIA UM OBJETO BOX NA SESSAO PARA TESTES
		Box box = Fixture.criarBox("300", "Box 300", TipoBox.REPARTE);
		save(box);
		
		//CRIA UM OBJETO COTA NA SESSAO PARA TESTES
		Cota cota = Fixture.cota(123, pessoaJuridica, SituacaoCadastro.ATIVO,box);
		save(cota);
		
		Rota rota = Fixture.rota("Rota1232");
		save(rota);
		
		Roteiro roteiro = Fixture.roteiro("Pinheiros");
		save(roteiro);
		
		RotaRoteiroOperacao rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cota, TipoOperacao.IMPRESSAO_DIVIDA);
		save(rotaRoteiroOperacao);
		
		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010",
				  			  		123456L, "1", "1", "Instruções.", Moeda.REAL, "HSBC", "399");
		save(bancoHSBC);
		
		
		
		//AMARRAÇAO DIVIDA X BOLETO
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
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
				produtoEdicaoVeja1, cota, BigDecimal.TEN, BigDecimal.ZERO);
		save(estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				cota, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), new Date());
		save(movimentoFinanceiroCota);
		
		ConsolidadoFinanceiroCota consolidado = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cota,
						new Date(), new BigDecimal(200));
		save(consolidado);
		
		Divida divida = Fixture.divida(consolidado, cota, new Date(),
				        usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		divida.setAcumulada(false);
		save(divida);
		
		
		
		//CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10));
		save(consolidado1);
		
		Divida divida1 = Fixture.divida(consolidado1, cota, new Date(), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10));
		divida1.setAcumulada(false);
		save(divida1);
		
	    Boleto boleto = Fixture.boleto("5", 
                					   new Date(), 
                					   new Date(), 
                					   new Date(), 
                					   BigDecimal.ZERO, 
                					   new BigDecimal(100.00), 
                					   "1", 
                					   "1",
                					   StatusCobranca.NAO_PAGO,
                					   cota,
                					   bancoHSBC,
                					   divida,0);
		save(boleto);		
	}
	
	@Ignore
	@Test
	public void consultaDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		List<GeraDividaDTO> lista = dividaRepository.obterDividasGeradas(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());
	}
	
	@Ignore
	@Test
	public void consultaQuantidadeDividasGeradas(){
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO();
		filtro.setDataMovimento(new Date());
		
		Long quantidade = dividaRepository.obterQuantidadeRegistroDividasGeradas(filtro);
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}
	@Ignore
	@Test
	public void consultaQuantidadeDividasGeradasPorData(){
		
		Long quantidade = dividaRepository.obterQunatidadeDividaGeradas(new Date());
		
		Assert.assertNotNull(quantidade);
		
		Assert.assertTrue(quantidade > 0);
	}
}
