package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.repository.DescontoLogisticaRepository;

@Repository
public class DescontoLogisticaRepositoryImpl extends AbstractRepositoryModel<DescontoLogistica, Long> implements DescontoLogisticaRepository {

	public DescontoLogisticaRepositoryImpl() {
		super(DescontoLogistica.class);
	}

}
