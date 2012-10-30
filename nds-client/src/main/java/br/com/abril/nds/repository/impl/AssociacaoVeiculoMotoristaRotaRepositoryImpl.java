package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;

@Repository
public class AssociacaoVeiculoMotoristaRotaRepositoryImpl extends
		AbstractRepositoryModel<AssociacaoVeiculoMotoristaRota, Long> implements
		AssociacaoVeiculoMotoristaRotaRepository {

	public AssociacaoVeiculoMotoristaRotaRepositoryImpl() {
		super(AssociacaoVeiculoMotoristaRota.class);
	}

	@Override
	public void removerAssociacaoPorId(Set<Long> ids) {
		
		Query query = this.getSession().createQuery("delete from AssociacaoVeiculoMotoristaRota where id in (:ids)");
		
		query.setParameterList("ids", ids);
		
		query.executeUpdate();
	}

	@Override
	public void removerAssociacaoTransportador(Long id) {
		
		Query query = this.getSession().createQuery(
				"delete from AssociacaoVeiculoMotoristaRota where transportador.id = :id");
		query.setParameter("id", id);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssociacaoVeiculoMotoristaRota> buscarAssociacoesTransportador(
			Long idTransportador, Set<Long> idsIgnorar, String sortname, String sortorder) {
		
		Criteria criteria = this.getSession().createCriteria(AssociacaoVeiculoMotoristaRota.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			criteria.add(Restrictions.not(Restrictions.in("id", idsIgnorar)));
		}
		
		String property = null;
		
		if ("roteiro".equals(sortname)){
			
			criteria.createAlias("rota.roteiro", "roteiro");
			property = "roteiro.descricaoRoteiro";
		} else if ("placa".equals(sortname)){
			
			criteria.createAlias("veiculo", "veiculo");
			property = "veiculo.placa";
		} else if ("motorista".equals(sortname)){
			
			criteria.createAlias("motorista", "motorista");
			property = "motorista.nome";
		} else if ("cnh".equals(sortname)){
			
			criteria.createAlias("motorista", "motorista");
			property = "motorista.cnh";
		} else if ("rota".equals(sortname)){
			
			criteria.createAlias("rota", "rota");
			property = "rota.descricaoRota";
		} else {
			
			criteria.createAlias("veiculo", "veiculo");
			property = "veiculo.tipoVeiculo";
		}
		
		if ("asc".equals(sortorder)){
			
			criteria.addOrder(Order.asc(property));
		} else {
			
			criteria.addOrder(Order.desc(property));
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarIdsRotasPorAssociacao(Set<Long> assocRemovidas) {
		
		Query query = this.getSession().createQuery("select a.rota.id from AssociacaoVeiculoMotoristaRota a where a.id in (:ids)");
		
		query.setParameterList("ids", assocRemovidas);
		
		return (List<Long>) query.list();
	}
	
	@Override
	public boolean verificarAssociacaoMotorista(Long idMotorista, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select count (m.id) ");
		hql.append(" from Motorista m, AssociacaoVeiculoMotoristaRota a ")
		   .append(" where m.id = :idMotorista ")
		   .append(" and m.id = a.motorista.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			hql.append(" and a.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMotorista", idMotorista);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
					
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return (Long)query.uniqueResult() > 0;
	}

	@Override
	public boolean verificarAssociacaoVeiculo(Long idVeiculo, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select count (m.id) ");
		hql.append(" from Veiculo m, AssociacaoVeiculoMotoristaRota a ")
		   .append(" where m.id = :idVeiculo ")
		   .append(" and m.id = a.veiculo.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			hql.append(" and a.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idVeiculo", idVeiculo);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return (Long)query.uniqueResult() > 0;
	}

	@Override
	public boolean verificarAssociacaoRotaRoteiro(Long idRota) {
		
		StringBuilder hql = new StringBuilder("select count (m.id) ");
		hql.append(" from Rota m, AssociacaoVeiculoMotoristaRota a ")
		   .append(" where m.id = :idRota ")
		   .append(" and m.id = a.rota.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idRota", idRota);
		
		return (Long)query.uniqueResult() > 0;
	}
}