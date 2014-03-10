package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProdutoFila;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;

@Repository
public class EstoqueProdutoFilaRepositoryImpl extends AbstractRepositoryModel<EstoqueProdutoFila, Long> implements EstoqueProdutoFilaRepository {

	public EstoqueProdutoFilaRepositoryImpl() {
		super(EstoqueProdutoFila.class);
	}
	
	@Override
	public List<EstoqueProdutoFila> buscarEstoqueProdutoFilaDaCota(Long idCota) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoFila ");
		hql.append(" from EstoqueProdutoFila estoqueProdutoFila ");
		hql.append(" join estoqueProdutoFila.cota cota			");
		hql.append(" join estoqueProdutoFila.produtoEdicao produtoEdicao ");
		hql.append(" where cota.id = :idCota 					");
		hql.append(" order by produtoEdicao.id desc 			");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return query.list();
		
	}

	@Override
	public List<EstoqueProdutoFila> buscarEstoqueProdutoFilaNumeroCota(Integer numeroCota) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoFila ");
		hql.append(" from EstoqueProdutoFila estoqueProdutoFila ");
		hql.append(" join estoqueProdutoFila.cota cota			");
		hql.append(" join estoqueProdutoFila.produtoEdicao produtoEdicao  ");
		hql.append(" where cota.numeroCota = :numeroCota 				  ");
		hql.append(" order by produtoEdicao.id desc 			");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setLockOptions(LockOptions.UPGRADE);
		
		return query.list();
		
	}
	
}