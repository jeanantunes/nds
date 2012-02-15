package br.com.abril.nds.controllers.lancamento;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("lancamento")
public class FuroProdutoController {

	private Result result;
	
	public FuroProdutoController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/furoProduto")
	public void index(){
		
	}
	
	@Post
	@Path("/furoProduto/pesquisar")
	public void pesquisar(String codigo, String produto, String edicao, String dataLancamento){
		System.out.println("Código: " + codigo);
		System.out.println("Produto: " + produto);
		System.out.println("Edicao: " + edicao);
		System.out.println("dataLancamento: " + dataLancamento);
		result.use(Results.json()).from(codigo + ", " + produto + ", " + edicao + ", " + dataLancamento, "result").serialize();
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	@Post
	@Path("/furoProduto/confirmarFuro")
	public void confirmarFuro(){
		
	}
}
