package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.InformacoesReparteComplementarEstudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.InformacoesReparteEstudoComplementarRepository;

@Repository
public class InformacoesReparteEstudoComplementarRepositoryImpl extends AbstractRepositoryModel<InformacoesReparteComplementarEstudo, Long> implements InformacoesReparteEstudoComplementarRepository { 
	
	public InformacoesReparteEstudoComplementarRepositoryImpl() {
		super(InformacoesReparteComplementarEstudo.class);
	}
	
	
	@Override
	public InformacoesReparteComplementarEstudo buscarInformacoesIdEstudo(Long idestudo){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		sql.append("    i.ID as id, ");
		sql.append("    i.ID_ESTUDO as idEstudo, ");
		sql.append("    i.TOTAL_REPARTE_A_DISTRIBUIR as totalReparteDistribuir, ");
		sql.append("    i.TOTAL_REPARTE_COMPLEMENTAR as totalReparteDistribuidoPorComplementar,  ");
		sql.append("    i.TOTAL_REPARTE_DISTRIBUIDO as totalReparteDistribuidoSemComplementar");
		sql.append(" FROM informacoes_reparte_complementar_estudo i  ");
		sql.append(" WHERE ID_ESTUDO = :estudoId ");
		
		Query query = super.getSession().createSQLQuery(sql.toString())
				.addScalar("id", StandardBasicTypes.LONG)
				.addScalar("idEstudo", StandardBasicTypes.LONG)
				.addScalar("totalReparteDistribuir", StandardBasicTypes.BIG_INTEGER)
				.addScalar("totalReparteDistribuidoPorComplementar", StandardBasicTypes.BIG_INTEGER)
				.addScalar("totalReparteDistribuidoSemComplementar", StandardBasicTypes.BIG_INTEGER);
		
		query.setParameter("estudoId", idestudo);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(InformacoesReparteComplementarEstudo.class));
		
		
		
		if(query.uniqueResult() != null){
			return (InformacoesReparteComplementarEstudo) query.uniqueResult();
		}else{
			return null;
		}
		
	}
	
}
