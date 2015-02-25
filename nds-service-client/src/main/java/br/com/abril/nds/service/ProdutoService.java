package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

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
	
	List<Produto> obterProdutoLikeNome(String nome, Integer qtdMaxResult);
	/**
	 * Obtém produtos cujo código começa com o código informado.
	 * 
	 * @param nomeProduto - código do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeCodigo(String codigo);

	
	/**
	 * Obtém um produto de acordo com o código do produto.
	 * 
	 * @param codigoProduto - código do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorCodigo(String codigoProduto);
	
	Produto obterProdutoBalanceadosPorCodigo(String codigoProduto, Date dataLancamento);
	
	String obterNomeProdutoPorCodigo(String codigoProduto);

	List<ConsultaProdutoDTO> pesquisarProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto,
			String sortorder, String sortname, int page, int rp, Boolean isGeracaoAutomatica);

	void removerProduto(Long id);

	Integer pesquisarCountProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto, Boolean isGeracaoAutomatica);
	
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
	 * @param idDesconto
	 * @param codigoTipoProduto
	 */
	void salvarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, Long idDesconto, Long codigoTipoProduto);
	
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
	
	List<Produto> obterProdutosBalanceadosOrdenadosNome(Date dataLancamento);

	List<String> verificarProdutoExiste(String...codigoProduto);
	
	List<TipoSegmentoProduto> carregarSegmentos();
	
	List<TipoClassificacaoProduto> carregarClassificacaoProduto();

    Produto obterProdutoPorProdin (String codigoProdin);

    String obterCodigoDisponivel();
    
    Long obterIdFornecedorUnificadorPorCodigoProduto(String codigoProduto);
	
    Produto obterProdutoPorICDBaseadoNoPrimeiroBarra (String codigoICD);

	Boolean isIcdValido(String codIcd);
}
