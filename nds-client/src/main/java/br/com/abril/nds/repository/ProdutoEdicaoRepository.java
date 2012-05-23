package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ProdutoEdicaoRepository extends Repository<ProdutoEdicao, Long> {
	
	/**
	 * Obtém o codigoSM a partir do idProdutoEdicao
	 * 
	 * @param sequenciaMatriz
	 * 
	 * @return Integer
	 */
	public Integer obterCodigoMatrizPorProdutoEdicao(Long idProdutoEdicao);
	
	/**
	 * Obtém o produtoEdicao através do código SM do mesmo produtoEdicao que esta amarrado a seu lancamento.
	 * 
	 * @param sequenciaMatriz
	 * 
	 * @return ProdutoEdicao
	 */
	public ProdutoEdicao obterProdutoEdicaoPorSequenciaMatriz(Integer sequenciaMatriz);
	
	/**
	 * Obtem o percentual de comissionamento (fatorDesconto) de acordo com os parâmetros
	 * de idProdutoEdicao, numerCota e idDistribuidor;
	 * 
	 * @param idProdutoEdicao
	 * @param numeroCota
	 * @param idDistribuidor
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterFatorDesconto(Long idProdutoEdicao, Integer numeroCota, Long idDistribuidor);
	
	/**
	 * Obtém produtos edição de acordo com o nome do produto.
	 * 
	 * @param nomeProduto - nome do produto
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto);
	
	/**
	 * Obtém um furo do produto de acordo com os parâmetros informados.
	 * 
	 * @param codigo - código do produto
	 * @param nomeProduto - nome do produto
	 * @param edicao - número da edição
	 * @param dataLancamento - data de lançamento
	 * 
	 * @return {@link FuroProdutoDTO}
	 */
	FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento);
	
	/**
	 * Obtém um produto de edição de acordo com o 
	 * código do produto e o número da edição.
	 * 
	 * @param codigoProduto - nome do produto
	 * @param numeroEdicao - número da edição
	 * 
	 * @return {@link ProdutoEdicao}
	 */
	ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto,
														   Long numeroEdicao);
	
	/**
	 * Obtém produtos edição de acordo com o 
	 * produto e o produto edição.
	 * 
	 * @param produto - produto
	 * @param produtoEdicao - produto edição
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterListaProdutoEdicao(Produto produto, ProdutoEdicao produtoEdicao);

	/**
	 * Obtém edições do produto 
	 * 
	 * @param codigoProduto - código do produto
	 * @return Lista de edições do produto
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto);

	
	
	ProdutoEdicao obterProdutoEdicaoPorCodigoBarra(String codigoBarra);

	ProdutoEdicao obterProdutoEdicaoPorSM(Long sm);
	
	List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto);
	
	/**
	 * Obtém uma lista de produtos edição de acordo com o parâmetro iformado.
	 * 
	 * @param idsProdutoEdicao - identificadores de produto edição
	 * 
	 * @return {@link List<ProdutoEdicao>}
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorId(Set<Long> idsProdutoEdicao);
	
}