package br.com.abril.nds.repository.impl;


import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;

@Repository
public class DistribuidorRepositoryImpl extends
		AbstractRepository<Distribuidor, Long> implements
		DistribuidorRepository {

	public DistribuidorRepositoryImpl() {
		super(Distribuidor.class);
	}

	@Override
	public Distribuidor obter() {
		String hql = "from Distribuidor";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Distribuidor> distribuidores = query.list();
		return distribuidores.isEmpty() ? null : distribuidores.get(0);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores, 
															OperacaoDistribuidor operacaoDistribuidor) {
		
		StringBuilder hql = 
			new StringBuilder("from DistribuicaoFornecedor where fornecedor.id in (:idsFornecedores) ");
		
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameterList("idsFornecedores", idsForncedores);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistribuicaoDistribuidor> buscarDiasDistribuicaoDistribuidor(
															Long idDistruibuidor,
															OperacaoDistribuidor operacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("from DistribuicaoDistribuidor ");
		hql.append("where distribuidor.id = :idDistribuidor ");
		hql.append("and operacaoDistribuidor = :operacaoDistribuidor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idDistribuidor", idDistruibuidor);
		query.setParameter("operacaoDistribuidor", operacaoDistribuidor);
		
		return query.list();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obterEnderecoPrincipal()
	 */
	@Override
	public EnderecoDistribuidor obterEnderecoPrincipal(){
		Criteria criteria=  getSession().createCriteria(EnderecoDistribuidor.class);
		criteria.add(Restrictions.eq("principal", true) );		
		criteria.setMaxResults(1);
		
		return (EnderecoDistribuidor) criteria.uniqueResult();
	}
	

}
