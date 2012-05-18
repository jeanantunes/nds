package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.TipoDescontoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}  
 * 
 *
 */
public interface TipoDescontoCotaRepository extends Repository<TipoDescontoCota,Long>{

	int obterSequencial();	
	
}
