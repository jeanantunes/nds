package br.com.abril.nds.controllers.lancamento;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/lancamento/relatorioVendas")
public class RelatorioVendasController {

	private Result result;
	
	public RelatorioVendasController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	public void index(){
	}

	@Post
	public void pesquisarCurvaABCDistribuidor(String dataDe, String dataAte) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCDistribuidor(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCEditor(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCProduto(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto) throws Exception{
	}

	@Post
	public void pesquisarCurvaABCCota(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
	}

	@Post
	public void pesquisarProdutosPorEditor(String codigo, String dataDe, String dataAte) throws Exception{
	}

}
