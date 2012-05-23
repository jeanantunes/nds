package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;

@Repository
public class CobrancaRepositoryImpl extends AbstractRepository<Cobranca, Long> implements CobrancaRepository {

	public CobrancaRepositoryImpl() {
		super(Cobranca.class);		
	}

	public Date obterDataAberturaDividas(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		criteria.createAlias("cobranca.cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.setProjection(Projections.min("dataVencimento"));			
		
		return (Date) criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota) {		
		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		criteria.createAlias("cobranca.cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		
		criteria.addOrder(Order.asc("dataVencimento"));
		
		return criteria.list();				
	}
	
	public Cobranca obterCobrancaPorNossoNumero(String nossoNumero){
		
		Criteria criteria = this.getSession().createCriteria(Cobranca.class);
		criteria.add(Restrictions.eq("nossoNumero", nossoNumero));
		criteria.setMaxResults(1);
	
		return (Cobranca) criteria.uniqueResult();
	}
	
	public void incrementarVia(String... nossoNumero){
		Query query = this.getSession().createQuery("update Cobranca set vias = vias + 1 where nossoNumero IN (:nossoNumero)");
		query.setParameterList("nossoNumero", nossoNumero);
		query.executeUpdate();
	}
	
	
	/**
	 * Método responsável por obter a quantidade de cobrancas
	 * @param filtro
	 * @return quantidade: quantidade de cobrancas
	 */
	@Override
	public long obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(c) from Cobranca c where ");		
		hql.append(" c.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimento()!=null){
		    hql.append(" and c.dataVencimento >= :vcto ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimento()!=null){
		    query.setDate("vcto", filtro.getDataVencimento());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	/**
	 * Método responsável por obter uma lista de cobrancas
	 * @param filtro
	 * @return query.list(): lista de cobrancas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" from Cobranca c where ");		
		hql.append(" c.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimento()!=null){
		    hql.append(" and c.dataVencimento >= :vcto ");
		}
	
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case CODIGO:
					hql.append(" order by c.id ");
					break;
				case NOME_COTA:
					hql.append(" order by c.cota.pessoa.nome ");
					break;
				case DATA_EMISSAO:
					hql.append(" order by c.dataEmissao ");
					break;
				case DATA_VENCIMENTO:
					hql.append(" order by c.dataVencimento ");
					break;
				case VALOR:
					hql.append(" order by c.valor ");
					break;
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimento()!=null){
		    query.setDate("vcto", filtro.getDataVencimento());
		}

        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	@Override
	public void excluirCobrancaPorIdDivida(Long idDivida) {
		
		Query query = this.getSession().createQuery("delete from Cobranca where divida.id = :idDivida");
		
		query.setParameter("idDivida", idDivida);
		
		query.executeUpdate();
	}

	
}
