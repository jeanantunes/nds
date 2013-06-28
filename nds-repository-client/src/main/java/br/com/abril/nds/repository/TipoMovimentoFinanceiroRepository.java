package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public interface TipoMovimentoFinanceiroRepository extends Repository<TipoMovimentoFinanceiro, Long> {
	
	TipoMovimentoFinanceiro buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro grupoMovimentoFinanceiro);

	List<TipoMovimentoFinanceiro> buscarTiposMovimentoFinanceiro(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro);
	
	TipoMovimentoFinanceiro buscarPorDescricao(String string);

	List<Long> buscarIdsTiposMovimentoFinanceiro(
			List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro);

	List<TipoMovimentoFinanceiro> buscarTiposMovimentoFinanceiroPorOperacaoFinanceira(
			OperacaoFinaceira operacaoFinaceira, List<TipoMovimentoFinanceiro> movimentosIgnorar);
	
	List<Long> buscarIdsTiposMovimentoFinanceiroPorOperacaoFinanceira(
			OperacaoFinaceira operacaoFinaceira,
			List<TipoMovimentoFinanceiro> tiposIgnorar);

	/**
	 * Obtem tipo de movimento financeiro por GrupoMovimentoFinanceiro e OperacaoFinanceira
	 * @param grupo
	 * @param operacao
	 * @return TipoMovimentoFinanceiro
	 */
	TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(
			GrupoMovimentoFinaceiro grupo, OperacaoFinaceira operacao);
}