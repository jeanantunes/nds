package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.financeiro.HistoricoInadimplencia;
import br.com.abril.nds.repository.HistoricoInadimplenciaRepository;

public class HistoricoInadimplenciaRepositoryImpl  extends AbstractRepository<HistoricoInadimplencia, Long> implements HistoricoInadimplenciaRepository {

	public HistoricoInadimplenciaRepositoryImpl() {
		super(HistoricoInadimplencia.class);
	}

}
