package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.estoque.LancamentoDiferenca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class LancamentoDiferencaRepositoryImpl extends AbstractRepositoryModel<LancamentoDiferenca, Long> implements LancamentoDiferencaRepository {

	public LancamentoDiferencaRepositoryImpl() {
		
		super(LancamentoDiferenca.class);
	}

}
