package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FuroProdutoRepository;

@Repository
public class FuroProdutoRepositoryImpl extends AbstractRepositoryModel<FuroProduto, Long>
		implements FuroProdutoRepository {

	public FuroProdutoRepositoryImpl() {
		super(FuroProduto.class);
	}

	@Override
	public FuroProduto obterFuroProdutoPor(Long lancamentoId, Long produtoEdicaoId) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select fp ")
		   .append(" from FuroProduto fp ")
		   .append(" where fp.lancamento.id = :lancamentoId ")
		   .append(" and fp.produtoEdicao.id = :produtoEdicaoId ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("lancamentoId", lancamentoId);
		query.setParameter("produtoEdicaoId", produtoEdicaoId);
		
		return (FuroProduto) query.uniqueResult();		
		
	}
}
