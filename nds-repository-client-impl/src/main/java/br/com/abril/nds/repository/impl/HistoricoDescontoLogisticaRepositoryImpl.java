package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoLogisticaRepository;

@Repository
public class HistoricoDescontoLogisticaRepositoryImpl 
		extends AbstractRepositoryModel<HistoricoDescontoLogistica, Long> implements HistoricoDescontoLogisticaRepository {

	public HistoricoDescontoLogisticaRepositoryImpl() {
		super(HistoricoDescontoLogistica.class);
	}
	
	public HistoricoDescontoLogistica obterHistoricoDesconto(Integer tipoDesconto, Date inicioVigenciaDesconto){
				
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select hs from HistoricoDescontoLogistica hs ")
			.append(" where hs.tipoDesconto =:tipoDesconto ")
			.append(" and hs.dataInicioVigencia=:dataInicioVigencia ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("tipoDesconto",tipoDesconto);
		query.setParameter("dataInicioVigencia", inicioVigenciaDesconto);
		query.setMaxResults(1);
		
		return (HistoricoDescontoLogistica) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoDescontoLogistica> obterProximosDescontosVigente(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select hs from HistoricoDescontoLogistica hs  ")
			.append(" where hs.dataInicioVigencia <= :dataInicioVigencia")
			.append(" and hs.dataProcessamento is null ")
			.append(" order by hs.tipoDesconto, hs.dataInicioVigencia  ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("dataInicioVigencia", dataOperacao);
		
		return query.list();
	}

}
