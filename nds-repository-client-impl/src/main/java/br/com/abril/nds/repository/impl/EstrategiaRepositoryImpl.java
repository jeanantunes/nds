package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;

@Repository
public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<Estrategia,Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
	super(Estrategia.class);
    }
}
