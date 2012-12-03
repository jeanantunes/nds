package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<DescontoProdutoEdicao, Long> implements DescontoProdutoEdicaoRepository {
 
	private static final int QUINHENTOS = 500;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoProdutoEdicaoRepositoryImpl() {
		
		super(DescontoProdutoEdicao.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescontoProdutoEdicao buscarDescontoProdutoEdicao(TipoDesconto tipoDesconto,
															 Fornecedor fornecedor, 
															 Cota cota,
															 ProdutoEdicao produto) {
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);

		if (fornecedor != null) {
		
			criteria.add(Restrictions.eq("fornecedor", fornecedor));
			
		}

		if (cota != null) {
		
			criteria.add(Restrictions.eq("cota", cota));
		}

		if (produto != null) {
		
			criteria.add(Restrictions.eq("produtoEdicao", produto));
		}
		
		if (tipoDesconto != null) {
			
			criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		}
		
		criteria.setMaxResults(1);
		
		return (DescontoProdutoEdicao) criteria.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, null, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, null, produtoEdicao,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicao> obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicao> obterDescontoProdutoEdicaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, cota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontoProdutoEdicao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, null,tipoDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontoProdutoEdicao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, produtoEdicao,tipoDesconto);
	}
	
	@SuppressWarnings("unchecked")
	private Set<DescontoProdutoEdicao> obterDescontoProdutoEdicaoCotaFornecedor(Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		if (fornecedor != null) {
			
			criteria.add(Restrictions.eq("fornecedor", fornecedor));
		}
		
		if (cota != null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		if (produtoEdicao != null) {
			
			criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		}
		
		if(tipoDesconto!= null){
			
			criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		}
		
		criteria.setFetchMode("cota", FetchMode.JOIN);
		
		return new HashSet<DescontoProdutoEdicao>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	private List<DescontoProdutoEdicao> obterDescontoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		criteria.add(Restrictions.eq("fornecedor", fornecedor));

		if (cota!= null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		criteria.add(Restrictions.not(Restrictions.eq("tipoDesconto", tipoDesconto)));
	
		return criteria.list();
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public BigDecimal obterDescontoPorCotaProdutoEdicao(Cota cota,
            ProdutoEdicao produtoEdicao) {
        
		Query query = null;
		BigDecimal desconto = BigDecimal.ZERO;
		
		//Obtem o desconto do ProdutoEdicao baseado em excessoes 
		query = obterDescontoCotaProdutoEdicaoExcessoes(cota, produtoEdicao);
		desconto = (BigDecimal) query.uniqueResult();
		
		if(desconto != null) return desconto;
				
		//Obtem o desconto do Produto baseado em excessoes 
		query = obterDescontoCotaProdutoExcessoes(cota, produtoEdicao);
		desconto = (BigDecimal) query.uniqueResult();
		
		if(desconto != null) return desconto;
		
		//Obtem o desconto da Cota-Fornecedor baseado em excessoes 
		query = obterDescontoCotaExcessoes(cota, produtoEdicao);
		desconto = (BigDecimal) query.uniqueResult();
		
		if(desconto != null) return desconto;
		
		//Obtem o desconto do ProdutoEdicao 
		query = obterDescontoProdutoEdicao(produtoEdicao);
		desconto = (BigDecimal) query.uniqueResult();
		
		if(desconto != null) return desconto;
		
		//Obtem o desconto do Produto 
		query = obterDescontoProduto(produtoEdicao);
		desconto = (BigDecimal) query.uniqueResult();
		
        return desconto;
        
        /*StringBuilder hql = new StringBuilder("select view.desconto ");
        hql.append("from ViewDesconto view ")
           .append("where view.cotaId = :idCota ")
           .append("and view.produtoEdicaoId = :idProdutoEdicao ")
           .append("and view.fornecedorId = :idFornecedor ");
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("idFornecedor", idFornecedor);*/
    }

	private Query obterDescontoCotaProdutoEdicaoExcessoes(Cota cota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append("vdcfpe.desconto_id as idTipoDesconto ")
			.append(", vdcfpe.codigo_produto as codigoProduto ")
			.append(", vdcfpe.nome_produto as nomeProduto ")
			.append(", vdcfpe.numero_edicao as numeroEdicao ")
			.append(", vdcfpe.valor as desconto ")
			.append(", vdcfpe.data_alteracao as dataAlteracao ")
			.append(", vdcfpe.nome_usuario as nomeUsuario ") 
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ") 
		    .append("where 1 = 1 ")
		    .append("and vdcfpe.fornecedor_id = :idFornecedor ")
		    .append("and vdcfpe.cota_id = :idCota ")
		    .append("and vdcfpe.produto_id = :idProduto ")
		    .append("and vdcfpe.produto_edicao_id = :idProdutoEdicao ");
		
		Query query = getSession().createSQLQuery(hql.toString());
		//TODO: Validar nulos
		query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		query.setParameter("idCota", cota.getId());
        query.setParameter("idProduto", produtoEdicao.getProduto().getId());
        query.setParameter("idProdutoEdicao", produtoEdicao.getId());
        
		return query;
	}
	
	private Query obterDescontoCotaProdutoExcessoes(Cota cota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append("vdcfpe.desconto_id as idTipoDesconto ")
			.append(", vdcfpe.codigo_produto as codigoProduto ")
			.append(", vdcfpe.nome_produto as nomeProduto ")
			.append(", vdcfpe.numero_edicao as numeroEdicao ")
			.append(", vdcfpe.valor as desconto ")
			.append(", vdcfpe.data_alteracao as dataAlteracao ")
			.append(", vdcfpe.nome_usuario as nomeUsuario ") 
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ") 
		    .append("where 1 = 1 ")
		    .append("and vdcfpe.fornecedor_id = :idFornecedor ")
		    .append("and vdcfpe.cota_id = :idCota ")
		    .append("and vdcfpe.produto_id = :idProduto ");
		
		Query query = getSession().createSQLQuery(hql.toString());
		//TODO: Validar nulos
		query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		query.setParameter("idCota", cota.getId());
        query.setParameter("idProduto", produtoEdicao.getProduto().getId());
        
		return query;
	}
	
	private Query obterDescontoCotaExcessoes(Cota cota, ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append("vdcfpe.desconto_id as idTipoDesconto ")
			.append(", vdcfpe.codigo_produto as codigoProduto ")
			.append(", vdcfpe.nome_produto as nomeProduto ")
			.append(", vdcfpe.numero_edicao as numeroEdicao ")
			.append(", vdcfpe.valor as desconto ")
			.append(", vdcfpe.data_alteracao as dataAlteracao ")
			.append(", vdcfpe.nome_usuario as nomeUsuario ") 
		    .append("from VIEW_DESCONTO_COTA_FORNECEDOR_PRODUTOS_EDICOES as vdcfpe ") 
		    .append("where 1 = 1 ")
		    .append("and vdcfpe.fornecedor_id = :idFornecedor ")
		    .append("and vdcfpe.cota_id = :idCota ");
		
		Query query = getSession().createSQLQuery(hql.toString());
		//TODO: Validar nulos
		query.setParameter("idFornecedor", produtoEdicao.getProduto().getFornecedor().getId());
		query.setParameter("idCota", cota.getId());

		return query;
	}
	
	private Query obterDescontoProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append("vdpe.desconto_id as idTipoDesconto ")
			.append(", vdpe.codigo as codigoProduto ")
			.append(", vdpe.nome_produto as nomeProduto ")
			.append(", vdpe.numero_edicao as numeroEdicao ")
			.append(", vdpe.valor as desconto ")
			.append(", vdpe.data_alteracao as dataAlteracao ")
			.append(", vdpe.nome_usuario as nomeUsuario ") 
		    .append("from VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ") 
		    .append("where 1 = 1 ")
		    .append("and vdpe.codigo = :codigoProduto ")
		    .append("and vdpe.numero_edicao = :numeroEdicao ");
		
		//TODO: Validar nulos
		Query query = getSession().createSQLQuery(hql.toString());
		query.setParameter("codigoProduto", produtoEdicao.getProduto().getCodigo());
		query.setParameter("numeroEdicao", produtoEdicao.getNumeroEdicao());

		return query;
	}
	
	private Query obterDescontoProduto(ProdutoEdicao produtoEdicao) {
		
		StringBuilder hql = new StringBuilder("select ")
			.append("vdpe.desconto_id as idTipoDesconto ")
			.append(", vdpe.codigo as codigoProduto ")
			.append(", vdpe.nome_produto as nomeProduto ")
			.append(", vdpe.numero_edicao as numeroEdicao ")
			.append(", vdpe.valor as desconto ")
			.append(", vdpe.data_alteracao as dataAlteracao ")
			.append(", vdpe.nome_usuario as nomeUsuario ") 
		    .append("from VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ") 
		    .append("where 1 = 1 ")
		    .append("and vdpe.codigo = :codigoProduto ")
		    .append("and vdpe.numero_edicao is null ");
		
		//TODO: Validar nulos
		Query query = getSession().createSQLQuery(hql.toString());
		query.setParameter("codigoProduto", produtoEdicao.getProduto().getCodigo());

		return query;
	}

	@Override
	public void salvarListaDescontoProdutoEdicao(List<DescontoProdutoEdicao> lista) {
		
		int i = 0;
		
		for (DescontoProdutoEdicao descontoProdutoEdicao : lista) {
			this.merge(descontoProdutoEdicao);
			i++;
			if (i % QUINHENTOS == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
		
		getSession().flush();
		getSession().clear();
		
	}

}
