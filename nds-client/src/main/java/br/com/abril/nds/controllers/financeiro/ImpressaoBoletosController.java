package br.com.abril.nds.controllers.financeiro;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.GerarCobrancaService;
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
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	public ImpressaoBoletosController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){}
	
	@Post
	public void gerarDivida(){
		//trecho que simula possível demora no processamento dessa rotina
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.gerarCobrancaService.gerarCobranca();
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
}
