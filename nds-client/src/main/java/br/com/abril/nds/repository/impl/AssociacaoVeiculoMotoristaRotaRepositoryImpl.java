package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.repository.AssociacaoVeiculoMotoristaRotaRepository;

@Repository
public class AssociacaoVeiculoMotoristaRotaRepositoryImpl extends
		AbstractRepository<AssociacaoVeiculoMotoristaRota, Long> implements
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
			Long idTransportador, Set<Long> idsIgnorar) {
		
		Criteria criteria = this.getSession().createCriteria(AssociacaoVeiculoMotoristaRota.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			criteria.add(Restrictions.not(Restrictions.in("id", idsIgnorar)));
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
}