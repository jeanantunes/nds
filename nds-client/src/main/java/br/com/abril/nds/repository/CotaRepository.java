package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaRepository extends Repository<Cota, Long> {

	/**
	 * Obtém uma Cota pelo seu número.
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return {@link Cota}
	 */
	Cota obterPorNumerDaCota(Integer numeroCota);
	
}
