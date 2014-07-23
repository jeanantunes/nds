package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

@Repository
public class TipoMovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<TipoMovimentoEstoque, Long> implements TipoMovimentoEstoqueRepository{

	public TipoMovimentoEstoqueRepositoryImpl() {
		super(TipoMovimentoEstoque.class);
	}

	@Override
	public TipoMovimentoEstoque buscarTipoMovimentoEstoque(GrupoMovimentoEstoque grupoMovimentoEstoque) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoEstoque.class);
		
		criteria.add(Restrictions.eq("operacaoEstoque", grupoMovimentoEstoque.getOperacaoEstoque()));
		criteria.add(Restrictions.eq("grupoMovimentoEstoque", grupoMovimentoEstoque));
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoEstoque) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoMovimentoEstoque> buscarTiposMovimentoEstoque(List<GrupoMovimentoEstoque> gruposMovimentoEstoque) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoEstoque.class);
		
		criteria.add(Restrictions.in("grupoMovimentoEstoque", gruposMovimentoEstoque));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarIdTiposMovimentoEstoque(List<GrupoMovimentoEstoque> gruposMovimentoEstoque) {

		Query query = super.getSession().createQuery( " select tm.id from TipoMovimentoEstoque tm "
				+ " where tm.grupoMovimentoEstoque in (:gruposMovimentoEstoque) ");
		
		query.setParameterList("gruposMovimentoEstoque", gruposMovimentoEstoque);
		
		return query.list();
	}

	
}
