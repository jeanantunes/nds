package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;

@Repository
public class AreaInfluenciaPDVRepositoryImpl extends AbstractRepositoryModel<AreaInfluenciaPDV, Long> implements AreaInfluenciaPDVRepository {

	public AreaInfluenciaPDVRepositoryImpl() {
		super(AreaInfluenciaPDV.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaInfluenciaPDV> obterAreaInfluenciaPDV(Long...codigos ){
		
		Criteria criteria = getSession().createCriteria(AreaInfluenciaPDV.class);
		
		criteria.add(Restrictions.in("codigo", codigos));
		
		return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AreaInfluenciaPDV> obterTodasAreaInfluenciaPDV() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from AreaInfluenciaPDV order by descricao");
		
		Query query = getSession().createQuery(hql.toString());
		
		return query.list();
	}
}
