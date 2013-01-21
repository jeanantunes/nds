package br.com.abril.nds.controllers.distribuicao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/distribuicao/fixacaoReparte")
public class FixacaoReparteController extends BaseController {
	@Autowired
	private Result result;
	
	@Autowired
	TipoProdutoService tipoProdutoService;

	@Rules(Permissao.ROLE_DISTRIBUICAO_FIXACAOREPARTE)
	public void index(){
		result.include("listaTiposProduto", getListaTipoProduto());
	}
	
	public List<TipoProduto> getListaTipoProduto(){
		return tipoProdutoService.obterTodosTiposProduto();
	}

}
