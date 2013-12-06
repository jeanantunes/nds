package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoCotaRepository;

@Repository
public class FechamentoDiarioConsolidadoCotaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoCota, Long> implements FechamentoDiarioConsolidadoCotaRepository {
	
	public FechamentoDiarioConsolidadoCotaRepositoryImpl() {
		super(FechamentoDiarioConsolidadoCota.class);
	}
	
	public FechamentoDiarioConsolidadoCota obterResumoConsolidadoCotas(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("  select c ")
			.append(" from FechamentoDiarioConsolidadoCota c  ")
			.append(" where c.fechamentoDiario.dataFechamento=:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setMaxResults(1);
		
		return (FechamentoDiarioConsolidadoCota) query.uniqueResult();
	}
}
