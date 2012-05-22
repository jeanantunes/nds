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
import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABC;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.ColunaOrdenacaoCurvaABCDistribuidor;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoService;
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
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EditorService editorService;

	private static final String QTD_REGISTROS_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaCurvaAbcDistribuidor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "filtroPesquisaCurvaAbcDistribuidor";
	private static final String RESULTADO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "resultadoPesquisaCurvaAbcDistribuidor";

	private static final int DISTRIBUIDOR = 1;
	private static final int EDITOR       = 2;
	private static final int PRODUTO      = 3;
	private static final int COTA         = 4;

	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	@Path("/")
	public void index() {
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO));
		result.include("editores", editorService.obterEditores());
		//result.include("municipios", municipioService.obterMunicipiosCotas());
	}
	
	public RelatorioVendasController(Result result) {
		this.result = result;
	}

	@Get
	public void exportar(FileType fileType, int tipoRelatorio) throws IOException {
		
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de arquivo não encontrado!");
		}
		
		switch (tipoRelatorio) {
		case DISTRIBUIDOR:

			FiltroCurvaABCDistribuidorDTO filtro = (FiltroCurvaABCDistribuidorDTO) this.session.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);
			
			ResultadoCurvaABC resultado = (ResultadoCurvaABC) this.session.getAttribute(RESULTADO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);
			
			List<RegistroCurvaABCDistribuidorVO> lista = (List<RegistroCurvaABCDistribuidorVO>) resultado.getTableModel().getRows();
			FileExporter.to("relatorio-vendas-curva-abc-distribuidor", fileType).inHTTPResponse(getNDSFileHeader(), filtro, resultado, 
						lista, RegistroCurvaABCDistribuidorVO.class, this.httpServletResponse);

			break;
		case EDITOR:
		case PRODUTO:
		case COTA:
		default:
			break;
		}

	}
	
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

	@Post
	@Path("/pesquisarCurvaABCDistribuidor")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte,
			String sortorder, String sortname, int page, int rp)
			throws Exception {
		pesquisarCurvaABCDistribuidor(dataDe, dataAte, 0L, "", "", "", 0L, "",
				"", "", sortorder, sortname, page, rp);
	}

	@Post
	@Path("/pesquisarCurvaABCDistribuidorAvancada")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte,
			Long codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, Long codigoEditor, String codigoCota,
			String nomeCota, String municipio, String sortorder,
			String sortname, int page, int rp) throws Exception {

		this.validarDadosEntradaPesquisa(dataDe, dataAte);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constantes.DATE_PATTERN_PT_BR);

		FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO = carregarFiltroPesquisa(sdf.parse(dataDe), sdf.parse(dataAte), codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio, sortorder, sortname, page, rp);

		List<RegistroCurvaABCDistribuidorVO> resultadoCurvaABCDistribuidor = null;
		try {
			resultadoCurvaABCDistribuidor = distribuidorService
					.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		} catch (Exception e) {

			if (e instanceof ValidacaoException) {
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Erro ao pesquisar produto: " + e.getMessage());
			}
		}

		if (resultadoCurvaABCDistribuidor == null
				|| resultadoCurvaABCDistribuidor.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		} else {

			int qtdeTotalRegistros = resultadoCurvaABCDistribuidor.size();

			PaginacaoUtil
					.armazenarQtdRegistrosPesquisa(
							this.session,
							QTD_REGISTROS_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE,
							qtdeTotalRegistros);

			TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>> tableModel = new TableModel<CellModelKeyValue<RegistroCurvaABCDistribuidorVO>>();

			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoCurvaABCDistribuidor));
			tableModel.setPage(filtroCurvaABCDistribuidorDTO.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);

			ResultadoCurvaABC resultado = distribuidorService.obterCurvaABCDistribuidorTotal(filtroCurvaABCDistribuidorDTO);
			resultado.setTableModel(tableModel);
			
			session.setAttribute(RESULTADO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE, resultado);
			
			result.use(Results.json()).withoutRoot().from(resultado)
					.recursive().serialize();

		}

	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto)
			throws Exception {
	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto,
			Long edicao, String dataLancamento) throws Exception {
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto)
			throws Exception {
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto,
			Long edicao, String dataLancamento) throws Exception {
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto)
			throws Exception {
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto,
			Long edicao, String dataLancamento) throws Exception {
	}

	@Post
	public void pesquisarProdutosPorEditor(String codigo, String dataDe,
			String dataAte) throws Exception {
	}

	private FiltroCurvaABCDistribuidorDTO carregarFiltroPesquisa(Date dataDe, Date dataAte, Long codigoFornecedor, 
			String codigoProduto, String nomeProduto, String edicaoProduto, Long codigoEditor,
			String codigoCota, String nomeCota, String municipio,
			String sortorder, String sortname, int page, int rp) {

		FiltroCurvaABCDistribuidorDTO filtro = new FiltroCurvaABCDistribuidorDTO(dataDe, dataAte, codigoFornecedor,
				codigoProduto, nomeProduto, edicaoProduto, codigoEditor,
				codigoCota, nomeCota, municipio);
		
		this.configurarPaginacaoPesquisa(filtro, sortorder, sortname,
				page, rp);

		FiltroCurvaABCDistribuidorDTO filtroSessao = (FiltroCurvaABCDistribuidorDTO) this.session
				.getAttribute(FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE);

		PaginacaoUtil.calcularPaginaAtual(this.session,
				QTD_REGISTROS_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE,
				FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE,
				filtro, filtroSessao);

		return filtro;
	}

	private void configurarPaginacaoPesquisa(FiltroCurvaABCDistribuidorDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(
					ColunaOrdenacaoCurvaABCDistribuidor.values(), sortname));
		}
	}

	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
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
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
}
