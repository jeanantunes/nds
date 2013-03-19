package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;

public interface TipoMovimentoFinanceiroService {

	List<TipoMovimentoFinanceiro> obterTipoMovimentosFinanceirosCombo();
	
	TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorId(Long idTipoMovimentoFinanceiro);
	
}
