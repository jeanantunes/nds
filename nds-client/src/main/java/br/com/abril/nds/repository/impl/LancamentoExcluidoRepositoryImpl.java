package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.LancamentoExcluido;
import br.com.abril.nds.repository.LancamentoExcluidoRepository;

@Repository
public class LancamentoExcluidoRepositoryImpl extends AbstractRepositoryModel<LancamentoExcluido, Long> 
											  implements LancamentoExcluidoRepository {

	public LancamentoExcluidoRepositoryImpl() {
		super(LancamentoExcluido.class);
	}
}