package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioResumoEstoqueRepository;

@Repository
public class FechamentoDiarioResumoEstoqueRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoEstoque, Long> implements FechamentoDiarioResumoEstoqueRepository {
	
	public FechamentoDiarioResumoEstoqueRepositoryImpl() {
		super(FechamentoDiarioResumoEstoque.class);
	}
	
	public FechamentoDiarioResumoEstoque obterResumoEstoque(Date dataFechamento, TipoEstoque tipoEstoque){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f from FechamentoDiarioResumoEstoque f ")
			.append(" where f.tipoEstoque=:tipoEstoque ")
			.append(" and f.fechamentoDiario.dataFechamento=:dataFechamento ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setParameter("tipoEstoque", tipoEstoque);
		
		query.setMaxResults(1);
		
		return (FechamentoDiarioResumoEstoque) query.uniqueResult();
	}
}
