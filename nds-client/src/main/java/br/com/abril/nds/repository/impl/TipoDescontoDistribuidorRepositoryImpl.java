package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.TipoDescontoDistribuidorRepository;

@Repository
public class TipoDescontoDistribuidorRepositoryImpl extends AbstractRepositoryModel<TipoDescontoDistribuidor,Long> implements	TipoDescontoDistribuidorRepository {

	public TipoDescontoDistribuidorRepositoryImpl() {
		super(TipoDescontoDistribuidor.class);
	}

	@Override
	public int obterSequencial() {		
		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT MAX(tipo.sequencial) FROM TipoDescontoDistribuidor as tipo");

		Query query = getSession().createQuery(hql.toString());
		
		return (Integer) ((query.uniqueResult() == null) ? 0 : query.uniqueResult()); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDistribuidor> obterTipoDescontosDistribuidor() {
		
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT tipoDistribuidor FROM TipoDescontoDistribuidor as tipoDistribuidor ");

		Query query = getSession().createQuery(hql.toString());		
		
		return query.list();
	}

	@Override
	public Integer buscarTotalDescontosDistribuidor() {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(desconto) FROM TipoDescontoDistribuidor as desconto  ");		
		
		Query query =  getSession().createQuery(hql.toString());
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}


}
