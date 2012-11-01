package br.com.abril.nds.integracao.repository.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.ParametroSistema;
import br.com.abril.nds.integracao.repository.ParametroSistemaRepository;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;

@Repository
public class SolicitacaoFaltasSobrasRepositoryImpl extends AbstractRepositoryModel<SolicitacaoFaltaSobra, Long>
		implements SolicitacaoFaltasSobrasRepository {

	public SolicitacaoFaltasSobrasRepositoryImpl() {
		super(SolicitacaoFaltaSobra.class);
		// TODO Auto-generated constructor stub
	}

	public Set<Integer> recuperaSolicitacoesSolicitadas() {
         Criteria crit = getSession().createCriteria(SolicitacaoFaltaSobra.class);
         crit.setProjection(
        		 Projections.projectionList()
                 .add(Projections.groupProperty("codigoSolicitacao"))
                 .add(Projections.property("count"))
                 .add(Projections.count("codigoSolicitacao")));

		return (Set<Integer>) crit.list();
	
	}

	public Set<Integer> recuperaSolicitacoesAcertadas() {
        Criteria crit = getSession().createCriteria(DetalheFaltaSobra.class);
        crit.setProjection(
       		 Projections.projectionList()
                .add(Projections.groupProperty("codigoAcerto"))
                .add(Projections.property("count"))
                .add(Projections.count("codigoAcerto")));
		
		return (Set<Integer>) crit.list();
	
	}

	@Override
	public List<SolicitacaoDTO> recuperaSolicitacoes() {
		/*
		select s.cod_distribuidor, s.dat_solicitacao,
	       s.dat_solicitacao, s.hra_solicitacao, d.cod_situacao_acerto,
	       m.NUM_SEQUENCIA_MOTIVO, m.dsc_motivo_situacao, m.cod_origem_motivo   
	from solicitacao_faltas_sobras s, detalhe_faltas_sobras d,  motivo_situacao_faltas_sobras m
	where s.cod_distribuidor = d.cod_distribuidor
	and   s.dat_solicitacao = d.dat_solicitacao
	and   s.hra_solicitacao = d.hra_solicitacao
	and   s.cod_distribuidor = m.cod_distribuidor
	and   s.dat_solicitacao = m.dat_solicitacao
	and   s.hra_solicitacao = m.hra_solicitacao
	and   s.cod_distribuidor = 6248116
	--and   COD_SITUACAO_ACERTO = 'DESPREZADO'
	order by s.cod_distribuidor, s.dat_solicitacao
	*/
		return null;
	}	
	
}
