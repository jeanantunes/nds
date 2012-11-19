package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetencaoICMSTransporte;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresRetencoesTributos;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ProdutoServicoRepository;

public class ProdutoServicoRepositoryImplTest extends
		AbstractRepositoryImplTest {

	private ProdutoServico produtoServico;
	private ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCota;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepositoryImpl;

	@Before
	public void setup() {
		Integer cfop = 1;

		Long codigoBarras = 1L;
		String descricaoProduto = "";
		EncargoFinanceiro encargoFinanceiro = null;
		Long extipi = 1L;
		Long ncm = 1L;
		BigInteger quantidade = BigInteger.ZERO;
		String unidade = "";
		BigDecimal valorDesconto = BigDecimal.ZERO;
		BigDecimal valorFrete = BigDecimal.ZERO;
		BigDecimal valorOutros = BigDecimal.ZERO;
		BigDecimal valorSeguro = BigDecimal.ZERO;
		BigDecimal valorTotalBruto = BigDecimal.ZERO;
		BigDecimal valorUnitario = BigDecimal.ZERO;
		int contador = 950;
		String codigoProduto = "" + contador;
		String nomeProduto = "produto_" + contador;
		String descProduto = "";
		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.ANUAL;
		int produtoPeb = 1;
		int produtoPacotePadrao = 1;
		Long produtoPeso = new Long(10000);

		String codigoProdutoEdicao = contador + "";
		Long numeroEdicao = new Long(contador);
		int pacotePadrao = 1;
		int peb = 1;
		Long peso = new Long(0);
		BigDecimal precoCusto = BigDecimal.ZERO;
		BigDecimal precoVenda = BigDecimal.ZERO;
		String codigoDeBarras = contador + "";
		BigDecimal expectativaVenda = BigDecimal.ZERO;
		boolean parcial = false;
		NCM ncmFigurinha = Fixture.ncm(48205000l, "CROMO", "KG");
		save(ncmFigurinha);
		TipoProduto tipoProdutoCromo = Fixture.tipoProduto("Cromos",
				GrupoProduto.CROMO, ncmFigurinha, "4908.90.00", 004L);
		save(tipoProdutoCromo);
		TipoFornecedor tipoFornecedorPublicacao = Fixture
				.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		Fornecedor fornecedorDinap = Fixture
				.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorDinap);
		ProdutoEdicao produtoEdicaoCE = null;
		Produto produtoCE = Fixture.produto(codigoProduto, descProduto,
				nomeProduto, periodicidade, tipoProdutoCromo, produtoPeb,
				produtoPacotePadrao, produtoPeso, TributacaoFiscal.TRIBUTADO);
		produtoCE.addFornecedor(fornecedorDinap);
		save(produtoCE);
		produtoEdicaoCE = Fixture.produtoEdicao(codigoProdutoEdicao,
				numeroEdicao, pacotePadrao, peb, peso, precoCusto, precoVenda,
				codigoDeBarras, produtoCE, expectativaVenda, parcial,
				descProduto);
		save(produtoEdicaoCE);
		CFOP cfop5102 = Fixture.cfop5102();
		save(cfop5102);
		TipoNotaFiscal tipoNotaFiscalRecebimento = Fixture
				.tipoNotaFiscalRecebimento(cfop5102);
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		NotaFiscal notaFiscal = criarNovaNotaFiscal();

		codigoProduto = String.valueOf(contador);
		produtoServico = Fixture.produtoServico(
				Integer.valueOf(contador), cfop, codigoBarras, codigoProduto,
				descricaoProduto, encargoFinanceiro, extipi, ncm, notaFiscal,
				produtoEdicaoCE, quantidade, unidade, valorDesconto,
				valorFrete, valorOutros, valorSeguro, valorTotalBruto,
				valorUnitario);
		
		save(produtoServico);
		Produto cromoBrasileirao = Fixture.produto("3333", "Cromo Brasileirão", "Cromo Brasileirão", PeriodicidadeProduto.ANUAL, tipoProdutoCromo, 5, 5, new Long(10000), TributacaoFiscal. TRIBUTADO);
		cromoBrasileirao.addFornecedor(fornecedorDinap);
		
		ProdutoEdicao cromoBrasileiraoEd1 = Fixture.produtoEdicao("COD_FF", 1L, 5, 30,
				new Long(50), new BigDecimal(100), new BigDecimal(100), "3333", cromoBrasileirao, null, false,"Cromo Brasileirão");
		
		TipoMovimentoEstoque tipoMovimentoEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEncalhe);
		
		Box box2 = Fixture.criarBox(2, "BX-002", TipoBox.LANCAMENTO);
		save(box2);
		Pessoa guilherme = Fixture.pessoaFisica("99933355511", "sys.discover@gmail.com", "Guilherme de Morais Leandro");
		save(guilherme);
		
		Cota cota = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO,box2);
		save(cota);
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoCE, cota, BigInteger.valueOf(10), BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		
		MovimentoEstoqueCota movimento = Fixture.movimentoEstoqueCota(cromoBrasileiraoEd1, tipoMovimentoEncalhe,
				usuario, estoqueProdutoCota, BigInteger.valueOf(10), cota, StatusAprovacao.APROVADO, "motivo");
		
		listaMovimentoEstoqueCota = new ArrayList<>();
		listaMovimentoEstoqueCota.add(movimento);
		
		itemNotaFiscal.setIdProdutoEdicao(1L);
		itemNotaFiscal.setQuantidade(BigInteger.TEN);
		itemNotaFiscal.setValorUnitario(BigDecimal.ONE);
		itemNotaFiscal.setCstICMS("teste");
		itemNotaFiscal.setListaMovimentoEstoqueCota(listaMovimentoEstoqueCota);

	}

	@Test
	public void atualizarProdutosQuePossuemNota() {
		List<ProdutoServico> listaProdutoServico = new ArrayList<>();
		listaProdutoServico.add(produtoServico);
		List<ItemNotaFiscal> listItemNotaFiscal = new ArrayList<>();
		listItemNotaFiscal.add(itemNotaFiscal);
		
		produtoServicoRepositoryImpl.atualizarProdutosQuePossuemNota(listaProdutoServico, listItemNotaFiscal);

	}
	
	@Test
	public void atualizarProdutosQuePossuemNotaProdutoServicoListaMovimentoEstoque() {
		List<ProdutoServico> listaProdutoServico = new ArrayList<>();
		listaProdutoServico.add(produtoServico);
		produtoServico.setListaMovimentoEstoqueCota(listaMovimentoEstoqueCota);
		List<ItemNotaFiscal> listItemNotaFiscal = new ArrayList<>();
		listItemNotaFiscal.add(itemNotaFiscal);
		
		produtoServicoRepositoryImpl.atualizarProdutosQuePossuemNota(listaProdutoServico, listItemNotaFiscal);

	}

	private NotaFiscal criarNovaNotaFiscal() {

		Date dataEmissao = Fixture.criarData(01, Calendar.JANUARY, 2012);
		Date dataEntradaContigencia = Fixture.criarData(01, Calendar.JANUARY,
				2012);
		Date dataSaidaEntrada = Fixture.criarData(01, Calendar.JANUARY, 2012);
		String descricaoNaturezaOperacao = "Natureza";
		Integer digitoVerificadorChaveAcesso = 1;
		FormaPagamento formaPagamento = FormaPagamento.A_VISTA;
		Date horaSaidaEntrada = new Date();
		String justificativaEntradaContigencia = "Justificativa";
		List<NotaFiscalReferenciada> listReferenciadas = null;
		Long numeroDocumentoFiscal = 1234L;
		Integer serie = 123;
		TipoOperacao tipoOperacao = TipoOperacao.ENTRADA;

		Identificacao identificacao = Fixture.identificacao(dataEmissao,
				dataEntradaContigencia, dataSaidaEntrada,
				descricaoNaturezaOperacao, digitoVerificadorChaveAcesso,
				formaPagamento, horaSaidaEntrada,
				justificativaEntradaContigencia, listReferenciadas,
				numeroDocumentoFiscal, serie, tipoOperacao);

		String documento = "";
		String email = "";

		Endereco enderecoDestinatario = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13852123", "Rua das paineiras",
				"4585", "Jrd Limeira", "Pedra de Guaratiba", "RJ", 3543402);

		save(enderecoDestinatario);

		String inscricaoEstadual = "";
		String inscricaoSuframa = "";
		String nome = "";
		String nomeFantasia = "";
		Pessoa pessoaDestinatarioReferencia = null;
		Telefone telefone = null;

		IdentificacaoDestinatario identificacaoDestinatario = Fixture
				.identificacaoDestinatario(documento, email,
						enderecoDestinatario, inscricaoEstadual,
						inscricaoSuframa, nome, nomeFantasia,
						pessoaDestinatarioReferencia, telefone);

		String cnae = "";
		String documentoEmitente = "";

		Endereco enderecoEmitente = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13852345", "Rua Laranjeiras", "4585",
				"Jrd Brasil", "Santana do Livramento", "RJ", 6);

		save(enderecoEmitente);

		String inscricaoEstualEmitente = "";
		String inscricaoEstualSubstituto = "";
		String inscricaoMunicipalEmitente = "";
		String nomEmitente = "";
		String nomeFantasiaEmitente = "";
		Pessoa pessoaEmitenteReferencia = null;
		RegimeTributario regimeTributario = null;
		Telefone telefoneEmitente = null;

		IdentificacaoEmitente identificacaoEmitente = Fixture
				.identificacaoEmitente(cnae, documentoEmitente,
						enderecoEmitente, inscricaoEstualEmitente,
						inscricaoEstualSubstituto, inscricaoMunicipalEmitente,
						nomEmitente, nomeFantasiaEmitente,
						pessoaEmitenteReferencia, regimeTributario,
						telefoneEmitente);

		String informacoesComplementares = "";

		InformacaoAdicional informacaoAdicional = Fixture
				.informacaoAdicional(informacoesComplementares);

		String chaveAcesso = "523524352354";

		Date dataRecebimento = Fixture.criarData(01, Calendar.JANUARY, 2012);
		String motivo = "";
		Long protocolo = 32165487L;
		Status status = Status.AUTORIZADO;

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = Fixture
				.retornoComunicacaoEletronica(dataRecebimento, motivo,
						protocolo, status);

		InformacaoEletronica informacaoEletronica = Fixture
				.informacaoEletronica(chaveAcesso, retornoComunicacaoEletronica);

		String documentoTranposrte = "564645664";

		Endereco enderecoTransporte = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13852345", "Rua Maracuja", "4585",
				"Jrd Brasil", "Piuí", "MG", 3543402);

		save(enderecoTransporte);

		Integer modalidadeFrente = 1;
		String municipio = "";
		String nomeFantasiaTransporte = "";
		RetencaoICMSTransporte retencaoICMS = null;
		String ufTransporte = "";
		Veiculo veiculo = null;

		InformacaoTransporte informacaoTransporte = Fixture
				.informacaoTransporte(documentoTranposrte, enderecoTransporte,
						inscricaoEstadual, modalidadeFrente, municipio,
						nomeFantasiaTransporte, retencaoICMS, ufTransporte,
						veiculo);

		ValoresRetencoesTributos valoresRetencoesTributos = Fixture
				.valoresRetencoesTributos(1L, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO);

		// save(valoresRetencoesTributos);

		BigDecimal valorBaseCalculoICMS = BigDecimal.ZERO;
		BigDecimal valorBaseCalculoICMSST = BigDecimal.ZERO;
		BigDecimal valorCOFINS = BigDecimal.ZERO;
		BigDecimal valorDesconto = BigDecimal.ZERO;
		BigDecimal valorFrete = BigDecimal.ZERO;
		BigDecimal valorICMS = BigDecimal.ZERO;
		BigDecimal valorICMSST = BigDecimal.ZERO;
		BigDecimal valorIPI = BigDecimal.ZERO;
		BigDecimal valorNotaFiscal = BigDecimal.ZERO;
		BigDecimal valorOutro = BigDecimal.ZERO;
		BigDecimal valorPIS = BigDecimal.ZERO;
		BigDecimal valorProdutos = BigDecimal.ZERO;
		BigDecimal valorSeguro = BigDecimal.ZERO;
		BigDecimal valorBaseCalculo = BigDecimal.ZERO;
		BigDecimal valorISS = BigDecimal.ZERO;
		BigDecimal valorServicos = BigDecimal.ZERO;

		ValoresTotaisISSQN valoresTotaisISSQN = null;

		InformacaoValoresTotais informacaoValoresTotais = Fixture
				.informacaoValoresTotais(valoresRetencoesTributos,
						valoresTotaisISSQN, valorBaseCalculoICMS,
						valorBaseCalculoICMSST, valorCOFINS, valorDesconto,
						valorFrete, valorICMS, valorICMSST, valorIPI,
						valorNotaFiscal, valorOutro, valorPIS, valorProdutos,
						valorSeguro);

		StatusProcessamentoInterno statusProcessamentoInterno = StatusProcessamentoInterno.ENVIADA;

		NotaFiscal notaFiscal = new NotaFiscal();

		notaFiscal.setIdentificacao(identificacao);
		notaFiscal.setIdentificacaoDestinatario(identificacaoDestinatario);
		notaFiscal.setIdentificacaoEmitente(identificacaoEmitente);
		notaFiscal.setInformacaoAdicional(informacaoAdicional);
		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setInformacaoTransporte(informacaoTransporte);
		notaFiscal.setInformacaoValoresTotais(informacaoValoresTotais);
		notaFiscal.setStatusProcessamentoInterno(statusProcessamentoInterno);

		save(notaFiscal);

		return notaFiscal;

	}

}
