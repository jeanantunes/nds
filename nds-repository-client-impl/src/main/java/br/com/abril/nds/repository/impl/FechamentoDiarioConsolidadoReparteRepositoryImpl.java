package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoReparte;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoReparteRepository;

@Repository
public class FechamentoDiarioConsolidadoReparteRepositoryImpl  extends 
		AbstractRepositoryModel<FechamentoDiarioConsolidadoReparte,Long> implements FechamentoDiarioConsolidadoReparteRepository{

	public FechamentoDiarioConsolidadoReparteRepositoryImpl() {
		super(FechamentoDiarioConsolidadoReparte.class);
	}

	public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f.valorReparte as totalReparte,") 
			.append(" f.valorSobras as totalSobras,")
			.append(" f.valorFaltas as totalFaltas,")
			.append(" f.valorTransferido as totalTransferencias,")
			.append(" f.valorADistribuir as totalDistribuir, ")
			.append(" f.valorSobraDistribuida as totalSobraDistribuicao,")
			.append(" f.valorDiferenca as totalDiferenca, ")
			.append(" f.valorDistribuido as totalDistribuido ")
			.append(" from FechamentoDiarioConsolidadoReparte f join f.fechamentoDiario fd ")
			.append(" where fd.dataFechamento =:dataFechamento ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", data);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(SumarizacaoReparteDTO.class));
		
		return (SumarizacaoReparteDTO) query.uniqueResult();
	}
}
