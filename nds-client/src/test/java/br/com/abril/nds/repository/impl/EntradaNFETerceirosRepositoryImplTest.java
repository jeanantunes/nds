package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosPendentesDTO;
import br.com.abril.nds.dto.ConsultaEntradaNFETerceirosRecebidasDTO;
import br.com.abril.nds.dto.ItemNotaFiscalPendenteDTO;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros;
import br.com.abril.nds.dto.filtro.FiltroEntradaNFETerceiros.TipoNota;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EntradaNFETerceirosRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class EntradaNFETerceirosRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private EntradaNFETerceirosRepository entradaNFETerceirosRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Before
	public void setUp() {

		TipoFornecedor tipoFornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);

		Fornecedor fornecedorDinap = Fixture
				.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorDinap.setCodigoInterface(9999999);
		fornecedorDinap.setResponsavel("Maria");
		fornecedorDinap.setOrigem(Origem.MANUAL);
		fornecedorDinap.setEmailNfe("maria@email.com");

		save(fornecedorDinap);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		PessoaFisica johnyDasNotas = Fixture.pessoaFisica("65485291673",
				"johny@discover.com.br", "Johny da Silva");

		save(johnyDasNotas);

		Cota cotaJohnyConsultaEncalhe = Fixture.cota(2700, johnyDasNotas,
				SituacaoCadastro.ATIVO, box1);

		save(cotaJohnyConsultaEncalhe);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		CFOP cfop5102 = Fixture.cfop5102();

		save(cfop5102);

		TipoNotaFiscal tipoNotaFiscalRecebimento = Fixture
				.tipoNotaFiscalRecebimento(cfop5102);

		save(tipoNotaFiscalRecebimento);

		String naturezaOperacao = "00001";
		String formaPagamento = "00002";
		String horaSaida = "12:00";
		String ambiente = "000005";
		String protocolo = "321321";
		String versao = "0000000885";
		String emissorInscricaoEstadualSubstituto = "111.565.365.25";
		String emissorInscricaoMunicipal = "854.632.002.54";
		BigDecimal valorBaseICMS = BigDecimal.TEN;
		BigDecimal valorICMS = BigDecimal.TEN;
		BigDecimal valorBaseICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorICMSSubstituto = BigDecimal.TEN;
		BigDecimal valorProdutos = BigDecimal.TEN;
		BigDecimal valorFrete = BigDecimal.TEN;
		BigDecimal valorSeguro = BigDecimal.TEN;
		BigDecimal valorOutro = BigDecimal.TEN;
		BigDecimal valorIPI = BigDecimal.TEN;
		BigDecimal valorNF = BigDecimal.TEN;
		Integer frete = 0;
		String transportadoraCNPJ = "123.235.284.663-52";
		String transportadoraNome = "TRANS. ABRIL LTDA";
		String transportadoraInscricaoEstadual = "465.456.878.34-785";
		String transportadoraEndereco = "Rua Laranjeiras";
		String transportadoraMunicipio = "Cachoeira do Itapemirim";
		String transportadoraUF = "SP";
		String transportadoraQuantidade = "100";
		String transportadoraEspecie = "0544400";
		String transportadoraMarca = "NOVA TRANSP.";
		String transportadoraNumeracao = "1111111111222";
		BigDecimal transportadoraPesoBruto = BigDecimal.TEN;
		BigDecimal transportadoraPesoLiquido = BigDecimal.TEN;
		String transportadoraANTT = "3454-4454545-345345-54";
		String transportadoraPlacaVeiculo = "BYS9012";
		String transportadoraPlacaVeiculoUF = "SP";
		BigDecimal ISSQNTotal = BigDecimal.TEN;
		BigDecimal ISSQNBase = BigDecimal.TEN;
		BigDecimal ISSQNValor = BigDecimal.TEN;
		String informacoesComplementares = "NENHUMA INFO A COMPLEMENTAR";
		String numeroFatura = "222222233354";
		BigDecimal valorFatura = BigDecimal.TEN;

		NotaFiscalEntradaCota notaFiscalEntradaCota = Fixture
				.notaFiscalEntradaCotaNFE(cfop5102, 222220000202L,
						"220202022220", "2000022", cotaJohnyConsultaEncalhe,
						StatusEmissaoNfe.NFE_AUTORIZADA,
						TipoEmissaoNfe.CONTINGENCIA_DPEC,
						tipoNotaFiscalRecebimento, usuarioJoao, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN, true,
						naturezaOperacao, formaPagamento, horaSaida, ambiente,
						protocolo, versao, emissorInscricaoEstadualSubstituto,
						emissorInscricaoMunicipal, valorBaseICMS, valorICMS,
						valorBaseICMSSubstituto, valorICMSSubstituto,
						valorProdutos, valorFrete, valorSeguro, valorOutro,
						valorIPI, valorNF, frete, transportadoraCNPJ,
						transportadoraNome, transportadoraInscricaoEstadual,
						transportadoraEndereco, transportadoraMunicipio,
						transportadoraUF, transportadoraQuantidade,
						transportadoraEspecie, transportadoraMarca,
						transportadoraNumeracao, transportadoraPesoBruto,
						transportadoraPesoLiquido, transportadoraANTT,
						transportadoraPlacaVeiculo,
						transportadoraPlacaVeiculoUF, ISSQNTotal, ISSQNBase,
						ISSQNValor, informacoesComplementares, numeroFatura,
						valorFatura);

		save(notaFiscalEntradaCota);

		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor = Fixture
				.notaFiscalEntradaFornecedorNFE(cfop5102, 11011110L,
						"11111000", "11101011101", fornecedorDinap,
						StatusEmissaoNfe.NFE_AUTORIZADA,
						TipoEmissaoNfe.CONTINGENCIA_DPEC,
						tipoNotaFiscalRecebimento, usuarioJoao, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN, true,
						naturezaOperacao, formaPagamento, horaSaida, ambiente,
						protocolo, versao, emissorInscricaoEstadualSubstituto,
						emissorInscricaoMunicipal, valorBaseICMS, valorICMS,
						valorBaseICMSSubstituto, valorICMSSubstituto,
						valorProdutos, valorFrete, valorSeguro, valorOutro,
						valorIPI, valorNF, frete, transportadoraCNPJ,
						transportadoraNome, transportadoraInscricaoEstadual,
						transportadoraEndereco, transportadoraMunicipio,
						transportadoraUF, transportadoraQuantidade,
						transportadoraEspecie, transportadoraMarca,
						transportadoraNumeracao, transportadoraPesoBruto,
						transportadoraPesoLiquido, transportadoraANTT,
						transportadoraPlacaVeiculo,
						transportadoraPlacaVeiculoUF, ISSQNTotal, ISSQNBase,
						ISSQNValor, informacoesComplementares, numeroFatura,
						valorFatura);

		save(notaFiscalEntradaFornecedor);

		NCM ncmRevista = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevista);

		TipoProduto tipoProdutoRevista = Fixture.tipoProduto("Revistas",
				GrupoProduto.REVISTA, ncmRevista, "4902.90.00", 001L);
		save(tipoProdutoRevista);

		Produto produtoCE = Fixture.produto("00086", "Produto CE 3",
				"ProdutoCE_3", PeriodicidadeProduto.MENSAL, tipoProdutoRevista,
				5, 5, new Long(10000), TributacaoFiscal.TRIBUTADO);
		save(produtoCE);

		ProdutoEdicao produtoEdicaoCE = Fixture.produtoEdicao("COD_PP", 86L,
				10, 7, new Long(100), BigDecimal.TEN, new BigDecimal(90),
				"EZ8", produtoCE, null, false, "Produto CE 3");
		save(produtoEdicaoCE);

		EstoqueProdutoCota estoqueProdutoCotaJohny = Fixture
				.estoqueProdutoCota(produtoEdicaoCE, cotaJohnyConsultaEncalhe,
						BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCotaJohny);

		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				produtoEdicaoCE, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		save(chamadaEncalhe);

		ChamadaEncalheCota chamadaEncalheCota_3 = Fixture
				.chamadaEncalheCota(chamadaEncalhe, false,
						cotaJohnyConsultaEncalhe, BigInteger.TEN);
		save(chamadaEncalheCota_3);

		ControleConferenciaEncalhe controleConferenciaEncalhe = Fixture
				.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);

		ControleConferenciaEncalheCota controleConferenciaEncalheCota = Fixture
				.controleConferenciaEncalheCota(controleConferenciaEncalhe,
						cotaJohnyConsultaEncalhe,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						StatusOperacao.CONCLUIDO, usuarioJoao, box1);

		List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
		notaFiscalEntradaCotas.add(notaFiscalEntradaCota);
		controleConferenciaEncalheCota
				.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);

		save(controleConferenciaEncalheCota);

	}

	@Test
	public void buscarItensPorNota() {

		List<ItemNotaFiscalPendenteDTO> lista = this.entradaNFETerceirosRepository
				.buscarItensPorNota(1L, null, null, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarNFNotasRecebidas() {
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(new PaginacaoVO());

		List<ConsultaEntradaNFETerceirosRecebidasDTO> lista = entradaNFETerceirosRepository
				.buscarNFNotasRecebidas(filtro, false);

		Assert.assertNotNull(lista);

		int tamanhoEsperado = 2;

		Assert.assertEquals(tamanhoEsperado, lista.size());

	}

	@Test
	public void buscarNFNotasPendentes() {
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(new PaginacaoVO());

		List<ConsultaEntradaNFETerceirosPendentesDTO> lista = entradaNFETerceirosRepository
				.buscarNFNotasPendentes(filtro, false);

		Assert.assertNotNull(lista);

		int tamanhoEsperado = 1;

//		Assert.assertEquals(tamanhoEsperado, lista.size());

	}

	public void buscarQtdeNFNotasRecebidas() {
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.RECEBIDA);
		filtro.setPaginacao(new PaginacaoVO());

		Integer tamanho = entradaNFETerceirosRepository
				.buscarTotalNotas(filtro);

		Assert.assertNotNull(tamanho);

		Integer tamanhoEsperado = 2;

		Assert.assertEquals(tamanhoEsperado, tamanho);

	}

	@Test
	public void buscarQtdeNFNotasPendentes() {
		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO);
		filtro.setPaginacao(new PaginacaoVO());

		Integer tamanho = entradaNFETerceirosRepository
				.buscarTotalNotas(filtro);

		Assert.assertNotNull(tamanho);

		Integer tamanhoEsperado = 1;

//		Assert.assertEquals(tamanhoEsperado, tamanho);

	}

	// TESTES SEM USO DE MASSA

	@Test
	public void testarBuscarNFNotasRecebidas() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	// Testes getSqlFromEWhereNotaEntrada() com buscarNFNotasRecebidas()
	// TipoNota
	@Test
	public void testarGetSqlFromEWhereNotaEntradaTipoNota() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		filtro.setTipoNota(TipoNota.TODAS);
		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	// Cota
	@Test
	public void testarGetSqlFromEWhereNotaEntradaCota() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.RECEBIDA);

		Cota cota = new Cota();
		cota.setId(1L);

		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	// Fornecedor
	@Test
	public void testarGetSqlFromEWhereNotaEntradaFornecedor() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		filtro.setFornecedor(fornecedor);

		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	// DataInicial / DataFinal
	@Test
	public void testarGetSqlFromEWhereNotaEntradaDataInicialEDataFinal() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);

		Calendar data = Calendar.getInstance();
		filtro.setDataInicial(data.getTime());
		filtro.setDataFinal(data.getTime());

		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	// Testes getOrderBy() com buscarNFNotasRecebidas()
	// StatusNotaFiscalEntrada
	@Test
	public void testarGetOrderByStatusNotaFiscalEntrada() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosRecebidasDTO> buscaNFNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.RECEBIDA);

		boolean limitar = false;

		buscaNFNotas = entradaNFETerceirosRepository.buscarNFNotasRecebidas(
				filtro, limitar);

		Assert.assertNotNull(buscaNFNotas);

	}

	@Test
	public void testarBuscarTotalNotas() {

		Integer totalNotas;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.RECEBIDA);

		PessoaJuridica juridica = new PessoaJuridica();
		juridica.setId(1L);

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(1L);
		fornecedor.setJuridica(juridica);
		filtro.setFornecedor(fornecedor);

		totalNotas = entradaNFETerceirosRepository.buscarTotalNotas(filtro);

		Assert.assertNotNull(totalNotas);
	}

	@Test
	public void testarBuscarNFNotasPendentes() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosPendentesDTO> notasPendentes;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		boolean limitar = false;

		notasPendentes = entradaNFETerceirosRepository.buscarNFNotasPendentes(
				filtro, limitar);

		Assert.assertNotNull(notasPendentes);

	}

	// Teste getSqlFromEWhereNotaPendente() com buscarNFNotasPendente()
	// TipoNota
	@Test
	public void testarGetSqlFromEWhereNotaPendenteTipoNota() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosPendentesDTO> notasPendentes;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		filtro.setTipoNota(TipoNota.TODAS);
		boolean limitar = false;

		notasPendentes = entradaNFETerceirosRepository.buscarNFNotasPendentes(
				filtro, limitar);

		Assert.assertNotNull(notasPendentes);

	}

	// Cota
	@Test
	public void testarGetSqlFromEWhereNotaPendenteCota() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosPendentesDTO> notasPendentes;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);
		filtro.setStatusNotaFiscalEntrada(StatusNotaFiscalEntrada.PENDENTE_RECEBIMENTO);
		filtro.setTipoNota(TipoNota.TODAS);

		Cota cota = new Cota();
		cota.setId(1L);

		filtro.setCota(cota);
		boolean limitar = false;

		notasPendentes = entradaNFETerceirosRepository.buscarNFNotasPendentes(
				filtro, limitar);

		Assert.assertNotNull(notasPendentes);

	}

	// DataInicial / DataFinal
	@Test
	public void testarGetSqlFromEWhereNotaPendenteDataInicialEDataFinal() {

		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc");

		List<ConsultaEntradaNFETerceirosPendentesDTO> notasPendentes;

		FiltroEntradaNFETerceiros filtro = new FiltroEntradaNFETerceiros();
		filtro.setPaginacao(paginacao);

		Calendar data = Calendar.getInstance();
		filtro.setDataInicial(data.getTime());
		filtro.setDataFinal(data.getTime());
		Cota cota = new Cota();
		cota.setId(1L);

		boolean limitar = false;

		notasPendentes = entradaNFETerceirosRepository.buscarNFNotasPendentes(
				filtro, limitar);

		Assert.assertNotNull(notasPendentes);

	}

	@Test
	public void testarBuscarItensPorNota() {

		List<ItemNotaFiscalPendenteDTO> buscarItens;

		Long idConferenciaCota = 1L;
		String orderBy = "teste";
		Integer firstResult = 1;
		Integer maxResults = 2;

		buscarItens = entradaNFETerceirosRepository.buscarItensPorNota(
				idConferenciaCota, orderBy, Ordenacao.ASC, firstResult,
				maxResults);

		Assert.assertNotNull(buscarItens);
	}
	
	@Test
	public void testarBuscarTodasItensPorNota() {
		
		Integer todosItens;
		
		Long idConferenciaCota = 1L;
		
		todosItens = entradaNFETerceirosRepository.buscarTodasItensPorNota(idConferenciaCota);
		
		Assert.assertNotNull(todosItens);		
		
	}

}
