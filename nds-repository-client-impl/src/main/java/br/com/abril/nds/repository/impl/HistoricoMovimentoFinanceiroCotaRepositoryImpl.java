package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;

@Repository
public class HistoricoMovimentoFinanceiroCotaRepositoryImpl 
										extends AbstractRepositoryModel<HistoricoMovimentoFinanceiroCota, Long> 
										implements HistoricoMovimentoFinanceiroCotaRepository {
	
	public HistoricoMovimentoFinanceiroCotaRepositoryImpl() {
		super(HistoricoMovimentoFinanceiroCota.class);
	}
}
