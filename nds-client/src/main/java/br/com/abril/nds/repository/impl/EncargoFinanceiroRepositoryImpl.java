package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.repository.EncargoFinanceiroRepository;

@Repository
public class EncargoFinanceiroRepositoryImpl extends AbstractRepositoryModel<EncargoFinanceiro, Long> implements EncargoFinanceiroRepository{

	public EncargoFinanceiroRepositoryImpl() {
		super(EncargoFinanceiro.class);
	}

}
