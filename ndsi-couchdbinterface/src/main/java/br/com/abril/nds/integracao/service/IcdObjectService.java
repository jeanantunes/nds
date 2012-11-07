package br.com.abril.nds.integracao.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor);

	public SolicitacaoFaltaSobra recuperaSolicitacao(Long distribuidor, EMS0128Input doc);

	public void insereSolicitacao(EMS0128Input doc);
	
}
