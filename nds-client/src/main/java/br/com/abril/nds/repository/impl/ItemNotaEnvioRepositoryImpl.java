package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;

@Repository
public class ItemNotaEnvioRepositoryImpl extends AbstractRepositoryModel<ItemNotaEnvio, ItemNotaEnvioPK> implements ItemNotaEnvioRepository {

	public ItemNotaEnvioRepositoryImpl() {
		super(ItemNotaEnvio.class);
	}

	
}
