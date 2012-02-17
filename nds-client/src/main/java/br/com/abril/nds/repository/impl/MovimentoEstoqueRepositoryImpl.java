package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;

@Repository
public class MovimentoEstoqueRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long> 
implements MovimentoEstoqueRepository {
	
	/**
	 * Construtor padrão.
	 */
	public MovimentoEstoqueRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<MovimentoEstoque> obterListaMovimentoEstoque() {

		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select m from MovimentoEstoque m ");
		
		Query q = getSession().createQuery(hql.toString());
		
		return q.list();
				
	}
	
	
}
