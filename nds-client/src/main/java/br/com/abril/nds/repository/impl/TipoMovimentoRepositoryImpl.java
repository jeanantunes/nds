package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.DominioTipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.TipoMovimentoRepository;

@Repository
public class TipoMovimentoRepositoryImpl extends AbstractRepository<TipoMovimento, Long> implements TipoMovimentoRepository{

	public TipoMovimentoRepositoryImpl() {
		super(TipoMovimento.class);
	}

	@Override
	public TipoMovimento buscarTipoMovimento(TipoOperacao tipoOperacao, DominioTipoMovimento tipoMovimento) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimento.class);
		
		criteria.add(Restrictions.eq("tipoOperacao", tipoOperacao));
		criteria.add(Restrictions.eq("tipoMovimento", tipoMovimento));
		
		
		criteria.setMaxResults(1);
		
		return (TipoMovimento) criteria.uniqueResult();
	}
}
