package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.repository.RomaneioRepository;

public class RomaneioRepositoryImpl extends AbstractRepository<Box, Long> implements RomaneioRepository {

	public RomaneioRepositoryImpl() {
		super(Box.class);
	}
	

	

}
