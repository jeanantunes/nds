package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioRepository;

@Repository
public class FechamentoDiarioRepositoryImpl extends AbstractRepositoryModel<FechamentoDiario,Long> implements FechamentoDiarioRepository{

	public FechamentoDiarioRepositoryImpl() {
		super(FechamentoDiario.class);
	}
	
	public Date obterDataUltimoFechamento(Date dataFechamento){
		
		String hql = " select max(fechamentoDiario.dataFechamento) from FechamentoDiario fechamentoDiario where fechamentoDiario.dataFechamento < :dataFechamento ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setMaxResults(1);
		
		return (Date) query.uniqueResult();
		
	}
	
	
public FechamentoDiario obterFechamentoDiario(Date dataFechamento){
		
		String hql = " select fechamentoDiario from FechamentoDiario fechamentoDiario where fechamentoDiario.dataFechamento = :dataFechamento ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setMaxResults(1);
		
		return (FechamentoDiario) query.uniqueResult();
		
	}
	
	public Date obterDataUltimoFechamento(){
		
		String hql = " select max(fechamentoDiario.dataFechamento) from FechamentoDiario fechamentoDiario ";
		
		Query query = getSession().createQuery(hql);
		
		query.setMaxResults(1);
		
		return (Date) query.uniqueResult();
		
	}
	
}
