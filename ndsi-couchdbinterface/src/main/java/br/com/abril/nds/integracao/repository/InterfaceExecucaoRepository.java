package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.integracao.persistence.model.InterfaceExecucao;

public interface InterfaceExecucaoRepository extends Repository<InterfaceExecucao, Long> {

	InterfaceExecucao findById(Long codigoInterface);

}
