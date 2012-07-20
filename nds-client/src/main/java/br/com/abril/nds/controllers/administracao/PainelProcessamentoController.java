package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.dto.filtro.FiltroProcessosDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.InterfaceExecucaoService;
import br.com.abril.nds.service.PainelProcessamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
public class PainelProcessamentoController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	private static final String FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE = "filtroPesquisaInterfaces";
	private static final String FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE = "filtroPesquisaProcessos";

	@Autowired
	private PainelProcessamentoService painelProcessamentoService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private InterfaceExecucaoService interfaceExecucaoService;

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
	}

	/**
	 * Exporta arquivos de interface
	 * @param fileType
	 * @throws IOException 
	 */
	private void exportarInterface(FileType fileType) throws IOException {
		FiltroInterfacesDTO filtroSessao = (FiltroInterfacesDTO) this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<InterfaceDTO> lista = painelProcessamentoService.listarInterfaces();
		
		String nomeArquivo = "relatorio-interfaces";
		
		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, lista, InterfaceDTO.class, this.httpServletResponse);
	}

	/**
	 * Exporta arquivos de processos
	 * @param fileType
	 * @throws IOException 
	 */
	private void exportarProcesso(FileType fileType) throws IOException {
		FiltroProcessosDTO filtroSessao = (FiltroProcessosDTO) this.session.getAttribute(FILTRO_PESQUISA_PROCESSOS_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<ProcessoDTO> lista = painelProcessamentoService.listarProcessos();
		
		String nomeArquivo = "relatorio-processos";
		
		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, lista, ProcessoDTO.class, this.httpServletResponse);
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
		
		List<InterfaceDTO> resultado = null;
		try {
			resultado = painelProcessamentoService.listarInterfaces();
		} catch (Exception e) {
			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultado == null || resultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {

			List<InterfaceDTO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

			TableModel<CellModelKeyValue<InterfaceDTO>> tableModel = new TableModel<CellModelKeyValue<InterfaceDTO>>();
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(resultado.size());

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
	public void pesquisarDetalhesInterfaceProcessamento(String idLogProcessamento, String sortname, String sortorder, int rp, int page) throws Exception {

		List<LogExecucaoMensagem> lista;
		Long quantidade = 0L;
		try {
			lista = painelProcessamentoService.listarProcessamentoInterface(Long.parseLong(idLogProcessamento), sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
			quantidade = painelProcessamentoService.quantidadeProcessamentoInterface(Long.parseLong(idLogProcessamento));
		} catch (Exception e) {
			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		result.use(FlexiGridJson.class).from(lista).total(quantidade.intValue()).page(page).serialize();
		
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
	 * Obtém os dados do cabeçalho de exportação.
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}

	/**
	 * Executa uma interface
	 * @param classeInterface
	 */
	public void executarInterface(String classeInterface) throws Exception {
		interfaceExecucaoService.executarInterface(classeInterface, getUsuario());
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Execução da interface foi realizada com sucesso"),"result").recursive().serialize();
	}
	
	/**
	 * Retorna o usuário logado
	 * @return
	 */
	// TODO: Implementar quando funcionar
	private Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");
		return usuario;
	}
}
