package br.com.abril.nds.controllers.cadastro;

import java.util.Date;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/cadastro/edicao")
public class ProdutoEdicaoController {

	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	public void index() { }
	
	@Post
	@Path("/pesquisarEdicoes")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamento, String situacaoLancamento,
			String codigoDeBarras, boolean brinde) {
		
		
		System.out.println("codigoProduto: " + codigoProduto);
		System.out.println("nomeProduto: " + nomeProduto);
		System.out.println("dataLancamento: " + dataLancamento);
		System.out.println("situacaoLancamento: " + situacaoLancamento);
		System.out.println("codigoDeBarras: " + codigoDeBarras);
		System.out.println("brinde: " + brinde);
		
	}
	
}
