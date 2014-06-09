package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.model.integracao.InterfaceExecucao;

public interface InterfaceExecucaoRepository extends Repository<InterfaceExecucao, Long> {

	InterfaceExecucao findById(Long codigoInterface);

}
