package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;

/**
 * Interface responsável por definir as regras de implementação referente a expedição de lançamentos de produtos.
 * @author Discover Technology
 *
 */
public interface ExpedicaoService {
	
	/**
	 * Efetua consulta do resumo de expedição de produtos por box.
	 *  
	 * @param filtro
	 * @return List<ExpedicaoDTO>
	 */
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro);

	/**
	 * Retorna a quantidade de resumo de expedição de produtos por box.
	 * 
	 * @param filtro
	 * @return Long
	 */
	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro);
	
	/**
	 * Efetua consulta do resumo de expedição de produtos.
	 * @param filtro
	 * @return List<ExpedicaoDTO>
	 */
	List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro);
	
	/**
	 * Efetua consulta do resumo de expedição de box.
	 * @param filtro
	 * @return List<ExpedicaoDTO>
	 */
	List<ExpedicaoDTO> obterResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro);
	
	/**
	 * Retorna a quantidade de resumo de expedição de produtos.
	 * @param filtro
	 * @return List<ExpedicaoDTO>
	 */
	Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro);

	/**
	 * Obtém a última expedição do dia
	 * @param dataOperacao
	 * @return Date
	 */
	Date obterDataUltimaExpedicaoDia(Date dataOperacao);

	/**
	 * Obtém a última expedição realizada
	 * @return Date
	 */
	Date obterDataUltimaExpedicao();

}
