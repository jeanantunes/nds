package br.com.abril.nds.controllers.administracao;

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
import br.com.abril.nds.dto.ArquivoDTO;
import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroModalFornecedor;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
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
    
    public static final FileType[] extensoesAceitas = { FileType.DOC, FileType.DOCX, FileType.BMP, FileType.GIF,
            FileType.PDF, FileType.JPEG, FileType.JPG, FileType.PNG };
    
    private Result result;
    
    private static final String NOME_DEFAULT_TERMO_ADESAO = "termo_adesao.pdf";
    
    private static final String NOME_DEFAULT_PROCURACAO = "procuracao.pdf";
    
    public AlteracaoCotaController(Result result) {
        super();
        this.result = result;
    }
    
    @Path("/")
    public void index() {
        result.include("listFornecedores", fornecedorService.obterFornecedoresAtivos());
        result.include("listBairros", enderecoService.obterBairrosCotas());
        result.include("listMunicipios", enderecoService.obterMunicipiosCotas());
        
        List<Integer> listaVencimento = new ArrayList<Integer>();
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
    public void buscarBairroPorCidade(String cidade) {
        List<String> bairros = enderecoService.obterBairrosPorCidade(cidade);
        result.use(CustomJson.class).from(bairros).serialize();
    }
    
    @Post("/pesquisarAlteracaoCota.json")
    public void pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname,
            String sortorder, int rp, int page) {
        
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortname);
        if ("DESC".equalsIgnoreCase(sortorder)) {
            paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);
        } else {
            paginacao.setOrdenacao(PaginacaoVO.Ordenacao.DESC);
        }
        filtroAlteracaoCotaDTO.setPaginacao(paginacao);
        
        filtroAlteracaoCotaDTO.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtroAlteracaoCotaDTO.getNomeCota()));
        
        List<ConsultaAlteracaoCotaDTO> listaCotas = this.alteracaoCotaService
                .pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
        
        if (listaCotas == null || listaCotas.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        final int qtdCotas = this.alteracaoCotaService.contarAlteracaoCota(filtroAlteracaoCotaDTO);
        
        this.result.use(FlexiGridJson.class).from(listaCotas).total(qtdCotas).page(page).serialize();
        
    }
    
    @Post
    @Rules(Permissao.ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO)
    public void carregarCamposAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, String sortname, int page, int rp) {
        
        List<Fornecedor> listaFornecedoresAtivos = fornecedorService.obterFornecedores();
        List<ItemDTO<Long, String>> fornecedoresAtivos = getFornecedores(listaFornecedoresAtivos);
        
        // Carregará os dados apenas se o usuário selecionar uma linha do grid
        // p/ alteração.
        if (filtroAlteracaoCotaDTO != null && filtroAlteracaoCotaDTO.getListaLinhaSelecao() != null
            && filtroAlteracaoCotaDTO.getListaLinhaSelecao().size() == 1) {
            
            List<ItemDTO<Long, String>> listFornecedoresCota = new ArrayList<ItemDTO<Long, String>>();
            
            Long cotaId = filtroAlteracaoCotaDTO.getListaLinhaSelecao().get(0);
            
            Cota cota = cotaService.obterPorId(cotaId);
            
            if (cotaId != null) {
                listFornecedoresCota = getFornecedores(fornecedorService.obterFornecedoresCota(cotaId));
                removerFornecedorAssociadoLista(listFornecedoresCota, fornecedoresAtivos);
            }

            FiltroModalFornecedor filtroModalFornecedor = filtroAlteracaoCotaDTO.getFiltroModalFornecedor();
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
        
        this.result.use(Results.json()).from(filtroAlteracaoCotaDTO, "filtroAlteracaoCotaDTO").recursive().serialize();
    }
    
    private List<ItemDTO<Long, String>> getFornecedores(List<Fornecedor> fornecedores) {
        
        List<ItemDTO<Long, String>> itensFornecedor = new ArrayList<ItemDTO<Long, String>>();
        
        for (Fornecedor fornecedor : fornecedores) {
            
            itensFornecedor
                    .add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
        }
        
        return itensFornecedor;
    }
    
    @Post
    public void salvarAlteracao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
        
        if (filtroAlteracaoCotaDTO.getFiltroModalFornecedor() != null
            && filtroAlteracaoCotaDTO.getFiltroModalFornecedor().getListaFornecedoresSelecionados() != null
            && filtroAlteracaoCotaDTO.getFiltroModalFornecedor().getListaFornecedoresSelecionados().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos 1 Fornecedor");
        }
        
        this.alteracaoCotaService.salvarAlteracoes(filtroAlteracaoCotaDTO);
        
        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota alterada com sucesso."),
                Constantes.PARAM_MSGS).recursive().serialize();
    }
    
    public void preencherFiltroFinanceiro(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {
        
        // FINANCEIRO
        if (cota.getParametroCobranca().getFatorVencimento() != null)
            filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setIdVencimento(
                    cota.getParametroCobranca().getFatorVencimento());
        
        if (cota.getParametroCobranca().getValorMininoCobranca() != null)
            filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrMinimo(
                    String.valueOf(cota.getParametroCobranca().getValorMininoCobranca()));
        if (cota.getParametroCobranca().getPoliticaSuspensao() != null) {
            if (cota.getParametroCobranca().getPoliticaSuspensao().getNumeroAcumuloDivida() != null)
                filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setQtdDividaEmAberto(
                        cota.getParametroCobranca().getPoliticaSuspensao().getNumeroAcumuloDivida());
            
            if (cota.getParametroCobranca().getPoliticaSuspensao().getValor() != null)
                filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().setVrDividaEmAberto(
                        String.valueOf(cota.getParametroCobranca().getPoliticaSuspensao().getValor()));
        }
        
    }
    
    public void preencherFiltroDistribuicao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {
        
        if (cota.getParametroDistribuicao().getAssistenteComercial() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmAssitPromoComercial(
                    cota.getParametroDistribuicao().getAssistenteComercial());
        
        if (cota.getParametroDistribuicao().getGerenteComercial() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setNmGerenteComercial(
                    cota.getParametroDistribuicao().getGerenteComercial());
        
        if (cota.getParametroDistribuicao().getObservacao() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setObservacao(
                    cota.getParametroDistribuicao().getObservacao());
        
        if (cota.getParametroDistribuicao().getRepartePorPontoVenda() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRepartePontoVenda(
                    cota.getParametroDistribuicao().getRepartePorPontoVenda());
        
        if (cota.getParametroDistribuicao().getSolicitaNumAtras() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsSolicitacaoNumAtrasoInternet(
                    cota.getParametroDistribuicao().getSolicitaNumAtras());
        
        if (cota.getParametroDistribuicao().getRecebeRecolheParciais() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setIsRecebeRecolheProdutosParciais(
                    cota.getParametroDistribuicao().getRecebeRecolheParciais());
        
        // Tipo Entrega
        if (cota.getParametroDistribuicao().getDescricaoTipoEntrega() != null) {
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setDescricaoTipoEntrega(
                    cota.getParametroDistribuicao().getDescricaoTipoEntrega());
            
            if (cota.getParametroDistribuicao().getDescricaoTipoEntrega().equals(DescricaoTipoEntrega.ENTREGA_EM_BANCA)) {
                if (cota.getParametroDistribuicao().getUtilizaTermoAdesao() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setTermoAdesao(
                            cota.getParametroDistribuicao().getUtilizaTermoAdesao());
                if (cota.getParametroDistribuicao().getTermoAdesaoRecebido() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setTermoAdesaoRecebido(
                            cota.getParametroDistribuicao().getTermoAdesaoRecebido());
                // TODO arquivo
                if (cota.getParametroDistribuicao().getPercentualFaturamento() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setPercentualFaturamentoEntregaBanca(
                            cota.getParametroDistribuicao().getPercentualFaturamento());
                if (cota.getParametroDistribuicao().getTaxaFixa() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setTaxaFixaEntregaBanca(
                            cota.getParametroDistribuicao().getTaxaFixa());
                if (cota.getParametroDistribuicao().getInicioPeriodoCarencia() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setCarenciaInicioEntregaBanca(
                            cota.getParametroDistribuicao().getInicioPeriodoCarencia());
                if (cota.getParametroDistribuicao().getFimPeriodoCarencia() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setCarenciaFimEntregaBanca(
                            cota.getParametroDistribuicao().getFimPeriodoCarencia());
            } else {
                if (cota.getParametroDistribuicao().getUtilizaProcuracao() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setProcuracao(
                            cota.getParametroDistribuicao().getUtilizaProcuracao());
                if (cota.getParametroDistribuicao().getProcuracaoRecebida() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setProcuracaoRecebida(
                            cota.getParametroDistribuicao().getProcuracaoRecebida());
                if (cota.getParametroDistribuicao().getPercentualFaturamento() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setPercentualFaturamentoEntregador(
                            cota.getParametroDistribuicao().getPercentualFaturamento());
                if (cota.getParametroDistribuicao().getInicioPeriodoCarencia() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setCarenciaInicioEntregador(
                            cota.getParametroDistribuicao().getInicioPeriodoCarencia());
                if (cota.getParametroDistribuicao().getFimPeriodoCarencia() != null)
                    filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().setCarenciaFimEntregador(
                            cota.getParametroDistribuicao().getFimPeriodoCarencia());
            }
        }
        
        // --Emissao Documentos
        if (cota.getParametroDistribuicao().getSlipImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipImpresso(
                    cota.getParametroDistribuicao().getSlipImpresso());
        
        if (cota.getParametroDistribuicao().getSlipEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsSlipEmail(
                    cota.getParametroDistribuicao().getSlipEmail());
        
        if (cota.getParametroDistribuicao().getBoletoImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoImpresso(
                    cota.getParametroDistribuicao().getBoletoImpresso());
        
        if (cota.getParametroDistribuicao().getBoletoEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoEmail(
                    cota.getParametroDistribuicao().getBoletoEmail());
        
        if (cota.getParametroDistribuicao().getBoletoSlipImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipImpresso(
                    cota.getParametroDistribuicao().getBoletoSlipImpresso());
        
        if (cota.getParametroDistribuicao().getBoletoSlipEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsBoletoSlipEmail(
                    cota.getParametroDistribuicao().getBoletoSlipEmail());
        
        if (cota.getParametroDistribuicao().getReciboImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboImpresso(
                    cota.getParametroDistribuicao().getReciboImpresso());
        
        if (cota.getParametroDistribuicao().getReciboEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsReciboEmail(
                    cota.getParametroDistribuicao().getReciboEmail());
        
        if (cota.getParametroDistribuicao().getNotaEnvioImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioImpresso(
                    cota.getParametroDistribuicao().getNotaEnvioImpresso());
        
        if (cota.getParametroDistribuicao().getNotaEnvioEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsNotaEnvioEmail(
                    cota.getParametroDistribuicao().getNotaEnvioEmail());
        
        if (cota.getParametroDistribuicao().getChamadaEncalheImpresso() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc()
                    .setIsChamdaEncalheImpresso(cota.getParametroDistribuicao().getChamadaEncalheImpresso());
        
        if (cota.getParametroDistribuicao().getChamadaEncalheEmail() != null)
            filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getFiltroCheckDistribEmisDoc().setIsChamdaEncalheEmail(
                    cota.getParametroDistribuicao().getChamadaEncalheEmail());
        
    }
    
    private static void removerFornecedorAssociadoLista(List<ItemDTO<Long, String>> listFornecedoresCota,
            List<ItemDTO<Long, String>> fornecedoresAtivos) {
        
        if (listFornecedoresCota != null && listFornecedoresCota.size() > 0 && fornecedoresAtivos != null
            && fornecedoresAtivos.size() > 0) {
            
            for (int i = 0; i < listFornecedoresCota.size(); i++) {
                ItemDTO<Long, String> fornecedorCota = listFornecedoresCota.get(i);
                if (fornecedorCota != null) {

                    if (fornecedoresAtivos.size() == 0) {
                        break;
                    }
                    for (int j = 0; j < fornecedoresAtivos.size(); j++) {
                        ItemDTO<Long, String> fornecedor = fornecedoresAtivos.get(j);
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
    public void uploadTermoAdesao(UploadedFile uploadedFileTermo, FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO)
            throws IOException {
        for (Long cotaId : filtroAlteracaoCotaDTO.getListaLinhaSelecao()) {
            upload(uploadedFileTermo, cotaId, TipoParametroSistema.PATH_TERMO_ADESAO);
        }
    }
    
    @Post
    public void uploadProcuracao(UploadedFile uploadedFileProcuracao, FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO)
            throws IOException {
        for (Long cotaId : filtroAlteracaoCotaDTO.getListaLinhaSelecao()) {
            upload(uploadedFileProcuracao, cotaId, TipoParametroSistema.PATH_PROCURACAO);
        }
        
    }
    
    private void upload(UploadedFile uploadedFile, Long numCota, TipoParametroSistema parametroPath) throws IOException {
        
        String fileName = "";
        
        if (uploadedFile != null) {
            
            this.fileService.validarArquivo(1, uploadedFile, extensoesAceitas);
            
            ParametroSistema raiz = this.parametroSistemaService
                    .buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
            
            ParametroSistema path = this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
            
            String dirBase = (raiz.getValor() + path.getValor() + numCota.toString()).replace("\\", "/");
            
            fileService.setArquivoTemp(dirBase, uploadedFile.getFileName(), uploadedFile.getFile());
            
            fileName = uploadedFile.getFileName();
            InputStream inputStream = uploadedFile.getFile();
            inputStream.close();
            
        }
        
        this.result.use(PlainJSONSerialization.class).from(fileName, "result").recursive().serialize();
    }
    
    @Post
    public void validarValoresParaDownload(BigDecimal taxa, BigDecimal percentual) {
        
        this.validarPercentualTaxa(percentual, taxa);
        
        this.result.use(Results.json()).from("", "result").serialize();
    }
    
    @Get
    public void downloadTermoAdesao(Boolean termoAdesaoRecebido, Integer numeroCota, BigDecimal taxa,
            BigDecimal percentual) throws Exception {
        
        download(termoAdesaoRecebido, numeroCota, TipoParametroSistema.PATH_TERMO_ADESAO, taxa, percentual);
    }
    
    @Get
    public void downloadProcuracao(Boolean procuracaoRecebida, Integer numeroCota) throws Exception {
        
        download(procuracaoRecebida, numeroCota, TipoParametroSistema.PATH_PROCURACAO, null, null);
    }

    private void download(Boolean documentoRecebido, Integer numeroCota, TipoParametroSistema parametroPath,
            BigDecimal taxa, BigDecimal percentual) throws Exception {
        
        ParametroSistema raiz = this.parametroSistemaService
                .buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
        
        ParametroSistema path = this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);
        
        String dirBase = (raiz.getValor() + path.getValor() + numeroCota.toString()).replace("\\", "/");
        
        ArquivoDTO dto = fileService.obterArquivoTemp(dirBase);
        
        byte[] arquivo = null;
        
        String contentType = null;
        String nomeArquivo = null;
        
        if (dto == null || !documentoRecebido) {
            
            if (TipoParametroSistema.PATH_TERMO_ADESAO.equals(parametroPath)) {
                
                arquivo = this.cotaService.getDocumentoTermoAdesao(numeroCota, taxa, percentual);
                
                nomeArquivo = NOME_DEFAULT_TERMO_ADESAO;
                
            } else {
                
                arquivo = this.cotaService.getDocumentoProcuracao(numeroCota);
                
                nomeArquivo = NOME_DEFAULT_PROCURACAO;
            }
            
            contentType = "application/pdf";
            
        } else {
            
            arquivo = IOUtils.toByteArray(dto.getArquivo());
            
            ((FileInputStream) dto.getArquivo()).close();
            
            contentType = dto.getContentType();
            
            nomeArquivo = dto.getNomeArquivo();
        }
        
        this.httpResponse.setContentType(contentType);
        this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);
        
        OutputStream output = this.httpResponse.getOutputStream();
        output.write(arquivo);
        
        httpResponse.flushBuffer();
        
    }
    
    private void validarPercentualTaxa(BigDecimal percentualFaturamento, BigDecimal taxaFixa) {
        
        if (percentualFaturamento == null && taxaFixa == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O Percentual de Faturamento ou a Taxa Fixa devem ser preenchidos!");
        }
    }
    
    @Post
    public void buscarValorMinimo() {
        
        List<BigDecimal> valores = parametroCobrancaCotaService.comboValoresMinimos();
        
        this.result.use(Results.json()).from(valores, "result").serialize();
    }

}