package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.model.estoque.Expedicao;

/**
 * Interface responsável por definir as regras de implementação referente a expedição de lançamentos de produtos.
 * 
 * @author Discover Technology
 *
 */
public interface ExpedicaoRepository extends Repository<Expedicao, Long> {
	
	/**
	 * Retorna a quantidade do resumo de expedição de produtos de um box.
	 * @param filtro
	 * 
	 * @return Long
	 */
	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro);
	
	/**
	 * Efetua consulta do resumo de expedição de produtos de um box.
	 * @param filtro
	 * 
	 * @return List - ExpedicaoDTO
	 */
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro);
	
	
	
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
	 * Retorna a quantidade de resumo de expedição de box.
	 * @param filtro
	 * @return List<ExpedicaoDTO>
	 */
	Long obterQuantidadeResumoExpedicaoPorBox(Long idBox,Date dataLancamento);

	/**
	 * Retorna a ultima expedição do dia especificado
	 * @param dataOperacao
	 * @return Expedicao
	 */
	Date obterUltimaExpedicaoDia(Date dataOperacao);
	
	/**
	 * Retorna a ultima espedição realizada
	 * @return Date
	 */
	Date obterDataUltimaExpedicao();

}
