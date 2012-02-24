package br.com.abril.nds.controllers.estoque;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.lancamento.FuroProdutoController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	public DiferencaEstoqueController(Result result) {
		
		this.result = result;
	}
	
	@Get
	public void lancamento() {
		
		this.carregarCombosLancamento();
	}
	
	@Post
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(String dataMovimento, String tipoDiferenca) {
		
		System.out.println(dataMovimento);
		
		System.out.println(tipoDiferenca);
		
		result.use(Results.json()).from("teste", "result").serialize();
	}
	
	@Get
	public void consulta() {
		this.carregarCombosConsulta();
	}
	
	@Post
	@Path("/consultarFaltasSobras")
	public void consultarFaltasSobras() {
		result.use(Results.json()).from("Retorno", "result").serialize();
	}

	/**
	 * Método responsável por carregar todos os combos da tela de consulta.
	 */
	public void carregarCombosConsulta() {
		this.carregarComboTiposDiferenca();
		this.carregarComboFornecedores();
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de lançamento.
	 */
	private void carregarCombosLancamento() {
		
		this.carregarComboTiposDiferenca();
	}
	
	/**
	 * Método responsável por carregar o combo de tipos de diferença.
	 */
	private void carregarComboTiposDiferenca() {

		List<ItemDTO<TipoDiferenca, String>> listaTiposDiferenca =
			new ArrayList<ItemDTO<TipoDiferenca, String>>();
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_DE, TipoDiferenca.FALTA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_EM, TipoDiferenca.FALTA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_DE, TipoDiferenca.SOBRA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_EM, TipoDiferenca.SOBRA_EM.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		//TODO: adicionar todos os fornecedores no combo
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedores();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia())
			);
		}
		
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		
		//TODO: tratar retorno da consulta
		
		if (listaProduto != null && !listaProduto.isEmpty()){
			
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
	public void pesquisarPorCodigoProduto(String codigoProduto) {
		Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		
		//TODO: tratar retorno da consulta
		
		result.use(Results.json()).from(produto, "result").serialize();
	}
	
	@Get
	@Path("/")
	public void index(){
		
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
