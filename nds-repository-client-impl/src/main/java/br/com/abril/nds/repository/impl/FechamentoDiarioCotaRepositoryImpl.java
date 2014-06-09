package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota.TipoSituacaoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioCotaRepository;

@Repository
public class FechamentoDiarioCotaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioCota, Long> implements FechamentoDiarioCotaRepository {

	public FechamentoDiarioCotaRepositoryImpl() {
		super(FechamentoDiarioCota.class);
	}
	
	public List<CotaResumoDTO> obterCotas(Date dataFechamento, TipoSituacaoCota tipoSituacaoCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select c.nomeCota as nome, ")
			.append(" c.numeroCota as numero ")
			.append(" from FechamentoDiarioCota c ")
			.append(" where c.fechamentoDiarioConsolidadoCota.fechamentoDiario.dataFechamento=:dataFechamento ")
			.append(" and c.tipoSituacaoCota=:tipoSituacaoCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setParameter("tipoSituacaoCota", tipoSituacaoCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaResumoDTO.class));
		
		return query.list();
	}
}
