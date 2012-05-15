package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.repository.LancamentoParcialRepository;

@Repository
public class LancamentoParcialRepositoryImpl extends AbstractRepository<LancamentoParcial, Long> 
			implements LancamentoParcialRepository  {

	public LancamentoParcialRepositoryImpl() {
		super(LancamentoParcial.class);
	}

	@Override
	public LancamentoParcial obterLancamentoPorProdutoEdicao(
			Long idProdutoEdicao) {
		
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		Object lancamento = criteria.uniqueResult();
		
		return (lancamento!=null) ? (LancamentoParcial) lancamento : null ;	
	}

}
