package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.VeiculoRepository;

@Repository
public class VeiculoRepositoryImpl extends AbstractRepositoryModel<Veiculo, Long>
		implements VeiculoRepository {

	public VeiculoRepositoryImpl() {
		super(Veiculo.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Veiculo> buscarVeiculosPorTransportador(Long idTransportador, Set<Long> idsIgnorar, 
			String sortname, String sortorder) {
		
		Criteria criteria = this.getSession().createCriteria(Veiculo.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			criteria.add(Restrictions.not(Restrictions.in("id", idsIgnorar)));
		}
		
		String property = null;
		
		if ("placa".equals(sortname)){
			
			property = "placa";
		} else {
			
			property = "tipoVeiculo";
		}
		
		if ("asc".equals(sortorder)){
			
			criteria.addOrder(Order.asc(property));
		} else {
			
			criteria.addOrder(Order.desc(property));
		}
		
		return criteria.list();
	}

	@Override
	public void removerPorId(Long idVeiculo) {
		
		Query query = this.getSession().createQuery("delete from Veiculo where id = :idVeiculo");
		query.setParameter("idVeiculo", idVeiculo);
		
		query.executeUpdate();
	}

	@Override
	public void removerVeiculos(Long idTransportador, Set<Long> listaVeiculosRemover) {
		
		StringBuilder hql = new StringBuilder("delete from Veiculo ");
		hql.append(" where transportador.id = :idTransportador ");
		
		if (listaVeiculosRemover != null && !listaVeiculosRemover.isEmpty()){
			
			hql.append(" and id in (:listaVeiculosRemover) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTransportador", idTransportador);
		
		if (listaVeiculosRemover != null && !listaVeiculosRemover.isEmpty()){
			
			query.setParameterList("listaVeiculosRemover", listaVeiculosRemover);
		}
		
		query.executeUpdate();
	}
}