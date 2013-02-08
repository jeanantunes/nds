package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public interface TipoMovimentoFinanceiroRepository extends Repository<TipoMovimentoFinanceiro, Long> {
	
	TipoMovimentoFinanceiro buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro grupoMovimentoFinanceiro);

	List<TipoMovimentoFinanceiro> buscarTiposMovimentoFinanceiro(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro);
	
	TipoMovimentoFinanceiro buscarPorDescricao(String string);

	List<Long> buscarIdsTiposMovimentoFinanceiro(
			List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro);
}