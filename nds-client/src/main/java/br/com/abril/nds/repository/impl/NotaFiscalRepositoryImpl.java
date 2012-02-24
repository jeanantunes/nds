package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author francisco.garcia
 *
 */
@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}
	
	@Override	
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		this.adicionar(notaFiscal);
		
	}

	
}
