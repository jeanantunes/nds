package br.com.abril.nds.integracao.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.repository.LogExecucaoRepository;
import br.com.abril.nds.model.integracao.LogExecucao;

@Repository
@Transactional("transactionManager")
public class LogExecucaoRepositoryImpl extends AbstractRepositoryModel<LogExecucao, Long>
		implements LogExecucaoRepository {
	
	
	public LogExecucaoRepositoryImpl() {
		super(LogExecucao.class);
	}

	public LogExecucao inserir(LogExecucao logExecucao) {
		getSession().persist(logExecucao);
		getSession().flush();
		return logExecucao;
	}
	
	public void atualizar(LogExecucao logExecucao) {
		getSession().merge(logExecucao);
		getSession().flush();
	}
}
