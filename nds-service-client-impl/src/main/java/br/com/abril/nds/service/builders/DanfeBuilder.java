package br.com.abril.nds.service.builders;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.abril.nds.dto.DanfeDTO;
import br.com.abril.nds.dto.DanfeWrapper;
import br.com.abril.nds.dto.Duplicata;
import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalTelefone;
import br.com.abril.nds.service.impl.ImpressaoNFEServiceImpl;
import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;

public class DanfeBuilder  implements Serializable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DanfeBuilder.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 777530822917552572L;

	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param danfe
	 * @param nfe
	 * @param notaFiscal
	 * @return 
	 */
	public static void carregarDanfeDadosPrincipais(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		String chave = null;
		String protocolo = null;
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica() != null){
			
			InformacaoEletronica informacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica();
			RetornoComunicacaoEletronica retornoComunicacaoEletronica = notaFiscal.getNotaFiscalInformacoes().getInformacaoEletronica().getRetornoComunicacaoEletronica();
			chave = informacaoEletronica.getChaveAcesso();
			protocolo = retornoComunicacaoEletronica.getProtocolo() == null ? "" : retornoComunicacaoEletronica.getProtocolo().toString();
		}
		
		Identificacao identificacao 				= notaFiscal.getNotaFiscalInformacoes().getIdentificacao();
		
		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais();
		
		ValoresTotaisISSQN valoresTotaisISSQN	=	notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais().getTotaisISSQN();
		
		int tipoNF = identificacao.getTipoOperacao().ordinal();
		
		String serie 				= identificacao.getSerie().toString();
		Long numeroNF 	    		= identificacao.getNumeroDocumentoFiscal();
		
		Date dataEmissao 			= identificacao.getDataEmissao();
		Date dataSaida 				= identificacao.getDataSaidaEntrada();
		
		BigDecimal valorLiquido  	= informacaoValoresTotais.getValorProdutos();
		BigDecimal valorDesconto	= informacaoValoresTotais.getValorDesconto();
		
	    String naturezaOperacao = identificacao.getDescricaoNaturezaOperacao();
	    String formaPagamento 	= identificacao.getFormaPagamento().name();
		String horaSaida 		= obterHoras(identificacao.getDataSaidaEntrada());
		
		String ambiente 	= ""; //TODO obter campo
		
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
		if(notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper() != null)
			informacoesComplementares = notaFiscal.getNotaFiscalInformacoes().getInfAdicWrapper().getInformacoesAdicionais().toString();
		
		String numeroFatura 				=  "";//TODO obter campo
		BigDecimal valorFatura 				= BigDecimal.ZERO; //TODO obter campo
		
		danfe.setISSQNTotal(ISSQNTotal);
		danfe.setISSQNBase(ISSQNBase);
		danfe.setISSQNValor(ISSQNValor);
		danfe.setInformacoesComplementares(informacoesComplementares);
		danfe.setNumeroFatura(numeroFatura);
		danfe.setValorFatura(valorFatura);
		danfe.setNaturezaOperacao(naturezaOperacao);
		danfe.setFormaPagamento(formaPagamento);
		danfe.setSerie(serie);
		danfe.setNumeroNF(numeroNF);
		danfe.setDataEmissao(dataEmissao);
		danfe.setDataSaida(dataSaida);
		danfe.setHoraSaida(horaSaida);
		danfe.setTipoNF(tipoNF);
		danfe.setAmbiente(ambiente);
		danfe.setChave(chave);
		danfe.setProtocolo(protocolo);
		danfe.setVersao(versao);
		danfe.setValorLiquido(valorLiquido);
		danfe.setValorDesconto(valorDesconto);
	}
	
	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param notaFiscal
	 */
	public static void carregarDanfeDadosEmissor(final DanfeDTO danfe, final NotaFiscal notaFiscal) {
	
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
		
		if(endereco != null) {
			
			emissorLogradouro 	= endereco.getLogradouro();
			emissorNumero 		= endereco.getNumero().toString();
			emissorBairro 		= endereco.getBairro();
			emissorMunicipio 	= endereco.getCidade();
			emissorUF 			= endereco.getUf();
			emissorCEP 			= endereco.getCep();
			
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
	
	/**
	 * Carrega os dados de destinatario na DANFE.
	 * 
	 * @param danfe
	 * @param nfe
	 * @param notaFiscal
	 */
	public static void carregarDanfeDadosDestinatario(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		IdentificacaoDestinatario identificacaoDestinatario = notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario();
		
		String documento 			= identificacaoDestinatario.getDocumento().getDocumento();
		
		NotaFiscalEndereco endereco = identificacaoDestinatario.getEndereco();
		Telefone telefone = identificacaoDestinatario.getTelefone();
		
		String destinatarioNome 				=  "";
		String destinatarioCNPJ 				= "";
		
		if (notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota() != null) {
			destinatarioNome = String.format("%s - ", notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getCota().getNumeroCota().toString());
		}
		
		destinatarioNome 						+=  identificacaoDestinatario.getNome();
		String destinatarioInscricaoEstadual 	= identificacaoDestinatario.getInscricaoEstadual();
		String destinatarioLogradouro 			= "";
		String destinatarioNumero 				= "";
		String destinatarioComplemento 			= "";
		String destinatarioBairro 				= "";
		String destinatarioMunicipio 			= "";
		String destinatarioUF 					= "";
		String destinatarioCEP 					= "";
		String destinatarioTelefone 			= "";
		
		destinatarioCNPJ = documento; 
		
		if(endereco != null) {
			
			destinatarioLogradouro = endereco.getLogradouro();
			destinatarioNumero	=	endereco.getNumero().toString();
			destinatarioComplemento	=	endereco.getComplemento();
			destinatarioBairro	=	endereco.getBairro();
			destinatarioMunicipio	=	 endereco.getCidade();
			destinatarioUF	=	endereco.getUf();
			destinatarioCEP	=	endereco.getCep();
			
			destinatarioLogradouro = destinatarioLogradouro +", "+ destinatarioNumero + (destinatarioComplemento != null ? ", "+ destinatarioComplemento : "");
			
		}
		
		if(telefone != null) {
			
			String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
			String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
			destinatarioTelefone = ddd + phone;
			
		}
		
		destinatarioCEP = tratarCep(destinatarioCEP);
		
		destinatarioTelefone = tratarTelefone(destinatarioTelefone);
		
		if(destinatarioCNPJ.length() > 11) {			
			System.out.println("Valor do CNPJ"+ destinatarioCNPJ);
			danfe.setDestinatarioCNPJ(new CNPJFormatter().format(destinatarioCNPJ));
		} else { 
			danfe.setDestinatarioCNPJ(new CPFFormatter().format(destinatarioCNPJ));
		}
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
	 * @param danfe
	 */
	public static void  carregarDanfeDadosTributarios(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		InformacaoValoresTotais informacaoValoresTotais = notaFiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais();
		
		BigDecimal valorBaseICMS 			= informacaoValoresTotais.getValorBaseCalculoICMS();
		BigDecimal valorICMS 				= informacaoValoresTotais.getValorICMS();
		
		BigDecimal valorBaseICMSSubstituto 	= informacaoValoresTotais.getValorBaseCalculoICMSST();
		BigDecimal valorICMSSubstituto 		= informacaoValoresTotais.getValorICMSST();
		
		BigDecimal valorProdutos 			= notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().getValorProdutos();
		
		BigDecimal valorFrete 				= informacaoValoresTotais.getValorFrete();
		BigDecimal valorSeguro 				= informacaoValoresTotais.getValorSeguro();
		BigDecimal valorOutro 				= informacaoValoresTotais.getValorOutro();
		BigDecimal valorIPI 				= informacaoValoresTotais.getValorIPI();
		BigDecimal valorNF 					= notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().getValorNF();
		
		danfe.setValorBaseICMS(valorBaseICMS);
		danfe.setValorICMS(valorICMS);
		danfe.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
		danfe.setValorICMSSubstituto(valorICMSSubstituto);
		danfe.setValorProdutos(valorProdutos);
		danfe.setValorFrete(valorFrete);
		danfe.setValorSeguro(valorSeguro);
		danfe.setValorOutro(valorOutro);
		danfe.setValorIPI(valorIPI);
		danfe.setValorNF(valorNF);
	}
	
	/**
	 * Carrega os dados de tranportadora na DANFE.
	 * 
	 * @param danfe
	 */
	public static void carregarDanfeDadosTransportadora(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		InformacaoTransporte informacaoTransporte = notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte();
		
		NotaFiscalEndereco endereco = informacaoTransporte.getTransportadorWrapper().getEndereco();
		
		Veiculo veiculo = informacaoTransporte.getTransportadorWrapper().getVeiculo();
		
		Integer frete = informacaoTransporte.getModalidadeFrete();
		
		String transportadoraDocumento = informacaoTransporte.getTransportadorWrapper().getDocumento();
		String transportadoraNome = informacaoTransporte.getTransportadorWrapper().getNome();
		
		String transportadoraInscricaoEstadual = informacaoTransporte.getTransportadorWrapper().getInscricaoEstadual();

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
		
		danfe.setFrete(frete);
		danfe.setTransportadoraCNPJ(transportadoraDocumento);
		danfe.setTransportadoraNome(transportadoraNome);
		danfe.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
		danfe.setTransportadoraEndereco(transportadoraEndereco);
		danfe.setTransportadoraMunicipio(transportadoraMunicipio);
		danfe.setTransportadoraUF(transportadoraUF);
		danfe.setTransportadoraQuantidade(transportadoraQuantidade);
		danfe.setTransportadoraEspecie(transportadoraEspecie);
		danfe.setTransportadoraMarca(transportadoraMarca);
		danfe.setTransportadoraNumeracao(transportadoraNumeracao);
		danfe.setTransportadoraPesoBruto(transportadoraPesoBruto);
		danfe.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
		danfe.setTransportadoraANTT(transportadoraANTT);
		danfe.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
		danfe.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);
		
	}
	
	public static void carregarDadosItensDanfe(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		List<ItemDanfe> listaItemDanfe = new ArrayList<ItemDanfe>();
		
		List<DetalheNotaFiscal> detalhesNotaFiscal =  notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal();

		String codigoProduto 		= "";
		String descricaoProduto 	= "";
		Long produtoEdicao          = Long.valueOf(0);
		String NCMProduto 			= "";
		String CFOPProduto 			= "";
		BigInteger quantidadeProduto = BigInteger.ZERO;
		BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
		BigDecimal valorTotalProduto = BigDecimal.ZERO;
		String CSTProduto = "";
		String CSOSNProduto = "";
		BigDecimal baseCalculoProduto 	= BigDecimal.ZERO;
		BigDecimal aliquotaICMSProduto 	= BigDecimal.ZERO;
		BigDecimal valorICMSProduto 	= BigDecimal.ZERO;
		BigDecimal aliquotaIPIProduto 	= BigDecimal.ZERO;
		BigDecimal valorIPIProduto 		= BigDecimal.ZERO;
		BigDecimal totalProduto         = BigDecimal.ZERO;
		
		for(DetalheNotaFiscal detalheNotaFiscal : detalhesNotaFiscal) {
			
			String unidade = detalheNotaFiscal.getProdutoServico().getUnidade();
					
			codigoProduto 		= detalheNotaFiscal.getProdutoServico().getCodigoProduto().toString();
			descricaoProduto 	= detalheNotaFiscal.getProdutoServico().getDescricaoProduto().trim();
			produtoEdicao       = detalheNotaFiscal.getProdutoServico().getProdutoEdicao().getNumeroEdicao();
			NCMProduto 			= detalheNotaFiscal.getProdutoServico().getNcm().toString();
			CFOPProduto 		= detalheNotaFiscal.getProdutoServico().getCfop().toString();                            
			
			quantidadeProduto 	= detalheNotaFiscal.getProdutoServico().getQuantidade();              
			valorUnitarioProduto = detalheNotaFiscal.getProdutoServico().getValorUnitario();
			valorTotalProduto = detalheNotaFiscal.getProdutoServico().getValorTotalBruto();              
			
			CSTProduto 		= detalheNotaFiscal.getProdutoServico().getCst();                               
			CSOSNProduto 	= ""; //TODO obter campo                                    
			baseCalculoProduto 	= BigDecimal.ZERO;		//TODO obter campo           
			aliquotaICMSProduto = BigDecimal.ZERO;  //TODO obter campo         
			valorICMSProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			aliquotaIPIProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			valorIPIProduto 	= BigDecimal.ZERO;  //TODO obter campo         
			totalProduto.add(valorTotalProduto);
			
			ItemDanfe item = new ItemDanfe();
			
			item.setCodigoProduto(codigoProduto);
			item.setDescricaoProduto(descricaoProduto);
			item.setProdutoEdicao(produtoEdicao);
			item.setNCMProduto(NCMProduto);
			item.setCFOPProduto(CFOPProduto);
			item.setUnidadeProduto(unidade);
			item.setQuantidadeProduto(quantidadeProduto);
			item.setValorUnitarioProduto(valorUnitarioProduto);
			item.setValorTotalProduto(valorTotalProduto);
			item.setCSTProduto(CSTProduto);
			item.setCSOSNProduto(CSOSNProduto);
			item.setBaseCalculoProduto(baseCalculoProduto);
			item.setAliquotaICMSProduto(aliquotaICMSProduto);
			item.setValorICMSProduto(valorICMSProduto);
			item.setAliquotaIPIProduto(aliquotaIPIProduto);
			item.setValorIPIProduto(valorIPIProduto);
			item.setInfAdProd(detalheNotaFiscal.getInfAdProd());
			if(detalheNotaFiscal.getProdutoServicoPK() != null
					&& detalheNotaFiscal.getProdutoServicoPK().getNotaFiscal() != null
					&& detalheNotaFiscal.getProdutoServicoPK().getNotaFiscal().getNotaFiscalInformacoes() != null
					&& detalheNotaFiscal.getProdutoServicoPK().getNotaFiscal().getNotaFiscalInformacoes().getInformacaoAdicional() != null) {
				
			}
			listaItemDanfe.add(item);
		}
		
		danfe.setValorProdutos(totalProduto);
		danfe.setValorNF(totalProduto);
		danfe.setItensDanfe(listaItemDanfe);
	}
	
	/* TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar após 
	 * modelagem de dados e EMS relativa a calculo de duplicatas.
	 */
	public static void carregarDadosDuplicatas(DanfeDTO danfe, NotaFiscal notaFiscal) {
		List<Duplicata> faturas = new ArrayList<Duplicata>();
		danfe.setFaturas(faturas);	
	}
	
	private static String obterHoras(Date dataHoras) {
		DateFormat df = SimpleDateFormat.getTimeInstance();
		return dataHoras != null ? df.format(dataHoras) : null;
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
	
	public static byte[] gerarDocumentoIreport(List<DanfeWrapper> list, boolean indEmissaoDepec, URL diretorioReports, InputStream logoTipoDistribuidor) {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		String path;
		try {
			path = diretorioReports.toURI().getPath() + "/danfeWrapper.jasper";
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			
			parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
			
			parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);
			
			InputStream inputStream = logoTipoDistribuidor;
			
			if(inputStream == null) {
				inputStream = new ByteArrayInputStream(new byte[0]);
			}
			
			parameters.put("LOGO_DISTRIBUIDOR", inputStream);
			
			return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
		} catch (JRException jre) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ jre.getMessage());
		} catch (URISyntaxException e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao enviar o arquivo..."+ e.getMessage());
		}
	}
}		
