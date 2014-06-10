package br.com.abril.nds.repository.impl;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleNumeracaoNotaFiscalRepository;

@Repository
public class ControleNumeracaoNotaFiscalRepositoryImpl extends AbstractRepositoryModel<ControleNumeracaoNotaFiscal, Long> implements ControleNumeracaoNotaFiscalRepository {

	public ControleNumeracaoNotaFiscalRepositoryImpl() {
		super(ControleNumeracaoNotaFiscal.class);
	}

	public ControleNumeracaoNotaFiscal obterControleNumeracaoNotaFiscal(String serieNF) {

		String hql = " select c from ControleNumeracaoNotaFiscal c where c.serieNF = :serieNF ";

		Query query = getSession().createQuery(hql);
		
		query.setParameter("serieNF", serieNF);
		
		return (ControleNumeracaoNotaFiscal) query.uniqueResult();

	}

	//TODO verificar se esta e a forma correta
	public ControleNumeracaoNotaFiscal obterForUpdateControleNumeracaoNotaFiscal(Long idControleNumeracaoNotaFiscal) {
		
		return (ControleNumeracaoNotaFiscal) getSession().get(ControleNumeracaoNotaFiscal.class, idControleNumeracaoNotaFiscal, LockOptions.READ);
		
	}

	
	
}