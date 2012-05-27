package br.com.abril.nds.controllers.administracao;

import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/administracao/cadastroCalendario")
public class CadastroCalendarioController {
	
	public CadastroCalendarioController() {
		
	}
	
	@Path("/")
	public void index(){
		
	}
	
	@Post
	@Path("/novoFeriado")
	public void novoFeriado(Date dtFeriado, TipoFeriado tipoFeriado){
		
		System.out.println(dtFeriado +" - " + tipoFeriado);
	}
	
	

}
