package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.model.integracao.LogExecucao;

public interface LogExecucaoRepository  extends Repository<LogExecucao, Long>{

	LogExecucao inserir(LogExecucao logExecucao);

	void atualizar(LogExecucao logExecucao);

}
