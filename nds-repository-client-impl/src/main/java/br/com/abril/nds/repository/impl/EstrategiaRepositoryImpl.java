package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;

public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<Estrategia,Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
	super(Estrategia.class);
    }
}
