package br.com.abril.nds.repository.impl;

import java.util.List;

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
	
	@Override
	public void inserir(List<LogExecucaoMensagem> logs) {
		
		for(int logN = 0; logN < logs.size(); logN++){
			getSession().persist(logs.get(logN));
			
			if(logN % 50 == 0){
				getSession().flush();
				getSession().clear();
			}
		}
		
	}
	

}
