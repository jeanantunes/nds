package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;

@Repository
public class HistoricoLancamentoRepositoryImpl extends AbstractRepositoryModel<HistoricoLancamento, Long>
		implements HistoricoLancamentoRepository {

	public HistoricoLancamentoRepositoryImpl() {
		super(HistoricoLancamento.class);
	}
}
