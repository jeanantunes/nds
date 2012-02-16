package br.com.abril.nds.controllers.estoque;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	public DiferencaEstoqueController(Result result) {
		
		this.result = result;
	}
	
	@Get
	public void lancamento() {
		
	}
	
	@Get
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos() {
	
		result.forwardTo(DiferencaEstoqueController.class).lancamento();
	}
	
}
