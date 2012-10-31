package br.com.abril.nds.integracao.service;

import java.util.Set;

import br.com.abril.nds.integracao.icd.model.IcdObject;
import br.com.abril.nds.integracao.model.InterfaceExecucao;
import br.com.abril.nds.integracao.repository.Repository;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas();
	
	public Set<Integer> recuperaSolicitacoesAcertadas();
	
}
