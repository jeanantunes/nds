package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public interface TipoMovimentoFinanceiroService {

	List<TipoMovimentoFinanceiro> obterTipoMovimentosFinanceirosCombo(boolean flag);
	
	TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorId(Long idTipoMovimentoFinanceiro);
	
	/**
	 * Obtem tipo de movimento financeiro por GrupoMovimentoFinanceiro e OperacaoFinanceira
	 * @param grupo
	 * @param operacao
	 * @return TipoMovimentoFinanceiro
	 */
	TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(GrupoMovimentoFinaceiro grupo, OperacaoFinaceira operacao);

	TipoMovimentoFinanceiro buscarPorGrupoMovimento(GrupoMovimentoFinaceiro grupoMovimento);
}
