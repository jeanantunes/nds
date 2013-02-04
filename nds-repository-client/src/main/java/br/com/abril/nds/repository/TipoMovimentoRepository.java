package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
public interface TipoMovimentoRepository extends Repository<TipoMovimento, Long> {
	
	/**
	 * Obtém uma lista de tipos de movimento.
	 * 
	 * @return {@link List<TipoMovimento>}
	 */
	List<TipoMovimento> obterTiposMovimento();
	
	/**
	 * Obtém lista de tipos de movimento filtrada
	 * 
	 * @param filtro
	 * @return
	 */
	List<TipoMovimentoDTO> obterTiposMovimento(FiltroTipoMovimento filtro);

	/**
	 * Count da busca "obterTiposMovimento"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer countObterTiposMovimento(FiltroTipoMovimento filtro);
	
}
