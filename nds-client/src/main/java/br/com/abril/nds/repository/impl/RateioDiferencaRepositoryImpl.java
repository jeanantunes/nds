package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.repository.RateioDiferencaRepository;

@Repository
public class RateioDiferencaRepositoryImpl extends AbstractRepository<RateioDiferenca, Long>
		implements RateioDiferencaRepository {

	public RateioDiferencaRepositoryImpl() {
		super(RateioDiferenca.class);
	}

	@Override
	public boolean verificarExistenciaRateioDiferenca(Long idDiferenca) {
		StringBuilder hql = new StringBuilder("select count(rateioDiferenca.id) ");
		hql.append(" from RateioDiferenca reateioDiferenca, Diferenca diferenca ")
		   .append(" where rateioDiferenca.diferenca.id = diferenca.id ")
		   .append(" and diferenca.id = :idDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		
		return ((Long)query.uniqueResult()) > 0;
	}
	
	public RateioDiferenca obterRateioDiferencaPorDiferenca(Long idDiferenca){
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RateioDiferenca.class.getCanonicalName())
		   .append(" (rateioDiferenca, rateioDiferenca.cota) ")
		   .append(" from RateioDiferenca rateioDiferenca, Diferenca diferenca ")
		   .append(" where rateioDiferenca.diferenca.id = diferenca.id ")
		   .append(" and diferenca.id = :idDiferenca ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idDiferenca", idDiferenca);
		query.setMaxResults(1);
		
		return ((RateioDiferenca)query.uniqueResult());
	}
}