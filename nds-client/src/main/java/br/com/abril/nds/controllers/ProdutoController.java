package br.com.abril.nds.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/produto")
public class ProdutoController {

	private Result result;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	public ProdutoController(Result result) {
		this.result = result;
	}
	
	@Post
	public void pesquisarPorCodigoProduto(String codigoProduto) throws ValidacaoException{
		Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		
		if (produto == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto não encontrado.");
			
		} else {
			
			result.use(Results.json()).from(produto, "result").serialize();
			
		}
		
		
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			Produto produtoAutoComplete = null;
			
			for (Produto produto : listaProduto){
				produtoAutoComplete = new Produto();
				produtoAutoComplete.setCodigo(produto.getCodigo());
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produto.getNome(), null, produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, String numeroEdicao) {
		
		boolean numEdicaoValida = false;
		
		try {
			
			numEdicaoValida = produtoEdicaoService.validarNumeroEdicao(codigoProduto, numeroEdicao);
			
			if (!numEdicaoValida) {

				throw new ValidacaoException(TipoMensagem.ERROR, "Edição não encontrada para o produto.");
				
			} else {
				
				result.use(Results.json()).from("", "result").serialize();
				
			}
			
		} catch (IllegalArgumentException e ) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			
		}
		
	}
	
}
