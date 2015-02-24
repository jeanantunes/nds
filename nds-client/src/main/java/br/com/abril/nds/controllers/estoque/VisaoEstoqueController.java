package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.VisaoEstoqueConferenciaCegaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.VisaoEstoqueDetalheJuramentadoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.VisaoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("estoque/visaoEstoque")
@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
public class VisaoEstoqueController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VisaoEstoqueController.class);
    
    private static final String FILTRO_VISAO_ESTOQUE = "FILTRO_VISAO_ESTOQUE";
    private static final String LISTA_CONFERENCIA_CEGA = "LISTA_CONFERENCIA_CEGA";
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private VisaoEstoqueService visaoEstoqueService;
    
    @Autowired
    private HttpServletResponse httpServletResponse;
    
    @Autowired
    private HttpSession session;
    
    private final Result result;
    
    public VisaoEstoqueController(final Result result) {
        super();
        this.result = result;
    }
    
    @Path("/")
    public void index() {
        final List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
        result.include("listFornecedores", listFornecedores);
        result.include("dataAtual", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
    }
    
    @Path("/pesquisar.json")
    public void pesquisar(final FiltroConsultaVisaoEstoque filtro) {
        
        if (session.getAttribute(LISTA_CONFERENCIA_CEGA) != null) {
            session.removeAttribute(LISTA_CONFERENCIA_CEGA);
        }
        
        tratarErro(validarDadosConsulta(filtro));
        
        this.atualizarDataMovimentacao(filtro);
        
        session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
        
        final List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
        
        result.use(FlexiGridJson.class).from(listVisaoEstoque).total(listVisaoEstoque.size()).serialize();
        
    }
    
    @Path("/pesquisarDetalhe.json")
    public void pesquisarDetalhe(final FiltroConsultaVisaoEstoque filtro, String sortname, String sortorder,
            final int rp, final int page) {
        
        if (session.getAttribute(LISTA_CONFERENCIA_CEGA) != null) {
            session.removeAttribute(LISTA_CONFERENCIA_CEGA);
        }
        
        tratarErro(validarDadosConsulta(filtro));
        
        this.atualizarDataMovimentacao(filtro);
        
        session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
        
        sortorder = verificaUndefined(sortorder);
        
        sortname = verificaUndefined(sortname);
        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
        
        if (filtro.getPaginar() != null && !filtro.getPaginar()) {
            filtro.getPaginacao().setQtdResultadosPorPagina(rp);
            filtro.getPaginacao().setPaginaAtual(page);
        }
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        this.atualizarDataMovimentacao(filtro);
        
        if (filtro.getDataMovimentacao().compareTo(dataOperacao) < 0) {
            
            filtro.setBuscaHistorico(true);
        }
        
        session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
        
        final Long count = visaoEstoqueService.obterCountVisaoEstoqueDetalhe(filtro);
        
        final List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
        
        final Map<String, Object> mapaDetalhes = new HashMap<>();
        
        mapaDetalhes.put("listDetalhe", this.getDetalhesTableModel(listDetalhe, filtro.getTipoEstoque(), page, count
                .intValue()));
        
        mapaDetalhes.put("isBuscaHistorico", filtro.isBuscaHistorico());
        
        result.use(CustomJson.class).from(mapaDetalhes).serialize();
    }
    
    private String verificaUndefined(String sortorder) {
        if ("undefined".equalsIgnoreCase(sortorder)) {
            sortorder = null;
        }
        return sortorder;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private TableModel getDetalhesTableModel(final List<? extends VisaoEstoqueDetalheDTO> listDetalhe,
            final String tipoEstoque, final int page, final int total) {
        
        final TableModel table = new TableModel<CellModelKeyValue<VisaoEstoqueDetalheDTO>>();
        
        table.setRows(CellModelKeyValue.toCellModelKeyValue((List<VisaoEstoqueDetalheJuramentadoDTO>) listDetalhe));
        table.setPage(page);
        table.setTotal(total);
        
        return table;
    }
    
    @Path("/transferir")
    public void transferir(final FiltroConsultaVisaoEstoque filtro) {
        
        tratarErro(validarDadosConsulta(filtro));
        
        this.atualizarDataMovimentacao(filtro);
        
        session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
        
        final TipoEstoque entrada = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoqueSelecionado());
        final TipoEstoque saida = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoque());
        
        filtro.setGrupoMovimentoEntrada(this.getGrupoMovimentoTransferencia(entrada, true));
        filtro.setGrupoMovimentoSaida(this.getGrupoMovimentoTransferencia(saida, false));
        
        visaoEstoqueService.transferirEstoque(filtro, this.getUsuarioLogado());
        
        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Transferência realizada com sucesso.");
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
    }
    
    private GrupoMovimentoEstoque getGrupoMovimentoTransferencia(final TipoEstoque tipoEstoque, final boolean isEntrada) {
        
        switch (tipoEstoque) {
        
        case LANCAMENTO:
            return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO
                    : GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
        case SUPLEMENTAR:
            return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR
                    : GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
        case RECOLHIMENTO:
            return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO
                    : GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
        case PRODUTOS_DANIFICADOS:
            return isEntrada ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS
                    : GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
        default:
            return null;
        }
    }
    
    @Path("/inventario")
    public void inventario(final FiltroConsultaVisaoEstoque filtro) {
        
        tratarErro(validarDadosConsulta(filtro));
        
        this.atualizarDataMovimentacao(filtro);
        
        session.setAttribute(FILTRO_VISAO_ESTOQUE, filtro);
        
        final TipoEstoque tipoEstoque = Util.getEnumByStringValue(TipoEstoque.values(), filtro.getTipoEstoque());
        
        visaoEstoqueService.atualizarInventarioEstoque(filtro.getListaTransferencia(), tipoEstoque, this.getUsuarioLogado());
        
        final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Atualização de inventário concluída com sucesso.");
        
        result.use(Results.json()).from(validacao, "result").serialize();
    }
    
    @Path("/exportar")
    public void exportar(final FileType fileType) throws IOException {
        
        final FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) session
                .getAttribute(FILTRO_VISAO_ESTOQUE);
        
        final List<VisaoEstoqueDTO> listVisaoEstoque = visaoEstoqueService.obterVisaoEstoque(filtro);
        
        FileExporter.to("visao-estoque", fileType).inHTTPResponse(this.getNDSFileHeader(filtro.getDataMovimentacao()),
                null, null, listVisaoEstoque, VisaoEstoqueDTO.class, httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Path("/exportarDetalhe")
    public void exportarDetalhe(final FileType fileType) throws IOException {
        
        final FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) session
                .getAttribute(FILTRO_VISAO_ESTOQUE);
        
        if (filtro.getPaginacao() == null) {
            filtro.setPaginacao(new PaginacaoVO());
        }
        
        filtro.getPaginacao().setPaginaAtual(null);
        filtro.getPaginacao().setQtdResultadosPorPagina(null);
        filtro.getPaginacao().setQtdResultadosTotal(null);
        
        final List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
        Class clazz = VisaoEstoqueDetalheDTO.class;
        
        if (listDetalhe != null && !listDetalhe.isEmpty()
                && listDetalhe.get(0) instanceof VisaoEstoqueDetalheJuramentadoDTO) {
            clazz = VisaoEstoqueDetalheJuramentadoDTO.class;
        }
        
        FileExporter.to("visao-estoque", fileType).inHTTPResponse(this.getNDSFileHeader(filtro.getDataMovimentacao()),
                null, null, listDetalhe, clazz, httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    @Path("/gerarDadosConferenciaCega")
    public void gerarDadosConferenciaCega(final FiltroConsultaVisaoEstoque filtro) throws IOException {
        
        this.atualizarDataMovimentacao(filtro);
        
        final List<? extends VisaoEstoqueDetalheDTO> listDetalhe = visaoEstoqueService.obterVisaoEstoqueDetalhe(filtro);
        
        final List<VisaoEstoqueConferenciaCegaVO> listaExport = new ArrayList<VisaoEstoqueConferenciaCegaVO>();
        
        for (final VisaoEstoqueDetalheDTO dto : listDetalhe) {
            
            final VisaoEstoqueConferenciaCegaVO vo = new VisaoEstoqueConferenciaCegaVO(dto);
            
            listaExport.add(vo);
        }
        
        session.setAttribute(LISTA_CONFERENCIA_CEGA, listaExport);
        result.use(Results.nothing());
    }
    
    @SuppressWarnings("unchecked")
    @Path("/exportarConferenciaCega")
    public void exportarConferenciaCega(final FileType fileType) throws IOException {
        
        final FiltroConsultaVisaoEstoque filtro = (FiltroConsultaVisaoEstoque) session
                .getAttribute(FILTRO_VISAO_ESTOQUE);
        
        gerarDadosConferenciaCega(filtro);
        final List<VisaoEstoqueConferenciaCegaVO> listaExport = (List<VisaoEstoqueConferenciaCegaVO>) session
                .getAttribute(LISTA_CONFERENCIA_CEGA);
        
        if (listaExport == null || listaExport.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
        }
        
        FileExporter.to("visao-estoque-conferencia-cega", fileType).inHTTPResponse(getNDSFileHeader(), null, null,
                listaExport, VisaoEstoqueConferenciaCegaVO.class, httpServletResponse);
        
        result.use(Results.nothing());
    }
    
    private void atualizarDataMovimentacao(final FiltroConsultaVisaoEstoque filtro) {
        
        if (filtro == null || filtro.getDataMovimentacaoStr() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Valores de filtros inválidos.");
        }
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        if (filtro.getDataMovimentacao() == null || filtro.getDataMovimentacaoStr() == null
                || DateUtil.isDataInicialMaiorDataFinal(filtro.getDataMovimentacao(), dataOperacao)) {
            
            filtro.setDataMovimentacao(dataOperacao);
        }
        
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            filtro.setDataMovimentacao(sdf.parse(filtro.getDataMovimentacaoStr()));
        } catch (final ParseException e) {
            LOGGER.debug(e.getMessage(), e);
            filtro.setDataMovimentacao(dataOperacao);
        }
        
    }
    
    private List<String> validarDadosConsulta(final FiltroConsultaVisaoEstoque filtro) {
        
        final List<String> mensagens = new ArrayList<String>();
        if (filtro != null && filtro.getDataMovimentacaoStr() != null && !"".equals(filtro.getDataMovimentacaoStr())) {
            
            if (!DateUtil.isValidDate(filtro.getDataMovimentacaoStr(), "dd/MM/yyyy")) {
                
                mensagens.add("O campo Data Movimento de é inválido");
            } else {
                filtro.setDataMovimentacao(DateUtil.parseDataPTBR(filtro.getDataMovimentacaoStr()));
            }
        }
        
        return mensagens;
    }
    
    private void tratarErro(final List<String> mensagensErro) {
        
        final ValidacaoVO validacao = new ValidacaoVO();
        
        validacao.setTipoMensagem(TipoMensagem.ERROR);
        
        if (!mensagensErro.isEmpty()) {
            
            validacao.setListaMensagens(mensagensErro);
            
            throw new ValidacaoException(validacao);
        }
    }
}