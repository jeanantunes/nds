package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.movimentacao.TipoMovimento;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
public interface TipoMovimentoService {

	/**
	 * Obtém os tipos de movimento.
	 * 
	 * @return {@link List<TipoMovimento>}
	 */
	List<TipoMovimento> obterTiposMovimento();
	
}
