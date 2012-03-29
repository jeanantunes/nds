package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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
	
	public Double obterDividaAcumuladaCota(Long idCota) {		
		Criteria criteria = getSession().createCriteria(Cobranca.class,"cobranca");
		criteria.createAlias("cobranca.cota", "cota");
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("statusCobranca", StatusCobranca.NAO_PAGO));
		criteria.setProjection(Projections.sum("valor"));
		
		BigDecimal dividaAcumulada = (BigDecimal) criteria.uniqueResult();
		
		return dividaAcumulada == null ? 0 : dividaAcumulada.doubleValue();				
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
		
		return (Cobranca) criteria.uniqueResult();
	}
	
	public void incrementarVia(String nossoNumero){
		Query query = this.getSession().createQuery("update Cobranca set vias = vias + 1 where nossoNumero = :nossoNumero");
		query.setParameter("nossoNumero", nossoNumero);
		query.executeUpdate();
	}
}
