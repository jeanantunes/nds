package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;

import br.com.abril.nds.dto.ConsolidadoFinanceiroCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;

public class ConsolidadoFinanceiroRepositoryImpl extends AbstractRepository<ConsolidadoFinanceiroCota,Long> implements ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl(){
		
		super(ConsolidadoFinanceiroCota.class);
		
	}
	
	public List<ConsolidadoFinanceiroCota> buscarListaDeConsolidado(Long idCota){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select new ");
		
		
		hql.append(ConsolidadoFinanceiroCotaDTO.class.getCanonicalName());
		
		hql.append(" ( 	consolidadoFinanceiroCota.dataConsignado, 								");
		hql.append(" 	consolidadoFinanceiroCota.numeroAtrasado, 						");
		hql.append(" 	consolidadoFinanceiroCota.consignado,       	");
		hql.append("  	consolidadoFinanceiroCota.encalhe,      		");
		hql.append("  	consolidadoFinanceiroCota.vendaEncalhe, 		");
		hql.append("  	consolidadoFinanceiroCota.debCred, 				");
		hql.append(" 	consolidadoFinanceiroCota.encargos,     		");
		hql.append(" 	consolidadoFinanceiroCota.pendente, 			");		
		hql.append(" 	consolidadoFinanceiroCota.total) 				");
			
		hql.append(" from ");

		hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");
		
		hql.append(" where ");
		
		hql.append(" consolidadoFinanceiroCota.id = :idCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return query.list();
	}
	
}
