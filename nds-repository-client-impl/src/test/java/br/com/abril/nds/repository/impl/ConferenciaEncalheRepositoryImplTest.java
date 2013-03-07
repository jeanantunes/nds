package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

public class ConferenciaEncalheRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;

	private static FormaCobranca formaBoleto;
	private static Distribuidor distribuidor;
	private static Banco bancoHSBC;

	private Lancamento lancamentoVeja;
	private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	private Cota cotaManoel;

	private ItemRecebimentoFisico itemRecebimentoFisico1Veja;
	private ItemRecebimentoFisico itemRecebimentoFisico2Veja;

	private ProdutoEdicao veja1;
	private ProdutoEdicao quatroRoda2;

	private CFOP cfop;
	private TipoNotaFiscal tipoNotaFiscal;
	private Usuario usuario;
	private Date dataRecebimento;

	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;

	@Before
	public void setUpGeral() {

		criarDistribuidor();

		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l, "REVISTAS", "KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l, "CROMO", "KG");
		save(ncmCromo);

		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);

		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);

		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		veja1 = Fixture.produtoEdicao(1L, 10, 7, new Long(100),
				BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 
				veja, null, false);

		quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30, new Long(100),
				BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPZ", 
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30,
				new Long(100), BigDecimal.TEN, new BigDecimal(12),
				"ABCDEFGHIJKLMNOPA", infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15,
				new Long(120), BigDecimal.TEN, BigDecimal.TEN,
				"ABCDEFGHIJKLMNOPA", capricho, null, false);

		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60,
				new Long(10), BigDecimal.ONE, new BigDecimal(1.5),
				"ABCDEFGHIJKLMNOB", cromoReiLeao, null, false);

		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);

		usuario = Fixture.usuarioJoao();
		save(usuario);

		cfop = Fixture.cfop5102();
		save(cfop);

		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento(cfop);
		save(tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal1Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);

		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);

		itemRecebimentoFisico1Veja = Fixture.itemRecebimentoFisico(
				itemNotaFiscal1Veja, recebimentoFisico1Veja,
				BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);

		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC,
						tipoNotaFiscal, usuario, BigDecimal.TEN,
						BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, usuario, notaFiscal2Veja,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				TipoLancamento.LANCAMENTO, BigInteger.valueOf(50));

		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento, dataRecebimento,
				StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);

		itemRecebimentoFisico2Veja = Fixture.itemRecebimentoFisico(
				itemNotaFiscal2Veja, recebimentoFisico2Veja,
				BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);

		lancamentoVeja = Fixture.lancamento(TipoLancamento.SUPLEMENTAR, veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), new Date(),
				new Date(), BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO,
				itemRecebimentoFisico1Veja, 1);

		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);

		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);

		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		estoqueProdutoCota = Fixture.estoqueProdutoCota(quatroRoda2,
				cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);

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
				chamadaEncalhe, false, cotaManoel, BigInteger.TEN);
		save(chamadaEncalheCota);

		/**
		 * CONTROLE CONFERENCIA ENCALHE
		 */
		ControleConferenciaEncalhe controleConferenciaEncalhe = Fixture
				.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012));
		save(controleConferenciaEncalhe);

		/**
		 * CONTROLE CONFERENCIA ENCALHE COTA
		 */
		controleConferenciaEncalheCota = Fixture
				.controleConferenciaEncalheCota(controleConferenciaEncalhe,
						cotaManoel,
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						Fixture.criarData(28, Calendar.FEBRUARY, 2012),
						StatusOperacao.CONCLUIDO, usuarioJoao, box1);

		save(controleConferenciaEncalheCota);
		
		
		
		/**
		 * MOVIMENTOS DE ENVIO ENCALHE ABAIXO
		 */
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(8), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				BigInteger.valueOf(8), BigInteger.valueOf(8), veja1);
		save(conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(1, Calendar.MARCH, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(50), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(1, Calendar.MARCH, 2012),
				BigInteger.valueOf(50), BigInteger.valueOf(50), veja1);
		save(conferenciaEncalhe);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(2, Calendar.MARCH, 2012), veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(45), cotaManoel, StatusAprovacao.APROVADO,
				"Aprovado");

		save(mec);

		conferenciaEncalhe = Fixture.conferenciaEncalhe(mec,
				chamadaEncalheCota, controleConferenciaEncalheCota,
				Fixture.criarData(2, Calendar.MARCH, 2012),
				BigInteger.valueOf(45), BigInteger.valueOf(45), veja1);
		save(conferenciaEncalhe);
		
		
	
		controleConferenciaEncalheCota = Fixture
				.controleConferenciaEncalheCota(controleConferenciaEncalhe,
						cotaManoel,
						Fixture.criarData(1, Calendar.FEBRUARY, 2013),
						Fixture.criarData(10, Calendar.FEBRUARY, 2013),
						Fixture.criarData(1, Calendar.FEBRUARY, 2013),
						StatusOperacao.EM_ANDAMENTO, usuarioJoao, box1);
		save(controleConferenciaEncalheCota);

	}

	public void criarDistribuidor() {

		bancoHSBC = Fixture.banco(10L, true, 1, "1010", 123456L, "1", "1",
				"Instrucoes.", "HSBC", "BANCO HSBC", "399", BigDecimal.ZERO,
				BigDecimal.ZERO);

		save(bancoHSBC);

		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica(
				"Distribuidor Acme", "56003315000147", "333333333333",
				"distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);

		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200),
				true, bancoHSBC, BigDecimal.ONE, BigDecimal.ONE, null);

		save(formaBoleto);

		distribuidor = null;

		PoliticaCobranca politicaCobranca = Fixture.criarPoliticaCobranca(
				distribuidor, formaBoleto, true, FormaEmissao.INDIVIDUAL_BOX);

		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);

		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));

		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(),
				politicasCobranca);

		distribuidor.setPoliticaSuspensao(politicaSuspensao);

		distribuidor.setInformacoesComplementaresProcuracao("Informacoes");
		distribuidor.setNegociacaoAteParcelas(4);

		ParametroContratoCota parametroContrato = Fixture
				.criarParametroContratoCota(
						"<font color='blue'><b>CONSIDERANDO QUE:</b></font><br>"
								+ "<br>"
								+ "<b>(i)</b>	A Contratante contempla, dentro de seu objeto social, a atividade de distribuição de livros, jornais, revistas, impressos e publicações em geral e, portanto, necessita de serviços de transporte de revistas;"
								+ "<br>"
								+ "<b>(ii)</b>	A Contratada é empresa especializada e, por isso, capaz de prestar serviços de transportes, bem como declara que possui qualificação técnica e documentação necessária para a prestação dos serviços citados acima;"
								+ "<br>"
								+ "<b>(iii)</b>	A Contratante deseja contratar a Contratada para a prestação dos serviços de transporte de revistas;"
								+ "<br>"
								+ "RESOLVEM, mútua e reciprocamente, celebrar o presente Contrato de Prestação de Serviços de Transporte de Revistas (“Contrato”), que se obrigam a cumprir, por si e seus eventuais sucessores a qualquer título, em conformidade com os termos e condições a seguir:"
								+ "<br><br>"
								+ "<font color='blue'><b>1.	OBJETO DO CONTRATO</b><br></font>"
								+ "<br>"
								+ "<b>1.1.</b>	O presente contrato tem por objeto a prestação dos serviços pela Contratada de transporte de revistas, sob sua exclusiva responsabilidade, sem qualquer relação de subordinação com a Contratante e dentro da melhor técnica, diligência, zelo e probidade, consistindo na disponibilização de veículos e motoristas que atendam a demanda da Contratante.",
						"neste ato, por seus representantes infra-assinados, doravante denominada simplesmente CONTRATADA.",
						30, 30);
		save(parametroContrato);

		distribuidor.setParametroContratoCota(parametroContrato);

		distribuidor.setFatorDesconto(BigDecimal.TEN);

		save(distribuidor);

		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);

	}

	@Test
	public void testObterListaConferenciaEncalheCota() {

		Long idControleConferenciaEncalheCota = controleConferenciaEncalheCota
				.getId();

		List<ConferenciaEncalheDTO> listaConferenciaEncalhe = conferenciaEncalheRepository
				.obterListaConferenciaEncalheDTO(idControleConferenciaEncalheCota);

		Assert.assertEquals(3, listaConferenciaEncalhe.size());

	}

	@Test
	public void testObterListaConferenciaEncalheCotaContingencia() {

		Long idDistribuidor = 1L;
		Integer numeroCota = 5637;
		Date dataRecolhimento = Fixture.criarData(1, Calendar.JANUARY, 2012);
		boolean indFechado = false;
		boolean indPostergado = false;

		Set<Long> listaIdProdutoEdicao = new HashSet<Long>();

		listaIdProdutoEdicao.add(185L);

		@SuppressWarnings("unused")
		List<ConferenciaEncalheDTO> listaConferenciaEncalhe = conferenciaEncalheRepository
				.obterListaConferenciaEncalheDTOContingencia(
						numeroCota, dataRecolhimento, indFechado,
						indPostergado, listaIdProdutoEdicao);

	}

	// TESTE SEM USO DE MASSA

	@Test
	public void testarObterDadosSlipConferenciaEncalhe() {

		List<ProdutoEdicaoSlipDTO> dadosSlip;

		Long idControleConferenciaEncalheCota = 1L;
		dadosSlip = conferenciaEncalheRepository
				.obterDadosSlipConferenciaEncalhe(
						idControleConferenciaEncalheCota);

		Assert.assertNotNull(dadosSlip);

	}

	@Test
	public void testarObterChamadaEncalheDevolucao() {

		ChamadaEncalheCota chamadaEncalheCota;

		Long idMovimentoDevolucao = 1L;

		chamadaEncalheCota = conferenciaEncalheRepository
				.obterChamadaEncalheDevolucao(idMovimentoDevolucao);

		// Assert.assertNull(chamadaEncalheCota);

	}

	@Test
	public void testarObterComposicaoCobrancaSlip() {

		List<ComposicaoCobrancaSlipDTO> composicaoCobranca;

		Integer numeroCota = 1;

		Calendar data = Calendar.getInstance();
		Date dataOperacao = data.getTime();

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();

		composicaoCobranca = conferenciaEncalheRepository
				.obterComposicaoCobrancaSlip(numeroCota, dataOperacao,
						tiposMovimentoFinanceiroIgnorados);

		Assert.assertNotNull(composicaoCobranca);

	}

	// Testa condição TipoMovimentoFinanceiro dentro de
	// obterComposicaoCobrancaSlip()
	@Test
	public void testarObterComposicaoCobrancaSlipTipoMovimentoFinanceiro() {

		List<ComposicaoCobrancaSlipDTO> composicaoCobranca;

		Integer numeroCota = 1;

		Calendar data = Calendar.getInstance();
		Date dataOperacao = data.getTime();

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
		tipoMovimentoFinanceiro.setAprovacaoAutomatica(true);
		tipoMovimentoFinanceiro.setDescricao("testeDescricao");
		tipoMovimentoFinanceiro.setId(1L);

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiro);

		composicaoCobranca = conferenciaEncalheRepository
				.obterComposicaoCobrancaSlip(numeroCota, dataOperacao,
						tiposMovimentoFinanceiroIgnorados);

		Assert.assertNotNull(composicaoCobranca);

	}

	@Test
	public void testarObterListaConferenciaEncalheDTOContigencia() {

		List<ConferenciaEncalheDTO> listaConferencia;

		Long idDistribuidor = 1L;
		Integer numeroCota = 1;

		Calendar data = Calendar.getInstance();
		
		Date dataRecolhimento = data.getTime();
		

		boolean indFechado = false;
		boolean indPostergado = false;

		Set<Long> listaIdProdutoEdicao = new HashSet<Long>();


		listaConferencia = conferenciaEncalheRepository
				.obterListaConferenciaEncalheDTOContingencia(
						numeroCota, 
						dataRecolhimento,
						indFechado,
						indPostergado, 
						listaIdProdutoEdicao
						);
		
		Assert.assertNotNull(listaConferencia);

	}
	
