package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
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
	
	
	/**
	 * Altera um produtoEdicao
	 * 
	 * @param ProdutoEdicao produtoEdicao
	 * @return void
	 */
	void alterarProdutoEdicao(ProdutoEdicao produtoEdicao);
	
	/**
	 * Pesquisa as Edições já cadastradas.<br>
	 * Possui como opções de filtro:<br>
	 * <ul>
	 * <li>Código do Produto;</li>
	 * <li>Nome do Produto;</li>
	 * <li>Data de Lançamento;</li>
	 * <li>Situação do Lançamento;</li>
	 * <li>Código de Barra da Edição;</li>
	 * <li>Contém brinde;</li>
	 * </ul>
	 * 
	 * @param dto
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param maxResults
	 * 
	 * @return
	 */
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(ProdutoEdicaoDTO dto,
			String sortorder, String sortname, int page, int maxResults);
	
	/**
	 * Obtém a quantidade de edições cadastradas filtradas pelos critérios 
	 * escolhidos pelo usuário.
	 * 
	 * @param dto
	 * @return
	 */
	public Long countPesquisarEdicoes(ProdutoEdicaoDTO dto);
	
}
