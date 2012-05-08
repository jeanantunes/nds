package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;


/**
 * Interface que define os serviços referentes
 * ao cadastro de garantias da cota.
 * 
 * @author Discover Technology
 */
public interface CotaGarantiaService {
	
	
	
	
	/**
	 * Salva no repositorio de dados a garantia da cota.
	 * @param entity garantia da cota.
	 * @return
	 */
	public abstract CotaGarantia salva(CotaGarantia entity);
	
	/**
	 * Recupera a garantia da cota.
	 * @param idCota Id da cota.
	 * @return
	 */
	public abstract CotaGarantia getByCota(Long idCota);
	
	/**
	 * Salva no repositorio de dados a garantia de nota promissoria.
	 * @param notaPromissoria Nota Promissoria
	 * @param idCota Id da Cota
	 * @return
	 * @throws RelationshipRestrictionException Caso ocorra um violação de relacionamento na entidade.
	 */
	public abstract CotaGarantiaNotaPromissoria salvaNotaPromissoria(NotaPromissoria notaPromissoria, Long idCota) throws RelationshipRestrictionException;
	
	/**
	 * @return
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	public abstract List<TipoGarantia> obtemTiposGarantiasAceitas();

}
