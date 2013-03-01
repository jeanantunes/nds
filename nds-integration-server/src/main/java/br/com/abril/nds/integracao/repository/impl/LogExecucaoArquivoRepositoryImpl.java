package br.com.abril.nds.integracao.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.repository.LogExecucaoArquivoRepository;
import br.com.abril.nds.model.integracao.LogExecucaoArquivo;

@Repository
@Transactional("transactionManager")
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
