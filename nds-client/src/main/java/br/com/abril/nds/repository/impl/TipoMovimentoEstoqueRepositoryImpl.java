package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

@Repository
public class TipoMovimentoEstoqueRepositoryImpl extends AbstractRepositoryModel<TipoMovimentoEstoque, Long> implements TipoMovimentoEstoqueRepository{

	public TipoMovimentoEstoqueRepositoryImpl() {
		super(TipoMovimentoEstoque.class);
	}

	@Override
	public TipoMovimentoEstoque buscarTipoMovimentoEstoque(GrupoMovimentoEstoque grupoMovimentoEstoque) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoEstoque.class);
		
		criteria.add(Restrictions.eq("operacaoEstoque", grupoMovimentoEstoque.getOperacaoEstoque()));
		criteria.add(Restrictions.eq("grupoMovimentoEstoque", grupoMovimentoEstoque));
		
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoEstoque) criteria.uniqueResult();
	}
}
