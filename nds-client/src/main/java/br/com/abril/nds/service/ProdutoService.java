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
	Produto obterProdutoPorNomeProduto(String nome);
	
	/**
	 * Obtém produtos cujo nome começa com o nome informado.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link List<Produto>}
	 */
	List<Produto> obterProdutoLikeNomeProduto(String nome);
	
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
	 * @param id
	 * @param codigo
	 * @param nomeProduto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param sloganProduto
	 * @param codigoTipoDesconto
	 * @param codigoTipoProduto
	 * @param formaComercializacao
	 * @param peb
	 * @param pacotePadrao
	 * @param periodicidade
	 * @param percentualAbrangencia
	 * @param algoritmo
	 * @param parametrosAbertos
	 * @param lancamentoImediato
	 * @param comprimento
	 * @param espessura
	 * @param largura
	 * @param peso
	 * @param tributacaoFiscal
	 * @param situacaoTributaria
	 * @param classeHistogramaAnalitico
	 * @param percentualCotaFixacao
	 * @param percentualReparteFixacao
	 * @param grupoEditorial
	 * @param subGrupoEditorial
	 */
	void salvarProduto(Long id, String codigo, String nomeProduto, Long codigoEditor, Long codigoFornecedor,
			String sloganProduto, Long codigoTipoDesconto, Long codigoTipoProduto, String formaComercializacao, Integer peb, 
			Integer pacotePadrao, String periodicidade, Double percentualAbrangencia, String algoritmo, boolean parametrosAbertos, 
			boolean lancamentoImediato, Double comprimento, Double espessura, Double largura, Double peso, 
			String tributacaoFiscal, String situacaoTributaria, String classeHistogramaAnalitico, Double percentualCotaFixacao,
			Double percentualReparteFixacao, String grupoEditorial, String subGrupoEditorial);
}
