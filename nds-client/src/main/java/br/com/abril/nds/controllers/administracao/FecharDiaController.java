package br.com.abril.nds.controllers.administracao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/administracao/fecharDia")
public class FecharDiaController {
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
	public void index(){
		
	}

}
