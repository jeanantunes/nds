package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
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
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
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
    
    @Override
    @Transactional
    public InfoNfeDTO pesquisarNFe(final FiltroMonitorNfeDTO filtro) {
        
        final InfoNfeDTO info = new InfoNfeDTO();
        
        final List<NfeDTO> listaNotaFisal = notaFiscalRepository.pesquisarNotaFiscal(filtro);
        
        final Integer qtdeRegistros = notaFiscalRepository.obterQtdeRegistroNotaFiscal(filtro);
        
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
            dataLancamento = this.notaEnvioRepository.obterMenorDataLancamentoPorNotaEnvio(notaEnvio.getNumero());
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
    private void carregarNEDadosEmissor(final NfeImpressaoDTO nfeImpressao, final NotaEnvio notaEnvio) {
        
        final Pessoa pessoaEmitente = notaEnvio.getEmitente().getPessoaEmitenteReferencia();
        
        boolean indPessoaJuridica = false;
        
        if(pessoaEmitente instanceof PessoaJuridica) {
            
            indPessoaJuridica = true;
            
        }
        
        final String documento 	= notaEnvio.getEmitente().getDocumento();
        final Endereco endereco 	= notaEnvio.getEmitente().getEndereco();
        final Telefone telefone 	= notaEnvio.getEmitente().getTelefone();
        
        final String emissorNome 							 = notaEnvio.getEmitente().getNome();
        
        final String emissorFantasia 						 = notaEnvio.getEmitente().getNome();
        final String emissorInscricaoEstadual 			 = notaEnvio.getEmitente().getInscricaoEstadual();
        
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
            final String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
            final String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
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
    private void carregarNEDadosDestinatario(final NfeImpressaoDTO nfeImpressao, final NotaEnvio notaEnvio) {
        
        final String documento 			= notaEnvio.getDestinatario().getDocumento();
        final Integer codigoBox			= notaEnvio.getDestinatario().getCodigoBox();
        final String nomeBox				= notaEnvio.getDestinatario().getNomeBox();
        final String codigoRota			= notaEnvio.getDestinatario().getCodigoRota();
        final String descricaoRota 		= notaEnvio.getDestinatario().getDescricaoRota();
        
        final Endereco endereco = notaEnvio.getDestinatario().getEndereco();
        final Telefone telefone = notaEnvio.getDestinatario().getTelefone();
        
        final String destinatarioCNPJ = documento;
        final String destinatarioNome 				= notaEnvio.getDestinatario().getNome();
        final String destinatarioInscricaoEstadual 	= notaEnvio.getDestinatario().getInscricaoEstadual();
        
        String destinatarioLogradouro 			= "";
        String destinatarioNumero 				= "";
        String destinatarioComplemento 			= "";
        String destinatarioBairro 				= "";
        String destinatarioMunicipio 			= "";
        String destinatarioUF 					= "";
        String destinatarioCEP 					= "";
        String destinatarioTelefone 			= "";
        
        if(endereco != null) {
            
            destinatarioLogradouro 	= (endereco.getTipoLogradouro() == null ? "" : endereco.getTipoLogradouro()) 
            		+ " " + (endereco.getLogradouro() == null ? "" : endereco.getLogradouro());
            
            destinatarioNumero		= endereco.getNumero()!=null?endereco.getNumero().toString():"";
            destinatarioComplemento	= endereco.getComplemento();
            destinatarioBairro		= endereco.getBairro();
            destinatarioMunicipio	= endereco.getCidade();
            destinatarioUF			= endereco.getUf();
            destinatarioCEP			= endereco.getCep();
            
        }
        
        if(telefone != null) {
            
            final String ddd = (telefone.getDdd() == null) ? "()" : "("+telefone.getDdd()+")" ;
            final String phone = (telefone.getNumero() == null) ? "" : telefone.getNumero().toString();
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
    
    private void carregarNEDadosItens(final NfeImpressaoDTO nfeImpressao, final NotaEnvio notaEnvio) {
        
        final List<ItemImpressaoNfe> listaItemImpressaoNfe = new ArrayList<ItemImpressaoNfe>();
        
        final List<ItemNotaEnvio> itensNotaEnvio =  notaEnvio.getListaItemNotaEnvio();
        
        String codigoProduto 		= "";
        String descricaoProduto 	= "";
        Long produtoEdicao 			= null;
        BigDecimal valorUnitarioProduto = BigDecimal.ZERO;
        BigDecimal valorTotalProduto 	= BigDecimal.ZERO;
        BigDecimal valorDescontoProduto = BigDecimal.ZERO;
        
        Collections.sort(itensNotaEnvio, new Comparator<ItemNotaEnvio>(){
            @Override
            public int compare(final ItemNotaEnvio o1, final ItemNotaEnvio o2) {
                if (o1 != null && o2 != null) {
                    if(o1 != null && o1.getSequenciaMatrizLancamento() != null && o2 != null && o2.getSequenciaMatrizLancamento() != null) {
                        return o1.getSequenciaMatrizLancamento().compareTo(o2.getSequenciaMatrizLancamento());
                    } else if ((o1.getProdutoEdicao() != null && o1.getProdutoEdicao().getProduto() != null)
                            && (o2.getProdutoEdicao() != null && o2.getProdutoEdicao().getProduto() != null)) {
                        o1.getProdutoEdicao().getProduto().getNome().compareTo(o2.getProdutoEdicao().getProduto().getNome());
                    }
                }
                return 0;
            }
            
        });
        
        boolean temLancamentoComFuroDeProduto = false;
        
        for(final ItemNotaEnvio itemNotaEnvio : itensNotaEnvio) {
            
            codigoProduto 		= itemNotaEnvio.getCodigoProduto().toString();
            descricaoProduto 	= (itemNotaEnvio.getFuroProduto()==null) ? itemNotaEnvio.getPublicacao() : itemNotaEnvio.getPublicacao()+" (1) ";
            produtoEdicao		= itemNotaEnvio.getProdutoEdicao().getNumeroEdicao();
            
            valorUnitarioProduto = itemNotaEnvio.getPrecoCapa();
            valorDescontoProduto = itemNotaEnvio.getDesconto().divide(new BigDecimal("100"));
            valorTotalProduto	 = itemNotaEnvio.getPrecoCapa().multiply(new BigDecimal(itemNotaEnvio.getReparte()));
            
            final ItemImpressaoNfe item = new ItemImpressaoNfe();
            
            item.setCodigoProduto(codigoProduto);
            item.setDescricaoProduto(descricaoProduto);
            item.setProdutoEdicao(produtoEdicao);
            item.setQuantidadeProduto(new BigDecimal(itemNotaEnvio.getReparte().toString()));
            item.setValorUnitarioProduto(valorUnitarioProduto);
            item.setValorTotalProduto(valorTotalProduto);
            item.setValorDescontoProduto(valorTotalProduto.subtract(valorTotalProduto.multiply(valorDescontoProduto)));
            item.setPercentualDesconto(itemNotaEnvio.getDesconto().setScale(2));
            item.setSequencia(itemNotaEnvio.getSequenciaMatrizLancamento());
            item.setCodigoBarra(itemNotaEnvio.getProdutoEdicao().getCodigoDeBarras());
            
            listaItemImpressaoNfe.add(item);
            
            if(itemNotaEnvio.getFuroProduto()!= null){
            	temLancamentoComFuroDeProduto = true;
            }
        }
        
        nfeImpressao.setItensComFuroLancamento(temLancamentoComFuroDeProduto);
        
        nfeImpressao.setItensImpressaoNfe(listaItemImpressaoNfe);
        
    }
    
    private String getStringDataDeAte(final Intervalo<Date> intervalo) {
        
        String dataRecolhimento = null;
        
        if(intervalo.getDe().equals(intervalo.getAte())) {
            dataRecolhimento =  DateUtil.formatarDataPTBR(intervalo.getDe());
        } else {
            dataRecolhimento =  "De "  + DateUtil.formatarDataPTBR(intervalo.getDe()) +
 " até "
                    + DateUtil.formatarDataPTBR(intervalo.getAte());
        }
        
        return dataRecolhimento;
    }
    
}
