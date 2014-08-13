package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * FixacaoRepartePdv 
 */

@Repository
public class FixacaoRepartePdvRepositoryImpl extends  AbstractRepositoryModel<FixacaoRepartePdv, Long> implements FixacaoRepartePdvRepository {
 
	public FixacaoRepartePdvRepositoryImpl() {
		super(FixacaoRepartePdv.class);
	}
	
	
	public List<FixacaoRepartePdv> obterFixacaoRepartePdvPorFixacaoReparte(FixacaoReparte fixacaoReparte){
		StringBuilder hql = new StringBuilder("");
		hql.append(" from FixacaoRepartePdv frp where frp.fixacaoReparte = :fixacaoReparte");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("fixacaoReparte",  fixacaoReparte );
		return query.list();
		
	}
	
	public void removerFixacaoReparte(FixacaoReparte fixacaoReparte){
		StringBuilder hql = new StringBuilder("");
		hql.append("delete from FixacaoRepartePdv frp where frp.fixacaoReparte = :fixacaoReparte");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("fixacaoReparte",  fixacaoReparte );
		query.executeUpdate();
	}

    @Override
    public FixacaoRepartePdv obterPorFixacaoReparteEPdv(FixacaoReparte fixacaoReparte, PDV pdv) {
        return (FixacaoRepartePdv) getSession().createCriteria(FixacaoRepartePdv.class)
                .add(Restrictions.eq("fixacaoReparte", fixacaoReparte))
                .add(Restrictions.eq("pdv", pdv))
                .uniqueResult();
    }

    @Override
    public List<FixacaoRepartePdv> obterFixacaoRepartePdvPorFixacaoReparteId(Long id) {
        return getSession().createCriteria(FixacaoRepartePdv.class).add(Restrictions.eq("fixacaoReparte.id", id)).list();
    }

}
