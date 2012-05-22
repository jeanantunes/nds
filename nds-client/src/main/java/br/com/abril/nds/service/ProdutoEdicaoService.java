package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ProdutoEdicaoService {

	/**
	 * Obtém os produtos edição de acordo com o nome do produto.
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
	 * Obtém um produtos edição de acordo
	 * com o código do produto e o número da edição.
	 * 
	 * @param codigoProduto - código do produto
	 * @param numeroEdicao - número da edição
	 * 
	 * @return {@link ProdutoEdicao}
	 */
	ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao);

	/**
	 * Obtém edições do produto 
	 * 
	 * @param codigoProduto - código do produto
	 * @return Lista de edições do produto
	 */
	List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(String codigoProduto);

	List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto);
	
}