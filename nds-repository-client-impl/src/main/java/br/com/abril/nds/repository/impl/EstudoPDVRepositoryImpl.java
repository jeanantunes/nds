package br.com.abril.nds.repository.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoPDVRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 27/05/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EstudoPDVRepositoryImpl extends AbstractRepositoryModel<EstudoPDV, Long> implements EstudoPDVRepository {

    public EstudoPDVRepositoryImpl() {
        super(EstudoPDV.class);
    }

    @Override
    public EstudoPDV buscarPorEstudoCotaPDV(EstudoGerado estudo, Cota cota, PDV pdv) {
        return (EstudoPDV) getSession()
                .createCriteria(EstudoPDV.class)
                .add(Restrictions.eq("estudo", estudo))
                .add(Restrictions.eq("cota", cota))
                .add(Restrictions.eq("pdv", pdv))
                .uniqueResult();
    }
}
