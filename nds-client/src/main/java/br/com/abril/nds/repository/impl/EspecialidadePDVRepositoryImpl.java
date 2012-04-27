package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.repository.EspecialidadePDVRepository;

@Repository
public class EspecialidadePDVRepositoryImpl extends AbstractRepository<EspecialidadePDV,Long>implements EspecialidadePDVRepository {
	
	public EspecialidadePDVRepositoryImpl() {
		super(EspecialidadePDV.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EspecialidadePDV> obterEspecialidades(Long... codigos ){
		
		Criteria criteria = super.getSession().createCriteria(EspecialidadePDV.class);
		
		criteria.add(Restrictions.in("codigo", codigos));
		
		return criteria.list();
	}
}
