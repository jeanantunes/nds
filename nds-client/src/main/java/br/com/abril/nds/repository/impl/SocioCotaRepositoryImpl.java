package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.repository.SocioCotaRepository;

@Repository
public class SocioCotaRepositoryImpl extends AbstractRepositoryModel<SocioCota,Long> implements SocioCotaRepository {

	public SocioCotaRepositoryImpl() {
		super(SocioCota.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<SocioCota> obterSocioCotaPorIdCota(Long idCota){
		
		Criteria criteria  = getSession().createCriteria(SocioCota.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		return criteria.list();
	}
	
	@Override
	public void removerSociosCota(Long idCota) {
		
		Query query  = getSession().createQuery("delete from SocioCota socio where socio.cota.id =:idCota ");
		
		query.setParameter("idCota", idCota);
		
		query.executeUpdate();
	}
}
