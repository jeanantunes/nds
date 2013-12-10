package br.com.abril.nds.integracao.repository.impl;

import java.util.Date;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.model.integracao.icd.DetalheFaltaSobra;
import br.com.abril.nds.model.integracao.icd.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.SolicitacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.pks.DfsPK;
import br.com.abril.nds.model.integracao.icd.pks.MfsPK;

@Repository
@Transactional("transactionManagerGfs")
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

		return (SolicitacaoFaltaSobra) getSessionGfs()
			.createCriteria(SolicitacaoFaltaSobra.class, "s")
			.createAlias("s.itens", "d")			
			.createAlias("d.motivoSituacaoFaltaSobra","m", JoinType.LEFT_OUTER_JOIN)
	        .add(
	        	Restrictions.and(
	        		Restrictions.eq("s.sfsPK.codigoDistribuidor", codigoDistribuidor )
	        	)
	        	.add(Restrictions.eq("s.sfsPK.dataSolicitacao", dataSolicitacao ))
	        	.add(Restrictions.eq("s.sfsPK.horaSolicitacao", horaSolicitacao ))
	        	.add(Restrictions.in("s.codigoSituacao", 
	        			new String[] {"EM PROCESSAMENTO", "PROCESSADO"} 
	        	))	        	
	        )
	        .uniqueResult();     		
	}	
	
	@Override
	public void save(SolicitacaoFaltaSobra sfs) {
		this.getSessionGfs().persist(sfs);		
	}

	@Override
	public MotivoSituacaoFaltaSobra recuperaMotivoPorDetalhe(
			DfsPK pkItem) {
		
		MfsPK id = new MfsPK();
		id.setCodigoDistribuidor(pkItem.getCodigoDistribuidor());
		id.setDataSolicitacao(pkItem.getDataSolicitacao());
		id.setHoraSolicitacao(pkItem.getHoraSolicitacao());
		id.setNumeroSequencia(pkItem.getNumeroSequencia());
		return (MotivoSituacaoFaltaSobra) getSessionGfs().get(MotivoSituacaoFaltaSobra.class, id);		
	}
	
}
