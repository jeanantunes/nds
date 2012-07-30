package br.com.abril.nds.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoEndereco;
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
import br.com.abril.nds.repository.NotaFiscalRepository;

public class MonitorNFEServiceImplTest {
	
	
	@Mock
	private NotaFiscalRepository notaFiscalRepository;
	
	private NotaFiscal notaFiscalMockada;
	
	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
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
		
		String documentoTransporte = "646546454654";
		Endereco enderecoTransporte = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "10500250", "Rua Nova", 1000, "Bairro Novo", "Olimpia", "SP",12);
		Integer modalidadeFrente = 1;
		String municipio = "";
		String nomeFantasiaTransporte = "";
		RetencaoICMSTransporte retencaoICMS = null;
		String ufTransporte = "";
		Veiculo veiculo = null;
		
		
		InformacaoTransporte informacaoTransporte = 
				Fixture.informacaoTransporte(
						documentoTransporte,
						enderecoTransporte, 
						inscricaoEstadual, 
						modalidadeFrente, 
						municipio, 
						nomeFantasiaTransporte, 
						retencaoICMS, 
						ufTransporte, 
						veiculo);

		ValoresRetencoesTributos valoresRetencoesTributos = null;
		
		
		
		
		
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
		
		ValoresTotaisISSQN valoresTotaisISSQN = Fixture.valoresTotaisISSQN(
				1L,
				valorBaseCalculo, 
				valorCOFINS, 
				valorISS, 
				valorPIS, 
				valorServicos);
		
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
		
		notaFiscal.setProdutosServicos(criarProdutosServicos());
		
		this.notaFiscalMockada = notaFiscal;
		
	}
	
	
	private List<ProdutoServico> criarProdutosServicos() {
		
		Integer sequencia = 1;
		Integer cfop = 1;
		Long codigoBarras = 1L;
		String codigoProduto = "1";
		String descricaoProduto = "";
		EncargoFinanceiro encargoFinanceiro = null;
		Long extipi = 1L;
		Long ncm = 1L;
		NotaFiscal notaFiscal = null;
		ProdutoEdicao produtoEdicao = null;
		BigDecimal quantidade = BigDecimal.ONE;
		String unidade = "";
		BigDecimal valorDesconto 	= BigDecimal.ZERO;
		BigDecimal valorFrete 		= BigDecimal.ZERO;
		BigDecimal valorOutros 		= BigDecimal.ZERO;
		BigDecimal valorSeguro 		= BigDecimal.ZERO;
		BigDecimal valorTotalBruto 	= BigDecimal.ZERO;
		BigDecimal valorUnitario 	= BigDecimal.ZERO;
		
		List<ProdutoServico> listaProdutoServico = new ArrayList<ProdutoServico>();
		
		int contador = 0;
		
		while(contador++<10) {
			
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
			
			listaProdutoServico.add(produtoServico);
			
		}
		
		return listaProdutoServico;
		
	}
	
	@Test
	public void testarObterDanfes() throws IOException {
		
		MonitorNFEServiceImpl monitorNFEServiceImpl = mock(MonitorNFEServiceImpl.class);
		
		monitorNFEServiceImpl.notaFiscalRepository = notaFiscalRepository;
		
		when(notaFiscalRepository.buscarPorId(1L)).thenReturn(notaFiscalMockada);
		
		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		when(monitorNFEServiceImpl.obterDiretorioReports()).thenReturn(urlDanfe);
		
		List<NfeVO> listaNotas = new ArrayList<NfeVO>();
		NfeVO nf = new NfeVO();
		nf.setIdNotaFiscal(1L);
		listaNotas.add(nf);

		when(monitorNFEServiceImpl.obterDanfes(Mockito.anyList(), Mockito.anyBoolean())).thenCallRealMethod();
		
		byte[] bytesArquivoDanfe = monitorNFEServiceImpl.obterDanfes(listaNotas, true);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		File arquivoDanfe = new File(url.getPath() + "/arquivoDanfe.pdf");
		
		FileOutputStream fos = new FileOutputStream(arquivoDanfe);
		
		fos.write(bytesArquivoDanfe);
		
	}
	
}
