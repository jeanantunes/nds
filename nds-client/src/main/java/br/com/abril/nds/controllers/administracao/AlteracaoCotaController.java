package br.com.abril.nds.controllers.administracao;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.controllers.cadastro.validator.DistribuicaoEntregaValidator;
import br.com.abril.nds.dto.ArquivoDTO;
import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroModalDistribuicao;
import br.com.abril.nds.dto.filtro.FiltroModalFornecedor;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaDistribuicaoCota;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Resource
@Path("/administracao/alteracaoCota")
@Rules(Permissao.ROLE_CADASTRO_ALTERACAO_COTA)
public class AlteracaoCotaController extends BaseController {
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private EnderecoService enderecoService;
    
    @Autowired
    private AlteracaoCotaService alteracaoCotaService;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private ParametroCobrancaCotaService parametroCobrancaCotaService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    @Autowired
    private HttpServletResponse httpResponse;
    
    private static final FileType[] EXTENSOES_ACEITAS = { FileType.DOC, FileType.DOCX, FileType.BMP, FileType.GIF,
        FileType.PDF, FileType.JPEG, FileType.JPG, FileType.PNG };
    
    private final Result result;
    
    private static final String NOME_DEFAULT_TERMO_ADESAO = "termo_adesao.pdf";
    
    private static final String NOME_DEFAULT_PROCURACAO = "procuracao.pdf";
    
    public AlteracaoCotaController(final Result result) {
        super();
        this.result = result;
    }
    
    @Path("/")
    public void index() {
        result.include("listFornecedores", fornecedorService.obterFornecedoresAtivos());
        result.include("listBairros", enderecoService.obterBairrosCotas());
        result.include("listMunicipios", enderecoService.obterMunicipiosCotas());
        
        final List<Integer> listaVencimento = new ArrayList<Integer>();
        for (int i = 1; i < 31; i++) {
            listaVencimento.add(i);
        }
        
        result.include("listaVencimento", listaVencimento);
        result.include("listTipoEntrega", DescricaoTipoEntrega.values());
        result.include("listTipoDesconto", TipoDesconto.values());
        result.include("listBaseCalculo", BaseCalculo.values());
        result.include("listValoresMinimos", parametroCobrancaCotaService.comboValoresMinimos());
        
    }
    
    @Post
    @Path("/buscarBairroPorCidade.json")
    public void buscarBairroPorCidade(final String cidade) {
        final List<String> bairros = enderecoService.obterBairrosPorCidade(cidade);
        result.use(CustomJson.class).from(bairros).serialize();
    }
    
    @Post("/pesquisarAlteracaoCota.json")
    public void pesquisarAlteracaoCota(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, final String sortname,
            final String sortorder, final int rp, final int page) {
        
        final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortname);
        if ("DESC".equalsIgnoreCase(sortorder)) {
            paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
        } else {
            paginacao.setOrdenacao(PaginacaoVO.Ordenacao.DESC);
        }
        filtroAlteracaoCotaDTO.setPaginacao(paginacao);
        
