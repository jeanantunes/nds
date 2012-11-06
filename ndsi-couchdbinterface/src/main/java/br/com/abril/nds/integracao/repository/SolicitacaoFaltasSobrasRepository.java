package br.com.abril.nds.integracao.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.ParametroSistema;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;

public interface SolicitacaoFaltasSobrasRepository  extends Repository<SolicitacaoFaltaSobra, Long> {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long codigoDistribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Long codigoDistribuidor);

	public List<SolicitacaoDTO> recuperaSolicitacoes(Long codigoDistribuidor, Date dataSolicitacao, String horaSolicitacao);

	void save(SolicitacaoFaltaSobra sfs);

}
