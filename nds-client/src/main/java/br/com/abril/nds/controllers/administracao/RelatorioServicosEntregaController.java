package br.com.abril.nds.controllers.administracao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/administracao/relatorioServicoEntrega")
public class RelatorioServicosEntregaController {
	
	private Result result;

	public RelatorioServicosEntregaController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA)
	public void index(){
		
		
	}
	
	
}
