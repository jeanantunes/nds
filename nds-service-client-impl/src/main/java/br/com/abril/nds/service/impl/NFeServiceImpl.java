package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.Duplicata;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.ItemImpressaoNfe;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.NfeImpressaoWrapper;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalTelefone;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

@Service
public class NFeServiceImpl implements NFeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NFeServiceImpl.class);
	
	@Autowired
	protected NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	protected NotaEnvioRepository notaEnvioRepository;
	
	@Autowired
	protected MonitorNFEService monitorNFEService;
	
	@Autowired
	protected ParametrosDistribuidorService parametrosDistribuidorService;

	@Autowired
	protected ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;

	@Autowired
	protected ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;

	@Transactional
	public InfoNfeDTO pesquisarNFe(FiltroMonitorNfeDTO filtro) {

		InfoNfeDTO info = new InfoNfeDTO();

		List<NfeDTO> listaNotaFisal = notaFiscalRepository.pesquisarNotaFiscal(filtro);

		Integer qtdeRegistros = notaFiscalRepository.obterQtdeRegistroNotaFiscal(filtro);

		info.setListaNfeDTO(listaNotaFisal);

		info.setQtdeRegistros(qtdeRegistros);

		return info;

	}
	    
    /**
     * Obtém os arquivos das DANFE relativas as NFes passadas como parâmetro.
     * 
     * @param listaNfeImpressao
     * @param indEmissaoDepec
     * 
     * @return byte[] - Bytes das DANFES
     */
	@Transactional
	public byte[] obterDanfesPDF(List<NotaFiscal> listaNfeImpressao, boolean indEmissaoDepec) {

		List<NfeVO> nfes = new ArrayList<NfeVO>();
		for(NotaFiscal nf : listaNfeImpressao) {
			NfeVO nfe = new NfeVO();
			nfe.setIdNotaFiscal(nf.getId());
			nfes.add(nfe);
		}
		
		return monitorNFEService.obterDanfes(nfes, indEmissaoDepec);

	}
	
	@Transactional
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA, Intervalo<Date> intervaloLancamento) {
				
		List<NfeImpressaoWrapper> listaNEWrapper = new ArrayList<NfeImpressaoWrapper>();

		for(NotaEnvio ne :  listaNfeImpressaoNE) {

			NfeImpressaoDTO nfeImpressao = obterDadosNENECA(ne);

			if(nfeImpressao!=null) {
				
				if(intervaloLancamento != null)
					nfeImpressao.setDataLancamentoDeAte(this.getStringDataDeAte(intervaloLancamento));
				
				listaNEWrapper.add(new NfeImpressaoWrapper(nfeImpressao));
			}

		}

		try {

			return gerarDocumentoIreportNE(listaNEWrapper, false);

		} catch(Exception e) {
            LOGGER.error("Falha na geração dos arquivos NE!" + e.getMessage(), e);
            throw new RuntimeException("Falha na geração dos arquivos NE!", e);
		}
	}
	
	@Transactional(readOnly=true)
	public NotaFiscal obterNotaFiscalPorId(NotaFiscal notaFiscal) {
		return notaFiscalRepository.buscarPorId(notaFiscal.getId());
	}
	
	@Transactional(readOnly=true)
	public NotaEnvio obterNotaEnvioPorId(NotaEnvio notaEnvio) {
		return notaEnvioRepository.buscarPorId(notaEnvio.getNumero());
	}
	
	@Transactional
	public NotaFiscal mergeNotaFiscal(NotaFiscal notaFiscal) {
		return notaFiscalRepository.merge(notaFiscal);
	}
	
	@Transactional
	public NotaEnvio mergeNotaEnvio(NotaEnvio notaEnvio) {
		return notaEnvioRepository.merge(notaEnvio);
	}
	
	private NfeImpressaoDTO obterDadosNENECA(NotaEnvio ne) {
		NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();

		//TODO: concluir
		NotaEnvio notaEnvio = notaEnvioRepository.buscarPorId(ne.getNumero()); 

		if(notaEnvio == null) {
			return null;
		}
		
		carregarNEDadosPrincipais(nfeImpressao, notaEnvio);
		
		carregarNEDadosEmissor(nfeImpressao, notaEnvio);
		
		carregarNEDadosDestinatario(nfeImpressao, notaEnvio);
		
		carregarNEDadosItens(nfeImpressao, notaEnvio);
		
		return nfeImpressao;
		
	}
	
	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param nfeImpressao
	 * @param nfe
	 * @param notaFiscal
	 */
	private void carregarNfesDadosPrincipais(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {

		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica() == null) return;
		
		Identificacao identificacao 				= notaFiscal.getNotaFiscalInformacoes().getIdentificacao();
		InformacaoEletronica informacaoEletronica 	= notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();
		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais();
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica();
		ValoresTotaisISSQN valoresTotaisISSQN	=	notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais().getTotaisISSQN();
		InformacaoAdicional informacaoAdicional = notaFiscal.getNotaFiscalInformacoes().getInformacaoAdicional();

		int tipoNF = identificacao.getTipoOperacao().ordinal();

		String serie 				= identificacao.getSerie().toString();
		Long numeroNF 	    		= identificacao.getNumeroDocumentoFiscal();
		String chave 				= informacaoEletronica.getChaveAcesso();
		Date dataEmissao 			= identificacao.getDataEmissao();
		Date dataSaida 				= identificacao.getDataSaidaEntrada();

		BigDecimal valorLiquido  	= informacaoValoresTotais.getValorProdutos();
		BigDecimal valorDesconto	= informacaoValoresTotais.getValorDesconto();

		String naturezaOperacao = identificacao.getDescricaoNaturezaOperacao();
		String formaPagamento 	= identificacao.getFormaPagamento().name();
		
		String horaSaida = "";
		if(identificacao.getDataSaidaEntrada() != null)
			horaSaida = DateFormat.getTimeInstance().format(identificacao.getDataSaidaEntrada());

		String ambiente 	= ""; //TODO obter campo
		String protocolo 	= retornoComunicacaoEletronica.getProtocolo().toString();
		String versao		= ""; //TODO obter campo

		BigDecimal ISSQNTotal 				= BigDecimal.ZERO;
		BigDecimal ISSQNBase 				= BigDecimal.ZERO;
		BigDecimal ISSQNValor 				= BigDecimal.ZERO;

		if(valoresTotaisISSQN!=null) {
			ISSQNTotal 				= valoresTotaisISSQN.getValorServicos();
			ISSQNBase 				= valoresTotaisISSQN.getValorBaseCalculo();
			ISSQNValor 				= valoresTotaisISSQN.getValorISS();
		}

		String informacoesComplementares = "";
		if(informacaoAdicional != null)
			informacoesComplementares = informacaoAdicional.getInformacoesComplementares();

		String numeroFatura 				=  "";//TODO obter campo
		BigDecimal valorFatura 				= BigDecimal.ZERO; //TODO obter campo

		nfeImpressao.setISSQNTotal(ISSQNTotal);
		nfeImpressao.setISSQNBase(ISSQNBase);
		nfeImpressao.setISSQNValor(ISSQNValor);
		nfeImpressao.setInformacoesComplementares(informacoesComplementares);
		nfeImpressao.setNumeroFatura(numeroFatura);
		nfeImpressao.setValorFatura(valorFatura);
		nfeImpressao.setNaturezaOperacao(naturezaOperacao);
		nfeImpressao.setFormaPagamento(formaPagamento);
		nfeImpressao.setSerie(serie);
		nfeImpressao.setNumeroNF(numeroNF);
		nfeImpressao.setDataEmissao(dataEmissao);
		nfeImpressao.setDataSaida(dataSaida);
		nfeImpressao.setHoraSaida(horaSaida);
		nfeImpressao.setTipoNF(tipoNF);
		nfeImpressao.setAmbiente(ambiente);
		nfeImpressao.setChave(chave);
		nfeImpressao.setProtocolo(protocolo);
		nfeImpressao.setVersao(versao);
		nfeImpressao.setValorLiquido(valorLiquido);
		nfeImpressao.setValorDesconto(valorDesconto);


	}

	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param notaFiscal
	 */
	private void carregarDanfeDadosEmissor(NfeImpressaoDTO danfe, NotaFiscal notaFiscal) {

		IdentificacaoEmitente identificacaoEmitente = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente();

		String documento 	= identificacaoEmitente.getDocumento().getDocumento();
		NotaFiscalEndereco endereco 	= identificacaoEmitente.getEndereco();
		NotaFiscalTelefone telefone 	= identificacaoEmitente.getTelefone();

		String emissorNome 							 = identificacaoEmitente.getNome();

		String emissorFantasia 						 = identificacaoEmitente.getNomeFantasia();
		String emissorInscricaoEstadual 			 = identificacaoEmitente.getInscricaoEstadual();
		String emissorInscricaoEstadualSubstituto 	 = identificacaoEmitente.getInscricaoEstadualSubstituto();
		String emissorInscricaoMunicipal 			 = identificacaoEmitente.getInscricaoMunicipal();

		String emissorCNPJ 							 = "";

		emissorCNPJ = documento;

		String emissorLogradouro 	=	"";
		String emissorNumero 		=   "";
		String emissorBairro 		=   "";
		String emissorMunicipio 	=   "";
		String emissorUF 			=   "";
		String emissorCEP 			=   "";

		if(endereco!=null) {

			emissorLogradouro 	= endereco.getLogradouro();
			emissorNumero 		= endereco.getNumero().toString();
			emissorBairro 		= endereco.getBairro();
			emissorMunicipio 	= endereco.getCidade();
			emissorUF 			= endereco.getUf();
			// emissorCEP 			= endereco.getCep();

		}

		String emissorTelefone 		= "";

		if(telefone != null) {
			String ddd = (telefone.getDDD() == null) ? "()" : "("+telefone.getDDD()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			emissorTelefone = ddd + phone;	
		}


		emissorCEP = tratarCep(emissorCEP);
		emissorTelefone = tratarTelefone(emissorTelefone);

		danfe.setEmissorCNPJ(emissorCNPJ);
		danfe.setEmissorNome(emissorNome);
		danfe.setEmissorFantasia(emissorFantasia);
		danfe.setEmissorInscricaoEstadual(emissorInscricaoEstadual);
		danfe.setEmissorInscricaoEstadualSubstituto(emissorInscricaoEstadualSubstituto);
		danfe.setEmissorInscricaoMunicipal(emissorInscricaoMunicipal);
		danfe.setEmissorLogradouro(emissorLogradouro);
		danfe.setEmissorNumero(emissorNumero);
		danfe.setEmissorBairro(emissorBairro);
		danfe.setEmissorMunicipio(emissorMunicipio);
		danfe.setEmissorUF(emissorUF);
		danfe.setEmissorCEP(emissorCEP);
		danfe.setEmissorTelefone(emissorTelefone);

	}



	private static String tratarTelefone(String telefone) {

		if(telefone == null) {
			return "          ";
		}

		if(telefone.length() == 8) {
			return telefone;
		}

		if(telefone.length() == 10) {
			return telefone;
		}

		int qtdDigitosFaltantes =  10 - telefone.length();

		while(--qtdDigitosFaltantes >= 0) {
			telefone = telefone + " ";
		}

		return telefone;

	}

	private static String tratarCep(String cep) {

		if(cep == null) {
			return "          ";
		}

		if(cep.length() == 8) {
			return cep;
		}

		int qtdDigitosFaltantes = 8 - cep.length();

		while(--qtdDigitosFaltantes >= 0) {
			cep = cep + " ";
		}

		return cep;

	}

	/**
	 * Carrega os dados de destinatario na DANFE.
	 * 
	 * @param danfe
	 * @param nfe
	 * @param notaFiscal
	 */
	private void carregarDanfeDadosDestinatario(NfeImpressaoDTO danfe, NotaFiscal notaFiscal) {

		IdentificacaoDestinatario identificacaoDestinatario = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario();

		String documento			= identificacaoDestinatario.getDocumento().getDocumento();
		NotaFiscalEndereco endereco = identificacaoDestinatario.getEndereco();
		Telefone telefone = identificacaoDestinatario.getTelefone();

		boolean indPessoaJuridica = false;

		String destinatarioCNPJ 				= "";
		String destinatarioNome 				= identificacaoDestinatario.getNome();
		String destinatarioInscricaoEstadual 	= identificacaoDestinatario.getInscricaoEstadual();

		String destinatarioLogradouro 			= "";
		String destinatarioNumero 				= "";
		String destinatarioComplemento 			= "";
		String destinatarioBairro 				= "";
		String destinatarioMunicipio 			= "";
		String destinatarioUF 					= "";
		String destinatarioCEP 					= "";
		String destinatarioTelefone 			= "";

		if (indPessoaJuridica) {
			destinatarioCNPJ = documento;
		} 

		if(endereco!=null) {

			destinatarioLogradouro = endereco.getLogradouro();
			destinatarioNumero	=	endereco.getNumero().toString();
			destinatarioComplemento	=	endereco.getComplemento();
			destinatarioBairro	=	endereco.getBairro();
			destinatarioMunicipio	=	 endereco.getCidade();
			destinatarioUF	=	endereco.getUf();
			// destinatarioCEP	=	endereco.getCep();

		}

		if(telefone != null) {

			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			destinatarioTelefone = ddd + phone;

		}

		destinatarioCEP = tratarCep(destinatarioCEP);

		destinatarioTelefone = tratarTelefone(destinatarioTelefone);

		danfe.setDestinatarioCNPJ(destinatarioCNPJ);
		danfe.setDestinatarioNome(destinatarioNome);
		danfe.setDestinatarioInscricaoEstadual(destinatarioInscricaoEstadual);
		danfe.setDestinatarioLogradouro(destinatarioLogradouro);
		danfe.setDestinatarioNumero(destinatarioNumero);
		danfe.setDestinatarioComplemento(destinatarioComplemento);
		danfe.setDestinatarioBairro(destinatarioBairro);
		danfe.setDestinatarioMunicipio(destinatarioMunicipio);
		danfe.setDestinatarioUF(destinatarioUF);
		danfe.setDestinatarioCEP(destinatarioCEP);
		danfe.setDestinatarioTelefone(destinatarioTelefone);

	}

	/**
	 * Carrega os dados tributarios na DANFE.
	 * 
	 * @param nfeImpressao
	 */
	private void carregarDanfeDadosTributarios(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {

		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais();

		BigDecimal valorBaseICMS 			= informacaoValoresTotais.getValorBaseCalculoICMS();
		BigDecimal valorICMS 				= informacaoValoresTotais.getValorICMS();

		BigDecimal valorBaseICMSSubstituto 	= informacaoValoresTotais.getValorBaseCalculoICMSST();
		BigDecimal valorICMSSubstituto 		= informacaoValoresTotais.getValorICMSST();

		BigDecimal valorProdutos 			= informacaoValoresTotais.getValorProdutos();

		BigDecimal valorFrete 				= informacaoValoresTotais.getValorFrete();
		BigDecimal valorSeguro 				= informacaoValoresTotais.getValorSeguro();
		BigDecimal valorOutro 				= informacaoValoresTotais.getValorOutro();
		BigDecimal valorIPI 				= informacaoValoresTotais.getValorIPI();
		BigDecimal valorNF 					= informacaoValoresTotais.getValorNotaFiscal();

		nfeImpressao.setValorBaseICMS(valorBaseICMS);
		nfeImpressao.setValorICMS(valorICMS);
		nfeImpressao.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
		nfeImpressao.setValorICMSSubstituto(valorICMSSubstituto);
		nfeImpressao.setValorProdutos(valorProdutos);
		nfeImpressao.setValorFrete(valorFrete);
		nfeImpressao.setValorSeguro(valorSeguro);
		nfeImpressao.setValorOutro(valorOutro);
		nfeImpressao.setValorIPI(valorIPI);
		nfeImpressao.setValorNF(valorNF);


	}

	/**
	 * Carrega os dados de tranportadora na DANFE.
	 * 
	 * @param nfeImpressao
	 */
	private void carregarDanfeDadosTransportadora(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {

		InformacaoTransporte informacaoTransporte = notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte();

		NotaFiscalEndereco endereco = informacaoTransporte.getEndereco();

		Veiculo veiculo = informacaoTransporte.getVeiculo();

		Integer frete = informacaoTransporte.getModalidadeFrente();

		String transportadoraDocumento = informacaoTransporte.getDocumento();
		String transportadoraNome = informacaoTransporte.getNome();

		String transportadoraInscricaoEstadual = informacaoTransporte.getInscricaoEstadual();

		String transportadoraEndereco 	= "";
		String transportadoraMunicipio 	= "";
		String transportadoraUF 		= "";

		if(endereco!=null) {

			transportadoraEndereco = endereco.getLogradouro();
			transportadoraMunicipio = endereco.getCidade();
			transportadoraUF = endereco.getUf();

		}

		String transportadoraQuantidade = ""; // TODO obter campo 
		String transportadoraEspecie 	= ""; // TODO obter campo
		String transportadoraMarca 		= ""; // TODO obter campo
		String transportadoraNumeracao 	= ""; // TODO obter campo

		BigDecimal transportadoraPesoBruto = BigDecimal.ZERO; // TODO obter campo 
		BigDecimal transportadoraPesoLiquido = BigDecimal.ZERO; // TODO obter campo 

		String transportadoraANTT = "";
		String transportadoraPlacaVeiculo = "";
		String transportadoraPlacaVeiculoUF = "";

		if(veiculo!=null) {
			transportadoraPlacaVeiculoUF = veiculo.getPlaca();
			transportadoraANTT = veiculo.getRegistroTransCarga();
			transportadoraPlacaVeiculoUF = veiculo.getUf();
		}

		nfeImpressao.setFrete(frete);
		nfeImpressao.setTransportadoraCNPJ(transportadoraDocumento);
		nfeImpressao.setTransportadoraNome(transportadoraNome);
		nfeImpressao.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
		nfeImpressao.setTransportadoraEndereco(transportadoraEndereco);
		nfeImpressao.setTransportadoraMunicipio(transportadoraMunicipio);
		nfeImpressao.setTransportadoraUF(transportadoraUF);
		nfeImpressao.setTransportadoraQuantidade(transportadoraQuantidade);
		nfeImpressao.setTransportadoraEspecie(transportadoraEspecie);
		nfeImpressao.setTransportadoraMarca(transportadoraMarca);
		nfeImpressao.setTransportadoraNumeracao(transportadoraNumeracao);
		nfeImpressao.setTransportadoraPesoBruto(transportadoraPesoBruto);
		nfeImpressao.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
		nfeImpressao.setTransportadoraANTT(transportadoraANTT);
		nfeImpressao.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
		nfeImpressao.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);

	}

	    /**
     * Carrega e retorna um objeto DANFE com os dados pertinentes a notaFiscal
     * passada como parâmetro.
     * 
     * @param nfe
     * 
     * @return DanfeDTO
     */
//	private NfeImpressaoDTO obterDadosNFe(NfeVO nfe) {
//
//		NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();
//
//		if(nfe == null || nfe.getIdNotaFiscal() == null) {
//			return null;
//		}
//
//		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal()); 
//
//		if(notaFiscal == null) {
//			return null;
//		}
//
//		carregarNfesDadosPrincipais(nfeImpressao, notaFiscal);
//
//		carregarDanfeDadosEmissor(nfeImpressao, notaFiscal);
//
//		carregarDanfeDadosDestinatario(nfeImpressao, notaFiscal);
//
//		carregarDanfeDadosTributarios(nfeImpressao, notaFiscal);
//
//		carregarDanfeDadosTransportadora(nfeImpressao, notaFiscal);
//
//		carregarDadosItensNfe(nfeImpressao, notaFiscal);
//
//		carregarDadosDuplicatas(nfeImpressao, notaFiscal);
//
//		return nfeImpressao;
//
//	}


	/* TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar após 
	 * modelagem de dados e EMS relativa a calculo de duplicatas.
	 */
	private void carregarDadosDuplicatas(NfeImpressaoDTO danfe, NotaFiscal notaFiscal) {
		List<Duplicata> faturas = new ArrayList<Duplicata>();
		danfe.setFaturas(faturas);	
	}

	private void carregarDadosItensNfe(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {

		List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();

		List<DetalheNotaFiscal> detalhesNotaFiscal = notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal();

		String codigoProduto 		= "";
		String descricaoProduto 	= "";
		Long produtoEdicao 			= null;
		String NCMProduto 			= "";
		String CFOPProduto 			= "";
		String unidadeProduto 		= "";
		BigDecimal quantidadeProduto 	= BigDecimal.ZERO;
		BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
		BigDecimal valorTotalProduto 	= BigDecimal.ZERO;
		BigDecimal valorDescontoProduto = BigDecimal.ZERO;
		String CSTProduto = "";
		String CSOSNProduto = "";
		BigDecimal baseCalculoProduto 	= BigDecimal.ZERO;
		BigDecimal aliquotaICMSProduto 	= BigDecimal.ZERO;
		BigDecimal valorICMSProduto 	= BigDecimal.ZERO;
		BigDecimal aliquotaIPIProduto 	= BigDecimal.ZERO;
		BigDecimal valorIPIProduto 		= BigDecimal.ZERO;

		for(DetalheNotaFiscal dnf : detalhesNotaFiscal) {

			String unidade = dnf.getProdutoServico().getUnidade();

			codigoProduto 		= dnf.getProdutoServico().getCodigoProduto().toString();
			descricaoProduto 	= dnf.getProdutoServico().getDescricaoProduto();
			produtoEdicao		= dnf.getProdutoServico().getProdutoEdicao().getNumeroEdicao();

			NCMProduto 			= dnf.getProdutoServico().getNcm().toString();
			CFOPProduto 		= dnf.getProdutoServico().getCfop().toString();                            

			//TODO: Acertar a unidade do produto
			unidadeProduto 		= null;//(unidade == null || unidade.isEmpty()) ? 0L : new Long(unidade);

			quantidadeProduto 	= null; //TODO: dnf.getProdutoServico().getQuantidade();              
			valorUnitarioProduto = dnf.getProdutoServico().getValorUnitario();
			valorTotalProduto 	= dnf.getProdutoServico().getValorTotalBruto();   
			valorDescontoProduto = dnf.getProdutoServico().getValorDesconto();

			CSTProduto 			= ""; //TODO obter campo                                   
			CSOSNProduto 		= ""; //TODO obter campo                                    
			baseCalculoProduto 	= BigDecimal.ZERO;		//TODO obter campo           
			aliquotaICMSProduto = BigDecimal.ZERO;  //TODO obter campo         
			valorICMSProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			aliquotaIPIProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			valorIPIProduto 	= BigDecimal.ZERO;  //TODO obter campo         


			ItemImpressaoNfe item = new ItemImpressaoNfe();

			item.setCodigoProduto(codigoProduto);
			item.setDescricaoProduto(descricaoProduto);
			item.setProdutoEdicao(produtoEdicao);
			item.setNCMProduto(NCMProduto);
			item.setCFOPProduto(CFOPProduto);
			item.setUnidadeProduto(unidadeProduto);
			item.setQuantidadeProduto(quantidadeProduto);
			item.setValorUnitarioProduto(valorUnitarioProduto);
			item.setValorTotalProduto(valorTotalProduto);
			item.setValorDescontoProduto(valorDescontoProduto);
			item.setCSTProduto(CSTProduto);
			item.setCSOSNProduto(CSOSNProduto);
			item.setBaseCalculoProduto(baseCalculoProduto);
			item.setAliquotaICMSProduto(aliquotaICMSProduto);
			item.setValorICMSProduto(valorICMSProduto);
			item.setAliquotaIPIProduto(aliquotaIPIProduto);
			item.setValorIPIProduto(valorIPIProduto);

			listaItemImpressaoNfe.add(item);

		}

		nfeImpressao.setItensImpressaoNfe(listaItemImpressaoNfe);

	}

    /*
     * TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar
     * após modelagem de dados e EMS relativa a calculo de duplicatas.
     */
//	private void carregarDadosDuplicatas(NfeImpressaoDTO danfe, NotaFiscal notaFiscal) {
//		List<Duplicata> faturas = new ArrayList<Duplicata>();
//		danfe.setFaturas(faturas);	
//	}
//
//	private void carregarDadosItensNfe(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {
//
//		List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();
//
//		List<ProdutoServico> produtosSevicos =  notaFiscal.getProdutosServicos();
//
//		String codigoProduto 		= "";
//		String descricaoProduto 	= "";
//		Long produtoEdicao 			= null;
//		String NCMProduto 			= "";
//		String CFOPProduto 			= "";
//		String unidadeProduto 		= "";
//		BigDecimal quantidadeProduto 	= BigDecimal.ZERO;
//		BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
//		BigDecimal valorTotalProduto 	= BigDecimal.ZERO;
//		BigDecimal valorDescontoProduto = BigDecimal.ZERO;
//		String CSTProduto = "";
//		String CSOSNProduto = "";
//		BigDecimal baseCalculoProduto 	= BigDecimal.ZERO;
//		BigDecimal aliquotaICMSProduto 	= BigDecimal.ZERO;
//		BigDecimal valorICMSProduto 	= BigDecimal.ZERO;
//		BigDecimal aliquotaIPIProduto 	= BigDecimal.ZERO;
//		BigDecimal valorIPIProduto 		= BigDecimal.ZERO;
//
//		for(ProdutoServico produtoServico : produtosSevicos) {
//
////			String unidade = produtoServico.getUnidade();
//
//			codigoProduto 		= produtoServico.getCodigoProduto().toString();
//			descricaoProduto 	= produtoServico.getDescricaoProduto();
//			produtoEdicao		= produtoServico.getProdutoEdicao().getNumeroEdicao();
//
//			NCMProduto 			= produtoServico.getNcm().toString();
//			CFOPProduto 		= produtoServico.getCfop().toString();                            
//
//			//TODO: Acertar a unidade do produto
//			unidadeProduto 		= null;//(unidade == null || unidade.isEmpty()) ? 0L : Long.valueOf(unidade);
//
//			quantidadeProduto 	= null; //TODO: produtoServico.getQuantidade();              
//			valorUnitarioProduto = produtoServico.getValorUnitario();
//			valorTotalProduto 	= produtoServico.getValorTotalBruto();   
//			valorDescontoProduto = produtoServico.getValorDesconto();
//
//			CSTProduto 			= ""; //TODO obter campo                                   
//			CSOSNProduto 		= ""; //TODO obter campo                                    
//			baseCalculoProduto 	= BigDecimal.ZERO;		//TODO obter campo           
//			aliquotaICMSProduto = BigDecimal.ZERO;  //TODO obter campo         
//			valorICMSProduto 	= BigDecimal.ZERO;      //TODO obter campo     
//			aliquotaIPIProduto 	= BigDecimal.ZERO;      //TODO obter campo     
//			valorIPIProduto 	= BigDecimal.ZERO;  //TODO obter campo         
//
//
//			ItemImpressaoNfe item = new ItemImpressaoNfe();
//
//			item.setCodigoProduto(codigoProduto);
//			item.setDescricaoProduto(descricaoProduto);
//			item.setProdutoEdicao(produtoEdicao);
//			item.setNCMProduto(NCMProduto);
//			item.setCFOPProduto(CFOPProduto);
//			item.setUnidadeProduto(unidadeProduto);
//			item.setQuantidadeProduto(quantidadeProduto);
//			item.setValorUnitarioProduto(valorUnitarioProduto);
//			item.setValorTotalProduto(valorTotalProduto);
//			item.setValorDescontoProduto(valorDescontoProduto);
//			item.setCSTProduto(CSTProduto);
//			item.setCSOSNProduto(CSOSNProduto);
//			item.setBaseCalculoProduto(baseCalculoProduto);
//			item.setAliquotaICMSProduto(aliquotaICMSProduto);
//			item.setValorICMSProduto(valorICMSProduto);
//			item.setAliquotaIPIProduto(aliquotaIPIProduto);
//			item.setValorIPIProduto(valorIPIProduto);
//
//			listaItemImpressaoNfe.add(item);
//
//		}
//
//		nfeImpressao.setItensImpressaoNfe(listaItemImpressaoNfe);
//
//	}

	protected URL obterDiretorioReports() {

		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("/reports/");

		return urlDanfe;
	}

	private byte[] gerarDocumentoIreportNE(List<NfeImpressaoWrapper> list, boolean indEmissaoDepec) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

		URL diretorioReports = obterDiretorioReports();
		
		TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE = distribuidorRepository.tipoImpressaoNENECADANFE();
		
		String path = diretorioReports.toURI().getPath();
		
		if (TipoImpressaoNENECADANFE.MODELO_1.equals(tipoImpressaoNENECADANFE)) {
			
			path += "/ne_modelo1_wrapper.jasper";
		
		} else if (TipoImpressaoNENECADANFE.MODELO_2.equals(tipoImpressaoNENECADANFE)) {
		
			path += "/ne_modelo2_wrapper.jasper";
		
		} else if (TipoImpressaoNENECADANFE.DANFE.equals(tipoImpressaoNENECADANFE)) {
			
			path += "/danfeWrapper.jasper";
		
		} else {
			
            throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do documento da NE");
		}

		Map<String, Object> parameters = new HashMap<String, Object>();

		InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if(inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}
		
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);
		parameters.put("LOGO_DISTRIBUIDOR", inputStream);

		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param nfeImpressao
	 * @param nfe
	 * @param notaEnvio
	 */
	private void carregarNEDadosPrincipais(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		//FIXME: Alterado o ordenador por motivos de performance
		List<ItemNotaEnvio> lista = new ArrayList<ItemNotaEnvio>(notaEnvio.getListaItemNotaEnvio());
		Collections.sort(lista, new Comparator<ItemNotaEnvio>() {

			public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {
				if(o1 != null && o2 != null && o1.getEstudoCota() != null && o2.getEstudoCota() != null
						&& o1.getEstudoCota().getEstudo() != null && o2.getEstudoCota().getEstudo() != null) {
					if(o1.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime() < o2.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime()){
						return -1;
					}
					if(o1.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime() > o2.getEstudoCota().getEstudo().getLancamento().getDataLancamentoDistribuidor().getTime()){
						return 1;
					}
				}
				if(o1 != null && o2 != null && o1.getEstudoCota() != null) {
					return -1;
				}
				return 0;
			}
			
		});
		
		Date dataLancamento = null;
		
		if(lista.get(0) != null 
				&& lista.get(0).getEstudoCota() != null 
				&& lista.get(0).getEstudoCota().getEstudo() != null
				&& lista.get(0).getEstudoCota().getEstudo().getDataLancamento() != null) {
			dataLancamento = lista.get(0).getEstudoCota().getEstudo().getDataLancamento();
		} else {
			dataLancamento = this.notaEnvioRepository.obterMenorDataLancamentoPorNotaEnvio(notaEnvio.getNumero());
		}
		
		Long numeroNF 	    		= notaEnvio.getNumero();
		String chave 				= notaEnvio.getChaveAcesso();
		Date dataEmissao 			= notaEnvio.getDataEmissao();

		BigDecimal valorLiquido  	= BigDecimal.ZERO;
		
		for(ItemNotaEnvio ine : notaEnvio.getListaItemNotaEnvio()) {
            valorLiquido = valorLiquido.add(ine.getPrecoCapa());
		}
		
		BigDecimal valorDesconto	= BigDecimal.ZERO;

		String ambiente 	= ""; //TODO obter campo
		String versao		= ""; //TODO obter campo

		nfeImpressao.setNumeroNF(numeroNF);
		nfeImpressao.setDataEmissao(dataEmissao);
		nfeImpressao.setAmbiente(ambiente);
		nfeImpressao.setChave(chave);
		nfeImpressao.setVersao(versao);
		nfeImpressao.setValorLiquido(valorLiquido);
		nfeImpressao.setValorDesconto(valorDesconto);
		nfeImpressao.setDataLancamento(dataLancamento);
	}
	
	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param notaEnvio
	 */
	private void carregarNEDadosEmissor(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		Pessoa pessoaEmitente = notaEnvio.getEmitente().getPessoaEmitenteReferencia();

		boolean indPessoaJuridica = false;

		if(pessoaEmitente instanceof PessoaJuridica) {

			indPessoaJuridica = true;

		} 

		String documento 	= notaEnvio.getEmitente().getDocumento();
		Endereco endereco 	= notaEnvio.getEmitente().getEndereco();
		Telefone telefone 	= notaEnvio.getEmitente().getTelefone();

		String emissorNome 							 = notaEnvio.getEmitente().getNome();

		String emissorFantasia 						 = notaEnvio.getEmitente().getNome();
		String emissorInscricaoEstadual 			 = notaEnvio.getEmitente().getInscricaoEstadual();

		String emissorCNPJ 							 = "";

		if(indPessoaJuridica) {
			emissorCNPJ = documento;
		} 

		String emissorLogradouro 	=	"";
		String emissorNumero 		=   "";
		String emissorBairro 		=   "";
		String emissorMunicipio 	=   "";
		String emissorUF 			=   "";
		String emissorCEP 			=   "";

		if(endereco!=null) {

			emissorLogradouro 	= endereco.getTipoLogradouro() +" "+ endereco.getLogradouro();
			emissorNumero 		= endereco.getNumero().toString();
			emissorBairro 		= endereco.getBairro();
			emissorMunicipio 	= endereco.getCidade();
			emissorUF 			= endereco.getUf();
			emissorCEP 			= endereco.getCep();

		}

		String emissorTelefone 		= "";

		if(telefone != null) {
			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			emissorTelefone = ddd + phone;	
		}


		emissorCEP = tratarCep(emissorCEP);
		emissorTelefone = tratarTelefone(emissorTelefone);

		nfeImpressao.setEmissorCNPJ(emissorCNPJ);
		nfeImpressao.setEmissorNome(emissorNome);
		nfeImpressao.setEmissorFantasia(emissorFantasia);
		nfeImpressao.setEmissorInscricaoEstadual(emissorInscricaoEstadual);
		nfeImpressao.setEmissorLogradouro(emissorLogradouro);
		nfeImpressao.setEmissorNumero(emissorNumero);
		nfeImpressao.setEmissorBairro(emissorBairro);
		nfeImpressao.setEmissorMunicipio(emissorMunicipio);
		nfeImpressao.setEmissorUF(emissorUF);
		nfeImpressao.setEmissorCEP(emissorCEP);
		nfeImpressao.setEmissorTelefone(emissorTelefone);

	}
	
	/**
	 * Carrega os dados de destinatario na DANFE.
	 * 
	 * @param nfeImpressao
	 * @param nfe
	 * @param notaEnvio
	 */
	private void carregarNEDadosDestinatario(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		String documento 			= notaEnvio.getDestinatario().getDocumento();
		Integer codigoBox			= notaEnvio.getDestinatario().getCodigoBox();
		String nomeBox				= notaEnvio.getDestinatario().getNomeBox();
		String codigoRota			= notaEnvio.getDestinatario().getCodigoRota();
		String descricaoRota 		= notaEnvio.getDestinatario().getDescricaoRota();
		
		Endereco endereco = notaEnvio.getDestinatario().getEndereco();
		Telefone telefone = notaEnvio.getDestinatario().getTelefone();

		String destinatarioCNPJ = documento;
		String destinatarioNome 				= notaEnvio.getDestinatario().getNome();
		String destinatarioInscricaoEstadual 	= notaEnvio.getDestinatario().getInscricaoEstadual();

		String destinatarioLogradouro 			= "";
		String destinatarioNumero 				= "";
		String destinatarioComplemento 			= "";
		String destinatarioBairro 				= "";
		String destinatarioMunicipio 			= "";
		String destinatarioUF 					= "";
		String destinatarioCEP 					= "";
		String destinatarioTelefone 			= "";

		if(endereco != null) {

			destinatarioLogradouro 	= endereco.getTipoLogradouro() +" "+ endereco.getLogradouro();
			destinatarioNumero		= endereco.getNumero()!=null?endereco.getNumero().toString():"";
			destinatarioComplemento	= endereco.getComplemento();
			destinatarioBairro		= endereco.getBairro();
			destinatarioMunicipio	= endereco.getCidade();
			destinatarioUF			= endereco.getUf();
			destinatarioCEP			= endereco.getCep();

		}

		if(telefone != null) {

			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			destinatarioTelefone = ddd + phone;

		}

		destinatarioCEP = tratarCep(destinatarioCEP);

		destinatarioTelefone = tratarTelefone(destinatarioTelefone);

		nfeImpressao.setDestinatarioCNPJ(destinatarioCNPJ);
		nfeImpressao.setDestinatarioNome(destinatarioNome);
		nfeImpressao.setDestinatarioInscricaoEstadual(destinatarioInscricaoEstadual);
		nfeImpressao.setDestinatarioLogradouro(destinatarioLogradouro);
		nfeImpressao.setDestinatarioNumero(destinatarioNumero);
		nfeImpressao.setDestinatarioComplemento(destinatarioComplemento);
		nfeImpressao.setDestinatarioBairro(destinatarioBairro);
		nfeImpressao.setDestinatarioMunicipio(destinatarioMunicipio);
		nfeImpressao.setDestinatarioUF(destinatarioUF);
		nfeImpressao.setDestinatarioCEP(destinatarioCEP);
		nfeImpressao.setDestinatarioTelefone(destinatarioTelefone);
		nfeImpressao.setDestinatarioCodigoBox(codigoBox);
		nfeImpressao.setDestinatarioNomeBox(nomeBox);
		nfeImpressao.setDestinatarioCodigoRota(codigoRota);
		nfeImpressao.setDestinatarioDescricaoRota(descricaoRota);
		nfeImpressao.setNumeroCota(notaEnvio.getDestinatario().getNumeroCota());
		
	}
	
	private void carregarNEDadosItens(NfeImpressaoDTO nfeImpressao, NotaEnvio notaEnvio) {

		List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();

		List<ItemNotaEnvio> itensNotaEnvio =  notaEnvio.getListaItemNotaEnvio();

		String codigoProduto 		= "";
		String descricaoProduto 	= "";
		Long produtoEdicao 			= null;
		BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
		BigDecimal valorTotalProduto 	= BigDecimal.ZERO;
		BigDecimal valorDescontoProduto = BigDecimal.ZERO;
		
		Collections.sort(itensNotaEnvio, new Comparator<ItemNotaEnvio>(){
			@Override
			public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {
			    
			    	if(o1 != null && o1.getSequenciaMatrizLancamento() != null && o2 != null && o2.getSequenciaMatrizLancamento() != null) {
			    	    return o1.getSequenciaMatrizLancamento().compareTo(o2.getSequenciaMatrizLancamento());
			    	} else if ((o1.getProdutoEdicao() != null && o1.getProdutoEdicao().getProduto() != null)
			    		&& (o2.getProdutoEdicao() != null && o2.getProdutoEdicao().getProduto() != null)) {
    						o1.getProdutoEdicao().getProduto().getNome().compareTo(o2.getProdutoEdicao().getProduto().getNome());
    				}
    							    	
			    	return 0;
			}
			
		});

		for(ItemNotaEnvio itemNotaEnvio : itensNotaEnvio) {

			codigoProduto 		= itemNotaEnvio.getCodigoProduto().toString();
			descricaoProduto 	= itemNotaEnvio.getProdutoEdicao().getProduto().getNome();
			produtoEdicao		= itemNotaEnvio.getProdutoEdicao().getNumeroEdicao();

			valorUnitarioProduto = itemNotaEnvio.getPrecoCapa();
			valorDescontoProduto = itemNotaEnvio.getDesconto().divide(new BigDecimal("100"));
			valorTotalProduto	 = itemNotaEnvio.getPrecoCapa().multiply(new BigDecimal(itemNotaEnvio.getReparte()));

			ItemImpressaoNfe item = new ItemImpressaoNfe();

			item.setCodigoProduto(codigoProduto);
			item.setDescricaoProduto(descricaoProduto);
			item.setProdutoEdicao(produtoEdicao);
			item.setQuantidadeProduto(new BigDecimal(itemNotaEnvio.getReparte().toString()));
			item.setValorUnitarioProduto(valorUnitarioProduto);
			item.setValorTotalProduto(valorTotalProduto);
			item.setValorDescontoProduto(valorTotalProduto.subtract(valorTotalProduto.multiply(valorDescontoProduto)));
			item.setSequencia(itemNotaEnvio.getSequenciaMatrizLancamento());
			item.setCodigoBarra(itemNotaEnvio.getProdutoEdicao().getCodigoDeBarras());

			listaItemImpressaoNfe.add(item);

		}

		nfeImpressao.setItensImpressaoNfe(listaItemImpressaoNfe);

	}
	
	private String getStringDataDeAte(Intervalo<Date> intervalo) {
		
		String dataRecolhimento = null;
		
		if(intervalo.getDe().equals(intervalo.getAte()))
			dataRecolhimento =  DateUtil.formatarDataPTBR(intervalo.getDe());
		else
			dataRecolhimento =  "De "  + DateUtil.formatarDataPTBR(intervalo.getDe()) + 
 " até "
                + DateUtil.formatarDataPTBR(intervalo.getAte());
		
		return dataRecolhimento;
	}
	
}
