package br.com.abril.nds.integracao.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Integer distribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Integer distribuidor);

	public List<SolicitacaoDTO> recuperaSolicitacoes(Integer distribuidor);
	
}
