package br.com.abril.nds.controllers.devolucao;

import br.com.abril.nds.controllers.BaseController;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path(value="/devolucao/conferenciaEncalheContingencia")
public class ConferenciaEncalheContingenciaController extends BaseController {

	@Path("/")
	public void index(){
		
	}
}