package br.com.abril.nds.integracao.repository.impl;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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
		String sql = "select COD_SITUACAO_SOLICITACAO, count(*) from solicitacao_faltas_sobras group by COD_SITUACAO_SOLICITACAO";
		Query query = getSession().createQuery(sql);
		return (Set<Integer>) query.list();
	
	}
	

	public Set<Integer> recuperaSolicitacoesAcertadas() {
		String sql = "select cod_situacao_acerto, count(*) from detalhe_faltas_sobras group by cod_situacao_acerto";
		Query query = getSession().createQuery(sql);
		return (Set<Integer>) query.list();
	
	}	
	
}
