package br.com.abril.nds.controllers.administracao;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.TipoEntregaService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("administracao/alteracaoCota")
public class AlteracaoCotaController {

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoEntregaService tipoEntregaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	private Result result;
	
	public AlteracaoCotaController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_ALTERACAO_COTA)
	public void index()
	{
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		List<TipoEntrega> listTipoEntrega = tipoEntregaService.obterTodos();
		
		
		
		result.include("listFornecedores", listFornecedores);
		result.include("listTipoEntrega", listTipoEntrega);
	}
}
