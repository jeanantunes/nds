package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.ProdutoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoRepositoryImpl extends AbstractRepository<Produto, Long> implements ProdutoRepository {

	/**
	 * Construtor padrão.
	 */
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
	
	@Override
	public Produto obterProdutoPorNomeProduto(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) = upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome);
		
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
	}
	
	@Override
	public Produto obterProdutoPorCodigo(String codigoProduto) {
		String hql = "from Produto produto " 
				   + " where produto.codigo = :codigoProduto";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto", codigoProduto);
		
		return (Produto) query.uniqueResult();
	}

	@Override
	public String obterNomeProdutoPorCodigo(String codigoProduto) {
		Query query = this.getSession().createQuery("select p.nome from Produto p where p.codigo = :codigoProduto");
		query.setParameter("codigoProduto", codigoProduto);
		
		return (String) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Produto> pesquisarProdutos(Integer codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto,
			String sortorder, String sortname, int page, int rp) {
		
		String hql = "select produto From Produto produto ";
		
		
		hql += "order by " + sortname + " " + sortorder;
		
		Query query = super.getSession().createQuery(hql);
		
		query.setMaxResults(rp);
		query.setFirstResult(page);
		
		return query.list();
	}
}