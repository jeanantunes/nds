package br.com.abril.nds.controllers.distribuicao;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Path("/distribuicao/excecaoSegmentoParciais")
@Resource()
public class ExcecaoSegmentoParciaisController extends BaseController {

	@Rules(Permissao.ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS)
	public void index(){
		System.out.println("tamo junto!");
	}
	
	@Post("/teste")
	public void teste(String text){
		System.out.println("rodou o teste " + text);
	}
}
