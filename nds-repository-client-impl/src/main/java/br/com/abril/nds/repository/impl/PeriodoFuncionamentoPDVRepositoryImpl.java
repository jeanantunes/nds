package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.PeriodoFuncionamentoPDVRepository;

@Repository
public class PeriodoFuncionamentoPDVRepositoryImpl extends AbstractRepositoryModel<PeriodoFuncionamentoPDV,Long> implements PeriodoFuncionamentoPDVRepository {
	
	public PeriodoFuncionamentoPDVRepositoryImpl() {
		super(PeriodoFuncionamentoPDV.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PeriodoFuncionamentoPDV> obterPeriodoFuncionamentoPDV(Long idPDV){
		
		Criteria criteria = getSession().createCriteria(PeriodoFuncionamentoPDV.class);
		
		criteria.add(Restrictions.eq("pdv.id", idPDV));
		
		return criteria.list();
	}
}
