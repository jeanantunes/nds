package br.com.abril.nds.controllers.devolucao;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

/**
 * 
 * Classe responsável pelo controle das ações referentes a
 * tela de venda de encalhe.
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/devolucao/vendaEncalhe")
public class VendaEncalheController {
	
	@Path("/")
	public void index() {}
}
