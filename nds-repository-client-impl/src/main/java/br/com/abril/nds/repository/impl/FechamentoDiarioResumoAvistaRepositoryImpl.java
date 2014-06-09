package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioResumoAvistaRepository;

@Repository
public class FechamentoDiarioResumoAvistaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoAvista, Long> implements FechamentoDiarioResumoAvistaRepository {
	
	public FechamentoDiarioResumoAvistaRepositoryImpl() {
		
		super(FechamentoDiarioResumoAvista.class);
	
	}
	
	public BigDecimal obterSaldoAVistaFechamentoDiarioAnterior(Date dataAtual) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select resumoAVista.saldoAtual ")
		.append(" from FechamentoDiarioResumoAvista resumoAVista ")
		.append(" inner join resumoAVista.fechamentoDiario fd ")
		.append(" where fd.dataFechamento < :dataAtual ")
		.append(" order by fd.dataFechamento desc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataAtual", dataAtual);
		
		query.setMaxResults(1);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	public FechamentoDiarioResumoAvista obterResumoConsignado(Date dataFechamento){
		
		String hql = "select f from FechamentoDiarioResumoAvista f where f.fechamentoDiario.dataFechamento=:dataFechamento ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("dataFechamento",dataFechamento);
		query.setMaxResults(1);
		
		return (FechamentoDiarioResumoAvista) query.uniqueResult();
	}
}
