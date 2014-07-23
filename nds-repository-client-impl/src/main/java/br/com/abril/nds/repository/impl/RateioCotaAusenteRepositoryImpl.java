package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.RateioCotaAusente;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RateioCotaAusenteRepository;

@Repository
public class RateioCotaAusenteRepositoryImpl extends AbstractRepositoryModel<RateioCotaAusente, Long> implements RateioCotaAusenteRepository {

	/**
	 * Construtor.
	 */
	public RateioCotaAusenteRepositoryImpl() {
		
		super( RateioCotaAusente.class);
	}
}
