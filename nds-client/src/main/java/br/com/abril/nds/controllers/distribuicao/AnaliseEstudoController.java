package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.AnaliseEstudoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/analiseEstudo")
public class AnaliseEstudoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private TipoClassificacaoProdutoService classificacao;
	
	@Autowired
	private AnaliseEstudoService analiseEstudoService;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroEstudo";
	
	public AnaliseEstudoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS)
	public void index(){
		this.carregarComboClassificacao();
	}
	
	
	private void carregarComboClassificacao(){
		List<TipoClassificacaoProduto> classificacoes = classificacao.obterTodos();
		result.include("listaClassificacao", classificacoes);
		}
	
	
	@Post
	@Path("/buscarEstudos")
	public void buscarEstudos (FiltroAnaliseEstudoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<AnaliseEstudoDTO>> tableModel = efetuarConsultaEstudos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<AnaliseEstudoDTO>> efetuarConsultaEstudos(FiltroAnaliseEstudoDTO filtro) {

		List<AnaliseEstudoDTO> listaEstudos = analiseEstudoService.buscarEstudos(filtro);

		if (listaEstudos == null || listaEstudos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<AnaliseEstudoDTO>> tableModel = new TableModel<CellModelKeyValue<AnaliseEstudoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaEstudos));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void tratarFiltro(FiltroAnaliseEstudoDTO filtroAtual) {

		FiltroAnaliseEstudoDTO filtroSession = (FiltroAnaliseEstudoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

}
