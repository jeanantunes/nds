package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements NotaFiscalRepository {
	
	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}
	
}
