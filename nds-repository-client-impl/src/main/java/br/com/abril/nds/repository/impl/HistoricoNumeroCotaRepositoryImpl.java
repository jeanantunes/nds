package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.HistoricoNumeroCota;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCotaPK;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoNumeroCotaRepository;

@Repository
public class HistoricoNumeroCotaRepositoryImpl extends AbstractRepositoryModel<HistoricoNumeroCota, HistoricoNumeroCotaPK> implements HistoricoNumeroCotaRepository{

	public HistoricoNumeroCotaRepositoryImpl() {
		super(HistoricoNumeroCota.class);
	}
}
