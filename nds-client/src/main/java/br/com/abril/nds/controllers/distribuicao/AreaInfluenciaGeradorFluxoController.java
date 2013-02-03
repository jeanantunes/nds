package br.com.abril.nds.controllers.distribuicao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Resource;

@Resource
public class AreaInfluenciaGeradorFluxoController extends BaseController {

	@Rules(Permissao.ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO)
	public void index(){
		System.out.println("To na minha controller truta xD");
	}
}
