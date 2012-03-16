package br.com.abril.nds.controllers.financeiro;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/impressaoBoletos")
public class ImpressaoBoletosController {

	private Result result;
	
	public ImpressaoBoletosController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){}
	
	@Post
	public void gerarDivida(){
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
}
