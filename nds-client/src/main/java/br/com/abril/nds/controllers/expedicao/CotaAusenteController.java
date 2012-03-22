package br.com.abril.nds.controllers.expedicao;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class CotaAusenteController {

	protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
	
	private static final Logger LOG = LoggerFactory
			.getLogger(CotaAusenteController.class);
	
	private final Result result;
	private final HttpSession session;
	
	public CotaAusenteController(Result result, HttpSession session) {
		this.result=result;
		this.session = session;
	}
	
	public void cotaAusente() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		result.forwardTo(CotaAusenteController.class).cotaAusente();
	}
}
