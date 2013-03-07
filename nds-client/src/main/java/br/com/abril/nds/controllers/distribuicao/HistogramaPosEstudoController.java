package br.com.abril.nds.controllers.distribuicao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Path("/distribuicao/histogramaPosEstudo")
@Resource
public class HistogramaPosEstudoController extends BaseController{
	
	@Autowired
	private Result result;
	
	@Path("/")
	public void index(){
	}
	
	@Post
	public void carregarGridHistogramaPosEstudo(){
		
	}
//	
//	@Get
//	@Path("/histogramaPosEstudo")
//	public void histogramaPosEstudo(){
//		result.forwardTo(HistogramaPosEstudoController.class).index();
//	}
//	
//	@Post
//	@Path("/carregarDadosFieldset")
//	public void carregarDadosFieldset(HistogramaPosEstudoDadoInicioDTO selecionado ){
//		result.use(Results.json()).withoutRoot().from(selecionado)
//		.recursive().serialize();
//		
////		result.redirectTo("distribuicao/histogramaPosEstudoindex.jsp");
//	}
	
}
