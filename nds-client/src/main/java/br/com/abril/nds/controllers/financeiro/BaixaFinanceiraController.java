package br.com.abril.nds.controllers.financeiro;

import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;

@Resource
@Path("/financeiro")
public class BaixaFinanceiraController {

	private Result result;
	
	private Localization localization;
	
	private HttpSession httpSession;
	
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
	}
	
	@Get
	public void baixa() {
		
	}
	
}
