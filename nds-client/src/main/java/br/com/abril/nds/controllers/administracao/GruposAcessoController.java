package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * @author infoA2
 * Controller dos grupos de acesso
 */
@Resource
@Path("/administracao/gruposAcesso")
public class GruposAcessoController {

	@Autowired
	private Result result;

	public GruposAcessoController() {
		super();
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_GRUPOS_ACESSO)
	public void index() {
	}

	/**
	 * Retorna a lista de permissões do sistema
	 * @return List
	 */
	public void carregarPermissoes(String nome, String descricao, String sortname, String sortorder, int rp, int page) {
	}
	
}
