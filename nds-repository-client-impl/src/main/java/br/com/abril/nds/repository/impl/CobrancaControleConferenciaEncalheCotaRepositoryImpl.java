package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.CobrancaControleConferenciaEncalheCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;

@Repository
public class CobrancaControleConferenciaEncalheCotaRepositoryImpl extends AbstractRepositoryModel<CobrancaControleConferenciaEncalheCota, Long> implements CobrancaControleConferenciaEncalheCotaRepository {

	public CobrancaControleConferenciaEncalheCotaRepositoryImpl() {
		super(CobrancaControleConferenciaEncalheCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CobrancaControleConferenciaEncalheCota> obterCobrancaControleConferenciaEncalheCota(Long idControleConferenciaEncalheCota) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select c  ")
		.append(" from CobrancaControleConferenciaEncalheCota c ")
		.append(" where ")
		.append(" c.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
		
	}
	
	@Override
	public void excluirPorCobranca(Long idCobranca){
		
		Query query = 
				this.getSession().createQuery(
						"delete from CobrancaControleConferenciaEncalheCota c where c.cobranca.id = :idCobranca");
		
		query.setParameter("idCobranca", idCobranca);
		
		query.executeUpdate();
		
		getSession().flush();
		
	}
	
}
