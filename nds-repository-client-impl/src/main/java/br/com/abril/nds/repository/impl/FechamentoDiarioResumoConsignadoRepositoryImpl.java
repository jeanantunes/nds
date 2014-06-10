package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsignadoRepository;

@Repository
public class FechamentoDiarioResumoConsignadoRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoConsignado, Long> implements FechamentoDiarioResumoConsignadoRepository {

	public FechamentoDiarioResumoConsignadoRepositoryImpl() {
	
		super(FechamentoDiarioResumoConsignado.class);
	}
	
	public BigDecimal obterSaldoConsignadoFechamentoDiarioAnterior(Date dataAtual) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select resumoConsignado.saldoAtual ")
		.append(" from FechamentoDiarioResumoConsignado resumoConsignado ")
		.append(" inner join resumoConsignado.fechamentoDiario fd ")
		.append(" where fd.dataFechamento < :dataAtual ")
		.append(" order by fd.dataFechamento desc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataAtual", dataAtual);
		
		query.setMaxResults(1);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	public FechamentoDiarioResumoConsignado obterResumoConsignado(Date dataFechamento){
		
		String hql = "select f from FechamentoDiarioResumoConsignado f where f.fechamentoDiario.dataFechamento=:dataFechamento ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("dataFechamento",dataFechamento);
		query.setMaxResults(1);
		
		return (FechamentoDiarioResumoConsignado) query.uniqueResult();
	}
}
