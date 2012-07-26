package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
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
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.Intervalo;

public class CotaRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private CotaRepository cotaRepository;
		
	private static final Integer NUMERO_COTA = 1;
	
	private Cota cota;
	private Boleto boleto1;
	private HistoricoAcumuloDivida histInadimplencia1;
	
	private Boleto boleto2;
	private HistoricoAcumuloDivida histInadimplencia2;
	private Usuario usuario;
	private PessoaJuridica pessoaJuridica;
	
	private Editor abril;
	
	@Before
	public void setup() {
		abril = Fixture.editoraAbril();
		save(abril);
		
		pessoaJuridica = 
			Fixture.pessoaJuridica("FC", "01.001.001/001-00", "000.000.000.00", "fc@mail.com", "99.999-9");

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
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebReparte = Fixture
				.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinanceiroRecebReparte);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produto = Fixture.produtoBoaForma(tipoProdutoRevista);
		produto.setEditor(abril);
		save(produto);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L,
				produto, null, false);
		
		save(produtoEdicaoVeja1);
		
		
		Estudo estudo = Fixture.estudo(new BigDecimal(50), new Date(), produtoEdicaoVeja1);
		save(estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(50), new BigDecimal(50), estudo, cota);
		save(estudoCota);
		
				
		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010",
			  		123456L, "1", "1", "Instruções.", "HSBC","BANCO HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);
		
		
		
		
		//AMARRAÇAO DIVIDA X BOLETO 
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		//save(tipoMovimentoFinenceiroReparte);
		
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
				cota, tipoMovimentoFinanceiroRecebReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec), StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);
		
		
		ConsolidadoFinanceiroCota consolidado1 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		Divida divida1 = Fixture.divida(consolidado1, cota, Fixture.criarData(1, 10, 2010), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		save(divida1);
		
		boleto1  = Fixture.boleto(
				"123", "123", "123123",
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
				divida1,0);
		save(boleto1);
		
		ConsolidadoFinanceiroCota consolidado2 = Fixture.consolidadoFinanceiroCota(null, cota, new Date(), new BigDecimal(10), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado2);
		Divida divida2 = Fixture.divida(consolidado2, cota, Fixture.criarData(2, 10, 2010), usuario, StatusDivida.EM_ABERTO, new BigDecimal(10),false);
		save(divida2);

		boleto2  = Fixture.boleto(
				"124", "123", "124123",
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
				divida2,0);
		save(boleto2);
		
		
		histInadimplencia1 = Fixture.criarHistoricoAcumuloDivida(
				divida1, new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia1);
		
		histInadimplencia2 = Fixture.criarHistoricoAcumuloDivida(
				divida2, new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia2);
		
		
		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE,null);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
				Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"","",true,FormaEmissao.INDIVIDUAL_BOX);
		save(politicaCobranca);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
				
		Distribuidor distribuidor = Fixture.distribuidor(1, pessoaJuridica, new Date(), politicasCobranca);
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));
		
		distribuidor.setPoliticaSuspensao(politicaSuspensao);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobrancaConta = 
				Fixture.parametroCobrancaCota(formasCobranca, null, null, cota, 1, 
											  true, BigDecimal.TEN);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaConta);
		formaBoleto.setPrincipal(true);
		
		save(parametroCobrancaConta);
		
	}
	
	public void setUpSuspensaoCota() {
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRST", 2L,
				produtoVeja, null, false);
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
		List<CotaSuspensaoDTO> lista = cotaRepository.obterCotasSujeitasSuspensao("asc",CotaSuspensaoDTO.Ordenacao.NOME.name(),0,50);
		Assert.assertEquals(lista.size(),1);			
	}
	
	@Test
	public void obterTotalCotasSujeitasSuspensao() throws Exception {
		setupHistoricoInadimplencia();
		Long total = cotaRepository.obterTotalCotasSujeitasSuspensao();
		Assert.assertTrue(total==1L);			
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
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", 50, "Centro", "Mococa", "SP",1);

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cota);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(false);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP",1);

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
	
	@Test
	public void obterCota(){

		
		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCota(cota.getNumeroCota());
		
		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);
		
		Assert.assertNotNull(cotas);
		
		Assert.assertTrue(!cotas.isEmpty());
	}
	
	@Test
	public void buscarCotasPorIN() {
		
		List<Long> idsCotas = new ArrayList<Long>();

		idsCotas.add(1L);
		idsCotas.add(2L);
		
		List<Cota> listaCotas = 
			this.cotaRepository.obterCotasPorIDS(idsCotas);
		
		Assert.assertNotNull(listaCotas);
	}

	@Test
	public void obterIdCotasEntre() {
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(1, 10);
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(1, 2);
		SituacaoCadastro situacao =  SituacaoCadastro.ATIVO;
		cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox,
				situacao);
		
		cotaRepository.obterIdCotasEntre(null, intervaloBox,
				situacao);
		
		cotaRepository.obterIdCotasEntre(intervaloCota, null,
				situacao);
		
		cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox,
				null);
		
		cotaRepository.obterIdCotasEntre(null, null,
				null);
	}
	
	

}
