package br.com.abril.nds.controllers.administracao;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/tipoMovimento")
public class TipoMovimentoController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroTipoMovimento";
	
	@Autowired
	private HttpSession session;
		
	@Autowired
	private Result result;
	
	public void tipoMovimento() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);	
		
		result.forwardTo(TipoMovimentoController.class).tipoMovimento();
	}
	
}
