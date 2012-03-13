package br.com.abril.nds.repository.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoletoRepositoryImpl extends AbstractRepository<Boleto,Long> implements BoletoRepository {

	
	/**
	 * Construtor padrão
	 */
	public BoletoRepositoryImpl() {
		super(Boleto.class);
	}
    
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosPorCota(Integer numeroCota, Date vencimentoDe, Date vencimentoAte, StatusCobranca status) {

		
		StringBuilder hql = new StringBuilder();;
		
		hql.append(" from Boleto b where ");
		hql.append(" b.cota.numeroCota = :ncota ");
		hql.append(" and b.dataVencimento >= :vctode ");
		hql.append(" and b.dataVencimento <= :vctoate ");
		if (status!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", numeroCota);
		query.setParameter("vctode", vencimentoDe);
		query.setParameter("vctoate", vencimentoAte);
		if (status!=null){	
		    query.setParameter("status", status);
		}
		return query.list();
		
	}
	
	public Boleto obterPorNossoNumero(String nossoNumero) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumero", nossoNumero));
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}

}
