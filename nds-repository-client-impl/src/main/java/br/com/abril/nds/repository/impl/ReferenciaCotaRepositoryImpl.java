package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ReferenciaCotaRepository;

@Repository
public class ReferenciaCotaRepositoryImpl extends AbstractRepositoryModel<ReferenciaCota, Long> implements ReferenciaCotaRepository {

	public ReferenciaCotaRepositoryImpl() {
		super(ReferenciaCota.class);
	}
	
	@Override
	public void excluirReferenciaCota(Long idBaseReferencia) {
		
		Query query = getSession().createQuery(" delete from ReferenciaCota referencia where referencia.baseReferenciaCota.id = :idBaseReferencia ");
		
		query.setParameter("idBaseReferencia",idBaseReferencia);
		
		query.executeUpdate();
	}
}
