package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.DanfeWrapper;
import br.com.abril.nds.dto.Duplicata;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.ItemImpressaoNfe;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.NfeImpressaoWrapper;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class NFeServiceImpl implements NFeService {

	@Autowired
	protected NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	protected NotaEnvioRepository notaEnvioRepository;
	
	@Autowired
	protected MonitorNFEService monitorNFEService;	

	@Autowired
	protected ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;

	@Autowired
	protected ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;

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
	public byte[] obterNEsPDF(List<NotaEnvio> listaNfeImpressaoNE, boolean isNECA) {
				
		List<NfeImpressaoWrapper> listaNEWrapper = new ArrayList<NfeImpressaoWrapper>();

		for(NotaEnvio ne :  listaNfeImpressaoNE) {

			NfeImpressaoDTO nfeImpressao = obterDadosNENECA(ne);

			if(nfeImpressao!=null) {
				listaNEWrapper.add(new NfeImpressaoWrapper(nfeImpressao));
			}

		}

		try {

			return gerarDocumentoIreportNE(listaNEWrapper, false);

		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração dos arquivos NE");
		}
	}
	
	private NfeImpressaoDTO obterDadosNENECA(NotaEnvio ne) {
		NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();

		//TODO: concluir
		NotaEnvio notaEnvio = notaEnvioRepository.buscarPorId(ne.getNumero()); 

		if(notaEnvio == null) {
			return null;
		}
		
		carregarNEDadosPrincipais(nfeImpressao, notaEnvio);
		
/*

		carregarDanfeDadosEmissor(nfeImpressao, notaEnvio);

		carregarDanfeDadosDestinatario(nfeImpressao, notaEnvio);

		carregarDanfeDadosTributarios(nfeImpressao, notaEnvio);

		carregarDanfeDadosTransportadora(nfeImpressao, notaEnvio);

		carregarDadosItensNfe(nfeImpressao, notaEnvio);

		carregarDadosDuplicatas(nfeImpressao, notaEnvio);*/

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

		if(notaFiscal.getInformacaoEletronica() == null) return;
		
		Identificacao identificacao 				= notaFiscal.getIdentificacao();
		InformacaoEletronica informacaoEletronica 	= notaFiscal.getInformacaoEletronica();
		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getInformacaoValoresTotais();
		RetornoComunicacaoEletronica retornoComunicacaoEletronica = notaFiscal.getInformacaoEletronica().getRetornoComunicacaoEletronica();
		ValoresTotaisISSQN valoresTotaisISSQN	=	notaFiscal.getInformacaoValoresTotais().getTotaisISSQN();
		InformacaoAdicional informacaoAdicional = notaFiscal.getInformacaoAdicional();

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

		IdentificacaoEmitente identificacaoEmitente = notaFiscal.getIdentificacaoEmitente();
		Pessoa pessoaEmitente = notaFiscal.getIdentificacaoEmitente().getPessoaEmitenteReferencia();

		boolean indPessoaJuridica = false;

		if(pessoaEmitente instanceof PessoaJuridica) {

			indPessoaJuridica = true;

		} 

		String documento 	= identificacaoEmitente.getDocumento();
		Endereco endereco 	= identificacaoEmitente.getEndereco();
		Telefone telefone 	= identificacaoEmitente.getTelefone();

		String emissorNome 							 = identificacaoEmitente.getNome();

		String emissorFantasia 						 = identificacaoEmitente.getNomeFantasia();
		String emissorInscricaoEstadual 			 = identificacaoEmitente.getInscricaoEstual();
		String emissorInscricaoEstadualSubstituto 	 = identificacaoEmitente.getInscricaoEstualSubstituto();
		String emissorInscricaoMunicipal 			 = identificacaoEmitente.getInscricaoMunicipal();

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

			emissorLogradouro 	= endereco.getLogradouro();
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

		IdentificacaoDestinatario identificacaoDestinatario = notaFiscal.getIdentificacaoDestinatario();

		String documento 			= identificacaoDestinatario.getDocumento();
		Pessoa pessoaDestinatario 	= identificacaoDestinatario.getPessoaDestinatarioReferencia();

		Endereco endereco = identificacaoDestinatario.getEndereco();
		Telefone telefone = identificacaoDestinatario.getTelefone();

		boolean indPessoaJuridica = false;

		if(pessoaDestinatario instanceof PessoaJuridica) {
			indPessoaJuridica = true;
		} 

		String destinatarioCNPJ 				= "";
		String destinatarioNome 				= identificacaoDestinatario.getNome();
		String destinatarioInscricaoEstadual 	= identificacaoDestinatario.getInscricaoEstual();

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
			destinatarioCEP	=	endereco.getCep();

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

		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getInformacaoValoresTotais();

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

		InformacaoTransporte informacaoTransporte = notaFiscal.getInformacaoTransporte();

		Endereco endereco = informacaoTransporte.getEndereco();

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
	private NfeImpressaoDTO obterDadosNFe(NfeVO nfe) {

		NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();

		if(nfe == null || nfe.getIdNotaFiscal() == null) {
			return null;
		}

		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal()); 

		if(notaFiscal == null) {
			return null;
		}

		carregarNfesDadosPrincipais(nfeImpressao, notaFiscal);

		carregarDanfeDadosEmissor(nfeImpressao, notaFiscal);

		carregarDanfeDadosDestinatario(nfeImpressao, notaFiscal);

		carregarDanfeDadosTributarios(nfeImpressao, notaFiscal);

		carregarDanfeDadosTransportadora(nfeImpressao, notaFiscal);

		carregarDadosItensNfe(nfeImpressao, notaFiscal);

		carregarDadosDuplicatas(nfeImpressao, notaFiscal);

		return nfeImpressao;

	}



	/* TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar após 
	 * modelagem de dados e EMS relativa a calculo de duplicatas.
	 */
	private void carregarDadosDuplicatas(NfeImpressaoDTO danfe, NotaFiscal notaFiscal) {
		List<Duplicata> faturas = new ArrayList<Duplicata>();
		danfe.setFaturas(faturas);	
	}

	private void carregarDadosItensNfe(NfeImpressaoDTO nfeImpressao, NotaFiscal notaFiscal) {

		List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();

		List<ProdutoServico> produtosSevicos =  notaFiscal.getProdutosServicos();

		String codigoProduto 		= "";
		String descricaoProduto 	= "";
		Long produtoEdicao 			= null;
		Long reparteProduto 		= null;
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

		for(ProdutoServico produtoServico : produtosSevicos) {

			String unidade = produtoServico.getUnidade();

			codigoProduto 		= produtoServico.getCodigoProduto().toString();
			descricaoProduto 	= produtoServico.getDescricaoProduto();
			produtoEdicao		= produtoServico.getProdutoEdicao().getNumeroEdicao();
			//TODO: Sérgio - Corrigir reparte
			//reparteProduto		= produtoServico.getListaMovimentoEstoqueCota().get(0).getLancamento().getReparte();

			NCMProduto 			= produtoServico.getNcm().toString();
			CFOPProduto 		= produtoServico.getCfop().toString();                            

			//TODO: Acertar a unidade do produto
			unidadeProduto 		= null;//(unidade == null || unidade.isEmpty()) ? 0L : new Long(unidade);

			quantidadeProduto 	= null; //TODO: produtoServico.getQuantidade();              
			valorUnitarioProduto = produtoServico.getValorUnitario();
			valorTotalProduto 	= produtoServico.getValorTotalBruto();   
			valorDescontoProduto = produtoServico.getValorDesconto();

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

		nfeImpressao.setItensDanfe(listaItemImpressaoNfe);

	}

	protected URL obterDiretorioReports() {

		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("/reports/");

		return urlDanfe;
	}

	private byte[] gerarDocumentoIreportDANFE(List<DanfeWrapper> list, boolean indEmissaoDepec) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

		URL diretorioReports = obterDiretorioReports();

		String path = diretorioReports.toURI().getPath() + "/danfeWrapper.jasper";

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);

		return  JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	private byte[] gerarDocumentoIreportNE(List<NfeImpressaoWrapper> list, boolean indEmissaoDepec) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

		URL diretorioReports = obterDiretorioReports();

		String path = diretorioReports.toURI().getPath() + "/neWrapper.jasper";

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);

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

		Long numeroNF 	    		= notaEnvio.getNumero();
		String chave 				= notaEnvio.getChaveAcesso();
		Date dataEmissao 			= notaEnvio.getDataEmissao();

		BigDecimal valorLiquido  	= BigDecimal.ZERO;
		
		for(ItemNotaEnvio ine : notaEnvio.getListaItemNotaEnvio()) {
			valorLiquido.add(ine.getPrecoCapa());
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

	}
	
	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param notaFiscal
	 */
	private void carregarDanfeDadosEmissor(NfeImpressaoDTO nfeImpressao, NotaEnvio notaFiscal) {

		Pessoa pessoaEmitente = notaFiscal.getEmitente().getPessoaEmitenteReferencia();

		boolean indPessoaJuridica = false;

		if(pessoaEmitente instanceof PessoaJuridica) {

			indPessoaJuridica = true;

		} 

		String documento 	= notaFiscal.getEmitente().getDocumento();
		Endereco endereco 	= notaFiscal.getEmitente().getEndereco();
		Telefone telefone 	= notaFiscal.getEmitente().getTelefone();

		String emissorNome 							 = notaFiscal.getEmitente().getNome();

		String emissorFantasia 						 = notaFiscal.getEmitente().getNome();
		String emissorInscricaoEstadual 			 = notaFiscal.getEmitente().getInscricaoEstual();

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

			emissorLogradouro 	= endereco.getLogradouro();
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
	
}
