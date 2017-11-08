package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.integracao.LogExecucaoMensagem;

public interface LogExecucaoMensagemRepository {
	
	LogExecucaoMensagem inserir(LogExecucaoMensagem logExecucaoMensagem );
	
	void inserir(List<LogExecucaoMensagem> logs);

}
