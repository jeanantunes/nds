package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/mixCotaProduto")
public class MixCotaProdutoController extends BaseController {

	private static final String FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE = "filtroMixPorProduto";
	private static final String FILTRO_MIX_COTA_SESSION_ATTRIBUTE = "filtroMixPorCota";

	@Autowired
	private Result result;

	@Autowired
	TipoProdutoService tipoProdutoService;

	@Autowired
	ProdutoService produtoService;

	@Autowired
	FixacaoReparteService fixacaoReparteService;

	@Autowired
	MixCotaProdutoService mixCotaProdutoService;
	
	@Autowired
	PdvService pdvService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RepartePdvService repartePdvService;

	FiltroConsultaMixPorCotaDTO filtroMixPorCota;

	FiltroConsultaFixacaoProdutoDTO filtroMixPorProduto;

	FixacaoReparteDTO fixacaoReparteDTO;

	@Autowired
	HttpSession session;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private HttpServletResponse httpResponse;

	@Rules(Permissao.ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO)
	@Path("/")
	public void index() {
		result.include("classificacao",
				fixacaoReparteService.obterClassificacoesProduto());
	}

	@Post
	@Path("/pesquisarPorProduto")
	public void pesquisarPorProduto(FiltroConsultaMixPorProdutoDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		if (session.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE, filtro);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		tratarFiltroPorProduto(filtro);

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorProduto(filtro);

		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Não foram encontrados resultados para a pesquisa.");
		}

		TableModel<CellModelKeyValue<MixProdutoDTO>> tableModelProduto = montarTableModelProduto(filtro);

