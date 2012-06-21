package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.LogExecucaoMensagemService;
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

	@Autowired
	private LogExecucaoMensagemService logExecucaoMensagemService;
	
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
		
		List<InterfaceDTO> lista = logExecucaoMensagemService.listarInterfaces();
		
		String nomeArquivo = "relatorio-interface";
		
		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, lista, InterfaceDTO.class, this.httpServletResponse);
	}

	/**
	 * Exporta arquivos de processos
	 * @param fileType
	 */
	private void exportarProcesso(FileType fileType) {
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

		FiltroInterfacesDTO filtro = carregarFiltro(sortorder, sortname, page, rp);
		
		List<InterfaceDTO> resultado = null;
		try {
			resultado = logExecucaoMensagemService.listarInterfaces();
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
			lista = logExecucaoMensagemService.listarProcessamentoInterface(Long.parseLong(idLogProcessamento), sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
			quantidade = logExecucaoMensagemService.quantidadeProcessamentoInterface(Long.parseLong(idLogProcessamento));
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
	
	@Path("/pesquisarProcessos")
	public void pesquisarProcessos(String sortname, String sortorder, int rp, int page) {
		/*List<RegistroProcessosPainelProcessamentoVO> resultado = null;
		BigDecimal saldoTotal = new BigDecimal("0");
		try {
			resultado = edicoesFechadasService.obterResultadoEdicoesFechadas();
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}*/
	}

	/*private FiltroPainelProcessamentoDTO carregarFiltro(String sortorder, String sortname, int page, int rp) {

		FiltroPainelProcessamentoDTO filtro = new FiltroPainelProcessamentoDTO();

		this.configurarPaginacao(filtro, sortorder, sortname, page, rp);

		FiltroPainelProcessamentoDTO filtroSessao = (FiltroPainelProcessamentoDTO) this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}*/
	
	/**
	 * Configura a paginação dos filtros
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	/*private void configurarPaginacao(FiltroPainelProcessamentoDTO filtro, String sortorder,	String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroPainelProcessamentoDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}*/	

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
	private FiltroInterfacesDTO carregarFiltro(String sortorder, String sortname, int page, int rp) {

		FiltroInterfacesDTO filtro = new FiltroInterfacesDTO();
		
		this.configurarPaginacao(filtro, sortorder, sortname, page, rp);

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
	private void configurarPaginacao(FiltroInterfacesDTO filtro, String sortorder,	String sortname, int page,int rp) {

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
	 * Obtém o filtro de pesquisa para exportação.
	 * @return
	 */
	private FiltroInterfacesDTO obterFiltroParaExportacao() {
		
		FiltroInterfacesDTO filtroSessao =
			(FiltroInterfacesDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_INTERFACES_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		return filtroSessao;
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
