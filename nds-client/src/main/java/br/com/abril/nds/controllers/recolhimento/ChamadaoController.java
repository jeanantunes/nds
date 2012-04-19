package br.com.abril.nds.controllers.recolhimento;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/recolhimento/chamadao")
public class ChamadaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Path("/")
	public void index() {
		
	}
	
}
