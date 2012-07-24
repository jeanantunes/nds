package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;

@Repository
public class TipoDescontoCotaRepositoryImpl extends AbstractRepositoryModel<TipoDescontoCota,Long> implements TipoDescontoCotaRepository {

	public TipoDescontoCotaRepositoryImpl() {
		super(TipoDescontoCota.class);		 
	}

	@Override
	public int obterSequencial() {
		StringBuilder hql = new StringBuilder();

		hql.append(
				" SELECT MAX(tipo.sequencial) FROM TipoDescontoCota as tipo");

		Query query = getSession().createQuery(hql.toString());

		return (Integer) query.uniqueResult();
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
	public Integer buscarTotalDescontoPorCota() {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(desconto) FROM TipoDescontoDistribuidor as desconto  ");		
		
		Query query =  getSession().createQuery(hql.toString());
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	

}
