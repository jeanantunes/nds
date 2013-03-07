package br.com.abril.nds.repository.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RankingFaturamentoIdRepository;
@Repository
public class RankingFaturamentoIdRepositoryImpl extends AbstractRepositoryModel implements	RankingFaturamentoIdRepository {

	public RankingFaturamentoIdRepositoryImpl(){
		super(Object.class);
	}
	
	@Override
	public BigInteger criarNovoIDRanking() {
		StringBuilder hql = new StringBuilder();
		
		hql = new StringBuilder("insert into ranking_faturamento_gerados(data_geracao_rank) values (now()) ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
		
		hql = new StringBuilder("select max(id) lastID from ranking_faturamento_gerados");
		
		query = this.getSession().createSQLQuery(hql.toString());
		Object obj = query.uniqueResult();
		
		return (BigInteger)obj;
	}

}
