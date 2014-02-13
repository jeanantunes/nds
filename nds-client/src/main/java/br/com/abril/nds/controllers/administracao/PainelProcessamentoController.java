package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.DetalheInterfaceVO;
import br.com.abril.nds.client.vo.DetalheProcessamentoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheInterfaceDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroProcessosDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.InterfaceExecucaoService;
import br.com.abril.nds.service.PainelProcessamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
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

	@Autowired
	private PainelProcessamentoService painelProcessamentoService;
	
	@Autowired
	private InterfaceExecucaoService interfaceExecucaoService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private HttpServletResponse httpServletResponse;

	private static final int INTERFACE = 1;
	private static final int PROCESSO  = 2;
	
	@Path("/")
	public void index() {
	}

	/**
	 * Exporta o arquivo para o tipo selecionado
	 * @param fileType
	 * @throws IOException
	 */
	@Get
	public void exportar(FileType fileType, int tipoRelatorio) throws IOException {
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
		
		this.result.nothing();
	}

	/**
	 * Exporta arquivos de interface
	 * @param fileType
	 * @throws IOException 
	 */
	private void exportarInterface(FileType fileType) throws IOException {
		FiltroInterfacesDTO filtroSessao = 
			(FiltroInterfacesDTO) this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<InterfaceDTO> lista = painelProcessamentoService.listarInterfaces(filtroSessao);
		
		String nomeArquivo = "relatorio-interfaces";
		
		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(
			this.getNDSFileHeader(), filtroSessao, lista, InterfaceDTO.class, this.httpServletResponse);
	}

	/**
	 * Exporta arquivos de processos
	 * @param fileType
	 * @throws IOException 
	 */
	private void exportarProcesso(FileType fileType) throws IOException {
		FiltroProcessosDTO filtroSessao = 
				(FiltroProcessosDTO) this.session.getAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<ProcessoDTO> lista = painelProcessamentoService.listarProcessos();
		
		String nomeArquivo = "relatorio-processos";
		
		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtroSessao, lista, ProcessoDTO.class, this.httpServletResponse);
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
	public void pesquisarInterfaces(String sortname, String sortorder, int rp, int page) throws Exception {

		FiltroInterfacesDTO filtro = carregarFiltroInterfaces(sortorder, sortname, page, rp);
		
		List<InterfaceDTO> resultado = painelProcessamentoService.listarInterfaces(filtro);

		if (resultado == null || resultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {

			//List<InterfaceDTO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

			TableModel<CellModelKeyValue<InterfaceDTO>> tableModel = new TableModel<CellModelKeyValue<InterfaceDTO>>();
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultado));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(Integer.valueOf(painelProcessamentoService.listarTotalInterfaces(filtro).toString()));

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
	public void pesquisarDetalhesInterfaceProcessamento(String idLogProcessamento, String dataProcessamento, String idLogExecucao,
			String sortname, String sortorder, int rp, int page) throws Exception {

		FiltroDetalheProcessamentoDTO filtro = carregarFiltroDetalhesProcessamento(sortorder, sortname, page, rp);
		
		List<DetalheProcessamentoVO> lista;
		int quantidade = 0;
		try {

			Long idProcessamentoLong = 0l;
			if(!StringUtil.isEmpty(idLogProcessamento)){
				idProcessamentoLong = Long.parseLong(idLogProcessamento);
			}
			
			Long idLogExecucaoLong = 0l;
			if(!StringUtil.isEmpty(idLogExecucao)){
				idLogExecucaoLong = Long.parseLong(idLogExecucao);
			}
			
			filtro.setCodigoLogExecucao(idProcessamentoLong);
			filtro.setIdLogExecucao(idLogExecucaoLong);
			
			Date dataOperacao = null;
			if(!StringUtil.isEmpty(dataProcessamento)){
				try {
					dataOperacao = new SimpleDateFormat("dd/MM/yyyy").parse(dataProcessamento);
				} catch (ParseException e) {
                    LOGGER.error(e.getMessage(), e);
				}
			}
			
			filtro.setDataProcessamento(dataOperacao);
			
			lista = painelProcessamentoService.listardetalhesProcessamentoInterface(filtro);
			quantidade = Integer.valueOf( painelProcessamentoService.listarTotaldetalhesProcessamentoInterface(filtro).toString() );
			
			if (lista == null || lista.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
			
		} catch (Exception e) {
			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		//List<DetalheProcessamentoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(lista, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

		TableModel<CellModelKeyValue<DetalheProcessamentoVO>> tableModel = new TableModel<CellModelKeyValue<DetalheProcessamentoVO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());		
		tableModel.setTotal(quantidade);		
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	private FiltroDetalheProcessamentoDTO carregarFiltroDetalhesProcessamento(String sortorder, String sortname, int page, int rp) {
		FiltroDetalheProcessamentoDTO filtro = new FiltroDetalheProcessamentoDTO();

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroDetalheProcessamentoDTO.OrdenacaoColunaConsulta.values(), sortname));
		}

		FiltroDetalheProcessamentoDTO filtroSessao = (FiltroDetalheProcessamentoDTO) this.session.getAttribute(FILTRO_PESQUISA_DETALHES_PROCESSAMENTO_SESSION_ATTRIBUTE);

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
	public void pesquisarDetalhesInterface(String idLogProcessamento, String sortname, String sortorder, int rp, int page) throws Exception {

		FiltroDetalheInterfaceDTO filtro = carregarFiltroDetalhesInterfaces(sortorder, sortname, page, rp);

		List<DetalheInterfaceVO> lista;
		int quantidade = 0;
		try {
			lista = painelProcessamentoService.listarDetalhesInterface(Long.parseLong(idLogProcessamento));
			
			if (lista == null || lista.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
			
            quantidade = lista.size();

		} catch (Exception e) {
			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		List<DetalheInterfaceVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(lista, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

		TableModel<CellModelKeyValue<DetalheInterfaceVO>> tableModel = new TableModel<CellModelKeyValue<DetalheInterfaceVO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());		
		tableModel.setTotal(quantidade);		
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private FiltroDetalheInterfaceDTO carregarFiltroDetalhesInterfaces(String sortorder, String sortname, int page, int rp) {
		FiltroDetalheInterfaceDTO filtro = new FiltroDetalheInterfaceDTO();

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroDetalheInterfaceDTO.OrdenacaoColunaConsulta.values(), sortname));
		}

		FiltroDetalheInterfaceDTO filtroSessao = (FiltroDetalheInterfaceDTO) this.session.getAttribute(FILTRO_PESQUISA_DETALHES_INTERFACE_SESSION_ATTRIBUTE);

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
	public void pesquisarProcessos(String sortname, String sortorder, int rp, int page) throws Exception {

		FiltroProcessosDTO filtro = carregarFiltroProcessos(sortorder, sortname, page, rp);
		
		List<ProcessoDTO> resultado = null;
		
		try {
			resultado = painelProcessamentoService.listarProcessos();
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}
		
		List<ProcessoDTO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

		TableModel<CellModelKeyValue<ProcessoDTO>> tableModel = new TableModel<CellModelKeyValue<ProcessoDTO>>();
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
	private FiltroProcessosDTO carregarFiltroProcessos(String sortorder, String sortname, int page, int rp) {

		FiltroProcessosDTO filtro = new FiltroProcessosDTO();

		this.configurarPaginacaoProcessos(filtro, sortorder, sortname, page, rp);

		FiltroProcessosDTO filtroSessao = (FiltroProcessosDTO) this.session.getAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE);

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
	private void configurarPaginacaoProcessos(FiltroProcessosDTO filtro, String sortorder, String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroProcessosDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}	

	/**
	 * Retorna o estado operacional do sistema (Encerrado, Fechamento, Offline, Operando)
	 */
	@Post
	public void buscarEstadoOperacional() {
		String statusSistemaOperacional = painelProcessamentoService.obterEstadoOperacional();
		this.result.use(Results.json()).from(statusSistemaOperacional, "result").recursive().serialize();
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
	private FiltroInterfacesDTO carregarFiltroInterfaces(String sortorder, String sortname, int page, int rp) {

		FiltroInterfacesDTO filtro = new FiltroInterfacesDTO();
		
		this.configurarPaginacaoInterfaces(filtro, sortorder, sortname, page, rp);

		FiltroInterfacesDTO filtroSessao = (FiltroInterfacesDTO) this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);

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
	private void configurarPaginacaoInterfaces(FiltroInterfacesDTO filtro, String sortorder,	String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroInterfacesDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}

	/**
	 * Executa uma interface
	 * @param classeInterface
	 */
	public void executarInterface(String idInterface) throws Exception {
		
		if (this.interfaceExecucaoService.isInterfaceProdin(idInterface)) {
			
			interfaceExecucaoService.executarInterface(
				idInterface, getUsuarioLogado(), this.distribuidorService.codigoDistribuidorFC());
			
			interfaceExecucaoService.executarInterface(
				idInterface, getUsuarioLogado(), this.distribuidorService.codigoDistribuidorDinap());
			
		} else {
			
			interfaceExecucaoService.executarInterface(idInterface, getUsuarioLogado(), null);
		}
		
        result.use(Results.json())
.from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Execução da interface foi realizada com sucesso"),
                        "result").recursive().serialize();
	}
	
	/**
	 * Executa uma interface
	 * @param classeInterface
	 */
	public void executarTodasInterfacesEmOrdem() throws Exception {
		
		interfaceExecucaoService.executarTodasInterfacesProdinEmOrdem(
			getUsuarioLogado(), this.distribuidorService.codigoDistribuidorFC());
		
		interfaceExecucaoService.executarTodasInterfacesProdinEmOrdem(
			getUsuarioLogado(), this.distribuidorService.codigoDistribuidorDinap());
		
		interfaceExecucaoService.executarTodasInterfacesMDCEmOrdem(getUsuarioLogado());
		
        result.use(Results.json())
.from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Execução da interface foi realizada com sucesso"),
                        "result").recursive().serialize();
	}
	
}