package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
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
		
		criteria.setCacheable(true);
		
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
		
		return query.list();
	}

	@Override
	public TipoMovimentoFinanceiro buscarPorDescricao(String descricao) {
		Criteria criteria = super.getSession().createCriteria(TipoMovimentoFinanceiro.class);
		
		criteria.add(Restrictions.eq("descricao", descricao ) );
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoFinanceiro) criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoMovimentoFinanceiro> buscarTiposMovimentoFinanceiroPorOperacaoFinanceira(
			OperacaoFinaceira operacaoFinaceira, List<TipoMovimentoFinanceiro> movimentosIgnorar){
		
		StringBuilder hql = new StringBuilder("select t ");
		hql.append(" from TipoMovimentoFinanceiro t ")
		   .append(" where t.operacaoFinaceira = :operacaoFinaceira ");
		
		if (movimentosIgnorar != null && 
				!movimentosIgnorar.isEmpty()){
			
			hql.append(" and t not in (:movimentosIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("operacaoFinaceira", operacaoFinaceira);
		
		if (movimentosIgnorar != null && 
				!movimentosIgnorar.isEmpty()){
			
			query.setParameterList("movimentosIgnorar", movimentosIgnorar);
		}
		
		return query.list();
	}

	/**
	 * Obtem tipo de movimento financeiro por GrupoMovimentoFinanceiro e OperacaoFinanceira
	 * @param grupo
	 * @param operacao
	 * @return TipoMovimentoFinanceiro
	 */
	@Override
	public TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(GrupoMovimentoFinaceiro grupo, OperacaoFinaceira operacao){
		
		StringBuilder hql = new StringBuilder("select t ");
		hql.append(" from TipoMovimentoFinanceiro t ")
		   .append(" where t.operacaoFinaceira = :operacaoFinaceira ")
		   .append(" and t.grupoMovimentoFinaceiro = :grupoMovimentoFinanceiro");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("operacaoFinaceira", operacao);
		
		query.setParameter("grupoMovimentoFinanceiro", grupo);
		
		return (TipoMovimentoFinanceiro) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarIdsTiposMovimentoFinanceiroPorOperacaoFinanceira(
			OperacaoFinaceira operacaoFinaceira, List<TipoMovimentoFinanceiro> tiposIgnorar) {
		
		StringBuilder hql = new StringBuilder("select t.id ");
		hql.append(" from TipoMovimentoFinanceiro t ")
		   .append(" where t.operacaoFinaceira = :operacaoFinaceira ");
		
		if (tiposIgnorar != null &&
				!tiposIgnorar.isEmpty()){
			
			hql.append(" and t not in (:tiposIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("operacaoFinaceira", operacaoFinaceira);
		
		if (tiposIgnorar != null &&
				!tiposIgnorar.isEmpty()){
			
			query.setParameterList("tiposIgnorar", tiposIgnorar);
		}
		
		return query.list();
	}
}
