package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;




import br.com.abril.nds.dto.DeparaDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DeparaRepository;

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
	
	
	
	
}
