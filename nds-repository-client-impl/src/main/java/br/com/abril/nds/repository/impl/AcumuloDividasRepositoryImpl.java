package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AcumuloDividasRepository;

/**
 * Repositório de dados referentes ao acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class AcumuloDividasRepositoryImpl extends AbstractRepositoryModel<AcumuloDivida, Long> implements AcumuloDividasRepository {

	public AcumuloDividasRepositoryImpl() {
		super(AcumuloDivida.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorMovimentoFinanceiroPendente(Long idMovimentoFinanceiro) {
		
		return (AcumuloDivida) getSession()
				.createSQLQuery(" SELECT * FROM acumulo_divida WHERE MOV_PENDENTE_ID = :idMovimentoFinanceiro ")
				.addEntity(AcumuloDivida.class)
				.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro)
				.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AcumuloDivida> obterAcumuloDividaPorDivida(Long idDivida) {
		
		return getSession()
				.createSQLQuery(" select ad.* " 
								+ " from acumulo_divida ad "
								+ " join movimento_financeiro_cota mfc on mfc.ID = ad.MOV_PENDENTE_ID "
								+ " join CONSOLIDADO_MVTO_FINANCEIRO_COTA cmfc on mfc.ID = cmfc.MVTO_FINANCEIRO_COTA_ID "
								+ " join consolidado_financeiro_cota cfc on cmfc.CONSOLIDADO_FINANCEIRO_ID = cfc.ID "
								+ " join divida d on d.CONSOLIDADO_ID = cfc.id "
								+ " join cobranca c on c.DIVIDA_ID = d.ID "
								+ " where d.ID=:idDivida ")
		
				.addEntity(AcumuloDivida.class)
				.setParameter("idDivida", idDivida)
				.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroMaximoAcumuloCota(Long idCota) {

		return (BigInteger) getSession().createCriteria(AcumuloDivida.class)
				.add(Restrictions.eq("cota.id", idCota))
				.add(Restrictions.ne("status", StatusInadimplencia.QUITADA))
				.setProjection(Projections.max("numeroAcumulo"))
				.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroDeAcumulosDivida(Long idConsolidadoFinanceiroCota) {
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" select ad.numeroAcumulo ");
		jpql.append(" from AcumuloDivida ad ");
		jpql.append(" join ad.movimentoFinanceiroPendente movimentoFinanceiroPendente ");
		jpql.append(" join movimentoFinanceiroPendente.consolidadoFinanceiroCota consolidadoFinanceiroCota ");		
		jpql.append(" where consolidadoFinanceiroCota.id = :idConsolidadoFinanceiroCota ");
		
		Query query = this.getSession().createQuery(jpql.toString());

		query.setParameter("idConsolidadoFinanceiroCota", idConsolidadoFinanceiroCota);
		
		query.setMaxResults(1);
		
		return (BigInteger) query.uniqueResult();
	}
	
}
