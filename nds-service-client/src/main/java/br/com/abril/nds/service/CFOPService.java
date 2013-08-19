package br.com.abril.nds.service;

import br.com.abril.nds.model.fiscal.CFOP;

public interface CFOPService {
	
	
	/**
	 * Obtem CFOP por id
	 * @param codigo
	 * @return CFOP
	 */
	CFOP buscarPorId(Long id);
	
	/**
	 * Obtem CFOP por código
	 * @param codigo
	 * @return
	 */
	CFOP buscarPorCodigo(String codigo);

}
