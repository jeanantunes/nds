package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.repository.CFOPRepository;

@Repository
public class CFOPRepositoryImpl  extends AbstractRepository<CFOP, Long> implements CFOPRepository {

	/**
	 * Construtor.
	 */
	public CFOPRepositoryImpl() {
		
		super(CFOP.class);
	}
	
}
