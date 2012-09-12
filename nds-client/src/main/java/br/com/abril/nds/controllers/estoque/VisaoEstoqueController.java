package br.com.abril.nds.controllers.estoque;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("estoque/visaoEstoque")
public class VisaoEstoqueController {
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private Result result;

	@Path("/")
	@Rules(Permissao.ROLE_ESTOQUE_VISAO_DO_ESTOQUE)
	public void index()
	{
		List<Fornecedor> listFornecedores = fornecedorService.obterFornecedores();
		result.include("listFornecedores", listFornecedores);
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaVisaoEstoque filtro, String sortname, String sortorder, int rp, int page) {
		
		
		
		
		
	}
}
