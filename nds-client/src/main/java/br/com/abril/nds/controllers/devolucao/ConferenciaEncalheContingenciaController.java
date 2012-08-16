package br.com.abril.nds.controllers.devolucao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path(value="/devolucao/conferenciaEncalheContingencia")
public class ConferenciaEncalheContingenciaController {

	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_CONTINGENCIA)
	public void index(){
		
	}
}