		result.use(Results.json()).withoutRoot().from(tableModelProduto)
				.recursive().serialize();
	}

	@Post
	@Path("/pesquisarPorCota")
	public void pesquisarPorCota(FiltroConsultaMixPorCotaDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		if (session.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtro);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		tratarFiltroPorCota(filtro);

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorCota(filtro);

		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Não foram encontrados resultados para a pesquisa.");
		}

		TableModel<CellModelKeyValue<MixCotaDTO>> tableModelCota = montarTableModelCota(filtro);

		result.use(Results.json()).withoutRoot().from(tableModelCota)
				.recursive().serialize();
	}

	@Post
	@Path("/removerMixCotaProduto")
	public void removerMixCotaProduto(FiltroConsultaMixPorCotaDTO filtro) {
		mixCotaProdutoService.removerMixCotaProduto(filtro);
		throw new ValidacaoException(TipoMensagem.SUCCESS,
				"Operação realizada com sucesso!");
	}

	private TableModel<CellModelKeyValue<MixCotaDTO>> montarTableModelCota(
			FiltroConsultaMixPorCotaDTO filtro) {

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorCota(filtro);

		TableModel<CellModelKeyValue<MixCotaDTO>> tableModel = new TableModel<CellModelKeyValue<MixCotaDTO>>();

		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	private TableModel<CellModelKeyValue<MixProdutoDTO>> montarTableModelProduto(
			FiltroConsultaMixPorProdutoDTO filtro) {

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorProduto(filtro);

		TableModel<CellModelKeyValue<MixProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<MixProdutoDTO>>();

		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		return tableModel;
	}

	@Post
	@Path("/carregarGridHistoricoCota")
	public void carregarGridHistoricoCota(FiltroConsultaFixacaoCotaDTO filtro,
			String codigoProduto, String sortorder, String sortname, int page,
			int rp) {
		if (session.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE) == null) {
			this.session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtro);
		}
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		List<FixacaoReparteDTO> resultadoPesquisa = fixacaoReparteService
				.obterHistoricoLancamentoPorCota(filtro);

		if (resultadoPesquisa.size() > 0) {
			FixacaoReparteDTO fixacaoReparteDTO = resultadoPesquisa.get(0);
			this.result.include("ultimaEdicao", fixacaoReparteDTO);
		}

		TableModel<CellModelKeyValue<FixacaoReparteDTO>> tableModel = new TableModel<CellModelKeyValue<FixacaoReparteDTO>>();
		tableModel.setRows(CellModelKeyValue
				.toCellModelKeyValue(resultadoPesquisa));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(resultadoPesquisa.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive()
				.serialize();
	}

	

	@Post
	@Path("/editarRepartePorPdv")
	public void editarRepartePDV(FiltroConsultaMixPorCotaDTO filtro,
			String sortorder, String sortname, int page, int rp) {

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		MixCotaProdutoDTO resultadoPesquisa = mixCotaProdutoService.obterPorId(filtro.getId());
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa)
				.recursive().serialize();
	}
	
	@Post
	@Path("/carregarGridPdv")
	public void carregarGridPdv(FiltroConsultaMixPorCotaDTO filtro , String sortorder, String sortname, int page, int rp){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		List<PdvDTO> listaPDVDTO = mixCotaProdutoService.obterListaPdvPorMix(filtro.getId());
		Produto produto = produtoService.obterProdutoPorID(filtro.getProdutoId());
		
		for (PdvDTO pdvDTO : listaPDVDTO) {
			RepartePDV reparteEncontrado = repartePdvService.obterRepartePorPdvMix(filtro.getId(), produto.getId(), pdvDTO.getId());
			if(reparteEncontrado !=null)
				pdvDTO.setReparte( reparteEncontrado.getReparte());
			}
		
 		TableModel<CellModelKeyValue<PdvDTO>> tableModel = new TableModel<CellModelKeyValue<PdvDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPDVDTO));
		tableModel.setPage(filtro.getPaginacao().getPosicaoInicial());
		tableModel.setTotal(listaPDVDTO.size());
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	@Path("/salvarGridPdvReparte")
	public void salvarGridPdvReparte(List<RepartePDVDTO> listPDV, String codProduto, String codCota, Long idMix){
		repartePdvService.salvarRepartesPDVMix(listPDV,codProduto, codCota, idMix);
		throw new ValidacaoException(TipoMensagem.SUCCESS,"Operação realizada com sucesso!");
	}
	
	@Post
	@Path("/excluirTodos")
	public void excluirTodos(){
		mixCotaProdutoService.excluirTodos();
		throw new ValidacaoException(TipoMensagem.SUCCESS,
				"Operação realizada com sucesso!");
	}
	
	
	@Post
	@Path("/adicionarMixProduto")
	public void adicionarMixProduto(List<MixCotaProdutoDTO>listaNovosMixProduto,String produtoId ){
		
		mixCotaProdutoService.adicionarListaMixPorProduto(listaNovosMixProduto,produtoId);
		throw new ValidacaoException(TipoMensagem.SUCCESS,
				"Operação realizada com sucesso!");
	}
	
	@Post
	@Path("/adicionarMixCota")
	public void adicionarMixCota(List<MixCotaProdutoDTO>listaNovosMixCota, Integer cotaId){
		mixCotaProdutoService.adicionarListaMixPorCota(listaNovosMixCota,cotaId);
		throw new ValidacaoException(TipoMensagem.SUCCESS,
				"Operação realizada com sucesso!");
	}

	
	@Get
	public void exportarGridCota(FileType fileType, String tipoExportacao)
			throws IOException {

		FiltroConsultaMixPorCotaDTO filtroPorCota = (FiltroConsultaMixPorCotaDTO) session
				.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE);

		List<MixCotaDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorCota(filtroPorCota);
		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A última pesquisa realizada não obteve resultado.");
		} else {

			FileExporter.to("mix", fileType).inHTTPResponse(
					this.getNDSFileHeader(), filtroPorCota, null,
					resultadoPesquisa, MixCotaDTO.class, this.httpResponse);
		}

		result.nothing();
	}

	@Get
	public void exportarGridProduto(FileType fileType, String tipoExportacao)
			throws IOException {

		FiltroConsultaMixPorProdutoDTO filtroPorProduto = (FiltroConsultaMixPorProdutoDTO) session
				.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE);

		List<MixProdutoDTO> resultadoPesquisa = mixCotaProdutoService
				.pesquisarPorProduto(filtroPorProduto);
		if (resultadoPesquisa.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A última pesquisa realizada não obteve resultado.");
		} else {

			FileExporter.to("mix", fileType).inHTTPResponse(
					this.getNDSFileHeader(), filtroPorProduto, null,
					resultadoPesquisa, MixProdutoDTO.class, this.httpResponse);
		}

		result.nothing();
	}

	private void tratarFiltroPorCota(FiltroConsultaMixPorCotaDTO filtroAtual) {

		FiltroConsultaMixPorCotaDTO filtroSession = (FiltroConsultaMixPorCotaDTO) session
				.getAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_MIX_COTA_SESSION_ATTRIBUTE, filtroAtual);
	}

	private void tratarFiltroPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroAtual) {

		FiltroConsultaMixPorProdutoDTO filtroSession = (FiltroConsultaMixPorProdutoDTO) session
				.getAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_MIX_PRODUTO_SESSION_ATTRIBUTE, filtroAtual);
	}

	private boolean isRangeRepartesValido(FixacaoReparteDTO fixacaoReparteDTO) {
		boolean rangeEdicoesOK = (fixacaoReparteDTO.getEdicaoFinal() >= fixacaoReparteDTO
				.getEdicaoInicial());
		return rangeEdicoesOK;
	}

}
