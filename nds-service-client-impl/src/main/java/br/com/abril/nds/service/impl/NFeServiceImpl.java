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
import br.com.abril.nds.dto.ItemImpressaoNfe;
import br.com.abril.nds.dto.NfeImpressaoDTO;
import br.com.abril.nds.dto.NfeImpressaoWrapper;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.ValoresTotaisISSQN;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.repository.ItemNotaFiscalSaidaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.builders.NecaBuilder;
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

    /**
     * Obtém os arquivos das DANFE relativas as NFes passadas como parâmetro.
     * 
     * @param listaNfeImpressao
     * @param indEmissaoDepec
     * 
     * @return byte[] - Bytes das DANFES
     */
    @Override
    @Transactional
    public byte[] obterDanfesPDF(final List<NotaFiscal> listaNfeImpressao, final boolean indEmissaoDepec) {
        
        final List<NfeVO> nfes = new ArrayList<NfeVO>();
        for(final NotaFiscal nf : listaNfeImpressao) {
            final NfeVO nfe = new NfeVO();
            nfe.setIdNotaFiscal(nf.getId());
            nfes.add(nfe);
        }
        
        return monitorNFEService.obterDanfes(nfes, indEmissaoDepec);
        
    }
    
    @Override
    @Transactional
    public byte[] obterNEsPDF(final List<NotaEnvio> listaNfeImpressaoNE, final boolean isNECA, final Intervalo<Date> intervaloLancamento) {
        
        final List<NfeImpressaoWrapper> listaNEWrapper = new ArrayList<NfeImpressaoWrapper>();
        
        for(final NotaEnvio ne :  listaNfeImpressaoNE) {
            
            final NfeImpressaoDTO nfeImpressao = obterDadosNENECA(ne);
            
            if(nfeImpressao!=null) {
                
                if(intervaloLancamento != null) {
                    nfeImpressao.setDataLancamentoDeAte(this.getStringDataDeAte(intervaloLancamento));
                }
                
                listaNEWrapper.add(new NfeImpressaoWrapper(nfeImpressao));
            }
            
        }
        
        try {
            
            return gerarDocumentoIreportNE(listaNEWrapper, false);
            
        } catch(final Exception e) {
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
		
		NecaBuilder.carregarNEDadosPrincipais(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosEmissor(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosDestinatario(nfeImpressao, notaEnvio);
		
		NecaBuilder.carregarNEDadosItens(nfeImpressao, notaEnvio);
		
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
     * Carrega e retorna um objeto DANFE com os dados pertinentes a notaFiscal
     * passada como parâmetro.
=======
        }
    }
    
    @Override
    @Transactional(readOnly=true)
    public NotaFiscal obterNotaFiscalPorId(final NotaFiscal notaFiscal) {
        return notaFiscalRepository.buscarPorId(notaFiscal.getId());
    }
    
    @Override
    @Transactional(readOnly=true)
    public NotaEnvio obterNotaEnvioPorId(final NotaEnvio notaEnvio) {
        return notaEnvioRepository.buscarPorId(notaEnvio.getNumero());
    }
    
    @Override
    @Transactional
    public NotaFiscal mergeNotaFiscal(final NotaFiscal notaFiscal) {
        return notaFiscalRepository.merge(notaFiscal);
    }
    
    @Override
    @Transactional
    public NotaEnvio mergeNotaEnvio(final NotaEnvio notaEnvio) {
        return notaEnvioRepository.merge(notaEnvio);
    }
    
    private NfeImpressaoDTO obterDadosNENECA(final NotaEnvio ne) {
        final NfeImpressaoDTO nfeImpressao = new NfeImpressaoDTO();
        
        //TODO: concluir
        final NotaEnvio notaEnvio = notaEnvioRepository.buscarPorId(ne.getNumero());
        
        if(notaEnvio == null) {
            return null;
        }
        
        carregarNEDadosPrincipais(nfeImpressao, notaEnvio);
        
        carregarNEDadosEmissor(nfeImpressao, notaEnvio);
        
        carregarNEDadosDestinatario(nfeImpressao, notaEnvio);
        
        carregarNEDadosItens(nfeImpressao, notaEnvio);
        
        return nfeImpressao;
        
    }
    
    
    
    private static String tratarTelefone(final String telefone) {
        return StringUtils.rightPad(telefone, 10);
    }
    
    private static String tratarCep(final String cep) {
        return StringUtils.rightPad(cep, 8);
    }
    
    protected URL obterDiretorioReports() {
        
        final URL urlDanfe = Thread.currentThread().getContextClassLoader().getResource("/reports/");
        
        return urlDanfe;
    }
    
    private byte[] gerarDocumentoIreportNE(final List<NfeImpressaoWrapper> list, final boolean indEmissaoDepec) throws JRException, URISyntaxException {
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        
        final URL diretorioReports = obterDiretorioReports();
        
        final TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE = distribuidorRepository.tipoImpressaoNENECADANFE();
        
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
        
        final Map<String, Object> parameters = new HashMap<String, Object>();
        
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
    private void carregarNEDadosPrincipais(final NfeImpressaoDTO nfeImpressao, final NotaEnvio notaEnvio) {
        
        //FIXME: Alterado o ordenador por motivos de performance
        final List<ItemNotaEnvio> lista = new ArrayList<ItemNotaEnvio>(notaEnvio.getListaItemNotaEnvio());
        Collections.sort(lista, new Comparator<ItemNotaEnvio>() {
            
            @Override
            public int compare(final ItemNotaEnvio o1, final ItemNotaEnvio o2) {
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
            dataLancamento = notaEnvioRepository.obterMenorDataLancamentoPorNotaEnvio(notaEnvio.getNumero());
        }
        
        final Long numeroNF 	    		= notaEnvio.getNumero();
        final String chave 				= notaEnvio.getChaveAcesso();
        final Date dataEmissao 			= notaEnvio.getDataEmissao();
        
        BigDecimal valorLiquido  	= BigDecimal.ZERO;
        
        for(final ItemNotaEnvio ine : notaEnvio.getListaItemNotaEnvio()) {
            valorLiquido = valorLiquido.add(ine.getPrecoCapa());
        }
        
        final BigDecimal valorDesconto	= BigDecimal.ZERO;
        
        final String ambiente 	= ""; //TODO obter campo
        final String versao		= ""; //TODO obter campo
        
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
		
		switch (tipoImpressaoNENECADANFE) {
			case MODELO_1:
				
				path += "/ne_modelo1_wrapper.jasper";
				break;
	
			case MODELO_2:
				path += "/ne_modelo2_wrapper.jasper";
				break;	
				
			case DANFE:
				path += "/danfeWrapper.jasper";
				break;	
			default:
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
	
	private String getStringDataDeAte(Intervalo<Date> intervalo) {
		
		String dataRecolhimento = null;
		if(intervalo.getDe().equals(intervalo.getAte())){
			dataRecolhimento =  DateUtil.formatarDataPTBR(intervalo.getDe());
		}else{
			dataRecolhimento = "De "+ DateUtil.formatarDataPTBR(intervalo.getDe()) + " até " + DateUtil.formatarDataPTBR(intervalo.getAte());
		}
		
		return dataRecolhimento;
	}
	
}