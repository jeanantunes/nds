package br.com.abril.nds.repository.impl;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;

@Repository
public class BoletoDistribuidorRepositoryImpl extends AbstractRepositoryModel<BoletoDistribuidor, Long> implements BoletoDistribuidorRepository {
	
	public BoletoDistribuidorRepositoryImpl() {
		super(BoletoDistribuidor.class);
	}

	public BoletoDistribuidor obterBoletoDistribuidorPorChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select b from BoletoDistribuidor b ")
		.append(" where b.chamadaEncalheFornecedor.id = :idChamadaEncalheFornecedor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idChamadaEncalheFornecedor", idChamadaEncalheFornecedor);
		
		return (BoletoDistribuidor) query.uniqueResult();
		
	}
	
}
