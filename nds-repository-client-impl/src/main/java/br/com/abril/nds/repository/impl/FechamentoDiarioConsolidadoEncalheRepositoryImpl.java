package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoEncalheRepository;

@Repository
public class FechamentoDiarioConsolidadoEncalheRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoEncalhe, Long> implements FechamentoDiarioConsolidadoEncalheRepository {
	
	public FechamentoDiarioConsolidadoEncalheRepositoryImpl() {
		super(FechamentoDiarioConsolidadoEncalhe.class);
	}
	
	public ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f.saldo  as saldo,  ")
			.append(" f.valorFaltaEM as totalFaltas,  ")
			.append(" f.valorSobraEM as totalSobras, ")
			.append(" f.valorFisico  as  totalFisico, ")
			.append(" f.valorJuramentado as totalJuramentado, ")
			.append(" f.valorLogico as totalLogico, ")
			.append(" f.valorVenda as venda  ")
			.append(" from FechamentoDiarioConsolidadoEncalhe f  ")
			.append(" where f.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ResumoEncalheFecharDiaDTO.class));
		
		return (ResumoEncalheFecharDiaDTO) query.uniqueResult();
	}
}
