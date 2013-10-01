package br.com.abril.nds.controllers.financeiro;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/financeiro/movimentoFinanceiroCota")
@Rules(Permissao.ROLE_MOVIMENTO_FINANCEIRO_COTA)
public class MovimentoFinanceiroCotaController extends BaseController{
	
	@Path("/")
	public void index(){
		
	}

}
