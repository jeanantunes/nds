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
	public List<Produto> obterProdutoLikeNome(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) like upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", "%" + nome + "%");
		
		return query.list();
	}
	
	@Override
	public Produto obterProdutoPorNome(String nome) {
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
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select  ");
		
		hql.append(" produto.id as id, 								");
		hql.append(" produto.codigo as codigo, 						");
		hql.append(" produto.nome as produtoDescricao, 				");
		hql.append(" tipoproduto.nome as tipoProdutoDescricao, ");
		
		hql.append(" ( select case when editor.id is null then '' 	");
		hql.append(" else editor.pessoaJuridica.razaoSocial end		");
		hql.append(" from Produto p left join p.editor as editor 	");
		hql.append(" where p.id = produto.id ) as nomeEditor, 		");
		
		hql.append(" ( select "); 
		hql.append(" case when count(f.id)>1 then 'Diversos' 				");
		hql.append(" else f.juridica.razaoSocial end 						");
		hql.append(" from Produto p join p.fornecedores f 					");
		hql.append(" where p.id = produto.id ) as tipoContratoFornecedor, 	");
		
		hql.append(" produto.peb as peb,  						");
		hql.append(" produto.pacotePadrao as pacotePadrao, 		");
		hql.append(" coalesce(descontoLogistica.percentualDesconto, 0) as percentualDesconto, ");
		hql.append(" produto.periodicidade as periodicidade 	");
		
		try {
			
			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, sortname, sortorder, false);
			
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
		
		StringBuffer hql = new StringBuffer(" select count(distinct produto.id) ");
		
		try {

			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, null, null, true);
			
			return ((Long) query.uniqueResult()).intValue();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Query getQueryBuscaProdutos(StringBuffer hql, String codigo, String nome,
			String fornecedor, String editor, Long codigoTipoProduto, String sortname, String sortorder, boolean isCount) {
		
		hql.append(" from ");
		hql.append(" Produto produto ");
		hql.append(" join produto.tipoProduto tipoProduto 					");
		hql.append(" left join produto.descontoLogistica descontoLogistica 	");

		if (fornecedor != null && !fornecedor.isEmpty()) {
			hql.append(" join produto.fornecedores fornecedorProd ");
		}
		
		if (editor != null && !editor.isEmpty()) {
			hql.append(" join produto.editor editorProd ");
		}
		
		String auxHql = " where ";
		
		if (codigo != null && !codigo.isEmpty()) {
			hql.append(auxHql).append(" upper(produto.codigo) = :codigo ");
			auxHql = " and ";
		}
		
		if (nome != null && !nome.isEmpty()) {
			hql.append(auxHql).append(" produto.nome like :nome ");
			auxHql = " and ";
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			
			hql.append(auxHql);
			hql.append(" fornecedorProd.juridica.razaoSocial like :fornecedor ");
			auxHql = " and ";
		}
		
		if (editor != null && !editor.isEmpty()) {
			
			hql.append(auxHql);
			hql.append(" editorProd.pessoaJuridica.razaoSocial like :nomeEditor ");
			auxHql = " and ";
		}

		if (codigoTipoProduto != null && codigoTipoProduto > 0) {
			hql.append(auxHql).append(" tipoProduto.id = :codigoTipoProduto ");
		}

		if(!isCount) {
		
			hql.append(" group by  			");
			hql.append(" produto.id, 		");
			hql.append(" produto.codigo, 	");
			hql.append(" produto.nome,      ");
			hql.append(" tipoproduto.nome, ");
			hql.append(" col_4_0_, 				");
			hql.append(" col_5_0_,				");
			hql.append(" produto.peb,			");
			hql.append(" produto.pacotePadrao,  ");
			hql.append(" col_8_0_, 				");
			hql.append(" produto.periodicidade  ");
			
		}

		
		if (sortname != null && sortorder != null) {
			hql.append(" order by ").append(sortname).append(" ").append(sortorder);
		}
		
		Query query = super.getSession().createQuery(hql.toString());

		if (codigo != null && !codigo.isEmpty()) {
			query.setParameter("codigo", codigo.toUpperCase());
		}
		
		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome + "%");
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