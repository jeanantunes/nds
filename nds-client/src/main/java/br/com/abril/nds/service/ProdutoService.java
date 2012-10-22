package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
public interface ProdutoService {

	/**
	 * Obtém um produto de acordo com o nome do produto.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorNome(String nome);
	
	/**
	 * Obtém produtos cujo nome começa com o nome informado.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeNome(String nome);
	
	/**
	 * Obtém um produto de acordo com o código do produto.
	 * 
	 * @param codigoProduto - código do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorCodigo(String codigoProduto);
	
	String obterNomeProdutoPorCodigo(String codigoProduto);

	List<ConsultaProdutoDTO> pesquisarProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto,
			String sortorder, String sortname, int page, int rp);

	void removerProduto(Long id) throws UniqueConstraintViolationException;

	Integer pesquisarCountProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto);
	
	/**
	 * Verifica se o Produto está em estoque
	 * desconsiderando a quantidade
	 * 
	 * @param codigoProduto codigo do produto
	 * @return true caso ja exista o produto no estoque
	 */
	boolean isProdutoEmEstoque(String codigoProduto);
	
	/**
	 * 
	 * @param produto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param codigoTipoDesconto
	 * @param codigoTipoProduto
	 */
	void salvarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, Long codigoTipoDesconto, Long codigoTipoProduto);
	
	/**
	 * Retorna o Produto pelo se ID.
	 * 
	 * @param id
	 * @return
	 */
	Produto obterProdutoPorID(Long id);
	
	/**
	 * Obtém uma lista de produtos dos ids paramestrizados
	 * 
	 * @param idsProdutos lista de ids dos produtos
	 * @return lista de produtos
	 */
	List<Produto> obterProdutosPelosIds(List<Long> idsProdutos);
	
	/**
	 * Obtém todos os produtos.
	 * 
	 * @return lista de produtos
	 */
	List<Produto> obterProdutos();
}
