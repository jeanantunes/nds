package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.UsuarioService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/usuario")
public class UsuarioController {

	@Autowired
	private Result result;
	
	@Autowired
	private UsuarioService usuarioService;

    @Post
	@Path("/validarUsuarioSupervisor")
    public void validarUsuarioSupervisor(String username, String senha) {

    	boolean isSupervisor = true;

    	if (this.usuarioService.isNotSupervisor()) {

    		isSupervisor = username != null && senha != null ? 
    				this.usuarioService.verificarUsuarioSupervisor(username, senha) : false;
    	}

    	if (!isSupervisor) {

    		throw new ValidacaoException(TipoMensagem.NONE, "Usuário não tem permissão de supervisor.");
    	}

    	this.result.use(Results.json()).from("").serialize();
    }
}
