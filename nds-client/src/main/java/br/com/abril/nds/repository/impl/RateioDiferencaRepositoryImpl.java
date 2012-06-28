package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.repository.RateioDiferencaRepository;

@Repository
public class RateioDiferencaRepositoryImpl extends AbstractRepositoryModel<RateioDiferenca, Long>
		implements RateioDiferencaRepository {

	public RateioDiferencaRepositoryImpl() {
		super(RateioDiferenca.class);
	}
	
	public RateioDiferenca obterRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RateioDiferenca.class.getCanonicalName())
		   .append(" (rateioDiferenca, rateioDiferenca.cota, rateioDiferenca.estudoCota) ")
		   .append(" from RateioDiferenca rateioDiferenca, Diferenca diferenca ")
		   .append(" where rateioDiferenca.diferenca.id = diferenca.id ")
		   .append(" and diferenca.id = :idDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		query.setMaxResults(1);
		
		return ((RateioDiferenca)query.uniqueResult());
	}
	
	public void removerRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder();
		hql.append("delete from RateioDiferenca r where r.diferenca.id = :idDiferenca");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		
		query.executeUpdate();
	}
}