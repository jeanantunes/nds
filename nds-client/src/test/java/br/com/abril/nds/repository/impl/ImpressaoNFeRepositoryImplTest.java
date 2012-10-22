package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO.ColunaOrdenacaoImpressaoNFE;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroServico;
import br.com.abril.nds.model.fiscal.nota.ISSQN;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
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
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.vo.PaginacaoVO;


public class ImpressaoNFeRepositoryImplTest  extends AbstractRepositoryImplTest {
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	
	private TipoProduto tipoRevista;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;
	
	@Before
	public void setUp() {

		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(tipoFornecedorPublicacao, fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);

		
		Date dataEmissao = Fixture.criarData(01, Calendar.JANUARY, 2012); 
		Date dataEntradaContigencia = Fixture.criarData(01, Calendar.JANUARY, 2012); 
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
		
		Identificacao identificacao = Fixture.identificacao(
				dataEmissao, 
				dataEntradaContigencia, 
				dataSaidaEntrada, 
				descricaoNaturezaOperacao, 
				digitoVerificadorChaveAcesso, 
				formaPagamento, 
				horaSaidaEntrada, 
				justificativaEntradaContigencia, 
				listReferenciadas, 
				numeroDocumentoFiscal, 
				serie, 
				tipoOperacao);
		
		String documento 	= "";
		String email 		= "";
		
		Endereco enderecoDestinatario 	= 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852123", "Rua das paineiras", "4585", "Jrd Limeira", "Pedra de Guaratiba", "RJ",1);
		
		save(enderecoDestinatario);
		
		String inscricaoEstadual 	= "";
		String inscricaoSuframa 	= "";
		String nome 		= "";
		String nomeFantasia = "";
		Pessoa pessoaDestinatarioReferencia = null;
		Telefone telefone = null;
		
		IdentificacaoDestinatario identificacaoDestinatario = 
				Fixture.identificacaoDestinatario(
						documento, 
						email, 
						enderecoDestinatario, 
						inscricaoEstadual, 
						inscricaoSuframa, 
						nome, 
						nomeFantasia, 
						pessoaDestinatarioReferencia, 
						telefone);
		
		String cnae = "";
		String documentoEmitente = "";
		
		Endereco enderecoEmitente= 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852345", "Rua Laranjeiras", "4585", "Jrd Brasil", "Santana do Livramento", "RJ",2);
		
		save(enderecoEmitente);
		
		
		String inscricaoEstualEmitente = "";
		String inscricaoEstualSubstituto = "";
		String inscricaoMunicipalEmitente = "";
		String nomEmitente = "";
		String nomeFantasiaEmitente = "";
		Pessoa pessoaEmitenteReferencia = null;
		RegimeTributario regimeTributario = null;
		Telefone telefoneEmitente = null;
		
		
		
		IdentificacaoEmitente identificacaoEmitente = 
				Fixture.identificacaoEmitente(
						cnae, 
						documentoEmitente, 
						enderecoEmitente, 
						inscricaoEstualEmitente, 
						inscricaoEstualSubstituto, 
						inscricaoMunicipalEmitente, 
						nomEmitente, 
						nomeFantasiaEmitente, 
						pessoaEmitenteReferencia, 
						regimeTributario, 
						telefoneEmitente);
		
		String informacoesComplementares = "";
		
		InformacaoAdicional informacaoAdicional = Fixture.informacaoAdicional(informacoesComplementares);
		
		String chaveAcesso = "523524352354";
		
		Date dataRecebimento 	= Fixture.criarData(01, Calendar.JANUARY, 2012); 
		String motivo 			= "";
		Long protocolo 			= 32165487L;
		Status status			= Status.AUTORIZADO;
		
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = 
				Fixture.retornoComunicacaoEletronica(dataRecebimento, motivo, protocolo, status);
		
		InformacaoEletronica informacaoEletronica = Fixture.informacaoEletronica(
				chaveAcesso, 
				retornoComunicacaoEletronica);
		
		String documentoTranposrte = "564645664";
	
		
		Endereco enderecoTransporte = 
				Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13852345", "Rua Maracuja", "4585", "Jrd Brasil", "Piuí", "MG",2);
		
		save(enderecoTransporte);
		
		Integer modalidadeFrente = 1;
		String municipio = "";
		String nomeFantasiaTransporte = "";
		RetencaoICMSTransporte retencaoICMS = null;
		String ufTransporte = "";
		Veiculo veiculo = null;
		
		
		InformacaoTransporte informacaoTransporte = 
				Fixture.informacaoTransporte(
						documentoTranposrte, 
						enderecoTransporte, 
						inscricaoEstadual, 
						modalidadeFrente, 
						municipio, 
						nomeFantasiaTransporte, 
						retencaoICMS, 
						ufTransporte, 
						veiculo);

		ValoresRetencoesTributos valoresRetencoesTributos = 
				Fixture.valoresRetencoesTributos(
						1L,
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO, 
						BigDecimal.ZERO);
		
		//save(valoresRetencoesTributos);
		
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
		
		ValoresTotaisISSQN valoresTotaisISSQN = null;// Fixture.valoresTotaisISSQN(
//				1L,
//				valorBaseCalculo, 
//				valorCOFINS, 
//				valorISS, 
//				valorPIS, 
//				valorServicos);
//		
//		save(valoresTotaisISSQN);
		
		InformacaoValoresTotais informacaoValoresTotais = 
				Fixture.informacaoValoresTotais(
						valoresRetencoesTributos, 
						valoresTotaisISSQN, 
						valorBaseCalculoICMS, 
						valorBaseCalculoICMSST, 
						valorCOFINS, 
						valorDesconto, 
						valorFrete, 
						valorICMS, 
						valorICMSST, 
						valorIPI, 
						valorNotaFiscal, 
						valorOutro, 
						valorPIS, 
						valorProdutos, 
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
		
		Set<Processo> processos = new HashSet<Processo>();
		processos.add(Processo.GERACAO_NF_E);

		notaFiscal.setProcessos(processos);
		
		save(notaFiscal);
		
		criarProdutosServicos(notaFiscal);
		
	}
	
	private ProdutoEdicao criarProdutoEdicao(
			String codigoProduto,
			String nomeProduto,
			String descProduto,
			PeriodicidadeProduto periodicidade,
			int produtoPeb,
			int produtoPacotePadrao,
			Long produtoPeso,

			String codigoProdutoEdicao,
			Long numeroEdicao,
			int pacotePadrao,
			int peb,
			Long peso,
			BigDecimal precoCusto,
			BigDecimal precoVenda,
			String codigoDeBarras,
			BigDecimal expectativaVenda,
			boolean parcial
			
			) {
		
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE = null;

		/**
		 * PRODUTO EDICAO
		 */

		Lancamento lancamentoRevistaCE = null;

		ProdutoEdicao produtoEdicaoCE = null;

		Produto produtoCE = Fixture.produto(codigoProduto, descProduto, nomeProduto, periodicidade, tipoRevista, produtoPeb, produtoPacotePadrao, produtoPeso, TributacaoFiscal. TRIBUTADO);

		produtoCE.addFornecedor(fornecedorDinap);

		save(produtoCE);

		produtoEdicaoCE = Fixture.produtoEdicao(codigoProdutoEdicao, numeroEdicao, pacotePadrao, peb,
				peso, precoCusto, precoVenda, codigoDeBarras, null, produtoCE, expectativaVenda, parcial);

		save(produtoEdicaoCE);
		
		return produtoEdicaoCE;
		
	}
	
	
	private void criarProdutosServicos(NotaFiscal notaFiscal) {
		
		Integer sequencia = 1;
		Integer cfop = 1;
		Long codigoBarras = 1L;
		//String codigoProduto = "1";
		String descricaoProduto = "";
		EncargoFinanceiro encargoFinanceiro = null;
		Long extipi = 1L;
		Long ncm = 1L;
		ProdutoEdicao produtoEdicao = null;
		BigInteger quantidade = BigInteger.ONE;
		String unidade = "";
		BigDecimal valorDesconto 	= BigDecimal.ZERO;
		BigDecimal valorFrete 		= BigDecimal.ZERO;
		BigDecimal valorOutros 		= BigDecimal.ZERO;
		BigDecimal valorSeguro 		= BigDecimal.ZERO;
		BigDecimal valorTotalBruto 	= BigDecimal.ZERO;
		BigDecimal valorUnitario 	= BigDecimal.ZERO;
	
		int contador = 0;
		
		while(contador++<10) {
			
			String codigoProduto = ""+contador;
			String nomeProduto = "produto_"+contador;
			String descProduto = "";
			PeriodicidadeProduto periodicidade = PeriodicidadeProduto.ANUAL;
			int produtoPeb = 1;
			int produtoPacotePadrao = 1;
			Long produtoPeso = new Long(100);

			String codigoProdutoEdicao = contador+"";
			Long numeroEdicao = new Long(contador);
			int pacotePadrao = 1;
			int peb = 1;
			Long peso = new Long(0);
			BigDecimal precoCusto = BigDecimal.ZERO;
			BigDecimal precoVenda = BigDecimal.ZERO;
			String codigoDeBarras = contador+"";
			BigDecimal expectativaVenda = BigDecimal.ZERO;
			boolean parcial = false;
			
			ISSQN issqn = new ISSQN();
			issqn.setCst("0");
			issqn.setCodigoMunicipio(1111111);
			issqn.setAliquota(BigDecimal.ZERO);
			issqn.setValor(BigDecimal.ZERO);
			issqn.setValorBaseCalculo(BigDecimal.ZERO);
			issqn.setItemListaServico(1111);

			encargoFinanceiro = new EncargoFinanceiroServico();
			((EncargoFinanceiroServico)encargoFinanceiro).setIssqn(issqn);
			
			produtoEdicao = criarProdutoEdicao(
					codigoProduto, 
					nomeProduto, 
					descProduto, 
					periodicidade, 
					produtoPeb, 
					produtoPacotePadrao, 
					produtoPeso, 
					codigoProdutoEdicao, 
					numeroEdicao, 
					pacotePadrao, 
					peb, 
					peso, 
					precoCusto, 
					precoVenda, 
					codigoDeBarras, 
					expectativaVenda, 
					parcial);
			
			codigoProduto = String.valueOf(contador);
			
			ProdutoServico produtoServico = Fixture.produtoServico(
					sequencia++,
					cfop, 
					codigoBarras, 
					codigoProduto, 
					descricaoProduto, 
					encargoFinanceiro, 
					extipi, 
					ncm, 
					notaFiscal, 
					produtoEdicao, 
					quantidade, 
					unidade, 
					valorDesconto, 
					valorFrete, 
					valorOutros, 
					valorSeguro, 
					valorTotalBruto, 
					valorUnitario);
			
			save(encargoFinanceiro, produtoServico);
			
		}
		
		
	}
	
	@Test
	public void buscarNFeParaImpressao() {
		
		FiltroImpressaoNFEDTO filtro = obterFiltroImpressaoNfeDTOOrdenadoPorCota();
		
		List<CotasImpressaoNfeDTO> listaNotaFiscal = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
		
		Assert.assertNotNull(listaNotaFiscal);
		
		int tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, listaNotaFiscal.size());
		
	}
	
	private FiltroImpressaoNFEDTO obterFiltroImpressaoNfeDTOOrdenadoPorCota() {
		
		FiltroImpressaoNFEDTO filtro = new FiltroImpressaoNFEDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(5);

		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(ColunaOrdenacaoImpressaoNFE.COTA);
		
		return filtro;
		
	}
	
}