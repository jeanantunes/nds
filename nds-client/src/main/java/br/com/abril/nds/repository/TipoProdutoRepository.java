package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoProduto } 
 * 
 * @author Discover Technology
 *
 */
public interface TipoProdutoRepository extends Repository<TipoProduto, Long> {
	

	/**
	 * Busca os Tipos de Produtos respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto nome do tipo do produto
	 * @param codigo
	 * @param codigoNCM 
	 * @param codigoNBM
	 * @param orderBy nome do campo para compor a ordencao
	 * @param ordenacao tipo da ordencao
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return lista de Tipos de Produtos.
	 */
	public abstract List<TipoProduto> busca(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM, String orderBy, 
			Ordenacao ordenacao, int initialResult, int maxResults);
	
	/**
	 * Obtem a quantidade de registros respeitando as restricoes parametrizadas.
	 * @param nomeTipoProduto
	 * @param codigo
	 * @param codigoNCM
	 * @param codigoNBM
	 * @return quantidade de registros
	 */
	public abstract Long quantidade(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM);
	
	/**
	 * Verifica se um tipo de produto ja possui um produto vinculado.
	 * @param tipoProduto
	 * @return true para tipo produto que ja possui produto vinculado
	 */
	public abstract boolean hasProdutoVinculado(TipoProduto tipoProduto);
	
	/**
	 * Obtem o ultimo id da tabela.
	 * @return
	 */
	public abstract Long getMaxCodigo();
	
	/**
	 * Obtem tipo de produto por código
	 * @param codigo
	 * @return TipoProduto
	 */
	public abstract TipoProduto obterPorCodigo(Long codigo);
}
