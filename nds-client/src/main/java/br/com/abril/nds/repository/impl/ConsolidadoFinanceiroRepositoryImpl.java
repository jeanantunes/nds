package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends AbstractRepository<ConsolidadoFinanceiroCota,Long> implements ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl(){
		
		super(ConsolidadoFinanceiroCota.class);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer numeroCota){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select ");
		
		hql.append("    consolidadoFinanceiroCota.dataConsolidado as dataConsolidado, 		");
		hql.append(" 	consolidadoFinanceiroCota.valorPostergado as valorPostergado,		");
		hql.append(" 	consolidadoFinanceiroCota.numeroAtrasados as numeroAtradao,	    	");
		hql.append(" 	consolidadoFinanceiroCota.consignado as consignado,       	        ");
		hql.append("  	consolidadoFinanceiroCota.encalhe as encalhe,               		");
		hql.append("  	consolidadoFinanceiroCota.vendaEncalhe as vendaEncalhe,     		");
		hql.append("  	consolidadoFinanceiroCota.debitoCredito as debCred,	            	");
		hql.append(" 	consolidadoFinanceiroCota.encargos as encargos,             		");
		hql.append(" 	consolidadoFinanceiroCota.pendente as pendente, 		        	");		
		hql.append(" 	consolidadoFinanceiroCota.total as total            				");
			
		hql.append(" from ");

		hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");
		
		hql.append(" where ");
		
		hql.append(" consolidadoFinanceiroCota.cota.numeroCota = :numeroCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(ViewContaCorrenteCotaDTO.class);
		
		query.setResultTransformer(resultTransformer);

		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
	}
	
}
