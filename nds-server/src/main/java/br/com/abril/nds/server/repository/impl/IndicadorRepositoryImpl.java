package br.com.abril.nds.server.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.impl.AbstractRepositoryModel;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.repository.IndicadorRepository;

@Repository
public class IndicadorRepositoryImpl extends AbstractRepositoryModel<Indicador, Long> implements
		IndicadorRepository {

	public IndicadorRepositoryImpl() {
		super(Indicador.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Indicador> buscarIndicadores() {
		
		Criteria criteria = this.getSession().createCriteria(Indicador.class);
		criteria.add(Restrictions.eq("data", new Date()));
		
		criteria.createAlias("operacaoDistribuidor", "distribuidor");
		
		criteria.addOrder(Order.asc("distribuidor.uf"));
		criteria.addOrder(Order.asc("distribuidor.idDistribuidorInterface"));
		criteria.addOrder(Order.asc("grupoIndicador"));
		criteria.addOrder(Order.asc("tipoIndicador"));
		
		return criteria.list();
	}
}