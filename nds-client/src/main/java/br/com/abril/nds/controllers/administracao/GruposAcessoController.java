package br.com.abril.nds.controllers.administracao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.PermissaoService;
import br.com.abril.nds.util.CellModelKeyValue;
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

/**
 * @author infoA2
 * Controller dos grupos de acesso
 */
@Resource
@Path("/administracao/gruposAcesso")
public class GruposAcessoController {

	@Autowired
	private Result result;

	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private HttpSession session;
	
	public GruposAcessoController() {
		super();
	}
	
	private static final String FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaPermissao";
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO)
	public void index() {
	}

	/**
	 * Retorna a lista de regras do sistema
	 * @return List
	 */
	@Get
	@Path("/pesquisarRegras")
	public void pesquisarRegras(FiltroConsultaPermissaoDTO filtro, int rp, int page, String sortname, String sortorder) {
		filtro = carregarFiltroConsultaRegras(filtro, rp, page, sortname, sortorder);
		List<ResultadoPermissaoVO> permissoes = permissaoService.buscar(filtro);

		if (permissoes == null || permissoes.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		int qtdeTotalRegistros = permissoes.size();
		
		List<ResultadoPermissaoVO> resultadoPaginado = PaginacaoUtil.paginarEOrdenarEmMemoria(permissoes, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
		TableModel<CellModelKeyValue<ResultadoPermissaoVO>> tableModel = new TableModel<CellModelKeyValue<ResultadoPermissaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(resultadoPaginado));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	/**
	 * @param filtro
	 * @param rp
	 * @param page
	 * @param sortname
	 * @param sortorder
	 * @return FiltroConsultaPermissaoDTO
	 */
	private FiltroConsultaPermissaoDTO carregarFiltroConsultaRegras(FiltroConsultaPermissaoDTO filtro, int rp, int page, String sortname, String sortorder) {

		if (filtro != null) {
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			filtro.setPaginacao(paginacao);
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroConsultaPermissaoDTO.ColunaOrdenacao.values(), sortname));
		}

		FiltroConsultaPermissaoDTO filtroSessao = (FiltroConsultaPermissaoDTO) this.session.getAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_PESQUISA_CONSULTA_PERMISSAO_SESSION_ATTRIBUTE, filtro);

		return filtro;
		
	}
	
}
