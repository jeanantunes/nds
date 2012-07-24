package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TipoDescontoCota}  
 * 
 *
 */
public interface TipoDescontoCotaRepository extends Repository<TipoDescontoCota,Long>{

	int obterSequencial();
	
	List<TipoDescontoDistribuidor> obterTipoDescontosDistribuidor();

	Integer buscarTotalDescontoPorCota();
	
}
