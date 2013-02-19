package br.com.abril.nds.controllers.distribuicao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/historicoVenda")
public class HistoricoVendaController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroHistoricoVendaDTO";
	
	private static final ValidacaoVO VALIDACAO_VO_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/index")
	@Rules(Permissao.ROLE_DISTRIBUICAO_HISTORICO_VENDA)
	public void historicoVenda(){
		
	}
	
	@Post
	public void pesquisaProduto(FiltroHistoricoVendaDTO filtro){
		validarEntradaFiltroProduto(filtro);
		
		List<ProdutoEdicaoDTO> listEdicoesProdutoDto = this.produtoEdicaoService.obterEdicoesProduto(filtro);
		
		TableModel<CellModelKeyValue<ProdutoEdicaoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoEdicaoDTO>>();
		
		this.configurarTableModelSemPaginacao(listEdicoesProdutoDto, tableModel);
		//result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private void validarEntradaFiltroProduto(FiltroHistoricoVendaDTO filtro) {
		if((filtro.getProdutoDto().getCodigoProduto() == null || filtro.getProdutoDto().getCodigoProduto().trim().isEmpty()) && 
				(filtro.getProdutoDto().getNomeProduto() == null || filtro.getProdutoDto().getNomeProduto().trim().isEmpty()) &&
				(filtro.getNumeroEdicao() == null || filtro.getNumeroEdicao().equals(0)) &&
				(filtro.getTipoClassificacaoProdutoId() == null || filtro.getTipoClassificacaoProdutoId().equals(0)))
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe algum campo para filtrar a pesquisa por produto.");		
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void validarLista(List list, String mensagem){
		if (list == null || list.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, mensagem);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void guardarFiltroNaSession(FiltroHistoricoVendaDTO filtro) {
		
		FiltroHistoricoVendaDTO filtroSession = (FiltroHistoricoVendaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
