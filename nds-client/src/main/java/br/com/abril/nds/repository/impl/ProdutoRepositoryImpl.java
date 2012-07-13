package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.ProdutoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoRepositoryImpl extends AbstractRepositoryModel<Produto, Long> implements ProdutoRepository {

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
	public List<ConsultaProdutoDTO> pesquisarProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto,
			String sortorder, String sortname, int page, int rp) {
		
		String hql = "select produto.id as id, produto.codigo as codigo, produto.nome as produtoDescricao, ";
		hql += " tipoProduto.descricao as tipoProdutoDescricao, editor.nome as nomeEditor, ";
		hql += " juridica.razaoSocial as tipoContratoFornecedor, ";
		hql += " fornecedor.situacaoCadastro as situacao, ";
		hql += " produto.peb as peb ";

		try {
			
			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, sortname, sortorder);
			
			query.setResultTransformer(
				new AliasToBeanResultTransformer(
					ConsultaProdutoDTO.class));
			
			query.setMaxResults(rp);
			query.setFirstResult(page);
			
			return query.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer pesquisarCountProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto) {
		
		String hql = "select count(produto.id) ";
		
		try {

			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, null, null);
			
			return ((Long) query.uniqueResult()).intValue();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Query getQueryBuscaProdutos(String hql, String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto, String sortname, String sortorder) {
		
		hql += " from Produto produto ";
		hql += " join produto.fornecedores fornecedor ";
		hql += " join fornecedor.juridica juridica ";
		hql += " join produto.editor editor ";
		hql += " join produto.tipoProduto tipoProduto ";
		hql += " where 1 = 1 ";
		
		if (codigo != null && !codigo.isEmpty()) {
			hql += " and upper(produto.codigo) = :codigo ";
		}
		
		if (produto != null && !produto.isEmpty()) {
			hql += " and produto.nome like :produto ";
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			hql += " and fornecedor.juridica.razaoSocial like :fornecedor ";
		}
		
		if (editor != null && !editor.isEmpty()) {
			hql += " and editor.nome like :nomeEditor ";
		}
		
		if (codigoTipoProduto != null && codigoTipoProduto > 0) {
			hql += " and tipoProduto.id = :codigoTipoProduto ";
		}

		if (sortname != null && sortorder != null) {
			hql += " order by " + sortname + " " + sortorder;
		}
		
		Query query = super.getSession().createQuery(hql);

		if (codigo != null && !codigo.isEmpty()) {
			query.setParameter("codigo", codigo.toUpperCase());
		}
		
		if (produto != null && !produto.isEmpty()) {
			query.setParameter("produto", "%" + produto + "%");
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			query.setParameter("fornecedor", "%" + fornecedor + "%");
		}
		
		if (editor != null && !editor.isEmpty()) {
			query.setParameter("nomeEditor", "%" + editor + "%");
		}
		
		if (codigoTipoProduto != null && codigoTipoProduto > 0) {
			query.setParameter("codigoTipoProduto", codigoTipoProduto);
		}
		
		return query;
	}

	@Override
	public Produto obterProdutoPorID(Long id) {
		
		Criteria criteria = super.getSession().createCriteria(Produto.class);
		
		criteria.add(Restrictions.eq("id", id))
			.setFetchMode("fornecedores", FetchMode.JOIN)
			.setFetchMode("descontoLogistica", FetchMode.JOIN);
		
		return (Produto) criteria.uniqueResult();
	}
	
	/**
	 * Obtem produto por nome ou codigo
	 * @param nome
	 * @param codigo
	 * @return Produto
	 */
	@Override
	public Produto obterProdutoPorNomeProdutoOuCodigo(String nome, String codigo) {
		String hql = "from Produto produto "
				   + " where ((upper(produto.nome) = upper(:nome)) or (produto.codigo = :codigo)) ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome);
		
		query.setParameter("codigo", codigo);
		
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
	}
	
}