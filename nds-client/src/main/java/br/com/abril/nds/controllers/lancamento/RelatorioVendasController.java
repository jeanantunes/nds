package br.com.abril.nds.controllers.lancamento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.ColunaOrdenacaoCurvaABCDistribuidor;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
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
	private DistribuidorService distribuidorService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EditorService editorService;

	@Autowired
	private CotaService cotaService;

	private static final String QTD_REGISTROS_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaCurvaAbcDistribuidor";
	private static final String FILTRO_PESQUISA_CURVA_ABC_DISTRIBUIDOR_SESSION_ATTRIBUTE = "filtroPesquisaCurvaAbcDistribuidor";

	public RelatorioVendasController(Result result) {
		this.result = result;
	}

	@Get
	@Path("/")
	public void index() {
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
		pesquisarCurvaABCDistribuidor(dataDe, dataAte, "", "", "", "", "", "",
				"", "", sortorder, sortname, page, rp);
	}

	@Post
	@Path("/pesquisarCurvaABCDistribuidorAvancada")
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte,
			String codigoFornecedor, String codigoProduto, String nomeProduto,
			String edicaoProduto, String codigoEditor, String codigoCota,
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

			result.use(Results.json()).withoutRoot().from(tableModel)
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

	private FiltroCurvaABCDistribuidorDTO carregarFiltroPesquisa(Date dataDe, Date dataAte, String codigoFornecedor, 
			String codigoProduto, String nomeProduto, String edicaoProduto, String codigoEditor,
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

}
