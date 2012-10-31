package br.com.abril.nds.integracao.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.model.LogExecucaoArquivo;
import br.com.abril.nds.integracao.repository.LogExecucaoArquivoRepository;

@Repository
public class LogExecucaoArquivoRepositoryImpl extends AbstractRepositoryModel<LogExecucaoArquivo, Long>
		implements LogExecucaoArquivoRepository {
	

	
	public LogExecucaoArquivoRepositoryImpl() {
		super(LogExecucaoArquivo.class);

	}

	public LogExecucaoArquivo inserir(LogExecucaoArquivo logExecucaoArquivo) {
		getSession().persist(logExecucaoArquivo);
		getSession().flush();
		return logExecucaoArquivo;
	}
}
