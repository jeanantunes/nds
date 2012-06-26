package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO.OrdenacaoColuna;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Telefone;
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
import br.com.abril.nds.vo.PaginacaoVO;

public class NotaFiscalRepositoryImplTest  extends AbstractRepositoryImplTest {

	private static Box box1;
	
	@Before
	public void setUp() {
		
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
		Identificacao.TipoOperacao tipoOperacao = Identificacao.TipoOperacao.ENTRADA;
		
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
		Endereco endereco 	= null;
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
						endereco, 
						inscricaoEstadual, 
						inscricaoSuframa, 
						nome, 
						nomeFantasia, 
						pessoaDestinatarioReferencia, 
						telefone);
		
		String cnae = "";
		String documentoEmitente = "";
		Endereco enderecoEmitente = null;
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
		
		String chaveAcesso = "";
		
		Date dataRecebimento 	= Fixture.criarData(01, Calendar.JANUARY, 2012); 
		String motivo 			= "";
		Long protocolo 			= 32165487L;
		Status status			= Status.AUTORIZADO;
		
		
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = 
				Fixture.retornoComunicacaoEletronica(dataRecebimento, motivo, protocolo, status);
		
		InformacaoEletronica informacaoEletronica = Fixture.informacaoEletronica(
				chaveAcesso, 
				retornoComunicacaoEletronica);
		
		String cnpjTransporte = "";
		String cpfTransporte	= "";
		String enderecoCompleto = null;
		Integer modalidadeFrente = 1;
		String municipio = "";
		String nomeFantasiaTransporte = "";
		RetencaoICMSTransporte retencaoICMS = null;
		String ufTransporte = "";
		Veiculo veiculo = null;
		
		
		InformacaoTransporte informacaoTransporte = 
				Fixture.informacaoTransporte(
						cnpjTransporte, 
						cpfTransporte, 
						enderecoCompleto, 
						inscricaoEstadual, 
						modalidadeFrente, 
						municipio, 
						nomeFantasiaTransporte, 
						retencaoICMS, 
						ufTransporte, 
						veiculo);

		ValoresRetencoesTributos valoresRetencoesTributos = null;
		ValoresTotaisISSQN valoresTotaisISSQN = null;
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
		
		List<ProdutoServico> produtosServicos = new ArrayList<ProdutoServico>();
		
		notaFiscal.setProdutosServicos(produtosServicos);
		
		
	}
	
	
	private void criarProdutosServicos() {
		
		Integer cfop = 1;
		Long codigoBarras = 1L;
		String codigoProduto = "1";
		String descricaoProduto = "";
		EncargoFinanceiro encargoFinanceiro = null;
		Long extipi = 1L;
		Long ncm = 1L;
		NotaFiscal notaFiscal = null;
		ProdutoEdicao produtoEdicao = null;
		Long quantidade = 1L;
		String unidade = "";
		Double valorDesconto = 1.0D;
		Double valorFrete = 1.0D;
		Double valorOutros = 1.0D;
		Double valorSeguro = 1.0D;
		Double valorTotalBruto = 1.0D;
		Double valorUnitario = 1.0D;
		
		ProdutoServico produtoServico = Fixture.produtoServico(
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
		
	}
	
	
	
	@Test
	public void teste() {
		
		FiltroMonitorNfeDTO filtro = obterFiltroMonitorNfeDTO();
		filtro.setBox(box1.getCodigo());
		
		//List<NfeDTO> lista = viewNotaFiscalRepository.pesquisarNotaFiscal(filtro);
		
		
	}
	
	
	private FiltroMonitorNfeDTO obterFiltroMonitorNfeDTO() {
		
		FiltroMonitorNfeDTO filtro = new FiltroMonitorNfeDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO();

		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
		paginacao.setPaginaAtual(1);
		paginacao.setQtdResultadosPorPagina(500);

		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(OrdenacaoColuna.EMISSAO);
		
		return filtro;
		
	}
	
}