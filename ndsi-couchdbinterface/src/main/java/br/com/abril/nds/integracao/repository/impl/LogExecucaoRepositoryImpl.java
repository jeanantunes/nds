package br.com.abril.nds.integracao.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.model.LogExecucao;
import br.com.abril.nds.integracao.repository.LogExecucaoRepository;

@Repository
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
