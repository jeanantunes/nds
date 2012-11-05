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
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicaoExcessao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.DescontoProdutoEdicaoExcessaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoExcessaoRepositoryImpl extends AbstractRepositoryModel<DescontoProdutoEdicaoExcessao, Long> implements DescontoProdutoEdicaoExcessaoRepository {
 
	private static final int QUINHENTOS = 500;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoProdutoEdicaoExcessaoRepositoryImpl() {
		
		super(DescontoProdutoEdicaoExcessao.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescontoProdutoEdicaoExcessao buscarDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto,
															 Fornecedor fornecedor, 
															 Cota cota,
															 ProdutoEdicao produto) {
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicaoExcessao.class);

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
		
		return (DescontoProdutoEdicaoExcessao) criteria.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(fornecedor, null, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(fornecedor, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, null, produtoEdicao,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, cota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(fornecedor, cota, null,tipoDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(fornecedor, cota, produtoEdicao,tipoDesconto);
	}
	
	@SuppressWarnings("unchecked")
	private Set<DescontoProdutoEdicaoExcessao> obterDescontoProdutoEdicaoExcessaoCotaFornecedor(Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicaoExcessao.class);
		
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
		
		return new HashSet<DescontoProdutoEdicaoExcessao>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	private List<DescontoProdutoEdicaoExcessao> obterDescontoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota){
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicaoExcessao.class);
		
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
    public BigDecimal obterDescontoPorCotaProdutoEdicao(Long idCota,
            Long idProdutoEdicao, Long idFornecedor) {
        
        StringBuilder hql = new StringBuilder("select view.desconto ");
        hql.append("from ViewDesconto view ")
           .append("where view.cotaId = :idCota ")
           .append("and view.produtoEdicaoId = :idProdutoEdicao ")
           .append("and view.fornecedorId = :idFornecedor ");
        Query query = getSession().createQuery(hql.toString());
        query.setParameter("idCota", idCota);
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameter("idFornecedor", idFornecedor);
        
        return (BigDecimal) query.uniqueResult();
    }

	@Override
	public void salvarListaDescontoProdutoEdicaoExcessao(List<DescontoProdutoEdicaoExcessao> lista) {
		
		int i = 0;
		
		for (DescontoProdutoEdicaoExcessao DescontoProdutoEdicaoExcessao : lista) {
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
