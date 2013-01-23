package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.repository.FixacaoReparteRepository;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * FixacaoReparte
 */

@Repository
public class FixacaoReparteRepositoryImpl extends  AbstractRepositoryModel<FixacaoReparte, Long> implements FixacaoReparteRepository {
 
	public FixacaoReparteRepositoryImpl() {
		super(FixacaoReparte.class);
	}
}
