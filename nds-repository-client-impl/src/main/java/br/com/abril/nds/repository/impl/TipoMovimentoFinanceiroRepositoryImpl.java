package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;

@Repository
public class TipoMovimentoFinanceiroRepositoryImpl extends AbstractRepositoryModel<TipoMovimentoFinanceiro, Long> implements TipoMovimentoFinanceiroRepository {

	public TipoMovimentoFinanceiroRepositoryImpl() {
		super(TipoMovimentoFinanceiro.class);
	}

	@Override
	public TipoMovimentoFinanceiro buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro grupoMovimentoFinanceiro) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoFinanceiro.class);
		
		criteria.add(Restrictions.eq("operacaoFinaceira", grupoMovimentoFinanceiro.getOperacaoFinaceira()));
		criteria.add(Restrictions.eq("grupoMovimentoFinaceiro", grupoMovimentoFinanceiro));
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoFinanceiro) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoMovimentoFinanceiro> buscarTiposMovimentoFinanceiro(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoFinanceiro.class);
		
		criteria.add(Restrictions.in("grupoMovimentoFinaceiro", gruposMovimentoFinanceiro));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarIdsTiposMovimentoFinanceiro(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro){
		
		StringBuilder hql = new StringBuilder("select t.id ");
		hql.append(" from TipoMovimentoFinanceiro t ")
		   .append(" where t.grupoMovimentoFinaceiro in (:gruposMovimentoFinanceiro)");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("gruposMovimentoFinanceiro", gruposMovimentoFinanceiro);
		query.setMaxResults(1);
		
		return query.list();
	}

	@Override
	public TipoMovimentoFinanceiro buscarPorDescricao(String descricao) {
		Criteria criteria = super.getSession().createCriteria(TipoMovimentoFinanceiro.class);
		
		criteria.add(Restrictions.eq("descricao", descricao ) );
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoFinanceiro) criteria.uniqueResult();
	}
}
