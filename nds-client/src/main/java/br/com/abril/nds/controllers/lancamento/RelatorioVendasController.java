package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.RegistroCurvaABCCotaVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO.ColunaOrdenacaoCurvaABCCota;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO.ColunaOrdenacaoCurvaABCEditor;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author infoA2
 * Controller do relatório de vendas
 */
@Resource
@Path("/lancamento/relatorioVendas")
public class RelatorioVendasController {

	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EditorService editorService;

	private static final String FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "filtroPesquisaCurbaABCDistribuidor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE = "filtroPesquisaCurbaABCEditor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_PRODUTO_SESSION_ATTRIBUTE = "filtroPesquisaCurbaABCProduto";
	
	private static final int DISTRIBUIDOR = 1;
	private static final int EDITOR       = 2;
	private static final int PRODUTO      = 3;
	private static final int COTA         = 4;

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	/**
	 * Inicializa a página, populando os combos
	 */
	@Path("/")
	public void index() {
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO));
		result.include("editores", editorService.obterEditores());
		result.include("municipios", enderecoService.obterMunicipiosCotas());
	}
	
	public RelatorioVendasController(Result result) {
		this.result = result;
	}

	/**
	 * Exporta para o tipo de arquivo passado em fileType 
	 * @param fileType
	 * @param tipoRelatorio
	 * @throws IOException
	 */
	@Get
	public void exportar(FileType fileType, int tipoRelatorio) throws IOException {
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}

		switch (tipoRelatorio) {
		case DISTRIBUIDOR:

			FiltroCurvaABCDistribuidorDTO filtroSessao = (FiltroCurvaABCDistribuidorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);
			if (filtroSessao != null) {
				if (filtroSessao.getPaginacao() != null) {				
					filtroSessao.getPaginacao().setPaginaAtual(null);
					filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
				}
			}
			
			List<RegistroCurvaABCDistribuidorVO> lista = distribuidorService.obterCurvaABCDistribuidor(filtroSessao);
			ResultadoCurvaABC resultadoTotal = distribuidorService.obterCurvaABCDistribuidorTotal(filtroSessao);
			
			FileExporter.to("consulta-edicoes-fechadas-com-saldo", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoTotal, lista, RegistroCurvaABCDistribuidorVO.class, this.httpServletResponse);
			
			break;

		case EDITOR:
			break;

		case PRODUTO:
			break;

		case COTA:
			break;

		default:
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de relatório " + tipoRelatorio + " não encontrado!");
		}
		
	}
	
	/**
	 * Valida os dados de entrada
	 * @param dataDe
	 * @param dataAte
	 */
	private void validarDadosEntradaPesquisa(String dataDe, String dataAte) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null || dataDe.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null || dataAte.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}

	/**
	 * Valida os dados de entrada de relatórios de Curva ABC por produto
	 * @param dataDe
	 * @param dataAte
	 * @param codigoProduto
	 * @param nomeProduto
	 */
	private void validarDadosEntradaPesquisaProduto(String dataDe, String dataAte, String codigoProduto, String nomeProduto) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null || dataDe.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null || dataAte.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if ((codigoProduto == null || codigoProduto.isEmpty()) && (nomeProduto == null || nomeProduto.isEmpty())) {
			listaMensagemValidacao.add("O preenchimento do campo nome do Produto ou Código do Produto é obrigatório!");
		}
		
		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}

	/**
	 * Valida os dados de entrada de relatório
	 * @param dataDe
	 * @param dataAte
	 * @param codigoCota
	 * @param nomeCota
	 */
	private void validarDadosEntradaPesquisaCota(String dataDe, String dataAte, String codigoCota, String nomeCota) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null || dataDe.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null || dataAte.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if ((codigoCota == null || codigoCota.isEmpty()) && (nomeCota == null || nomeCota.isEmpty())) {
			listaMensagemValidacao.add("O preenchimento do campo nome da Cota ou Código da Cota é obrigatório!");
		}
		
		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}

	}

	/**
	 * Realiza a pesquisa do histórico do editor
	 * @param dataDe
	 * @param dataAte
	 * @param codigoEditor
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarHistoricoEditor")
	public void pesquisarHistoricoEditor(String dataDe, String dataAte, String codigoEditor) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroPesquisarHistoricoEditorDTO filtro = new FiltroPesquisarHistoricoEditorDTO(sdf.parse(dataDe), sdf.parse(dataAte), codigoEditor);

		List<RegistroHistoricoEditorVO> resultado = null;
		try {
			resultado = editorService.obterHistoricoEditor(filtro);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultado == null
				|| resultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {
			
			TableModel<CellModelKeyValue<RegistroHistoricoEditorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroHistoricoEditorVO>>();
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultado));
			tableModel.setPage(1);
			tableModel.setTotal(resultado.size());
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	
	}
	
	/**
	 * Realiza a pesquisa da curva ABC do distribuidor
	 * @param dataDe
	 * @param dataAte
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCDistribuidor")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte,
			String sortorder, String sortname, int page, int rp)
			throws Exception {
		pesquisarCurvaABCDistribuidor(dataDe, dataAte, 0L, "", "", "", 0L, "",
				"", "", sortorder, sortname, page, rp);
	}

	/**
	 * Realiza a pesquisa da curva ABC do distribuidor com os parametros da busca avançada
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCDistribuidorAvancada")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {

		this.validarDadosEntradaPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCDistribuidorDTO filtro = carregarFiltroPesquisaDistribuidor(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname, page, rp);

		List<RegistroCurvaABCDistribuidorVO> resultadoCurvaABCDistribuidor = null;
		try {
			resultadoCurvaABCDistribuidor = distribuidorService.obterCurvaABCDistribuidor(filtro);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultadoCurvaABCDistribuidor == null
				|| resultadoCurvaABCDistribuidor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = resultadoCurvaABCDistribuidor.size();

			List<RegistroCurvaABCDistribuidorVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(resultadoCurvaABCDistribuidor, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);

			ResultadoCurvaABC<RegistroCurvaABCDistribuidorVO> resultado = distribuidorService.obterCurvaABCDistribuidorTotal(filtro);
			resultado.setTableModel(tableModel);
			
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

		}

	}

	/**
	 * Realiza a pesquisa da curva ABC do editor
	 * @param dataDe
	 * @param dataAte
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCEditor")
	public void pesquisarCurvaABCEditor(String dataDe, String dataAte, String sortorder,
			String sortname, int page, int rp)
			throws Exception {
		pesquisarCurvaABCEditor(dataDe, dataAte, 0L, "", "", "", 0L, "",
				"", "", sortorder, sortname, page, rp);
	}

	/**
	 * Realiza a pesquisa da curva ABC do editor com os parametros da busca avançada
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCEditorAvancada")
	public void pesquisarCurvaABCEditor(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {
		this.validarDadosEntradaPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO = carregarFiltroPesquisaEditor(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname, page, rp);

		List<RegistroCurvaABCEditorVO> resultadoCurvaABCEditor = null;
		try {
			resultadoCurvaABCEditor = editorService.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultadoCurvaABCEditor == null
				|| resultadoCurvaABCEditor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = resultadoCurvaABCEditor.size();

			/*PaginacaoUtil
					.armazenarQtdRegistrosPesquisa(
							this.session,
							QTD_REGISTROS_PESQUISA_CURVA_COTA_ABC_SESSION_ATTRIBUTE,
							qtdeTotalRegistros);*/

			TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCEditor));
			tableModel.setPage(filtroCurvaABCEditorDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);

			ResultadoCurvaABC resultado = editorService.obterCurvaABCEditorTotal(filtroCurvaABCEditorDTO);
			resultado.setTableModel(tableModel);
			
			//session.setAttribute(RESULTADO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE, resultado);
			
			result.use(Results.json()).withoutRoot().from(resultado)
					.recursive().serialize();
		}
		
	}

	/**
	 * Realiza a pesquisa da curva ABC por produto
	 * @param dataDe
	 * @param dataAte
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCProduto")
	public void pesquisarCurvaABCProduto(String dataDe, String dataAte,
			String codigoProduto, String nomeProduto, String sortorder,
			String sortname, int page, int rp) throws Exception {
		pesquisarCurvaABCProduto(dataDe, dataAte, 0L, codigoProduto, nomeProduto, "", 0L, "", "", "", sortorder, sortname, page, rp);
	}
	
	/**
	 * Realiza a pesquisa da curva ABC por produto avançada
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCProdutoAvancada")
	public void pesquisarCurvaABCProduto(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {
		
			validarDadosEntradaPesquisaProduto(dataDe, dataAte, codigoProduto, nomeProduto);
		
			pesquisarCurvaABCDistribuidor(dataDe, dataAte, codigoFornecedor, codigoProduto, nomeProduto, edicaoProduto, codigoEditor, codigoCota,
					nomeCota, municipio, sortorder, sortname, page, rp);
		
	}

	/**
	 * Realiza a pesquisa da curva ABC por cota
	 * @param dataDe
	 * @param dataAte
	 * @param codigoCota
	 * @param nomeCota
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCCota")
	public void pesquisarCurvaABCCota(String dataDe, String dataAte, 
			String codigoCota, String nomeCota, String sortorder,
			String sortname, int page, int rp)
			throws Exception {
		pesquisarCurvaABCCota(dataDe, dataAte, 0L, "", "", "", 0L, codigoCota, nomeCota, "", sortorder, sortname, page, rp);
	}

	/**
	 * Realiza a pesquisa da curva ABC por cota com os parametros da pesquisa avançada
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisarCurvaABCCotaAvancada")
	public void pesquisarCurvaABCCota(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {
		
		this.validarDadosEntradaPesquisaCota(dataDe, dataAte, codigoCota, nomeCota);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO = carregarFiltroPesquisaCota(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname, page, rp);

		List<RegistroCurvaABCCotaVO> resultadoCurvaABCCota = null;
		try {
			resultadoCurvaABCCota = cotaService.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar registros: " + e.getMessage());
			}
		}

		if (resultadoCurvaABCCota == null
				|| resultadoCurvaABCCota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = resultadoCurvaABCCota.size();

			/*PaginacaoUtil
					.armazenarQtdRegistrosPesquisa(
							this.session,
							QTD_REGISTROS_PESQUISA_CURVA_COTA_ABC_SESSION_ATTRIBUTE,
							qtdeTotalRegistros);*/

			TableModel<CellModelKeyValue<RegistroCurvaABCCotaVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCCotaVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCCota));
			tableModel.setPage(filtroCurvaABCCotaDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);

			ResultadoCurvaABC resultado = cotaService.obterCurvaABCCotaTotal(filtroCurvaABCCotaDTO);
			resultado.setTableModel(tableModel);
			
			//session.setAttribute(RESULTADO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE, resultado);
			
			result.use(Results.json()).withoutRoot().from(resultado)
					.recursive().serialize();
		}
		
	}

	/**
	 * Carrega filtro da pesquisa por editor 
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @return
	 */
	private FiltroCurvaABCEditorDTO carregarFiltroPesquisaEditor(Date dataDe, Date dataAte, Long codigoFornecedor, 
			String codigoProduto, String nomeProduto, String edicaoProduto, Long codigoEditor,
			String codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {

		FiltroCurvaABCEditorDTO filtro = new FiltroCurvaABCEditorDTO(dataDe, dataAte, (codigoFornecedor == null ? "" : codigoFornecedor.toString()),
				codigoProduto, nomeProduto, edicaoProduto, (codigoEditor == null ? "" : codigoEditor.toString()),
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoEditorPesquisa(filtro, sortorder, sortname,
				page, rp);

		FiltroCurvaABCEditorDTO filtroSessao = (FiltroCurvaABCEditorDTO) this.session
				.getAttribute(FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE);

		/*PaginacaoUtil.calcularPaginaAtual(this.session,
				QTD_REGISTROS_PESQUISA_CURVA_EDITOR_ABC_SESSION_ATTRIBUTE,
				FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE,
				filtro, filtroSessao);*/

		return filtro;
	}
	
	/**
	 * Carrega o filtro da pesquisa por distribuidor
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @return
	 */
	private FiltroCurvaABCDistribuidorDTO carregarFiltroPesquisaDistribuidor(Date dataDe, Date dataAte, Long codigoFornecedor, 
			String codigoProduto, String nomeProduto, String edicaoProduto, Long codigoEditor,
			String codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {

		FiltroCurvaABCDistribuidorDTO filtro = new FiltroCurvaABCDistribuidorDTO(dataDe, dataAte, (codigoFornecedor == null ? "" : codigoFornecedor.toString()),
				codigoProduto, nomeProduto, edicaoProduto, (codigoEditor == null ? "" : codigoEditor.toString()),
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoDistribuidorPesquisa(filtro, sortorder, sortname, page, rp);

		FiltroCurvaABCDistribuidorDTO filtroSessao = (FiltroCurvaABCDistribuidorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}

	/**
	 * Carrega filtro da pesquisa por cota
	 * @param dataDe
	 * @param dataAte
	 * @param codigoFornecedor
	 * @param codigoProduto
	 * @param nomeProduto
	 * @param edicaoProduto
	 * @param codigoEditor
	 * @param codigoCota
	 * @param nomeCota
	 * @param municipio
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @return
	 */
	private FiltroCurvaABCCotaDTO carregarFiltroPesquisaCota(Date dataDe, Date dataAte, Long codigoFornecedor, 
			String codigoProduto, String nomeProduto, String edicaoProduto, Long codigoEditor,
			String codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {

		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(dataDe, dataAte, (codigoFornecedor == null ? "" : codigoFornecedor.toString()),
				codigoProduto, nomeProduto, edicaoProduto, (codigoEditor == null ? "" : codigoEditor.toString()),
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoCotaPesquisa(filtro, sortorder, sortname,
				page, rp);

		/*FiltroCurvaABCCotaDTO filtroSessao = (FiltroCurvaABCCotaDTO) this.session
				.getAttribute(FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE);

		PaginacaoUtil.calcularPaginaAtual(this.session,
				QTD_REGISTROS_PESQUISA_CURVA_COTA_ABC_SESSION_ATTRIBUTE,
				FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE,
				filtro, filtroSessao);*/

		return filtro;
	}	

	/**
	 * Configura paginação da pesquisa do editor
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoEditorPesquisa(FiltroCurvaABCEditorDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(
					ColunaOrdenacaoCurvaABCEditor.values(), sortname));
		}
	}
	
	/**
	 * Configura a paginação da pesquisa por cota
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoCotaPesquisa(FiltroCurvaABCCotaDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(
					ColunaOrdenacaoCurvaABCCota.values(), sortname));
		}
	}

	/**
	 * Configura a paginação da pesquisa do distribuidor
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoDistribuidorPesquisa(FiltroCurvaABCDistribuidorDTO filtro, String sortorder, String sortname, int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroCurvaABCDistribuidorDTO.ColunaOrdenacaoCurvaABCDistribuidor.values(), sortname));
		}
	}

	/**
	 * Obtém os dados do cabeçalho de exportação.
	 * @return
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
	 * Retorna o usuário logado
	 * @return
	 */
	//TODO: Implementar quando a segurança for implementada
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
}
