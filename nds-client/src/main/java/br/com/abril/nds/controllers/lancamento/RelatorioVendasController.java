package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCExportacaoDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.client.vo.ResultadoCurvaABCEditor;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.RegistroRankingSegmentoDTO;
import br.com.abril.nds.dto.ResultadoCurvaABCCotaDTO;
import br.com.abril.nds.dto.TotalizadorRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.TipoConsultaCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RelatorioVendasService;
import br.com.abril.nds.service.TipoSegmentoProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
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
@Rules(Permissao.ROLE_LANCAMENTO_RELATORIO_VENDAS)
public class RelatorioVendasController extends BaseController {

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
	private RelatorioVendasService relatorioVendasService;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private TipoSegmentoProdutoService tipoSegmentoProdutoService;

	private static final String FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "filtroPesquisaCurvaABCDistribuidor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE = "filtroPesquisaCurvaABCEditor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE = "filtroPesquisaCurvaABCCota";
	private static final String FILTRO_PESQUISA_CURVA_ABC_HISTORICO_EDITOR_SESSION_ATTRIBUTE = "filtroPesquisaCurvaABCHistoricoEditor";
	private static final String FILTRO_PESQUISA_RANKING_SEGMENTO = "filtroPesquisaRankingSegmento";
	
	private static final int DISTRIBUIDOR 	  = 1;
	private static final int EDITOR       	  = 2;
	private static final int PRODUTO      	  = 3;
	private static final int COTA         	  = 4;
	private static final int HISTORICO_EDITOR = 5;
	private static final int SEGMENTO 		  = 6;

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	/**
	 * Inicializa a página, populando os combos
	 */
	@Path("/")
	public void index() {
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO));
		result.include("editores", editorService.obterEditoresDesc());
		result.include("municipios", enderecoService.obterMunicipiosCotas());
		result.include("segmentacoes", tipoSegmentoProdutoService.obterTipoSegmentoProdutoOrdenados(Ordenacao.ASC));
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
			
		    if(this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE) == null) {
	            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma pesquisa realizada.");
	        }
		    
		    exportarDistribuidor(fileType, TipoConsultaCurvaABC.DISTRIBUIDOR);
			break;
		case EDITOR:
			exportarEditor(fileType);
			break;
		case PRODUTO:
			exportarDistribuidor(fileType, TipoConsultaCurvaABC.PRODUTO);
			break;
		case COTA:
			exportarCota(fileType);
			break;
		case HISTORICO_EDITOR:
			exportarHistoricoEditor(fileType);
			break;
		case SEGMENTO:
			this.exportarSegmento(fileType);
			break;
		default:
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de relatório " + tipoRelatorio + " não encontrado!");
		}
		
	}
	
	/**
	 * Exporta arquivos para filtros do tipo distribuidor e produto (que é a pesquisa do distribuidor, mas com o produto como campo obrigatório)
	 * @param fileType
	 * @throws IOException
	 */
	private void exportarDistribuidor(FileType fileType, TipoConsultaCurvaABC tipoRelatorio) throws IOException {
		FiltroCurvaABCDistribuidorDTO filtroSessao = (FiltroCurvaABCDistribuidorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<RegistroCurvaABCDistribuidorVO> lista = null;
		
		filtroSessao.setTipoConsultaCurvaABC(tipoRelatorio);
		
		if (tipoRelatorio == TipoConsultaCurvaABC.DISTRIBUIDOR) {
			
			lista = relatorioVendasService.obterCurvaABCDistribuidor(filtroSessao);
			
		} else {
			
			lista = relatorioVendasService.obterCurvaABCProduto(filtroSessao);
		
		}
		
		List<RegistroCurvaABCExportacaoDistribuidorVO> exportacao = new ArrayList<RegistroCurvaABCExportacaoDistribuidorVO>();
		
		for(RegistroCurvaABCDistribuidorVO item: lista){
			exportacao.add(new RegistroCurvaABCExportacaoDistribuidorVO(item));
		}
		
		ResultadoCurvaABCDistribuidor resultadoTotal = this.obterTotaisCurvaABCDistribuidor(lista);
		
		String nomeArquivo = "";
		
		if (tipoRelatorio == TipoConsultaCurvaABC.DISTRIBUIDOR) {
			nomeArquivo = "relatorio-vendas-curva-abc-distribuidor";
		} else {
			nomeArquivo = "relatorio-vendas-curva-abc-produto";
		}

		FileExporter.to(nomeArquivo, fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoTotal, exportacao, RegistroCurvaABCExportacaoDistribuidorVO.class, this.httpServletResponse);
		
		result.nothing();
	}
	
	private void exportarSegmento(FileType fileType) throws IOException {
		
		FiltroRankingSegmentoDTO filtro = (FiltroRankingSegmentoDTO) this.session.getAttribute(FILTRO_PESQUISA_RANKING_SEGMENTO);
		
		if (filtro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Realiza uma pesquisa.");
		}
		
		filtro.setPaginacao(null);
		
		List<RegistroRankingSegmentoDTO> rankingSegmento = this.relatorioVendasService.obterRankingSegmento(filtro);
		
		FileExporter.to("ranking-segmento", fileType).inHTTPResponse(
			this.getNDSFileHeader(), filtro, rankingSegmento, RegistroRankingSegmentoDTO.class, this.httpServletResponse
		);
		
		this.result.nothing();
	}
	
	
	/**
	 * Exporta arquivos para filtros do tipo editor
	 * @param fileType
	 * @throws IOException
	 */
	private void exportarEditor(FileType fileType) throws IOException {
		FiltroCurvaABCEditorDTO filtroSessao = (FiltroCurvaABCEditorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<RegistroCurvaABCEditorVO> lista = this.relatorioVendasService.obterCurvaABCEditor(filtroSessao);
		
		ResultadoCurvaABCEditor resultado = this.obterTotaisCurvaABCEditor(lista);
		
		FileExporter.to("relatorio-vendas-curva-abc-editor", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtroSessao, resultado, lista, 
				RegistroCurvaABCEditorVO.class, this.httpServletResponse);
		
		result.nothing();
	}

	/**
	 * Exporta arquivos para filtros do tipo cota
	 * @param fileType
	 * @throws IOException
	 */
	private void exportarCota(FileType fileType) throws IOException {
		FiltroCurvaABCCotaDTO filtroSessao = (FiltroCurvaABCCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<RegistroCurvaABCCotaDTO> lista = this.relatorioVendasService.obterCurvaABCCota(filtroSessao);
		
		ResultadoCurvaABCCotaDTO resultadoTotal = this.obterTotaisCurvaABCCota(lista);
		
		FileExporter.to("relatorio-vendas-curva-abc-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), 
				filtroSessao, resultadoTotal, lista, 
				RegistroCurvaABCCotaDTO.class, this.httpServletResponse);
		
		result.nothing();
	}

	/**
	 * Exporta arquivos para filtros do tipo histórico do editor
	 * @param fileType
	 * @throws IOException
	 */
	private void exportarHistoricoEditor(FileType fileType) throws IOException {
		FiltroPesquisarHistoricoEditorDTO filtroSessao = (FiltroPesquisarHistoricoEditorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_HISTORICO_EDITOR_SESSION_ATTRIBUTE);
		if (filtroSessao != null) {
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		
		List<RegistroHistoricoEditorVO> lista = relatorioVendasService.obterHistoricoEditor(filtroSessao);
		
		FileExporter.to("consulta-historico-editor", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, lista, RegistroHistoricoEditorVO.class, this.httpServletResponse);
		
		result.nothing();
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
	private void validarDadosEntradaPesquisaCota(String dataDe, String dataAte, Integer codigoCota, String nomeCota) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (dataDe == null || dataDe.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período De é obrigatório!");
		}

		if (dataAte == null || dataAte.isEmpty()) {
			listaMensagemValidacao
					.add("O preenchimento do campo Período Até é obrigatório!");
		}

		if ((codigoCota == null) && (nomeCota == null || nomeCota.isEmpty())) {
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
	public void pesquisarHistoricoEditor(String dataDe, String dataAte, String codigoEditor, 
			String sortorder, String sortname, int page, int rp) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		
		FiltroPesquisarHistoricoEditorDTO filtro = 
				carregarFiltroHistoricoEditor(sortorder, sortname, page, rp, 
						sdf.parse(dataDe), sdf.parse(dataAte), codigoEditor);	

		
		
		List<RegistroHistoricoEditorVO> resultado = null;
		try {
			resultado = relatorioVendasService.obterHistoricoEditor(filtro);
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
			
			int qtdeTotalRegistros = resultado.size();

			List<RegistroHistoricoEditorVO> resultadoPaginado = 
				PaginacaoUtil.paginarEOrdenarEmMemoria(
					resultado, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());

			TableModel<CellModelKeyValue<RegistroHistoricoEditorVO>> tableModel = 
					new TableModel<CellModelKeyValue<RegistroHistoricoEditorVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
			
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
		pesquisarCurvaABCDistribuidor(dataDe, dataAte, 0L, "", "", null, 0L, null,
				"", "", sortorder, sortname, page, rp);
	}

	@Post
	@Path("/pesquisarRankingSegmentacao")
	public void pesquisarRankingSegmentacao(Long idSegmentacao, String descricaoSegmento, Date dataDe, Date dataAte, 
			String sortorder, String sortname, int page, int rp) {
		
		if (idSegmentacao == null) {			
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de segmento.");
		}
		
		FiltroRankingSegmentoDTO filtro = new FiltroRankingSegmentoDTO(dataDe, dataAte, idSegmentacao, page, rp, sortorder, sortname);
		
		filtro.setDescricaoTipoSegmento(descricaoSegmento);
		
		TotalizadorRankingSegmentoDTO totalizador = this.relatorioVendasService.obterQuantidadeRegistrosRankingSegmento(filtro);
		
		Integer count = totalizador.getQuantidadeRegistros();
		
		if (count == 0) {			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		filtro.setTotalFaturamento(totalizador.getTotalFaturamentoCapa());
		
		this.session.setAttribute(FILTRO_PESQUISA_RANKING_SEGMENTO, filtro);
		
		TableModel<CellModelKeyValue<RegistroRankingSegmentoDTO>> tableModel = 
				this.obterTableModelRankingSegmento(filtro, count);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		retorno.put("tableModel", tableModel);
		retorno.put("totalFaturamentoCapa", totalizador.getTotalFaturamentoCapaFormatado());
		
		this.result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<RegistroRankingSegmentoDTO>> obterTableModelRankingSegmento(FiltroRankingSegmentoDTO filtro, int rows) {

		TableModel<CellModelKeyValue<RegistroRankingSegmentoDTO>> tableModel = 
				new TableModel<CellModelKeyValue<RegistroRankingSegmentoDTO>>();
		
		List<RegistroRankingSegmentoDTO> lista = this.relatorioVendasService.obterRankingSegmento(filtro);

		tableModel.setTotal(rows);
		tableModel.setPage(filtro.getPaginacao() == null ? 1 : filtro.getPaginacao().getPaginaAtual());
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(PaginacaoUtil.paginarEmMemoria(lista, filtro.getPaginacao())));

		return tableModel;
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
			List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {

		consultarCurvaABCDistribuidorProduto(dataDe, dataAte, codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname, page, rp,TipoConsultaCurvaABC.DISTRIBUIDOR);

	}

	private void consultarCurvaABCDistribuidorProduto(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp, TipoConsultaCurvaABC tipoConsulta) throws ParseException, Exception {
		
		this.validarDadosEntradaPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCDistribuidorDTO filtro =
			this.carregarFiltroPesquisaDistribuidor(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
											   	 	codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
											   	 	codigoCota, nomeCota, municipio, sortorder, sortname, page, rp);
		
		filtro.setTipoConsultaCurvaABC(tipoConsulta);

		List<RegistroCurvaABCDistribuidorVO> resultadoCurvaABCDistribuidor = null;
		
		if (tipoConsulta == TipoConsultaCurvaABC.DISTRIBUIDOR) {
			
			resultadoCurvaABCDistribuidor = relatorioVendasService.obterCurvaABCDistribuidor(filtro);
			
		} else {
			
			resultadoCurvaABCDistribuidor = relatorioVendasService.obterCurvaABCProduto(filtro);
		
		}
		
		if (resultadoCurvaABCDistribuidor == null || resultadoCurvaABCDistribuidor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {

			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCDistribuidor));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
			
			ResultadoCurvaABCDistribuidor resultado =
				this.obterTotaisCurvaABCDistribuidor(resultadoCurvaABCDistribuidor);
			
			resultado.setTableModel(tableModel);
			
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();

		}
	}

	private ResultadoCurvaABCDistribuidor obterTotaisCurvaABCDistribuidor(
			List<RegistroCurvaABCDistribuidorVO> listaCurvaABCDistribuidor) {

		ResultadoCurvaABCDistribuidor resultadoCurvaABCDistribuidor = new ResultadoCurvaABCDistribuidor();
		
		BigInteger totalVendaExemplares = BigInteger.ZERO;
		BigDecimal totalFaturamento = BigDecimal.ZERO;
		
		for (RegistroCurvaABCDistribuidorVO registroCurvaABCDistribuidor : listaCurvaABCDistribuidor) {
			
			totalVendaExemplares = totalVendaExemplares.add(registroCurvaABCDistribuidor.getVendaExemplares());
			totalFaturamento = totalFaturamento.add(registroCurvaABCDistribuidor.getFaturamentoCapa());
		}
		
		resultadoCurvaABCDistribuidor.setTotalVendaExemplares(totalVendaExemplares);
		resultadoCurvaABCDistribuidor.setTotalFaturamento(totalFaturamento);
		
		return resultadoCurvaABCDistribuidor;
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
		pesquisarCurvaABCEditor(dataDe, dataAte, 0L, "", "", null, 0L, null,
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
			List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {

		this.validarDadosEntradaPesquisa(dataDe, dataAte);
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO = carregarFiltroPesquisaEditor(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname.replaceAll("Formatado", ""), page, rp);

		List<RegistroCurvaABCEditorVO> resultadoCurvaABCEditor = null;
		
		resultadoCurvaABCEditor = this.relatorioVendasService.obterCurvaABCEditor(filtroCurvaABCEditorDTO);

		if (resultadoCurvaABCEditor == null || resultadoCurvaABCEditor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"Nenhum registro encontrado.");
		} else {

			TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCEditorVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCEditor));
			tableModel.setPage(filtroCurvaABCEditorDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(filtroCurvaABCEditorDTO.getPaginacao().getQtdResultadosTotal());
			
			ResultadoCurvaABCEditor resultado =
				this.obterTotaisCurvaABCEditor(resultadoCurvaABCEditor);
			
			resultado.setTableModel(tableModel);
			
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		}
	}

	private ResultadoCurvaABCEditor obterTotaisCurvaABCEditor(
			List<RegistroCurvaABCEditorVO> listaCurvaABCDistribuidor) {

		ResultadoCurvaABCEditor resultadoCurvaABCDistribuidor = new ResultadoCurvaABCEditor();

		BigInteger totalVendaExemplares = BigInteger.ZERO;
		BigDecimal totalFaturamento = BigDecimal.ZERO;

		for (RegistroCurvaABCEditorVO registroCurvaABCDistribuidor : listaCurvaABCDistribuidor) {

			totalVendaExemplares = totalVendaExemplares.add(registroCurvaABCDistribuidor.getVendaExemplares());
			totalFaturamento = totalFaturamento.add(registroCurvaABCDistribuidor.getFaturamentoCapa());
		}

		resultadoCurvaABCDistribuidor.setTotalVendaExemplares(totalVendaExemplares);
		
		resultadoCurvaABCDistribuidor.setTotalFaturamento(totalFaturamento);

		return resultadoCurvaABCDistribuidor;
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
		pesquisarCurvaABCProduto(dataDe, dataAte, 0L, codigoProduto, nomeProduto, null, 0L, null, "", "", sortorder, sortname, page, rp);
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
			List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {
			validarDadosEntradaPesquisaProduto(dataDe, dataAte, codigoProduto, nomeProduto);
			
			consultarCurvaABCDistribuidorProduto(dataDe, dataAte, codigoFornecedor,
					codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
					codigoCota, nomeCota, municipio, sortorder, sortname, page, rp,TipoConsultaCurvaABC.PRODUTO);
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
			Integer codigoCota, String nomeCota, String sortorder,
			String sortname, int page, int rp)
			throws Exception {
		pesquisarCurvaABCCota(dataDe, dataAte, 0L, "", "", null, 0L, codigoCota, nomeCota, "", sortorder, sortname, page, rp);
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
			List<Long> edicaoProduto, Long codigoEditor, Integer codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {
		
		this.validarDadosEntradaPesquisaCota(dataDe, dataAte, codigoCota, nomeCota);

		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO = carregarFiltroPesquisaCota(
				sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname.replace("Formatado", ""), page, rp);

		List<RegistroCurvaABCCotaDTO> resultadoCurvaABCCota = null;
		
		resultadoCurvaABCCota = this.relatorioVendasService.obterCurvaABCCota(filtroCurvaABCCotaDTO);

		if (resultadoCurvaABCCota == null
				|| resultadoCurvaABCCota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = relatorioVendasService.obterQtdeRegistrosCurvaABCCota(filtroCurvaABCCotaDTO);
			
            List<RegistroCurvaABCCotaDTO> resultadoPaginado = PaginacaoUtil.paginarEmMemoria(resultadoCurvaABCCota, filtroCurvaABCCotaDTO.getPaginacao());
			
			TableModel<CellModelKeyValue<RegistroCurvaABCCotaDTO>> tableModel = 
					new TableModel<CellModelKeyValue<RegistroCurvaABCCotaDTO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
			tableModel.setPage(filtroCurvaABCCotaDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
			
			ResultadoCurvaABCCotaDTO resultado = this.obterTotaisCurvaABCCota(resultadoCurvaABCCota);
			resultado.setTableModel(tableModel);
			
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
			
		}
		
	}
	
	private ResultadoCurvaABCCotaDTO obterTotaisCurvaABCCota(
			List<RegistroCurvaABCCotaDTO> listaCurvaABCCota) {

		ResultadoCurvaABCCotaDTO resultadoCurvaABCDistribuidor = new ResultadoCurvaABCCotaDTO();

		BigInteger totalVendaExemplares = BigInteger.ZERO;
		BigDecimal totalFaturamento = BigDecimal.ZERO;

		for (RegistroCurvaABCCotaDTO registroCurvaABCDistribuidor : listaCurvaABCCota) {

			totalVendaExemplares =
				totalVendaExemplares.add(registroCurvaABCDistribuidor.getVendaExemplares());
			
			totalFaturamento =
				totalFaturamento.add(registroCurvaABCDistribuidor.getFaturamento());
		}

		resultadoCurvaABCDistribuidor.setTotalVendaExemplares(totalVendaExemplares);
		resultadoCurvaABCDistribuidor.setTotalFaturamento(totalFaturamento);

		return resultadoCurvaABCDistribuidor;
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
			String codigoProduto, String nomeProduto, List<Long> edicaoProduto, Long codigoEditor,
			Integer codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {

		FiltroCurvaABCEditorDTO filtro = new FiltroCurvaABCEditorDTO(dataDe, dataAte, (codigoFornecedor == null ? "" : codigoFornecedor.toString()),
				codigoProduto, nomeProduto, edicaoProduto, (codigoEditor == null ? "" : codigoEditor.toString()),
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoEditorPesquisa(filtro, sortorder, sortname, page, rp);

		FiltroCurvaABCEditorDTO filtroSessao = (FiltroCurvaABCEditorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_CURVA_ABC_EDITOR_SESSION_ATTRIBUTE, filtro);
		
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
			String codigoProduto, String nomeProduto, List<Long> edicaoProduto, Long codigoEditor,
			Integer codigoCota, String nomeCota, String municipio,
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
			String codigoProduto, String nomeProduto, List<Long> edicaoProduto, Long codigoEditor,
			Integer codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {
		
		if(nomeCota!= null){
			nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		}
		
		FiltroCurvaABCCotaDTO filtro = new FiltroCurvaABCCotaDTO(dataDe, dataAte, (codigoFornecedor == null ? "" : codigoFornecedor.toString()),
				codigoProduto, nomeProduto, edicaoProduto, (codigoEditor == null ? "" : codigoEditor.toString()),
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoCotaPesquisa(filtro, sortorder, sortname, page, rp);

		FiltroCurvaABCCotaDTO filtroSessao = (FiltroCurvaABCCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_CURVA_ABC_COTA_SESSION_ATTRIBUTE, filtro);

		return filtro;
	}	

	/**
	 * Carrega filtro da pesquisa de histórico de editor
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 * @param dataDe
	 * @param dataAte
	 * @param codigoEditor
	 * @return
	 */
	private FiltroPesquisarHistoricoEditorDTO carregarFiltroHistoricoEditor(String sortorder, 
			String sortname, int page, int rp, Date dataDe, Date dataAte, String codigoEditor) {

		FiltroPesquisarHistoricoEditorDTO filtro = new FiltroPesquisarHistoricoEditorDTO(dataDe, dataAte, codigoEditor);
		
		this.configurarPaginacaoPesquisarHistoricoEditor(filtro, sortorder, sortname, page, rp);

		FiltroPesquisarHistoricoEditorDTO filtroSessao = 
				(FiltroPesquisarHistoricoEditorDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_HISTORICO_EDITOR_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(page);
		}

		session.setAttribute(FILTRO_PESQUISA_CURVA_ABC_HISTORICO_EDITOR_SESSION_ATTRIBUTE, filtro);
		
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
	private void configurarPaginacaoEditorPesquisa(FiltroCurvaABCEditorDTO filtro, String sortorder, String sortname, int page, int rp) {
		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroCurvaABCEditorDTO.ColunaOrdenacaoCurvaABCEditor.values(), sortname));
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
	private void configurarPaginacaoCotaPesquisa(FiltroCurvaABCCotaDTO filtro, String sortorder, String sortname, int page, int rp) {
		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(
					Util.getEnumByStringValue(
							FiltroCurvaABCCotaDTO.ColunaOrdenacaoCurvaABCCota.values(), sortname));
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
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroCurvaABCDistribuidorDTO.ColunaOrdenacaoCurvaABCDistribuidor.values(), sortname));
		}
	}

	/**
	 * Configuração a paginação da pesquisa de histórico por editor
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisarHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro, String sortorder, String sortname, int page, int rp) {
		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroPesquisarHistoricoEditorDTO.ColunaOrdenacaoPesquisarHistoricoEditorDTO.values(), sortname));
		}
	}
}
