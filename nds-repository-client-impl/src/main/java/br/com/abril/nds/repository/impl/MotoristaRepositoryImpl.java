package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MotoristaRepository;

@Repository
public class MotoristaRepositoryImpl extends
		AbstractRepositoryModel<Motorista, Long> implements MotoristaRepository {

	public MotoristaRepositoryImpl() {
		super(Motorista.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Motorista> buscarMotoristasPorTransportador(
			Long idTransportador, Set<Long> idsIgnorar, String sortname,
			String sortorder) {

		Criteria criteria = this.getSession().createCriteria(Motorista.class);
		criteria.add(Restrictions.eq("transportador.id", idTransportador));

		if (idsIgnorar != null && !idsIgnorar.isEmpty()) {

			criteria.add(Restrictions.not(Restrictions.in("id", idsIgnorar)));
		}
		
		String property = null;

		if ("cnh".equals(sortname)) {

			property = "cnh";
		} else {

			property = "nome";
		}

		if ("asc".equals(sortorder)) {

			criteria.addOrder(Order.asc(property));
		} else {

			criteria.addOrder(Order.desc(property));
		}

		return criteria.list();
	}

	@Override
	public void removerPorId(Long idMotorista) {

		Query query = this.getSession().createQuery(
				"delete from Motorista where id = :idMotorista");
		query.setParameter("idMotorista", idMotorista);

		query.executeUpdate();
	}

	@Override
	public void removerMotoristas(Long idTransportador,
			Set<Long> listaMotoristasRemover) {

		StringBuilder hql = new StringBuilder("delete from Motorista ");
		hql.append(" where transportador.id = :idTransportador ");

		if (listaMotoristasRemover != null && !listaMotoristasRemover.isEmpty()) {

			hql.append(" and id in (:listaMotoristasRemover) ");
		}

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTransportador", idTransportador);

		if (listaMotoristasRemover != null && !listaMotoristasRemover.isEmpty()) {

			query.setParameterList("listaMotoristasRemover", listaMotoristasRemover);
		}

		query.executeUpdate();
	}
}