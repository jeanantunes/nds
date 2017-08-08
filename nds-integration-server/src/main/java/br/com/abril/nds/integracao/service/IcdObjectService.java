package br.com.abril.nds.integracao.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.model.integracao.icd.IcdEdicaoBaseEstrategia;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;
import br.com.abril.nds.model.integracao.icd.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.SolicitacaoFaltaSobra;
import br.com.abril.nds.model.integracao.icd.pks.DfsPK;

public interface IcdObjectService {

	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor);
	
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor);

	public SolicitacaoFaltaSobra recuperaSolicitacao(Long distribuidor, EMS0128Input doc);

	public void insereSolicitacao(EMS0128Input doc);

	public MotivoSituacaoFaltaSobra recuperaMotivoPorDetalhe(
			DfsPK dfsPK);

	public List<IcdEstrategia> obterEstrategias(Long codigoDistribuidor);

	public List<IcdEdicaoBaseEstrategia> obterEdicaoBaseEstrategia(Integer codigoPraca, BigInteger codigoLancamentoEdicao);
}
