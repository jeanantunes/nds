package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Contrato;
import br.com.abril.nds.repository.ContratoRepository;

@Repository
public class ContratoRepositoryImpl extends AbstractRepositoryModel<Contrato, Long> implements ContratoRepository{

	public ContratoRepositoryImpl() {
		super(Contrato.class);
	}

}
