package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.dto.ResultadoCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO.OrdenacaoColuna;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO.OrdemColuna;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
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
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class CotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private CotaRepository cotaRepository;

	private static final Integer NUMERO_COTA = 1;
	private static final Integer NUMERO_COTA_INATIVA = 2;

	private Cota cota;
	private Cota cotaInativa;
	private Boleto boleto1;
	private HistoricoAcumuloDivida histInadimplencia1;

	private Boleto boleto2;
	private HistoricoAcumuloDivida histInadimplencia2;
	private Usuario usuario;
	private PessoaJuridica pessoaJuridica;

	private Editor abril;

	private Box box;

	@Before
	public void setup() {
		abril = Fixture.editoraAbril();
		save(abril);

		pessoaJuridica = Fixture.pessoaJuridica("FC", "01.001.001/001-00",
				"000.000.000.00", "fc@mail.com", "99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39",
				"joao@gmail.com", "João da Silva");
		save(pessoaFisica);

		box = Fixture.boxReparte300();
		save(box);

		cota = Fixture.cota(NUMERO_COTA, pessoaFisica, SituacaoCadastro.ATIVO,
				box);
		cota.setSugereSuspensao(true);

		cotaInativa = Fixture.cota(NUMERO_COTA_INATIVA, pessoaFisica,
				SituacaoCadastro.INATIVO, box);

		save(cota, cotaInativa);

		criarEnderecoCota(cota);

		usuario = Fixture.usuarioJoao();
		save(usuario);

		CotaAusente cotaAusente = Fixture.cotaAusente(new Date(), true, cota);

		save(cotaAusente);
	}

	public void setupHistoricoInadimplencia() {

		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebReparte = Fixture
				.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinanceiroRecebReparte);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);

		Produto produto = Fixture.produtoBoaForma(tipoProdutoRevista);
		produto.setEditor(abril);
		save(produto);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPQ", produto, null, false);

		save(produtoEdicaoVeja1);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(50), new Date(),
				produtoEdicaoVeja1);
		save(estudo);

		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.valueOf(50),
				BigInteger.valueOf(50), estudo, cota);
		save(estudoCota);

		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010", 123456L, "1",
				"1", "Instruções.", "HSBC", "BANCO HSBC", "399",
				BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);

		// AMARRAÇAO DIVIDA X BOLETO
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		// save(tipoMovimentoFinenceiroReparte);

		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture
				.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(
				produtoEdicaoVeja1, tipoMovimentoRecReparte, usuarioJoao,
				estoqueProdutoCota, BigInteger.valueOf(101), cota,
				StatusAprovacao.APROVADO, "Aprovado");
		save(mec);

		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture
				.movimentoFinanceiroCota(cota,
						tipoMovimentoFinanceiroRecebReparte, usuarioJoao,
						new BigDecimal(200), Arrays.asList(mec),
						StatusAprovacao.APROVADO, new Date(), true);
		save(movimentoFinanceiroCota);

		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(null, cota, new Date(),
						new BigDecimal(10), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado1);
		Divida divida1 = Fixture.divida(consolidado1, cota,
				Fixture.criarData(1, 10, 2010), usuario,
				StatusDivida.EM_ABERTO, new BigDecimal(10), false);
		save(divida1);

		boleto1 = Fixture.boleto("123", "123", "123123", new Date(),
				Fixture.criarData(10, 10, 2000), new Date(),
				new BigDecimal(20), new BigDecimal(10.10), "tipoBaixa", "acao",
				StatusCobranca.NAO_PAGO, cota, bancoHSBC, divida1, 0);
		save(boleto1);

		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(null, cota, new Date(),
						new BigDecimal(10), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado2);
		Divida divida2 = Fixture.divida(consolidado2, cota,
				Fixture.criarData(2, 10, 2010), usuario,
				StatusDivida.EM_ABERTO, new BigDecimal(10), false);
		save(divida2);

		boleto2 = Fixture.boleto("124", "123", "124123", new Date(),
				Fixture.criarData(1, 10, 2010), new Date(), BigDecimal.ZERO,
				new BigDecimal(10.10), "tipoBaixa", "acao",
				StatusCobranca.NAO_PAGO, cota, bancoHSBC, divida2, 0);
		save(boleto2);

		histInadimplencia1 = Fixture.criarHistoricoAcumuloDivida(divida1,
				new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia1);

		histInadimplencia2 = Fixture.criarHistoricoAcumuloDivida(divida2,
				new Date(), usuario, StatusInadimplencia.ATIVA);
		save(histInadimplencia2);

		FormaCobranca formaBoleto = Fixture.formaCobrancaBoleto(true,
				new BigDecimal(200), true, bancoHSBC, BigDecimal.ONE,
				BigDecimal.ONE, null);
		save(formaBoleto);

		PoliticaCobranca politicaCobranca = Fixture.criarPoliticaCobranca(null,
				formaBoleto, true, true, true, 1, "", "", true,
				FormaEmissao.INDIVIDUAL_BOX);
		save(politicaCobranca);

		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);

		Distribuidor distribuidor = Fixture.distribuidor(1, pessoaJuridica,
				new Date(), politicasCobranca);

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));

		distribuidor.setPoliticaSuspensao(politicaSuspensao);
		save(distribuidor);

		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);

		Set<FormaCobranca> formasCobranca = new HashSet<FormaCobranca>();
		formasCobranca.add(formaBoleto);
		ParametroCobrancaCota parametroCobrancaConta = Fixture
				.parametroCobrancaCota(formasCobranca, null, null, cota, 1,
						true, BigDecimal.TEN, null);
		formaBoleto.setParametroCobrancaCota(parametroCobrancaConta);
		formaBoleto.setPrincipal(true);

		save(parametroCobrancaConta);

	}

	public void setUpSuspensaoCota() {

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);

		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPA", produtoVeja, null, false);
		save(produtoEdicaoVeja1);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture
				.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(
				produtoEdicaoVeja1, tipoMovimentoRecReparte, usuario,
				estoqueProdutoCota, BigInteger.valueOf(20), cota,
				StatusAprovacao.APROVADO, "Aprovado");
		save(mec);
	}

	@Test
	public void obterPorNumeroCota() {

		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);

		Assert.assertNotNull(cota);

		Assert.assertEquals(NUMERO_COTA, cota.getNumeroCota());
	}

	@Test
	public void obterPorNumerDaCotaAtiva() {

		Cota cota = this.cotaRepository.obterPorNumerDaCotaAtiva(NUMERO_COTA);

		Assert.assertNotNull(cota);

	}

	@Test
	public void obterCotasPorNomePessoa() {
		List<Cota> cotas = this.cotaRepository
				.obterCotasPorNomePessoa(pessoaJuridica.getNome());

		Assert.assertNotNull(cotas);
	}

	@Test
	public void obterPorNome() {
		List<Cota> cotas = this.cotaRepository.obterPorNome(pessoaJuridica
				.getNome());

		Assert.assertNotNull(cotas);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterTelefonePorTelefoneCota() {
		TelefoneCota telefoneCota = this.cotaRepository
				.obterTelefonePorTelefoneCota(1L, cota.getId());

	}

	
	@Test
	public void obterCotasSujeitasSuspensao() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						CotaSuspensaoDTO.Ordenacao.NOME.name(), 0, 50);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoNulo() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao(null,
						null, null, null);
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterCotasSujeitasSuspensaoPorInicio() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao(null,
						null, 1, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoPorRp() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao(null,
						null, null, 10);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderNumCota() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"numCota", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderNome() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"nome", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderVlrConsignado() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"vlrConsignado", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderVlrReparte() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"vlrReparte", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderDividaAcumulada() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"dividaAcumulada", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderDiasAberto() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						"diasAberto", null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasSuspensaoSortOrderAsc() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("asc",
						null, null, null);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterIdCotasEntre() {
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(1, 10);
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(1, 2);
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(SituacaoCadastro.ATIVO);
		
		cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox,
				situacoesCadastro, null, null, null, null, null, null);
		
		cotaRepository.obterIdCotasEntre(null, intervaloBox,
				situacoesCadastro, null, null, null, null, null, null);
		
		cotaRepository.obterIdCotasEntre(intervaloCota, null,
				situacoesCadastro, null, null, null, null, null, null);
		
		cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox,
				null, null, null, null, null, null, null);
		
		cotaRepository.obterIdCotasEntre(null, null,
				null, null, null, null, null, null, null);
	}
	
	public void obterCotasSujeitasSuspensaoSortOrderDesc() throws Exception {
		setupHistoricoInadimplencia();
		List<CotaSuspensaoDTO> lista = cotaRepository
				.obterCotasSujeitasSuspensao("desc",
						null, null, null);
		Assert.assertNotNull(lista);

	}
	
	
	@Test
	public void obterTotalCotasSujeitasSuspensao(){
		setupHistoricoInadimplencia();
		Long total = cotaRepository.obterTotalCotasSujeitasSuspensao();
		Assert.assertTrue(total == 1L);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhe() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorNumeroConta() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setNumeroCota(1);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorFornecedor() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setFornecedor(1L);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorBox() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setBox(1L);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorRota() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRota(1L);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorRoteiro() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setRoteiro(1L);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorCodMunicipio() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodMunicipio(1);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntCotasSujeitasAntecipacoEncalhePorCodTipoPontoPDV() {
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setCodTipoPontoPDV(1L);
		long quantidade = cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQntExemplaresCotasSujeitasAntecipacoEncalhe(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		BigInteger quantidade = cotaRepository.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);		
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalhe(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalhePorPaginacao(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalheOrdenacaoBox(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setOrdenacaoColuna(OrdenacaoColuna.BOX);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalheOrdenacaoNomeCota(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NOME_COTA);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalheOrdenacaoNumeroCota(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setOrdenacaoColuna(OrdenacaoColuna.NUMERO_COTA);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalheOrdenacaoQntExemplares(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.setOrdenacaoColuna(OrdenacaoColuna.QNT_EXEMPLARES);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalhePaginacaoOrdenacaoAsc(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColuna.BOX);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotasSujeitasAntecipacoEncalhePaginacaoOrdenacaoDesc(){
		FiltroChamadaAntecipadaEncalheDTO filtro = new FiltroChamadaAntecipadaEncalheDTO();
		filtro.setOrdenacaoColuna(OrdenacaoColuna.BOX);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.DESC);
		List<ChamadaAntecipadaEncalheDTO> lista = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		Assert.assertNotNull(lista);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterEnderecoPrincipal(){
		EnderecoCota enderecoCota = cotaRepository.obterEnderecoPrincipal(1L);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterCotaPDVPorNumeroDaCota(){
		Cota cota = cotaRepository.obterCotaPDVPorNumeroDaCota(1);
	}
	
	@Test
	public void obterCotaAssociadaFiador() throws Exception {
		setupHistoricoInadimplencia();
		List<Cota> lista= cotaRepository.obterCotaAssociadaFiador(1L);
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterEnderecosPorIdCotaSucesso() {
		Cota cota = this.cotaRepository.obterPorNumerDaCota(NUMERO_COTA);

		Assert.assertNotNull(cota);

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.cotaRepository
				.obterEnderecosPorIdCota(cota.getId());

		Assert.assertNotNull(listaEnderecoAssociacao);

		int expectedListSize = 2;

		int actualListSize = listaEnderecoAssociacao.size();

		Assert.assertEquals(expectedListSize, actualListSize);
	}

	private void criarEnderecoCota(Cota cota) {

		Endereco endereco = Fixture.criarEndereco(TipoEndereco.COMERCIAL,
				"13730-000", "Rua Marechal Deodoro", "50", "Centro", "Mococa",
				"SP", 1);

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cota);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(false);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);

		Endereco endereco2 = Fixture.criarEndereco(TipoEndereco.LOCAL_ENTREGA,
				"13730-000", "Rua X", "50", "Vila Carvalho", "Mococa", "SP", 1);

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

		List<ProdutoValorDTO> valores = cotaRepository
				.obterValorConsignadoDaCota(cota.getId());

		Assert.assertNotNull(valores);
		Assert.assertEquals(valores.get(0).getTotal(), 200.0);

		List<ProdutoValorDTO> valores2 = cotaRepository
				.obterReparteDaCotaNoDia(cota.getId(), new Date());

		Assert.assertNotNull(valores2);
		Assert.assertEquals(valores2.get(0).getTotal(), 400.0);

	}

	@Test
	public void obterDiasConcentracaoPagamentoCota() {
		List<Integer> lista = cotaRepository.obterDiasConcentracaoPagamentoCota(1L);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotas() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}

	@Test
	public void obterCotasPorNumeroCota() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCota(cota.getNumeroCota());

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

		Assert.assertTrue(!cotas.isEmpty());
	}

	@Test
	public void obterCotasPorNomeCota() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNomeCota("Teste");

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}

	@Test
	public void obterCotasPorLogradouro() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setLogradouro("teste");

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasPorBairro() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setBairro("teste1");

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasPorMunicipio() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setMunicipio("municipio");

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasPorPaginacao() {
		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(10);
		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasPorNumeroCpfCnpj() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCpfCnpj("123.456.789-12");

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaNumeroCota() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.NUMERO_COTA);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaNomePessoa() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.NOME_PESSOA);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaNumeroCpfCnpj() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.NUMERO_CPF_CNPJ);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaContato() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.CONTATO);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaTelefone() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.TELEFONE);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaEmail() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.EMAIL);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaStatus() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.STATUS);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasOrdemColunaBox() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterCotasPaginacaoOrdenacao() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setOrdemColuna(OrdemColuna.BOX);

		List<CotaDTO> cotas = cotaRepository.obterCotas(filtro);

		Assert.assertNotNull(cotas);

	}
	
	@Test
	public void obterQuantidadeCotasPesquisadasNulo() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
	
	@Test
	public void obterQuantidadeCotasPesquisadasPorNumeroCota() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCota(cota.getNumeroCota());

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
	@Test
	public void obterQuantidadeCotasPesquisadasPorNumeroCpfCnpj() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCpfCnpj("123.456.789.12");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}

	@Test
	public void obterQuantidadeCotasPesquisadasPorNomeCota() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNomeCota("teste");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
	@Test
	public void obterQuantidadeCotasPesquisadasPorLogradouro() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setLogradouro("logradouro");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
	@Test
	public void obterQuantidadeCotasPesquisadasPorBairro() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setBairro("bairro");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
		
	@Test
	public void obterQuantidadeCotasPesquisadasPorMunicipio() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setMunicipio("municipio");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}
	
		
	@Test
	public void obterQuantidadeCotasPesquisadas() {

		FiltroCotaDTO filtro = new FiltroCotaDTO();
		filtro.setNumeroCota(cota.getNumeroCota());
		filtro.setNumeroCpfCnpj("123.456.789.12");
		filtro.setNomeCota("teste");
		filtro.setLogradouro("logradouro");
		filtro.setBairro("bairro");
		filtro.setMunicipio("municipio");

		Long qtde = cotaRepository.obterQuantidadeCotasPesquisadas(filtro);

		Assert.assertNotNull(qtde);

	}

	@SuppressWarnings("unused")
	@Test
	public void gerarSugestaoNumeroCota(){
		Integer numeroCota = cotaRepository.gerarSugestaoNumeroCota();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalNulo(){
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, null, null, null, null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotal(){
		List<Long> edicaoProduto = new ArrayList<>();
		edicaoProduto.add(1L);
		edicaoProduto.add(2L);
		Date dataDe = Fixture.criarData(10, Calendar.APRIL, 2012);
		Date dataAte = Fixture.criarData(10, Calendar.MAY, 2012);
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(dataDe, dataAte, "1", "1", "Teste", edicaoProduto, "1", 1, "teste", "municipio");
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorEdicaoProduto(){
		List<Long> edicaoProduto = new ArrayList<>();
		edicaoProduto.add(1L);
		edicaoProduto.add(2L);
		
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, null, edicaoProduto, null, null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorNomeProduto(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, "Teste", null, null, null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorCodigoFornecedor(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, "1", null, null, null, null, null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorCodigoEditor(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, "1", null, null, null, "1", null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorCodigoProduto(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, "1", null, null, null, null, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}

	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorCodigoCota(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, null, null, null, 1, null, null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorNomeCota(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, null, null, null, null, "teste", null);
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCurvaABCCotaTotalPorMunicipio(){
		List<Long> edicaoProduto = new ArrayList<>();
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(null, null, null, null, null, null, null, null, null, "municipio");
		ResultadoCurvaABCCotaDTO resultadoCurvaABCCotaDTO = cotaRepository.obterCurvaABCCotaTotal(filtro);
		
	}
	
	
	
	@Test
	public void buscarCotasPorIN() {

		List<Long> idsCotas = new ArrayList<Long>();

		idsCotas.add(1L);
		idsCotas.add(2L);

		List<Cota> listaCotas = this.cotaRepository.obterCotasPorIDS(idsCotas);

		Assert.assertNotNull(listaCotas);
	}
	
	@Test
	public void obterCotasPorIDSNulo() {

		List<Long> idsCotas = null;

		List<Cota> listaCotas = this.cotaRepository.obterCotasPorIDS(idsCotas);

		Assert.assertNull(listaCotas);
	}
	
	@Test
	public void obterIdCotasEntreNull() {
		Set<Long> set = cotaRepository.obterIdCotasEntre(null, null, null,
				null, null, null, null, null, null);
		
		Assert.assertNotNull(set);
	}

	@Test
	public void obterIdCotasEntrePorIntervaloCota() {

		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(1, 10);
		Set<Long> set = cotaRepository.obterIdCotasEntre(intervaloCota, null, null,
				null, null, null, null, null, null);
		
		Assert.assertNotNull(set);
	}
	
	@Test
	public void obterIdCotasEntrePorIntervaloBox() {
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(1, 2);
		Set<Long> set = cotaRepository.obterIdCotasEntre(null, intervaloBox, null,
				null, null, null, null, null, null);
		Assert.assertNotNull(set);
	}
	
	@Test
	public void obterIdCotasEntrePorSituacao() {
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(SituacaoCadastro.ATIVO);
		
		Set<Long> set = cotaRepository.obterIdCotasEntre(null, null, situacoesCadastro,
				null, null, null, null, null, null);
		Assert.assertNotNull(set);
	}
	
	@Test
	public void obterIdCotasEntrePorIdRoteiro() {
		Set<Long> set = cotaRepository.obterIdCotasEntre(null, null, null,
				1L, null, null, null, null, null);
		Assert.assertNotNull(set);
	}
	
	@Test
	public void obterIdCotasEntrePorIdRota() {
		Set<Long> set = cotaRepository.obterIdCotasEntre(null, null, null,
				null, 1L, null, null, null, null);
		Assert.assertNotNull(set);
	}
	
	@Test
	public void obterQuantidadeCotasAtivas() {

		final Long quantidadeEsperada = 1L;

		final Long quantidadeObtida = this.cotaRepository
				.obterQuantidadeCotas(SituacaoCadastro.ATIVO);

		Assert.assertEquals(quantidadeEsperada, quantidadeObtida);
	}

	
	@Test
	public void obterQuantidadeCotasInativas() {

		final Long quantidadeEsperada = 1L;

		final Long quantidadeObtida = this.cotaRepository
				.obterQuantidadeCotas(SituacaoCadastro.INATIVO);

		Assert.assertEquals(quantidadeEsperada, quantidadeObtida);
	}
	
	@Test
	public void obterCotasPorFornecedor() {
		Set<Cota> set = cotaRepository.obterCotasPorFornecedor(1L);
		Assert.assertNotNull(set);
	}

	@Test
	public void obterQuantidadeTotalCotas() {

		final Long quantidadeEsperada = 2L;

		final Long quantidadeObtida = this.cotaRepository
				.obterQuantidadeCotas(null);

		Assert.assertEquals(quantidadeEsperada, quantidadeObtida);
	}

	@Test
	public void obterCotasInativas() {

		final int quantidadeEsperada = 1;

		final List<Cota> cotasInativasObtidas = this.cotaRepository
				.obterCotas(SituacaoCadastro.INATIVO);

		Assert.assertNotNull(cotasInativasObtidas);

		Assert.assertEquals(quantidadeEsperada, cotasInativasObtidas.size());

		final Integer numeroCota = cotasInativasObtidas.get(0).getNumeroCota();

		Assert.assertEquals(NUMERO_COTA_INATIVA, numeroCota);
	}
	
	
	@Test
	public void obterCotasAtivas() {

		final List<Cota> cotasInativasObtidas = this.cotaRepository
				.obterCotas(SituacaoCadastro.ATIVO);

		Assert.assertNotNull(cotasInativasObtidas);

	}
	
	@Test
	public void obterCotasNulo() {
		SituacaoCadastro situacaoCadastro = null;

		final List<Cota> cotasInativasObtidas = this.cotaRepository
				.obterCotas(situacaoCadastro);

		Assert.assertNotNull(cotasInativasObtidas);

	}

	@Test
	public void obterNovasCotas() {

		final int quantidadeEsperada = 2;

		final List<Cota> novasCotas = this.cotaRepository
				.obterCotasComInicioAtividadeEm(new Date());

		Assert.assertNotNull(novasCotas);

		Assert.assertEquals(quantidadeEsperada, novasCotas.size());
	}

	@Test
	public void obterNovasCotasNulo() {

		final List<Cota> novasCotas = this.cotaRepository
				.obterCotasComInicioAtividadeEm(null);

		Assert.assertNotNull(novasCotas);

	}
	
	
	@Test
	public void obterCotasAusentesNaExpedicaoDoReparte() {

		final int quantidadeEsperada = 1;

		final List<Cota> cotasAusentesExpedicaoReparte = this.cotaRepository
				.obterCotasAusentesNaExpedicaoDoReparteEm(new Date());

		Assert.assertNotNull(cotasAusentesExpedicaoReparte);

		Assert.assertEquals(quantidadeEsperada,
				cotasAusentesExpedicaoReparte.size());
	}

	@Test
	public void obterCotasAusentesNaExpedicaoDoReparteNulo() {

		final List<Cota> cotasAusentesExpedicaoReparte = this.cotaRepository
				.obterCotasAusentesNaExpedicaoDoReparteEm(null);

		Assert.assertNotNull(cotasAusentesExpedicaoReparte);

	}
	
	@Test
	public void obterCotasAusentesNoRecolhimentoDeEncalhe() {

		this.carregarDadosControleConferenciaEncalhe();

		final int quantidadeEsperada = 1;

		final Date dataRecolhimentoEncalhe = Fixture.criarData(28,
				Calendar.FEBRUARY, 2012);

		final List<Cota> cotasAusentesRecolhimentoEncalhe = this.cotaRepository
				.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataRecolhimentoEncalhe);

		Assert.assertNotNull(cotasAusentesRecolhimentoEncalhe);

		Assert.assertEquals(quantidadeEsperada,
				cotasAusentesRecolhimentoEncalhe.size());
	}
	
	@Test
	public void obterCotaPorTipoPorNumCota(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, "numCota", "asc");
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotaPorTipoPorNome(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, "nome", "asc");
		Assert.assertNotNull(lista);
	}

	@Test
	public void obterCotaPorTipoPorMunicipio(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, "municipio", "asc");
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotaPorTipoPorEndereco(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, "endereco", "asc");
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotaSortOrderDesc(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, "numCota", "desc");
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterCotaNulo(){
		List<CotaTipoDTO> lista = cotaRepository.obterCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 1, 2, null, null);
		Assert.assertNotNull(lista);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCountCotaPorTipo(){
		int count = cotaRepository.obterCountCotaPorTipo(TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO);
	}
	
	@Test
	public void obterQtdeCotaMunicipio(){
		List<MunicipioDTO> lista = cotaRepository.obterQtdeCotaMunicipio(1, 1, null, null);
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterQtdeCotaMunicipioSortNameMunicipio(){
		List<MunicipioDTO> lista = cotaRepository.obterQtdeCotaMunicipio(1, 1, "municipio", "asc");
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterQtdeCotaMunicipioSortNameQtde(){
		List<MunicipioDTO> lista = cotaRepository.obterQtdeCotaMunicipio(1, 1, "qtde", "asc");
		
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterQtdeCotaMunicipioSortOrderDesc(){
		List<MunicipioDTO> lista = cotaRepository.obterQtdeCotaMunicipio(1, 1, "qtde", "desc");
		
		Assert.assertNotNull(lista);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterCountQtdeCotaMunicipio(){
		int count = cotaRepository.obterCountQtdeCotaMunicipio();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterHistoricoTitularidade(){
		HistoricoTitularidadeCota historicoTitularidadeCota = cotaRepository.obterHistoricoTitularidade(1L, 1L);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterFormaPagamentoHistoricoTitularidade(){
		HistoricoTitularidadeCotaFormaPagamento historicoTitularidadeCotaFormaPagamento = 
				cotaRepository.obterFormaPagamentoHistoricoTitularidade(1L);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterSocioHistoricoTitularidade(){
		HistoricoTitularidadeCotaSocio historico = cotaRepository.obterSocioHistoricoTitularidade(1L);
	}
	
	@Test
	public void ativarCota(){
		cotaRepository.ativarCota(1);
	}
	
	private void carregarDadosControleConferenciaEncalhe() {

		TipoFornecedor tipoFornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		

		Fornecedor fornecedorDinap = Fixture
				.fornecedorDinap(tipoFornecedorPublicacao);

		save(fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");

		save(ncmRevistas);

		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);

		save(tipoRevista);

		Produto veja = Fixture.produtoVeja(tipoRevista);

		veja.addFornecedor(fornecedorDinap);

		save(veja);

		ProdutoEdicao veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100),
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPA", veja,
				null, false);

		save(veja1);

		CFOP cfop = Fixture.cfop5102();

		save(cfop);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);

		save(tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorDinap,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);

		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal1Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));

		save(itemNotaFiscal1Veja);

		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);

		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);

		save(recebimentoFisico1Veja);

		ItemRecebimentoFisico itemRecebimentoFisico1Veja = Fixture
				.itemRecebimentoFisico(itemNotaFiscal1Veja,
						recebimentoFisico1Veja, BigInteger.valueOf(50));

		save(itemRecebimentoFisico1Veja);

		Lancamento lancamentoVeja = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisico1Veja, 1);

		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico1Veja);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cota, BigInteger.TEN, BigInteger.ZERO);

		save(estoqueProdutoCota);

		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture
				.tipoMovimentoEnvioEncalhe();

		save(tipoMovimentoEnvioEncalhe);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), veja1,
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalhe);

		/**
		 * CHAMADA ENCALHE COTA
		 */
		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, false, cota, BigInteger.TEN);

		save(chamadaEncalheCota);
	}
	

}