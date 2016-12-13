package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
public interface ProdutoRepository extends Repository<Produto, Long> {

	/**
	 * Obtém produtos cujo nome começa com o nome informado.
	 * 
	 * @param nome - nome do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeNome(String nome);
	
	List<Produto> obterProdutoLikeNome(String nome, Integer qtdMaxRegRetorno);
	/**
	 * Obtém produtos cujo código começa com o código informado.
	 * 
	 * @param codigo - código do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeCodigo(String codigo);
	
	/**
	 * Obtém um produto de acordo com o número do produto.
	 * 
	 * @param nome - nome do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorNome(String nome);
	
	/**
	 * Obtém um produto de acordo com o código do produto.
	 * 
	 * @param codigoProduto - código do produto
	 * 
	 * @return {@link Produto}
	 */
	Produto obterProdutoPorCodigoProdin(String codigoProduto);
	
	Produto obterProdutoBalanceadosPorCodigo(String codigoProduto, Date dataLancamento);
	
	String obterNomeProdutoPorCodigo(String codigoProduto);
	
	List<ConsultaProdutoDTO> pesquisarProdutos(String codigo, String produto, String fornecedor, String editor,
			Long codigoTipoProduto, String sortorder, String sortname, int page, int rp, Boolean isGeracaoAutomatica);

	Integer pesquisarCountProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto, Boolean isGeracaoAutomatica);

	Produto obterProdutoPorID(Long id);
	
	/**
	 * Obtem produto por nome ou codigo
	 * @param nome
	 * @param codigo
	 * @return Produto
	 */
	Produto obterProdutoPorNomeProdutoOuCodigo(String nome, String codigo);
	
	GrupoProduto obterGrupoProduto(String codigoProduto);
	
	List<Produto> buscarProdutosBalanceadosOrdenadosNome(Date dataLancamento);

	BigDecimal obterDescontoLogistica(Long idProduto);

	List<String> verificarProdutoExiste(String... codigoProduto);

    Produto obterProdutoPorCodigoICD(String codigoProduto);

    Produto obterProdutoPorCodigoICDLike(String codigoProduto);

    Produto obterProdutoPorCodigoProdinLike(String codigoProduto);
	
	String obterUltimoCodigoProdutoRegional();
	
	boolean existeProdutoRegional(String codigo);
	
	Produto obterProdutoPorICDBaseadoNoPrimeiroBarra(String codigo_icd);
	
	Produto obterProdutoPorICDSegmentoNotNull(String codigo_icd);
	
	List<Produto> obterProdutosPorDescontoLogistica(DescontoLogistica descontoLogistica);
	
}
