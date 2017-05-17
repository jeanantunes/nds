package br.com.abril.nds.controllers.administracao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSServiceClient;
import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.DetalheInterfaceVO;
import br.com.abril.nds.client.vo.DetalheProcessamentoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CodigoDistribuidorDTO;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheInterfaceDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroProcessosDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.DestinoEncalhe;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoAmbiente;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.FTFService;
import br.com.abril.nds.service.GerarArquivosMicroservicosService;
import br.com.abril.nds.service.InterfaceExecucaoService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PainelProcessamentoService;
import br.com.abril.nds.service.RankingFaturamentoService;
import br.com.abril.nds.service.RankingSegmentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ItemEncalheBandeiraVO;
import br.com.abril.nds.vo.NotaEncalheBandeiraVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author infoA2
 * Controller de painel de processamento
 */
@Resource
@Path("/administracao/painelProcessamento")
@Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO)
public class PainelProcessamentoController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PainelProcessamentoController.class);
    @Autowired
    private Result result;
    
    @Autowired
    private HttpSession session;
    
    private static final String FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE = "filtroPesquisaInterfaces";
    private static final String FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE = "filtroPesquisaProcessos";
    
    private static final String FILTRO_PESQUISA_DETALHES_INTERFACE_SESSION_ATTRIBUTE = "filtroPesquisaDetalheInterfaceGrid";
    private static final String FILTRO_PESQUISA_DETALHES_PROCESSAMENTO_SESSION_ATTRIBUTE = "filtroPesquisaDetalheProcessamentoGrid";
    
    private static final String INTERFACE_EXECUCAO_MATRIZ = "9001";
    private static final String INTERFACE_EXECUCAO_ESTUDO = "9002";
    
    @Autowired
    private PainelProcessamentoService painelProcessamentoService;
    
    @Autowired
    private InterfaceExecucaoService interfaceExecucaoService;
    
    @Autowired
    private RankingSegmentoService rankingSegmentoService;
    
    @Autowired
    private RankingFaturamentoService rankingFaturamentoService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private CobrancaService cobrancaService;
    
    @Autowired
    private NotaFiscalService notaFiscalService;
    
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    @Autowired
    private FTFService ftfService;
	
	@Autowired
	private GerarArquivosMicroservicosService gerarArquivoMatrizService;
    
    private static final int INTERFACE = 1;
    private static final int PROCESSO  = 2;
    
    @Path("/")
    public void index() {
        
    	String codigoDinap = this.distribuidorService.codigoDistribuidorDinap();
        String codigoFC = this.distribuidorService.codigoDistribuidorFC();
        
        this.result.include("codigoDistribuidor", new CodigoDistribuidorDTO(codigoDinap, codigoFC));
    }
    
    /**
     * Exporta o arquivo para o tipo selecionado
     * @param fileType
     * @throws IOException
     */
    @Get
    public void exportar(final FileType fileType, final int tipoRelatorio) throws IOException {
        if (fileType == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
        }
        
        switch (tipoRelatorio) {
        case INTERFACE:
            exportarInterface(fileType);
            break;
        case PROCESSO:
            exportarProcesso(fileType);
            break;
        default:
            throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de relatório " + tipoRelatorio + " não encontrado!");
        }
        
        result.nothing();
    }
    
    /**
     * Exporta arquivos de interface
     * @param fileType
     * @throws IOException
     */
    private void exportarInterface(final FileType fileType) throws IOException {
        final FiltroInterfacesDTO filtroSessao =
                (FiltroInterfacesDTO) session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && filtroSessao.getPaginacao() != null) {
            filtroSessao.getPaginacao().setPaginaAtual(null);
            filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
            
        }
        
        final List<InterfaceDTO> lista = painelProcessamentoService.listarInterfaces(filtroSessao);
        
        final String nomeArquivo = "relatorio-interfaces";
        
        FileExporter.to(nomeArquivo, fileType).inHTTPResponse(
                this.getNDSFileHeader(), filtroSessao, lista, InterfaceDTO.class, httpServletResponse);
    }
    
    /**
     * Exporta arquivos de processos
     * @param fileType
     * @throws IOException
     */
    private void exportarProcesso(final FileType fileType) throws IOException {
        final FiltroProcessosDTO filtroSessao =
                (FiltroProcessosDTO) session.getAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && filtroSessao.getPaginacao() != null) {
            filtroSessao.getPaginacao().setPaginaAtual(null);
            filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
            
        }
        
        final List<ProcessoDTO> lista = painelProcessamentoService.listarProcessos();
        
        final String nomeArquivo = "relatorio-processos";
        
        FileExporter.to(nomeArquivo, fileType).inHTTPResponse(
                this.getNDSFileHeader(), filtroSessao, lista, ProcessoDTO.class, httpServletResponse);
    }
    
    /**
     * Retorna a lista de interfaces
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     * @throws Exception
     */
    @Path("/pesquisarInterfaces")
    public void pesquisarInterfaces(final String codigoDistribuidor, final String sortname, final String sortorder, final int rp, final int page) throws Exception {
        
        final FiltroInterfacesDTO filtro = carregarFiltroInterfaces(codigoDistribuidor, sortorder, sortname, page, rp);
        
        final List<InterfaceDTO> resultado = painelProcessamentoService.listarInterfaces(filtro);
        
        if (resultado == null || resultado.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        } else {
            
            
            final TableModel<CellModelKeyValue<InterfaceDTO>> tableModel = new TableModel<CellModelKeyValue<InterfaceDTO>>();
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultado));
            tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
            tableModel.setTotal(Integer.valueOf(painelProcessamentoService.listarTotalInterfaces(filtro).toString()));
            
            result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        }
        
    }
    
    
    
    @Path("/pesquisarInterfacesMicroDistribuicao")
    public void pesquisarInterfacesMicroDistribuicao() throws Exception {
        
    	 List<InterfaceDTO> resultado = painelProcessamentoService.listarInterfacesExecucaoMicroDistribuicao();
        
        if (resultado == null || resultado.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        } else {
            
            final TableModel<CellModelKeyValue<InterfaceDTO>> tableModel = new TableModel<CellModelKeyValue<InterfaceDTO>>();
            tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultado));
            tableModel.setPage(1);
            tableModel.setTotal(1);
            
            result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        }
        
    }
    
    /**
     * Retorna a lista de mensagens de processamento da interface
     * @param idLogProcessamento
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     */
    public void pesquisarDetalhesInterfaceProcessamento(final String idLogProcessamento, final String dataProcessamento, final String idLogExecucao,
            final String sortname, final String sortorder, final int rp, final int page) throws Exception {
        
        final FiltroDetalheProcessamentoDTO filtro = carregarFiltroDetalhesProcessamento(sortorder, sortname, page, rp);
        
        List<DetalheProcessamentoVO> lista;
        int quantidade = 0;
        try {
            
            Long idProcessamentoLong = 0L;
            if (StringUtils.isNotEmpty(idLogProcessamento)) {
                idProcessamentoLong = Long.parseLong(idLogProcessamento);
            }
            
            Long idLogExecucaoLong = 0L;
            if (StringUtils.isNotEmpty(idLogExecucao)) {
                idLogExecucaoLong = Long.parseLong(idLogExecucao);
            }
            
            filtro.setCodigoLogExecucao(idProcessamentoLong);
            filtro.setIdLogExecucao(idLogExecucaoLong);
            
            Date dataOperacao = null;
            if (StringUtils.isNotEmpty(dataProcessamento)) {
                try {
                    dataOperacao = new SimpleDateFormat("dd/MM/yyyy").parse(dataProcessamento);
                } catch (final ParseException e) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }
            
            filtro.setDataProcessamento(dataOperacao);
            
            lista = painelProcessamentoService.listardetalhesProcessamentoInterface(filtro);
            quantidade = Integer.valueOf( painelProcessamentoService.listarTotaldetalhesProcessamentoInterface(filtro).toString() );
            
            if (lista == null || lista.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
            }
            
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                throw e;
            } else {
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Erro ao pesquisar registros: " + e.getMessage());
            }
        }
        
        final TableModel<CellModelKeyValue<DetalheProcessamentoVO>> tableModel = new TableModel<CellModelKeyValue<DetalheProcessamentoVO>>();
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
        tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
        tableModel.setTotal(quantidade);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        
    }
    
    public void pesquisarDetalhesInterfaceMicroDistribuicao(final String idInterface, final String dataProcessamento, final String idLogExecucao,
            final String sortname, final String sortorder, final int rp, final int page) throws Exception {
        
        try {
        	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dataProcessamento);
		   	List<DetalheProcessamentoVO> lista = painelProcessamentoService.listardetalhesProcessamentoInterface(new FiltroDetalheProcessamentoDTO(null,date, new Long(idInterface),new Long(idLogExecucao)));
	    	
	           
           final TableModel<CellModelKeyValue<DetalheProcessamentoVO>> tableModel = new TableModel<CellModelKeyValue<DetalheProcessamentoVO>>();
           tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
           tableModel.setPage(1);
           tableModel.setTotal(lista.size());
           result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
 
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                throw e;
            } else {
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Erro ao pesquisar registros: " + e.getMessage());
            }
        }
    }
    
    
    private FiltroDetalheProcessamentoDTO carregarFiltroDetalhesProcessamento(final String sortorder, final String sortname, final int page, final int rp) {
        final FiltroDetalheProcessamentoDTO filtro = new FiltroDetalheProcessamentoDTO();
        
        if (filtro != null) {
            
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            
            filtro.setPaginacao(paginacao);
            
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroDetalheProcessamentoDTO.OrdenacaoColunaConsulta.values(), sortname));
        }
        
        final FiltroDetalheProcessamentoDTO filtroSessao = (FiltroDetalheProcessamentoDTO) session.getAttribute(FILTRO_PESQUISA_DETALHES_PROCESSAMENTO_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_DETALHES_PROCESSAMENTO_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
    }
    
    /**
     * Retorna a lista de detalhes da interface
     * @param idLogProcessamento
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     * @throws Exception
     */
    public void pesquisarDetalhesInterface(final String idLogProcessamento, final String sortname, final String sortorder, final int rp, final int page) throws Exception {
        
        final FiltroDetalheInterfaceDTO filtro = carregarFiltroDetalhesInterfaces(sortorder, sortname, page, rp);
        
        List<DetalheInterfaceVO> lista;
        int quantidade = 0;
        try {
            lista = painelProcessamentoService.listarDetalhesInterface(Long.parseLong(idLogProcessamento));
            
            if (lista == null || lista.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
            }
            
            quantidade = lista.size();
            
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                throw e;
            } else {
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Erro ao pesquisar registros: " + e.getMessage());
            }
        }
        
        final List<DetalheInterfaceVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(lista, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
        
        final TableModel<CellModelKeyValue<DetalheInterfaceVO>> tableModel = new TableModel<CellModelKeyValue<DetalheInterfaceVO>>();
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
        tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
        tableModel.setTotal(quantidade);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    private FiltroDetalheInterfaceDTO carregarFiltroDetalhesInterfaces(final String sortorder, final String sortname, final int page, final int rp) {
        final FiltroDetalheInterfaceDTO filtro = new FiltroDetalheInterfaceDTO();
        
        if (filtro != null) {
            
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            
            filtro.setPaginacao(paginacao);
            
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroDetalheInterfaceDTO.OrdenacaoColunaConsulta.values(), sortname));
        }
        
        final FiltroDetalheInterfaceDTO filtroSessao = (FiltroDetalheInterfaceDTO) session.getAttribute(FILTRO_PESQUISA_DETALHES_INTERFACE_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_DETALHES_INTERFACE_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
    }
    
    /**
     * Retorna a lista de mensagens de processos do sistema
     * @param sortname
     * @param sortorder
     * @param rp
     * @param page
     * @throws Exception
     */
    @Path("/pesquisarProcessos")
    public void pesquisarProcessos(final String sortname, final String sortorder, final int rp, final int page) throws Exception {
        
        final FiltroProcessosDTO filtro = carregarFiltroProcessos(sortorder, sortname, page, rp);
        
        List<ProcessoDTO> resultado = null;
        
        try {
            resultado = painelProcessamentoService.listarProcessos();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (e instanceof ValidacaoException) {
                throw e;
            } else {
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Erro ao pesquisar registros: " + e.getMessage());
            }
        }
        
        final List<ProcessoDTO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
        
        final TableModel<CellModelKeyValue<ProcessoDTO>> tableModel = new TableModel<CellModelKeyValue<ProcessoDTO>>();
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
        tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
        tableModel.setTotal(resultado.size());
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
        
    }
    
    /**
     * Retorna o filtro de processos do sistema
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     * @return
     */
    private FiltroProcessosDTO carregarFiltroProcessos(final String sortorder, final String sortname, final int page, final int rp) {
        
        final FiltroProcessosDTO filtro = new FiltroProcessosDTO();
        
        this.configurarPaginacaoProcessos(filtro, sortorder, sortname, page, rp);
        
        final FiltroProcessosDTO filtroSessao = (FiltroProcessosDTO) session.getAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
    }
    
    /**
     * Configura a paginação dos filtros
     * 
     * @param filtro
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     */
    private void configurarPaginacaoProcessos(final FiltroProcessosDTO filtro, final String sortorder, final String sortname, final int page,final int rp) {
        
        if (filtro != null) {
            
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            
            filtro.setPaginacao(paginacao);
            
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroProcessosDTO.OrdenacaoColunaConsulta.values(), sortname));
        }
    }
    
    /**
     * Retorna o estado operacional do sistema (Encerrado, Fechamento, Offline, Operando)
     */
    @Post
    public void buscarEstadoOperacional() {
        final String statusSistemaOperacional = painelProcessamentoService.obterEstadoOperacional();
        result.use(Results.json()).from(statusSistemaOperacional, "result").recursive().serialize();
    }
    
    /**
     * Carrega os filtros de pesquisa
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     * @param codigoFornecedor
     * @param dataAte
     * @param dataDe
     * @return
     */
    private FiltroInterfacesDTO carregarFiltroInterfaces(final String codigoDistribuidor, final String sortorder, final String sortname, final int page, final int rp) {
        
        final FiltroInterfacesDTO filtro = new FiltroInterfacesDTO();
        filtro.setCodigoDistribuidor(codigoDistribuidor);
        
        this.configurarPaginacaoInterfaces(filtro, sortorder, sortname, page, rp);
        
        final FiltroInterfacesDTO filtroSessao = (FiltroInterfacesDTO) session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
        
        if (filtroSessao != null && !filtroSessao.equals(filtro)) {
            filtro.getPaginacao().setPaginaAtual(1);
        }
        
        session.setAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
    }
    
    /**
     * Configura a paginação dos filtros
     * 
     * @param filtro
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     */
    private void configurarPaginacaoInterfaces(final FiltroInterfacesDTO filtro, final String sortorder,	final String sortname, final int page,final int rp) {
        
        if (filtro != null) {
            
            final PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
            
            filtro.setPaginacao(paginacao);
            
            filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroInterfacesDTO.OrdenacaoColunaConsulta.values(), sortname));
        }
    }
    
    /**
     * Executa uma interface
     * @param classeInterface
     */
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void executarInterface(final String idInterface) throws Exception {
        
        interfaceExecucaoService.executarInterface(
                idInterface, getUsuarioLogado(), distribuidorService.codigoDistribuidorFC());
        
        interfaceExecucaoService.executarInterface(
                idInterface, getUsuarioLogado(), distribuidorService.codigoDistribuidorDinap());
            
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Execução da interface foi realizada com sucesso"),
                "result").recursive().serialize();
    }
    
    /**
     * Executa uma interface
     * @param classeInterface
     */
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void executarTodasInterfacesEmOrdem() throws Exception {
        
        interfaceExecucaoService.executarTodasInterfacesProdinEmOrdem(
                getUsuarioLogado(), distribuidorService.codigoDistribuidorFC());
        
        interfaceExecucaoService.executarTodasInterfacesProdinEmOrdem(
                getUsuarioLogado(), distribuidorService.codigoDistribuidorDinap());
        
        interfaceExecucaoService.executarTodasInterfacesMDCEmOrdem(getUsuarioLogado());
        
        result.use(Results.json())
        .from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Execução da interface foi realizada com sucesso"),
                "result").recursive().serialize();
    }
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void gerarRankingSegmento() {
        
        this.rankingSegmentoService.executeJobGerarRankingSegmento();
        
        result.use(Results.json()).from(
            new ValidacaoVO(TipoMensagem.SUCCESS,
                "Execução da geração de ranking de segmento foi realizada com sucesso"), "result").recursive().serialize();
    }
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void exportarCobranca() {
    	
    	Date dataDistribuicaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();
        
    	String msg=this.cobrancaService.processarExportacaoCobranca(dataDistribuicaoDistribuidor);
    	
    	result.use(Results.json()).from(
            new ValidacaoVO(TipoMensagem.SUCCESS,msg), "result").recursive().serialize();
    }
    
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void processarCobrancaConsolidada(String data) {
    	
    	Date dataDistribuicaoDistribuidor = DateUtil.parseDataPTBR(data);
        
    	String msg=this.cobrancaService.processarCobrancaConsolidada(dataDistribuicaoDistribuidor);
    	
    	result.use(Results.json()).from(
            new ValidacaoVO(TipoMensagem.SUCCESS,
                "Cobranças processadas com sucesso!"+msg), "result").recursive().serialize();
    }
    
    
    // enviar notas de devolucao para fornecedor via web service quando nota ainda nao foi enviado
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void processarInterfaceDevolucaoFornecedor() {
    	int nn=0;
    	//Date dataDistribuicaoDistribuidor = distribuidorService.obterDataOperacaoDistribuidor();
    	/*  
        try {
        DevolucaoEncalheBandeirasWSServiceTestCase.test2inserirNotasDevEncalheBandeiras();
        }catch (Exception e )
        {
     	   e.printStackTrace();
        }
      */
    	boolean homolog=true;
    	ParametroSistema parametroSistema = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.NFE_INFORMACOES_AMBIENTE);
    	if(parametroSistema == null || parametroSistema.getValor().equals(String.valueOf(TipoAmbiente.HOMOLOGACAO))) {			
			homolog=true;
		} else {
			homolog=false;
		}
    	 
    	
        String msg=homolog ?"AMBIENTE HOMOLOGACAO":"AMBIENTE PRODUCAO";
        
       
       // int codigoFC = Integer.parseInt(this.distribuidorService.codigoDistribuidorFC());
       // String uf= ( codigoFC == 0 ?"RJ":"SP");
        String uf = this.distribuidorService.obter().getEnderecoDistribuidor().getEndereco().getUf();
        LOGGER.warn("INTERFACE BANDEIRA USANDO UF="+uf);
        int erros=0;
         List<NotaEncalheBandeiraVO> notas = ftfService.obterNotasNaoEnviadas();
	     int cont=0;
	     int semPaletes=0;
	     int semDE=0;
	     int jaenviadas=0;
	     int enviadas=0;
        for (NotaEncalheBandeiraVO nota: notas ) {
	       try {
	    	 
	            LOGGER.warn("ENVIADO NOTA "+(++cont)+" de "+notas.size());
	      
	    	List<ItemEncalheBandeiraVO> itens= ftfService.obterItensNotasNaoEnviadas(nota.getNotaId()) ;
	    	 try {
	    		 
	    		  if ( nota.getQtdSacosPaletes() == null || nota.getQtdSacosPaletes().intValue() <= 0  ) {
	    			  // nao enviar, pois nao foi digitado ainda
	    			  LOGGER.warn("Nota nao enviada  .. sem palete "+nota.getNumNota() );
	    			  semPaletes++;
	    			  continue;
	    		  }
	    		  
	    		  // setar anosemana do encalhe de acordo com destino encalhe do item
	    		 
	    		    boolean semDestino=false;
	    			for(ItemEncalheBandeiraVO item : itens ) {
	    				
		    			DestinoEncalhe destinoEncalhe = notaFiscalService.obterDestinoEncalhe(item.getCodPublicacao().toString(),new Long(item.getNumEdicao()));
		    			if ( destinoEncalhe != null ) {
		    			item.setAnoSemanaRecolhimento(destinoEncalhe.getSemanaRecolhimento());
		    			nota.setNomeDestinoEncalhe(destinoEncalhe.getNomeDestinoDDE());
		    			} else {
		    				  LOGGER.warn("ERRO AO GRAVAR BANDEIRA  ..SEM DESTINO ENCALHE "+nota.getNumNota() );
		    				 msg+="</br>ERRO AO ENVIAR NOTA ->Produto="+item.getCodPublicacao().toString()+" Edicao="+item.getNumEdicao()+" SEM DESTINO ENCALHE  Nota:"+nota.getNumNota() ;
		        	    	  
		        	    	  semDestino=true;
		    			
		    			}
		    			
	    			}
	    			if ( semDestino ) {
	    				semDE++;
	    				continue;
	    				
	    			   }
	    			
	    	          DevolucaoEncalheBandeirasWSServiceClient.enviarNotasDevEncalheBandeiras(nota,itens,homolog,uf);
	    	   
	    	      
	    	       }catch (Exception e )
	    	       {
	    	    	   
	    	    	  if ( e.getLocalizedMessage().contains("0020 - NOTA JA ENVIADA")) {
	    	    		  LOGGER.warn("BANDEIRA:NOTA JA ENVIADA nota="+nota.getNumNota()+ " - " +e.getLocalizedMessage(),e);
	    	    		  msg+="</br>NOTA JA ENVIADA  Nota:"+nota.getNumNota()+" Destino:"+nota.getNomeDestinoEncalhe()+" - " +e.getLocalizedMessage();
	    	    		
	    	    		  ftfService.atualizaFlagInterfaceNotasEnviadas(nota.getNotaId(),true); 
	    	    		  jaenviadas++;
	    	    	  }
	    	    	  else
	    	    	  if ( !e.getLocalizedMessage().contains("BANDEIRA GRAVADA COM SUCESSO")) {
	    	    		 
	    	    		  LOGGER.error("BANDEIRA:ERRO AO GRAVAR  nota="+nota.getNumNota()+ " - " +e.getLocalizedMessage(),e);
	    	    	   String it="";
	    	    	   for ( ItemEncalheBandeiraVO item:  itens)
	    	    		   it +="<"+item.getCodPublicacao()+"/"+item.getNumEdicao()+">";
	    	    	   msg+="</br>ERRO AO ENVIAR NOTA ->"+ e.getLocalizedMessage() +" " +e.getMessage()+ "  Nota:"+nota.getNumNota()+" Destino:"+nota.getNomeDestinoEncalhe()+" Itens:"+it;
	    	    	  
	    	    	   erros++;
	    	    	  }
	    	    	  else {
	    	    		  String it="";
	       	    	   for ( ItemEncalheBandeiraVO item:  itens)
	       	    		   it +="<"+item.getCodPublicacao()+"/"+item.getNumEdicao()+">";
	    	    		  msg+="</br>BANDEIRA GRAVADA COM SUCESSO  Nota:"+nota.getNumNota()+" Destino:"+nota.getNomeDestinoEncalhe()+" Itens:"+it;
	    	    		  LOGGER.warn("BANDEIRA: GRAVADA COM SUCESSO nota="+nota.getNumNota()+ " - " +e.getLocalizedMessage(),e);
	    	    		  ftfService.atualizaFlagInterfaceNotasEnviadas(nota.getNotaId(),true);
	    	    		  enviadas++;
	    	    	  }
	    	       }
		       } catch ( Exception ee ) {
		    	   LOGGER.error("ERRO ENVIANDO NOTA "+nota.getNumNota(),ee);
		    	   msg+="ERRO ENVIADO NOTA "+nota.getNumNota()+" Erro:"+ee;
		       }
    	 
        } 
    	
        msg = "</br>Interface de Devolucao ao Fornecedor processadas "+
                 "</br>Total Notas Processadas             :"+notas.size()+
                 "</br>Notas Enviadas                      :"+enviadas+
                 "</br>Notas que ja tinham sido Enviadas   :"+jaenviadas+
                 "</br>Notas com Erros                     :"+erros+
                 "</br>Notas Sem Paletes                   :"+semPaletes+
                 "</br>Sem Destino Encalhe                 :"+semDE+
                "</br></br>Mensagens            :"+msg;
         LOGGER.error(msg); 
    	result.use(Results.json()).from(
            new ValidacaoVO(TipoMensagem.WARNING,
                msg), "result").recursive().serialize();
    }
    
    @Rules(Permissao.ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO)
    public void gerarRankingFaturamento() {
        
        this.rankingFaturamentoService.executeJobGerarRankingFaturamento();
        
        result.use(Results.json()).from(
            new ValidacaoVO(TipoMensagem.SUCCESS,
                "Execução da geração de ranking de faturamento foi realizada com sucesso"), "result").recursive().serialize();
    }
    
    @Path("/downloadArquivo")
	public void downloadArquivo(final String idInterface, final String dataInterfaceExecucao) throws FileNotFoundException, IOException{
    	try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dataInterfaceExecucao);
			if(idInterface.equals(INTERFACE_EXECUCAO_MATRIZ)){
				gerarArquivoMatrizService.gerarArquivoMatriz(date);
    		}else if(idInterface.equals(INTERFACE_EXECUCAO_ESTUDO)){
    			gerarArquivoMatrizService.gerarArquivoDeapr(date);
    			gerarArquivoMatrizService.gerarArquivoDeajo(date);
    		}
		//	gerarArquivoMatrizService.gerarArquivoDeapr(cl.getTime());  // 9002
		//	gerarArquivoMatrizService.gerarArquivoDeajo(cl.getTime());  // 9002
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Arquivo enviado com sucesso!"),"result").recursive().serialize();
		}
    	
    	catch (ParseException e) {
			e.printStackTrace();
		}
	}
    
    
    
	@Path("/uploadArquivo")
	public void uploadArquivo(final String idInterface) throws FileNotFoundException, IOException{
			
		
    	gerarArquivoMatrizService.processarMicrodistribuicao(idInterface, getUsuarioLogado(), null);
		
    	//Teste para gerar os arquivos de texto. Remover após criação do processo automatizado.
    	Calendar cl = Calendar.getInstance();
		gerarArquivoMatrizService.gerarArquivoMatriz(cl.getTime()); // 9001 
		gerarArquivoMatrizService.gerarArquivoDeapr(cl.getTime());  // 9002
		gerarArquivoMatrizService.gerarArquivoDeajo(cl.getTime());  // 9002
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Arquivo enviado com sucesso!"),"result").recursive().serialize();
	}
    
}