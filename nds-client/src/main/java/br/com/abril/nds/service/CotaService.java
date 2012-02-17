package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface CotaService {

	Cota obterPorNumeroDaCota(Integer numeroCota);
	
}
