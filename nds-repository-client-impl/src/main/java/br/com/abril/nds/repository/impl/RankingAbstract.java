package br.com.abril.nds.repository.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;

import br.com.abril.nds.repository.AbstractRepositoryModel;

public abstract class RankingAbstract extends AbstractRepositoryModel{

	public RankingAbstract(){
		super(Object.class);
	}
	
	abstract String getTipoRanking();
	
	public BigInteger criarNovoIDRanking(){
		StringBuilder hql = new StringBuilder();
		
		hql = new StringBuilder("insert into ranking_").append(getTipoRanking()).
				append("_gerados(data_geracao_rank) values (now()) ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
		
		
		hql = new StringBuilder("select max(id) lastID from ranking_").append(getTipoRanking()).append("_gerados");
		
		query = this.getSession().createSQLQuery(hql.toString());
		Object obj = query.uniqueResult();
		
		
		return (BigInteger)obj;
	}
	
}
