package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;

/**
 * 
 * @author Diego Fernandes
 *
 */
public interface CotaGarantiaRepository extends Repository<CotaGarantia,Long>{
	
	
	/**
	 * Recupera a garantia da cota.
	 * @param idCota Id da cota.
	 * @return
	 */
	public CotaGarantia getByCota(Long idCota);
}
