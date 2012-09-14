package br.com.abril.nds.controllers.nfe;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path(value="nfe/impressaoNFE")
public class ImpressaoNFEController {
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
	public void index(){
		
	}

}
