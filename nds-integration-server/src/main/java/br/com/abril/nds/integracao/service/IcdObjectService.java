package br.com.abril.nds.integracao.service;

import java.util.Set;

import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.icd.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.integracao.model.icd.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.icd.pks.DfsPK;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor);

	public SolicitacaoFaltaSobra recuperaSolicitacao(Long distribuidor, EMS0128Input doc);

	public void insereSolicitacao(EMS0128Input doc);

	public MotivoSituacaoFaltaSobra recuperaMotivoPorDetalhe(
			DfsPK dfsPK);
	
}
