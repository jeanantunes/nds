package br.com.abril.nds.integracao.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.IcdObject;
import br.com.abril.nds.integracao.model.InterfaceExecucao;
import br.com.abril.nds.integracao.repository.Repository;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas(String distribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(String distribuidor);

	public List<SolicitacaoDTO> recuperaSolicitacoes(String distribuidor);
	
}
