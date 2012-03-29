package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscalSaida;
import br.com.abril.nds.repository.NotaFiscalSaidaRepository;

@Repository
public class NotaFiscalSaidaRepositoryImpl extends AbstractRepository<NotaFiscalSaida, Long> implements NotaFiscalSaidaRepository {

	public NotaFiscalSaidaRepositoryImpl() {
		super(NotaFiscalSaida.class);
	}

}
