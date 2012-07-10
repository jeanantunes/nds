package br.com.abril.nds.server.controller;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/painelOperacional")
public class PainelOperacionalController {

	private Result result;
	
	public PainelOperacionalController(Result result){
		
		this.result = result;
	}
	
	@Path("/")
	public void index(){
		
	}
	
	@Post
	public void atualizarPainel(){
		
	}
}