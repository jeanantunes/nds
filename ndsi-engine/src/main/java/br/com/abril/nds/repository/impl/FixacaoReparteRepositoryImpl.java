package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.repository.FixacaoReparteRepository;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * FixacaoReparte
 */

@Repository
public class FixacaoReparteRepositoryImpl extends  AbstractRepositoryModel<FixacaoReparte, Long> implements FixacaoReparteRepository {
 
	public FixacaoReparteRepositoryImpl() {
		super(FixacaoReparte.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FixacaoReparte> obterFixacoesRepartePorProduto(Produto produto) {
		Criteria criteria = super.getSession().createCriteria(FixacaoReparte.class);

		if (produto != null) {
			criteria.add(Restrictions.eq("produtoFixado", produto));
		}
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FixacaoReparte> obterFixacoesRepartePorCota(Cota cota) {

		Criteria criteria = super.getSession().createCriteria(FixacaoReparte.class);

		if (cota != null) {
			criteria.add(Restrictions.eq("cotaFixada", cota));
		}
		
		return criteria.list();
	}
	
	
	
	
}
