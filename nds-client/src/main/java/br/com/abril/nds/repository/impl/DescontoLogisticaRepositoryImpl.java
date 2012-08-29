package br.com.abril.nds.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.repository.DescontoLogisticaRepository;

@Repository
public class DescontoLogisticaRepositoryImpl extends AbstractRepositoryModel<DescontoLogistica, Long> implements DescontoLogisticaRepository {

	public DescontoLogisticaRepositoryImpl() {
		super(DescontoLogistica.class);
	}

	/**
	 * Obtem Desconto Logistica por tipoDesconto
	 * @param tipoDesconto
	 * @return DescontoLogistica
	 */
	@Override
	public DescontoLogistica obterPorTipoDesconto(Integer tipoDesconto) {
        Criteria criteria = getSession().createCriteria(DescontoLogistica.class);
		
		criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		return (DescontoLogistica) criteria.uniqueResult();
	}

}
