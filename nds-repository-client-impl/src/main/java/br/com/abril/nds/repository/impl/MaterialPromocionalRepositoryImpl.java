package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MaterialPromocionalRepository;

@Repository
public class MaterialPromocionalRepositoryImpl extends AbstractRepositoryModel<MaterialPromocional, Long> implements MaterialPromocionalRepository {

	public MaterialPromocionalRepositoryImpl() {
		super(MaterialPromocional.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MaterialPromocional> obterMateriaisPromocional(Long...codigos){
		
		Criteria criteria = super.getSession().createCriteria(MaterialPromocional.class);
		
		criteria.add(Restrictions.in("codigo", codigos));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<MaterialPromocional> obterMateriaisPromocionalNotIn(Long...codigos){
		
		Criteria criteria = super.getSession().createCriteria(MaterialPromocional.class);
		
		criteria.add(Restrictions.not(Restrictions.in("codigo", codigos)));
		
		return criteria.list();
	}
}
