package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoLogisticaRepository;

@Repository
public class DescontoLogisticaRepositoryImpl extends AbstractRepositoryModel<DescontoLogistica, Long> implements DescontoLogisticaRepository {

	public DescontoLogisticaRepositoryImpl() {
		super(DescontoLogistica.class);
	}
	
	@Override
	public DescontoLogistica obterDescontoLogistica(Integer tipoDesconto, Long idFornecedor, Date dataVigencia, BigDecimal percentualDesconto) {
        
	    Criteria criteria = getSession().createCriteria(DescontoLogistica.class);
		
		criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		criteria.add(Restrictions.eq("fornecedor.id", idFornecedor));
		criteria.add(Restrictions.eq("dataInicioVigencia", dataVigencia));
		
		if (percentualDesconto != null) {
		    
		    criteria.add(Restrictions.eq("percentualDesconto", percentualDesconto));
		}
		
		criteria.setMaxResults(1);
		
		return (DescontoLogistica) criteria.uniqueResult();
	}
	
	@Override
    public DescontoLogistica obterDescontoLogisticaVigente(Integer tipoDesconto, Long idFornecedor, Date dataVigencia) {
        
	    Criteria criteria = getSession().createCriteria(DescontoLogistica.class);
        
        criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
        criteria.add(Restrictions.eq("fornecedor.id", idFornecedor));
        criteria.add(Restrictions.le("dataInicioVigencia", dataVigencia));
        
        criteria.addOrder(Order.desc("dataInicioVigencia"));
        
        criteria.setMaxResults(1);
                
        return (DescontoLogistica) criteria.uniqueResult();
    }

}