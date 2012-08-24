package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.ProdutoRepository;

@Repository
public class ProdutoRepositoryImpl extends AbstractRepositoryModel<Produto, Long> implements ProdutoRepository {

	/**
	 * Construtor padrão.
	 */
	public ProdutoRepositoryImpl() {
		super(Produto.class);
	}
	
	public GrupoProduto obterGrupoProduto(String codigoProduto) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select p.tipoProduto.grupoProduto from Produto p where p.codigo = :codigoProduto ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("codigoProduto", codigoProduto);
		
		return (GrupoProduto) query.uniqueResult();
		
	}

	
}
