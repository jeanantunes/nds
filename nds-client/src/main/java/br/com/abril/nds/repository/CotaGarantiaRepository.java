package br.com.abril.nds.repository;

import org.apache.poi.ss.formula.functions.T;

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
	
	/**
	 * Delete os imóveis da garantia.
	 * @param idGarantia id da garantia
	 */
	public void deleteListaImoveis(Long idGarantia);
	
	
	/**
	 * Delete a garantia da cota.
	 * @param idCota Id da cota.
	 */
	public void deleteByCota(Long idCota);
	/**
	 * Recupera a garantia da cota.
	 * @param idCota idCota Id da cota.
	 * @param type tipo da garantia
	 * @return
	 */
	@SuppressWarnings("hiding")
	public abstract <T extends CotaGarantia> T getByCota(Long idCota, Class<T> type);
}
