package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.exception.RelationshipRestrictionException;

/**
 * 
 * @author Diego Fernandes
 * 
 */
@Service
public class CotaGarantiaServiceImpl implements CotaGarantiaService {

	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.CotaGarantiaService#salva(CotaGarantia)
	 */
	@Override
	@Transactional
	public CotaGarantia salva(CotaGarantia entity) {

		// TODO: validações para substituição da cota.
		return cotaGarantiaRepository.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#getByCota(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public CotaGarantia getByCota(Long idCota) {
		return cotaGarantiaRepository.getByCota(idCota);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#salvaNotaPromissoria(br.
	 * com.abril.nds.model.cadastro.NotaPromissoria, java.lang.Long)
	 */
	@Override
	@Transactional
	public CotaGarantiaNotaPromissoria salvaNotaPromissoria(
			NotaPromissoria notaPromissoria, Long idCota) throws RelationshipRestrictionException {
		
		CotaGarantiaNotaPromissoria cotaGarantiaNota = (CotaGarantiaNotaPromissoria) cotaGarantiaRepository.getByCota(idCota);		
		
		if(cotaGarantiaNota == null){
			cotaGarantiaNota =  new CotaGarantiaNotaPromissoria();
		}
		
		Cota cota =  cotaRepository.buscarPorId(idCota);
		if(cota == null ){
			throw new RelationshipRestrictionException("Cota " + idCota+ " não encotrada.");
		}
		
		cotaGarantiaNota.setCota(cota);
		cotaGarantiaNota.setData(new Date());
		
		cotaGarantiaNota.setNotaPromissoria(notaPromissoria);
		
		return (CotaGarantiaNotaPromissoria) cotaGarantiaRepository.merge(cotaGarantiaNota);
	}

	/**
	 * @return
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {
		return distribuidorRepository.obtemTiposGarantiasAceitas();
	}
	
	
	

}
