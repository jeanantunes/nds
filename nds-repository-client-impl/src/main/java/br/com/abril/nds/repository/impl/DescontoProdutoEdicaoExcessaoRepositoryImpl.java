package br.com.abril.nds.repository.impl;

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
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoProdutoEdicaoExcessaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoExcessaoRepositoryImpl extends AbstractRepositoryModel<DescontoCotaProdutoExcessao, Long> implements DescontoProdutoEdicaoExcessaoRepository {
 
	private static final int QUINHENTOS = 500;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoProdutoEdicaoExcessaoRepositoryImpl() {
		
		super(DescontoCotaProdutoExcessao.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescontoCotaProdutoExcessao buscarDescontoCotaProdutoExcessao(
			TipoDesconto tipoDesconto,
			Desconto desconto,
			Fornecedor fornecedor,
			Cota cota, 
			Long idProduto, 
			Long idProdutoEdicao) {
		
		boolean indWhere = false;
		StringBuilder hql = new StringBuilder("select d ");
		hql.append(" from DescontoCotaProdutoExcessao d ");
		
		if (fornecedor != null) {
			
			hql.append(" where d.fornecedor = :fornecedor ");
			indWhere = true;
		}

		if (cota != null) {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.cota = :cota ");
			indWhere = true;
		}

		if (idProdutoEdicao != null) {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.produtoEdicao.id = :idProdutoEdicao ");
			indWhere = true;
		} else {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.produtoEdicao is null ");
			indWhere = true;
			
		}
	
		if (idProduto != null) {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.produto.id = :idProduto ");
			indWhere = true;
		}		
		
		if (tipoDesconto != null) {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.tipoDesconto = :tipoDesconto ");
			indWhere = true;
		}
		
		if (desconto != null) {
			
			hql.append(indWhere ? " and " : " where ")
			   .append(" d.desconto = :desconto ");
			indWhere = true;
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (fornecedor != null) {
			
			query.setParameter("fornecedor", fornecedor);
		}

		if (cota != null) {
			
			query.setParameter("cota", cota);
		}

		if (idProdutoEdicao != null) {
			
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		if (tipoDesconto != null) {
			
			query.setParameter("tipoDesconto", tipoDesconto);
		}
		
		if (desconto != null) {
			
			query.setParameter("desconto", desconto);
		}
		
		if (idProduto != null) {
			
			query.setParameter("idProduto", idProduto);
		}
		
		query.setMaxResults(1);
		
		return (DescontoCotaProdutoExcessao) query.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, null, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, null, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, null, null, produtoEdicao,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, cota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, cota, null,tipoDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(desconto, fornecedor, cota, produtoEdicao,tipoDesconto);
	}
	
	@SuppressWarnings("unchecked")
	private Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoCotaFornecedor(Desconto desconto, Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Criteria criteria = getSession().createCriteria(DescontoCotaProdutoExcessao.class);
		
		
		if(desconto != null) {
			
			criteria.add(Restrictions.eq("desconto", desconto));
			
		}
		
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
		
		return new HashSet<DescontoCotaProdutoExcessao>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	private List<DescontoCotaProdutoExcessao> obterDescontoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota){
		
		Criteria criteria = getSession().createCriteria(DescontoCotaProdutoExcessao.class);
		
		criteria.add(Restrictions.eq("fornecedor", fornecedor));

		if (cota!= null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		criteria.add(Restrictions.not(Restrictions.eq("tipoDesconto", tipoDesconto)));
	
		return criteria.list();
	}

	@Override
	public void salvarListaDescontoProdutoEdicaoExcessao(List<DescontoCotaProdutoExcessao> lista) {
		
		int i = 0;
		
		for (DescontoCotaProdutoExcessao DescontoProdutoEdicaoExcessao : lista) {
			this.merge(DescontoProdutoEdicaoExcessao);
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
