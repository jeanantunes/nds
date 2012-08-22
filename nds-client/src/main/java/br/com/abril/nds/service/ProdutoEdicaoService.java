package br.com.abril.nds.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.Intervalo;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
public interface ProdutoEdicaoService {

	/**
	 * Obtém o ProdutoEdição.
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @return
	 */
	public ProdutoEdicao obterProdutoEdicao(Long idProdutoEdicao);
	
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
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde,
			String sortorder, String sortname, int page, int maxResults);
	
	/**
	 * Pesquisa as últimas edições cadastradas, .<br>
	 * 
	 * @param codigoProduto
	 * @param maxResults
	 * 
	 * @return
	 */
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(String codigoProduto,
			int maxResults);
	
	/**
	 * Obtém a quantidade de edições cadastradas filtradas pelos critérios 
	 * escolhidos pelo usuário.
	 * 
	 * @param dto
	 * 
	 * @return
	 */
	public Long countPesquisarEdicoes(String codigoProduto, String nomeProduto,
			Intervalo<Date> dataLancamento, Intervalo<BigDecimal> preco , StatusLancamento statusLancamento,
			String codigoDeBarras, boolean brinde);
	
	/**
	 * Salva ou Atualiza um novo ProdutoEdição.
	 * 
	 * @param dto
	 * @param codigoProduto
	 * @param contentType
	 * @param imgInputStream
	 */
	public void salvarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, String contentType, InputStream imgInputStream);
	
	/**
	 * Exclui uma Edição da base de dados.<br>
	 * Os critérios para exclusão são:
	 * <ul>
	 * <li>A Edição não pode ser cadastrado via INTERFACE;</li>
	 * <li>A Edição não pode estar sendo utilizada em outras partes dos sitema;</li>
	 * </ul>
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @exception 
	 */
	public void excluirProdutoEdicao(Long idProdutoEdicao) throws UniqueConstraintViolationException;
	/**
	 * Retorna um produto edição dado seu código de barras
	 * @param codigoBarras - código de barras
	 * @return  ProdutoEdicao
	 */
	ProdutoEdicao buscarProdutoPorCodigoBarras(String codigoBarras);
	
}