//	Testa condicao listaIdProdutoEdicao dentro de obterListaConferenciaEncalheDTIOContigencia();	
	@Test
	public void testarObterListaConferenciaEncalheDTOContigenciaListaIdProdutoEdicao() {

		List<ConferenciaEncalheDTO> listaConferencia;

		Long idDistribuidor = 1L;
		Integer numeroCota = 1;

		Calendar data = Calendar.getInstance();
		
		Date dataRecolhimento = data.getTime();
		
		boolean indFechado = false;
		boolean indPostergado = false;

		Set<Long> listaIdProdutoEdicao = new HashSet<Long>();
		listaIdProdutoEdicao.add(1L);
		listaIdProdutoEdicao.add(2L);
		listaIdProdutoEdicao.add(3L);


		listaConferencia = conferenciaEncalheRepository
				.obterListaConferenciaEncalheDTOContingencia(
						numeroCota, 
						dataRecolhimento, 
						indFechado,
						indPostergado, 
						listaIdProdutoEdicao
						);
		
		Assert.assertNotNull(listaConferencia);

	}
	
	@Test
	public void testarObterListaConferenciaEncalheDTO() {
		
		List<ConferenciaEncalheDTO> listaConferencia;
		
		Long idControleConferenciaEncalheCota = 1L;
		
		listaConferencia = conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(idControleConferenciaEncalheCota);
		
		Assert.assertNotNull(listaConferencia);		
		
		
	}
	
	@Test
	public void testarObterValorTotalEncalheOperacaoConferecnaiEncalhe() {
		
		BigDecimal valorTotal;
		
		Long idControleConferenciaEncalhe = 1L;
		
		valorTotal = conferenciaEncalheRepository.obterValorTotalEncalheOperacaoConferenciaEncalhe(idControleConferenciaEncalhe);
		
	}
	
	@Test
	public void testarObterReparteConferencia() {
		
		this.conferenciaEncalheRepository.obterReparteConferencia(1L, 1L, 1L);
	}
	
	
	
	@Test
	public void testarObterListaCotaConferenciaNaoFinalizada() {
		
		 List<CotaDTO> listaCota = this.conferenciaEncalheRepository.obterListaCotaConferenciaNaoFinalizada(Fixture.criarData(1, Calendar.FEBRUARY, 2013));
		 
		 Assert.assertEquals(1, listaCota.size());
		 
	}


}
