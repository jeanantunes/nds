package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoSuplementar;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoSuplementarRepository;

@Repository
public class FechamentoDiarioConsolidadoSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioConsolidadoSuplementar, Long> implements FechamentoDiarioConsolidadoSuplementarRepository {
	
	public FechamentoDiarioConsolidadoSuplementarRepositoryImpl() {
		super(FechamentoDiarioConsolidadoSuplementar.class);
	}
	
	public ResumoSuplementarFecharDiaDTO obterResumoGeralSuplementar(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select COALESCE(f.valorEstoqueLogico, 0) as totalEstoqueLogico, ")
			.append(" COALESCE(f.valorSaldo, 0) as saldo, ")
			.append(" COALESCE(f.valorTransferencia, 0) as totalTransferencia, ")
			.append(" COALESCE(f.valorAlteracaoPreco, 0) as totalAlteracaoPreco, ")
			.append(" COALESCE(f.valorVendas, 0) as totalVenda ")
			.append(" from FechamentoDiarioConsolidadoSuplementar f  ")
			.append(" where f.fechamentoDiario.dataFechamento = :dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ResumoSuplementarFecharDiaDTO.class));
		
		return (ResumoSuplementarFecharDiaDTO) query.uniqueResult();
	}
}
