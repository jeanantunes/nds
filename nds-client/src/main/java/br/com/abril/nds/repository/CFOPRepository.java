package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.CFOP;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.CFOP}  
 * 
 * @author Discover Technology
 *
 */
public interface CFOPRepository extends Repository<CFOP, Long> {
	
	/**
	 * Obtem CFOP por código
	 * @param codigo
	 * @return CFOP 
	 */
	CFOP obterPorCodigo(String codigo);
	
}
