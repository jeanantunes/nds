package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoFiscal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoMovimentoFiscalRepository;

@Repository
public class TipoMovimentoFiscalRepositoryImpl extends AbstractRepositoryModel<TipoMovimentoFiscal, Long> implements TipoMovimentoFiscalRepository {

	public TipoMovimentoFiscalRepositoryImpl() {
		super(TipoMovimentoFiscal.class);
	}

	@Override
	public TipoMovimentoFiscal buscarTiposMovimentoFiscalPorTipoOperacao(OperacaoEstoque operacaoEstoque) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoFiscal.class);
		
		criteria.add(Restrictions.eq("operacaoEstoque", operacaoEstoque));
		
		criteria.setCacheable(true);
		
		return (TipoMovimentoFiscal) criteria.uniqueResult();
	}
	
}