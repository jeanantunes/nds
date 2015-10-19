package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;







import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DeparaRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class DeparaRepositoryImpl extends AbstractRepositoryModel<Depara, Long> implements DeparaRepository {
	
	public DeparaRepositoryImpl( ) {
		super(Depara.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeparaDTO> buscarDepara() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT");
		hql.append(" depara.id as id,");
		hql.append(" depara.fc as fc, ");
		hql.append(" depara.dinap as dinap ");
	
		hql.append(" FROM Depara as depara ");
		hql.append(" order by depara.id");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				DeparaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String obterBoxDinap(String boxfc){
		Query query =  getSession().createQuery("select distinct depara.dinap as dinap from Depara depara where depara.fc = :boxfc order by depara.id asc");
		query.setParameter("boxfc",boxfc.replaceAll("^0+","")); 
		String boxdinap =(String) query.uniqueResult();
		if ( boxdinap == null || boxdinap.trim().length() == 0 )
		  return null;
		return ("00000".substring(boxdinap.length()) + boxdinap);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Depara> busca(String fc, String dinap, String  orderBy , Ordenacao ordenacao, Integer initialResult, Integer maxResults) {
		
		Criteria criteria = addRestrictions(fc,dinap);	
		if(Ordenacao.ASC ==  ordenacao) {
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		if(initialResult != null) {
			
			criteria.setFirstResult(initialResult);
			
		}
		
		if(maxResults != null) {
			
			criteria.setMaxResults(maxResults);
			
		}
		
		return criteria.list();
	}
	
	
	@Override
	public Long quantidade(String fc, String dinap ) {
		
		Criteria criteria = addRestrictions(fc,dinap);
		criteria.setProjection(Projections.rowCount());
		
		return (Long)criteria.list().get(0);
	}
	
	
	
private Criteria addRestrictions(String fc, String dinap) {
		
		Criteria criteria =  getSession().createCriteria(Depara.class);	
		
		if(fc != null ) {
			criteria.add(Restrictions.eq("fc", fc));
		}
		
		if(dinap != null ) {
			criteria.add(Restrictions.eq("dinap", dinap));
		}
		
		return criteria;
	}


@Override
public boolean hasFc(String fc){
	Criteria criteria =  getSession().createCriteria(Depara.class);	
	
	criteria.add(Restrictions.eq("fc", fc));
	
	
	criteria.setProjection(Projections.rowCount());
	Long qtd = (Long)criteria.list().get(0);
	return qtd > 0;
}
	
}
