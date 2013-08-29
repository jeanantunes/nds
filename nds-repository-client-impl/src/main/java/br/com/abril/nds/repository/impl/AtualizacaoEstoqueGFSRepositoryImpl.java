package br.com.abril.nds.repository.impl;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.AtualizacaoEstoqueGFS;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AtualizacaoEstoqueGFSRepository;

@Repository
public class AtualizacaoEstoqueGFSRepositoryImpl 
	extends AbstractRepositoryModel<AtualizacaoEstoqueGFS, Long> 
		implements AtualizacaoEstoqueGFSRepository {

	
	public AtualizacaoEstoqueGFSRepositoryImpl() {
		
		super(AtualizacaoEstoqueGFS.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AtualizacaoEstoqueGFS> obterPorProdutoEdicao(Long idProdutoEdicao) {

		Validate.notNull(idProdutoEdicao, "ID do Produto Edição não pode ser nulo.");
		
		Criteria criteria =  getSession().createCriteria(AtualizacaoEstoqueGFS.class);	
		
		criteria.createAlias("movimentoEstoque", "movimentoEstoque");
		criteria.createAlias("movimentoEstoque.produtoEdicao", "produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		return criteria.list();
	}

}
