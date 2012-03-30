package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;

public class CotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaRepositoryImpl cotaRepository;
		
	private static final Integer NUMERO_COTA = 1;
	
	private Cota cota;
	private Boleto boleto1;
	private HistoricoAcumuloDivida histInadimplencia1;
	
	private Boleto boleto2;
	private HistoricoAcumuloDivida histInadimplencia2;
	private Usuario usuario;
	private PessoaJuridica pessoaJuridica;
	
	@Before
	public void setup() {
		
		pessoaJuridica = 
			Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39", "joao@gmail.com", "João da Silva");
		save(pessoaFisica);
		
		Box box = Fixture.boxReparte300();
		save(box);
		
		cota = Fixture.cota(NUMERO_COTA, pessoaFisica, SituacaoCadastro.ATIVO, box);
		cota.setSugereSuspensao(true);
		save(cota);
		
		criarEnderecoCota(cota);
		
		usuario = Fixture.usuarioJoao();
		save(usuario);
		
		
			
	}
	
	public void setupHistoricoInadimplencia() {
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroReparte = Fixture
				.tipoMovimentoFinanceiroReparte();
		save(tipoMovimentoFinanceiroReparte);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista();
		save(tipoProdutoRevista);
		
		Produto produto = Fixture.produtoBoaForma(tipoProdutoRevista);
		save(produto);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produto);
		
		save(produtoEdicaoVeja1);
		
		
		Estudo estudo = Fixture.estudo(new BigDecimal(50), new Date(), produtoEdicaoVeja1);
		save(estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(50), new BigDecimal(50), estudo, cota);
		save(estudoCota);
		
				
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
		
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10));
		save(consolidado1);
		Divida divida1 = Fixture.divida(consolidado1, cota, Fixture.criarData(1, 10, 2010), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10));
		save(divida1);
		
		boleto1  = Fixture.boleto(
				"123", 
				new Date(), 
				Fixture.criarData(10, 10, 2000), 
				new Date(), 
				new BigDecimal(20), 
				new BigDecimal(10.10),
				"tipoBaixa", 
				"acao", 
				StatusCobranca.NAO_PAGO, 
				cota, 
				bancoHSBC,
				divida1);
		save(boleto1);
		
		ConsolidadoFinanceiroCota consolidado2 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10));
		save(consolidado2);
		Divida divida2 = Fixture.divida(consolidado2, cota, Fixture.criarData(2, 10, 2010), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10));
		save(divida2);

		boleto2  = Fixture.boleto(
				"1234", 
				new Date(), 
				Fixture.criarData(1, 10, 2010), 
				new Date(), 
				BigDecimal.ZERO, 
				new BigDecimal(10.10),
				"tipoBaixa", 
				"acao", 
				StatusCobranca.NAO_PAGO, 
				cota, 
				bancoHSBC,
				divida2);
		save(boleto2);
		
		
		histInadimplencia1 = Fixture.criarHistoricoAcumuloDivida(
				divida1, new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia1);
		
		histInadimplencia2 = Fixture.criarHistoricoAcumuloDivida(
				divida2, new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia2);
		
		
		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1);
		save(politicaCobranca);
				
		Distribuidor distribuidor = Fixture.distribuidor(pessoaJuridica, new Date(), politicaCobranca);
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));
		
		distribuidor.setPoliticaSuspensao(politicaSuspensao);
		save(distribuidor);
		
		ParametroCobrancaCota parametroCobrancaConta = 
				Fixture.parametroCobrancaCota(null, null, cota, 1, 
											  formaBoleto, true, BigDecimal.TEN);
			save(parametroCobrancaConta);
		
	}
	
	public void setUpSuspensaoCota() {
		
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
		
		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuario, estoqueProdutoCota,
				new BigDecimal(20), cota, StatusAprovacao.APROVADO, "Aprovado");
		save(mec);			
	}
	
	@Test
	public void obterPorNumeroCota() {
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);
		
		Assert.assertNotNull(cota);
		
		Assert.assertEquals(NUMERO_COTA, cota.getNumeroCota());
	}
	
	@Test
	public void obterCotasSujeitasSuspensao() throws Exception {
		
		setupHistoricoInadimplencia();
		
		try {			
			
			@SuppressWarnings("rawtypes")
			List lista = cotaRepository.obterCotasSujeitasSuspensao("asc",CotaSuspensaoDTO.Ordenacao.NOME.name(),0,50);
			Assert.assertEquals(lista.size(),1);			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
		

	
	@Test
	public void obterEnderecosPorIdCotaSucesso() {
		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);
	
		Assert.assertNotNull(cota);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = 
				this.cotaRepository.obterEnderecosPorIdCota(cota.getId());

		Assert.assertNotNull(listaEnderecoAssociacao);
		
		int expectedListSize = 2;

		int actualListSize = listaEnderecoAssociacao.size();

		Assert.assertEquals(expectedListSize, actualListSize);
	}
	
	private void criarEnderecoCota(Cota cota) {
		
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", 50, "Centro", "Mococa", "SP");

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cota);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(false);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cota);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(true);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);
		
		save(endereco, enderecoCota, endereco2, enderecoCota2);
	}
	
	@Test
	public void obterValorConsignadoReparteDaCota() {
		
		setUpSuspensaoCota();
		
		List<ProdutoValorDTO> valores =  cotaRepository.obterValorConsignadoDaCota(cota.getId());
				
		Assert.assertEquals(valores.get(0).getTotal(),200.0);
		
		List<ProdutoValorDTO> valores2 =  cotaRepository.obterReparteDaCotaNoDia(cota.getId(), new Date());
		
		Assert.assertEquals(valores2.get(0).getTotal(),400.0);
		
	}
	
	@Test
	public void obterDiasConcentracaoPagamentoCota(){
		this.cotaRepository.obterDiasConcentracaoPagamentoCota(1L);
	}

}
