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
		.append(" where fd.dataCriacao < :dataAtual ")
		.append(" order by fd.dataCriacao desc limit 1 ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataAtual", dataAtual);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	
}
