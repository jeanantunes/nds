package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoRepositoryImpl extends AbstractRepositoryModel<DescontoProdutoEdicao, Long> implements DescontoProdutoEdicaoRepository {
 
	public DescontoProdutoEdicaoRepositoryImpl() {
		super(DescontoProdutoEdicao.class);
	}
	
	@Override
	public DescontoProdutoEdicao buscarDescontoProdutoEdicao(Fornecedor fornecedor, Cota cota, ProdutoEdicao produto) {
		
		Criteria criteria = getSession().createCriteria(DescontoProdutoEdicao.class);
		
		criteria.add(Restrictions.eq("fornecedor",fornecedor));
		criteria.add(Restrictions.eq("cota",cota));
		criteria.add(Restrictions.eq("produtoEdicao",produto));
		
		criteria.setMaxResults(1);
		
		return (DescontoProdutoEdicao) criteria.uniqueResult();
	}

}
