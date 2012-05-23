package br.com.abril.nds.controllers.cadastro;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/cadastro/edicao")
public class ProdutoEdicaoController {

	
	@Get
	@Path("/")
	public void index() { }
	
}
