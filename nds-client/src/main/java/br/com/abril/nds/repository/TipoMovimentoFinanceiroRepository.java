package br.com.abril.nds.repository;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public interface TipoMovimentoFinanceiroRepository extends Repository<TipoMovimentoFinanceiro, Long> {
	
	TipoMovimentoFinanceiro buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro grupoMovimentoFinanceiro);

	TipoMovimentoFinanceiro buscarPorDescricao(String string);

}
