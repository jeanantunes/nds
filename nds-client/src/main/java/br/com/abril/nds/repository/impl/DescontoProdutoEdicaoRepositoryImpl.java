package br.com.abril.nds.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
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
	public DescontoProdutoEdicao buscarDescontoProdutoEdicao(Fornecedor fornecedor, 
															 Cota cota,
															 ProdutoEdicao produto) {
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		criteria.add(Restrictions.eq("fornecedor", fornecedor));

		criteria.add(Restrictions.eq("cota", cota));

		criteria.add(Restrictions.eq("produtoEdicao", produto));
		
		criteria.setMaxResults(1);
		
		return (DescontoProdutoEdicao) criteria.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, cota, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(fornecedor, cota, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoProdutoEdicao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoCotaFornecedor(null, null, produtoEdicao);
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
	
	@SuppressWarnings("unchecked")
	private Set<DescontoProdutoEdicao> obterDescontoProdutoEdicaoCotaFornecedor(Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao){
		
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

}
