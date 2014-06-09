package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;

@Repository
public class TipoGeradorFluxoPDVRepositoryImpl extends AbstractRepositoryModel<TipoGeradorFluxoPDV,Long> implements TipoGeradorFluxoPDVRepsitory {
	
	public TipoGeradorFluxoPDVRepositoryImpl() {
		super(TipoGeradorFluxoPDV.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long... codigos) {
		
		Criteria criteria = super.getSession().createCriteria(TipoGeradorFluxoPDV.class);
		
		criteria.add(Restrictions.in("codigo", codigos));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(Long... codigos ){
		
		Criteria criteria = super.getSession().createCriteria(TipoGeradorFluxoPDV.class);
		
		criteria.add(Restrictions.not(Restrictions.in("codigo", codigos)));
		
		return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoGeradorFluxoPDV> obterTodosTiposGeradorFluxo() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from TipoGeradorFluxoPDV order by descricao");
		
		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoOrdenado() {
		
		Criteria criteria = super.getSession().createCriteria(TipoGeradorFluxoPDV.class);
		
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}

}
