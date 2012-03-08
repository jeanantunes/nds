package br.com.abril.nds.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
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
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + codigoProduto + "\" não encontrado!");
			
		} else {
			
			result.use(Results.json()).from(produto, "result").serialize();
			
		}		
	}
	
	@Post
	public void autoCompletarPorPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeNomeProduto(nomeProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			Produto produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
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
	public void pesquisarPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		
		if (listaProduto == null || listaProduto.isEmpty()) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + nomeProduto + "\" não encontrado!");
		
		} else if (listaProduto.size() == 1) {
			
			result.use(Results.json()).from(listaProduto.get(0), "result").serialize();
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Existe mais de um produto com o nome \"" + nomeProduto + "\"!");
		
		}
	}
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, String numeroEdicao) {
		
		boolean numEdicaoValida = false;
		
		try {
			
			ProdutoEdicao produtoEdicao =
				produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
			
			numEdicaoValida = (produtoEdicao != null);
			
			if (!numEdicaoValida) {

				throw new ValidacaoException(TipoMensagem.WARNING, "Edição \"" + numeroEdicao + "\" não encontrada para o produto!");
				
			} else {
				
				result.use(Results.json()).from("", "result").serialize();
				
			}
			
		} catch (IllegalArgumentException e ) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			
		}
	}
	
	@Post
	@Path("/obterProdutoEdicao")
	public void obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao) {
		
		ProdutoEdicao produtoEdicao =
			produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Edição não encontrada para o produto!");
		}
		
		result.use(Results.json()).from(produtoEdicao, "result").serialize();
	}
	
}
