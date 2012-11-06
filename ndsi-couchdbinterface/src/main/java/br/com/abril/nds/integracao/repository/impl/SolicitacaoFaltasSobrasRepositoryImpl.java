package br.com.abril.nds.integracao.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;

@Repository
@Transactional("transactionManagerIcd")
public class SolicitacaoFaltasSobrasRepositoryImpl extends AbstractRepositoryModel<SolicitacaoFaltaSobra, Long>
		implements SolicitacaoFaltasSobrasRepository {

	public SolicitacaoFaltasSobrasRepositoryImpl() {
		super(SolicitacaoFaltaSobra.class);
		// TODO Auto-generated constructor stub
	}

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long codigoDistribuidor) {
         Criteria crit = getSession().createCriteria(SolicitacaoFaltaSobra.class);
         crit.setProjection(
        		 Projections.projectionList()
                 .add(Projections.groupProperty("codigoSolicitacao"))
                 .add(Projections.property("count"))
                 .add(Projections.count("codigoSolicitacao")));

		return (Set<Integer>) crit.list();
	
	}

	public Set<Integer> recuperaSolicitacoesAcertadas(Long codigoDistribuidor) {
        Criteria crit = getSession().createCriteria(DetalheFaltaSobra.class);
        crit.setProjection(
       		 Projections.projectionList()
                .add(Projections.groupProperty("codigoAcerto"))
                .add(Projections.property("count"))
                .add(Projections.count("codigoAcerto")));
		
		return (Set<Integer>) crit.list();
	
	}

	@Override
	public SolicitacaoFaltaSobra recuperaSolicitacao(Long codigoDistribuidor, Date dataSolicitacao, String horaSolicitacao) {

		return (SolicitacaoFaltaSobra) getSessionIcd()
			.createCriteria(SolicitacaoFaltaSobra.class, "s")
			.createAlias("s.itens", "d")			
			.createAlias("d.motivoSituacaoFaltaSobra","m", JoinType.LEFT_OUTER_JOIN)
			/*
	        .setProjection(
	       		 Projections.projectionList()	       		 	
						.add(Projections.property("s.sfsPK.codigoDistribuidor"), "codigoDistribuidor")
						.add(Projections.property("s.sfsPK.dataSolicitacao"), "solicitacao")
						.add(Projections.property("d.codigoAcerto"), "codigoAcerto")
						.add(Projections.property("d.codigoSituacao"), "codigoSituacao")
						.add(Projections.property("m.numeroSequencia"), "numeroSequencia")
						.add(Projections.property("m.descricaoMotivo"), "descricaoMotivo")
						.add(Projections.property("m.codigoMotivo"), "codigoMotivo")
						)
	        .setResultTransformer(Transformers.aliasToBean(SolicitacaoDTO.class))
	        */
	        .add(
	        	Restrictions.and(
	        		Restrictions.eq("s.sfsPK.codigoDistribuidor", codigoDistribuidor )	
	        		, Restrictions.eq("s.sfsPK.dataSolicitacao", dataSolicitacao )
	        		, Restrictions.eq("s.sfsPK.horaSolicitacao", horaSolicitacao )
	        	).add(Restrictions.in("s.codigoSituacao", 
	        			new String[] {"EM PROCESSAMENTO", "PROCESSADO"} 
	        	))	        	
	        )
	        .uniqueResult();       		
	}	
	
	@Override
	public void save(SolicitacaoFaltaSobra sfs) {
		this.getSessionIcd().persist(sfs);		
	}
	
}
