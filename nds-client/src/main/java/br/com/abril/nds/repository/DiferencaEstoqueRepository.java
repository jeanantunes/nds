package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.DiferencaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.model.estoque.Diferenca;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.estoque.Diferenca}.
 * 
 * @author Discover Technology
 *
 */
public interface DiferencaEstoqueRepository extends Repository<Diferenca, Long> {

	/**
	 * Obtém as diferenças de estoque (faltas/sobras) para lançamento
	 * de acordo com o filtro.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return {@link List<DiferencaDTO>}
	 */
	List<DiferencaDTO> obterDiferencasLancamento(FiltroLancamentoDiferencaEstoqueDTO filtro);
	
	/**
	 * Obtém as diferenças de estoque (faltas/sobras) para consulta
	 * de acordo com o filtro.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return {@link List<DiferencaDTO>}
	 */
	List<DiferencaDTO> obterDiferencas(FiltroConsultaDiferencaEstoqueDTO filtro);
	
}
