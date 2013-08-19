package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.LancamentoDiferencaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.estoque.LancamentoDiferenca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class LancamentoDiferencaRepositoryImpl extends AbstractRepositoryModel<LancamentoDiferenca, Long> implements LancamentoDiferencaRepository {

	public LancamentoDiferencaRepositoryImpl() {
		
		super(LancamentoDiferenca.class);
	}

	public LancamentoDiferenca obterLancamentoDiferencaEstoqueCota(Long idMovimentoEstoqueCota) {
        
		String hql = "from LancamentoDiferenca lancamentoDiferenca " +
        		"where lancamentoDiferenca.movimentoEstoqueCota.id = :idMovimentoEstoqueCota";
        
        Query query = getSession().createQuery(hql);
        
        query.setParameter("idMovimentoEstoqueCota", idMovimentoEstoqueCota);
        
        query.setMaxResults(1);
        
        return (LancamentoDiferenca) query.uniqueResult();
    }
	
	public LancamentoDiferenca obterLancamentoDiferencaEstoque(Long idMovimentoEstoque) {
        
		String hql = "from LancamentoDiferenca lancamentoDiferenca " +
        		"where lancamentoDiferenca.movimentoEstoque.id = :idMovimentoEstoque";
        
        Query query = getSession().createQuery(hql);
        
        query.setParameter("idMovimentoEstoque", idMovimentoEstoque);
        
        query.setMaxResults(1);
        
        return (LancamentoDiferenca) query.uniqueResult();
    }
}
