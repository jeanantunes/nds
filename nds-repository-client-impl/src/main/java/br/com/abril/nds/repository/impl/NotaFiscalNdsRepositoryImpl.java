package br.com.abril.nds.repository.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalNdsRepositoryImpl extends AbstractRepositoryModel<NotaFiscalNds, Long> implements NotaFiscalNdsRepository {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	public NotaFiscalNdsRepositoryImpl() {
		super(NotaFiscalNds.class);
	}	
	
}
