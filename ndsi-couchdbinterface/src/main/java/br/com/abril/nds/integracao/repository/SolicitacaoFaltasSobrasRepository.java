package br.com.abril.nds.integracao.repository;

import java.util.Set;

import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.ParametroSistema;

public interface SolicitacaoFaltasSobrasRepository  extends Repository<SolicitacaoFaltaSobra, Long> {

	public Set<Integer> recuperaSolicitacoesSolicitadas();
	
	public Set<Integer> recuperaSolicitacoesAcertadas();

}
