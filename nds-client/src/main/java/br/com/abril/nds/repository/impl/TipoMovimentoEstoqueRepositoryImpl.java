package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;

@Repository
public class TipoMovimentoEstoqueRepositoryImpl extends AbstractRepository<TipoMovimentoEstoque, Long> implements TipoMovimentoEstoqueRepository{

	public TipoMovimentoEstoqueRepositoryImpl() {
		super(TipoMovimentoEstoque.class);
	}

	@Override
	public TipoMovimentoEstoque buscarTipoMovimentoEstoque(TipoOperacao tipoOperacao, GrupoMovimentoEstoque tipoMovimento) {

		Criteria criteria = super.getSession().createCriteria(TipoMovimentoEstoque.class);
		
		criteria.add(Restrictions.eq("operacaoEstoque", tipoOperacao));
		criteria.add(Restrictions.eq("tipoMovimento", tipoMovimento));
		
		
		criteria.setMaxResults(1);
		
		return (TipoMovimentoEstoque) criteria.uniqueResult();
	}
}
