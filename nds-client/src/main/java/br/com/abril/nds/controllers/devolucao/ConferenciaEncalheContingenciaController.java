package br.com.abril.nds.controllers.devolucao;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path(value="/devolucao/conferenciaEncalheContingencia")
public class ConferenciaEncalheContingenciaController {

	private Result result;
	
	public ConferenciaEncalheContingenciaController(Result result){
		
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		
	}
}