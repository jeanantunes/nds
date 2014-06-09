package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.GarantiaRepository;

@Repository
public class GarantiaRepositoryImpl extends AbstractRepositoryModel<Garantia, Long> implements GarantiaRepository {

	public GarantiaRepositoryImpl() {
		super(Garantia.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Garantia> obterGarantiasFiador(Long idFiador, Set<Long> idsIgnorar) {
		Criteria criteria = this.getSession().createCriteria(Garantia.class);
		criteria.add(Restrictions.eq("fiador.id", idFiador));
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			criteria.add(Restrictions.not(Restrictions.in("id", idsIgnorar)));
		}
		
		return criteria.list();
	}
	
	public void removerGarantias(Set<Long> idsGarantias){
		
		Query query = this.getSession().createQuery("delete from Garantia where id in (:idsGarantias) ");
		query.setParameterList("idsGarantias", idsGarantias);
		
		query.executeUpdate();
	}
	
	public void removerGarantiasPorFiador(Long idFiador){
		
		Query query = this.getSession().createQuery("delete from Garantia where fiador.id = :idFiador ");
		query.setParameter("idFiador", idFiador);
		
		query.executeUpdate();
	}
}