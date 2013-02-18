package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscalSaida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalSaidaRepository;

@Repository
public class NotaFiscalSaidaRepositoryImpl extends AbstractRepositoryModel<NotaFiscalSaida, Long> implements NotaFiscalSaidaRepository {

	public NotaFiscalSaidaRepositoryImpl() {
		super(NotaFiscalSaida.class);
	}

}
