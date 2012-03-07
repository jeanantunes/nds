package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.repository.ExpedicaoRepository;

@Repository
public class ExposicaoRepositoryImpl extends AbstractRepository<Expedicao, Long> 
implements ExpedicaoRepository{

	public ExposicaoRepositoryImpl() {
		super(Expedicao.class);
	}

}
