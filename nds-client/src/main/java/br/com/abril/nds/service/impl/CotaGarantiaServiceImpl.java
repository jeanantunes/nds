package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.service.CotaGarantiaService;

/**
 * 
 * @author Diego Fernandes
 *
 */
@Service
public class CotaGarantiaServiceImpl implements CotaGarantiaService {
	
	
	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvaImoveis(br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel)
	 */
	@Override
	@Transactional
	public void salvaImoveis(CotaGarantiaImovel cotaGarantiaImovel) {
		
		cotaGarantiaRepository.merge(cotaGarantiaImovel);
	}

}
