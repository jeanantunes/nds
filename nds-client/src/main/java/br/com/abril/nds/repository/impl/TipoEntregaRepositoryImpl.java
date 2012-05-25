package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;

@Repository
public class TipoEntregaRepositoryImpl extends AbstractRepository<TipoEntrega, Long> implements TipoEntregaRepository {

	public TipoEntregaRepositoryImpl() {
		super(TipoEntrega.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoEntrega> pesquisarTiposEntrega(Long codigo,
			String descricao, String periodicidade, String sortname,
			String sortorder, int page, int rp) {

		Criteria criteria = getTipoEntregaCriteria(codigo, descricao, periodicidade);

		this.addOrdenacao(criteria, sortname, sortorder);
		
		criteria.setFirstResult(page);
		criteria.setMaxResults(rp);
		
		return criteria.list();
	}

	@Override
	public Integer pesquisarQuantidadeTiposEntrega(Long codigo,
			String descricao, String periodicidade) {
		
		Criteria criteria = getTipoEntregaCriteria(codigo, descricao, periodicidade);
		
		criteria.setProjection(Projections.rowCount());
		
		Long total = (Long) criteria.list().get(0);
		
		return total.intValue();
	}
	
	private Criteria getTipoEntregaCriteria(Long codigo, String descricao, String periodicidade) {
		
		Criteria criteria = getSession().createCriteria(TipoEntrega.class);
				
		if (codigo != null) {
			criteria.add(Restrictions.eq("id", codigo));
		}
		
		if (descricao != null && !descricao.isEmpty()) {
			criteria.add(Restrictions.ilike("descricao", descricao));
		}
		
		if (periodicidade != null && !periodicidade.isEmpty()) {
			criteria.add(Restrictions.eq("periodicidade", getPeriodicidade(periodicidade)));
		}
		
		return criteria;
	}
	
	private void addOrdenacao(Criteria criteria, String sortname, String sortorder) {
		
		if ("asc".equals(sortorder)) {
			criteria.addOrder(Order.asc(sortname));
		} else if ("desc".equals(sortorder)) {
			criteria.addOrder(Order.desc(sortname));
		}
	}

	private Periodicidade getPeriodicidade(String value) {
		
		for (Periodicidade periodicidade : Periodicidade.values()) {

			if (periodicidade.getValue().equals(value)) {
				return periodicidade;
			}
		}
		
		return null;
	}

}
