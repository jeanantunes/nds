package br.com.abril.nds.repository.impl;

import java.math.BigInteger;

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
	@Override
	public AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida) {
		
		return (AcumuloDivida) getSession()
				.createSQLQuery(" SELECT * FROM acumulo_divida WHERE DIVIDA_ID = :idDivida ")
				.addEntity(AcumuloDivida.class)
				.setParameter("idDivida", idDivida)
				.uniqueResult();
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
}
