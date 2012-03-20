package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author Discover Technology
 *
 */
@Repository
public class ConferenciaEncalheParcialRepositoryImpl extends AbstractRepository<ConferenciaEncalheParcial, Long> implements ConferenciaEncalheParcialRepository {

	public ConferenciaEncalheParcialRepositoryImpl() {
		super(ConferenciaEncalheParcial.class);
	}
	

	
}
