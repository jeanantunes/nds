package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.repository.NotaEnvioRepository;

@Repository
public class NotaEnvioRepositoryImpl  extends AbstractRepositoryModel<NotaEnvio, Long> implements NotaEnvioRepository{

	public NotaEnvioRepositoryImpl() {
		super(NotaEnvio.class);
	}

}
