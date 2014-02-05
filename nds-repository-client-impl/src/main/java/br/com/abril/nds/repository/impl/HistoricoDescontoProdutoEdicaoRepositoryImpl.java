package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoProdutoEdicaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do ProdutoEdicao
 * 
 */
@Repository
public class HistoricoDescontoProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoProdutoEdicao, Long> implements HistoricoDescontoProdutoEdicaoRepository {

	public HistoricoDescontoProdutoEdicaoRepositoryImpl() {
		super(HistoricoDescontoProdutoEdicao.class);
	}

	@Override
	public HistoricoDescontoProdutoEdicao buscarHistoricoPorDescontoEProduto(Long idDesconto, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder(" select hdp 		")
		.append(" from HistoricoDescontoProdutoEdicao as hdp	") 
		.append(" where hdp.desconto.id = :idDesconto 			")
		.append(" and hdp.produtoEdicao.id  = :idProdutoEdicao 	");

		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idDesconto", idDesconto);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		return (HistoricoDescontoProdutoEdicao) query.uniqueResult();
		
	}

}
