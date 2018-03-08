package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.cadastro.AcessoNA;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AcessoNaRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class AcessoNaRepositoryImpl extends AbstractRepositoryModel<AcessoNA, Long> implements AcessoNaRepository {

    public AcessoNaRepositoryImpl() {
        super(AcessoNA.class);
    }

    @Override
    public AcessoNA obterPorIdCota(Long idCota) {

        Criteria criteria = getSession().createCriteria(AcessoNA.class);
        criteria.add(Restrictions.eq("cota.id", idCota));

        return (AcessoNA) criteria.uniqueResult();
    }

}