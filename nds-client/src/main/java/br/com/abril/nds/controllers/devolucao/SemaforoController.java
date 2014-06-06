package br.com.abril.nds.controllers.devolucao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/estoque/semaforo")
@Rules(Permissao.ROLE_RECOLHIMENTO_SEMAFORO)
public class SemaforoController extends BaseController {

	
	
}
