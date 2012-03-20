package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsolidadoFinanceiroCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends AbstractRepository<ConsolidadoFinanceiroCota,Long> implements ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl(){
		
		super(ConsolidadoFinanceiroCota.class);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsolidadoFinanceiroCotaDTO> buscarListaDeConsolidado(Integer numeroCota){
		
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
		
		hql.append(" consolidadoFinanceiroCota.cota = :numeroCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
	}
	
}
