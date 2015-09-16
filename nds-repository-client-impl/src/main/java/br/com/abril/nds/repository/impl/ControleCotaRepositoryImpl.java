package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.model.cadastro.ControleCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleCotaRepository;

@Repository
public class ControleCotaRepositoryImpl extends AbstractRepositoryModel<ControleCota, Long> implements ControleCotaRepository {
	
	public ControleCotaRepositoryImpl( ) {
		super(ControleCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ControleCotaDTO> buscarControleCota() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT");
		hql.append(" cc.id as id,");
		hql.append(" cc.numeroCotaMaster as numeroCotaMaster, ");
		hql.append(" cc.numeroCota  as numeroCota ,");
		hql.append(" cc.situacao  as situacao ");
		hql.append(" FROM ControleCota as cc ");
		hql.append(" order by cc.id");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ControleCotaDTO.class));
		
		return query.list();
	}

	
	
	
	
	
}
