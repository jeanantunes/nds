package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.movimentacao.ControleNumeracaoSlip;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ControleNumeracaoSlipRepository;

@Repository
public class ControleNumeracaoSlipRepositoryImpl extends
		AbstractRepositoryModel<ControleNumeracaoSlip, Long> implements ControleNumeracaoSlipRepository {


	/**
	 * Construtor padrão.
	 */
	public ControleNumeracaoSlipRepositoryImpl() {
		super(ControleNumeracaoSlip.class);
	}

	@Override
	public ControleNumeracaoSlip obterControleNumeracaoSlip(TipoSlip tipoSlip) {
		
		String hql = "from ControleNumeracaoSlip controleNumeracaoSlip "				
				+ " where controleNumeracaoSlip.tipoSlip = :tipoSlip";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("tipoSlip", tipoSlip);

		return (ControleNumeracaoSlip) query.uniqueResult();
	}
	


}
