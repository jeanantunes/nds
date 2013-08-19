package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoInadimplenciaRepository;

public class HistoricoInadimplenciaRepositoryImpl  extends AbstractRepositoryModel<HistoricoAcumuloDivida, Long> implements HistoricoInadimplenciaRepository {

	public HistoricoInadimplenciaRepositoryImpl() {
		super(HistoricoAcumuloDivida.class);
	}

}
