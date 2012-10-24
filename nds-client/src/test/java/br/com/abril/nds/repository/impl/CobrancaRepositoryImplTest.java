package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO.OrdenacaoColunaDividas;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class CobrancaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private CobrancaRepository cobrancaRepository;

	private static final Integer NUMERO_COTA = 123;

	private Date dataAtual;

	private Boleto boleto;

	private Boleto boleto2;

	private Boleto boleto3;

	private BaixaAutomatica baixa;

	private BaixaAutomatica baixa2;

	private BaixaAutomatica baixa3;

	// TAREFAS ANTES DA EXECUCAO DO METODO A SER TESTADO
	@Before
	public void setup() {

		dataAtual = DateUtil.removerTimestamp(new Date());

		// CRIA UM OBJETO PESSOA NA SESSAO PARA TESTES
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("LH",
				"01.001.001/001-00", "000.000.000.00", "lh@mail.com",
				"99.999-9");
		save(pessoaJuridica);

		// CRIA UM OBJETO BOX NA SESSAO PARA TESTES
		Box box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);

		// CRIA UM OBJETO COTA NA SESSAO PARA TESTES
		Cota cota = Fixture.cota(NUMERO_COTA, pessoaJuridica,
				SituacaoCadastro.ATIVO, box);
		save(cota);

		Banco bancoHSBC = Fixture.banco(10L, true, null, "1010", 123456L, "1",
				"1", "Instruções.", "HSBC", "BANCO HSBC", "399",
				BigDecimal.ZERO, BigDecimal.ZERO);
		save(bancoHSBC);

		// AMARRAÇAO DIVIDA X BOLETO
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

		TipoMovimentoFinanceiro tipoMovimentoFinenceiroRecebimentoReparte = Fixture
				.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinenceiroRecebimentoReparte);

		TipoMovimentoEstoque tipoMovimentoRecReparte = Fixture
				.tipoMovimentoRecebimentoReparte();
		save(tipoMovimentoRecReparte);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l, "CROMO", "KG");
		save(ncmCromo);

		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);

		Editor abril = Fixture.editoraAbril();
		save(abril);

		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);

		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10,
				14, new Long(100), BigDecimal.TEN, new BigDecimal(20),
				"ABCDEFGHIJKLMNOPQ", produtoVeja, null, false);
		save(produtoEdicaoVeja1);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cota, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(
				produtoEdicaoVeja1, tipoMovimentoRecReparte, usuarioJoao,
				estoqueProdutoCota, BigInteger.valueOf(100), cota,
				StatusAprovacao.APROVADO, "Aprovado");
		save(mec);

		MovimentoFinanceiroCota movimentoFinanceiroCota = Fixture
				.movimentoFinanceiroCota(cota,
						tipoMovimentoFinenceiroRecebimentoReparte, usuarioJoao,
						new BigDecimal(200), Arrays.asList(mec),
						StatusAprovacao.APROVADO, dataAtual, true);
		save(movimentoFinanceiroCota);

		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota), cota,
						dataAtual, new BigDecimal(200), BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO);
		save(consolidado1);

		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(null, cota, dataAtual,
						new BigDecimal(200), BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO);
		save(consolidado2);

		ConsolidadoFinanceiroCota consolidado3 = Fixture
				.consolidadoFinanceiroCota(null, cota, dataAtual,
						new BigDecimal(200), BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO);
		save(consolidado3);

		Divida divida1 = Fixture
				.divida(consolidado1, cota, dataAtual, usuarioJoao,
						StatusDivida.EM_ABERTO, new BigDecimal(200), false);
		save(divida1);

		Divida divida2 = Fixture
				.divida(consolidado2, cota, dataAtual, usuarioJoao,
						StatusDivida.EM_ABERTO, new BigDecimal(200), false);
		save(divida2);

		Divida divida3 = Fixture
				.divida(consolidado3, cota, dataAtual, usuarioJoao,
						StatusDivida.EM_ABERTO, new BigDecimal(200), false);
		save(divida3);

		// CRIA UM OBJETO BOLETO NA SESSAO PARA TESTES

		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);

		ConsolidadoFinanceiroCota consolidado4 = Fixture
				.consolidadoFinanceiroCota(null, cota, dataAtual,
						new BigDecimal(10), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0),
						new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidado4);

		Divida divida4 = Fixture.divida(consolidado4, cota, dataAtual, usuario,
				StatusDivida.EM_ABERTO, new BigDecimal(10), false);
		save(divida4);

		boleto = Fixture.boleto("5", "5", "5", dataAtual, dataAtual, dataAtual,
				BigDecimal.ZERO, new BigDecimal(100.00), "1", "1",
				StatusCobranca.PAGO, cota, bancoHSBC, divida1, 0);
		save(boleto);

		boleto2 = Fixture.boleto("55", "5", "55", dataAtual, dataAtual,
				dataAtual, BigDecimal.ZERO, new BigDecimal(100.00), "1", "1",
				StatusCobranca.NAO_PAGO, cota, bancoHSBC, divida2, 0);
		save(boleto2);

		boleto3 = Fixture.boleto("555", "5", "555", dataAtual, dataAtual,
				dataAtual, BigDecimal.ZERO, new BigDecimal(100.00), "1", "1",
				StatusCobranca.NAO_PAGO, cota, bancoHSBC, divida3, 0);
		save(boleto3);

		baixa = Fixture.baixaAutomatica(boleto,
				DateUtil.removerTimestamp(dataAtual), null, null, null,
				StatusBaixa.PAGO, BigDecimal.TEN, bancoHSBC);

		baixa2 = Fixture.baixaAutomatica(boleto2,
				DateUtil.removerTimestamp(dataAtual), null, null, null,
				StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, BigDecimal.TEN,
				bancoHSBC);

		baixa3 = Fixture.baixaAutomatica(boleto3,
				DateUtil.removerTimestamp(dataAtual), null, null, null,
				StatusBaixa.PAGO_DIVERGENCIA_VALOR, BigDecimal.TEN, bancoHSBC);

		save(baixa, baixa2, baixa3);
	}

	@Test
	public void testarobterCobrancasPorIDS() {

		List<Long> listaIdsCobrancas = new ArrayList<Long>();

		listaIdsCobrancas.add(1L);
		listaIdsCobrancas.add(2L);
		listaIdsCobrancas.add(3L);

		List<Cobranca> listaCobrancas = this.cobrancaRepository
				.obterCobrancasPorIDS(listaIdsCobrancas);

		Assert.assertNotNull(listaCobrancas);
	}

	@Test
	public void testObterValorPendenteCobranca() {

		BigDecimal valorPendenteCobranca = cobrancaRepository
				.obterValorCobrancaNaoPagoDaCota(NUMERO_COTA);

		Assert.assertNotNull(valorPendenteCobranca);

		BigDecimal valorTotalCobrnaca = BigDecimal.ZERO;
		valorTotalCobrnaca = valorTotalCobrnaca.add(boleto2.getValor());
		valorTotalCobrnaca = valorTotalCobrnaca.add(boleto3.getValor());

		BigDecimal valorTotalBaixaCobrnaca = BigDecimal.ZERO;
		valorTotalBaixaCobrnaca = valorTotalBaixaCobrnaca.add(baixa2
				.getValorPago());
		valorTotalBaixaCobrnaca = valorTotalBaixaCobrnaca.add(baixa3
				.getValorPago());

		Assert.assertTrue(valorPendenteCobranca.compareTo(valorTotalCobrnaca
				.subtract(valorTotalBaixaCobrnaca)) == 0);
	}

	// TESTES SEM USO DE MASSA

	@Test
	public void testarObterDataAberturaDividas() {

		Date dataAbertura;

		Long idCota = 1L;

		dataAbertura = cobrancaRepository.obterDataAberturaDividas(idCota);

		Assert.assertNull(dataAbertura);

	}

	@Test
	public void testarObterCobrancasDaCotaEmAberto() {

		List<Cobranca> cobrancasCota;

		Long idCota = 1L;

		cobrancasCota = cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota);

		Assert.assertNotNull(cobrancasCota);

	}

	@Test
	public void testarObterCobrancaPorNossoNumero() {

		Cobranca cobranca;

		String nossoNumero = "123";

		cobranca = cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);

		Assert.assertNull(cobranca);

	}

	@Test
	public void testarIncrementarVia() {

		String[] nossoNumero = { "123", "12", "443" };

		cobrancaRepository.incrementarVia(nossoNumero);

	}

	@Test
	public void testarObterQuantidadeCobrancasPorCota() {

		long quantidadeCobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();

		quantidadeCobrancas = cobrancaRepository
				.obterQuantidadeCobrancasPorCota(filtro);

		Assert.assertNotNull(quantidadeCobrancas);

	}

	@Test
	public void testarObterQuantidadeCobrancasPorCotaDataVencimento() {

		long quantidadeCobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();

		quantidadeCobrancas = cobrancaRepository
				.obterQuantidadeCobrancasPorCota(filtro);
		Calendar data = Calendar.getInstance();
		filtro.setDataVencimento(data.getTime());

		Assert.assertNotNull(quantidadeCobrancas);

	}

	@Test
	public void testarObterQuantidadeCobrancasPorCotaStatusCobranca() {

		long quantidadeCobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();

		quantidadeCobrancas = cobrancaRepository
				.obterQuantidadeCobrancasPorCota(filtro);
		filtro.setStatusCobranca(StatusCobranca.NAO_PAGO);

		Assert.assertNotNull(quantidadeCobrancas);

	}

	@Test
	public void testarObterCobrancasPorCota() {

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	@Test
	public void testarObterCobrancasPorCotaDataVencimento() {

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		Calendar data = Calendar.getInstance();
		filtro.setDataVencimento(data.getTime());

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	@Test
	public void testarObterCobrancasPorCotaStatusCobranca() {

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setStatusCobranca(StatusCobranca.NAO_PAGO);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	// OrdenacaoColunaDividas.CODIGO
	@Test
	public void testarObterCobrancasPorCotaOrdenacaoColunaCODIGO() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "c.id");

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDividas.CODIGO);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	// OrdenacaoColunaDividas.NOME_COTA
	@Test
	public void testarObterCobrancasPorCotaOrdenacaoColunaNOMECOTA() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "c.id");

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDividas.NOME_COTA);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	// OrdenacaoColunaDividas.DATA_EMISSAO
	@Test
	public void testarObterCobrancasPorCotaOrdenacaoColunaDATAEMISSAO() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "c.id");

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDividas.DATA_EMISSAO);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	// OrdenacaoColunaDividas.DATA_VENCIMENTO
	@Test
	public void testarObterCobrancasPorCotaOrdenacaoColunaDATAVENCIMENTO() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "c.id");

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDividas.DATA_VENCIMENTO);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	// OrdenacaoColunaDividas.VALOR
	@Test
	public void testarObterCobrancasPorCotaOrdenacaoColunaVALOR() {
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "asc", "c.id");

		List<Cobranca> cobrancas;

		FiltroConsultaDividasCotaDTO filtro = new FiltroConsultaDividasCotaDTO();
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaDividas.VALOR);

		cobrancas = cobrancaRepository.obterCobrancasPorCota(filtro);

		Assert.assertNotNull(cobrancas);

	}

	@Test
	public void testarExcluirCobrancaPorIdDivida() {

		Long idDivida = 1L;

		cobrancaRepository.excluirCobrancaPorIdDivida(idDivida);

		// Assert.assertNotNull();

	}

	@Test
	public void testarObterCobrancasOrdenadasPorVencimento() {

		List<Cobranca> cobrancasPorVencimento;

		List<Long> idCobrancas = new ArrayList<Long>();
		idCobrancas.add(1L);
		idCobrancas.add(2L);
		idCobrancas.add(3L);

		cobrancasPorVencimento = cobrancaRepository
				.obterCobrancasOrdenadasPorVencimento(idCobrancas);

		Assert.assertNotNull(cobrancasPorVencimento);

	}

	@Test
	public void testarObterValorCobrancasQuitadasPorData() {

		BigDecimal valorCobranca;

		Calendar data = Calendar.getInstance();

		valorCobranca = cobrancaRepository
				.obterValorCobrancasQuitadasPorData(data.getTime());

		Assert.assertNotNull(valorCobranca);

	}

	@Test
	public void testarObterValorCobrancaNaoPagoDaCota() {

		BigDecimal valorCobranca;

		Integer numeroCota = 1;

		valorCobranca = cobrancaRepository
				.obterValorCobrancaNaoPagoDaCota(numeroCota);

		Assert.assertNotNull(valorCobranca);

	}

	@Test
	public void testarObterCobrancasEfetuadaNaDataOperacaoDistribuidor() {

		List<Cobranca> cobrancasEfetuada;

		Calendar data = Calendar.getInstance();

		cobrancasEfetuada = cobrancaRepository
				.obterCobrancasEfetuadaNaDataOperacaoDistribuidor(data
						.getTime());

		Assert.assertNotNull(cobrancasEfetuada);

	}

	@Test
	public void testarObterNossoNumeroPorMovimentoFinanceiroCota() {

		String nossoNumero;

		Long idMovimentoFinanceiro = 1L;

		nossoNumero = cobrancaRepository
				.obterNossoNumeroPorMovimentoFinanceiroCota(idMovimentoFinanceiro);

		Assert.assertNull(nossoNumero);

	}
	
	@Test
	public void testarObterDetalhesCobranca(){
		
		List<NegociacaoDividaDetalheVO> lista = this.cobrancaRepository.obterDetalhesCobranca(1L);
		
		Assert.assertNotNull(lista);
	}

}
