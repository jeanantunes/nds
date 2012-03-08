package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.ProdutoRepository;

@Repository
public class ProdutoRepositoryImpl extends AbstractRepository<Produto, Long> implements ProdutoRepository {

	public ProdutoRepositoryImpl() {
		super(Produto.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> obterProdutoLikeNomeProduto(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) like upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> obterProdutoPorNomeProduto(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) = upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome);
		
		return query.list();
	}
	
	@Override
	public Produto obterProdutoPorCodigo(String codigoProduto) {
		String hql = "from Produto produto " 
				   + " where produto.codigo = :codigoProduto";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto", codigoProduto);
		
		return (Produto) query.uniqueResult();
	}
	
}