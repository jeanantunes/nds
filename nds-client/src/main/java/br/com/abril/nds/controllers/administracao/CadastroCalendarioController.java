package br.com.abril.nds.controllers.administracao;

import java.util.Date;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/administracao/cadastroCalendario")
public class CadastroCalendarioController {
	
	public CadastroCalendarioController() {
		
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_CALENDARIO)
	public void index(){
		
	}
	
	@Post
	@Path("/novoFeriado")
	public void novoFeriado(Date dtFeriado, TipoFeriado tipoFeriado){
		
		System.out.println(dtFeriado +" - " + tipoFeriado);
	}
	
	

}
