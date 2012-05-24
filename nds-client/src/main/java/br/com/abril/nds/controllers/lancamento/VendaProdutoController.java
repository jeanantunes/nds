package br.com.abril.nds.controllers.lancamento;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/lancamento/vendaProduto")
public class VendaProdutoController {
	
	@SuppressWarnings("unused")
	private Result result;
	
	public VendaProdutoController(Result result) {
		this.result = result;
	}
	
	
	@Path("/")
	public void index(){
		
	}

}
