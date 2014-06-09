package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsolidadoDividaRepository;

@Repository
public class FechamentoDiarioResumoConsolidadoDividaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioResumoConsolidadoDivida, Long> implements FechamentoDiarioResumoConsolidadoDividaRepository {

	public FechamentoDiarioResumoConsolidadoDividaRepositoryImpl() {
		super(FechamentoDiarioResumoConsolidadoDivida.class);
	}
	
	public List<SumarizacaoDividasDTO> sumarizacaoDividas(Date dataFechamento, TipoDivida tipoDivida){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select d.tipoCobranca as tipoCobranca, ")
			.append(" d.valorInadimplencia as inadimplencia, ")
			.append(" d.valorPago as valorPago, ")
			.append(" d.valorTotal as total, ")
			.append(" cd.tipoDivida as tipoSumarizacao")
			.append(" from FechamentoDiarioResumoConsolidadoDivida d join d.fechamentoDiarioConsolidadoDivida cd ")
			.append(" where cd.fechamentoDiario.dataFechamento=:dataFechamento ")
			.append(" and cd.tipoDivida =:tipoDivida ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		query.setParameter("tipoDivida", tipoDivida);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(SumarizacaoDividasDTO.class));
		
		return query.list();
	}

}
