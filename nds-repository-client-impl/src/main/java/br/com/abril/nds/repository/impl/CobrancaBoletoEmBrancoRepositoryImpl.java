package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CobrancaBoletoEmBrancoRepository;

@Repository
public class CobrancaBoletoEmBrancoRepositoryImpl
    extends AbstractRepositoryModel<CobrancaBoletoEmBranco, Long>
    implements CobrancaBoletoEmBrancoRepository {

    public CobrancaBoletoEmBrancoRepositoryImpl() {
        super(CobrancaBoletoEmBranco.class);
    }

    @Override
    public CobrancaBoletoEmBranco obterPorNossoNumero(String nossoNumero) {
        
        final Query query = 
                this.getSession().createQuery(
                        "select c from CobrancaBoletoEmBranco c where c.nossoNumero = :nossoNumero");
        
        query.setParameter("nossoNumero", nossoNumero);
        
        query.setMaxResults(1);
        
        return (CobrancaBoletoEmBranco) query.uniqueResult();
    }
}
