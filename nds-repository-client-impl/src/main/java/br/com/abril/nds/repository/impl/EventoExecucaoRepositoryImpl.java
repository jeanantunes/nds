package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucao;
import br.com.abril.nds.model.integracao.InterfaceExecucao;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstrategiaRepository;
import br.com.abril.nds.repository.EventoExecucaoRepository;

@Repository
public class EventoExecucaoRepositoryImpl extends AbstractRepositoryModel<EventoExecucao,Long> implements EventoExecucaoRepository {

    public EventoExecucaoRepositoryImpl() {
	super(EventoExecucao.class);
    }
  
    
    


	@Override
	public EventoExecucao findByNome(String nome) {
		Criteria criteria = getSession().createCriteria(EventoExecucao.class);
		criteria.add(Restrictions.eq("nome", nome));
		return (EventoExecucao) criteria.uniqueResult();
	}
}
