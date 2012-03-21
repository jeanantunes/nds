package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;

@Repository
public class ControleConferenciaEncalheRepositoryImpl extends AbstractRepository<ControleConferenciaEncalhe,Long> implements ControleConferenciaEncalheRepository {

	public ControleConferenciaEncalheRepositoryImpl() {
		super(ControleConferenciaEncalhe.class);
	}

}
