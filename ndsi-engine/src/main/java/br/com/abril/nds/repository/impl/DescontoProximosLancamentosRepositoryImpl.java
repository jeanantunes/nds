package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;

@Repository
public class DescontoProximosLancamentosRepositoryImpl extends AbstractRepositoryModel<DescontoProximosLancamentos,Long> 
implements DescontoProximosLancamentosRepository{

	public DescontoProximosLancamentosRepositoryImpl() {
		super(DescontoProximosLancamentos.class);
	}

}
