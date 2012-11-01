package br.com.abril.nds.integracao.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.model.ParametroSistema;

public interface SolicitacaoFaltasSobrasRepository  extends Repository<SolicitacaoFaltaSobra, Long> {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Integer codigoDistribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Integer codigoDistribuidor);

	public List<SolicitacaoDTO> recuperaSolicitacoes(Integer codigoDistribuidor);

}
