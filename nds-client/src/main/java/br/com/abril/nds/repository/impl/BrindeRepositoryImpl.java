package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.repository.BrindeRepository;

@Repository
public class BrindeRepositoryImpl extends AbstractRepositoryModel<Brinde,Long> implements BrindeRepository  {

	/**
	 * Construtor padrão
	 */
	public BrindeRepositoryImpl() {
		super(Brinde.class);
	}
}
