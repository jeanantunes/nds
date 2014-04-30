package br.com.abril.nds.repository.impl;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoFechamentoFiscalRepository;

@Repository
public class MovimentoFechamentoFiscalRepositoryImpl extends AbstractRepositoryModel<MovimentoFechamentoFiscal, Long> implements MovimentoFechamentoFiscalRepository {
    
    @Autowired
    private DataSource dataSource;
    
    public MovimentoFechamentoFiscalRepositoryImpl() {
        super(MovimentoFechamentoFiscal.class);
    }

	@Override
	public MovimentoFechamentoFiscalCota buscarPorChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		
		Criteria criteria = getSession().createCriteria(MovimentoFechamentoFiscalCota.class);
		criteria.add(Restrictions.eq("chamadaEncalheCota", chamadaEncalheCota));
		
		return (MovimentoFechamentoFiscalCota) criteria.uniqueResult();
	}
    
}