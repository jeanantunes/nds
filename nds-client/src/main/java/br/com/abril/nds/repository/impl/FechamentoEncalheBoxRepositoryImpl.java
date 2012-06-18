package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;

@Repository
public class FechamentoEncalheBoxRepositoryImpl extends AbstractRepository<FechamentoEncalheBox, FechamentoEncalheBoxPK> implements FechamentoEncalheBoxRepository {

	public FechamentoEncalheBoxRepositoryImpl() {
		super(FechamentoEncalheBox.class);
	}
	

}
