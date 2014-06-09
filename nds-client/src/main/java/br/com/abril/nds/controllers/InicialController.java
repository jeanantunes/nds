package br.com.abril.nds.controllers;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/inicial")
public class InicialController {

	/**
	 * 
	 */
	@Get
	@Path("/")
	public void index() {
	}

}
