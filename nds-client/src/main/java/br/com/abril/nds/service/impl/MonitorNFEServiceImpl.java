package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
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
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

//import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
//import br.com.abril.nds.model.fiscal.NotaFiscalSaida;
//import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MonitorNFEServiceImpl implements MonitorNFEService {
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ItemNotaFiscalEntradaRepository itemNotaFiscalEntradaRepository;
	
	@Autowired
	private ItemNotaFiscalSaidaRepository itemNotaFiscalSaidaRepository;
	
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
	 * 
	 * @return byte[] - Bytes das DANFES
	 */
	@Transactional
	public byte[] obterDanfes(List<NfeVO> listaNfeImpressaoDanfe) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		
		
		if(distribuidor == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados do distribuidor não parametrizados");
		}
		
		List<EnderecoDistribuidor> listaEnderecoDistribuidor = distribuidor.getEnderecos();
		
		//TODO principal?
		Endereco endereco = null;
		if(listaEnderecoDistribuidor!=null && !listaEnderecoDistribuidor.isEmpty()) {
		
			EnderecoDistribuidor enderecoDistribuidor = listaEnderecoDistribuidor.get(0);
		
			if(enderecoDistribuidor!=null) {
				endereco = enderecoDistribuidor.getEndereco();
			}
			
		}
		
		List<TelefoneDistribuidor> listaTelefoneDistribuidor = distribuidor.getTelefones();
		
		//TODO principal?
		Telefone telefone = null;
		if(listaTelefoneDistribuidor!=null && !listaTelefoneDistribuidor.isEmpty()) {
		
			TelefoneDistribuidor telefoneDistribuidor = listaTelefoneDistribuidor.get(0);
		
			if(telefoneDistribuidor!=null) {
				telefone = telefoneDistribuidor.getTelefone();
			}
			
		}
		
		PessoaJuridica pessoaJuridicaDistribuidor = distribuidor.getJuridica();
		
		List<DanfeWrapper> listaDanfeWrapper = new ArrayList<DanfeWrapper>();
		
		for(NfeVO notaFiscal :  listaNfeImpressaoDanfe) {
			
			DanfeDTO danfe = obterDadosDANFE(notaFiscal, distribuidor, pessoaJuridicaDistribuidor, endereco, telefone);
			
			if(danfe!=null) {
				listaDanfeWrapper.add(new DanfeWrapper(danfe));
			}
			
		}
		
		try {
			
			return gerarDocumentoIreport(listaDanfeWrapper);
		
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração dos arquivos DANFE");
		}
		
	}

	/**
	 * Carrega os dados principais da DANFE
	 * 
	 * @param danfe
	 * @param nfe
	 * @param notaFiscal
	 */
	private void carregarDanfeDadosPrincipais(DanfeDTO danfe, NfeVO nfe, NotaFiscal notaFiscal) {
		
		String serie 				= notaFiscal.getIdentificacao().getSerie().toString();
		Long numeroNF 	    		= notaFiscal.getIdentificacao().getNumeroDocumentoFiscal();
		String chave 				= notaFiscal.getInformacaoEletronica().getChaveAcesso();
		Date dataEmissao 			= notaFiscal.getIdentificacao().getDataEmissao();
		Date dataSaida 				= notaFiscal.getIdentificacao().getDataSaidaEntrada();
		
		BigDecimal valorLiquido  	= notaFiscal.getInformacaoValoresTotais().getValorProdutos();
		BigDecimal valorDesconto	= notaFiscal.getInformacaoValoresTotais().getValorDesconto();
		
//	    String naturezaOperacao = notaFiscal.getNaturezaOperacao(); 	
//	    String formaPagamento 	= notaFiscal.getFormaPagamento();
//		String horaSaida 		= notaFiscal.getHoraSaida();
//		
//		int tipoNF = 0;
//		
//		if(TipoOperacao.ENTRADA.equals(nfe.getTipoOperacao())) {
//			tipoNF = 0;
//		} else {
//			tipoNF = 1;
//		}
//		
//		String ambiente 	= notaFiscal.getAmbiente();
//		String protocolo 	= notaFiscal.getProtocolo();
//		String versao		= notaFiscal.getVersao();
//
//		
//		BigDecimal ISSQNTotal 				= notaFiscal.getISSQNTotal();
//		BigDecimal ISSQNBase 				= notaFiscal.getISSQNBase();
//		BigDecimal ISSQNValor 				= notaFiscal.getISSQNValor();
//		String informacoesComplementares 	= notaFiscal.getInformacoesComplementares();
//		String numeroFatura 				= notaFiscal.getNumeroFatura();
//		BigDecimal valorFatura 				= notaFiscal.getValorFatura();		
//		
//		danfe.setISSQNTotal(ISSQNTotal);
//		danfe.setISSQNBase(ISSQNBase);
//		danfe.setISSQNValor(ISSQNValor);
//		danfe.setInformacoesComplementares(informacoesComplementares);
//		danfe.setNumeroFatura(numeroFatura);
//		danfe.setValorFatura(valorFatura);
//		danfe.setNaturezaOperacao(naturezaOperacao);
//		danfe.setFormaPagamento(formaPagamento);
//		danfe.setSerie(serie);
//		danfe.setNumeroNF(numeroNF);
//		danfe.setDataEmissao(dataEmissao);
//		danfe.setDataSaida(dataSaida);
//		danfe.setHoraSaida(horaSaida);
//		danfe.setTipoNF(tipoNF);
//		danfe.setAmbiente(ambiente);
//		danfe.setChave(chave);
//		danfe.setProtocolo(protocolo);
//		danfe.setVersao(versao);
//		danfe.setValorLiquido(valorLiquido);
//		danfe.setValorDesconto(valorDesconto);
		
		
	}
	
	/**
	 * Carrega os dados do emissor na DANFE
	 * 
	 * @param danfe
	 * @param pessoaJuridicaDistribuidor
	 * @param endereco
	 */
	private void carregarDanfeDadosEmissor(DanfeDTO danfe, NotaFiscal notaFiscal, PessoaJuridica pessoaJuridicaDistribuidor, Endereco endereco, Telefone telefone) {
	
		String emissorCNPJ 							= pessoaJuridicaDistribuidor.getCnpj();
		String emissorNome 							= pessoaJuridicaDistribuidor.getRazaoSocial();
		String emissorFantasia 						= pessoaJuridicaDistribuidor.getNomeFantasia();
		String emissorInscricaoEstadual 			= pessoaJuridicaDistribuidor.getInscricaoEstadual();
//		String emissorInscricaoEstadualSubstituto 	= notaFiscal.getEmissorInscricaoEstadualSubstituto();
//		String emissorInscricaoMunicipal 			= notaFiscal.getEmissorInscricaoMunicipal();
		
		String emissorLogradouro 	= "";
		String emissorNumero 		= "";
		String emissorBairro 		= "";
		String emissorMunicipio 	= "";
		String emissorUF 			= "";
		String emissorCEP 			= "";
		String emissorTelefone 		= "";
		
		if(telefone!=null) {
			emissorTelefone = (( telefone.getDdd()==null 	? "" : telefone.getDdd() ) + 
							  ( telefone.getNumero()==null 	? "" : telefone.getNumero() ));
		}
		
		if(endereco!=null) {
	
			emissorLogradouro 	= endereco.getLogradouro();
			emissorNumero 		= String.valueOf(endereco.getNumero());
			emissorBairro 		= endereco.getBairro();
			emissorMunicipio 	= endereco.getCidade();
			emissorUF 			= endereco.getUf();
			emissorCEP 			= endereco.getCep();
			
		}
		
		emissorCEP = tratarCep(emissorCEP);
		emissorTelefone = tratarTelefone(emissorTelefone);
		
		danfe.setEmissorCNPJ(emissorCNPJ);
		danfe.setEmissorNome(emissorNome);
		danfe.setEmissorFantasia(emissorFantasia);
		danfe.setEmissorInscricaoEstadual(emissorInscricaoEstadual);
//		danfe.setEmissorInscricaoEstadualSubstituto(emissorInscricaoEstadualSubstituto);
//		danfe.setEmissorInscricaoMunicipal(emissorInscricaoMunicipal);
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
	private void carregarDanfeDadosDestinatario(DanfeDTO danfe, NfeVO nfe, NotaFiscal notaFiscal) {
		
		//TODO se as notas forem de saida não havera destinatario, porém remetente!
		String destinatarioCNPJ 				= "";
		String destinatarioNome 				= "";
		String destinatarioInscricaoEstadual 	= "";
		
		String destinatarioLogradouro 			= "";
		String destinatarioNumero 				= "";
		String destinatarioComplemento 			= "";
		String destinatarioBairro 				= "";
		String destinatarioMunicipio 			= "";
		String destinatarioUF 					= "";
		String destinatarioCEP 					= "";
		String destinatarioTelefone 			= "";
		
		
//		if(TipoOperacao.SAIDA.equals(nfe.getTipoOperacao())) {
//			
//			if(notaFiscal instanceof NotaFiscalSaidaFornecedor) {
//				Fornecedor fornecedorDestinatario = ((NotaFiscalSaidaFornecedor)notaFiscal).getFornecedor();
//				
//				if(fornecedorDestinatario!=null) {
//					PessoaJuridica pessoaDestinatario = fornecedorDestinatario.getJuridica();
//					
//					if(pessoaDestinatario!=null) {
//						
//						destinatarioCNPJ = pessoaDestinatario.getCnpj();
//						destinatarioNome = pessoaDestinatario.getRazaoSocial();
//						destinatarioInscricaoEstadual = pessoaDestinatario.getInscricaoEstadual();
//						
//						List<Endereco> listaEnderecoDestinatario = pessoaDestinatario.getEnderecos();
//						
//						if(listaEnderecoDestinatario!=null && !listaEnderecoDestinatario.isEmpty()) {
//							
//							Endereco enderecoDestinatario = listaEnderecoDestinatario.get(0);
//							
//							destinatarioLogradouro 		= enderecoDestinatario.getLogradouro();
//							destinatarioNumero 			= String.valueOf(enderecoDestinatario.getNumero());
//							destinatarioComplemento 	= enderecoDestinatario.getComplemento();
//							destinatarioBairro 			= enderecoDestinatario.getBairro();
//							destinatarioMunicipio 		= enderecoDestinatario.getCidade();
//							destinatarioUF 				= enderecoDestinatario.getUf();
//							destinatarioCEP 			= enderecoDestinatario.getCep();
//													
//							
//						}
//
//						List<Telefone> listaTelefone = pessoaDestinatario.getTelefones();
//						
//						if(listaTelefone!=null && !listaTelefone.isEmpty()) {
//							
//							Telefone telefone = listaTelefone.get(0);
//							
//							if(telefone!=null) {
//								destinatarioTelefone = (( telefone.getDdd()==null 	? "" : telefone.getDdd() ) + 
//												  ( telefone.getNumero()==null 	? "" : telefone.getNumero() ));
//							}
//							
//							
//						}
//						
//						
//						
//					}
//				}
//			}
//		} 
		
		
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
		
//		BigDecimal valorBaseICMS 			= notaFiscal.getValorBaseICMS();
//		BigDecimal valorICMS 				= notaFiscal.getValorICMS();
//		BigDecimal valorBaseICMSSubstituto 	= notaFiscal.getValorBaseICMSSubstituto();
//		BigDecimal valorICMSSubstituto 		= notaFiscal.getValorICMSSubstituto();
//		BigDecimal valorProdutos 			= notaFiscal.getValorProdutos();
//		BigDecimal valorFrete 				= notaFiscal.getValorFrete();
//		BigDecimal valorSeguro 				= notaFiscal.getValorSeguro();
//		BigDecimal valorOutro 				= notaFiscal.getValorOutro();
//		BigDecimal valorIPI 				= notaFiscal.getValorIPI();
//		BigDecimal valorNF 					= notaFiscal.getValorNF();
//		
//		danfe.setValorBaseICMS(valorBaseICMS);
//		danfe.setValorICMS(valorICMS);
//		danfe.setValorBaseICMSSubstituto(valorBaseICMSSubstituto);
//		danfe.setValorICMSSubstituto(valorICMSSubstituto);
//		danfe.setValorProdutos(valorProdutos);
//		danfe.setValorFrete(valorFrete);
//		danfe.setValorSeguro(valorSeguro);
//		danfe.setValorOutro(valorOutro);
//		danfe.setValorIPI(valorIPI);
//		danfe.setValorNF(valorNF);

		
	}
	
	/**
	 * Carrega os dados de tranportadora na DANFE.
	 * 
	 * @param danfe
	 */
	private void carregarDanfeDadosTransportadora(DanfeDTO danfe, NotaFiscal notaFiscal) {
		
//		Integer frete = notaFiscal.getFrete();
//		String transportadoraCNPJ = notaFiscal.getTransportadoraCNPJ();
//		String transportadoraNome = notaFiscal.getTransportadoraNome();
//		String transportadoraInscricaoEstadual = notaFiscal.getTransportadoraInscricaoEstadual();
//		String transportadoraEndereco = notaFiscal.getTransportadoraEndereco();
//		String transportadoraMunicipio = notaFiscal.getTransportadoraMunicipio();
//		String transportadoraUF = notaFiscal.getTransportadoraUF();
//		String transportadoraQuantidade = notaFiscal.getTransportadoraQuantidade();
//		String transportadoraEspecie = notaFiscal.getTransportadoraEspecie();
//		String transportadoraMarca = notaFiscal.getTransportadoraMarca();
//		String transportadoraNumeracao = notaFiscal.getTransportadoraNumeracao();
//		
//		BigDecimal transportadoraPesoBruto = notaFiscal.getTransportadoraPesoBruto();
//		BigDecimal transportadoraPesoLiquido = notaFiscal.getTransportadoraPesoLiquido();
//		
//		String transportadoraANTT = notaFiscal.getTransportadoraANTT();
//		String transportadoraPlacaVeiculo = notaFiscal.getTransportadoraPlacaVeiculo();
//		String transportadoraPlacaVeiculoUF = notaFiscal.getTransportadoraPlacaVeiculoUF();
//
//		danfe.setFrete(frete);
//		danfe.setTransportadoraCNPJ(transportadoraCNPJ);
//		danfe.setTransportadoraNome(transportadoraNome);
//		danfe.setTransportadoraInscricaoEstadual(transportadoraInscricaoEstadual);
//		danfe.setTransportadoraEndereco(transportadoraEndereco);
//		danfe.setTransportadoraMunicipio(transportadoraMunicipio);
//		danfe.setTransportadoraUF(transportadoraUF);
//		danfe.setTransportadoraQuantidade(transportadoraQuantidade);
//		danfe.setTransportadoraEspecie(transportadoraEspecie);
//		danfe.setTransportadoraMarca(transportadoraMarca);
//		danfe.setTransportadoraNumeracao(transportadoraNumeracao);
//		danfe.setTransportadoraPesoBruto(transportadoraPesoBruto);
//		danfe.setTransportadoraPesoLiquido(transportadoraPesoLiquido);
//		danfe.setTransportadoraANTT(transportadoraANTT);
//		danfe.setTransportadoraPlacaVeiculo(transportadoraPlacaVeiculo);
//		danfe.setTransportadoraPlacaVeiculoUF(transportadoraPlacaVeiculoUF);
		
		
	}
	
	/**
	 * Carrega e retorna um objeto DANFE com os dados pertinentes a notaFiscal 
	 * passada como parâmetro.
	 * 
	 * @param nfe
	 * @param distribuidor
	 * @param pessoaJuridicaDistribuidor
	 * @param endereco
	 * @param telefone
	 * 
	 * @return DanfeDTO
	 */
	private DanfeDTO obterDadosDANFE(NfeVO nfe, Distribuidor distribuidor, PessoaJuridica pessoaJuridicaDistribuidor, Endereco endereco, Telefone telefone) {
		
		DanfeDTO danfe = new DanfeDTO();
		
		if(nfe == null || nfe.getIdNotaFiscal() == null) {
			return null;
		}
		
		NotaFiscal notaFiscal = notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal()); 
				
				//notaFiscalRepository.buscarPorId(nfe.getIdNotaFiscal());
		
		if(notaFiscal == null) {
			return null;
		}
		
		
		carregarDanfeDadosPrincipais(danfe, nfe, notaFiscal);
		
		carregarDanfeDadosEmissor(danfe, notaFiscal, pessoaJuridicaDistribuidor, endereco, telefone);

		carregarDanfeDadosDestinatario(danfe, nfe, notaFiscal);
		
		carregarDanfeDadosTributarios(danfe, notaFiscal);
		
		carregarDanfeDadosTransportadora(danfe, notaFiscal);
		
		carregarDadosItensDanfe(danfe, nfe, notaFiscal);
		
		carregarDadosDuplicatas(danfe, nfe, notaFiscal);
		
		return danfe;
		
	}
	
	
	
	/* TODO : Sem a modelagem do conceito de duplicatas no sistema, refatorar após 
	 * modelagem de dados e EMS relativa a calculo de duplicatas.
	 */
	private void carregarDadosDuplicatas(DanfeDTO danfe, NfeVO nfe, NotaFiscal notaFiscal) {
		List<Duplicata> faturas = new ArrayList<Duplicata>();
		danfe.setFaturas(faturas);	
	}
	
	private void carregarDadosItensDanfe(DanfeDTO danfe, NfeVO nfe, NotaFiscal notaFiscal) {
		
		List<ItemDanfe> listaItemDanfe = new ArrayList<ItemDanfe>();
		
//		if(TipoOperacao.ENTRADA.equals(nfe.getTipoOperacao())) {
//			
//			listaItemDanfe = itemNotaFiscalEntradaRepository.obterListaItemNotaFiscalEntradaDadosDanfe(((NotaFiscalEntrada) notaFiscal).getId());
//			
//		} else {
//			
//			listaItemDanfe = itemNotaFiscalSaidaRepository.obterListaItemNotaFiscalSaidaDadosDanfe(((NotaFiscalSaida) notaFiscal).getId());
//			
//		}
		
		danfe.setItensDanfe(listaItemDanfe);
		
		
	}
	
	private byte[] gerarDocumentoIreport(List<DanfeWrapper> list) throws JRException, URISyntaxException {

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/danfeWrapper.jasper");
		
		String path = url.toURI().getPath();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("SUBREPORT_DIR", Thread.currentThread().getContextClassLoader().getResource("/reports/").toURI().getPath());
		
		return  JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
}
