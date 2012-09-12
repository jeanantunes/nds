package br.com.abril.nds.controllers.estoque;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("estoque/visaoEstoque")
public class VisaoEstoqueController {

	@Path("/")
	@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
	public void index()
	{
		
	}
}
