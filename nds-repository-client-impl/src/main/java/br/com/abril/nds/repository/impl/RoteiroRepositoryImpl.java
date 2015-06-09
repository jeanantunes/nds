package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RoteiroRepositoryImpl extends
		AbstractRepositoryModel<Roteiro, Long> implements RoteiroRepository {

	public RoteiroRepositoryImpl() {
		super(Roteiro.class);
	}

	@SuppressWarnings("unchecked")
	public List<Roteiro> buscarRoteiro(String sortname, Ordenacao ordenacao) {

		Criteria criteria = getSession().createCriteria(Roteiro.class);

		if (Ordenacao.ASC == ordenacao) {
			criteria.addOrder(Order.asc(sortname));
		} else if (Ordenacao.DESC == ordenacao) {
			criteria.addOrder(Order.desc(sortname));
		}
		
		criteria.setCacheable(true);
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Roteiro> buscarRoteiroPorDescricao(String descricao, MatchMode matchMode) {
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.ilike("descricaoRoteiro", descricao, matchMode));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public void atualizaOrdenacao(Roteiro roteiro) {
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.ge("ordem", roteiro.getOrdem()));
		criteria.add(Restrictions.ne("id", roteiro.getId()));
		criteria.addOrder(Order.asc("ordem"));
		List<Roteiro> roteiros = criteria.list();
		Integer ordem = roteiro.getOrdem();
		for (Roteiro entity : roteiros) {
			ordem++;
			entity.setOrdem(ordem);
			merge(entity);

		}

	}

	@Override
	public List<Roteiro> buscarRoteiroDeBox(Long idBox) {
		return buscarRoteiroDeBox(idBox, null);
	}

	@Override
	public Integer buscarMaiorOrdemRoteiro() {
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		criteria.setProjection(Projections.max("ordem"));
		return (Integer) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> buscarRoteiroEspecial() {
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		criteria.add(Restrictions.eq("tipoRoteiro", TipoRoteiro.ESPECIAL));
		criteria.addOrder(Order.asc("descricaoRoteiro"));
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> buscarRoteiroDeBox(Long idBox, String descricaoRoteiro) {
		Criteria criteria = getSession().createCriteria(Roteiro.class,
				"roteiro");
		if (idBox != null) {
			criteria.createAlias("roteiro.roteirizacao", "roteirizacao");
			
			if (idBox < 1) {
				
				criteria.createAlias("roteirizacao.box", "box", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.isNull("roteirizacao.box"));
			
			} else {
			
				criteria.createAlias("roteirizacao.box", "box");
				criteria.add(Restrictions.eq("box.id", idBox));
			
			}
		}
		if (!StringUtil.isEmpty(descricaoRoteiro)) {
			criteria.add(Restrictions.ilike("descricaoRoteiro",
					descricaoRoteiro, MatchMode.START));
		}
		criteria.addOrder(Order.asc("descricaoRoteiro"));
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> obterRoteirosPorCota(Integer numeroCota) {

		Criteria criteria = getSession().createCriteria(Roteiro.class,
				"roteiro");

		if (numeroCota != null) {
			criteria.createAlias("roteiro.rotas", "rotas");
			criteria.createAlias("rotas.rotaPDVs", "rotaPDV");
			criteria.createAlias("rotaPDV.pdv", "pdv");
			criteria.createAlias("pdv.cota", "cota");
			criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
		}

		criteria.addOrder(Order.asc("roteiro.descricaoRoteiro"));

		return criteria.list();
	}

	/**
	 * @see br.com.abril.nds.repository.RoteiroRepository#obterRoteiroPorRota(java.lang.Long)
	 */
	@Override
	public Roteiro obterRoteiroPorRota(Long rotaID) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT r FROM Roteiro r ");
		sql.append(" JOIN r.rotas rota ");
		sql.append(" WHERE rota.id = :rotaID ");
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("rotaID", rotaID);
		query.setMaxResults(1);
		
		return (Roteiro) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> obterRoteirosNaoAssociadosAoBox(Long idBox) {
		
		Criteria criteria = this.getSession().createCriteria(Roteiro.class);
		criteria.createAlias("roteirizacao", "roteirizacao");
		criteria.add(Restrictions.ne("roteirizacao.box.id", idBox));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Roteiro> buscarRoteiroCodigoBox(Long codigoBoxDe, Long codigoBoxAte) {
		
		Criteria criteria = getSession().createCriteria(Roteiro.class, "roteiro");
		if (codigoBoxDe != null) {
			criteria.createAlias("roteiro.roteirizacao", "roteirizacao");
			
			if (codigoBoxDe < 1) {
				
				criteria.createAlias("roteirizacao.box", "box", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.isNull("roteirizacao.box"));
			
			} else {
			
				criteria.createAlias("roteirizacao.box", "box");
				if(codigoBoxDe != null && codigoBoxAte != null) {
					criteria.add(Restrictions.between("box.codigo", codigoBoxDe.intValue(), codigoBoxAte.intValue()));
				}
				if(codigoBoxDe != null && codigoBoxAte == null) {
					criteria.add(Restrictions.eq("box.codigo", codigoBoxDe.intValue()));
				}

			
			}
		}
		
		criteria.addOrder(Order.asc("descricaoRoteiro"));
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		
	}

}