package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProduto;
import br.com.abril.nds.repository.HistoricoDescontoProdutoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do Produto
 * 
 * @author Discover Technology
 */
@Repository
public class HistoricoDescontoProdutoRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoProduto, Long> implements HistoricoDescontoProdutoRepository {

	public HistoricoDescontoProdutoRepositoryImpl() {
		super(HistoricoDescontoProduto.class);
	}

	@Override
	public HistoricoDescontoProduto buscarHistoricoPorDescontoEProduto(Desconto desconto, Produto produto) {
		
		StringBuilder hql = new StringBuilder("select hdp ")
		.append("from HistoricoDescontoProduto as hdp ") 
		.append("where 1 = 1 ")
		.append("and hdp.desconto.id = :idDesconto ")
		.append("and hdp.produto.id  = :idProduto ");

		Query query = getSession().createQuery(hql.toString());
		
		//TODO: Validar nulos
		query.setParameter("idDesconto", desconto.getId());
		query.setParameter("idProduto", produto.getId());

		return (HistoricoDescontoProduto) query.uniqueResult();
	}

}
