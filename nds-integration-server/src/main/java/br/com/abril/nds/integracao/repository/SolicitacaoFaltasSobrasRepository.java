package br.com.abril.nds.integracao.repository;

import java.util.Date;
import java.util.Set;

import br.com.abril.nds.integracao.icd.model.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.pks.DfsPK;

public interface SolicitacaoFaltasSobrasRepository  extends Repository<SolicitacaoFaltaSobra, Long> {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long codigoDistribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Long codigoDistribuidor);

	public SolicitacaoFaltaSobra recuperaSolicitacao(Long codigoDistribuidor, Date dataSolicitacao, String horaSolicitacao);

	void save(SolicitacaoFaltaSobra sfs);

	public MotivoSituacaoFaltaSobra recuperaMotivoPorDetalhe(
			DfsPK pkItem);

}
