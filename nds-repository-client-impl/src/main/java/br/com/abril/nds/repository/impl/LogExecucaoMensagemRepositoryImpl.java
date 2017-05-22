package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.LogExecucaoMensagemRepository;

@Repository
public class LogExecucaoMensagemRepositoryImpl extends AbstractRepositoryModel<LogExecucaoMensagem, Long>  implements LogExecucaoMensagemRepository {

	public LogExecucaoMensagemRepositoryImpl() {
		super(LogExecucaoMensagem.class);
	}

	@Override
	public LogExecucaoMensagem inserir(LogExecucaoMensagem logExecucaoMensagem) {
		getSession().persist(logExecucaoMensagem);
		getSession().flush();
		return logExecucaoMensagem;
	}
	
	

}
