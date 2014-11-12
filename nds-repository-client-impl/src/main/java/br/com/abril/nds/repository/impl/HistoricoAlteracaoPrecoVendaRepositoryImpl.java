package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.HistoricoAlteracaoPrecoVenda;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoAlteracaoPrecoVendaRepository;

@Repository
public class HistoricoAlteracaoPrecoVendaRepositoryImpl extends AbstractRepositoryModel<HistoricoAlteracaoPrecoVenda, Long> implements HistoricoAlteracaoPrecoVendaRepository {
    
    public HistoricoAlteracaoPrecoVendaRepositoryImpl() {
        super(HistoricoAlteracaoPrecoVenda.class);
    }
    
}