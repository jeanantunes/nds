package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes a produtos.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/produto")
public class ProdutoController {

	private Result result;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private TipoProdutoService tipoProdutoService;
	
	public ProdutoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	public void index() {
		
		List<TipoProduto> listaTipoProduto = this.tipoProdutoService.obterTodosTiposProduto();
		
		if (listaTipoProduto != null && !listaTipoProduto.isEmpty()) {
			this.result.include("listaTipoProduto", listaTipoProduto);
		}
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
	public void autoCompletarEdicaoPorProduto(String codigoProduto) {
		List<ProdutoEdicao> listaProdutoEdicao = this.produtoEdicaoService.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
				
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()) {			
		
			ProdutoEdicao proEdicao = null;
			
			for (ProdutoEdicao produtoEd : listaProdutoEdicao) {
				
				proEdicao = new ProdutoEdicao();
				proEdicao.setNumeroEdicao(produtoEd.getNumeroEdicao());
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produtoEd.getNumeroEdicao().toString(), null, proEdicao);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
		
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) {
		Produto produto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		
		if (produto == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + nomeProduto + "\" não encontrado!");
		
		}
			
		result.use(Results.json()).from(produto, "result").serialize();
	}
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, String numeroEdicao) {
		
		boolean numEdicaoValida = false;
			
		ProdutoEdicao produtoEdicao =
			produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		numEdicaoValida = (produtoEdicao != null);
		
		if (!numEdicaoValida) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Edição \"" + numeroEdicao + "\" não encontrada para o produto!");
			
		} else {
			
			result.use(Results.json()).from("", "result").serialize();			
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
	
	@Post
	@Path("/obterEstoque")
	public void obterEstoque(Long idProdutoEdicao) {
		
		if (idProdutoEdicao == null) {
			
			result.use(Results.json()).from("", "estoqueProduto").serialize();
		}
		
		EstoqueProduto estoqueProduto =
			this.estoqueProdutoService.buscarEstoquePorProduto(idProdutoEdicao);
		
		if (estoqueProduto == null) {
			
			result.use(Results.json()).from("", "estoqueProduto").serialize();
			
		} else {
		
			result.use(Results.json()).from(estoqueProduto, "result").serialize();
		}
	}
	
	@Path("/pesquisarProdutos")
	public void pesquisarProdutos(String codigo, String produto, String fornecedor, String editor,
			Long codigoTipoProduto, String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page*rp - rp;
		
		List<ConsultaProdutoDTO> listaProdutos =
			this.produtoService.pesquisarProdutos(codigo, produto, fornecedor, editor, 
				codigoTipoProduto, sortorder, sortname, startSearch, rp);
		
		Integer totalResultados = this.produtoService.pesquisarCountProdutos(codigo, produto, fornecedor, editor, codigoTipoProduto);
		
		this.result.use(FlexiGridJson.class).from(listaProdutos).total(totalResultados).page(page).serialize();
	}
	
	@Post
	public void removerProduto(Long id) {
		
		try {
			
			this.produtoService.removerProduto(id);
			
		} catch (UniqueConstraintViolationException e) {
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, e.getMessage()), 
					"result").recursive().serialize();
			throw new ValidacaoException();
		}
			
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Produto excluido com sucesso."), 
				"result").recursive().serialize();
	}
	
}
