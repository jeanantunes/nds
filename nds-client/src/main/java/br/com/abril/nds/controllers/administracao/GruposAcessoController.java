package br.com.abril.nds.controllers.administracao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.PermissaoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
	private PermissaoService permissaoService;

	@Autowired
	private Result result;

	public GruposAcessoController() {
		super();
	}
	
	@Path("/")
	public void index() {
	}

	/**
	 * Retorna a lista de permissões do sistema
	 * @return List
	 */
	public void carregarPermissoes(String nome, String descricao, String sortname, String sortorder, int rp, int page) {
		List<Permissao> permissoes = permissaoService.busca(nome, descricao, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		Long quantidade = permissaoService.quantidade(nome, descricao);
		result.use(FlexiGridJson.class).from(permissoes).total(quantidade.intValue()).page(page).serialize();
	}
	
}
