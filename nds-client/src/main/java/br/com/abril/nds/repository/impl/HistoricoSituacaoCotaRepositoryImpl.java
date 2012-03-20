package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;

@Repository
public class HistoricoSituacaoCotaRepositoryImpl extends AbstractRepository<HistoricoSituacaoCota, Long> implements HistoricoSituacaoCotaRepository{

	public HistoricoSituacaoCotaRepositoryImpl() {
		super(HistoricoSituacaoCota.class);
	}

}
