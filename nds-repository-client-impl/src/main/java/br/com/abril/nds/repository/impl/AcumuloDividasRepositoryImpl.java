package br.com.abril.nds.repository.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
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
	public AcumuloDivida obterAcumuloDividaPorMovimentoFinanceiro(Long idMovimentoFinanceiro) {
		
		return (AcumuloDivida) getSession().createCriteria(AcumuloDivida.class)
				.add(Restrictions.eq("movimentoFinanceiroPendente.id", idMovimentoFinanceiro))
				.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida) {
		
		return (AcumuloDivida) getSession().createCriteria(AcumuloDivida.class)
				.add(Restrictions.eq("dividaAnterior.id", idDivida))
				.uniqueResult();
	}	
}
