package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.GrupoRepository;

@Repository
public class GrupoRepositoryImpl extends AbstractRepositoryModel<GrupoCota, Long> implements GrupoRepository {

	public GrupoRepositoryImpl() {
		super(GrupoCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterTodosGrupos() {
		
		Criteria criteria = super.getSession().createCriteria(GrupoCota.class);

		return criteria.list();
	}
	
	@Override
	public Integer countTodosGrupos() {

		Criteria criteria = super.getSession().createCriteria(GrupoCota.class); 
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.list().get(0)).intValue();
	}
	
}
