package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TransportadorRepository;

@Repository
public class TransportadorRepositoryImpl extends
		AbstractRepository<Transportador, Long> implements TransportadorRepository {

	public TransportadorRepositoryImpl() {
		super(Transportador.class);
	}
}