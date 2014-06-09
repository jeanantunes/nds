package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.NCM;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.NCM}  
 * 
 * @author Discover Technology
 *
 */
public interface NCMRepository extends Repository<NCM,Long>{
	
	public NCM obterPorCodigo(Long codigoNcm);

}
