package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao/analiseEstudo")
public class AnaliseEstudoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private TipoClassificacaoProdutoService classificacao;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
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
	
//	private void carregarComboSegmento() {
//		List<TipoSegmentoProduto> ListaSegmentos = ajusteService.buscarTodosSegmentos();
//		result.include("listaSegmentos", ListaSegmentos);
//	}
//	
//	private void carregarComboMotivoStatusCota() {
//		
//		List<ItemDTO<MotivoAlteracaoSituacao, String>> listaMotivosStatusCota =
//			new ArrayList<ItemDTO<MotivoAlteracaoSituacao, String>>();
//		
//		for (MotivoAlteracaoSituacao motivoAlteracaoSituacao : MotivoAlteracaoSituacao.values()) {
//			
//			listaMotivosStatusCota.add(new ItemDTO<MotivoAlteracaoSituacao, String>(
//					motivoAlteracaoSituacao, motivoAlteracaoSituacao.toString())
//			);
//		}
//		result.include("listaMotivosStatusCota", listaMotivosStatusCota);
//	}
	
}
