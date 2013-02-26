package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.model.integracao.LogExecucaoArquivo;

public interface LogExecucaoArquivoRepository extends Repository<LogExecucaoArquivo, Long> {

	LogExecucaoArquivo inserir(LogExecucaoArquivo logExecucaoArquivo);

}
