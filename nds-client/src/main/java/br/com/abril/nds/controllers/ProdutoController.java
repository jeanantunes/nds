package br.com.abril.nds.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
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
	public void pesquisarPorCodigoProduto(String codigoProduto) {
		Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		

		if (produto == null) {
			List<String> listaMensagemValidacao = new ArrayList<String>();
			
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("Produto não encontrado.");

			result.use(Results.json()).from(listaMensagemValidacao, Constantes.PARAM_MSGS).serialize();
		}
		
		result.use(Results.json()).from(produto, "result").serialize();
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		

		//TODO: tratar retorno da consulta
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			

			Produto produtoAutoComplete = null;
			
			for (Produto produto : listaProduto){
				produtoAutoComplete = new Produto();
				produtoAutoComplete.setCodigo(produto.getCodigo());
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produto.getNome(), null, produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}

			
			result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
		}

	}
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, Long numeroEdicao) {
		
		boolean numEdicaoValida =
			produtoEdicaoService.validarNumeroEdicao(codigoProduto, numeroEdicao);
		
		if (!numEdicaoValida) {
			List<String> listaMensagemValidacao = new ArrayList<String>();
			
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("Edição não encontrada para o produto.");

			result.use(Results.json()).from(listaMensagemValidacao, Constantes.PARAM_MSGS).serialize();
		} else {
			//TODO: retorno ajax quando não precisar de result
			result.use(Results.json()).from("", "result").serialize();
		}
	}
	
}
