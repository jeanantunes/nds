package br.com.abril.nds.repository.impl;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.repository.BaixaAutomaticaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.BaixaAutomatica}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BaixaAutomaticaRepositoryImpl extends AbstractRepository<BaixaAutomatica,Long> implements BaixaAutomaticaRepository {

	
	/**
	 * Construtor padrão
	 */
	public BaixaAutomaticaRepositoryImpl() {
		super(BaixaAutomatica.class);
	}

}
