package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.HistoricoConfChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoConfChamadaEncalheCotaRepository;

@Repository
public class HistoricoConfChamadaEncalheCotaRepositoryImpl extends AbstractRepositoryModel<HistoricoConfChamadaEncalheCota,Long> implements HistoricoConfChamadaEncalheCotaRepository  {

	/**
	 * Construtor padrão
	 */
	public HistoricoConfChamadaEncalheCotaRepositoryImpl() {
		super(HistoricoConfChamadaEncalheCota.class);
	}
	
	@Override
	public Long adicionar(HistoricoConfChamadaEncalheCota entity) {
		return super.adicionar(entity);
	}
	
}
