package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.repository.RoteirizacaoRepository;

@Repository
public class RoteirizacaoRepositoryImpl extends AbstractRepository<Roteirizacao, Long> implements RoteirizacaoRepository {
	
	public RoteirizacaoRepositoryImpl() {
		super(Roteirizacao.class);
	}
	
	public Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select roteirizacao from Roteirizacao roteirizacao " )
			.append(" join roteirizacao.pdv pdv " )
			.append(" join pdv.cota cota " )
			.append(" join roteirizacao.rota rota " )
			.append(" join rota.roteiro roteiro ")
			.append(" where pdv.caracteristicas.pontoPrincipal=:pontoPrincipal ")
			.append(" and cota.numeroCota=:numeroCota ")
			.append(" and roteiro.tipoRoteiro=:tipoRoteiro");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("pontoPrincipal", Boolean.TRUE);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("tipoRoteiro", TipoRoteiro.NORMAL);
		
		query.setMaxResults(1);
		
		return (Roteirizacao)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Roteirizacao> buscarRoterizacaoPorRota(Long rotaId) {
		Criteria criteria = getSession().createCriteria(Roteirizacao.class);
		criteria.add(Restrictions.eq("rota.id", rotaId));
		criteria.addOrder(Order.asc("ordem"));
		return criteria.list();
	}
}

