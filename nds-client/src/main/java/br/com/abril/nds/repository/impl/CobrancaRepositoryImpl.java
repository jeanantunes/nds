package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.Cobranca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class CobrancaRepositoryImpl  extends AbstractRepository<Cobranca, Integer> implements CobrancaRepository{
	
	/**
	 * Construtor padrão
	 */
	public CobrancaRepositoryImpl() {
		super(Cobranca.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cobranca> obterCobrancaPorCota(Integer numeroCota){
		
		StringBuffer hql = new StringBuffer();
		
		
		hql.append(" from Cobranca cobranca");
		
		hql.append(" where ");
		
		hql.append(" cobranca.cota.numeroCota = :numeroCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(ViewContaCorrenteCotaDTO.class);
		
		query.setResultTransformer(resultTransformer);

		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
		
	}
	

}
