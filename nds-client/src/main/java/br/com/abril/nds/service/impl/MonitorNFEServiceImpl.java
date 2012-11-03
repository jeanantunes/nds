package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import br.com.abril.nds.dto.DanfeDTO;
import br.com.abril.nds.dto.DanfeWrapper;
import br.com.abril.nds.dto.Duplicata;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.model.fiscal.nota.Veiculo;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MonitorNFEServiceImpl implements MonitorNFEService {
	
	@Autowired
	protected NotaFiscalRepository notaFiscalRepository;
	
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
	 * @param listaNfeImpressaoDanfe
	 * @param indEmissaoDepec
	 * 
	 * @return byte[] - Bytes das DANFES
	 */
	@Transactional
	public byte[] obterDanfes(List<NfeVO> listaNfeImpressaoDanfe, boolean indEmissaoDepec) {
		
		List<DanfeWrapper> listaDanfeWrapper = new ArrayList<DanfeWrapper>();
		
		for(NfeVO notaFiscal :  listaNfeImpressaoDanfe) {
			
			DanfeDTO danfe = obterDadosDANFE(notaFiscal);
			
			if(danfe!=null) {
				listaDanfeWrapper.add(new DanfeWrapper(danfe));
			}
			
		}
		
		try {
			
			return gerarDocumentoIreport(listaDanfeWrapper, indEmissaoDepec);
		
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração dos arquivos DANFE");
		}
		
	}
	
	@Transactional
	public void validarEmissaoDanfe(Long idNota, boolean indEmissaoDepec) {
		
		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(idNota);
		
		if(notaFiscal == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota Fiscal não encontrada!");
			
		}
		
		if(indEmissaoDepec) {
			
			if(	StatusProcessamentoInterno.GERADA.equals(notaFiscal.getStatusProcessamentoInterno()) ||
				StatusProcessamentoInterno.ENVIADA.equals(notaFiscal.getStatusProcessamentoInterno()) ) {
				
				notaFiscal.getIdentificacao().setTipoEmissao(TipoEmissao.CONTINGENCIA);
				notaFiscalRepository.alterar(notaFiscal);
				
				return;
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nota Fiscal não possui status correto para geração Depec");
			
		}
		
		
		
	}
	
	private String obterHoras(Date dataHoras) {
		DateFormat df = SimpleDateFormat.getTimeInstance();
		return dataHoras != null ? df.format(dataHoras) : null;
	}
	
	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param danfe
	 * @param nfe
	 * @param notaFiscal
	 */
	private void carregarDanfeDadosPrincipais(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
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
		String horaSaida 		= obterHoras(identificacao.getDataSaidaEntrada());
		
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
			informacoesComplementares 	= informacaoAdicional.getInformacoesComplementares();
		
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
	private void carregarDanfeDadosEmissor(DanfeDTO danfe, NotaFiscal notaFiscal) {
	
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
	private void carregarDanfeDadosDestinatario(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
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
	 * @param danfe
	 */
	private void carregarDanfeDadosTributarios(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
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
	private void carregarDanfeDadosTransportadora(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
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
	
	/**
	 * Carrega e retorna um objeto DANFE com os dados pertinentes a notaFiscal 
	 * passada como parâmetro.
	 * 
	 * @param nfe
	 * 
	 * @return DanfeDTO
	 */
	private DanfeDTO obterDadosDANFE(NfeVO nfe) {
		
		DanfeDTO danfe = new DanfeDTO();
		
		if(nfe == null || nfe.getIdNotaFiscal() == null) {
			return null;
		}
		
		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal()); 
		
		if(notaFiscal == null) {
			return null;
		}
		
		
		carregarDanfeDadosPrincipais(danfe, notaFiscal);
		
		carregarDanfeDadosEmissor(danfe, notaFiscal);

		carregarDanfeDadosDestinatario(danfe, notaFiscal);
		
		carregarDanfeDadosTributarios(danfe, notaFiscal);
		
		carregarDanfeDadosTransportadora(danfe, notaFiscal);
		
		carregarDadosItensDanfe(danfe, notaFiscal);
		
		carregarDadosDuplicatas(danfe, notaFiscal);
		
		return danfe;
		
	}
	
	
	
	/* TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar após 
	 * modelagem de dados e EMS relativa a calculo de duplicatas.
	 */
	private void carregarDadosDuplicatas(DanfeDTO danfe, NotaFiscal notaFiscal) {
		List<Duplicata> faturas = new ArrayList<Duplicata>();
		danfe.setFaturas(faturas);	
	}
	
	private void carregarDadosItensDanfe(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
		List<ItemDanfe> listaItemDanfe = new ArrayList<ItemDanfe>();
		
		List<ProdutoServico> produtosSevicos =  notaFiscal.getProdutosServicos();

		String codigoProduto 		= "";
		String descricaoProduto 	= "";
		String NCMProduto 			= "";
		String CFOPProduto 			= "";
		Long unidadeProduto 		= 0L;
		BigDecimal quantidadeProduto = BigDecimal.ZERO;
		BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
		BigDecimal valorTotalProduto = BigDecimal.ZERO;
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
			
			NCMProduto 			= produtoServico.getNcm().toString();
			CFOPProduto 		= produtoServico.getCfop().toString();                            
			
			unidadeProduto = 	null; //(unidade == null || unidade.isEmpty()) ? 0L : new Long(unidade);
			
			quantidadeProduto 	= null; //TODO: produtoServico.getQuantidade();              
			valorUnitarioProduto = produtoServico.getValorUnitario();
			valorTotalProduto = produtoServico.getValorTotalBruto();              
			
			CSTProduto 		= ""; //TODO obter campo                                   
			CSOSNProduto 	= ""; //TODO obter campo                                    
			baseCalculoProduto 	= BigDecimal.ZERO;		//TODO obter campo           
			aliquotaICMSProduto = BigDecimal.ZERO;  //TODO obter campo         
			valorICMSProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			aliquotaIPIProduto 	= BigDecimal.ZERO;      //TODO obter campo     
			valorIPIProduto 	= BigDecimal.ZERO;  //TODO obter campo         
			
			
			ItemDanfe item = new ItemDanfe();
			
			item.setCodigoProduto(codigoProduto);
			item.setDescricaoProduto(descricaoProduto);
			item.setNCMProduto(NCMProduto);
			item.setCFOPProduto(CFOPProduto);
			item.setUnidadeProduto(unidadeProduto);
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
			
			listaItemDanfe.add(item);
			
		}
		
		
		
		danfe.setItensDanfe(listaItemDanfe);
		
		
	}
	
	protected URL obterDiretorioReports() {
		
		URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("reports");
		
		return urlDanfe;
	}
	
	private byte[] gerarDocumentoIreport(List<DanfeWrapper> list, boolean indEmissaoDepec) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL diretorioReports = obterDiretorioReports();
		
		String path = diretorioReports.toURI().getPath() + "/danfeWrapper.jasper";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		parameters.put("IND_EMISSAO_DEPEC", indEmissaoDepec);
		
		return  JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
}
