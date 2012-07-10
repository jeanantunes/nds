package br.com.abril.nds.controllers.administracao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.service.CalendarioService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/administracao/cadastroCalendario")
public class CadastroCalendarioController {
	
	@Autowired
	private CalendarioService calendarioService;
	
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
