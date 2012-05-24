package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define servi�os referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
public interface TipoProdutoService {
	
	/**
	 * Busca o Tipo Produto pelo id. 
	 * @param id
	 * @return o TipoProduto
	 */
	public abstract TipoProduto obterPorId(Long id);
	
	/**
	 * Remove o Tipo Produto do reposit?rio de dados.
	 * @param id
	 * @throws ValidacaoException Lan�a quando o Tipo de Produto em quest�o n�o pode ser removido.
	 */
	public abstract void remover(Long id);
	
	/**
	 * Atualiza a entidade tipo produto no repositorio de dados.
	 * @param entity entidade a ser persistida.
	 * @return o TipoProduto que foi persistido.
	 * @throws ValidacaoException quando o Tipo de Produto em quest�o n�o pode ser editado.
	 */
	public abstract TipoProduto merge(TipoProduto entity) throws ValidacaoException;
	
	/**
	 * Busca os tipos de produtos respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto nome do tipo do produto
	 * @param codigo
	 * @param codigoNCM
	 * @param codigoNBM
	 * @param orderBy nome do campo para compor a orden��o
	 * @param ordenacao tipo da orden��o
	 * @param initialResult resultado inicial
	 * @param maxResults numero m�ximo de resultados
	 * @return lista de Tipos de Produtos.
	 */
	public abstract List<TipoProduto> busca(String nomeTipoProduto, String codigo, String codigoNCM, String codigoNBM, String orderBy, 
			Ordenacao ordenacao, int initialResult, int maxResults);
	
	/**
	 * Obtem a quantidade de registros respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto
	 * @param codigo
	 * @param codigoNCM
	 * @param codigoNBM
	 * @return
	 */
	public abstract Long quantidade(String nomeTipoProduto, String codigo, String codigoNCM, String NBM);
	
	/**
	 * Obt�m tipo produto por id.
	 * @param id 
	 * @return tipoProduto
	 */
	public abstract TipoProduto buscaPorId(Long id);
	
	/**
	 * Obtem todos os tipos de produtos
	 * @return lista tipoProduto
	 */
	public abstract List<TipoProduto> obterTodosTiposProduto();
}
