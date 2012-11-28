package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.repository.FechamentoDiarioResumoEstoqueRepository;

@Repository
public class FechamentoDiarioResumoEstoqueRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoEstoque, Long> implements FechamentoDiarioResumoEstoqueRepository {
	
	public FechamentoDiarioResumoEstoqueRepositoryImpl() {
		super(FechamentoDiarioResumoEstoque.class);
	}
}