        filtroAlteracaoCotaDTO.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtroAlteracaoCotaDTO.getNomeCota()));
        
        final List<ConsultaAlteracaoCotaDTO> listaCotas = alteracaoCotaService.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
        
        if (listaCotas == null || listaCotas.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        final int qtdCotas = alteracaoCotaService.contarAlteracaoCota(filtroAlteracaoCotaDTO);
        
        result.use(FlexiGridJson.class).from(listaCotas).total(qtdCotas).page(page).serialize();
        
    }
    
    @Post
    @Rules(Permissao.ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO)
    public void carregarCamposAlteracao(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, final String sortname, final int page, final int rp) {
        
        final List<Fornecedor> listaFornecedoresAtivos = fornecedorService.obterFornecedores();
        final List<ItemDTO<Long, String>> fornecedoresAtivos = getFornecedores(listaFornecedoresAtivos);
        
        // Carregará os dados apenas se o usuário selecionar uma linha do grid
        // p/ alteração.
        if (filtroAlteracaoCotaDTO != null) {
            if (filtroAlteracaoCotaDTO.getListaLinhaSelecao() != null
                    && filtroAlteracaoCotaDTO.getListaLinhaSelecao().size() == 1) {
                
                List<ItemDTO<Long, String>> listFornecedoresCota = new ArrayList<ItemDTO<Long, String>>();
                
                final Long cotaId = filtroAlteracaoCotaDTO.getListaLinhaSelecao().get(0);
                
                final Cota cota = cotaService.obterPorId(cotaId);
                
                if (cotaId != null) {
                    listFornecedoresCota = getFornecedores(fornecedorService.obterFornecedoresCota(cotaId));
                    removerFornecedorAssociadoLista(listFornecedoresCota, fornecedoresAtivos);
                }
                
                final FiltroModalFornecedor filtroModalFornecedor = filtroAlteracaoCotaDTO.getFiltroModalFornecedor();
                if (filtroModalFornecedor != null) {
                    filtroModalFornecedor.setListFornecedores(fornecedoresAtivos);
                    filtroModalFornecedor.setListaFornecedorAssociado(listFornecedoresCota);
                }
                if (filtroAlteracaoCotaDTO.getFiltroModalFinanceiro() != null) {
                    filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIsSugereSuspensao(cota.isSugereSuspensao());
                }
                if (cota.getParametroCobranca() != null) {
                    preencherFiltroFinanceiro(filtroAlteracaoCotaDTO, cota);
                }
                if (cota.getParametroDistribuicao() != null) {
                    preencherFiltroDistribuicao(filtroAlteracaoCotaDTO, cota);
                }
                
            } else {
            	
                filtroAlteracaoCotaDTO.getFiltroModalFornecedor().setListFornecedores(fornecedoresAtivos);
            }
            
            List<ItemDTO<BaseCalculo, String>> basesCalculo =  new ArrayList<ItemDTO<BaseCalculo, String>>();
    		for (BaseCalculo itemBaseCalculo: BaseCalculo.values()){
    			basesCalculo.add(new ItemDTO<BaseCalculo, String>(itemBaseCalculo, itemBaseCalculo.getValue()));
    		}
    		filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setBasesCalculo(basesCalculo);
        }
       
        result.use(Results.json()).from(filtroAlteracaoCotaDTO, "filtroAlteracaoCotaDTO").recursive().serialize();
    }
    
    private List<ItemDTO<Long, String>> getFornecedores(final List<Fornecedor> fornecedores) {
        
        final List<ItemDTO<Long, String>> itensFornecedor = new ArrayList<ItemDTO<Long, String>>();
        
        for (final Fornecedor fornecedor : fornecedores) {
            
            itensFornecedor
            .add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
        }
        
        return itensFornecedor;
    }
    
    @Post
    public void salvarAlteracao(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
        
    	this.validarDadosDistribuicaoEntrega(filtroAlteracaoCotaDTO);
    	
        alteracaoCotaService.salvarAlteracoes(filtroAlteracaoCotaDTO);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota alterada com sucesso."),
                Constantes.PARAM_MSGS).recursive().serialize();
    }
    
    private void validarDadosDistribuicaoEntrega(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
		
    	FiltroModalDistribuicao distribuicao = filtroAlteracaoCotaDTO.getFiltroModalDistribuicao();
    	
    	if(distribuicao!= null){
    		
        	if(distribuicao.getDescricaoTipoEntrega()!= null 
        			&& !DescricaoTipoEntrega.COTA_RETIRA.equals(distribuicao.getDescricaoTipoEntrega())){
        		
        		DistribuicaoEntregaValidator.validar(distribuicao);
        	}
    	}    	
	}
    
	public void preencherFiltroFinanceiro(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, final Cota cota) {
        
        // FINANCEIRO
        if (cota.getParametroCobranca().getFatorVencimento() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIdVencimento(
                    cota.getParametroCobranca().getFatorVencimento());
        }
        
        if (cota.getValorMinimoCobranca() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrMinimo(
                    String.valueOf(cota.getValorMinimoCobranca()));
        }
        if (cota.getPoliticaSuspensao() != null) {
            if (cota.getPoliticaSuspensao().getNumeroAcumuloDivida() != null) {
                filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setQtdDividaEmAberto(
                        cota.getPoliticaSuspensao().getNumeroAcumuloDivida());
            }
            
            if (cota.getPoliticaSuspensao().getValor() != null) {
                filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrDividaEmAberto(
                        String.valueOf(cota.getPoliticaSuspensao().getValor()));
            }
        }
        
    }
    
    public void preencherFiltroDistribuicao(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, final Cota cota) {
        
        if (cota.getParametroDistribuicao().getAssistenteComercial() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmAssitPromoComercial(cota.getParametroDistribuicao().getAssistenteComercial());
        }
        
        if (cota.getParametroDistribuicao().getGerenteComercial() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmGerenteComercial(cota.getParametroDistribuicao().getGerenteComercial());
        }
        
        if (cota.getParametroDistribuicao().getObservacao() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setObservacao(cota.getParametroDistribuicao().getObservacao());
        }
        
        if (cota.getParametroDistribuicao().getRepartePorPontoVenda() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setRepartePontoVenda(cota.getParametroDistribuicao().getRepartePorPontoVenda());
        }
        
        if (cota.getParametroDistribuicao().getSolicitaNumAtras() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setSolicitacaoNumAtrasoInternet(cota.getParametroDistribuicao().getSolicitaNumAtras());
        }
        
        if (cota.getParametroDistribuicao().getRecebeRecolheParciais() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setRecebeRecolheProdutosParciais(cota.getParametroDistribuicao().getRecebeRecolheParciais());
        }
        
        this.carregarDadosDistribuicaoEntrega(filtroAlteracaoCotaDTO, cota);
        
        // --Emissao Documentos
        if (cota.getParametroDistribuicao().getSlipImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipImpresso(cota.getParametroDistribuicao().getSlipImpresso());
        }
        
        if (cota.getParametroDistribuicao().getSlipEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipEmail(cota.getParametroDistribuicao().getSlipEmail());
        }
        
        if (cota.getParametroDistribuicao().getBoletoImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoImpresso(cota.getParametroDistribuicao().getBoletoImpresso());
        }
        
        if (cota.getParametroDistribuicao().getBoletoEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoEmail(cota.getParametroDistribuicao().getBoletoEmail());
        }
        
        if (cota.getParametroDistribuicao().getBoletoSlipImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipImpresso(cota.getParametroDistribuicao().getBoletoSlipImpresso());
        }
        
        if (cota.getParametroDistribuicao().getBoletoSlipEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipEmail(cota.getParametroDistribuicao().getBoletoSlipEmail());
        }
        
        if (cota.getParametroDistribuicao().getReciboImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboImpresso(cota.getParametroDistribuicao().getReciboImpresso());
        }
        
        if (cota.getParametroDistribuicao().getReciboEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboEmail(cota.getParametroDistribuicao().getReciboEmail());
        }
        
        if (cota.getParametroDistribuicao().getNotaEnvioImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioImpresso(cota.getParametroDistribuicao().getNotaEnvioImpresso());
        }
        
        if (cota.getParametroDistribuicao().getNotaEnvioEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioEmail(cota.getParametroDistribuicao().getNotaEnvioEmail());
        }
        
        if (cota.getParametroDistribuicao().getChamadaEncalheImpresso() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheImpresso(cota.getParametroDistribuicao().getChamadaEncalheImpresso());
        }
        
        if (cota.getParametroDistribuicao().getChamadaEncalheEmail() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheEmail(cota.getParametroDistribuicao().getChamadaEncalheEmail());
        }
        
    }

	private void carregarDadosDistribuicaoEntrega(final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, final Cota cota) {
		
		// Tipo Entrega
        if (cota.getParametroDistribuicao().getDescricaoTipoEntrega() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setDescricaoTipoEntrega(cota.getParametroDistribuicao().getDescricaoTipoEntrega());
            
            if (cota.getParametroDistribuicao().getDescricaoTipoEntrega().equals(DescricaoTipoEntrega.ENTREGA_EM_BANCA)) {
                if (cota.getParametroDistribuicao().getUtilizaTermoAdesao() != null) {
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setTermoAdesao(cota.getParametroDistribuicao().getUtilizaTermoAdesao());
                }
                if (cota.getParametroDistribuicao().getTermoAdesaoRecebido() != null) {
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setTermoAdesaoRecebido(cota.getParametroDistribuicao().getTermoAdesaoRecebido());
                }
                
            } else {
                if (cota.getParametroDistribuicao().getUtilizaProcuracao() != null) {
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setProcuracao(cota.getParametroDistribuicao().getUtilizaProcuracao());
                }
                if (cota.getParametroDistribuicao().getProcuracaoRecebida() != null) {
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setProcuracaoRecebida(cota.getParametroDistribuicao().getProcuracaoRecebida());
                }
            }
            
            ParametroCobrancaDistribuicaoCota parametro = cota.getParametroCobrancaDistribuicaoCota();

            if(parametro!= null){
            	
            	FiltroModalDistribuicao distribuicaoDTO = filtroAlteracaoCotaDTO.getFiltroModalDistribuicao();
                
                distribuicaoDTO.setCarenciaInicio(cota.getParametroDistribuicao().getInicioPeriodoCarencia());
                distribuicaoDTO.setCarenciaFim(cota.getParametroDistribuicao().getFimPeriodoCarencia());
                distribuicaoDTO.setDiaCobranca(parametro.getDiaCobranca());
                distribuicaoDTO.setDiaSemanaCobranca(parametro.getDiaSemanaCobranca());
                distribuicaoDTO.setModalidadeCobranca(parametro.getModalidadeCobranca());
                distribuicaoDTO.setPercentualFaturamento(parametro.getPercentualFaturamento());
                distribuicaoDTO.setPeriodicidadeCobranca(parametro.getPeriodicidadeCobranca());
                distribuicaoDTO.setPorEntrega(parametro.isPorEntrega());
                distribuicaoDTO.setTaxaFixa(parametro.getTaxaFixa());
                distribuicaoDTO.setBaseCalculo(parametro.getBaseCalculo());
            }
        }
	}
    
    private static void removerFornecedorAssociadoLista(final List<ItemDTO<Long, String>> listFornecedoresCota,
            final List<ItemDTO<Long, String>> fornecedoresAtivos) {
        
        if (listFornecedoresCota != null && !listFornecedoresCota.isEmpty() && fornecedoresAtivos != null
                && !fornecedoresAtivos.isEmpty()) {
            
            for (int i = 0; i < listFornecedoresCota.size(); i++) {
                final ItemDTO<Long, String> fornecedorCota = listFornecedoresCota.get(i);
                if (fornecedorCota != null) {
                    
                    for (int j = 0; j < fornecedoresAtivos.size(); j++) {
                        final ItemDTO<Long, String> fornecedor = fornecedoresAtivos.get(j);
                        if (fornecedor != null && fornecedor.getKey().compareTo(fornecedorCota.getKey()) == 0) {
                            fornecedoresAtivos.remove(j);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    @Post
    public void uploadTermoAdesao(final UploadedFile uploadedFileTermo, final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO)
            throws IOException {
        for (final Long cotaId : filtroAlteracaoCotaDTO.getListaLinhaSelecao()) {
            upload(uploadedFileTermo, cotaId, TipoParametroSistema.PATH_TERMO_ADESAO);
        }
    }
    
    @Post
    public void uploadProcuracao(final UploadedFile uploadedFileProcuracao, final FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO)
            throws IOException {
        for (final Long cotaId : filtroAlteracaoCotaDTO.getListaLinhaSelecao()) {
            upload(uploadedFileProcuracao, cotaId, TipoParametroSistema.PATH_PROCURACAO);
        }
        
    }
    
    private void upload(final UploadedFile uploadedFile, final Long numCota, final TipoParametroSistema parametroPath) throws IOException {
        
        String fileName = "";
        
        if (uploadedFile != null) {
            
            fileService.validarArquivo(1, uploadedFile, EXTENSOES_ACEITAS);
            
            final ParametroSistema raiz = parametroSistemaService
                    .buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
            
            final ParametroSistema path = parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
            
            final String dirBase = (raiz.getValor() + path.getValor() + numCota.toString()).replace("\\", "/");
            
            fileService.setArquivoTemp(dirBase, uploadedFile.getFileName(), uploadedFile.getFile());
            
            fileName = uploadedFile.getFileName();
            final InputStream inputStream = uploadedFile.getFile();
            inputStream.close();
            
        }
        
        result.use(PlainJSONSerialization.class).from(fileName, "result").recursive().serialize();
    }
    
    @Post
    public void validarValoresParaDownload(final BigDecimal taxa, final BigDecimal percentual) {
        
        this.validarPercentualTaxa(percentual, taxa);
        
        result.use(Results.json()).from("", "result").serialize();
    }
    
    @Get
    public void downloadTermoAdesao(final Boolean termoAdesaoRecebido, final List<Integer> idsCotas, final BigDecimal taxa,
            final BigDecimal percentual) throws Exception {
        
        download(termoAdesaoRecebido, idsCotas, TipoParametroSistema.PATH_TERMO_ADESAO, taxa, percentual);
    }
    
    @Get
    public void downloadProcuracao(final Boolean procuracaoRecebida, final List<Integer> numeroCota) throws Exception {
        
        download(procuracaoRecebida, numeroCota, TipoParametroSistema.PATH_PROCURACAO, null, null);
    }
    
    private void download(final Boolean documentoRecebido, final List<Integer> idsCotas, final TipoParametroSistema parametroPath,
            final BigDecimal taxa, final BigDecimal percentual) throws Exception {
        
        final ParametroSistema raiz = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
        
        final ParametroSistema path = parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
        
        final String dirBase = (raiz.getValor() + path.getValor() + idsCotas.toString()).replace("\\", "/");
        
        final ArquivoDTO dto = fileService.obterArquivoTemp(dirBase);
        
        byte[] arquivo = null;
        
        String contentType = null;
        String nomeArquivo = null;
        
        if (dto == null || !documentoRecebido) {
            
            if (TipoParametroSistema.PATH_TERMO_ADESAO.equals(parametroPath)) {
                
            	List<byte[]> pdfs = new ArrayList<>();
            	
            	for(Integer idCota : idsCotas) {
            		Cota c = cotaService.obterPorId(Long.valueOf(idCota));
            		if(c != null) {
            			byte[] pdf = cotaService.getDocumentoTermoAdesao(c.getNumeroCota(), taxa, percentual);
            			pdfs.add( pdf );
            		}
            	}
            	
            	ByteArrayOutputStream byteArrayOutputStream = null; 
                 
                try { 
                	
                  Document document = new Document(); 
                  byteArrayOutputStream = new ByteArrayOutputStream(); 
                  PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream); 

                  document.open(); 
                  PdfContentByte pdfContentByte = pdfWriter.getDirectContent(); 
                  PdfImportedPage pdfImportedPage = null; 

                  for(byte[] pdf : pdfs) {
	                  PdfReader pdfReader = new PdfReader(pdf); 
	
	                  for (int i = 0; i < pdfReader.getNumberOfPages(); i++) { 
	                      document.newPage(); 
	                      pdfImportedPage = pdfWriter.getImportedPage(pdfReader, i + 1); 
	                      float width = pdfImportedPage.getWidth(); 
	                      float height = pdfImportedPage.getHeight(); 
	                      document.setPageSize(new Rectangle(width, height)); // doesent work 
	                      pdfContentByte.addTemplate(pdfImportedPage, 0, 0); 
	                  }
                  }

                  document.close(); 
                  arquivo = byteArrayOutputStream.toByteArray(); 
                } catch (Exception e) {
                	
                }
            	
                nomeArquivo = NOME_DEFAULT_TERMO_ADESAO;
                
            } else {
                
            	ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            	for(Integer idCota : idsCotas) {
            		outputStream.write( cotaService.getDocumentoProcuracao(idCota) );
            	}
                arquivo = outputStream.toByteArray();
                
                nomeArquivo = NOME_DEFAULT_PROCURACAO;
            }
            
            contentType = "application/pdf";
            
        } else {
            
            arquivo = IOUtils.toByteArray(dto.getArquivo());
            
            ((FileInputStream) dto.getArquivo()).close();
            
            contentType = dto.getContentType();
            
            nomeArquivo = dto.getNomeArquivo();
        }
        
        httpResponse.setContentType(contentType);
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);
        
        final OutputStream output = httpResponse.getOutputStream();
        output.write(arquivo);
        
        httpResponse.flushBuffer();
        
    }
    
    private void validarPercentualTaxa(final BigDecimal percentualFaturamento, final BigDecimal taxaFixa) {
        
        if (percentualFaturamento == null && taxaFixa == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O Percentual de Faturamento ou a Taxa Fixa devem ser preenchidos!");
        }
    }
    
    @Post
    public void buscarValorMinimo() {
        
        final List<BigDecimal> valores = parametroCobrancaCotaService.comboValoresMinimos();
        
        result.use(Results.json()).from(valores, "result").serialize();
    }
    
}